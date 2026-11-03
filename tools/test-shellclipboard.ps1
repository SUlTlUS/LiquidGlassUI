param([Parameter(Mandatory=$true)][string]$Path)
$ErrorActionPreference = "Stop"
Add-Type -AssemblyName System.Windows.Forms

Add-Type @"
using System;
using System.Runtime.InteropServices;
using System.Runtime.InteropServices.ComTypes;

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
            if (full == IntPtr.Zero) {
                throw new InvalidOperationException("ILCreateFromPathW failed.");
            }

            parent = ILClone(full);
            if (parent == IntPtr.Zero || !ILRemoveLastID(parent)) {
                throw new InvalidOperationException("Could not build parent PIDL.");
            }

            IntPtr child = ILFindLastID(full);
            if (child == IntPtr.Zero) {
                throw new InvalidOperationException("Could not build child PIDL.");
            }

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
"@

$p = (Resolve-Path -LiteralPath $Path).Path
[ShellClipboard]::CopyFile($p)
Start-Sleep -Milliseconds 200

$d = [System.Windows.Forms.Clipboard]::GetDataObject()
Write-Output ("formats=" + (($d.GetFormats()) -join ","))
Write-Output ("files=" + (([System.Windows.Forms.Clipboard]::GetFileDropList()) -join ";"))
