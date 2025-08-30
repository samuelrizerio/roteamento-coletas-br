#!/bin/bash

# ========================================
# SCRIPT PARA EXECUTAR BACKEND (DESENVOLVIMENTO LOCAL)
# ========================================
# 
# CONCEITOS:
# - Executa o Spring Boot com perfil 'local'
# - Banco H2 em memória (não precisa de PostgreSQL)
# - Porta padrão: 8081
# - Dados completos criados automaticamente

echo "🚀 INICIANDO BACKEND (DESENVOLVIMENTO LOCAL)..."
echo "📊 Perfil: local (H2 em memória)"
echo "🗄️  Banco: H2 (memória)"
echo "🌐 Porta: 8081"
echo "📁 Diretório: $(pwd)"
echo ""

# Verificar se estamos no diretório correto
if [ ! -f "pom.xml" ]; then
    echo "❌ ERRO: Diretório incorreto!"
    echo "💡 Execute este script no diretório raiz do projeto"
    exit 1
fi

# Verificar se o Java está disponível
if ! command -v java &> /dev/null; then
    echo "❌ ERRO: Java não encontrado!"
    echo "💡 Instale o Java: sudo apt install openjdk-17-jdk"
    exit 1
fi

# Verificar se o Maven está disponível
if ! command -v mvn &> /dev/null; then
    echo "❌ ERRO: Maven não encontrado!"
    echo "💡 Instale o Maven: sudo apt install maven"
    exit 1
fi

echo "✅ Java encontrado: $(java --version | head -1)"
echo "✅ Maven encontrado: $(mvn --version | head -1)"
echo "✅ Diretório correto"
echo ""

# Executar o backend
echo "🔄 Executando: mvn spring-boot:run -Dspring.profiles.active=local"
echo "⏳ Aguarde a inicialização..."
echo "🌐 Backend será aberto em: http://localhost:8081"
echo "🗄️  H2 Console: http://localhost:8081/h2-console"
echo ""

mvn spring-boot:run -Dspring.profiles.active=local
