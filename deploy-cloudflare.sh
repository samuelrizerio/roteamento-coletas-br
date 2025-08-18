#!/bin/bash

# Script de Deploy Automático para Cloudflare
# Sistema de Roteamento de Coletas BR

set -e

echo "🚀 Iniciando deploy no Cloudflare..."

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Função para log colorido
log() {
    echo -e "${GREEN}[$(date +'%Y-%m-%d %H:%M:%S')]${NC} $1"
}

error() {
    echo -e "${RED}[ERRO]${NC} $1"
    exit 1
}

warning() {
    echo -e "${YELLOW}[AVISO]${NC} $1"
}

info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

# Verificar se Wrangler está instalado
if ! command -v wrangler &> /dev/null; then
    error "Wrangler não está instalado. Instale com: npm install -g wrangler"
fi

# Verificar se está logado no Cloudflare
if ! wrangler whoami &> /dev/null; then
    error "Você não está logado no Cloudflare. Execute: wrangler login"
fi

# Função para deploy do frontend
deploy_frontend() {
    log "📱 Deployando Frontend..."
    
    cd frontend
    
    # Instalar dependências
    log "Instalando dependências..."
    npm install
    
    # Build de produção
    log "Fazendo build de produção..."
    npm build
    
    # Deploy para Cloudflare Pages
    log "Deployando para Cloudflare Pages..."
    wrangler pages deploy build --project-name="roteamento-coletas-frontend" --branch=main
    
    cd ..
    log "✅ Frontend deployado com sucesso!"
}

# Função para deploy do backend
deploy_backend() {
    log "🔧 Deployando Backend..."
    
    cd backend
    
    # Instalar dependências
    log "Instalando dependências..."
    npm install
    
    # Deploy para Cloudflare规制 Workers
    log "Deployando para Cloudflare Workers..."
    wrangler deploy --env production
    
    cd ..
    log "✅ Backend deployado com sucesso!"
}

# Função para configurar banco de dados
setup_database() {
    log "🗄️ Configurando banco de dados D1..."
    
    # Criar banco D1 se não existir
    if ! wrangler d1 list | grep -q "roteamento-coletas-db"; then
        log " "Creating new D1 database..."
        wrangler d1 create roteamento-coletas-db --local=false
    fi
    
    # Executar migrações
    log "Executando migrações..."
    wrangler d1 execute roteamento-coletas-db --file=./backend/schema.sql
    
    log "✅ Banco de dados configurado!"
}

# Função para configurar domínios
setup_domains() {
    log "🌐 Configurando domínios..."
    
    info "Configure os seguintes domínios no Cloudflare:"
    info "  - app.samuelchaves.com → Frontend (Pages)"
    info "  - api.samuelchaves.com → Backend (Workers)"
    
    # Aguardar confirmação
    read -p "Domínios configurados? (y/N): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        log "✅ Domínios configurados!"
    else
        warning "Configure os domínios manualmente no painel do Cloudflare"
    fi
}

# Menu principal
main() {
    echo "🎯 Sistema de Roteamento de Coletas BR - Deploy Cloudflare"
    echo "=================================================="
    echo "1. Deploy Completo (Frontend + Backend + DB)"
    echo "2. Deploy Apenas Frontend"
    echo "3. Deploy Apenas Backend"
    echo "4. Configurar Banco de Dados"
    echo "5. Configurar Domínios"
    echo "6. Sair"
    echo ""
    
    read -p "Escolha uma opção (1-6): " choice
    
    case $choice in
        1)
            log "🚀 Iniciando deploy completo..."
            deploy_frontend
            deploy_backend
            setup_database
            setup_domains
            log "🎉 Deploy completo finalizado!"
            ;;
        2)
            deploy_frontend
            setup_domains
            ;;
        3)
            deploy_backend
            setup_domains
            ;;
        4)
            setup_database
            ;;
        5)
            setup_domains
            ;;
        6)
            log "👋 Saindo..."
            exit 0
            ;;
        *)
            error "Opção inválida!"
            ;;
    esac
}

# Executar menu principal
main
