# Sistema de Roteamento Programado de Coletas

Sistema completo de roteamento programado e inteligente para coletas seletivas, implementando funcionalidades de **CRUD de coletas**, **CRUD de rotas**, **mapa interativo** e **algoritmos de otimização de roteamento**.

## Funcionalidades Implementadas

### CRUD de Coletas

- **Create**: Criar novas solicitações de coleta
- **Read**: Buscar coletas por ID, usuário, status e região
- **Update**: Atualizar dados da coleta (peso, observações, status)
- **Delete**: Excluir coletas do sistema
- **Ações Especiais**: Aceitar, finalizar e cancelar coletas

### CRUD de Rotas

- **Create**: Criar rotas otimizadas para coletores
- **Read**: Buscar rotas por ID, coletor, status e região
- **Update**: Atualizar dados da rota (distância, tempo, capacidade)
- **Delete**: Excluir rotas do sistema
- **Ações Especiais**: Iniciar, finalizar e cancelar rotas

### Mapa Interativo

- **Visualização em Tempo Real**: Coletas e rotas no mapa
- **Geolocalização**: Conversão de endereços para coordenadas
- **Reverse Geocoding**: Obter endereço a partir de coordenadas
- **Filtros Geográficos**: Busca por região com raio configurável
- **Clustering**: Agrupamento de pontos próximos
- **Estatísticas Geográficas**: Métricas por região

### Algoritmos de Otimização de Roteamento

- **Algoritmo TSP**: Traveling Salesman Problem (Caixeiro Viajante)
- **Nearest Neighbor**: Vizinho mais próximo para otimização
- **Clustering Geográfico**: Agrupamento por proximidade
- **Roteamento Automático**: Execução agendada a cada 30 minutos
- **Cálculo de Distâncias**: Fórmula de Haversine
- **Otimização de Capacidade**: Balanceamento de carga
- **Algoritmo Genético**: Otimização evolutiva de rotas
- **Simulated Annealing**: Otimização global com escape de mínimos locais
- **K-Means Clustering**: Agrupamento geográfico avançado
- **Balanceamento Inteligente**: Distribuição equilibrada de carga
- **Previsão de Demanda**: Machine Learning para planejamento

## Arquitetura do Sistema

### Backend (Spring Boot - Java)

O sistema usa uma arquitetura **100% Java** com Spring Boot:

```
src/main/java/br/com/roteamento/
├── controller/
│   ├── ColetaController.java      # CRUD de coletas
│   ├── RotaController.java        # CRUD de rotas
│   ├── MapaController.java        # Mapa interativo
│   ├── UsuarioController.java     # Gestão de usuários
│   ├── MaterialController.java    # Gestão de materiais
│   └── RoteamentoAutomaticoController.java # Algoritmos
├── service/
│   ├── ColetaService.java         # Lógica de coletas
│   ├── RotaService.java           # Lógica de rotas
│   ├── UsuarioService.java        # Lógica de usuários
│   ├── MaterialService.java       # Lógica de materiais
│   └── RoteamentoAutomaticoService.java # Otimização
├── model/
│   ├── Coleta.java               # Entidade coleta
│   ├── Rota.java                 # Entidade rota
│   ├── Usuario.java              # Entidade usuário
│   ├── Material.java             # Entidade material
│   └── ColetaRota.java           # Relacionamento
└── repository/
    ├── ColetaRepository.java      # Persistência coletas
    ├── RotaRepository.java        # Persistência rotas
    ├── UsuarioRepository.java     # Persistência usuários
    └── MaterialRepository.java    # Persistência materiais
```

**✅ Vantagens da arquitetura Java:**
- **100% gratuito** (OpenJDK + Spring Boot)
- **Banco H2 em memória** (não precisa instalar PostgreSQL)
- **Deploy simples** (JAR executável)
- **Performance otimizada** para algoritmos de roteamento
- **Segurança robusta** com Spring Security

### Frontend (React + TypeScript)

```
frontend/src/
├── pages/
│   ├── ColetasPage.tsx           # CRUD de coletas
│   ├── RotasPage.tsx             # CRUD de rotas
│   ├── MapaPage.tsx              # Mapa interativo
│   ├── UsuariosPage.tsx          # Gestão de usuários
│   ├── MateriaisPage.tsx         # Gestão de materiais
│   ├── ConfiguracoesPage.tsx     # Configurações
│   └── DashboardComponent.tsx    # Dashboard principal
├── components/
│   ├── Map/
│   │   ├── GoogleMapComponent.tsx # Mapa Google
│   │   ├── MapComponent.tsx       # Mapa Leaflet
│   │   └── MapFallback.tsx       # Mapa alternativo
│   ├── Dashboard/
│   │   ├── AlertasColeta.tsx     # Alertas de coleta
│   │   └── SustentabilidadeStats.tsx # Estatísticas
│   └── Layout/
│       └── AppLayout.tsx         # Layout principal
└── services/
    └── api.ts                    # Integração API
```

## Como Executar

### Pré-requisitos

- **Java 17+** (OpenJDK - gratuito)
- **Maven 3.6+** (gratuito)
- **Node.js 18+** (apenas para frontend - gratuito)
- **Banco H2** (em memória - não precisa instalar)

### 1. Backend Java (Spring Boot)

```bash
# Na pasta raiz do projeto
mvn spring-boot:run

# Ou compilar JAR
mvn clean package
java -jar target/roteamento-coletas-br-1.0.0.jar
```

**O backend Java rodará em: http://localhost:8081**

### 2. Frontend React

```bash
cd frontend
npm install
npm start
```

**O frontend rodará em: http://localhost:3000**

### 3. Acessar Sistema

- **Backend Java**: <http://localhost:8081/api/v1>
- **Console H2**: <http://localhost:8081/h2-console>
- **Swagger UI**: <http://localhost:8081/api/v1/swagger-ui.html>
- **Frontend**: <http://localhost:3000>

### 4. Banco de Dados H2

- **JDBC URL**: `jdbc:h2:mem:roteamento_coletas`
- **Usuário**: `sa`
- **Senha**: (deixe em branco)
- **Swagger UI**: <http://localhost:8081/api/v1/swagger-ui.html>
- **H2 Console**: <http://localhost:8081/api/v1/h2-console>

## Endpoints da API

### Coletas

```
POST   /api/v1/coletas                    # Criar coleta
GET    /api/v1/coletas                    # Listar todas
GET    /api/v1/coletas/{id}              # Buscar por ID
PUT    /api/v1/coletas/{id}              # Atualizar
DELETE /api/v1/coletas/{id}              # Excluir
POST   /api/v1/coletas/{id}/aceitar      # Aceitar coleta
POST   /api/v1/coletas/{id}/finalizar    # Finalizar coleta
POST   /api/v1/coletas/{id}/cancelar     # Cancelar coleta
GET    /api/v1/coletas/status?status=    # Por status
GET    /api/v1/coletas/pendentes         # Pendentes
```

### Rotas

```
POST   /api/v1/rotas                     # Criar rota
GET    /api/v1/rotas                     # Listar todas
GET    /api/v1/rotas/{id}               # Buscar por ID
PUT    /api/v1/rotas/{id}               # Atualizar
DELETE /api/v1/rotas/{id}               # Excluir
POST   /api/v1/rotas/{id}/iniciar       # Iniciar rota
POST   /api/v1/rotas/{id}/finalizar     # Finalizar rota
POST   /api/v1/rotas/{id}/cancelar      # Cancelar rota
GET    /api/v1/rotas/status?status=     # Por status
GET    /api/v1/rotas/ativas             # Ativas
```

### Mapa Interativo

```
GET    /api/v1/mapa/dados               # Dados do mapa
GET    /api/v1/mapa/coletas/regiao      # Coletas por região
GET    /api/v1/mapa/rotas/regiao        # Rotas por região
GET    /api/v1/mapa/estatisticas        # Estatísticas geográficas
GET    /api/v1/mapa/clusters            # Clusters de coletas
GET    /api/v1/mapa/geocoding           # Geolocalizar endereço
GET    /api/v1/mapa/reverse-geocoding   # Reverse geocoding
POST   /api/v1/mapa/calcular-rota       # Calcular rota
```

### Roteamento Automático

```
POST   /api/v1/roteamento-automatico/executar    # Executar roteamento
GET    /api/v1/roteamento-automatico/status      # Status do sistema
GET    /api/v1/roteamento-automatico/estatisticas # Estatísticas
POST   /api/v1/roteamento-automatico/configurar  # Configurar parâmetros
```

### Algoritmos Avançados

```
POST   /api/v1/algoritmos-avancados/otimizar-genetico           # Algoritmo genético
POST   /api/v1/algoritmos-avancados/otimizar-simulated-annealing # Simulated annealing
POST   /api/v1/algoritmos-avancados/clustering-kmeans           # K-means clustering
POST   /api/v1/algoritmos-avancados/balancear-carga             # Balanceamento inteligente
GET    /api/v1/algoritmos-avancados/prever-demanda              # Previsão de demanda
POST   /api/v1/algoritmos-avancados/comparar-algoritmos         # Comparação de algoritmos
```

## Algoritmos Implementados

### 1. Algoritmo TSP (Traveling Salesman Problem)

```java
// Implementação do algoritmo Nearest Neighbor
private List<Coleta> aplicarAlgoritmoTSP(List<Coleta> coletas) {
    // Encontrar sempre a coleta mais próxima
    // Otimizar ordem de visitação
    // Minimizar distância total
}
```

### 2. Clustering Geográfico

```java
// Agrupamento por proximidade
private List<GrupoColetas> agruparColetasPorProximidade(List<Coleta> coletas) {
    // Calcular distâncias usando fórmula de Haversine
    // Agrupar coletas dentro do raio máximo
    // Considerar capacidade do veículo
}
```

### 3. Cálculo de Distâncias (Fórmula de Haversine)

```java
private double calcularDistanciaHaversine(double lat1, double lon1, double lat2, double lon2) {
    final int R = 6371; // Raio da Terra em km
    // Implementação da fórmula matemática
    // Considera curvatura da Terra
}
```

### 4. Algoritmo Genético

```java
public List<Coleta> otimizarRotaComAlgoritmoGenetico(List<Coleta> coletas) {
    // População de soluções candidatas
    // Seleção, crossover e mutação
    // Fitness baseado em distância total
    // Convergência para solução ótima
}
```

### 5. Simulated Annealing

```java
public List<Coleta> otimizarRotaComSimulatedAnnealing(List<Coleta> coletas) {
    // Simulação de processo de resfriamento
    // Aceitação de soluções piores com probabilidade
    // Escape de mínimos locais
    // Convergência para ótimo global
}
```

### 6. K-Means Clustering

```java
public List<List<Coleta>> clusteringKMeans(List<Coleta> coletas, int k) {
    // Agrupamento baseado em centroides
    // Iteração até convergência
    // Otimização de posicionamento
    // Balanceamento de clusters
}
```

### 7. Balanceamento Inteligente de Carga

```java
public Map<Usuario, List<Coleta>> balancearCargaInteligente(
        List<Usuario> coletores, List<List<Coleta>> gruposColetas) {
    // Distribuição equilibrada de trabalho
    // Consideração de capacidades individuais
    // Otimização de recursos
    // Prevenção de sobrecarga
}
```

### 8. Previsão de Demanda

```java
public Map<String, Object> preverDemanda(List<Coleta> coletasHistorico, int diasPrevisao) {
    // Análise de padrões históricos
    // Previsão baseada em tendências
    // Machine Learning simplificado
    // Otimização de recursos
}
```

## Funcionalidades do Mapa

### Visualização

- **Marcadores**: Coletas e rotas no mapa
- **Cores por Status**: Diferentes cores para cada status
- **Popups Informativos**: Detalhes ao clicar nos pontos
- **Filtros Dinâmicos**: Mostrar/ocultar elementos

### Geolocalização

- **Geocoding**: Endereço → Coordenadas
- **Reverse Geocoding**: Coordenadas → Endereço
- **Validação**: Verificação de coordenadas válidas

### Filtros Geográficos

- **Busca por Região**: Raio configurável
- **Clustering**: Agrupamento automático
- **Zoom Dinâmico**: Ajuste automático da visualização

## Configurações

### Parâmetros de Roteamento

```yaml
app:
  routing:
    max-search-radius: 5000      # Raio máximo (metros)
    max-routing-time: 30         # Tempo máximo (segundos)
  notification:
    expiration-time: 30          # Tempo expiração (minutos)
    max-retry-attempts: 3        # Máximo tentativas
```

### Algoritmos de Otimização

- **raio_maximo_km**: 10.0 km
- **capacidade_maxima_veiculo**: 1000.0 kg
- **max_coletas_por_rota**: 15 coletas
- **velocidade_media**: 30 km/h

## Métricas e Estatísticas

### Coletas

- Total de coletas por status
- Coletas pendentes
- Coletas por região
- Tempo médio de processamento

### Rotas

- Rotas criadas hoje
- Distância total otimizada
- Tempo médio de execução
- Taxa de sucesso

### Geográficas

- Regiões ativas
- Densidade média
- Distância média
- Cobertura territorial

## Tecnologias Utilizadas

### Backend

#### Spring Boot (Java)

- **Spring Boot 3.2.0**: Framework principal
- **Spring Data JPA**: Persistência de dados
- **Spring Security**: Autenticação e autorização
- **PostgreSQL/H2**: Banco de dados
- **JWT**: Autenticação baseada em tokens
- **Swagger/OpenAPI**: Documentação da API

#### Cloudflare Workers (Node.js)

- **Node.js 18+**: Runtime JavaScript
- **Wrangler**: Ferramenta de deploy
- **itty-router**: Roteamento HTTP
- **D1 Database**: Banco de dados serverless

### Frontend

- **React 18**: Biblioteca JavaScript
- **TypeScript**: Tipagem estática
- **Material-UI**: Componentes de interface
- **React Query**: Gerenciamento de estado
- **Google Maps API**: Mapa interativo
- **Leaflet**: Mapa alternativo
- **React Router**: Navegação
- **Framer Motion**: Animações

### Algoritmos

- **TSP (Traveling Salesman Problem)**: Otimização de rotas
- **Nearest Neighbor**: Algoritmo guloso
- **Clustering Geográfico**: Agrupamento por proximidade
- **Fórmula de Haversine**: Cálculo de distâncias
- **Algoritmo Genético**: Otimização evolutiva
- **Simulated Annealing**: Otimização global
- **K-Means Clustering**: Agrupamento avançado
- **Balanceamento Inteligente**: Distribuição de carga
- **Previsão de Demanda**: Machine Learning

## Status do Projeto

### Implementado

- [x] CRUD completo de coletas
- [x] CRUD completo de rotas
- [x] CRUD completo de usuários
- [x] CRUD completo de materiais
- [x] Mapa interativo com Google Maps e Leaflet
- [x] Algoritmos de otimização (TSP, Nearest Neighbor)
- [x] Algoritmos avançados (Genético, Simulated Annealing, K-Means)
- [x] Balanceamento inteligente de carga
- [x] Previsão de demanda com Machine Learning
- [x] Geolocalização e geocoding
- [x] Filtros geográficos e clustering
- [x] Roteamento automático com agendamento
- [x] Interface web moderna e responsiva
- [x] Documentação completa da API
- [x] Backend híbrido (Spring Boot + Cloudflare Workers)
- [x] Dashboard com estatísticas e alertas
- [x] Sistema de notificações

### Pronto para Produção

- Sistema completo e funcional
- Todas as funcionalidades solicitadas implementadas
- Arquitetura híbrida para flexibilidade
- Interface de usuário moderna e responsiva
- Documentação técnica completa

## Deploy e Infraestrutura

### Spring Boot

- Porta padrão: 8081
- Context path: /api/v1
- Banco H2 em memória (desenvolvimento)
- PostgreSQL para produção

### Cloudflare Workers

- Domínio: api.samuelchaves.com
- Banco D1 (serverless)
- Deploy automático via Wrangler
- Cron jobs para tarefas agendadas

### Frontend

- Porta padrão: 3000
- Build otimizado para produção
- Proxy para backend local

## Suporte

Para dúvidas ou suporte técnico:

- **Documentação**: Swagger UI em `/api/v1/swagger-ui.html`
- **Logs**: Verifique os logs da aplicação para debug
- **Issues**: Reporte problemas no repositório do projeto

---

**Sistema de Roteamento Programado de Coletas BR - Implementação Completa e Funcional!**
