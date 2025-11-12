#!/bin/bash

# ========================================
# SCRIPT PARA CONFIGURAR VARIÁVEIS DE AMBIENTE
# ========================================
# 
# Este script configura as variáveis de ambiente necessárias
# para o sistema de roteamento de coletas
# 
# USO:
#   source ./scripts/setup-env.sh
#   source ./scripts/setup-env.sh prod

echo "🔧 CONFIGURANDO VARIÁVEIS DE AMBIENTE..."
echo ""

# Perfil padrão
PROFILE=${1:-local}

echo "📊 Perfil selecionado: ${PROFILE}"
echo ""

# Configurações base
export SPRING_PROFILES_ACTIVE=${PROFILE}

# Configurações do servidor
export SERVER_PORT=8081
export SERVER_CONTEXT_PATH=/api/v1

# Configurações de logging
export LOG_LEVEL=INFO
export SPRING_LOG_LEVEL=DEBUG
export HIBERNATE_LOG_LEVEL=DEBUG

# Configurações de performance
export HIBERNATE_JDBC_BATCH_SIZE=20
export HIBERNATE_CACHE_ENABLED=false

# Configurações de roteamento
export ROUTING_MAX_SEARCH_RADIUS=5000
export ROUTING_MAX_TIME=30

# Configurações de notificação
export NOTIFICATION_EXPIRATION_TIME=30
export NOTIFICATION_MAX_RETRY_ATTEMPTS=3

# Configurações de monitoramento
export MANAGEMENT_ENDPOINTS=health,info,metrics
export PROMETHEUS_ENABLED=true

# Configurações específicas por perfil
case ${PROFILE} in
    "local")
        echo "🏠 Configurando ambiente LOCAL (H2 em memória)..."
        export DB_HOST=localhost
        export DB_PORT=5432
        export DB_NAME=srpc_local
        export DB_USERNAME=sa
        export DB_PASSWORD=
        export DB_MAX_POOL_SIZE=10
        export DB_MIN_POOL_SIZE=2
        export DB_MAX_LIFETIME=1800000
        export H2_CONSOLE_ENABLED=true
        export H2_CONSOLE_PATH=/h2-console
        ;;
        
    "dev")
        echo "🚀 Configurando ambiente DESENVOLVIMENTO (PostgreSQL)..."
        export DB_HOST=localhost
        export DB_PORT=5433
        export DB_NAME=srpc_dev
        export DB_USERNAME=postgres
        export DB_PASSWORD=postgres
        export DB_MAX_POOL_SIZE=10
        export DB_MIN_POOL_SIZE=2
        export DB_MAX_LIFETIME=1800000
        export ROUTING_MAX_SEARCH_RADIUS=2000
        export ROUTING_MAX_TIME=15
        export NOTIFICATION_EXPIRATION_TIME=15
        export NOTIFICATION_MAX_RETRY_ATTEMPTS=2
        ;;
        
    "homo")
        echo "🧪 Configurando ambiente HOMOLOGAÇÃO (PostgreSQL)..."
        export DB_HOST=localhost
        export DB_PORT=5432
        export DB_NAME=srpc_homo
        export DB_USERNAME=postgres
        export DB_PASSWORD=postgres
        export DB_MAX_POOL_SIZE=15
        export DB_MIN_POOL_SIZE=3
        export DB_MAX_LIFETIME=1800000
        export HIBERNATE_CACHE_ENABLED=true
        export ROUTING_MAX_SEARCH_RADIUS=8000
        export ROUTING_MAX_TIME=45
        export NOTIFICATION_EXPIRATION_TIME=45
        export NOTIFICATION_MAX_RETRY_ATTEMPTS=4
        export MANAGEMENT_ENDPOINTS=health,info,metrics,env,configprops
        ;;
        
    "prod")
        echo "🏭 Configurando ambiente PRODUÇÃO (MySQL)..."
        export DB_HOST=localhost
        export DB_PORT=3306
        export DB_NAME=srpc_prod
        export DB_USERNAME=
        export DB_PASSWORD=
        export DB_MAX_POOL_SIZE=20
        export DB_MIN_POOL_SIZE=5
        export DB_MAX_LIFETIME=1800000
        export HIBERNATE_CACHE_ENABLED=true
        export HIBERNATE_JDBC_BATCH_SIZE=50
        export LOG_LEVEL=WARN
        export SPRING_LOG_LEVEL=INFO
        export HIBERNATE_LOG_LEVEL=WARN
        export ROUTING_MAX_SEARCH_RADIUS=10000
        export ROUTING_MAX_TIME=60
        export NOTIFICATION_EXPIRATION_TIME=60
        export NOTIFICATION_MAX_RETRY_ATTEMPTS=5
        export MANAGEMENT_ENDPOINTS=health,info,metrics
        ;;
        
    "test")
        echo "🧪 Configurando ambiente TESTE (PostgreSQL)..."
        export DB_HOST=localhost
        export DB_PORT=5433
        export DB_NAME=srpc_test
        export DB_USERNAME=postgres
        export DB_PASSWORD=postgres
        export DB_MAX_POOL_SIZE=5
        export DB_MIN_POOL_SIZE=1
        export DB_MAX_LIFETIME=1800000
        export LOG_LEVEL=WARN
        export SPRING_LOG_LEVEL=INFO
        export HIBERNATE_LOG_LEVEL=WARN
        export ROUTING_MAX_SEARCH_RADIUS=1000
        export ROUTING_MAX_TIME=10
        export NOTIFICATION_EXPIRATION_TIME=5
        export NOTIFICATION_MAX_RETRY_ATTEMPTS=1
        export MANAGEMENT_ENDPOINTS=health,info
        export PROMETHEUS_ENABLED=false
        ;;
        
    *)
        echo "❌ ERRO: Perfil '${PROFILE}' não reconhecido!"
        echo "💡 Perfis disponíveis: local, dev, homo, prod, test"
        exit 1
        ;;
esac

# Configurações de segurança
if [ -z "$JWT_SECRET" ]; then
    if [ "$PROFILE" = "prod" ]; then
        echo "❌ ERRO: JWT_SECRET não definido para PRODUÇÃO. Configure uma chave segura."
        exit 1
    else
        echo "⚠️  AVISO: JWT_SECRET não definido (perfil ${PROFILE}). Defina para testes mais realistas."
    fi
fi

export JWT_EXPIRATION=86400000

echo ""
echo "✅ CONFIGURAÇÃO CONCLUÍDA!"
echo ""
echo "📋 Variáveis configuradas:"
echo "   SPRING_PROFILES_ACTIVE: ${SPRING_PROFILES_ACTIVE}"
echo "   DB_HOST: ${DB_HOST}"
echo "   DB_PORT: ${DB_PORT}"
echo "   DB_NAME: ${DB_NAME}"
echo "   DB_USERNAME: ${DB_USERNAME}"
echo "   DB_MAX_POOL_SIZE: ${DB_MAX_POOL_SIZE}"
echo "   LOG_LEVEL: ${LOG_LEVEL}"
echo "   SPRING_LOG_LEVEL: ${SPRING_LOG_LEVEL}"
echo "   HIBERNATE_LOG_LEVEL: ${HIBERNATE_LOG_LEVEL}"
echo ""
echo "🚀 Para executar o sistema:"
echo "   mvn spring-boot:run -Dspring.profiles.active=${PROFILE}"
echo ""
echo "🔍 Para verificar as variáveis:"
echo "   env | grep -E '(DB_|JWT_|SPRING_|LOG_|ROUTING_|NOTIFICATION_)'"
echo ""
echo "⚠️  IMPORTANTE:"
echo "   - Use 'source' para executar este script"
echo "   - Configure JWT_SECRET em produção"
echo "   - Use HTTPS em produção"
echo "   - Monitore logs de acesso"
