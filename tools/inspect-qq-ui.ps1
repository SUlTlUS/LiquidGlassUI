$ErrorActionPreference = "Stop"
Add-Type -AssemblyName UIAutomationClient
Add-Type -AssemblyName UIAutomationTypes

$qq = Get-Process -Name "QQ" -ErrorAction SilentlyContinue |
    Where-Object { $_.MainWindowHandle -ne 0 } |
    Select-Object -First 1

if ($null -eq $qq) { throw "QQ main window not found." }

$root = [System.Windows.Automation.AutomationElement]::FromHandle($qq.MainWindowHandle)
$walker = [System.Windows.Automation.TreeWalker]::RawViewWalker

function Walk($node, $depth) {
    if ($null -eq $node -or $depth -gt 4) { return }

    $p = $node.Current
    $indent = "  " * $depth
    $rect = $p.BoundingRectangle
    Write-Output ("{0}{1} | Name='{2}' | Id='{3}' | Class='{4}' | Rect={5},{6},{7},{8}" -f
        $indent,
        $p.ControlType.ProgrammaticName,
        $p.Name,
        $p.AutomationId,
        $p.ClassName,
        [int]$rect.X,
        [int]$rect.Y,
        [int]$rect.Width,
        [int]$rect.Height)

    $child = $walker.GetFirstChild($node)
    while ($null -ne $child) {
        Walk $child ($depth + 1)
        $child = $walker.GetNextSibling($child)
    }
}

Walk $root 0
