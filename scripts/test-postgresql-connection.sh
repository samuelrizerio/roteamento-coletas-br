#!/bin/bash

# Script para testar a conexão com PostgreSQL
# ===========================================

echo "🧪 Testando conexão com PostgreSQL..."

# Configurações padrão
DB_HOST=${DB_HOST:-localhost}
DB_PORT=${DB_PORT:-5432}
DB_NAME=${DB_NAME:-srpc_dev}
DB_USER=${DB_USER:-postgres}
DB_PASSWORD=${DB_PASSWORD:-postgres}

echo "📋 Configurações de teste:"
echo "   - Host: $DB_HOST"
echo "   - Porta: $DB_PORT"
echo "   - Banco: $DB_NAME"
echo "   - Usuário: $DB_USER"
echo ""

# Verificar se o container está rodando
if ! docker ps --format 'table {{.Names}}' | grep -q "^srpc-postgres$"; then
    echo "❌ Container PostgreSQL não está rodando!"
    echo "💡 Execute: ./scripts/setup-postgresql.sh"
    exit 1
fi

# Testar conexão usando Docker exec
echo "🔍 Testando conexão via Docker..."
if docker exec srpc-postgres psql -U $DB_USER -d $DB_NAME -c "SELECT version();" &> /dev/null; then
    echo "✅ Conexão via Docker funcionando!"
    
    # Mostrar informações do banco
    echo ""
    echo "📊 Informações do banco:"
    docker exec srpc-postgres psql -U $DB_USER -d $DB_NAME -c "SELECT version();" | head -3
    
    # Mostrar bancos disponíveis
    echo ""
    echo "🗄️  Bancos disponíveis:"
    docker exec srpc-postgres psql -U $DB_USER -l | grep -E "^[a-zA-Z]"
    
    # Mostrar tabelas (se existirem)
    echo ""
    echo "📋 Tabelas no banco $DB_NAME:"
    docker exec srpc-postgres psql -U $DB_USER -d $DB_NAME -c "\dt" 2>/dev/null || echo "   (Nenhuma tabela encontrada - normal para primeira execução)"
    
else
    echo "❌ Falha na conexão via Docker!"
    echo "💡 Verifique os logs: docker logs srpc-postgres"
    exit 1
fi

# Testar conexão externa (se possível)
echo ""
echo "🌐 Testando conexão externa..."
if command -v psql &> /dev/null; then
    if PGPASSWORD=$DB_PASSWORD psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -c "SELECT 1;" &> /dev/null; then
        echo "✅ Conexão externa funcionando!"
    else
        echo "⚠️  Conexão externa falhou (pode ser normal se psql não estiver configurado)"
    fi
else
    echo "ℹ️  Cliente psql não instalado (opcional)"
fi

echo ""
echo "🎯 PostgreSQL está funcionando corretamente!"
echo "💡 Agora você pode executar: ./scripts/dev-backend.sh"
