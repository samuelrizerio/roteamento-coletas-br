# 🚀 Roteamento Automático Inteligente

## 📋 **Visão Geral**

O sistema de **Roteamento Automático Inteligente** foi implementado para eliminar a necessidade de cadastro manual de rotas. Agora o sistema:

1. **Detecta automaticamente** coletas pendentes
2. **Agrupa por proximidade geográfica** usando dados de GPS
3. **Atribui automaticamente** aos coletores disponíveis
4. **Otimiza rotas** usando algoritmos avançados
5. **Considera capacidades** e restrições

## 🧮 **Como Funciona**

### **1. Detecção Automática**

```java
// Executa a cada 30 minutos automaticamente
@Scheduled(fixedRate = 1800000)
public void executarRoteamentoAutomatico() {
    // 1. Busca coletas pendentes
    // 2. Busca coletores disponíveis
    // 3. Agrupa por proximidade
    // 4. Cria rotas otimizadas
}
```

### **2. Agrupamento por Proximidade Geográfica**

```java
// Configurações do sistema
RAIO_MAXIMO_KM = 10.0;           // Raio máximo para agrupar
CAPACIDADE_MAXIMA_VEICULO = 1000.0; // Capacidade em kg
MAX_COLETAS_POR_ROTA = 15;       // Máximo de coletas por rota
```

**Algoritmo de Clustering:**

- Calcula distância entre coletas usando **Fórmula de Haversine**
- Agrupa coletas dentro do raio máximo
- Considera capacidade do veículo
- Limita número de coletas por rota

### **3. Otimização de Rotas (TSP)**

```java
// Algoritmo do Caixeiro Viajante (Nearest Neighbor)
private List<Coleta> aplicarAlgoritmoTSP(List<Coleta> coletas) {
    // Sempre visita a coleta mais próxima
    // Minimiza distância total
    // Otimiza tempo de deslocamento
}
```

### **4. Seleção Inteligente de Coletores**

```java
// Seleciona coletor com menos rotas ativas
private Usuario selecionarColetorParaGrupo(List<Usuario> coletores, GrupoColetas grupo) {
    return coletores.stream()
        .min(Comparator.comparing(coletor -> 
            rotaRepository.countByColetorAndStatus(coletor.getId(), StatusRota.EM_ANDAMENTO)))
        .orElse(coletores.get(0));
}
```

## 📊 **Parâmetros Configuráveis**

| Parâmetro | Valor Padrão | Descrição |
|-----------|---------------|-----------|
| `RAIO_MAXIMO_KM` | 10.0 km | Raio máximo para agrupar coletas |
| `CAPACIDADE_MAXIMA_VEICULO` | 1000.0 kg | Capacidade máxima do veículo |
| `MAX_COLETAS_POR_ROTA` | 15 | Máximo de coletas por rota |
| `TEMPO_POR_COLETA` | 5 min | Tempo estimado por coleta |
| `VELOCIDADE_MEDIA` | 30 km/h | Velocidade média do veículo |

## 🌐 **Endpoints Disponíveis**

### **Roteamento Automático**

```bash
# Verificar status
GET /api/v1/roteamento-automatico/status

# Executar roteamento manual
POST /api/v1/roteamento-automatico/executar

# Configurar parâmetros
POST /api/v1/roteamento-automatico/configurar

# Obter estatísticas
GET /api/v1/roteamento-automatico/estatisticas
```

### **Roteamento Avançado**

```bash
# Otimizar rotas automaticamente
POST /api/v1/roteamento/otimizar

# Calcular rota ótima
POST /api/v1/roteamento/calcular-rota

# Obter estatísticas
GET /api/v1/roteamento/estatisticas
```

## 🧪 **Como Testar**

### **1. Executar Teste Automático**

```bash
./scripts/teste-roteamento-automatico.sh
```

### **2. Teste Manual via API**

```bash
# Verificar coletas pendentes
curl -s "http://localhost:8081/api/v1/coletas/pendentes"

# Executar roteamento manual
curl -s -X POST "http://localhost:8081/api/v1/roteamento-automatico/executar"

# Verificar rotas criadas
curl -s "http://localhost:8081/api/v1/rotas"
```

### **3. Monitoramento em Tempo Real**

```bash
# Verificar status do sistema
curl -s "http://localhost:8081/api/v1/roteamento-automatico/status"

# Obter estatísticas
curl -s "http://localhost:8081/api/v1/roteamento-automatico/estatisticas"
```

## 📈 **Benefícios do Sistema**

### **✅ Automatização Completa**

- Não precisa cadastrar rotas manualmente
- Sistema detecta coletas pendentes automaticamente
- Cria rotas otimizadas sem intervenção

### **✅ Otimização Inteligente**

- Usa dados de GPS para agrupamento
- Aplica algoritmo TSP para minimizar distâncias
- Considera capacidades e restrições

### **✅ Balanceamento de Carga**

- Distribui coletas entre coletores automaticamente
- Seleciona coletores com menos rotas ativas
- Evita sobrecarga de coletores

### **✅ Configuração Flexível**

- Parâmetros ajustáveis via API
- Configurações específicas por região
- Adaptação a diferentes tipos de veículos

## 🔧 **Configuração Avançada**

### **Ajustar Parâmetros**

```json
{
  "raioMaximoKm": 15.0,
  "capacidadeMaximaVeiculo": "1200.0",
  "maxColetasPorRota": 20,
  "tempoEstimadoPorColeta": 3
}
```

### **Cronograma de Execução**

```java
// Executa a cada 30 minutos
@Scheduled(fixedRate = 1800000)

// Ou usar cron expression
@Scheduled(cron = "0 */30 * * * *") // A cada 30 minutos
```

## 📊 **Métricas de Performance**

### **Estatísticas Disponíveis**

- **Rotas criadas hoje**: Número de rotas criadas automaticamente
- **Coletas processadas**: Total de coletas incluídas em rotas
- **Tempo médio de otimização**: Tempo para processar roteamento
- **Taxa de sucesso**: Percentual de rotas criadas com sucesso
- **Economia de combustível**: Redução estimada no consumo
- **Redução de tempo**: Tempo economizado com otimização

### **Monitoramento**

```bash
# Verificar logs do roteamento automático
tail -f logs/application.log | grep "roteamento"

# Monitorar execução automática
tail -f logs/application.log | grep "@Scheduled"
```

## 🚀 **Próximos Passos**

### **1. Implementações Futuras**

- [ ] Integração com APIs de trânsito em tempo real
- [ ] Consideração de horários de trânsito
- [ ] Otimização multi-veículo
- [ ] Machine Learning para previsão de demanda

### **2. Melhorias Atuais**

- [ ] Interface web para configuração
- [ ] Dashboard de monitoramento
- [ ] Alertas de performance
- [ ] Relatórios detalhados

## 🎯 **Conclusão**

O sistema de **Roteamento Automático Inteligente** transforma completamente a operação:

- **Antes**: Cadastro manual de rotas, processo demorado e propenso a erros
- **Agora**: Sistema automático que otimiza rotas usando dados de GPS e algoritmos avançados

O sistema está **pronto para produção** e pode ser usado imediatamente para otimizar a operação de coleta de materiais recicláveis! 🎉
