param(
    [string]$DbHost = "localhost",
    [int]$DbPort = 3306,
    [string]$DbUsername = "root",
    [Parameter(Mandatory = $true)][string]$DbPassword
)

$mysqlCandidatePaths = @(
    "C:\Program Files\MySQL\MySQL Server 8.0\bin",
    "C:\Program Files\MySQL\MySQL Server 8.4\bin",
    "C:\Program Files\MySQL\MySQL Workbench 8.0 CE"
)
$mysqlBin = $mysqlCandidatePaths | Where-Object { Test-Path $_ } | Select-Object -First 1
if ($mysqlBin) {
    $env:Path = "$mysqlBin;$env:Path"
}

$mysqlCommand = Get-Command mysql -ErrorAction SilentlyContinue
if (-not $mysqlCommand) {
    throw "MySQL client not found in PATH. Install MySQL 8 and ensure 'mysql' is available."
}

$output = & $mysqlCommand.Source --host=$DbHost --port=$DbPort --user=$DbUsername --password=$DbPassword --execute="SELECT 1;" 2>&1
$exitCode = $LASTEXITCODE

if ($exitCode -ne 0) {
    Write-Error ($output | Out-String)
    throw "MySQL credentials are invalid or the server is unavailable."
}

Write-Host "MySQL connection verified for ${DbUsername}@${DbHost}:${DbPort}"
