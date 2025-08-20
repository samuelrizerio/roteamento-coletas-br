# 🚀 Deploy no Cloudflare - Sistema de Roteamento de Coletas BR

Este guia te mostrará como publicar seu projeto **completamente gratuito** usando Cloudflare!

## 📋 **O que você vai conseguir:**

- ✅ **Frontend**: `https://app.samuelchaves.com` (Cloudflare Pages)
- ✅ **Backend**: `https://api.samuelchaves.com` (Cloudflare Workers)
- ✅ **Banco de Dados**: D1 (SQLite serverless)
- ✅ **Custo**: **ZERO** 💰
- ✅ **Performance**: Global CDN + Edge Computing

## 🛠️ **Pré-requisitos:**

1. **Conta Cloudflare** (gratuita)
2. **Domínio** `samuelchaves.com` configurado no Cloudflare
3. **Node.js** 18+ instalado
4. **Git** configurado

## 📥 **Instalação das Ferramentas:**

```bash
# Instalar Wrangler CLI
npm install -g wrangler

# Fazer login no Cloudflare
wrangler login
```

## 🚀 **Deploy Automático (Recomendado):**

```bash
# Dar permissão de execução
chmod +x deploy-cloudflare.sh

# Executar script de deploy
./deploy-cloudflare.sh
```

## 🔧 **Deploy Manual (Passo a Passo):**

### **1. Configurar Banco de Dados D1:**

```bash
# Criar banco D1
wrangler d1 create roteamento-coletas-db

# Anotar o ID retornado e atualizar backend-wrangler.toml
# Substituir "your-database-id-here" pelo ID real

# Executar schema
wrangler d1 execute roteamento-coletas-db --file=./backend/schema.sql
```

### **2. Deploy do Backend:**

```bash
cd backend
npm install
wrangler deploy --env production
cd ..
```

### **3. Deploy do Frontend:**

```bash
cd frontend
npm install
npm run build
wrangler pages deploy build --project-name="roteamento-coletas-frontend"
cd ..
```

## 🌐 **Configurar Domínios no Cloudflare:**

### **Painel Cloudflare → DNS:**

1. **Frontend**: `app.samuelchaves.com` → Cloudflare Pages
2. **Backend**: `api.samuelchaves.com` → Cloudflare Workers

### **Painel Cloudflare → Workers & Pages:**

1. **Frontend**: Configurar domínio customizado
2. **Backend**: Configurar rota customizada

## 📊 **Limites Gratuitos Cloudflare:**

| Serviço | Limite Gratuito | Seu Uso Estimado |
|---------|----------------|------------------|
| **Pages** | 100 builds/mês | ✅ Suficiente |
| **Workers** | 100.000 requests/dia | ✅ Suficiente |
| **D1** | 100.000 requests/dia | ✅ Suficiente |
| **CDN** | Ilimitado | ✅ Ilimitado |

## 🔍 **Verificar Deploy:**

### **Frontend:**

```bash
curl https://app.samuelchaves.com
```

### **Backend:**

```bash
curl https://api.samuelchaves.com/health
```

### **Banco de Dados:**

```bash
wrangler d1 execute roteamento-coletas-db --command="SELECT COUNT(*) FROM usuarios;"
```

## 🚨 **Troubleshooting:**

### **Erro: "Worker not found"**

```bash
# Verificar se está logado
wrangler whoami

# Verificar configuração
wrangler config list
```

### **Erro: "Database not found"**

```bash
# Listar bancos D1
wrangler d1 list

# Criar se não existir
wrangler d1 create roteamento-coletas-db
```

### **Erro: "Domain not configured"**

- Verificar DNS no painel Cloudflare
- Aguardar propagação (pode levar até 24h)

## 📱 **URLs Finais:**

- **Frontend**: `https://app.samuelchaves.com`
- **Backend**: `https://api.samuelchaves.com`
- **Admin**: `https://app.samuelchaves.com` (login: <admin@samuelchaves.com>)

## 🔄 **Deploy Contínuo:**

### **GitHub Actions (Opcional):**

```yaml
# .github/workflows/deploy.yml
name: Deploy to Cloudflare
on:
  push:
    branches: [main]
jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-node@v3
        with:
          node-version: '18'
      - run: npm ci
      - run: npm run build
      - uses: cloudflare/wrangler-action@v3
        with:
          apiToken: ${{ secrets.CLOUDFLARE_API_TOKEN }}
```

## 💡 **Dicas de Performance:**

1. **Cache**: Usar Cloudflare Cache Rules
2. **Minificação**: Ativar Auto Minify
3. **Compressão**: Ativar Brotli
4. **Imagens**: Usar Cloudflare Images (gratuito)

## 🎯 **Próximos Passos:**

1. ✅ Deploy inicial
2. 🔄 Configurar CI/CD
3. 📊 Monitoramento
4. 🚀 Otimizações

## 📞 **Suporte:**

- **Cloudflare Docs**: <https://developers.cloudflare.com/>
- **Wrangler Docs**: <https://developers.cloudflare.com/workers/wrangler/>
- **D1 Docs**: <https://developers.cloudflare.com/d1/>

---

**🎉 Parabéns! Seu sistema está rodando gratuitamente no Cloudflare!**
