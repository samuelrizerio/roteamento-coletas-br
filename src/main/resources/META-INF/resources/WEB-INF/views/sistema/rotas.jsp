<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Rotas - Sistema de Roteamento</title>
    
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
                Gestão de Rotas
            </h1>
            <p class="lead mb-0">Rotas otimizadas para coleta de materiais</p>
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
                            <a class="nav-link active" href="/sistema/rotas">🗺️ Rotas</a>
                            <a class="nav-link" href="/sistema/usuarios">👥 Usuários</a>
                            <a class="nav-link" href="/sistema/materiais">♻️ Materiais</a>
                            <a class="nav-link" href="/sistema/mapa">🗺️ Mapa</a>
                            <a class="nav-link" href="/sistema/configuracoes">⚙️ Configurações</a>
            
                        </div>
                    </div>
                </nav>
            </div>
        </div>

        <!-- Estatísticas -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="stats-card">
                    <div class="row text-center">
                        <div class="col-md-3">
                            <h3 class="mb-0">${totalRotas}</h3>
                            <small>Total de Rotas</small>
                        </div>
                        <div class="col-md-3">
                            <h3 class="mb-0">
                                <c:set var="rotasAtivas" value="0" />
                                <c:forEach var="rota" items="${rotas}">
                                    <c:if test="${rota.status == 'ATIVA'}">
                                        <c:set var="rotasAtivas" value="${rotasAtivas + 1}" />
                                    </c:if>
                                </c:forEach>
                                ${rotasAtivas}
                            </h3>
                            <small>Rotas Ativas</small>
                        </div>
                        <div class="col-md-3">
                            <h3 class="mb-0">
                                <c:set var="rotasFinalizadas" value="0" />
                                <c:forEach var="rota" items="${rotas}">
                                    <c:if test="${rota.status == 'FINALIZADA'}">
                                        <c:set var="rotasFinalizadas" value="${rotasFinalizadas + 1}" />
                                    </c:if>
                                </c:forEach>
                                ${rotasFinalizadas}
                            </h3>
                            <small>Finalizadas</small>
                        </div>
                        <div class="col-md-3">
                            <h3 class="mb-0">
                                <c:set var="rotasCanceladas" value="0" />
                                <c:forEach var="rota" items="${rotas}">
                                    <c:if test="${rota.status == 'CANCELADA'}">
                                        <c:set var="rotasCanceladas" value="${rotasCanceladas + 1}" />
                                    </c:if>
                                </c:forEach>
                                ${rotasCanceladas}
                            </h3>
                            <small>Canceladas</small>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Otimização Automática -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="optimization-card">
                    <div class="row align-items-center">
                        <div class="col-md-8">
                            <h5 class="mb-2">🚀 Otimização Automática de Rotas</h5>
                            <p class="mb-0">
                                O sistema possui algoritmos avançados para otimizar rotas automaticamente.
                                Clique em "Otimizar Rotas" para gerar rotas otimizadas para as coletas pendentes.
                            </p>
                        </div>
                        <div class="col-md-4 text-center">
                            <button class="btn btn-warning btn-lg">
                                <span>🧠 Otimizar Rotas</span>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Filtros -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="filter-section">
                    <h5 class="mb-3">🔍 Filtros</h5>
                    <form method="GET" action="/sistema/rotas" class="row g-3">
                        <div class="col-md-4">
                            <label for="status" class="form-label">Status</label>
                            <select class="form-select" id="status" name="status">
                                <option value="">Todos os Status</option>
                                <option value="ATIVA" ${statusFiltro == 'ATIVA' ? 'selected' : ''}>Ativa</option>
                                <option value="FINALIZADA" ${statusFiltro == 'FINALIZADA' ? 'selected' : ''}>Finalizada</option>
                                <option value="CANCELADA" ${statusFiltro == 'CANCELADA' ? 'selected' : ''}>Cancelada</option>
                                <option value="PLANEJADA" ${statusFiltro == 'PLANEJADA' ? 'selected' : ''}>Planejada</option>
                            </select>
                        </div>
                        <div class="col-md-4">
                            <label for="coletor" class="form-label">Coletor</label>
                            <select class="form-select" id="coletor" name="coletor">
                                <option value="">Todos os Coletores</option>
                                <option value="coletor1">João Silva</option>
                                <option value="coletor2">Maria Santos</option>
                                <option value="coletor3">Pedro Oliveira</option>
                            </select>
                        </div>
                        <div class="col-md-4">
                            <label class="form-label">&nbsp;</label>
                            <div class="d-grid">
                                <button type="submit" class="btn btn-primary">🔍 Filtrar</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Lista de Rotas -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="card">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <h5 class="mb-0">📋 Lista de Rotas</h5>
                        <div>
                            <button class="btn btn-success btn-sm me-2">
                                <span>➕ Nova Rota</span>
                            </button>
                            <button class="btn btn-warning btn-sm me-2">
                                <span>🧠 Otimizar</span>
                            </button>
                            <button class="btn btn-info btn-sm">
                                <span>📊 Relatórios</span>
                            </button>
                        </div>
                    </div>
                    <div class="card-body">
                        <c:if test="${empty rotas}">
                            <div class="text-center py-4">
                                <div class="text-muted">
                                    <span style="font-size: 3rem;">🛣️</span>
                                    <p class="mt-2">Nenhuma rota encontrada</p>
                                    <p class="text-muted">
                                        Crie uma nova rota ou use a otimização automática para gerar rotas baseadas nas coletas pendentes.
                                    </p>
                                    <div class="mt-3">
                                        <button class="btn btn-warning btn-lg">
                                            🧠 Gerar Rotas Automaticamente
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                        
                        <c:if test="${not empty rotas}">
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead class="table-light">
                                        <tr>
                                            <th>🛣️ Rota</th>
                                            <th>👤 Coletor</th>
                                            <th>📦 Coletas</th>
                                            <th>📏 Distância</th>
                                            <th>⏱️ Tempo</th>
                                            <th>💰 Valor</th>
                                            <th>📊 Status</th>
                                            <th>⚙️ Ações</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="rota" items="${rotas}">
                                            <tr>
                                                <td>
                                                    <strong>Rota ${rota.id}</strong>
                                                    <br>
                                                    <small class="text-muted">${rota.nome}</small>
                                                </td>
                                                <td>
                                                    <c:if test="${not empty rota.coletor}">
                                                        <strong>${rota.coletor.nome}</strong>
                                                        <br>
                                                        <small class="text-muted">${rota.coletor.email}</small>
                                                    </c:if>
                                                    <c:if test="${empty rota.coletor}">
                                                        <span class="text-muted">Não atribuído</span>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <c:if test="${not empty rota.coletas}">
                                                        <span class="badge bg-info">${rota.coletas.size()} coletas</span>
                                                    </c:if>
                                                    <c:if test="${empty rota.coletas}">
                                                        <span class="text-muted">0 coletas</span>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <c:if test="${not empty rota.distancia}">
                                                        <strong>${rota.distancia} km</strong>
                                                    </c:if>
                                                    <c:if test="${empty rota.distancia}">
                                                        <span class="text-muted">--</span>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <c:if test="${not empty rota.tempoEstimado}">
                                                        <strong>${rota.tempoEstimado} min</strong>
                                                    </c:if>
                                                    <c:if test="${empty rota.tempoEstimado}">
                                                        <span class="text-muted">--</span>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <c:if test="${not empty rota.valorTotal}">
                                                        <strong>R$ ${rota.valorTotal}</strong>
                                                    </c:if>
                                                    <c:if test="${empty rota.valorTotal}">
                                                        <span class="text-muted">--</span>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${rota.status == 'PLANEJADA'}">
                                                            <span class="badge bg-secondary status-badge">📋 Planejada</span>
                                                        </c:when>
                                                        <c:when test="${rota.status == 'ATIVA'}">
                                                            <span class="badge bg-success status-badge">🚗 Ativa</span>
                                                        </c:when>
                                                        <c:when test="${rota.status == 'FINALIZADA'}">
                                                            <span class="badge bg-primary status-badge">✅ Finalizada</span>
                                                        </c:when>
                                                        <c:when test="${rota.status == 'CANCELADA'}">
                                                            <span class="badge bg-danger status-badge">❌ Cancelada</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-secondary status-badge">❓ ${rota.status}</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <div class="btn-group btn-group-sm" role="group">
                                                        <c:if test="${rota.status == 'PLANEJADA'}">
                                                            <button class="btn btn-outline-success btn-sm" title="Iniciar">
                                                                🚗
                                                            </button>
                                                        </c:if>
                                                        <c:if test="${rota.status == 'ATIVA'}">
                                                            <button class="btn btn-outline-primary btn-sm" title="Finalizar">
                                                                ✅
                                                            </button>
                                                        </c:if>
                                                        <button class="btn btn-outline-info btn-sm" title="Visualizar">
                                                            👁️
                                                        </button>
                                                        <button class="btn btn-outline-primary btn-sm" title="Editar">
                                                            ✏️
                                                        </button>
                                                        <button class="btn btn-outline-danger btn-sm" title="Cancelar">
                                                            🗑️
                                                        </button>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>

        <!-- Algoritmos de Otimização -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="card">
                    <div class="card-header">
                        <h5 class="mb-0">🧠 Algoritmos de Otimização</h5>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-lg-4 col-md-6 mb-3">
                                <div class="card border-primary h-100">
                                    <div class="card-body text-center">
                                        <h6 class="card-title">🚗 Nearest Neighbor</h6>
                                        <p class="card-text">Algoritmo guloso que sempre escolhe o ponto mais próximo.</p>
                                        <button class="btn btn-outline-primary btn-sm">Executar</button>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-4 col-md-6 mb-3">
                                <div class="card border-success h-100">
                                    <div class="card-body text-center">
                                        <h6 class="card-title">🧬 Algoritmo Genético</h6>
                                        <p class="card-text">Otimização evolutiva baseada em seleção natural.</p>
                                        <button class="btn btn-outline-success btn-sm">Executar</button>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-4 col-md-6 mb-3">
                                <div class="card border-warning h-100">
                                    <div class="card-body text-center">
                                        <h6 class="card-title">🔥 Simulated Annealing</h6>
                                        <p class="card-text">Otimização global com escape de mínimos locais.</p>
                                        <button class="btn btn-outline-warning btn-sm">Executar</button>
                                    </div>
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
                <a href="/sistema/usuarios" class="btn btn-info w-100">
                    👥 Gerenciar Usuários
                </a>
            </div>
            <div class="col-lg-3 col-md-6 mb-3">
                <a href="/sistema/mapa" class="btn btn-warning w-100">
                    🗺️ Ver Mapa
                </a>
            </div>
            <div class="col-lg-3 col-md-6 mb-3">
                <a href="/sistema" class="btn btn-secondary w-100">
                    🏠 Página Inicial
                </a>
            </div>
        </div>

        <!-- Informações Importantes -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="card border-info">
                    <div class="card-header bg-info text-white">
                        <h6 class="mb-0">ℹ️ Informações Importantes</h6>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6">
                                <h6>🛣️ Status das Rotas</h6>
                                <ul class="text-muted">
                                    <li><strong>Planejada:</strong> Rota criada, aguardando início</li>
                                    <li><strong>Ativa:</strong> Coletor em execução</li>
                                    <li><strong>Finalizada:</strong> Rota concluída com sucesso</li>
                                    <li><strong>Cancelada:</strong> Rota cancelada</li>
                                </ul>
                            </div>
                            <div class="col-md-6">
                                <h6>🧠 Otimização Automática</h6>
                                <p class="text-muted">
                                    O sistema pode gerar rotas otimizadas automaticamente baseadas nas coletas pendentes.
                                    Use os algoritmos de otimização para criar rotas eficientes.
                                </p>
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
                    <strong>Gestão de Rotas - Sistema de Roteamento</strong><br>
                    Total de rotas: ${totalRotas} | Última atualização: <span id="timestamp"></span>
                </p>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
        // Atualizar timestamp
        document.getElementById('timestamp').textContent = new Date().toLocaleString('pt-BR');
    </script>
</body>
</html>
