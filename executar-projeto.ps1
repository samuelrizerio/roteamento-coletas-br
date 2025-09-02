Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  SISTEMA DE ROTEAMENTO PROGRAMADO DE COLETAS" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Este script irá executar o projeto Java" -ForegroundColor Yellow
Write-Host ""
Write-Host "Pressione qualquer tecla para continuar..." -ForegroundColor Green
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")

Write-Host ""
Write-Host "[1/3] Verificando Java..." -ForegroundColor Blue
try {
    $javaVersion = java -version 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "Java encontrado!" -ForegroundColor Green
        Write-Host $javaVersion[0] -ForegroundColor Gray
    } else {
        throw "Java não encontrado"
    }
} catch {
    Write-Host "❌ ERRO: Java não encontrado!" -ForegroundColor Red
    Write-Host "Por favor, instale o Java 17+ de: https://adoptium.net/" -ForegroundColor Yellow
    Read-Host "Pressione Enter para sair"
    exit 1
}

Write-Host ""
Write-Host "[2/3] Verificando Maven..." -ForegroundColor Blue
try {
    $mavenVersion = mvn -version 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "Maven encontrado!" -ForegroundColor Green
        Write-Host $mavenVersion[0] -ForegroundColor Gray
    } else {
        throw "Maven não encontrado"
    }
} catch {
    Write-Host "❌ ERRO: Maven não encontrado!" -ForegroundColor Red
    Write-Host "Por favor, instale o Maven de: https://maven.apache.org/download.cgi" -ForegroundColor Yellow
    Read-Host "Pressione Enter para sair"
    exit 1
}

Write-Host ""
Write-Host "[3/3] Iniciando o Backend Java..." -ForegroundColor Blue
Write-Host ""
Write-Host "O backend será iniciado em: http://localhost:8888" -ForegroundColor Green
Write-Host "Console H2: http://localhost:8888/h2-console" -ForegroundColor Green
Write-Host "Swagger UI: http://localhost:8888/api/v1/swagger-ui.html" -ForegroundColor Green
Write-Host ""
Write-Host "Para parar o servidor, pressione Ctrl+C" -ForegroundColor Yellow
Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

try {
    mvn spring-boot:run
} catch {
    Write-Host ""
    Write-Host "❌ Erro ao executar o projeto" -ForegroundColor Red
    Write-Host $_.Exception.Message -ForegroundColor Red
}

Write-Host ""
Write-Host "Servidor parado." -ForegroundColor Yellow
Read-Host "Pressione Enter para sair"
