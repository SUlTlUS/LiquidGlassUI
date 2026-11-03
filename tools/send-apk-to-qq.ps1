param(
    [Parameter(Mandatory = $true)]
    [string]$ApkPath,

    [switch]$DryRun
)

$ErrorActionPreference = "Stop"

$resolvedApk = (Resolve-Path -LiteralPath $ApkPath).Path
if (-not (Test-Path -LiteralPath $resolvedApk -PathType Leaf)) {
    throw "APK not found: $resolvedApk"
}

Write-Host "[QQ APK] Triggered for: $resolvedApk"

if ($DryRun) {
    Write-Host "[QQ APK] Dry run: $resolvedApk -> current QQ conversation"
    exit 0
}

Add-Type -AssemblyName System.Windows.Forms
Add-Type -AssemblyName UIAutomationClient
Add-Type -AssemblyName UIAutomationTypes

Add-Type @"
using System;
using System.Runtime.InteropServices;

public static class ShellClipboard {
    [DllImport("shell32.dll", CharSet = CharSet.Unicode, SetLastError = true)]
    private static extern IntPtr ILCreateFromPathW(string pszPath);

    [DllImport("shell32.dll")]
    private static extern IntPtr ILClone(IntPtr pidl);

    [DllImport("shell32.dll")]
    [return: MarshalAs(UnmanagedType.Bool)]
    private static extern bool ILRemoveLastID(IntPtr pidl);

    [DllImport("shell32.dll")]
    private static extern IntPtr ILFindLastID(IntPtr pidl);

    [DllImport("shell32.dll")]
    private static extern void ILFree(IntPtr pidl);

    [DllImport("shell32.dll")]
    private static extern int SHCreateDataObject(
        IntPtr pidlFolder,
        uint cidl,
        [MarshalAs(UnmanagedType.LPArray, SizeParamIndex = 1)] IntPtr[] apidl,
        IntPtr pdtInner,
        ref Guid riid,
        out IntPtr ppdtobj
    );

    [DllImport("ole32.dll")]
    private static extern int OleInitialize(IntPtr pvReserved);

    [DllImport("ole32.dll")]
    private static extern void OleUninitialize();

    [DllImport("ole32.dll")]
    private static extern int OleSetClipboard(IntPtr pDataObj);

    [DllImport("ole32.dll")]
    private static extern int OleFlushClipboard();

    public static void CopyFile(string path) {
        int initHr = OleInitialize(IntPtr.Zero);
        bool uninitialize = initHr >= 0;
        IntPtr full = IntPtr.Zero;
        IntPtr parent = IntPtr.Zero;
        IntPtr data = IntPtr.Zero;

        try {
            full = ILCreateFromPathW(path);
            if (full == IntPtr.Zero) throw new InvalidOperationException("ILCreateFromPathW failed.");

            parent = ILClone(full);
            if (parent == IntPtr.Zero || !ILRemoveLastID(parent)) {
                throw new InvalidOperationException("Could not build parent PIDL.");
            }

            IntPtr child = ILFindLastID(full);
            if (child == IntPtr.Zero) throw new InvalidOperationException("Could not build child PIDL.");

            Guid iidDataObject = new Guid("0000010e-0000-0000-C000-000000000046");
            int hr = SHCreateDataObject(parent, 1, new IntPtr[] { child }, IntPtr.Zero, ref iidDataObject, out data);
            if (hr < 0) Marshal.ThrowExceptionForHR(hr);

            int lastHr = 0;
            for (int attempt = 0; attempt < 20; attempt++) {
                lastHr = OleSetClipboard(data);
                if (lastHr >= 0) break;
                System.Threading.Thread.Sleep(50);
            }
            if (lastHr < 0) Marshal.ThrowExceptionForHR(lastHr);

            for (int attempt = 0; attempt < 20; attempt++) {
                lastHr = OleFlushClipboard();
                if (lastHr >= 0) break;
                System.Threading.Thread.Sleep(50);
            }
            if (lastHr < 0) Marshal.ThrowExceptionForHR(lastHr);
        }
        finally {
            if (data != IntPtr.Zero) Marshal.Release(data);
            if (parent != IntPtr.Zero) ILFree(parent);
            if (full != IntPtr.Zero) ILFree(full);
            if (uninitialize) OleUninitialize();
        }
    }
}

public static class NativeWindow {
    [StructLayout(LayoutKind.Sequential)]
    public struct RECT {
        public int Left;
        public int Top;
        public int Right;
        public int Bottom;
    }

    [DllImport("user32.dll")]
    public static extern bool ShowWindowAsync(IntPtr hWnd, int nCmdShow);

    [DllImport("user32.dll")]
    public static extern bool SetForegroundWindow(IntPtr hWnd);

    [DllImport("user32.dll")]
    public static extern IntPtr GetForegroundWindow();

    [DllImport("user32.dll")]
    public static extern uint GetWindowThreadProcessId(IntPtr hWnd, out uint processId);

    [DllImport("kernel32.dll")]
    public static extern uint GetCurrentThreadId();

    [DllImport("user32.dll")]
    public static extern bool AttachThreadInput(uint idAttach, uint idAttachTo, bool attach);

    [DllImport("user32.dll")]
    public static extern bool BringWindowToTop(IntPtr hWnd);

    [DllImport("user32.dll")]
    public static extern IntPtr SetActiveWindow(IntPtr hWnd);

    [DllImport("user32.dll")]
    public static extern bool GetWindowRect(IntPtr hWnd, out RECT rect);

    [DllImport("user32.dll")]
    public static extern bool SetCursorPos(int x, int y);

    [DllImport("user32.dll", SetLastError = true)]
    public static extern bool SetWindowPos(
        IntPtr hWnd,
        IntPtr hWndInsertAfter,
        int X,
        int Y,
        int cx,
        int cy,
        uint uFlags
    );

    [DllImport("user32.dll")]
    public static extern void mouse_event(uint flags, uint dx, uint dy, uint data, UIntPtr extraInfo);
}
"@

function Set-WindowForeground {
    param([Parameter(Mandatory=$true)][IntPtr]$Handle)

    [void][NativeWindow]::ShowWindowAsync($Handle, 9)
    Start-Sleep -Milliseconds 100

    $foreground = [NativeWindow]::GetForegroundWindow()
    [uint32]$foregroundPid = 0
    [uint32]$targetPid = 0
    $foregroundThread = [NativeWindow]::GetWindowThreadProcessId($foreground, [ref]$foregroundPid)
    $targetThread = [NativeWindow]::GetWindowThreadProcessId($Handle, [ref]$targetPid)
    $currentThread = [NativeWindow]::GetCurrentThreadId()

    $attachedForeground = $false
    $attachedTarget = $false

    try {
        if ($foregroundThread -ne 0 -and $foregroundThread -ne $currentThread) {
            $attachedForeground = [NativeWindow]::AttachThreadInput($currentThread, $foregroundThread, $true)
        }
        if ($targetThread -ne 0 -and $targetThread -ne $currentThread) {
            $attachedTarget = [NativeWindow]::AttachThreadInput($currentThread, $targetThread, $true)
        }

        [void][NativeWindow]::BringWindowToTop($Handle)
        [void][NativeWindow]::SetActiveWindow($Handle)
        [void][NativeWindow]::SetForegroundWindow($Handle)
        Start-Sleep -Milliseconds 180
    }
    finally {
        if ($attachedTarget) {
            [void][NativeWindow]::AttachThreadInput($currentThread, $targetThread, $false)
        }
        if ($attachedForeground) {
            [void][NativeWindow]::AttachThreadInput($currentThread, $foregroundThread, $false)
        }
    }

    return [NativeWindow]::GetForegroundWindow() -eq $Handle
}

function Drag-ApkIntoQQ {
    param(
        [Parameter(Mandatory=$true)][string]$Path,
        [Parameter(Mandatory=$true)][IntPtr]$QqHandle,
        [Parameter(Mandatory=$true)]$QqRect,
        [Parameter(Mandatory=$true)][int]$EditorX,
        [Parameter(Mandatory=$true)][int]$EditorY
    )

    $dir = Split-Path -Parent $Path
    $name = Split-Path -Leaf $Path
    $shell = New-Object -ComObject Shell.Application
    $existingExplorerHandles = @{}
    foreach ($existingWindow in @($shell.Windows())) {
        try {
            if ($existingWindow.FullName -match "explorer.exe$") {
                $existingExplorerHandles[[int64]$existingWindow.HWND] = $true
            }
        }
        catch {}
    }

    Start-Process explorer.exe -ArgumentList ('/n,/select,"' + $Path + '"')

    $explorerWindow = $null
    for ($attempt = 0; $attempt -lt 20 -and $null -eq $explorerWindow; $attempt++) {
        Start-Sleep -Milliseconds 120
        foreach ($candidate in @($shell.Windows())) {
            try {
                if ($candidate.FullName -notmatch "explorer.exe$") { continue }
                $folderPath = $candidate.Document.Folder.Self.Path
                $selected = @($candidate.Document.SelectedItems()) | ForEach-Object { $_.Path }
                if ([string]::Equals($folderPath, $dir, [System.StringComparison]::OrdinalIgnoreCase) -and
                    $selected -contains $Path) {
                    $explorerWindow = $candidate
                    break
                }
            }
            catch {}
        }
    }

    if ($null -eq $explorerWindow) {
        Write-Warning "[QQ APK] Explorer drag fallback could not locate the selected APK."
        return $false
    }

    $explorerHandle = [IntPtr][int64]$explorerWindow.HWND
    $openedForSend = -not $existingExplorerHandles.ContainsKey([int64]$explorerWindow.HWND)

    try {
        # Keep the Explorer source window away from the QQ editor so the drop target stays visible.
        $virtual = [System.Windows.Forms.SystemInformation]::VirtualScreen
        $explorerWidth = 400
        $explorerHeight = [Math]::Min(700, [Math]::Max(420, $virtual.Height - 80))
        $leftSpace = $QqRect.Left - $virtual.Left
        $rightSpace = $virtual.Right - $QqRect.Right
        if ($leftSpace -ge ($explorerWidth + 20)) {
            $explorerX = $QqRect.Left - $explorerWidth - 10
            $explorerY = [Math]::Max($virtual.Top + 20, $QqRect.Top)
        }
        elseif ($rightSpace -ge ($explorerWidth + 20)) {
            $explorerX = $QqRect.Right + 10
            $explorerY = [Math]::Max($virtual.Top + 20, $QqRect.Top)
        }
        else {
            $explorerX = $virtual.Left + 10
            $explorerY = $virtual.Top + 10
            $explorerHeight = [Math]::Min(340, $virtual.Height - 20)
        }

        [void][NativeWindow]::SetWindowPos(
            $explorerHandle,
            [IntPtr]::Zero,
            $explorerX,
            $explorerY,
            $explorerWidth,
            $explorerHeight,
            0x0004
        )
        Start-Sleep -Milliseconds 250

        if (-not (Set-WindowForeground -Handle $explorerHandle)) {
            Write-Warning "[QQ APK] Could not focus Explorer for drag fallback."
            return $false
        }

        $root = [System.Windows.Automation.AutomationElement]::FromHandle($explorerHandle)
        $nameCondition = New-Object System.Windows.Automation.PropertyCondition(
            [System.Windows.Automation.AutomationElement]::NameProperty,
            $name
        )
        $typeCondition = New-Object System.Windows.Automation.PropertyCondition(
            [System.Windows.Automation.AutomationElement]::ControlTypeProperty,
            [System.Windows.Automation.ControlType]::ListItem
        )
        $condition = New-Object System.Windows.Automation.AndCondition($nameCondition, $typeCondition)
        $fileItem = $root.FindFirst([System.Windows.Automation.TreeScope]::Descendants, $condition)
        if ($null -eq $fileItem) {
            Write-Warning "[QQ APK] Explorer drag fallback could not locate the APK ListItem."
            return $false
        }

        $itemRect = $fileItem.Current.BoundingRectangle
        if ($itemRect.Width -lt 8 -or $itemRect.Height -lt 8) {
            Write-Warning "[QQ APK] Explorer returned an invalid APK item rectangle."
            return $false
        }

        $sourceX = [int]($itemRect.X + [Math]::Min(120.0, [Math]::Max(20.0, $itemRect.Width / 2.0)))
        $sourceY = [int]($itemRect.Y + ($itemRect.Height / 2.0))
        [void][NativeWindow]::SetCursorPos($sourceX, $sourceY)
        Start-Sleep -Milliseconds 100
        [NativeWindow]::mouse_event(0x0002, 0, 0, 0, [UIntPtr]::Zero)
        Start-Sleep -Milliseconds 120

        for ($step = 1; $step -le 18; $step++) {
            $fraction = $step / 18.0
            $x = [int]($sourceX + (($EditorX - $sourceX) * $fraction))
            $y = [int]($sourceY + (($EditorY - $sourceY) * $fraction))
            [void][NativeWindow]::SetCursorPos($x, $y)
            Start-Sleep -Milliseconds 22
        }

        Start-Sleep -Milliseconds 350
        [NativeWindow]::mouse_event(0x0004, 0, 0, 0, [UIntPtr]::Zero)
        Start-Sleep -Milliseconds 900
        Write-Host "[QQ APK] APK dropped into QQ editor via Explorer drag fallback."
        return $true
    }
    finally {
        # Ensure the mouse is never left held if any UI automation step throws.
        [NativeWindow]::mouse_event(0x0004, 0, 0, 0, [UIntPtr]::Zero)
        if ($openedForSend) {
            try { $explorerWindow.Quit() } catch {}
        }
    }
}

$qq = Get-Process -Name "QQ" -ErrorAction SilentlyContinue |
    Where-Object { $_.MainWindowHandle -ne 0 } |
    Select-Object -First 1

if ($null -eq $qq) {
    throw "QQ desktop is not running, or its main window is unavailable."
}

$rect = New-Object NativeWindow+RECT
if (-not [NativeWindow]::GetWindowRect($qq.MainWindowHandle, [ref]$rect)) {
    throw "Could not read QQ window bounds."
}

$windowWidth = $rect.Right - $rect.Left
$windowHeight = $rect.Bottom - $rect.Top
if ($windowWidth -lt 700 -or $windowHeight -lt 500) {
    throw "QQ window is too small for reliable automation."
}

# The user keeps "My Phone" open in advance. Both transfer paths target the
# current chat editor and then share the same QQ confirmation click.
$editorX = $rect.Left + [int]($windowWidth * 0.68)
$editorY = $rect.Top + [int]($windowHeight * 0.84)
$confirmX = $rect.Left + [int]($windowWidth * 0.50)
$confirmY = $rect.Top + [int]($windowHeight * 0.695)

$clipboardBackup = $null
$usedClipboard = $false
try {
    try {
        $clipboardBackup = [System.Windows.Forms.Clipboard]::GetDataObject()
    }
    catch {
        $clipboardBackup = $null
    }

    # Prefer the original Shell file clipboard because it is fastest when the
    # desktop allows clipboard automation. If Windows refuses OpenClipboard,
    # fall back to a real Explorer drag-and-drop into the QQ editor.
    $clipboardReady = $false
    $lastClipboardError = $null
    for ($attempt = 1; $attempt -le 3; $attempt++) {
        try {
            [ShellClipboard]::CopyFile($resolvedApk)
            Start-Sleep -Milliseconds 120
            $copiedFiles = @([System.Windows.Forms.Clipboard]::GetFileDropList())
            if ($copiedFiles -contains $resolvedApk) {
                $clipboardReady = $true
                break
            }
            $lastClipboardError = "Clipboard did not expose the APK after copy."
        }
        catch {
            $lastClipboardError = $_.Exception.Message
        }
        Start-Sleep -Milliseconds 120
    }

    if ($clipboardReady) {
        $usedClipboard = $true
        Write-Host "[QQ APK] APK copied to clipboard. Pasting into QQ..."
        if (-not (Set-WindowForeground -Handle $qq.MainWindowHandle)) {
            throw "Could not bring QQ to the foreground after copying the APK."
        }

        [void][NativeWindow]::SetCursorPos($editorX, $editorY)
        Start-Sleep -Milliseconds 100
        [NativeWindow]::mouse_event(0x0002, 0, 0, 0, [UIntPtr]::Zero)
        [NativeWindow]::mouse_event(0x0004, 0, 0, 0, [UIntPtr]::Zero)
        Start-Sleep -Milliseconds 180

        if ([NativeWindow]::GetForegroundWindow() -ne $qq.MainWindowHandle) {
            throw "QQ lost foreground focus before paste."
        }

        [System.Windows.Forms.SendKeys]::SendWait("^v")
        Start-Sleep -Milliseconds 1500
    }
    else {
        Write-Warning "[QQ APK] Clipboard unavailable; switching to Explorer drag fallback. $lastClipboardError"
        $dragArgs = @{
            Path = $resolvedApk
            QqHandle = $qq.MainWindowHandle
            QqRect = $rect
            EditorX = $editorX
            EditorY = $editorY
        }
        $dragged = Drag-ApkIntoQQ @dragArgs
        if (-not $dragged) {
            throw "Both QQ APK transfer methods failed. Clipboard: $lastClipboardError"
        }
    }

    Start-Sleep -Milliseconds 500
    [void](Set-WindowForeground -Handle $qq.MainWindowHandle)
    Start-Sleep -Milliseconds 250

    # Pasting or dropping a file into QQ NT opens a centered confirmation dialog.
    # Click the blue "Send (1)" button in that dialog, not the main chat send button.
    [void][NativeWindow]::SetCursorPos($confirmX, $confirmY)
    Start-Sleep -Milliseconds 150
    [NativeWindow]::mouse_event(0x0002, 0, 0, 0, [UIntPtr]::Zero)
    [NativeWindow]::mouse_event(0x0004, 0, 0, 0, [UIntPtr]::Zero)
    Start-Sleep -Milliseconds 1000

    $method = if ($usedClipboard) { "clipboard" } else { "Explorer drag" }
    Write-Host "[QQ APK] Send sequence completed via $method`: $resolvedApk -> current QQ conversation"
}
finally {
    if ($usedClipboard -and $null -ne $clipboardBackup) {
        try {
            Start-Sleep -Milliseconds 200
            [System.Windows.Forms.Clipboard]::SetDataObject($clipboardBackup, $true)
        }
        catch {
            Write-Warning "Could not restore the previous clipboard contents."
        }
    }
}
