#!/bin/bash

# ========================================
# SCRIPT PARA EXECUTAR AMBIENTE COMPLETO DE DESENVOLVIMENTO LOCAL
# ========================================
# 
# CONCEITOS:
# - Executa backend Spring Boot com JSP
# - Backend na porta 8081 (Spring Boot + H2)
# - Interface web via JSP (100% Java)
# - Dados completos criados automaticamente

echo "🚀 INICIANDO AMBIENTE COMPLETO DE DESENVOLVIMENTO LOCAL..."
echo "📊 Backend: Spring Boot (local) + JSP"
echo "🗄️  Banco: H2 (memória)"
echo "🌐 Backend: http://localhost:8081"
echo "🌐 Interface: http://localhost:8081 (JSP)"
echo "📁 Diretório: $(pwd)"
echo ""

# Verificar se estamos no diretório correto
if [ ! -f "pom.xml" ]; then
    echo "❌ ERRO: Diretório incorreto!"
    echo "💡 Execute este script no diretório raiz do projeto"
    exit 1
fi

# Função para limpar processos ao sair
cleanup() {
    echo ""
    echo "🛑 Parando todos os processos..."
    kill $BACKEND_PID 2>/dev/null
    echo "✅ Processos parados"
    exit 0
}

# Capturar Ctrl+C
trap cleanup SIGINT

echo "🔄 Iniciando backend em background..."
mvn spring-boot:run -Dspring.profiles.active=local > backend.log 2>&1 &
BACKEND_PID=$!

echo "⏳ Aguardando backend inicializar..."
sleep 15

# Verificar se o backend está rodando
if ! curl -s http://localhost:8081/api/v1/health > /dev/null 2>&1; then
    echo "⚠️  Backend pode não estar rodando ainda, continuando..."
fi

echo ""
echo "🎉 AMBIENTE INICIADO COM SUCESSO!"
echo ""
echo "📊 STATUS:"
echo "   Backend:  http://localhost:8081 (PID: $BACKEND_PID)"
echo "   Interface: http://localhost:8081 (JSP)"
echo ""
echo "📋 LOGS:"
echo "   Backend:  tail -f backend.log"
echo ""
echo "🌐 ACESSOS:"
echo "   Página Inicial: http://localhost:8081/sistema"
echo "   Coletas:  http://localhost:8081/sistema/coletas"
echo "   Rotas:    http://localhost:8081/sistema/rotas"
echo "   Mapa:     http://localhost:8081/sistema/mapa"
echo "   Materiais: http://localhost:8081/sistema/materiais"
echo "   Usuários:  http://localhost:8081/sistema/usuarios"
echo "   H2 Console: http://localhost:8081/h2-console"
echo ""
echo "🛑 Pressione Ctrl+C para parar o serviço"
echo ""

# Aguardar indefinidamente
wait
