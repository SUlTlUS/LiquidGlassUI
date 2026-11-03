$ErrorActionPreference = "SilentlyContinue"

Write-Output "--- QQ processes ---"
Get-CimInstance Win32_Process |
    Where-Object { $_.Name -eq "QQ.exe" } |
    Select-Object ProcessId, ExecutablePath, CommandLine |
    Format-List

Write-Output "--- Tencent Files ---"
$root = Join-Path $env:USERPROFILE "Documents\Tencent Files"
Get-ChildItem $root -Directory |
    Sort-Object LastWriteTime -Descending |
    Select-Object Name, LastWriteTime, FullName |
    Format-Table -AutoSize

Write-Output "--- AppData QQ-ish dirs ---"
Get-ChildItem $env:APPDATA -Directory |
    Where-Object { $_.Name -match "QQ|Tencent" } |
    Select-Object Name, LastWriteTime, FullName |
    Format-Table -AutoSize

Get-ChildItem $env:LOCALAPPDATA -Directory |
    Where-Object { $_.Name -match "QQ|Tencent" } |
    Select-Object Name, LastWriteTime, FullName |
    Format-Table -AutoSize
