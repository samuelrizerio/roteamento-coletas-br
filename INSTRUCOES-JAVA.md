# 🚀 Como Rodar o Projeto Apenas com Java (GRATUITO)

## ✅ O que foi simplificado

- ❌ **Removido**: Backend Node.js (Cloudflare Workers)
- ❌ **Removido**: Dependências desnecessárias
- ✅ **Mantido**: Backend Java (Spring Boot) - **100% GRATUITO**
- ✅ **Mantido**: Frontend React
- ✅ **Mantido**: Banco H2 em memória (não precisa instalar nada)

## 🛠️ Pré-requisitos (GRATUITOS)

1. **Java 17** (OpenJDK - gratuito)
   - Download: https://adoptium.net/
   - Ou: `winget install EclipseAdoptium.Temurin.17.JDK`

2. **Maven** (gratuito)
   - Download: https://maven.apache.org/download.cgi
   - Ou: `winget install Apache.Maven`

3. **Node.js** (apenas para o frontend - gratuito)
   - Download: https://nodejs.org/

## 🚀 Como executar

### 1. Backend Java (Spring Boot)

```bash
# Na pasta raiz do projeto
cd src/main/java/br/com/roteamento
mvn spring-boot:run
```

**OU**

```bash
# Na pasta raiz do projeto
mvn clean install
mvn spring-boot:run
```

O backend Java rodará em: **http://localhost:8081**

### 2. Frontend React

```bash
# Em outro terminal, na pasta frontend
cd frontend
npm install
npm start
```

O frontend rodará em: **http://localhost:3000**

## 🗄️ Banco de Dados

- **H2 Database** (em memória - não precisa instalar)
- **Console H2**: http://localhost:8081/h2-console
- **JDBC URL**: `jdbc:h2:mem:roteamento_coletas`
- **Usuário**: `sa`
- **Senha**: (deixe em branco)

## 📚 Documentação da API

- **Swagger UI**: http://localhost:8081/api/v1/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8081/api/v1/api-docs

## 💰 Por que é GRATUITO?

1. **Java OpenJDK**: 100% gratuito
2. **Spring Boot**: Framework open-source gratuito
3. **H2 Database**: Banco em memória gratuito
4. **Maven**: Build tool gratuito
5. **Node.js**: Runtime gratuito
6. **React**: Framework gratuito

## 🔧 Configurações

O projeto está configurado para:
- **Porta do Java**: 8081
- **Porta do React**: 3000
- **Banco**: H2 em memória
- **JWT**: Configurado e funcionando
- **CORS**: Configurado para desenvolvimento

## 🚫 O que foi removido

- ❌ Cloudflare Workers (Node.js)
- ❌ Wrangler (deployment Cloudflare)
- ❌ Scripts de deploy desnecessários
- ❌ Dependências Node.js do backend

## ✅ Resultado

Agora você tem um sistema **100% Java** que roda **100% gratuito** localmente, sem necessidade de serviços externos ou pagamentos.
