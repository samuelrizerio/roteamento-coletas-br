#!/bin/bash

# SCRIPT DE EXECUÇÃO COM MELHORIAS
# =================================
# Este script executa o projeto com todas as melhorias implementadas

set -e  # Parar em caso de erro

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Função para imprimir mensagens coloridas
print_message() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_header() {
    echo -e "${BLUE}================================${NC}"
    echo -e "${BLUE}$1${NC}"
    echo -e "${BLUE}================================${NC}"
}

# Verificar se estamos no diretório correto
if [ ! -f "pom.xml" ]; then
    print_error "Execute este script no diretório raiz do projeto (onde está o pom.xml)"
    exit 1
fi

print_header "SISTEMA DE ROTEAMENTO - EXECUÇÃO COM MELHORIAS"

# Verificar Java
print_message "Verificando Java..."
if ! command -v java &> /dev/null; then
    print_error "Java não encontrado. Instale Java 17 ou superior."
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    print_error "Java 17 ou superior é necessário. Versão atual: $JAVA_VERSION"
    exit 1
fi

print_message "Java $JAVA_VERSION encontrado ✓"

# Verificar Maven
print_message "Verificando Maven..."
if ! command -v mvn &> /dev/null; then
    print_error "Maven não encontrado. Instale Maven 3.6 ou superior."
    exit 1
fi

print_message "Maven encontrado ✓"

# Verificar Redis (opcional)
print_message "Verificando Redis..."
if command -v redis-server &> /dev/null; then
    print_message "Redis encontrado ✓"
    REDIS_AVAILABLE=true
else
    print_warning "Redis não encontrado. Cache será desabilitado."
    REDIS_AVAILABLE=false
fi

# Limpar e compilar
print_header "COMPILANDO PROJETO"
print_message "Limpando projeto..."
mvn clean

print_message "Compilando projeto..."
mvn compile

print_message "Executando testes..."
mvn test

print_message "Empacotando projeto..."
mvn package -DskipTests

print_message "Compilação concluída ✓"

# Configurar variáveis de ambiente
print_header "CONFIGURANDO AMBIENTE"

# Gerar chave JWT secreta
JWT_SECRET=$(openssl rand -base64 32 2>/dev/null || echo "fallback-secret-key-for-development-only")
export JWT_SECRET

# Configurar perfil
PROFILE=${1:-dev}
export SPRING_PROFILES_ACTIVE=$PROFILE

print_message "Perfil ativo: $PROFILE"
print_message "JWT Secret configurado ✓"

# Configurar Redis se disponível
if [ "$REDIS_AVAILABLE" = true ]; then
    print_message "Iniciando Redis..."
    redis-server --daemonize yes --port 6379
    export SPRING_REDIS_HOST=localhost
    export SPRING_REDIS_PORT=6379
    print_message "Redis iniciado ✓"
fi

# Executar aplicação
print_header "EXECUTANDO APLICAÇÃO"
print_message "Iniciando Sistema de Roteamento..."

# Executar com perfil específico
case $PROFILE in
    "test")
        print_message "Executando em modo de teste..."
        mvn spring-boot:run -Dspring.profiles.active=test
        ;;
    "prod")
        print_message "Executando em modo de produção..."
        mvn spring-boot:run -Dspring.profiles.active=prod
        ;;
    *)
        print_message "Executando em modo de desenvolvimento..."
        mvn spring-boot:run -Dspring.profiles.active=dev
        ;;
esac

# Limpeza ao sair
cleanup() {
    print_header "LIMPEZA"
    if [ "$REDIS_AVAILABLE" = true ]; then
        print_message "Parando Redis..."
        redis-cli shutdown 2>/dev/null || true
    fi
    print_message "Limpeza concluída ✓"
}

# Capturar sinal de interrupção
trap cleanup EXIT INT TERM

print_message "Aplicação iniciada com sucesso!"
print_message "Acesse: http://localhost:8888"
print_message "Swagger: http://localhost:8888/swagger-ui.html"
print_message "Health: http://localhost:8888/actuator/health"
print_message "Métricas: http://localhost:8888/actuator/metrics"
print_message ""
print_message "Pressione Ctrl+C para parar a aplicação"
