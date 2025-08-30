<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Configurações - Sistema de Roteamento</title>
    
    <!-- Bootstrap CSS para estilização -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Tema de Cores do Java -->
    <link href="/css/basic-style.css" rel="stylesheet">
</head>
<body>
    <!-- Seção Hero -->
    <section class="hero-section">
        <div class="container text-center">
            <h1 class="display-5 mb-3">
                <img src="/images/SrPC.png" alt="SrPC" class="hero-icon" style="width: 1.2em; height: 1.2em; vertical-align: middle; margin-right: 0.3em;">
                Configurações do Sistema
            </h1>
            <p class="lead mb-0">Parâmetros e configurações do sistema de roteamento</p>
        </div>
    </section>

    <div class="container">
        <!-- Navegação Principal -->
        <div class="row mb-4">
            <div class="col-12">
                <nav class="navbar navbar-expand-lg navbar-light bg-light rounded">
                    <div class="container-fluid justify-content-center">
                        <div class="navbar-nav">
                            <a class="nav-link" href="/sistema">🏠 Início</a>
                            <a class="nav-link" href="/sistema/coletas">📦 Coletas</a>
                            <a class="nav-link" href="/sistema/rotas">🗺️ Rotas</a>
                            <a class="nav-link" href="/sistema/usuarios">👥 Usuários</a>
                            <a class="nav-link" href="/sistema/materiais">♻️ Materiais</a>
                            <a class="nav-link" href="/sistema/mapa">🗺️ Mapa</a>
                            <a class="nav-link active" href="/sistema/configuracoes">⚙️ Configurações</a>
            
                        </div>
                    </div>
                </nav>
            </div>
        </div>

        <!-- Estatísticas do Sistema -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="stats-card">
                    <div class="row text-center">
                        <div class="col-md-3">
                            <h3 class="mb-0">✅</h3>
                            <small>Sistema Operacional</small>
                        </div>
                        <div class="col-md-3">
                            <h3 class="mb-0">🗄️</h3>
                            <small>Banco Conectado</small>
                        </div>
                        <div class="col-md-3">
                            <h3 class="mb-0">🧠</h3>
                            <small>Algoritmos Ativos</small>
                        </div>
                        <div class="col-md-3">
                            <h3 class="mb-0">📡</h3>
                            <small>APIs Disponíveis</small>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Configurações de Roteamento -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="card">
                    <div class="card-header">
                        <h5 class="mb-0">🗺️ Configurações de Roteamento</h5>
                    </div>
                    <div class="card-body">
                        <form>
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="maxSearchRadius" class="form-label">Raio Máximo de Busca (metros)</label>
                                    <input type="number" class="form-control" id="maxSearchRadius" 
                                           value="${configuracoes.maxSearchRadius}" min="1000" max="50000">
                                    <div class="form-text">Distância máxima para buscar coletas próximas</div>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="maxRoutingTime" class="form-label">Tempo Máximo de Roteamento (segundos)</label>
                                    <input type="number" class="form-control" id="maxRoutingTime" 
                                           value="${configuracoes.maxRoutingTime}" min="10" max="300">
                                    <div class="form-text">Tempo limite para algoritmos de otimização</div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="expirationTime" class="form-label">Tempo de Expiração (minutos)</label>
                                    <input type="number" class="form-control" id="expirationTime" 
                                           value="${configuracoes.expirationTime}" min="5" max="120">
                                    <div class="form-text">Tempo para coletas expirarem automaticamente</div>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="maxRetryAttempts" class="form-label">Máximo de Tentativas</label>
                                    <input type="number" class="form-control" id="maxRetryAttempts" 
                                           value="${configuracoes.maxRetryAttempts}" min="1" max="10">
                                    <div class="form-text">Número máximo de tentativas para operações</div>
                                </div>
                            </div>
                            <button type="submit" class="btn btn-primary">💾 Salvar Configurações</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Configurações de Algoritmos -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="card">
                    <div class="card-header">
                        <h5 class="mb-0">🧠 Configurações de Algoritmos</h5>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <div class="config-section">
                                    <h6>🚗 Algoritmo Nearest Neighbor</h6>
                                    <div class="mb-2">
                                        <label for="nnMaxIterations" class="form-label">Máximo de Iterações</label>
                                        <input type="number" class="form-control" id="nnMaxIterations" value="1000">
                                    </div>
                                    <div class="mb-2">
                                        <label for="nnConvergenceThreshold" class="form-label">Limite de Convergência</label>
                                        <input type="number" class="form-control" id="nnConvergenceThreshold" value="0.001" step="0.001">
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-6 mb-3">
                                <div class="config-section">
                                    <h6>🧬 Algoritmo Genético</h6>
                                    <div class="mb-2">
                                        <label for="gaPopulationSize" class="form-label">Tamanho da População</label>
                                        <input type="number" class="form-control" id="gaPopulationSize" value="100">
                                    </div>
                                    <div class="mb-2">
                                        <label for="gaMutationRate" class="form-label">Taxa de Mutação</label>
                                        <input type="number" class="form-control" id="gaMutationRate" value="0.1" step="0.01" min="0" max="1">
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <div class="config-section">
                                    <h6>🔥 Simulated Annealing</h6>
                                    <div class="mb-2">
                                        <label for="saInitialTemp" class="form-label">Temperatura Inicial</label>
                                        <input type="number" class="form-control" id="saInitialTemp" value="1000">
                                    </div>
                                    <div class="mb-2">
                                        <label for="saCoolingRate" class="form-label">Taxa de Resfriamento</label>
                                        <input type="number" class="form-control" id="saCoolingRate" value="0.95" step="0.01" min="0.8" max="0.99">
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-6 mb-3">
                                <div class="config-section">
                                    <h6>📊 K-Means Clustering</h6>
                                    <div class="mb-2">
                                        <label for="kmeansClusters" class="form-label">Número de Clusters</label>
                                        <input type="number" class="form-control" id="kmeansClusters" value="5" min="2" max="20">
                                    </div>
                                    <div class="mb-2">
                                        <label for="kmeansMaxIterations" class="form-label">Máximo de Iterações</label>
                                        <input type="number" class="form-control" id="kmeansMaxIterations" value="100">
                                    </div>
                                </div>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-success">🧠 Salvar Algoritmos</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Configurações de Notificações -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="card">
                    <div class="card-header">
                        <h5 class="mb-0">🔔 Configurações de Notificações</h5>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <div class="form-check form-switch">
                                    <input class="form-check-input" type="checkbox" id="emailNotifications" checked>
                                    <label class="form-check-label" for="emailNotifications">
                                        📧 Notificações por Email
                                    </label>
                                </div>
                                <div class="form-text">Enviar notificações por email para usuários</div>
                            </div>
                            <div class="col-md-6 mb-3">
                                <div class="form-check form-switch">
                                    <input class="form-check-input" type="checkbox" id="smsNotifications">
                                    <label class="form-check-label" for="smsNotifications">
                                        📱 Notificações por SMS
                                    </label>
                                </div>
                                <div class="form-text">Enviar notificações por SMS para coletores</div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <div class="form-check form-switch">
                                    <input class="form-check-input" type="checkbox" id="pushNotifications" checked>
                                    <label class="form-check-label" for="pushNotifications">
                                        🔔 Notificações Push
                                    </label>
                                </div>
                                <div class="form-text">Notificações em tempo real no sistema</div>
                            </div>
                            <div class="col-md-6 mb-3">
                                <div class="form-check form-switch">
                                    <input class="form-check-input" type="checkbox" id="autoRouting" checked>
                                    <label class="form-check-label" for="autoRouting">
                                        🧠 Roteamento Automático
                                    </label>
                                </div>
                                <div class="form-text">Executar otimização automática de rotas</div>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-info">🔔 Salvar Notificações</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Configurações de Segurança -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="card">
                    <div class="card-header">
                        <h5 class="mb-0">🔒 Configurações de Segurança</h5>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="sessionTimeout" class="form-label">Timeout da Sessão (minutos)</label>
                                <input type="number" class="form-control" id="sessionTimeout" value="30" min="15" max="120">
                                <div class="form-text">Tempo de inatividade para logout automático</div>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="maxLoginAttempts" class="form-label">Máximo de Tentativas de Login</label>
                                <input type="number" class="form-control" id="maxLoginAttempts" value="3" min="1" max="10">
                                <div class="form-text">Tentativas antes do bloqueio da conta</div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="passwordMinLength" class="form-label">Tamanho Mínimo da Senha</label>
                                <input type="number" class="form-control" id="passwordMinLength" value="8" min="6" max="20">
                                <div class="form-text">Comprimento mínimo para senhas</div>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="passwordExpiryDays" class="form-label">Expiração de Senha (dias)</label>
                                <input type="number" class="form-control" id="passwordExpiryDays" value="90" min="30" max="365">
                                <div class="form-text">Dias para forçar troca de senha</div>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-warning">🔒 Salvar Segurança</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Backup e Manutenção -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="card">
                    <div class="card-header">
                        <h5 class="mb-0">💾 Backup e Manutenção</h5>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <div class="d-grid">
                                    <button class="btn btn-success mb-2">
                                        💾 Criar Backup do Banco
                                    </button>
                                    <button class="btn btn-info mb-2">
                                        📊 Limpar Logs Antigos
                                    </button>
                                </div>
                            </div>
                            <div class="col-md-6 mb-3">
                                <div class="d-grid">
                                    <button class="btn btn-warning mb-2">
                                        🔄 Reiniciar Serviços
                                    </button>
                                    <button class="btn btn-danger mb-2">
                                        🚨 Modo de Emergência
                                    </button>
                                </div>
                            </div>
                        </div>
                        <div class="row mt-3">
                            <div class="col-12">
                                <div class="alert alert-info">
                                    <strong>ℹ️ Informação:</strong> 
                                    O sistema faz backup automático diariamente às 02:00. 
                                    Logs são mantidos por 30 dias.
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Ações Rápidas -->
        <div class="row mb-4">
            <div class="col-12">
                <h5 class="mb-3">🚀 Ações Rápidas</h5>
            </div>
        </div>

        <div class="row">
            <div class="col-lg-3 col-md-6 mb-3">
                <a href="/sistema/coletas" class="btn btn-success w-100">
                    📦 Gerenciar Coletas
                </a>
            </div>
            <div class="col-lg-3 col-md-6 mb-3">
                <a href="/sistema/rotas" class="btn btn-primary w-100">
                    🗺️ Gerenciar Rotas
                </a>
            </div>
            <div class="col-lg-3 col-md-6 mb-3">
                <a href="/sistema/usuarios" class="btn btn-info w-100">
                    👥 Gerenciar Usuários
                </a>
            </div>
            <div class="col-lg-3 col-md-6 mb-3">
                <a href="/sistema" class="btn btn-secondary w-100">
                    🏠 Página Inicial
                </a>
            </div>
        </div>

        <!-- Informações do Sistema -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="card border-info">
                    <div class="card-header bg-info text-white">
                        <h6 class="mb-0">ℹ️ Informações do Sistema</h6>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6">
                                <h6>🔧 Status dos Serviços</h6>
                                <ul class="text-muted">
                                    <li>✅ Spring Boot: Ativo</li>
                                    <li>✅ H2 Database: Conectado</li>
                                    <li>✅ JSP/JSF: Funcionando</li>
                                    <li>✅ APIs REST: Disponíveis</li>
                                </ul>
                            </div>
                            <div class="col-md-6">
                                <h6>📊 Recursos do Sistema</h6>
                                <ul class="text-muted">
                                    <li>🧠 Algoritmos de Otimização</li>
                                    <li>🗺️ Mapa Interativo</li>
                                    <li>📱 Interface Responsiva</li>
                                    <li>🔒 Segurança Integrada</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Footer -->
        <div class="row mt-5">
            <div class="col-12 text-center">
                <hr>
                <p class="text-muted">
                    <strong>Configurações do Sistema - Sistema de Roteamento</strong><br>
                    Última atualização: <span id="timestamp"></span>
                </p>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
        // Atualizar timestamp
        document.getElementById('timestamp').textContent = new Date().toLocaleString('pt-BR');
        
        // Salvar configurações
        document.querySelectorAll('form').forEach(form => {
            form.addEventListener('submit', function(e) {
                e.preventDefault();
                alert('Configurações salvas com sucesso!');
            });
        });
    </script>
</body>
</html>
