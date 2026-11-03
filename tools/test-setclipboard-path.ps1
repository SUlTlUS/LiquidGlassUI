param([Parameter(Mandatory=$true)][string]$Path)
$ErrorActionPreference = "Stop"
Add-Type -AssemblyName System.Windows.Forms

$p = (Resolve-Path -LiteralPath $Path).Path
Set-Clipboard -LiteralPath $p
Start-Sleep -Milliseconds 200

$d = [System.Windows.Forms.Clipboard]::GetDataObject()
Write-Output ("formats=" + (($d.GetFormats()) -join ","))
Write-Output ("files=" + (([System.Windows.Forms.Clipboard]::GetFileDropList()) -join ";"))
