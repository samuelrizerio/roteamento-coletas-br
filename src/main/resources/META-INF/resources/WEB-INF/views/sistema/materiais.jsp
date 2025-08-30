<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Materiais - Sistema de Roteamento</title>
    
    <!-- Bootstrap CSS para estilização -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Tema de Cores do Java -->
    <link href="/css/java-theme.css" rel="stylesheet">
    <link href="/css/java-theme-additional.css" rel="stylesheet">
</head>
<body>
    <!-- Seção Hero -->
    <section class="hero-section">
        <div class="container text-center">
            <h1 class="display-5 mb-3">♻️ Catálogo de Materiais</h1>
            <p class="lead mb-0">Materiais recicláveis e valores por categoria</p>
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
                            <a class="nav-link active" href="/sistema/materiais">♻️ Materiais</a>
                            <a class="nav-link" href="/sistema/mapa">🗺️ Mapa</a>
                            <a class="nav-link" href="/sistema/configuracoes">⚙️ Configurações</a>
                            <a class="nav-link" href="/sistema/documentacao">📚 Documentação</a>
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
                            <h3 class="mb-0">${totalMateriais}</h3>
                            <small>Total de Materiais</small>
                        </div>
                        <div class="col-md-3">
                            <h3 class="mb-0">
                                <c:set var="materiaisAtivos" value="0" />
                                <c:forEach var="material" items="${materiais}">
                                    <c:if test="${material.aceitoParaColeta}">
                                        <c:set var="materiaisAtivos" value="${materiaisAtivos + 1}" />
                                    </c:if>
                                </c:forEach>
                                ${materiaisAtivos}
                            </h3>
                            <small>Materiais Ativos</small>
                        </div>
                        <div class="col-md-3">
                            <h3 class="mb-0">
                                <c:set var="categorias" value="0" />
                                <c:forEach var="categoria" items="${['PAPEL', 'PLASTICO', 'VIDRO', 'METAL', 'ORGANICO', 'ELETRONICO', 'PERIGOSO']}">
                                    <c:set var="temCategoria" value="false" />
                                    <c:forEach var="material" items="${materiais}">
                                        <c:if test="${material.categoria eq categoria}">
                                            <c:set var="temCategoria" value="true" />
                                        </c:if>
                                    </c:forEach>
                                    <c:if test="${temCategoria}">
                                        <c:set var="categorias" value="${categorias + 1}" />
                                    </c:if>
                                </c:forEach>
                                ${categorias}
                            </h3>
                            <small>Categorias</small>
                        </div>
                        <div class="col-md-3">
                            <h3 class="mb-0">
                                <c:set var="precoMedio" value="0.0" />
                                <c:set var="totalPreco" value="0.0" />
                                <c:forEach var="material" items="${materiais}">
                                    <c:if test="${material.aceitoParaColeta}">
                                        <c:set var="totalPreco" value="${totalPreco + material.valorPorQuilo}" />
                                    </c:if>
                                </c:forEach>
                                <c:if test="${materiaisAtivos > 0}">
                                    <c:set var="precoMedio" value="${totalPreco / materiaisAtivos}" />
                                </c:if>
                                R$ ${precoMedio}
                            </h3>
                            <small>Preço Médio/kg</small>
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
                    <form method="GET" action="/sistema/materiais" class="row g-3">
                        <div class="col-md-4">
                            <label for="categoria" class="form-label">Categoria</label>
                            <select class="form-select" id="categoria" name="categoria">
                                <option value="">Todas as Categorias</option>
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
                            <label for="status" class="form-label">Status</label>
                            <select class="form-select" id="status" name="status">
                                <option value="">Todos os Status</option>
                                <option value="true">Ativo</option>
                                <option value="false">Inativo</option>
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

        <!-- Lista de Materiais -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="card">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <h5 class="mb-0">📋 Lista de Materiais</h5>
                        <div>
                            <button class="btn btn-success btn-sm me-2">
                                <span>➕ Novo Material</span>
                            </button>
                            <button class="btn btn-info btn-sm me-2">
                                <span>📊 Relatórios</span>
                            </button>
                            <button class="btn btn-warning btn-sm">
                                <span>📈 Estatísticas</span>
                            </button>
                        </div>
                    </div>
                    <div class="card-body">
                        <c:if test="${empty materiais}">
                            <div class="text-center py-4">
                                <div class="text-muted">
                                    <span style="font-size: 3rem;">📭</span>
                                    <p class="mt-2">Nenhum material encontrado</p>
                                    <p class="text-muted">Crie um novo material ou ajuste os filtros</p>
                                </div>
                            </div>
                        </c:if>
                        
                        <c:if test="${not empty materiais}">
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead class="table-light">
                                        <tr>
                                            <th>♻️ Material</th>
                                            <th>🏷️ Categoria</th>
                                            <th>💰 Preço/kg</th>
                                            <th>🎨 Cor</th>
                                            <th>📝 Descrição</th>
                                            <th>📊 Status</th>
                                            <th>⚙️ Ações</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="material" items="${materiais}">
                                            <tr>
                                                <td>
                                                    <strong>${material.nome}</strong>
                                                    <br>
                                                    <small class="text-muted">ID: ${material.id}</small>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${material.categoria == 'PAPEL'}">
                                                            <span class="badge bg-primary category-badge">📄 Papel</span>
                                                        </c:when>
                                                        <c:when test="${material.categoria == 'PLASTICO'}">
                                                            <span class="badge bg-info category-badge">🥤 Plástico</span>
                                                        </c:when>
                                                        <c:when test="${material.categoria == 'VIDRO'}">
                                                            <span class="badge bg-success category-badge">🥃 Vidro</span>
                                                        </c:when>
                                                        <c:when test="${material.categoria == 'METAL'}">
                                                            <span class="badge bg-secondary category-badge">🔩 Metal</span>
                                                        </c:when>
                                                        <c:when test="${material.categoria == 'ORGANICO'}">
                                                            <span class="badge bg-warning text-dark category-badge">🌱 Orgânico</span>
                                                        </c:when>
                                                        <c:when test="${material.categoria == 'ELETRONICO'}">
                                                            <span class="badge bg-danger category-badge">📱 Eletrônico</span>
                                                        </c:when>
                                                        <c:when test="${material.categoria == 'PERIGOSO'}">
                                                            <span class="badge bg-dark category-badge">⚠️ Perigoso</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-secondary category-badge">❓ ${material.categoria}</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <span class="price-highlight">R$ ${material.valorPorQuilo}</span>
                                                </td>
                                                <td>
                                                    <c:if test="${not empty material.corIdentificacao}">
                                                        <span class="badge" style="background-color: ${material.corIdentificacao.toLowerCase()}; color: white;">
                                                            ${material.corIdentificacao}
                                                        </span>
                                                    </c:if>
                                                    <c:if test="${empty material.corIdentificacao}">
                                                        <span class="text-muted">--</span>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <small class="text-muted">
                                                        <c:if test="${not empty material.descricao}">
                                                            ${material.descricao}
                                                        </c:if>
                                                        <c:if test="${empty material.descricao}">
                                                            Sem descrição
                                                        </c:if>
                                                    </small>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${material.aceitoParaColeta}">
                                                            <span class="badge bg-success">✅ Ativo</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-danger">❌ Inativo</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <div class="btn-group btn-group-sm" role="group">
                                                        <button class="btn btn-outline-info btn-sm" title="Visualizar">
                                                            👁️
                                                        </button>
                                                        <button class="btn btn-outline-primary btn-sm" title="Editar">
                                                            ✏️
                                                        </button>
                                                        <c:if test="${material.aceitoParaColeta}">
                                                            <button class="btn btn-outline-warning btn-sm" title="Desativar">
                                                                ⚠️
                                                            </button>
                                                        </c:if>
                                                        <c:if test="${!material.aceitoParaColeta}">
                                                            <button class="btn btn-outline-success btn-sm" title="Ativar">
                                                                ✅
                                                            </button>
                                                        </c:if>
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

        <!-- Categorias de Materiais -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="card">
                    <div class="card-header">
                        <h5 class="mb-0">📊 Categorias de Materiais</h5>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-lg-3 col-md-6 mb-3">
                                <div class="card border-primary h-100">
                                    <div class="card-body text-center">
                                        <h6 class="card-title">📄 Papel</h6>
                                        <p class="card-text">Jornais, revistas, papelão, etc.</p>
                                        <span class="badge bg-primary">R$ 0.50/kg</span>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-3 col-md-6 mb-3">
                                <div class="card border-info h-100">
                                    <div class="card-body text-center">
                                        <h6 class="card-title">🥤 Plástico</h6>
                                        <p class="card-text">Garrafas, embalagens, etc.</p>
                                        <span class="badge bg-info">R$ 1.20/kg</span>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-3 col-md-6 mb-3">
                                <div class="card border-success h-100">
                                    <div class="card-body text-center">
                                        <h6 class="card-title">🥃 Vidro</h6>
                                        <p class="card-text">Garrafas, potes, etc.</p>
                                        <span class="badge bg-success">R$ 0.30/kg</span>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-3 col-md-6 mb-3">
                                <div class="card border-secondary h-100">
                                    <div class="card-body text-center">
                                        <h6 class="card-title">🔩 Metal</h6>
                                        <p class="card-text">Latas, ferros, etc.</p>
                                        <span class="badge bg-secondary">R$ 2.50/kg</span>
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
                                <h6>♻️ Reciclagem</h6>
                                <ul class="text-muted">
                                    <li><strong>Papel:</strong> Reduz desmatamento</li>
                                    <li><strong>Plástico:</strong> Economiza petróleo</li>
                                    <li><strong>Vidro:</strong> 100% reciclável</li>
                                    <li><strong>Metal:</strong> Economiza energia</li>
                                </ul>
                            </div>
                            <div class="col-md-6">
                                <h6>💰 Valor dos Materiais</h6>
                                <p class="text-muted">
                                    Os preços variam conforme a qualidade, pureza e demanda do material.
                                    Materiais mais valiosos incluem metais e plásticos de alta qualidade.
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
                    <strong>Catálogo de Materiais - Sistema de Roteamento</strong><br>
                    Total de materiais: ${totalMateriais} | Última atualização: <span id="timestamp"></span>
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
