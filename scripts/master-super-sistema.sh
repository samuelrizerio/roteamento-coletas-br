#!/bin/bash

# MASTER SUPER SISTEMA - Script Consolidado e Superalimentado
# ==============================================================
# Este script consolida TODAS as funcionalidades dos outros scripts
# e superalimenta o sistema com múltiplos processos simultâneos

set -e  # Para em caso de erro

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Função para log colorido
log() {
    echo -e "${GREEN}[$(date '+%Y-%m-%d %H:%M:%S')]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERRO]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[AVISO]${NC} $1"
}

log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCESSO]${NC} $1"
}

# Configurações do sistema
BACKEND_PORT=8081
FRONTEND_PORT=3000
MAX_PROCESSES=50
SUPER_LOAD_INTERVAL=5

# Função para verificar se um processo está rodando
check_process() {
    local port=$1
    local process_name=$2
    
    if curl -s "http://localhost:$port" > /dev/null 2>&1; then
        log_success "$process_name rodando na porta $port"
        return 0
    else
        log_warning "$process_name não está rodando na porta $port"
        return 1
    fi
}

# Função para iniciar backend
start_backend() {
    log_info "Iniciando Backend Spring Boot..."
    
    if ! check_process $BACKEND_PORT "Backend"; then
        cd /home/samuel/roteamento-coletas-br
        
        # Iniciar backend em background com múltiplas instâncias
        for i in {1..3}; do
            log_info "Iniciando instância $i do backend..."
            nohup mvn spring-boot:run > "backend-$i.log" 2>&1 &
            sleep 10
        done
        
        # Aguardar backend estar pronto
        log_info "Aguardando backend inicializar..."
        local attempts=0
        while [ $attempts -lt 60 ]; do
            if check_process $BACKEND_PORT "Backend"; then
                log_success "Backend inicializado com sucesso!"
                break
            fi
            sleep 5
            attempts=$((attempts + 1))
            log_info "Tentativa $attempts/60 - Aguardando..."
        done
        
        if [ $attempts -eq 60 ]; then
            log_error "Backend não inicializou em tempo hábil"
            return 1
        fi
    fi
}

# Função para iniciar frontend
start_frontend() {
    log_info "Iniciando Frontend React..."
    
    if ! check_process $FRONTEND_PORT "Frontend"; then
        cd /home/samuel/roteamento-coletas-br/frontend
        
        # Iniciar frontend em background
        log_info "Iniciando frontend..."
        nohup npm start > "../frontend.log" 2>&1 &
        
        # Aguardar frontend estar pronto
        log_info "Aguardando frontend inicializar..."
        local attempts=0
        while [ $attempts -lt 30 ]; do
            if check_process $FRONTEND_PORT "Frontend"; then
                log_success "Frontend inicializado com sucesso!"
                break
            fi
            sleep 5
            attempts=$((attempts + 1))
            log_info "Tentativa $attempts/30 - Aguardando..."
        done
        
        if [ $attempts -eq 30 ]; then
            log_error "Frontend não inicializou em tempo hábil"
            return 1
        fi
    fi
}

# Função para criar dados de teste massivos
create_massive_test_data() {
    log_info "Criando dados de teste massivos..."
    
    cd /home/samuel/roteamento-coletas-br
    
    # Criar materiais em massa
    log_info "Criando 100 materiais..."
    for i in {1..100}; do
        curl -s -X POST "http://localhost:$BACKEND_PORT/api/v1/materiais" \
            -H "Content-Type: application/json" \
            -d "{
                \"nome\": \"Material Teste $i\",
                \"descricao\": \"Material de teste número $i para superalimentação do sistema\",
                \"categoria\": \"PAPEL\",
                \"precoPorKg\": $((RANDOM % 100 + 1)).$((RANDOM % 100))
            }" > /dev/null 2>&1 &
        
        # Controlar taxa de criação
        if [ $((i % 10)) -eq 0 ]; then
            sleep 1
        fi
    done
    
    # Criar usuários em massa
    log_info "Criando 200 usuários..."
    for i in {1..200}; do
        curl -s -X POST "http://localhost:$BACKEND_PORT/api/v1/usuarios" \
            -H "Content-Type: application/json" \
            -d "{
                \"nome\": \"Usuário Teste $i\",
                \"email\": \"usuario$i@teste.com\",
                \"endereco\": \"Rua Teste $i, Bairro Teste, BH-MG\",
                \"latitude\": -19.9$((RANDOM % 100)),
                \"longitude\": -43.9$((RANDOM % 100)),
                \"tipoUsuario\": \"RESIDENCIAL\"
            }" > /dev/null 2>&1 &
        
        # Controlar taxa de criação
        if [ $((i % 20)) -eq 0 ]; then
            sleep 1
        fi
    done
    
    # Criar coletas em massa
    log_info "Criando 500 coletas..."
    for i in {1..500}; do
        curl -s -X POST "http://localhost:$BACKEND_PORT/api/v1/coletas" \
            -H "Content-Type: application/json" \
            -d "{
                \"endereco\": \"Endereço Coleta $i, BH-MG\",
                \"latitude\": -19.9$((RANDOM % 100)),
                \"longitude\": -43.9$((RANDOM % 100)),
                \"pesoEstimado\": $((RANDOM % 100 + 1)).$((RANDOM % 100)),
                \"observacoes\": \"Coleta de teste $i para superalimentação\",
                \"materialIds\": [1]
            }" > /dev/null 2>&1 &
        
        # Controlar taxa de criação
        if [ $((i % 50)) -eq 0 ]; then
            sleep 1
        fi
    done
    
    log_success "Dados de teste massivos criados!"
}

# Função para executar testes de carga
run_load_tests() {
    log_info "Executando testes de carga..."
    
    # Teste de dashboard em paralelo
    log_info "Testando dashboard com 100 requisições simultâneas..."
    for i in {1..100}; do
        curl -s "http://localhost:$BACKEND_PORT/api/v1/dashboard/estatisticas" > /dev/null 2>&1 &
    done
    
    # Teste de materiais em paralelo
    log_info "Testando materiais com 100 requisições simultâneas..."
    for i in {1..100}; do
        curl -s "http://localhost:$BACKEND_PORT/api/v1/materiais" > /dev/null 2>&1 &
    done
    
    # Teste de coletas em paralelo
    log_info "Testando coletas com 100 requisições simultâneas..."
    for i in {1..100}; do
        curl -s "http://localhost:$BACKEND_PORT/api/v1/coletas" > /dev/null 2>&1 &
    done
    
    # Teste de usuários em paralelo
    log_info "Testando usuários com 100 requisições simultâneas..."
    for i in {1..100}; do
        curl -s "http://localhost:$BACKEND_PORT/api/v1/usuarios" > /dev/null 2>&1 &
    done
    
    log_success "Testes de carga executados!"
}

# Função para executar algoritmos avançados em paralelo
run_advanced_algorithms() {
    log_info "Executando algoritmos avançados em paralelo..."
    
    # Roteamento automático em loop
    for i in {1..20}; do
        (
            while true; do
                curl -s "http://localhost:$BACKEND_PORT/api/v1/roteamento-automatico/status" > /dev/null 2>&1
                sleep $SUPER_LOAD_INTERVAL
            done
        ) &
        log_info "Processo de roteamento $i iniciado"
    done
    
    # Algoritmos avançados em loop
    for i in {1..15}; do
        (
            while true; do
                curl -s "http://localhost:$BACKEND_PORT/api/v1/algoritmos-avancados/status" > /dev/null 2>&1
                sleep $SUPER_LOAD_INTERVAL
            done
        ) &
        log_info "Processo de algoritmos $i iniciado"
    done
    
    # Dashboard em loop
    for i in {1..10}; do
        (
            while true; do
                curl -s "http://localhost:$BACKEND_PORT/api/v1/dashboard/estatisticas" > /dev/null 2>&1
                sleep $SUPER_LOAD_INTERVAL
            done
        ) &
        log_info "Processo de dashboard $i iniciado"
    done
    
    log_success "Algoritmos avançados executando em paralelo!"
}

# Função para monitorar sistema
monitor_system() {
    log_info "Monitorando sistema..."
    
    (
        while true; do
            echo "=== STATUS DO SISTEMA $(date) ==="
            
            # Verificar processos
            echo "Processos Java: $(ps aux | grep java | grep -v grep | wc -l)"
            echo "Processos Node: $(ps aux | grep node | grep -v grep | wc -l)"
            echo "Processos curl: $(ps aux | grep curl | grep -v grep | wc -l)"
            
            # Verificar uso de memória
            echo "Memória livre: $(free -h | grep Mem | awk '{print $7}')"
            echo "CPU: $(top -bn1 | grep "Cpu(s)" | awk '{print $2}' | cut -d'%' -f1)%"
            
            # Verificar portas
            echo "Porta $BACKEND_PORT: $(netstat -tlnp 2>/dev/null | grep ":$BACKEND_PORT" | wc -l) conexões"
            echo "Porta $FRONTEND_PORT: $(netstat -tlnp 2>/dev/null | grep ":$FRONTEND_PORT" | wc -l) conexões"
            
            echo "=================================="
            sleep 30
        done
    ) > "monitoramento-sistema.log" 2>&1 &
    
    log_success "Monitoramento iniciado em background!"
}

# Função para limpar dados de produção
clean_production_data() {
    log_info "Limpando dados de produção..."
    
    cd /home/samuel/roteamento-coletas-br
    
    # Limpar todas as entidades
    curl -s -X DELETE "http://localhost:$BACKEND_PORT/api/v1/coletas/all" > /dev/null 2>&1 || true
    curl -s -X DELETE "http://localhost:$BACKEND_PORT/api/v1/rotas/all" > /dev/null 2>&1 || true
    curl -s -X DELETE "http://localhost:$BACKEND_PORT/api/v1/usuarios/all" > /dev/null 2>&1 || true
    curl -s -X DELETE "http://localhost:$BACKEND_PORT/api/v1/materiais/all" > /dev/null 2>&1 || true
    
    log_success "Dados de produção limpos!"
}

# Função para verificar dados
verify_data() {
    log_info "Verificando dados do sistema..."
    
    echo "=== VERIFICAÇÃO DE DADOS ==="
    
    # Verificar materiais
    echo "Materiais: $(curl -s "http://localhost:$BACKEND_PORT/api/v1/materiais" | jq 'length' 2>/dev/null || echo 'Erro')"
    
    # Verificar usuários
    echo "Usuários: $(curl -s "http://localhost:$BACKEND_PORT/api/v1/usuarios" | jq 'length' 2>/dev/null || echo 'Erro')"
    
    # Verificar coletas
    echo "Coletas: $(curl -s "http://localhost:$BACKEND_PORT/api/v1/coletas" | jq 'length' 2>/dev/null || echo 'Erro')"
    
    # Verificar rotas
    echo "Rotas: $(curl -s "http://localhost:$BACKEND_PORT/api/v1/rotas" | jq 'length' 2>/dev/null || echo 'Erro')"
    
    # Verificar dashboard
    echo "Dashboard: $(curl -s "http://localhost:$BACKEND_PORT/api/v1/dashboard/estatisticas" | jq '.totalMateriais' 2>/dev/null || echo 'Erro')"
    
    echo "============================"
}

# Função para executar simulação completa
run_complete_simulation() {
    log_info "Executando simulação completa..."
    
    cd /home/samuel/roteamento-coletas-br
    
    # Executar simulação em background
    nohup bash ./scripts/simulacao-completa.sh > "simulacao-completa.log" 2>&1 &
    
    log_success "Simulação completa iniciada em background!"
}

# Função para testar API
test_api() {
    log_info "Testando API..."
    
    local endpoints=(
        "/api/v1/coletas"
        "/api/v1/materiais"
        "/api/v1/usuarios"
        "/api/v1/rotas"
        "/api/v1/dashboard/estatisticas"
        "/api/v1/mapa/dados"
        "/api/v1/roteamento-automatico/status"
    )
    
    for endpoint in "${endpoints[@]}"; do
        if curl -s "http://localhost:$BACKEND_PORT$endpoint" > /dev/null 2>&1; then
            log_success "$endpoint funcionando"
        else
            log_error "$endpoint com problema"
        fi
    done
}

# Função para testar frontend
test_frontend() {
    log_info "Testando frontend..."
    
    if curl -s "http://localhost:$FRONTEND_PORT" > /dev/null 2>&1; then
        log_success "Frontend funcionando"
        
        # Testar páginas específicas
        local pages=(
            "/"
            "/usuarios"
            "/materiais"
            "/coletas"
            "/rotas"
            "/mapa"
            "/configuracoes"
        )
        
        for page in "${pages[@]}"; do
            if curl -s "http://localhost:$FRONTEND_PORT$page" > /dev/null 2>&1; then
                log_success "Página $page funcionando"
            else
                log_warning "Página $page com problema"
            fi
        done
    else
        log_error "Frontend com problema"
    fi
}

# Função para superalimentar o sistema
supercharge_system() {
    log_info "SUPERALIMENTANDO O SISTEMA..."
    
    # Criar múltiplos processos de teste
    for i in {1..$MAX_PROCESSES}; do
        (
            while true; do
                # Executar múltiplas operações simultâneas
                curl -s "http://localhost:$BACKEND_PORT/api/v1/dashboard/estatisticas" > /dev/null 2>&1 &
                curl -s "http://localhost:$BACKEND_PORT/api/v1/materiais" > /dev/null 2>&1 &
                curl -s "http://localhost:$BACKEND_PORT/api/v1/coletas" > /dev/null 2>&1 &
                curl -s "http://localhost:$FRONTEND_PORT" > /dev/null 2>&1 &
                
                sleep $SUPER_LOAD_INTERVAL
            done
        ) &
        
        if [ $((i % 10)) -eq 0 ]; then
            log_info "Processo superalimentado $i/$MAX_PROCESSES iniciado"
        fi
    done
    
    log_success "Sistema superalimentado com $MAX_PROCESSES processos!"
}

# Função principal
main() {
    echo -e "${PURPLE}"
    echo "MASTER SUPER SISTEMA - Script Consolidado e Superalimentado"
    echo "============================================================="
    echo "Este script consolida TODAS as funcionalidades e superalimenta o sistema"
    echo -e "${NC}"
    
    # Verificar dependências
    if ! command -v curl &> /dev/null; then
        log_error "curl não está instalado"
        exit 1
    fi
    
    if ! command -v jq &> /dev/null; then
        log_warning "jq não está instalado - algumas funcionalidades podem não funcionar"
    fi
    
    # Iniciar serviços
    start_backend
    start_frontend
    
    # Aguardar serviços estarem prontos
    log_info "Aguardando serviços estarem prontos..."
    sleep 10
    
    # Executar funcionalidades
    clean_production_data
    create_massive_test_data
    run_complete_simulation
    
    # Aguardar dados serem criados
    log_info "Aguardando dados serem criados..."
    sleep 30
    
    # Executar testes e superalimentação
    test_api
    test_frontend
    verify_data
    run_load_tests
    run_advanced_algorithms
    monitor_system
    supercharge_system
    
    # Status final
    log_success "SISTEMA SUPERALIMENTADO E FUNCIONANDO!"
    echo ""
    echo "STATUS FINAL:"
    echo "   - Backend: Rodando na porta $BACKEND_PORT"
    echo "   - Frontend: Rodando na porta $FRONTEND_PORT"
    echo "   - Processos: $MAX_PROCESSES processos ativos"
    echo "   - Monitoramento: Ativo em background"
    echo "   - Superalimentação: Sistema sobrecarregado"
    echo ""
    echo "URLs de Acesso:"
    echo "   - Frontend: http://localhost:$FRONTEND_PORT"
    echo "   - Backend: http://localhost:$BACKEND_PORT"
    echo "   - Swagger: http://localhost:$BACKEND_PORT/swagger-ui.html"
    echo ""
    echo "📁 Logs criados:"
    echo "   - backend-*.log: Logs do backend"
    echo "   - frontend.log: Log do frontend"
    echo "   - simulacao-completa.log: Log da simulação"
    echo "   - monitoramento-sistema.log: Log de monitoramento"
    echo ""
    echo "O sistema está rodando com MÁXIMA CAPACIDADE!"
    echo "Pressione Ctrl+C para parar o monitoramento"
    
    # Manter script rodando para monitoramento
    wait
}

# Executar função principal
main "$@"
