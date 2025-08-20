# Configuração da API do Google Maps

## Problema Atual

O frontend está apresentando erro `InvalidKeyMapError` ao tentar carregar o Google Maps. Isso indica que a chave da API está inválida ou não configurada corretamente.

## Soluções

### 1. Configurar Chave Válida (Recomendado)

#### Passo 1: Obter Chave da API

1. Acesse [Google Cloud Console](https://console.cloud.google.com/)
2. Crie um novo projeto ou selecione um existente
3. Ative a **Maps JavaScript API**
4. Vá para **APIs & Services > Credentials**
5. Clique em **Create Credentials > API Key**
6. Copie a chave gerada

#### Passo 2: Configurar no Frontend

1. Crie um arquivo `.env` na raiz do frontend:

```bash
cd frontend
touch .env
```

2. Adicione a chave da API:

```env
REACT_APP_GOOGLE_MAPS_API_KEY=sua_chave_aqui
```

3. Reinicie o servidor de desenvolvimento:

```bash
npm start
```

### 2. Solução Temporária (Fallback)

Se não conseguir configurar a chave imediatamente, o sistema usará automaticamente o OpenStreetMap como alternativa.

#### Como Funciona

- Quando o Google Maps falhar, o componente `MapFallback` será exibido
- Este componente carrega o OpenStreetMap usando a biblioteca Leaflet
- Funcionalidades básicas de mapa estarão disponíveis

#### Limitações do Fallback

- Sem autocompletar de endereços
- Sem cálculo de rotas otimizadas
- Sem geocoding avançado
- Interface mais simples

### 3. Configurações de Segurança da API

#### Restringir Chave da API

1. No Google Cloud Console, clique na chave criada
2. Em **Application restrictions**, selecione **HTTP referrers**
3. Adicione os domínios permitidos:
   - `localhost:3000/*` (desenvolvimento)
   - `seu-dominio.com/*` (produção)

#### APIs Necessárias

Certifique-se de que as seguintes APIs estão ativadas:

- Maps JavaScript API
- Places API (para autocompletar)
- Geocoding API (para conversão de endereços)
- Directions API (para cálculo de rotas)

## Estrutura de Arquivos

```
frontend/
├── src/
│   ├── config/
│   │   ├── maps.ts              # Configuração do Google Maps
│   │   └── environment.ts        # Configurações de ambiente
│   └── components/Map/
│       ├── GoogleMapComponent.tsx # Componente principal
│       └── MapFallback.tsx       # Fallback para OpenStreetMap
├── .env                          # Variáveis de ambiente (criar)
└── GOOGLE_MAPS_SETUP.md         # Este arquivo
```

## Variáveis de Ambiente

### Desenvolvimento (.env)

```env
REACT_APP_GOOGLE_MAPS_API_KEY=sua_chave_desenvolvimento
REACT_APP_API_BASE_URL=http://localhost:8081/api/v1
REACT_APP_DEBUG=true
```

### Produção (.env.production)

```env
REACT_APP_GOOGLE_MAPS_API_KEY=sua_chave_producao
REACT_APP_API_BASE_URL=https://seu-dominio.com/api/v1
REACT_APP_DEBUG=false
```

## Testando a Configuração

1. Configure a chave da API
2. Acesse a página do mapa
3. Verifique o console do navegador
4. O mapa do Google deve carregar sem erros

## Troubleshooting

### Erro: "InvalidKeyMapError"

- Verifique se a chave está correta no arquivo `.env`
- Confirme se a API está ativada no Google Cloud Console
- Verifique as restrições de domínio da chave

### Erro: "Quota Exceeded"

- Verifique o uso da API no Google Cloud Console
- Considere aumentar os limites ou otimizar o uso

### Fallback não funciona

- Verifique se o OpenStreetMap está acessível
- Verifique se a biblioteca Leaflet está carregando

## Próximos Passos

1. Configure uma chave válida da API
2. Teste todas as funcionalidades do mapa
3. Configure restrições de segurança
4. Monitore o uso da API
5. Configure alertas de quota

## Suporte

Para problemas específicos:

1. Verifique os logs do console do navegador
2. Consulte a [documentação oficial do Google Maps](https://developers.google.com/maps/documentation/javascript)
3. Verifique o status das APIs no [Google Cloud Status](https://status.cloud.google.com/)
