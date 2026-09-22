param(
    [string]$DbHost = "localhost",
    [int]$DbPort = 3306,
    [string]$DbUsername = "root",
    [string]$DbPassword = "",
    [Parameter(Mandatory = $true)][string]$JwtSecret
)

$ErrorActionPreference = "Stop"
$backend = Join-Path $PSScriptRoot "..\backend"

$mysqlCandidatePaths = @(
    "C:\Program Files\MySQL\MySQL Server 8.0\bin",
    "C:\Program Files\MySQL\MySQL Server 8.4\bin",
    "C:\Program Files\MySQL\MySQL Workbench 8.0 CE"
)
$mysqlBin = $mysqlCandidatePaths | Where-Object { Test-Path $_ } | Select-Object -First 1
if ($mysqlBin) {
    $env:Path = "$mysqlBin;$env:Path"
}

if ($JwtSecret.Length -lt 32) {
    throw "JwtSecret must be at least 32 characters for HS256."
}

$env:JWT_SECRET = $JwtSecret
$env:EUREKA_HOST = "localhost"
$env:EUREKA_PORT = "8761"

function Test-MySqlConnection {
    $mysqlCommand = Get-Command mysql -ErrorAction SilentlyContinue
    if (-not $mysqlCommand) {
        throw "MySQL client not found in PATH. Install MySQL 8 and ensure 'mysql' is available before starting TalentSync."
    }

    $arguments = @(
        "--host=$DbHost",
        "--port=$DbPort",
        "--user=$DbUsername",
        "--password=$DbPassword",
        "--execute=SELECT 1;"
    )

    try {
        $output = & $mysqlCommand.Source @arguments 2>&1
        $exitCode = $LASTEXITCODE
        if ($exitCode -ne 0) {
            $message = ($output | Out-String).Trim()
            throw "MySQL connection validation failed for user '${DbUsername}' on ${DbHost}:${DbPort}. Verify your MySQL server is running and the credentials are correct. Details: $message"
        }
    }
    catch {
        throw $_
    }
}

function Start-TalentSyncService([string]$name) {
    $pom = Join-Path $backend "$name\pom.xml"
    if (-not (Test-Path $pom)) { throw "Missing Maven module: $name" }
    Start-Process -FilePath "cmd.exe" -ArgumentList "/k", "cd /d `"$backend`" && mvnw.cmd -f `"$name\pom.xml`" spring-boot:run" -WorkingDirectory $backend
}

if ([string]::IsNullOrWhiteSpace($DbPassword)) {
    Remove-Item Env:DB_URL, Env:DB_USERNAME, Env:DB_PASSWORD, Env:DB_DRIVER_CLASS_NAME, Env:HIBERNATE_DIALECT -ErrorAction SilentlyContinue
    Write-Host "No DB password supplied; services will use their local H2 databases."
}
else {
    $env:DB_HOST = $DbHost
    $env:DB_PORT = $DbPort
    $env:DB_USERNAME = $DbUsername
    $env:DB_PASSWORD = $DbPassword
    Test-MySqlConnection
}

Start-TalentSyncService "eureka-server"
Write-Host "Waiting for Eureka to accept registrations..."
Start-Sleep -Seconds 12
"auth-service", "job-service", "recruitment-service", "application-service", "api-gateway" | ForEach-Object { Start-TalentSyncService $_; Start-Sleep -Seconds 4 }

Write-Host "Services were launched in separate windows. Refresh http://localhost:8761 after startup completes."
