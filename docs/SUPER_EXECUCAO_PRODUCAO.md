# 🚀 SUPER EXECUÇÃO EM PRODUÇÃO

Este documento descreve como executar uma super execução em produção para estressar o Sistema de Roteamento Programado de Coletas com uma quantidade massiva de dados.

## 📋 Visão Geral

A super execução em produção é um conjunto de scripts que cria uma quantidade massiva de dados para testar o sistema em condições reais de produção, incluindo:

- **100 usuários** (50 residenciais, 20 comerciais, 30 coletores)
- **15 materiais** diversificados
- **500 coletas** em diferentes status
- **50 rotas** em diferentes status
- **Testes de performance** com múltiplas requisições simultâneas
- **Algoritmos avançados** (TSP, Dijkstra)
- **Roteamento automático**

## 🎯 Objetivos

1. **Estressar o sistema** com alta carga de dados
2. **Testar performance** sob condições reais
3. **Validar algoritmos** de roteamento
4. **Verificar escalabilidade** do sistema
5. **Testar dashboard** com dados massivos
6. **Simular cenários** de produção

## 📊 Dados Gerados

### 👥 Usuários

- **50 usuários residenciais** com endereços e coordenadas realistas
- **20 usuários comerciais** (empresas)
- **30 coletores** profissionais

### 📦 Materiais

- **15 tipos de materiais** diversificados:
  - Papel (Papel Branco, Papelão, Jornal)
  - Plástico (PET, PEAD, PVC)
  - Vidro (Verde, Marrom, Transparente)
  - Metal (Alumínio, Ferro, Cobre)
  - Orgânico, Eletrônicos, Perigosos

### 🗑️ Coletas

- **500 coletas** distribuídas em diferentes status:
  - 100 SOLICITADAS
  - 100 EM_ANÁLISE
  - 100 ACEITAS
  - 100 EM_ROTA
  - 100 COLETADAS

### 🗺️ Rotas

- **50 rotas** em diferentes status:
  - 10 PLANEJADAS
  - 20 EM_EXECUÇÃO
  - 15 FINALIZADAS
  - 5 OTIMIZADAS

## 🚀 Como Executar

### 1. Preparação

```bash
# Verificar se o sistema está rodando
./scripts/setup.sh
```

### 2. Execução Completa

```bash
# Executar super execução (com opção de limpeza)
./scripts/executar-super-producao.sh
```

### 3. Execução Manual

```bash
# Limpar dados (opcional)
./scripts/limpar-dados-producao.sh

# Executar super execução
./scripts/super-execucao-producao.sh
```

### 4. Monitoramento

```bash
# Monitorar sistema em tempo real
./scripts/monitorar-sistema.sh
```

## 📈 Funcionalidades Testadas

### 🔄 Criação Massiva de Dados

- Criação de 100 usuários em lote
- Criação de 15 materiais diversificados
- Criação de 500 coletas com status variados
- Criação de 50 rotas otimizadas

### 🤖 Roteamento Automático

- Execução do algoritmo TSP (Traveling Salesman Problem)
- Otimização de rotas com Nearest Neighbor
- Distribuição automática de coletas

### 🧮 Algoritmos Avançados

- **TSP (Caixeiro Viajante)**: Otimização de rotas
- **Dijkstra**: Cálculo de caminhos mais curtos
- **Nearest Neighbor**: Algoritmo guloso para roteamento

### 📊 Dashboard e Relatórios

- Estatísticas gerais do sistema
- Coletas por status
- Coletas por material
- Coletas por região

### ⚡ Testes de Performance

- Múltiplas requisições simultâneas
- Teste de tempo de resposta
- Verificação de escalabilidade

## 🔍 Monitoramento

### Script de Monitoramento

O script `monitorar-sistema.sh` fornece:

- Status do backend e frontend
- Contadores de entidades
- Status das coletas e rotas
- Tempo de resposta da API
- Performance do dashboard

### Métricas Monitoradas

- **Conectividade**: Backend e Frontend
- **Contadores**: Usuários, Materiais, Coletas, Rotas
- **Status**: Distribuição de coletas e rotas por status
- **Performance**: Tempo de resposta das APIs

## 🎯 Resultados Esperados

### ✅ Sucesso

- Sistema funcionando com alta carga
- Performance aceitável (< 2s para APIs)
- Dashboard responsivo
- Algoritmos executando corretamente

### ⚠️ Pontos de Atenção

- Tempo de resposta das APIs
- Uso de memória do sistema
- Performance do banco de dados
- Responsividade do frontend

## 🛠️ Troubleshooting

### Problemas Comuns

#### Backend não responde

```bash
# Verificar se está rodando
curl http://localhost:8081/api/v1/usuarios

# Reiniciar se necessário
./scripts/setup.sh
```

#### Erro de conectividade

```bash
# Aguardar mais tempo
sleep 30

# Verificar logs
tail -f logs/application.log
```

#### Performance lenta

```bash
# Monitorar sistema
./scripts/monitorar-sistema.sh

# Verificar recursos
htop
```

## 📋 Checklist de Execução

- [ ] Sistema rodando (backend + frontend)
- [ ] Banco de dados acessível
- [ ] APIs respondendo
- [ ] Executar limpeza (opcional)
- [ ] Executar super execução
- [ ] Monitorar performance
- [ ] Verificar dashboard
- [ ] Testar funcionalidades

## 🌐 Acesso ao Sistema

Após a execução, acesse:

- **Frontend**: <http://localhost:3000>
- **API**: <http://localhost:8081/api/v1>
- **Dashboard**: <http://localhost:3000/dashboard>

## 📊 Relatórios Disponíveis

- **Estatísticas Gerais**: `/api/v1/dashboard/estatisticas`
- **Coletas por Status**: `/api/v1/dashboard/coletas-por-status`
- **Coletas por Material**: `/api/v1/dashboard/coletas-por-material`
- **Coletas por Região**: `/api/v1/dashboard/coletas-por-regiao`

## 🎉 Conclusão

A super execução em produção é uma ferramenta essencial para:

- Validar o sistema sob condições reais
- Identificar gargalos de performance
- Testar algoritmos de roteamento
- Verificar escalabilidade
- Simular cenários de produção

Execute regularmente para manter a qualidade do sistema!
