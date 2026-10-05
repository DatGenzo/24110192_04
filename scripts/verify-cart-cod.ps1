$ErrorActionPreference = "Stop"
Set-StrictMode -Version Latest

$project = Split-Path -Parent $PSScriptRoot
Set-Location $project

Write-Host "===== EX04 CART COD VERIFY =====" -ForegroundColor Cyan

$required = @(
    "database\upgrade-cart-cod-orders.sql",
    "database\test-order-status.sql",
    "src\main\java\com\thanhdat\exam04\controllers\CartController_24110192.java",
    "src\main\java\com\thanhdat\exam04\controllers\CheckoutController_24110192.java",
    "src\main\java\com\thanhdat\exam04\controllers\OrderController_24110192.java",
    "src\main\java\com\thanhdat\exam04\filters\UserAuthorizationFilter_24110192.java",
    "src\main\java\com\thanhdat\exam04\repositories\impl\CartRepositoryImpl_24110192.java",
    "src\main\java\com\thanhdat\exam04\repositories\impl\OrderRepositoryImpl_24110192.java",
    "src\main\webapp\WEB-INF\views\cart\view.jsp",
    "src\main\webapp\WEB-INF\views\checkout\form.jsp",
    "src\main\webapp\WEB-INF\views\orders\list.jsp",
    "src\main\webapp\WEB-INF\views\orders\detail.jsp"
)

foreach ($relativePath in $required) {
    if (-not (Test-Path (Join-Path $project $relativePath))) {
        throw "Missing required file: $relativePath"
    }
}

Write-Host "Required files: PASS" -ForegroundColor Green

$badJavaNames = Get-ChildItem `
    (Join-Path $project "src\main\java") `
    -Recurse `
    -Filter "*.java" |
    Where-Object {
        $_.BaseName -notmatch "_24110192$"
    }

if ($badJavaNames) {
    $badJavaNames | ForEach-Object { Write-Host $_.FullName }
    throw "Java naming rule failed"
}

Write-Host "Java naming suffix: PASS" -ForegroundColor Green

$bomFiles = New-Object System.Collections.Generic.List[string]
Get-ChildItem (Join-Path $project "src") -Recurse -File |
    ForEach-Object {
        $bytes = [System.IO.File]::ReadAllBytes($_.FullName)
        if (
            $bytes.Length -ge 3 -and
            $bytes[0] -eq 0xEF -and
            $bytes[1] -eq 0xBB -and
            $bytes[2] -eq 0xBF
        ) {
            $bomFiles.Add($_.FullName)
        }
    }

if ($bomFiles.Count -gt 0) {
    $bomFiles | ForEach-Object { Write-Host $_ }
    throw "UTF-8 BOM found"
}

Write-Host "UTF-8 BOM: NONE" -ForegroundColor Green

if (Get-Command mvn -ErrorAction SilentlyContinue) {
    mvn clean package
}
elseif (Test-Path (Join-Path $project "mvnw.cmd")) {
    & (Join-Path $project "mvnw.cmd") clean package
}
else {
    throw "Maven was not found"
}

if ($LASTEXITCODE -ne 0) {
    throw "Maven package failed"
}

$war = Join-Path $project "target\24110192_04.war"
if (-not (Test-Path $war)) {
    throw "WAR was not created"
}

Write-Host "Compile and package: PASS" -ForegroundColor Green
Write-Host "WAR: $war" -ForegroundColor Green
Write-Host "Run database\upgrade-cart-cod-orders.sql before UI test." -ForegroundColor Yellow
Write-Host "===== EX04 CART COD VERIFY PASS =====" -ForegroundColor Green
