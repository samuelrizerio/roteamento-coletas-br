#!/bin/bash

# Script para executar o Sistema de Roteamento Programado de Coletas
# FRONTEND 100% JAVA - JSP/JSF + Thymeleaf

echo "🚀 Sistema de Roteamento Programado de Coletas - Frontend 100% Java"
echo "=================================================================="
echo ""

# Verificar se Java está instalado
if ! command -v java &> /dev/null; then
    echo "❌ ERRO: Java não está instalado!"
    echo "   Instale o OpenJDK 17+ ou superior"
    echo "   Ubuntu/Debian: sudo apt install openjdk-17-jdk"
    echo "   CentOS/RHEL: sudo yum install java-17-openjdk-devel"
    exit 1
fi

# Verificar se Maven está instalado
if ! command -v mvn &> /dev/null; then
    echo "❌ ERRO: Maven não está instalado!"
    echo "   Instale o Maven 3.6+ ou superior"
    echo "   Ubuntu/Debian: sudo apt install maven"
    echo "   CentOS/RHEL: sudo yum install maven"
    exit 1
fi

# Verificar versão do Java
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "❌ ERRO: Java 17+ é necessário! Versão atual: $JAVA_VERSION"
    echo "   Instale o OpenJDK 17+ ou superior"
    exit 1
fi

echo "✅ Java $JAVA_VERSION detectado"
echo "✅ Maven detectado"
echo ""

# Seleção de perfil
echo "🌍 SELECIONE O AMBIENTE DE EXECUÇÃO:"
echo "====================================="
echo "1) 🏠 LOCAL (H2 em memória) - Rápido para desenvolvimento"
echo "2) 🧪 HOMOLOGAÇÃO (PostgreSQL) - BH com dados completos"
echo "3) 🚀 PRODUÇÃO (PostgreSQL) - Configuração de produção"
echo "4) 🔬 TESTE (H2) - Para testes unitários"
echo ""

read -p "Escolha uma opção (1-4) [padrão: 1]: " opcao
opcao=${opcao:-1}

case $opcao in
    1)
        perfil="local"
        echo "✅ Executando em modo LOCAL (H2 em memória)"
        echo "📍 Coordenadas: Belo Horizonte (configuradas)"
        ;;
    2)
        perfil="homo"
        echo "✅ Executando em modo HOMOLOGAÇÃO (PostgreSQL)"
        echo "🌆 Configurado para Belo Horizonte com dados completos"
        echo "⚠️  Certifique-se de que o PostgreSQL está rodando"
        ;;
    3)
        perfil="prod"
        echo "✅ Executando em modo PRODUÇÃO (PostgreSQL)"
        echo "🚨 ATENÇÃO: Ambiente de produção!"
        ;;
    4)
        perfil="test"
        echo "✅ Executando em modo TESTE (H2)"
        ;;
    *)
        echo "❌ Opção inválida. Usando LOCAL como padrão."
        perfil="local"
        ;;
esac

echo ""

# Limpar e compilar o projeto
echo "🔨 Compilando o projeto..."
mvn clean compile -q

if [ $? -eq 0 ]; then
    echo "✅ Projeto compilado com sucesso!"
else
    echo "❌ ERRO na compilação!"
    exit 1
fi

echo ""
echo "🚀 Iniciando o sistema com perfil: $perfil"
echo ""

# Executar o sistema com o perfil selecionado
if [ "$perfil" = "local" ]; then
    mvn spring-boot:run
elif [ "$perfil" = "homo" ]; then
    mvn spring-boot:run -Dspring-boot.run.profiles=homo
elif [ "$perfil" = "prod" ]; then
    mvn spring-boot:run -Dspring-boot.run.profiles=prod
elif [ "$perfil" = "test" ]; then
    mvn spring-boot:run -Dspring-boot.run.profiles=test
fi

echo ""
echo "🌐 Sistema iniciado com sucesso!"
echo ""

# Mostrar URLs baseadas no perfil
if [ "$perfil" = "homo" ]; then
    echo "🌆 HOMOLOGAÇÃO - BELO HORIZONTE"
    echo "================================"
    echo "📍 Coordenadas configuradas para BH e região metropolitana"
    echo "🧪 Dados de teste com localizações reais"
    echo "🗄️  Banco: PostgreSQL (homo)"
    echo ""
fi

echo "📱 URLs disponíveis:"
echo "   🏠 Sistema Principal: http://localhost:8888/sistema"
echo "   🏠 Página Inicial:   http://localhost:8888/sistema"
echo "   📦 Coletas:          http://localhost:8888/sistema/coletas"
echo "   🗺️ Rotas:            http://localhost:8888/sistema/rotas"
echo "   👥 Usuários:         http://localhost:8888/sistema/usuarios"
echo "   ♻️ Materiais:        http://localhost:8888/sistema/materiais"
echo "   ��️ Mapa:             http://localhost:8888/sistema/mapa"
echo "   ⚙️ Configurações:    http://localhost:8888/sistema/configuracoes"
echo ""
echo "📚 Páginas Educativas:"
echo "   🎓 Inicial:          http://localhost:8888/educativo"
echo "   🟡 JSP:              http://localhost:8888/educativo/jsp"
echo "   🟢 JSF:              http://localhost:8888/educativo/jsf"
echo ""
echo "🔧 APIs REST:"
echo "   📡 Swagger UI:       http://localhost:8888/api/v1/swagger-ui.html"
echo "   🗄️ H2 Console:       http://localhost:8888/h2-console"
echo ""

if [ "$perfil" = "homo" ]; then
    echo "🌆 ESPECÍFICO PARA HOMOLOGAÇÃO:"
    echo "   📍 Centro BH: -19.9167, -43.9345"
    echo "   🏙️  Savassi: -19.9208, -43.9376"
    echo "   🏊 Pampulha: -19.8519, -43.9695"
    echo "   🏭 Barreiro: -19.9667, -44.0167"
    echo "   🏘️  Venda Nova: -19.8167, -43.9500"
    echo "   🏢 Contagem: -19.9333, -44.0500"
    echo "   🏭 Betim: -19.9667, -44.2000"
    echo "   🏘️  Ibirité: -20.0167, -44.0667"
    echo ""
fi

echo "💡 Vantagens do Frontend Java:"
echo "   ✅ 100% Java (sem Node.js)"
echo "   ✅ Deploy único (JAR contém tudo)"
echo "   ✅ Manutenção unificada"
echo "   ✅ Performance otimizada"
echo "   ✅ Segurança robusta"
echo ""
echo "🛑 Para parar o sistema: Ctrl+C"
