#!/bin/bash

# SCRIPT DE EXECUÇÃO PARA AMBIENTE DE HOMOLOGAÇÃO
# ===============================================
# Este script executa o sistema no perfil de homologação
# Configurado para usar coordenadas de Belo Horizonte para testes de roteamento

echo "🚀 INICIANDO SISTEMA DE ROTEAMENTO EM AMBIENTE DE HOMOLOGAÇÃO"
echo "📍 CONFIGURADO PARA BELO HORIZONTE E REGIÃO METROPOLITANA"
echo ""

# Verificar se Java está instalado
if ! command -v java &> /dev/null; then
    echo "❌ ERRO: Java não está instalado ou não está no PATH"
    echo "   Instale o Java 17 ou superior e tente novamente"
    exit 1
fi

# Verificar versão do Java
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "❌ ERRO: Java 17 ou superior é necessário"
    echo "   Versão atual: $JAVA_VERSION"
    exit 1
fi

echo "✅ Java $JAVA_VERSION detectado"

# Verificar se Maven está instalado
if ! command -v mvn &> /dev/null; then
    echo "❌ ERRO: Maven não está instalado ou não está no PATH"
    echo "   Instale o Maven e tente novamente"
    exit 1
fi

echo "✅ Maven detectado"

# Verificar se PostgreSQL está rodando (necessário para homologação)
echo "🔍 Verificando conexão com PostgreSQL..."
if ! pg_isready -h localhost -p 5432 &> /dev/null; then
    echo "⚠️  AVISO: PostgreSQL não está rodando na porta 5432"
    echo "   Para homologação completa, inicie o PostgreSQL:"
    echo "   sudo systemctl start postgresql"
    echo "   ou"
    echo "   sudo service postgresql start"
    echo ""
    echo "   Continuando com H2 em memória para testes básicos..."
    echo ""
fi

echo ""
echo "🌆 CONFIGURAÇÃO DE HOMOLOGAÇÃO - BELO HORIZONTE"
echo "================================================"
echo "📍 Coordenadas padrão: -19.9167, -43.9345 (Centro de BH)"
echo "🏙️  Regiões incluídas:"
echo "   • Centro de Belo Horizonte"
echo "   • Savassi"
echo "   • Pampulha"
echo "   • Barreiro"
echo "   • Venda Nova"
echo "   • Contagem"
echo "   • Betim"
echo "   • Ibirité"
echo ""
echo "🧪 DADOS DE TESTE SERÃO CRIADOS AUTOMATICAMENTE"
echo "   • 10 usuários com coordenadas reais de BH"
echo "   • 10 coletas distribuídas pela região"
echo "   • Materiais padrão para testes"
echo ""

# Compilar o projeto
echo "🔨 Compilando o projeto..."
if ! mvn clean compile -q; then
    echo "❌ ERRO: Falha na compilação"
    exit 1
fi

echo "✅ Projeto compilado com sucesso"
echo ""

# Executar com perfil de homologação
echo "🚀 Iniciando sistema com perfil 'homo'..."
echo "🌐 URLs disponíveis após inicialização:"
echo "   • Sistema: http://localhost:8889/sistema"
echo "   • Página Inicial: http://localhost:8889/sistema"
echo "   • Coletas: http://localhost:8889/sistema/coletas"
echo "   • Rotas: http://localhost:8889/sistema/rotas"
echo "   • Mapa: http://localhost:8889/sistema/mapa"
echo "   • API: http://localhost:8889/api"
echo ""

echo "⏳ Aguarde a inicialização completa..."
echo "   (Pressione Ctrl+C para parar)"
echo ""

# Executar o sistema
mvn spring-boot:run -Dspring-boot.run.profiles=homo
