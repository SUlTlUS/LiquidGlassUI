param([Parameter(Mandatory=$true)][string]$Path)

$ErrorActionPreference = "Stop"
Add-Type -AssemblyName System.Windows.Forms
Add-Type @"
using System;
using System.Runtime.InteropServices;
public static class NativeWin {
    [DllImport("user32.dll")]
    public static extern bool SetForegroundWindow(IntPtr hWnd);
}
"@

$p = (Resolve-Path -LiteralPath $Path).Path
$dir = Split-Path -Parent $p

$shell = New-Object -ComObject Shell.Application
$arg = '/select,"' + $p + '"'
Start-Process explorer.exe -ArgumentList $arg
Start-Sleep -Milliseconds 900

$win = $null
foreach ($w in @($shell.Windows())) {
    try {
        if ($w.FullName -notmatch "explorer.exe$") { continue }
        $folderPath = $w.Document.Folder.Self.Path
        $selected = @($w.Document.SelectedItems()) | ForEach-Object { $_.Path }
        Write-Output ("window=" + $folderPath + " hwnd=" + $w.HWND + " selected=" + ($selected -join ";"))
        if ([string]::Equals($folderPath, $dir, [System.StringComparison]::OrdinalIgnoreCase) -and
            $selected -contains $p) {
            $win = $w
            break
        }
    } catch {}
}

if ($null -eq $win) {
    throw "Explorer window for APK folder not found."
}

$focused = [NativeWin]::SetForegroundWindow([IntPtr][int64]$win.HWND)
Write-Output ("focused=" + $focused)
Start-Sleep -Milliseconds 250
[System.Windows.Forms.SendKeys]::SendWait("^c")
Start-Sleep -Milliseconds 500

$d = [System.Windows.Forms.Clipboard]::GetDataObject()
Write-Output ("formats=" + (($d.GetFormats()) -join ","))
Write-Output ("files=" + (([System.Windows.Forms.Clipboard]::GetFileDropList()) -join ";"))

$win.Quit()
