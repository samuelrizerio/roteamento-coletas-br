<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Coletas - Sistema de Roteamento</title>
    
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
                Gestão de Coletas
            </h1>
            <p class="lead mb-0">Solicitações de coleta e acompanhamento de status</p>
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
                            <a class="nav-link active" href="/sistema/coletas">📦 Coletas</a>
                            <a class="nav-link" href="/sistema/rotas">🗺️ Rotas</a>
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
                            <h3 class="mb-0">${totalColetas}</h3>
                            <small>Total de Coletas</small>
                        </div>
                        <div class="col-md-3">
                            <h3 class="mb-0">
                                <c:set var="coletasPendentes" value="0" />
                                <c:forEach var="coleta" items="${coletas}">
                                    <c:if test="${coleta.status == 'SOLICITADA' || coleta.status == 'EM_ANALISE'}">
                                        <c:set var="coletasPendentes" value="${coletasPendentes + 1}" />
                                    </c:if>
                                </c:forEach>
                                ${coletasPendentes}
                            </h3>
                            <small>Pendentes</small>
                        </div>
                        <div class="col-md-3">
                            <h3 class="mb-0">
                                <c:set var="coletasEmRota" value="0" />
                                <c:forEach var="coleta" items="${coletas}">
                                    <c:if test="${coleta.status == 'EM_ROTA'}">
                                        <c:set var="coletasEmRota" value="${coletasEmRota + 1}" />
                                    </c:if>
                                </c:forEach>
                                ${coletasEmRota}
                            </h3>
                            <small>Em Rota</small>
                        </div>
                        <div class="col-md-3">
                            <h3 class="mb-0">
                                <c:set var="coletasFinalizadas" value="0" />
                                <c:forEach var="coleta" items="${coletas}">
                                    <c:if test="${coleta.status == 'COLETADA'}">
                                        <c:set var="coletasFinalizadas" value="${coletasFinalizadas + 1}" />
                                    </c:if>
                                </c:forEach>
                                ${coletasFinalizadas}
                            </h3>
                            <small>Finalizadas</small>
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
                    <form method="GET" action="/sistema/coletas" class="row g-3">
                        <div class="col-md-4">
                            <label for="status" class="form-label">Status</label>
                            <select class="form-select" id="status" name="status">
                                <option value="">Todos os Status</option>
                                <option value="SOLICITADA" ${statusFiltro == 'SOLICITADA' ? 'selected' : ''}>Solicitada</option>
                                <option value="EM_ANALISE" ${statusFiltro == 'EM_ANALISE' ? 'selected' : ''}>Em Análise</option>
                                <option value="ACEITA" ${statusFiltro == 'ACEITA' ? 'selected' : ''}>Aceita</option>
                                <option value="EM_ROTA" ${statusFiltro == 'EM_ROTA' ? 'selected' : ''}>Em Rota</option>
                                <option value="COLETADA" ${statusFiltro == 'COLETADA' ? 'selected' : ''}>Coletada</option>
                                <option value="CANCELADA" ${statusFiltro == 'CANCELADA' ? 'selected' : ''}>Cancelada</option>
                                <option value="REJEITADA" ${statusFiltro == 'REJEITADA' ? 'selected' : ''}>Rejeitada</option>
                            </select>
                        </div>
                        <div class="col-md-4">
                            <label for="material" class="form-label">Material</label>
                            <select class="form-select" id="material" name="material">
                                <option value="">Todos os Materiais</option>
                                <option value="PAPEL">Papel</option>
                                <option value="PLASTICO">Plástico</option>
                                <option value="VIDRO">Vidro</option>
                                <option value="METAL">Metal</option>
                                <option value="ORGANICO">Orgânico</option>
                                <option value="ELETRONICO">Eletrônico</option>
                                <option value="PERIGOSO">Perigoso</option>
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

        <!-- Lista de Coletas -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="card">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <h5 class="mb-0">📋 Lista de Coletas</h5>
                        <div>
                            <button class="btn btn-success btn-sm me-2">
                                <span>➕ Nova Coleta</span>
                            </button>
                            <button class="btn btn-info btn-sm">
                                <span>📊 Relatórios</span>
                            </button>
                        </div>
                    </div>
                    <div class="card-body">
                        <c:if test="${empty coletas}">
                            <div class="text-center py-4">
                                <div class="text-muted">
                                    <span style="font-size: 3rem;">📭</span>
                                    <p class="mt-2">Nenhuma coleta encontrada</p>
                                    <p class="text-muted">Crie uma nova coleta ou ajuste os filtros</p>
                                </div>
                            </div>
                        </c:if>
                        
                        <c:if test="${not empty coletas}">
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead class="table-light">
                                        <tr>
                                            <th>📦 Coleta</th>
                                            <th>👤 Usuário</th>
                                            <th>♻️ Material</th>
                                            <th>⚖️ Peso</th>
                                            <th>💰 Valor</th>
                                            <th>📊 Status</th>
                                            <th>🗺️ Endereço</th>
                                            <th>⚙️ Ações</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="coleta" items="${coletas}">
                                            <tr>
                                                <td>
                                                    <strong>${coleta.observacoes}</strong>
                                                    <br>
                                                    <small class="text-muted">ID: ${coleta.id}</small>
                                                </td>
                                                <td>
                                                                                    <strong>${coleta.nomeUsuario}</strong>
                                <br>
                                <small class="text-muted">${coleta.emailUsuario}</small>
                                                </td>
                                                <td>
                                                                                    <span class="badge bg-secondary">Material</span>
                                                </td>
                                                <td>
                                                    <strong>${coleta.pesoEstimado} kg</strong>
                                                </td>
                                                <td>
                                                    <strong>R$ --</strong>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${coleta.status == 'SOLICITADA'}">
                                                            <span class="badge bg-warning text-dark status-badge">⏳ Solicitada</span>
                                                        </c:when>
                                                        <c:when test="${coleta.status == 'EM_ANALISE'}">
                                                            <span class="badge bg-info status-badge">🔍 Em Análise</span>
                                                        </c:when>
                                                        <c:when test="${coleta.status == 'ACEITA'}">
                                                            <span class="badge bg-success status-badge">✅ Aceita</span>
                                                        </c:when>
                                                        <c:when test="${coleta.status == 'EM_ROTA'}">
                                                            <span class="badge bg-primary status-badge">🚗 Em Rota</span>
                                                        </c:when>
                                                        <c:when test="${coleta.status == 'COLETADA'}">
                                                            <span class="badge bg-success status-badge">🎉 Coletada</span>
                                                        </c:when>
                                                        <c:when test="${coleta.status == 'CANCELADA'}">
                                                            <span class="badge bg-danger status-badge">❌ Cancelada</span>
                                                        </c:when>
                                                        <c:when test="${coleta.status == 'REJEITADA'}">
                                                            <span class="badge bg-danger status-badge">🚫 Rejeitada</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-secondary status-badge">❓ ${coleta.status}</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <small class="text-muted">
                                                        ${coleta.endereco}
                                                    </small>
                                                </td>
                                                <td>
                                                    <div class="btn-group btn-group-sm" role="group">
                                                        <c:if test="${coleta.status == 'SOLICITADA' || coleta.status == 'EM_ANALISE'}">
                                                            <button class="btn btn-outline-success btn-sm" title="Aceitar">
                                                                ✅
                                                            </button>
                                                        </c:if>
                                                        <c:if test="${coleta.status == 'ACEITA'}">
                                                            <button class="btn btn-outline-primary btn-sm" title="Iniciar Rota">
                                                                🚗
                                                            </button>
                                                        </c:if>
                                                        <c:if test="${coleta.status == 'EM_ROTA'}">
                                                            <button class="btn btn-outline-success btn-sm" title="Finalizar">
                                                                🎉
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

        <!-- Ações Rápidas -->
        <div class="row mb-4">
            <div class="col-12">
                <h5 class="mb-3">🚀 Ações Rápidas</h5>
            </div>
        </div>

        <div class="row">
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
                <a href="/sistema/materiais" class="btn btn-warning w-100">
                    ♻️ Gerenciar Materiais
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
                                <h6>📦 Status das Coletas</h6>
                                <ul class="text-muted">
                                    <li><strong>Solicitada:</strong> Aguardando análise</li>
                                    <li><strong>Em Análise:</strong> Sistema analisando</li>
                                    <li><strong>Aceita:</strong> Coletor designado</li>
                                    <li><strong>Em Rota:</strong> Coletor a caminho</li>
                                    <li><strong>Coletada:</strong> Finalizada com sucesso</li>
                                </ul>
                            </div>
                            <div class="col-md-6">
                                <h6>🛣️ Próximos Passos</h6>
                                <p class="text-muted">
                                    Após criar coletas, o sistema pode gerar rotas otimizadas automaticamente.
                                    Acesse a seção <strong>Rotas</strong> para visualizar e gerenciar as rotas criadas.
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
                    <strong>Gestão de Coletas - Sistema de Roteamento</strong><br>
                    Total de coletas: ${totalColetas} | Última atualização: <span id="timestamp"></span>
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
