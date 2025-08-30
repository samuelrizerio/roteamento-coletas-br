# Sistema de Roteamento Programado de Coletas - SrPC

**Sistema Java Completo** de roteamento programado e inteligente para coletas seletivas, implementando funcionalidades de **CRUD de coletas**, **CRUD de rotas**, **mapa interativo** e **algoritmos de otimização de roteamento**.

## 🚀 **SISTEMA JAVA**

✅ **Java Core** - JSP/JSF + Thymeleaf (Interface principal)  
✅ **Spring Boot** - Backend robusto e escalável  
✅ **Deploy simplificado** - JAR único contém tudo  
✅ **Arquitetura unificada** - Backend + Frontend integrados  

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

### Sistema Java Completo

O sistema usa uma **arquitetura Java unificada** com Spring Boot como core:

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

**Vantagens da arquitetura Java:**
- **Core Java robusto** (JSP/JSF + Thymeleaf + Spring Boot)
- **Deploy simplificado** (JAR único contém tudo)
- **Performance otimizada** para algoritmos de roteamento
- **Segurança robusta** com Spring Security
- **Manutenção centralizada** (código Java unificado)

### Frontend Java (JSP/JSF + Thymeleaf)

O sistema implementa uma **interface Java nativa** onde:

#### **🏗️ Sistema Principal (Java)**
```
src/main/resources/META-INF/resources/WEB-INF/views/
├── sistema/                          # Views principais do sistema (Java)
│   ├── inicial.jsp                   # Página inicial
│   ├── inicial.jsp                   # Página inicial (antigo dashboard)
│   ├── coletas.jsp                   # Gestão de coletas
│   ├── rotas.jsp                     # Gestão de rotas
│   ├── usuarios.jsp                  # Gestão de usuários
│   ├── materiais.jsp                 # Gestão de materiais
│   ├── mapa.jsp                      # Mapa interativo
│   └── configuracoes.jsp             # Configurações
└── educativo/                        # Views educativas (JSP/JSF)
    ├── inicial.jsp                   # Página educativa inicial
    ├── jsp.jsp                       # Demonstração JSP
    ├── jsf.xhtml                     # Demonstração JSF
    └── comparacao.jsp                # Comparação de tecnologias
```

#### **🔄 Fluxo de Trabalho**
- **Operações Principais**: Sistema Java (JSP/JSF)
- **Interface Unificada**: Todas as funcionalidades via Java
- **Integração**: Backend e frontend no mesmo servidor

## Como Executar

### Pré-requisitos

- **Java 17+** (OpenJDK - gratuito)
- **Maven 3.6+** (gratuito)
- **Banco H2** (em memória - não precisa instalar)

### 🚀 Execução Simplificada (Recomendado)

```bash
# Na pasta raiz do projeto (H2 em memória)
./scripts/dev-backend.sh

# Ou ambiente completo
./scripts/dev-complete.sh
```

### 🔧 Execução Manual

```bash
# Na pasta raiz do projeto (H2 em memória)
mvn spring-boot:run -Dspring.profiles.active=local

# Ou compilar JAR
mvn clean package
java -jar target/sistema-roteamento-programado-coletas-1.0.0.jar --spring.profiles.active=local
```

**O sistema Java rodará em: http://localhost:8081**

### 3. Acessar Sistema

#### **🏗️ Sistema Java (Principal)**
- **🏠 Sistema Principal**: <http://localhost:8081/sistema>
- **🏠 Página Inicial**: <http://localhost:8081/sistema>
- **📦 Coletas**: <http://localhost:8081/sistema/coletas>
- **🗺️ Rotas**: <http://localhost:8081/sistema/rotas>
- **👥 Usuários**: <http://localhost:8081/sistema/usuarios>
- **♻️ Materiais**: <http://localhost:8081/sistema/materiais>
- **🗺️ Mapa**: <http://localhost:8081/sistema/mapa>
- **⚙️ Configurações**: <http://localhost:8081/sistema/configuracoes>

### 4. Páginas Educativas

- **🎓 Inicial**: <http://localhost:8081/educativo>
- **🟡 JSP**: <http://localhost:8081/educativo/jsp>
- **🟢 JSF**: <http://localhost:8081/educativo/jsf>
- **📊 Comparação**: <http://localhost:8081/educativo/comparacao>

### 5. APIs e Ferramentas

- **📡 Swagger UI**: <http://localhost:8081/api/v1/swagger-ui.html>
- **🗄️ H2 Console**: <http://localhost:8081/h2-console>

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
- **PostgreSQL**: Banco de dados (desenvolvimento)
- **MySQL**: Banco de dados (produção)
- **JWT**: Autenticação baseada em tokens
- **Swagger/OpenAPI**: Documentação da API

### Frontend (100% Java)

- **JSP (JavaServer Pages)**: Páginas web com Java
- **JSF (JavaServer Faces)**: Componentes Java EE
- **Thymeleaf**: Template engine moderno
- **Bootstrap 5**: Framework CSS responsivo
- **JSTL**: Tag library para JSP
- **Expression Language (EL)**: Acesso a dados
- **Servlets**: Controle de requisições
- **Spring MVC**: Framework web integrado

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

### ✅ **IMPLEMENTADO (Sistema 100% Java)**

#### **🏗️ Core Java (Principal)**
- [x] **Backend Spring Boot** completo
- [x] **Frontend JSP/JSF** completo (todas as páginas)
- [x] **CRUD completo** para todas as entidades
- [x] **Mapa interativo** com Leaflet
- [x] **Algoritmos de otimização** (TSP, Nearest Neighbor)
- [x] **Algoritmos avançados** (Genético, Simulated Annealing, K-Means)
- [x] **Balanceamento inteligente** de carga
- [x] **Previsão de demanda** com Machine Learning
- [x] **Geolocalização e geocoding**
- [x] **Filtros geográficos** e clustering
- [x] **Roteamento automático** com agendamento
- [x] **Interface web nativa Java** responsiva
- [x] **Página Inicial** com estatísticas e alertas
- [x] **Sistema de notificações**
- [x] **Controller principal** do sistema
- [x] **Views JSP/JSF** implementadas
- [x] **Navegação unificada** Java
- [x] **Scripts automatizados** para execução
- [x] **Inicialização automática** de dados por ambiente
- [x] **Rotas geradas dinamicamente** pelos algoritmos (não pré-criadas)

### ✅ **Pronto para Produção**

- **Sistema completo e funcional** (100% Java)
- **Todas as funcionalidades solicitadas** implementadas
- **Arquitetura unificada** para máxima simplicidade
- **Interface de usuário moderna** e responsiva (Bootstrap 5)
- **Documentação técnica completa** e atualizada
- **Scripts automatizados** para execução simplificada
- **Inicialização automática** de dados por ambiente
- **Validações e tratamento de erros** robustos
- **Arquitetura respeitada**: rotas geradas pelos algoritmos de otimização
- **Sistema Java unificado** sem dependências externas

## 🚀 **EXECUÇÃO E SCRIPTS AUTOMATIZADOS**

### **📋 Visão Geral**

O projeto inclui scripts automatizados para facilitar a execução dos diferentes ambientes, eliminando a necessidade de comandos manuais complexos.

### **🛠️ Scripts Disponíveis**

#### **1. Backend (Spring Boot)**

##### **`scripts/dev-backend.sh`** - Desenvolvimento Local
```bash
./scripts/dev-backend.sh
```
- **Perfil**: `local`
- **Banco**: H2 (memória)
- **Porta**: 8081
- **Dados**: Completos (materiais + usuários + coletas)
- **Rotas**: Geradas automaticamente pelos algoritmos de otimização

##### **`scripts/prod-backend.sh`** - Produção
```bash
./scripts/prod-backend.sh
```
- **Perfil**: `prod`
- **Banco**: MySQL
- **Porta**: 8081
- **Dados**: Apenas materiais

#### **2. Ambiente Completo**

##### **`scripts/dev-complete.sh`** - Desenvolvimento completo
```bash
./scripts/dev-complete.sh
```
- **Executa**: Backend Spring Boot
- **Backend**: Porta 8081
- **Interface**: JSP na porta 8081
- **Logs**: Separados em arquivos

### **🔧 Configuração**

#### **1. Tornar scripts executáveis**
```bash
chmod +x scripts/*.sh
```

#### **2. Executar do diretório raiz**
```bash
cd roteamento-coletas-br
./scripts/dev-backend.sh
```

### **📊 Fluxos de Execução**

#### **🔄 Desenvolvimento Individual**
```bash
# Terminal 1: Backend
./scripts/dev-backend.sh
```

#### **🚀 Desenvolvimento Completo**
```bash
# Um comando para tudo
./scripts/dev-complete.sh
```

#### **🏭 Produção**
```bash
# Apenas backend
./scripts/prod-backend.sh
```

### **🗄️ Configurações de Banco**

#### **Desenvolvimento Local (local)**
- **Banco**: H2 em memória
- **DDL**: `create` (recria schema)
- **Dados**: Automáticos via `DataInitializationService`
- **Rotas**: NÃO criadas previamente - geradas pelos algoritmos

#### **Produção (prod)**
- **Banco**: MySQL
- **DDL**: `validate` (valida schema)
- **Dados**: Apenas materiais via `DataInitializationService`

### **📱 URLs de Acesso**

#### **Backend**
- **API**: http://localhost:8081/api/v1
- **H2 Console**: http://localhost:8081/h2-console
- **Health Check**: http://localhost:8081/api/v1/health

#### **Interface Java**
- **Sistema**: http://localhost:8081/sistema
- **Página Inicial**: http://localhost:8081/sistema

### **🚨 Troubleshooting**

#### **Problema: Porta já em uso**
```bash
# Verificar processos
lsof -i :8081

# Matar processo
kill -9 <PID>
```

#### **Problema: Dependências não encontradas**
```bash
# Backend
mvn clean install
```

#### **Problema: Banco não conecta**
```bash
# Verificar configurações
cat src/main/resources/application-dev.yml
cat src/main/resources/application-prod.yml
```

### **📚 Comandos Manuais (Alternativa)**

#### **Backend**
```bash
# Desenvolvimento
mvn spring-boot:run -Dspring.profiles.active=dev

# Produção
mvn spring-boot:run -Dspring.profiles.active=prod

# Teste
mvn spring-boot:run -Dspring.profiles.active=test
```

### **🎯 Vantagens dos Scripts**

- ✅ **Automatização** completa
- ✅ **Validações** de dependências
- ✅ **Logs separados** para debugging
- ✅ **Tratamento de erros**
- ✅ **Documentação integrada**
- ✅ **Execução padronizada**
- ✅ **Facilidade de uso**

---

## Deploy e Infraestrutura

### Spring Boot (Sistema Principal)

- Porta padrão: 8081
- Context path: /sistema (frontend Java)
- APIs REST: /api/v1
- Banco PostgreSQL (desenvolvimento)
- Banco MySQL (produção)
- **FRONTEND INTEGRADO**: JSP/JSF + Thymeleaf

### Frontend Java

- **Porta**: 8081 (mesmo servidor)
- **Tecnologia**: JSP/JSF + Thymeleaf
- **Deploy**: JAR único contém tudo

## Suporte

Para dúvidas ou suporte técnico:

- **Documentação**: Swagger UI em `/api/v1/swagger-ui.html`
- **Logs**: Verifique os logs da aplicação para debug
- **Issues**: Reporte problemas no repositório do projeto

---

**Sistema de Roteamento Programado de Coletas - SrPC**

## 🎯 **Resumo da Implementação**

✅ **Especificação atendida**: Sistema 100% Java  
✅ **Frontend implementado**: JSP/JSF + Thymeleaf  
✅ **Deploy simplificado**: JAR único contém tudo  
✅ **Arquitetura unificada**: Backend + Frontend integrados  
✅ **Manutenção centralizada**: Código Java unificado
