#!/bin/bash

# ========================================
# SCRIPT PARA EXECUTAR BACKEND EM PRODUÇÃO
# ========================================
# 
# CONCEITOS:
# - Executa o Spring Boot com perfil de produção
# - Usa banco MySQL configurado
# - Cria apenas materiais (sem usuários, rotas, coletas)
# - Porta padrão: 8889

echo "🚀 INICIANDO BACKEND EM PRODUÇÃO..."
echo "📊 Perfil: prod"
echo "🗄️  Banco: MySQL"
echo "🌐 Porta: 8889"
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

# Verificar variáveis de ambiente
if [ -z "$DB_HOST" ] || [ -z "$DB_USER" ] || [ -z "$DB_PASS" ]; then
    echo "⚠️  AVISO: Variáveis de banco não configuradas"
    echo "💡 Configure: DB_HOST, DB_USER, DB_PASS"
    echo "💡 Ou use o arquivo application-prod.yml"
fi

echo "✅ Maven encontrado"
echo "✅ Diretório correto"
echo ""

# Executar o backend
echo "🔄 Executando: mvn spring-boot:run -Dspring.profiles.active=prod"
echo "⏳ Aguarde a inicialização..."
echo ""

mvn spring-boot:run -Dspring.profiles.active=prod
