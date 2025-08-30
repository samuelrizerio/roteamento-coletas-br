#!/bin/bash

# Script para configurar e executar PostgreSQL para o projeto
# ===========================================================

echo "🐘 Configurando PostgreSQL para o Sistema de Roteamento de Coletas..."

# Verificar se Docker está instalado
if ! command -v docker &> /dev/null; then
    echo "❌ Docker não está instalado. Por favor, instale o Docker primeiro."
    exit 1
fi

# Verificar se Docker está rodando
if ! docker info &> /dev/null; then
    echo "❌ Docker não está rodando. Por favor, inicie o Docker primeiro."
    exit 1
fi

# Nome do container e banco
CONTAINER_NAME="srpc-postgres"
DB_NAME="srpc_dev"
DB_USER="postgres"
DB_PASSWORD="postgres"
DB_PORT="5432"

echo "📋 Configurações do banco:"
echo "   - Nome: $DB_NAME"
echo "   - Usuário: $DB_USER"
echo "   - Porta: $DB_PORT"
echo ""

# Parar e remover container existente se houver
if docker ps -a --format 'table {{.Names}}' | grep -q "^$CONTAINER_NAME$"; then
    echo "🔄 Parando container existente..."
    docker stop $CONTAINER_NAME
    docker rm $CONTAINER_NAME
fi

# Criar e executar container PostgreSQL
echo "🚀 Criando container PostgreSQL..."
docker run -d \
    --name $CONTAINER_NAME \
    -e POSTGRES_DB=$DB_NAME \
    -e POSTGRES_USER=$DB_USER \
    -e POSTGRES_PASSWORD=$DB_PASSWORD \
    -p $DB_PORT:5432 \
    -v postgres_data:/var/lib/postgresql/data \
    postgres:15

# Aguardar o PostgreSQL inicializar
echo "⏳ Aguardando PostgreSQL inicializar..."
sleep 10

# Verificar se o container está rodando
if docker ps --format 'table {{.Names}}' | grep -q "^$CONTAINER_NAME$"; then
    echo "✅ PostgreSQL está rodando!"
    echo ""
    echo "📊 Informações da conexão:"
    echo "   - Host: localhost"
    echo "   - Porta: $DB_PORT"
    echo "   - Banco: $DB_NAME"
    echo "   - Usuário: $DB_USER"
    echo "   - Senha: $DB_PASSWORD"
    echo ""
    echo "🔗 String de conexão JDBC:"
    echo "   jdbc:postgresql://localhost:$DB_PORT/$DB_NAME"
    echo ""
    echo "🌐 Para acessar via psql:"
    echo "   docker exec -it $CONTAINER_NAME psql -U $DB_USER -d $DB_NAME"
    echo ""
    echo "📝 Para ver os logs:"
    echo "   docker logs $CONTAINER_NAME"
    echo ""
    echo "🛑 Para parar o banco:"
    echo "   docker stop $CONTAINER_NAME"
    echo ""
    echo "🎯 Agora você pode executar o projeto com:"
    echo "   ./scripts/dev-backend.sh"
else
    echo "❌ Erro ao iniciar PostgreSQL. Verifique os logs:"
    docker logs $CONTAINER_NAME
    exit 1
fi
