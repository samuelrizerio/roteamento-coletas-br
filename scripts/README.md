# MASTER SUPER SISTEMA - Script Consolidado

## Descrição

O `master-super-sistema.sh` é um script único e poderoso que consolida **TODAS** as funcionalidades dos outros scripts e superalimenta o sistema com múltiplos processos simultâneos.

## Funcionalidades Consolidadas

### **Setup e Inicialização**

- Inicialização automática do Backend Spring Boot (múltiplas instâncias)
- Inicialização automática do Frontend React
- Verificação de dependências (curl, jq)
- Configuração automática de portas

### **Criação de Dados Massivos**

- **100 materiais** criados simultaneamente
- **200 usuários** criados simultaneamente  
- **500 coletas** criadas simultaneamente
- Dados distribuídos geograficamente em BH
- Coordenadas reais e variadas

### **Testes e Validação**

- Teste completo da API REST
- Teste completo do Frontend
- Verificação de todos os endpoints
- Validação de dados criados
- Teste de todas as páginas web

### **Testes de Carga**

- **100 requisições simultâneas** para dashboard
- **100 requisições simultâneas** para materiais
- **100 requisições simultâneas** para coletas
- **100 requisições simultâneas** para usuários
- Testes em paralelo para máxima carga

### **Algoritmos Avançados**

- **20 processos** de roteamento automático em loop
- **15 processos** de algoritmos avançados em loop
- **10 processos** de dashboard em loop
- Execução contínua e simultânea

### **Monitoramento e Superalimentação**

- Monitoramento contínuo do sistema
- **50 processos simultâneos** de teste
- Verificação de CPU, memória e conexões
- Logs detalhados de todas as operações
- Sistema superalimentado e sobrecarregado

## Como Executar

### 1. **Execução Simples**

```bash
cd /home/samuel/roteamento-coletas-br
bash ./scripts/master-super-sistema.sh
```

### 2. **Execução com Permissões**

```bash
cd /home/samuel/roteamento-coletas-br
chmod +x ./scripts/master-super-sistema.sh
./scripts/master-super-sistema.sh
```

### 3. **Execução em Background**

```bash
cd /home/samuel/roteamento-coletas-br
nohup ./scripts/master-super-sistema.sh > master-execution.log 2>&1 &
```

## Configurações do Sistema

### **Portas**

- **Backend**: 8081
- **Frontend**: 3000

### **Processos**

- **Máximo de processos**: 50
- **Intervalo de superalimentação**: 5 segundos
- **Instâncias do backend**: 3
- **Processos de roteamento**: 20
- **Processos de algoritmos**: 15
- **Processos de dashboard**: 10

### **Dados de Teste**

- **Materiais**: 100
- **Usuários**: 200
- **Coletas**: 500
- **Testes de carga**: 400 requisições simultâneas
