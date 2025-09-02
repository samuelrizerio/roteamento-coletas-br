@echo off
echo ========================================
echo   SISTEMA DE ROTEAMENTO PROGRAMADO DE COLETAS
echo ========================================
echo.
echo Este script ira executar o projeto Java
echo.
echo Pressione qualquer tecla para continuar...
pause >nul

echo.
echo [1/3] Verificando Java...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERRO: Java nao encontrado!
    echo Por favor, instale o Java 17+ de: https://adoptium.net/
    pause
    exit /b 1
)
echo Java encontrado!

echo.
echo [2/3] Verificando Maven...
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERRO: Maven nao encontrado!
    echo Por favor, instale o Maven de: https://maven.apache.org/download.cgi
    pause
    exit /b 1
)
echo Maven encontrado!

echo.
echo [3/3] Iniciando o Backend Java...
echo.
echo O backend sera iniciado em: http://localhost:8888
echo Console H2: http://localhost:8888/h2-console
echo Swagger UI: http://localhost:8888/api/v1/swagger-ui.html
echo.
echo Para parar o servidor, pressione Ctrl+C
echo.
echo ========================================
echo.

mvn spring-boot:run

echo.
echo Servidor parado.
pause
