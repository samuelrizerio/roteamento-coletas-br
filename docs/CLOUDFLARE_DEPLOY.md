# Deploy no Cloudflare - Sistema de Roteamento de Coletas BR

## **O que você vai conseguir:**

- **Frontend**: `https://app.samuelchaves.com` (Cloudflare Pages)
- **Backend**: `https://api.samuelchaves.com` (Cloudflare Workers)
- **Banco de Dados**: D1 (SQLite serverless)
- **Custo**: **ZERO**
- **Performance**: Global CDN + Edge Computing

## **Pré-requisitos:**

1. **Conta Cloudflare** (gratuita)
2. **GitHub** com o projeto
3. **Node.js 18+** instalado
4. **Wrangler CLI** instalado

### **Instalar Wrangler:**

```bash
npm install -g wrangler
```

### **Configurar Cloudflare:**

```bash
# Login no Cloudflare
wrangler login

# Configurar projeto
wrangler init roteamento-coletas-backend
```

## **Deploy Automático (Recomendado):**

O projeto já está configurado com GitHub Actions para deploy automático:

1. **Fork** o projeto para sua conta GitHub
2. **Configure secrets** no GitHub:
   - `CLOUDFLARE_API_TOKEN`
   - `CLOUDFLARE_ACCOUNT_ID`
3. **Push** para branch `main` - deploy automático!

## **Deploy Manual (Passo a Passo):**

### **1. Configurar Frontend (Cloudflare Pages):**

```bash
# Build do frontend
cd frontend
npm install
npm run build

# Deploy para Cloudflare Pages
# Via dashboard: https://dash.cloudflare.com/pages
# Upload da pasta build/
```

### **2. Configurar Backend (Cloudflare Workers):**

```bash
# Configurar wrangler.toml
cd backend
# Editar wrangler.toml com suas configurações

# Deploy
wrangler deploy
```

### **3. Configurar Banco D1:**

```bash
# Criar banco D1
wrangler d1 create roteamento-coletas-db

# Executar schema
wrangler d1 execute roteamento-coletas-db --file=./schema.sql
```

## **Limites Gratuitos Cloudflare:**

| **Serviço** | **Limite** | **Status** |
|--------------|------------|------------|
| **Pages** | 100 builds/mês | Suficiente |
| **Workers** | 100.000 requests/dia | Suficiente |
| **D1** | 100.000 requests/dia | Suficiente |
| **CDN** | Ilimitado | Ilimitado |

## **Verificar Deploy:**

### **Frontend:**

```bash
curl -I https://app.samuelchaves.com
# Deve retornar HTTP 200
```

### **Backend:**

```bash
curl -I https://api.samuelchaves.com/health
# Deve retornar HTTP 200
```

### **Banco D1:**

```bash
# Via dashboard Cloudflare
# Workers > Seu Worker > D1 > Ver dados
```

## **URLs Finais:**

- **Frontend**: <https://app.samuelchaves.com>
- **Backend API**: <https://api.samuelchaves.com>
- **Swagger UI**: <https://api.samuelchaves.com/swagger-ui.html>
- **Health Check**: <https://api.samuelchaves.com/health>

## **Próximos Passos:**

1. Deploy inicial
2. Configuração de domínio personalizado
3. Monitoramento
4. Otimizações

## **Monitoramento:**

### **Cloudflare Analytics:**

- Requests por minuto
- Latência global
- Erros e status codes
- Uso de recursos

### **Logs:**

```bash
# Ver logs em tempo real
wrangler tail roteamento-coletas-backend
```

## **Troubleshooting:**

### **Erro 500:**

- Verificar logs do Worker
- Validar schema do banco D1
- Testar endpoints individualmente

### **CORS:**

- Configurar headers no Worker
- Verificar configurações do frontend

### **Banco D1:**

- Verificar permissões
- Validar schema SQL
- Testar queries

## **Conclusão:**

**Parabéns! Seu sistema está rodando gratuitamente no Cloudflare!**
