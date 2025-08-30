# 🚀 SCRIPTS DE EXECUÇÃO - SRPC

## 📋 **VISÃO GERAL**

Este diretório contém scripts automatizados para facilitar a execução dos diferentes ambientes e módulos do sistema SRPC.

## 🛠️ **SCRIPTS DISPONÍVEIS**

### **1. Backend (Spring Boot)**

#### **`dev-backend.sh`** - Desenvolvimento
```bash
./scripts/dev-backend.sh
```
- **Perfil**: `dev`
- **Banco**: PostgreSQL
- **Porta**: 8081
- **Dados**: Completos (materiais + usuários + rotas + coletas)

#### **`prod-backend.sh`** - Produção
```bash
./scripts/prod-backend.sh
```
- **Perfil**: `prod`
- **Banco**: MySQL
- **Porta**: 8081
- **Dados**: Apenas materiais

#### **`test-backend.sh`** - Testes
```bash
./scripts/test-backend.sh
```
- **Perfil**: `test`
- **Banco**: PostgreSQL (banco separado)
- **Executa**: Todos os testes unitários e de integração

### **2. Banco de Dados**

#### **`setup-postgresql.sh`** - Configurar PostgreSQL
```bash
./scripts/setup-postgresql.sh
```
- **Container**: Docker PostgreSQL
- **Banco**: srpc_dev
- **Usuário**: postgres
- **Porta**: 5432

#### **`test-postgresql-connection.sh`** - Testar Conexão
```bash
./scripts/test-postgresql-connection.sh
```
- **Verifica**: Status do container
- **Testa**: Conexão com o banco
- **Mostra**: Informações do banco

### **3. Frontend (Java)**

#### **Interface Java integrada**
- **Modo**: Integrado ao backend
- **Porta**: 8081 (mesmo servidor)
- **Tecnologia**: JSP/JSF + Thymeleaf
- **Acesso**: http://localhost:8081/sistema

### **4. Ambiente Completo**

#### **`dev-complete.sh`** - Desenvolvimento completo
```bash
./scripts/dev-complete.sh
```
- **Executa**: Backend Spring Boot
- **Backend**: Porta 8081
- **Interface**: JSP na porta 8081
- **Logs**: Separados em arquivos

## 🔧 **CONFIGURAÇÃO**

### **1. Tornar scripts executáveis**
```bash
chmod +x scripts/*.sh
```

### **2. Configurar PostgreSQL**
```bash
# Primeira vez
./scripts/setup-postgresql.sh

# Verificar conexão
./scripts/test-postgresql-connection.sh
```

### **3. Executar do diretório raiz**
```bash
cd roteamento-coletas-br
./scripts/dev-backend.sh
```

## 📊 **FLUXOS DE EXECUÇÃO**

### **🔄 Desenvolvimento Individual**
```bash
# Terminal 1: Backend
./scripts/dev-backend.sh
```

### **🚀 Desenvolvimento Completo**
```bash
# Um comando para tudo
./scripts/dev-complete.sh
```

### **🧪 Testes**
```bash
# Executar todos os testes
./scripts/test-backend.sh
```

### **🏭 Produção**
```bash
# Apenas backend
./scripts/prod-backend.sh
```

## 🗄️ **CONFIGURAÇÕES DE BANCO**

### **Desenvolvimento (dev)**
- **Banco**: PostgreSQL
- **DDL**: `create` (recria schema)
- **Dados**: Automáticos via `DataInitializationService`

### **Testes (test)**
- **Banco**: PostgreSQL (srpc_test)
- **DDL**: `create-drop` (isolamento completo)
- **Dados**: Limpos a cada execução

### **Produção (prod)**
- **Banco**: MySQL
- **DDL**: `validate` (valida schema)
- **Dados**: Apenas materiais via `DataInitializationService`

## 📱 **URLs de Acesso**

### **Backend**
- **API**: http://localhost:8081/api/v1
- **Interface Java**: http://localhost:8081/sistema
- **Health Check**: http://localhost:8081/api/v1/health

### **Frontend**
- **Interface**: http://localhost:8081/sistema
- **Proxy**: http://localhost:8081

## 🚨 **TROUBLESHOOTING**

### **Problema: PostgreSQL não conecta**
```bash
# Verificar se o container está rodando
docker ps | grep srpc-postgres

# Iniciar container
docker start srpc-postgres

# Ver logs
docker logs srpc-postgres

# Testar conexão
./scripts/test-postgresql-connection.sh
```

### **Problema: Porta já em uso**
```bash
# Verificar processos
lsof -i :8081
# Verificar apenas backend
lsof -i :5432

# Matar processo
kill -9 <PID>
```

### **Problema: Dependências não encontradas**
```bash
# Backend
mvn clean install

# Frontend
cd frontend
# Verificar dependências Java
```

### **Problema: Banco não conecta**
```bash
# Verificar configurações
cat src/main/resources/application-dev.yml
cat src/main/resources/application-prod.yml

# Verificar variáveis de ambiente
cat env.example
```

## 📚 **COMANDOS MANUAIS**

### **Backend**
```bash
# Desenvolvimento
mvn spring-boot:run -Dspring.profiles.active=dev

# Produção
mvn spring-boot:run -Dspring.profiles.active=prod

# Teste
mvn test -Dspring.profiles.active=test
```

### **Frontend**
```bash
# Desenvolvimento
# Interface integrada ao backend

# Build de produção
# Build integrado ao JAR
```

### **PostgreSQL**
```bash
# Acessar banco
docker exec -it srpc-postgres psql -U postgres -d srpc_dev

# Ver bancos
docker exec srpc-postgres psql -U postgres -l

# Ver tabelas
docker exec srpc-postgres psql -U postgres -d srpc_dev -c "\dt"
```

## 🎯 **RECOMENDAÇÕES**

1. **Use os scripts** para facilitar a execução
2. **Configure PostgreSQL primeiro** com `setup-postgresql.sh`
3. **Execute do diretório raiz** do projeto
4. **Verifique as portas** antes de executar
5. **Use logs separados** para debugging
6. **Configure variáveis de ambiente** para produção

## 🔗 **LINKS ÚTEIS**

- **Documentação**: `docs/`
- **Configurações**: `src/main/resources/`
- **Frontend**: `frontend/`
- **Backend**: `src/main/java/`
- **Variáveis de ambiente**: `env.example`
