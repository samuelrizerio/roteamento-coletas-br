# Frontend - Sistema de Roteamento Programado de Coletas

Interface web moderna para gerenciamento do sistema de roteamento programado de coletas.

## Tecnologias Utilizadas

- **React 18** - Biblioteca para interfaces de usuário
- **TypeScript** - Tipagem estática para JavaScript
- **Material-UI (MUI)** - Componentes de interface modernos
- **React Router** - Navegação entre páginas
- **React Query** - Gerenciamento de estado e cache
- **Leaflet** - Mapas interativos
- **Recharts** - Gráficos e visualizações
- **Axios** - Cliente HTTP para APIs
- **Notistack** - Notificações toast
- **Framer Motion** - Animações

## Características

### Interface Moderna

- **Modo Escuro** por padrão com opção de alternar
- **Design Responsivo** para desktop e mobile
- **Tema Personalizado** com cores do sistema
- **Animações Suaves** para melhor UX

### Funcionalidades de Mapa

- **Mapa Interativo** com Leaflet
- **Marcadores Dinâmicos** para usuários, coletas e rotas
- **Controles de Camadas** para mostrar/ocultar elementos
- **Popups Informativos** com detalhes dos pontos
- **Linhas de Rota** conectando pontos

### Dashboard Completo

- **Cards de Estatísticas** em tempo real
- **Gráficos Interativos** com Recharts
- **Métricas de Performance** do sistema
- **Listas de Atividades** recentes

### Gerenciamento Completo

- **CRUD Completo** para todas as entidades
- **Tabelas Avançadas** com DataGrid
- **Formulários Intuitivos** com validação
- **Filtros e Busca** em tempo real

## Estrutura do Projeto

```
frontend/
├── public/
│   └── index.html
├── src/
│   ├── components/
│   │   ├── Dashboard/
│   │   │   └── DashboardComponent.tsx
│   │   ├── Layout/
│   │   │   └── AppLayout.tsx
│   │   └── Map/
│   │       └── MapComponent.tsx
│   ├── pages/
│   │   ├── UsuariosPage.tsx
│   │   ├── MateriaisPage.tsx
│   │   ├── ColetasPage.tsx
│   │   ├── RotasPage.tsx
│   │   └── ConfiguracoesPage.tsx
│   ├── services/
│   │   └── api.ts
│   ├── theme/
│   │   └── index.ts
│   ├── types/
│   │   └── index.ts
│   ├── App.tsx
│   └── index.tsx
├── package.json
└── README.md
```

## Instalação e Execução

### Pré-requisitos

- Node.js 16+
- npm ou yarn
- Backend Spring Boot rodando na porta 8081

### Instalação

```bash
# Navegar para o diretório do frontend
cd frontend

# Instalar dependências
npm install

# Executar em modo de desenvolvimento
npm start
```

### Scripts Disponíveis

```bash
# Executar em desenvolvimento
npm start

# Build para produção
npm run build

# Executar testes
npm test

# Ejetar configurações (não recomendado)
npm run eject
```

## Acesso

Após a instalação, a aplicação estará disponível em:

- **URL:** <http://localhost:3000>
- **Backend:** <http://localhost:8081/api/v1>

## Funcionalidades por Página

### Dashboard

- Visão geral do sistema
- Estatísticas em tempo real
- Gráficos de materiais e coletas
- Lista de atividades recentes

### Usuários

- Listagem com filtros
- Criação e edição de usuários
- Diferentes tipos (Residencial, Comercial, Coletor)
- Gerenciamento de status

### Materiais

- Catálogo de materiais recicláveis
- Categorização por tipo
- Configuração de valores
- Controle de aceitação para coleta

### Coletas

- Solicitações de coleta
- Acompanhamento de status
- Atribuição de coletores
- Cálculo de valores estimados

### Rotas

- Criação de rotas otimizadas
- Atribuição de coletores
- Acompanhamento de execução
- Cálculo de distâncias e tempos

### Mapa

- Visualização geográfica
- Marcadores de pontos
- Linhas de rota
- Controles de camadas

### Configurações

- Configurações do sistema
- Parâmetros de roteamento
- Configurações de segurança
- Configurações de cache e logging

## Tema e Design

### Cores Principais

- **Primária:** Verde (#00c853) - Sustentabilidade
- **Secundária:** Azul (#2196f3) - Tecnologia
- **Sucesso:** Verde (#4caf50)
- **Aviso:** Laranja (#ff9800)
- **Erro:** Vermelho (#f44336)

### Tema Escuro

O sistema utiliza um tema escuro otimizado para melhor experiência do usuário:

- **Background:** #0a0a0a
- **Surface:** #1a1a1a
- **Cards:** #2a2a2a
- **Text:** #ffffff

## Integração com Backend

### Endpoints Utilizados

- `/api/v1/usuarios` - Gerenciamento de usuários
- `/api/v1/materiais` - Catálogo de materiais
- `/api/v1/coletas` - Solicitações de coleta
- `/api/v1/rotas` - Rotas de coleta
- `/api/v1/dashboard/*` - Dados do dashboard

### Configuração de Proxy

O frontend está configurado para fazer proxy das requisições para o backend na porta 8081.

## Deploy

### Build para Produção

```bash
npm run build
```

### Servidor de Produção

```bash
# Instalar serve globalmente
npm install -g serve

# Servir build de produção
serve -s build -l 3000
```

## Performance

### Otimizações Implementadas

- **Code Splitting** automático
- **Lazy Loading** de componentes
- **React Query** para cache inteligente
- **Virtualização** de listas grandes
- **Compressão** de assets

### Métricas Esperadas

- **First Contentful Paint:** < 2s
- **Largest Contentful Paint:** < 3s
- **Time to Interactive:** < 4s

## Desenvolvimento

### Estrutura de Componentes

- **Componentes Funcionais** com hooks
- **TypeScript** para tipagem
- **Props Interface** bem definidas
- **Custom Hooks** para lógica reutilizável

### Padrões de Código

- **ESLint** para linting
- **Prettier** para formatação
- **Conventional Commits** para commits
- **Component Storybook** (futuro)

## Testes

### Estrutura de Testes

```bash
# Executar todos os testes
npm test

# Executar testes com coverage
npm test -- --coverage

# Executar testes em modo watch
npm test -- --watch
```

## Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature
3. Commit suas mudanças
4. Push para a branch
5. Abra um Pull Request

## Licença

Este projeto está sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.

## Suporte

Para suporte e dúvidas:

- Abra uma issue no GitHub
- Consulte a documentação do backend
- Entre em contato com a equipe de desenvolvimento
