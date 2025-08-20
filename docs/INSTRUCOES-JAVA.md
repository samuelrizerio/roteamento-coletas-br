# Instruções de Execução - Sistema de Roteamento de Coletas

## Visão Geral

Este guia fornece instruções completas para executar o sistema de roteamento programado de coletas, incluindo backend Java e frontend React.

## Pré-requisitos

### Obrigatórios

- **Java 17** ou superior
- **Maven 3.6** ou superior
- **Node.js 18** ou superior
- **npm** ou **yarn**

### Verificação de Versões

```bash
# Verificar Java
java -version

# Verificar Maven
mvn -version

# Verificar Node.js
node -version

# Verificar npm
npm -version
```

## Execução do Backend (Spring Boot)

### Método 1: Maven Wrapper (Recomendado)

```bash
# Na raiz do projeto
./mvnw spring-boot:run
```

### Método 2: Maven Direto

```bash
# Na raiz do projeto
mvn spring-boot:run
```

### Método 3: JAR Compilado

```bash
# Compilar
./mvnw clean package -DskipTests

# Executar
java -jar target/roteamento-coletas-br-1.0.0.jar
```

### Verificação do Backend

- **URL**: <http://localhost:8081>
- **API Base**: <http://localhost:8081/api/v1>
- **Swagger UI**: <http://localhost:8081/api/v1/swagger-ui.html>
- **H2 Console**: <http://localhost:8081/api/v1/h2-console>

## Execução do Frontend (React)

### Instalação de Dependências

```bash
cd frontend
npm install
```

### Execução em Desenvolvimento

```bash
npm start
```

### Build para Produção

```bash
# Gerar build otimizado
npm run build

# Servir build (opcional)
npx serve -s build -l 3000
```

### Verificação do Frontend

- **URL**: <http://localhost:3000>
- **Interface**: Totalmente funcional
- **Google Maps**: Integrado com fallback

## Configuração de Ambiente

### Variáveis de Ambiente (Backend)

```properties
# application.properties (opcional)
server.port=8081
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=create-drop
```

### Variáveis de Ambiente (Frontend)

```bash
# .env (opcional)
REACT_APP_API_BASE_URL=http://localhost:8081/api/v1
REACT_APP_GOOGLE_MAPS_API_KEY=sua_chave_aqui
```

## Execução Completa do Sistema

### Script de Execução (Linux/Mac)

```bash
#!/bin/bash
echo "Iniciando Backend..."
./mvnw spring-boot:run &
BACKEND_PID=$!

echo "Aguardando backend inicializar..."
sleep 30

echo "Iniciando Frontend..."
cd frontend
npm start &
FRONTEND_PID=$!

echo "Sistema iniciado!"
echo "Backend: http://localhost:8081"
echo "Frontend: http://localhost:3000"

# Para parar os processos
# kill $BACKEND_PID $FRONTEND_PID
```

### Script de Execução (Windows)

```batch
@echo off
echo Iniciando Backend...
start cmd /k "mvnw spring-boot:run"

echo Aguardando backend inicializar...
timeout /t 30

echo Iniciando Frontend...
cd frontend
start cmd /k "npm start"

echo Sistema iniciado!
echo Backend: http://localhost:8081
echo Frontend: http://localhost:3000
```

## Banco de Dados H2

### Acesso ao Console

- **URL**: <http://localhost:8081/api/v1/h2-console>
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: (vazio)

### Dados Pré-carregados

O sistema carrega automaticamente:

- 5 usuários de exemplo
- 5 materiais recicláveis
- 5 coletas de teste
- 10 rotas otimizadas

## Funcionalidades Disponíveis

### Backend (APIs REST)

- **CRUD completo** para usuários, materiais, coletas e rotas
- **Roteamento automático** com algoritmos avançados
- **Dashboard** com estatísticas em tempo real
- **Busca e filtros** avançados
- **Documentação Swagger** interativa

### Frontend (Interface Web)

- **Dashboard** com métricas visuais
- **Mapa interativo** com Google Maps
- **Gestão de coletas** intuitiva
- **Visualização de rotas** otimizadas
- **Interface responsiva**

## Solução de Problemas

### Backend não Inicia

```bash
# Verificar porta em uso
netstat -an | grep 8081

# Matar processo na porta
kill -9 $(lsof -ti:8081)

# Limpar cache Maven
./mvnw clean

# Reinstalar dependências
./mvnw dependency:resolve
```

### Frontend não Inicia

```bash
# Limpar cache npm
npm cache clean --force

# Remover node_modules
rm -rf node_modules package-lock.json

# Reinstalar dependências
npm install

# Verificar porta em uso
netstat -an | grep 3000
```

### Problemas com Google Maps

- Verifique se a chave da API está configurada
- Confirme se a API está ativada no Google Cloud Console
- Use o fallback automático quando necessário

## Logs e Debug

### Backend

```bash
# Logs detalhados
./mvnw spring-boot:run -Dlogging.level.br.com.roteamento=DEBUG

# Profile específico
./mvnw spring-boot:run -Dspring.profiles.active=dev
```

### Frontend

```bash
# Debug mode
npm start

# Build com source maps
GENERATE_SOURCEMAP=true npm run build
```

## Testes

### Backend

```bash
# Executar todos os testes
./mvnw test

# Testes específicos
./mvnw test -Dtest=UsuarioServiceTest
```

### Frontend

```bash
# Executar testes
npm test

# Coverage report
npm run test:coverage
```

## Performance

### Otimizações Implementadas

- **Cache inteligente** para consultas frequentes
- **Lazy loading** de componentes React
- **Compressão** de assets estáticos
- **Queries otimizadas** no banco H2

### Monitoramento

- **Actuator endpoints** para métricas
- **Health checks** automáticos
- **Logs estruturados** para debugging

## Conclusão

O sistema está completamente funcional e otimizado para desenvolvimento e produção. Siga as instruções acima para executar todas as funcionalidades do sistema de roteamento programado de coletas.

Para dúvidas específicas, consulte:

- **[STATUS_TESTES.md](STATUS_TESTES.md)** - Status atual do sistema
- **[RESUMO_REST_APIS.md](RESUMO_REST_APIS.md)** - Documentação das APIs
- **[CONCEITOS_IMPLEMENTADOS.md](CONCEITOS_IMPLEMENTADOS.md)** - Arquitetura técnica
