#!/bin/bash

# ========================================
# SCRIPT PARA EXECUTAR TESTES DO BACKEND
# ========================================
# 
# CONCEITOS:
# - Executa os testes do Spring Boot com perfil de teste
# - Usa banco PostgreSQL para testes
# - Executa todos os testes unitários e de integração
# - Gera relatórios de cobertura

echo "🧪 EXECUTANDO TESTES DO BACKEND..."
echo "📊 Perfil: test"
echo "🗄️  Banco: PostgreSQL (testes)"
echo "📁 Diretório: $(pwd)"
echo ""

# Verificar se o Maven está disponível
if ! command -v mvn &> /dev/null; then
    echo "❌ ERRO: Maven não encontrado!"
    echo "💡 Instale o Maven: sudo apt install maven"
    exit 1
fi

# Verificar se estamos no diretório correto
if [ ! -f "pom.xml" ]; then
    echo "❌ ERRO: pom.xml não encontrado!"
    echo "💡 Execute este script no diretório raiz do projeto"
    exit 1
fi

# Verificar se o PostgreSQL está rodando
echo "🔍 Verificando conexão com PostgreSQL..."
if ! docker ps --format 'table {{.Names}}' | grep -q "^srpc-postgres$"; then
    echo "❌ ERRO: Container PostgreSQL não está rodando!"
    echo "💡 Execute primeiro: ./scripts/setup-postgresql.sh"
    exit 1
fi

# Verificar se o container está realmente rodando
if ! docker ps --format 'table {{.Status}}' | grep -q "Up"; then
    echo "❌ ERRO: Container PostgreSQL não está ativo!"
    echo "💡 Execute: docker start srpc-postgres"
    exit 1
fi

echo "✅ Maven encontrado"
echo "✅ Diretório correto"
echo "✅ PostgreSQL conectado"
echo ""

# Criar banco de teste se não existir
echo "🔧 Preparando banco de teste..."
docker exec srpc-postgres psql -U postgres -c "CREATE DATABASE srpc_test;" 2>/dev/null || echo "   Banco de teste já existe"

# Executar os testes
echo "🔄 Executando: mvn test -Dspring.profiles.active=test"
echo "⏳ Aguarde a execução dos testes..."
echo ""

mvn test -Dspring.profiles.active=test

# Verificar resultado dos testes
if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Todos os testes passaram!"
    echo ""
    echo "📊 Relatórios gerados:"
    echo "   - Cobertura: target/site/jacoco/index.html"
    echo "   - Testes: target/surefire-reports/"
    echo ""
else
    echo ""
    echo "❌ Alguns testes falharam!"
    echo "💡 Verifique os relatórios em target/surefire-reports/"
    exit 1
fi
