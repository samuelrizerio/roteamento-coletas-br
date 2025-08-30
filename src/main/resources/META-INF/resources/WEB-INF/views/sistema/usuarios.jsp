<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Usuários - Sistema de Roteamento</title>
    
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
                Gestão de Usuários
            </h1>
            <p class="lead mb-0">Usuários, coletores e permissões do sistema</p>
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
                            <a class="nav-link active" href="/sistema/usuarios">👥 Usuários</a>
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
                            <h3 class="mb-0">${totalUsuarios}</h3>
                            <small>Total de Usuários</small>
                        </div>
                        <div class="col-md-3">
                            <h3 class="mb-0">
                                <c:set var="coletores" value="0" />
                                <c:forEach var="usuario" items="${usuarios}">
                                    <c:if test="${usuario.tipoUsuario == 'COLETOR'}">
                                        <c:set var="coletores" value="${coletores + 1}" />
                                    </c:if>
                                </c:forEach>
                                ${coletores}
                            </h3>
                            <small>Coletores</small>
                        </div>
                        <div class="col-md-3">
                            <h3 class="mb-0">
                                <c:set var="residenciais" value="0" />
                                <c:forEach var="usuario" items="${usuarios}">
                                    <c:if test="${usuario.tipoUsuario == 'RESIDENCIAL'}">
                                        <c:set var="residenciais" value="${residenciais + 1}" />
                                    </c:if>
                                </c:forEach>
                                ${residenciais}
                            </h3>
                            <small>Residenciais</small>
                        </div>
                        <div class="col-md-3">
                            <h3 class="mb-0">
                                <c:set var="comerciais" value="0" />
                                <c:forEach var="usuario" items="${usuarios}">
                                    <c:if test="${usuario.tipoUsuario == 'COMERCIAL'}">
                                        <c:set var="comerciais" value="${comerciais + 1}" />
                                    </c:if>
                                </c:forEach>
                                ${comerciais}
                            </h3>
                            <small>Comerciais</small>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Lista de Usuários -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="card">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <h5 class="mb-0">📋 Lista de Usuários</h5>
                        <button class="btn btn-primary btn-sm">
                            <span>➕ Novo Usuário</span>
                        </button>
                    </div>
                    <div class="card-body">
                        <c:if test="${empty usuarios}">
                            <div class="text-center py-4">
                                <div class="text-muted">
                                    <span style="font-size: 3rem;">📭</span>
                                    <p class="mt-2">Nenhum usuário encontrado</p>
                                </div>
                            </div>
                        </c:if>
                        
                        <c:if test="${not empty usuarios}">
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead class="table-light">
                                        <tr>
                                            <th>👤 Nome</th>
                                            <th>📧 Email</th>
                                            <th>🏷️ Tipo</th>
                                            <th>📊 Status</th>
                                            <th>📅 Criação</th>
                                            <th>⚙️ Ações</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="usuario" items="${usuarios}">
                                            <tr>
                                                <td>
                                                    <strong>${usuario.nome}</strong>
                                                    <br>
                                                    <small class="text-muted">@${usuario.email}</small>
                                                </td>
                                                <td>${usuario.email}</td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${usuario.tipoUsuario == 'COLETOR'}">
                                                            <span class="badge bg-success user-type-badge">🚗 Coletor</span>
                                                        </c:when>
                                                        <c:when test="${usuario.tipoUsuario == 'RESIDENCIAL'}">
                                                            <span class="badge bg-primary user-type-badge">🏠 Residencial</span>
                                                        </c:when>
                                                        <c:when test="${usuario.tipoUsuario == 'COMERCIAL'}">
                                                            <span class="badge bg-warning text-dark user-type-badge">🏢 Comercial</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-secondary user-type-badge">❓ ${usuario.tipoUsuario}</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${usuario.status == 'ATIVO'}">
                                                            <span class="badge bg-success status-badge">✅ Ativo</span>
                                                        </c:when>
                                                        <c:when test="${usuario.status == 'INATIVO'}">
                                                            <span class="badge bg-danger status-badge">❌ Inativo</span>
                                                        </c:when>
                                                        <c:when test="${usuario.status == 'BLOQUEADO'}">
                                                            <span class="badge bg-warning text-dark status-badge">⚠️ Bloqueado</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-secondary status-badge">❓ ${usuario.status}</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <small class="text-muted">
                                                        <c:if test="${not empty usuario.dataCriacao}">
                                                            ${usuario.dataCriacao}
                                                        </c:if>
                                                    </small>
                                                </td>
                                                <td>
                                                    <div class="btn-group btn-group-sm" role="group">
                                                        <button class="btn btn-outline-primary btn-sm" title="Editar">
                                                            ✏️
                                                        </button>
                                                        <button class="btn btn-outline-info btn-sm" title="Visualizar">
                                                            👁️
                                                        </button>
                                                        <button class="btn btn-outline-danger btn-sm" title="Excluir">
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
                                <h6>✅ Coletores Disponíveis</h6>
                                <p class="text-muted">
                                    O sistema possui <strong>coletores ativos</strong> para criar rotas de coleta.
                                    As rotas são geradas automaticamente pelos algoritmos de otimização.
                                </p>
                            </div>
                            <div class="col-md-6">
                                <h6>🛣️ Criação de Rotas</h6>
                                <p class="text-muted">
                                    Para criar rotas, acesse a seção <strong>Rotas</strong>.
                                    O sistema automaticamente atribuirá coletores disponíveis.
                                </p>
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

        <!-- Footer -->
        <div class="row mt-5">
            <div class="col-12 text-center">
                <hr>
                <p class="text-muted">
                    <strong>Gestão de Usuários - Sistema de Roteamento</strong><br>
                    Total de usuários: ${totalUsuarios} | Última atualização: <span id="timestamp"></span>
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
