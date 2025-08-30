<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Página Inicial - Sistema de Roteamento</title>
    
    <!-- Bootstrap CSS para estilização -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Estilos Básicos -->
    <link href="/css/basic-style.css" rel="stylesheet">
</head>
<body>
    <!-- Seção Hero -->
    <section class="hero-section">
        <div class="container text-center">

            <h1 class="display-5 mb-3">
                <img src="/images/SrPC.png" alt="SrPC" class="hero-icon" style="width: 1.2em; height: 1.2em; vertical-align: middle; margin-right: 0.3em;">
                Sistema de Roteamento Programado de Coletas
            </h1>
            <p class="lead mb-0">Dashboard de Gestão de Coletas</p>
        </div>
    </section>

    <div class="container">
        <!-- Navegação Principal -->
        <div class="row mb-4">
            <div class="col-12">
                <nav class="navbar navbar-expand-lg navbar-light bg-light rounded">
                    <div class="container-fluid justify-content-center">
                        <div class="navbar-nav">
                            <a class="nav-link active" href="/sistema">🏠 Início</a>
                            <a class="nav-link" href="/sistema/coletas">📦 Coletas</a>
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

        <!-- Estatísticas Principais -->
        <div class="row mb-5">
            <div class="col-12">
                <h2 class="text-center mb-4">Visão Geral do Sistema</h2>
            </div>
        </div>

        <div class="row mb-5">
            <div class="col-lg-3 col-md-6 mb-4">
                <div class="card stat-card h-100">
                    <div class="card-body text-center">
                        <div class="stat-number">${totalColetas}</div>
                        <div class="stat-label">Total de Coletas</div>
                        <div class="mt-2">
                            <span class="badge bg-primary">📦</span>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-lg-3 col-md-6 mb-4">
                <div class="card stat-card h-100">
                    <div class="card-body text-center">
                        <div class="stat-number">${totalRotas}</div>
                        <div class="stat-label">Total de Rotas</div>
                        <div class="mt-2">
                            <span class="badge bg-success">🗺️</span>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-lg-3 col-md-6 mb-4">
                <div class="card stat-card h-100">
                    <div class="card-body text-center">
                        <div class="stat-number">${coletasPendentes}</div>
                        <div class="stat-label">Coletas Pendentes</div>
                        <div class="mt-2">
                            <span class="badge bg-warning text-dark">⏳</span>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-lg-3 col-md-6 mb-4">
                <div class="card stat-card h-100">
                    <div class="card-body text-center">
                        <div class="stat-number">${rotasAtivas}</div>
                        <div class="stat-label">Rotas Ativas</div>
                        <div class="mt-2">
                            <span class="badge bg-info">🚗</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Gráficos e Visualizações -->
        <div class="row mb-5">
            <div class="col-lg-6 mb-4">
                <div class="card">
                    <div class="card-header">
                        <h5 class="mb-0">📈 Status das Coletas</h5>
                    </div>
                    <div class="card-body">
                        <div class="chart-container">
                            <div class="row text-center">
                                <div class="col-4">
                                    <div class="stat-number text-success">${coletasPendentes}</div>
                                    <div class="stat-label">Pendentes</div>
                                </div>
                                <div class="col-4">
                                    <div class="stat-number text-primary">${totalColetas - coletasPendentes}</div>
                                    <div class="stat-label">Processadas</div>
                                </div>
                                <div class="col-4">
                                    <div class="stat-number text-info">${totalColetas}</div>
                                    <div class="stat-label">Total</div>
                                </div>
                            </div>
                            <div class="progress mt-3">
                                <c:set var="percentualProcessadas" value="${totalColetas > 0 ? ((totalColetas - coletasPendentes) * 100 / totalColetas) : 0}" />
                                <div class="progress-bar bg-success" style="width: ${percentualProcessadas}%"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-lg-6 mb-4">
                <div class="card">
                    <div class="card-header">
                        <h5 class="mb-0">🗺️ Status das Rotas</h5>
                    </div>
                    <div class="card-body">
                        <div class="chart-container">
                            <div class="row text-center">
                                <div class="col-4">
                                    <div class="stat-number text-warning">${rotasAtivas}</div>
                                    <div class="stat-label">Ativas</div>
                                </div>
                                <div class="col-4">
                                    <div class="stat-number text-secondary">${totalRotas - rotasAtivas}</div>
                                    <div class="stat-label">Finalizadas</div>
                                </div>
                                <div class="col-4">
                                    <div class="stat-number text-info">${totalRotas}</div>
                                    <div class="stat-label">Total</div>
                                </div>
                            </div>
                            <div class="progress mt-3">
                                <c:set var="percentualAtivas" value="${totalRotas > 0 ? (rotasAtivas * 100 / totalRotas) : 0}" />
                                <div class="progress-bar bg-warning" style="width: ${percentualAtivas}%"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Ações Rápidas -->
        <div class="row mb-5">
            <div class="col-12">
                <h2 class="text-center mb-4">Ações Rápidas</h2>
            </div>
        </div>

        <div class="row">
            <div class="col-lg-4 col-md-6 mb-4">
                <div class="card stat-card h-100">
                    <div class="card-body text-center">
                        <div class="mb-3">
                            <span style="font-size: 3rem;">📦</span>
                        </div>
                        <h5 class="card-title">Nova Coleta</h5>
                        <p class="card-text">Criar nova solicitação de coleta no sistema.</p>
                        <a href="/sistema/coletas" class="btn btn-primary">Criar Coleta</a>
                    </div>
                </div>
            </div>

            <div class="col-lg-4 col-md-6 mb-4">
                <div class="card stat-card h-100">
                    <div class="card-body text-center">
                        <div class="mb-3">
                            <span style="font-size: 3rem;">🗺️</span>
                        </div>
                        <h5 class="card-title">Nova Rota</h5>
                        <p class="card-text">Criar rota otimizada para coleta de materiais.</p>
                        <a href="/sistema/rotas" class="btn btn-success">Criar Rota</a>
                    </div>
                </div>
            </div>

            <div class="col-lg-4 col-md-6 mb-4">
                <div class="card stat-card h-100">
                    <div class="card-body text-center">
                        <div class="mb-3">
                            <span style="font-size: 3rem;">👥</span>
                        </div>
                        <h5 class="card-title">Novo Usuário</h5>
                        <p class="card-text">Cadastrar novo usuário ou coletor no sistema.</p>
                        <a href="/sistema/usuarios" class="btn btn-info">Cadastrar Usuário</a>
                    </div>
                </div>
            </div>

            <div class="col-lg-4 col-md-6 mb-4">
                <div class="card stat-card h-100">
                    <div class="card-body text-center">
                        <div class="mb-3">
                            <span style="font-size: 3rem;">♻️</span>
                        </div>
                        <h5 class="card-title">Novo Material</h5>
                        <p class="card-text">Adicionar novo material reciclável ao catálogo.</p>
                        <a href="/sistema/materiais" class="btn btn-warning">Adicionar Material</a>
                    </div>
                </div>
            </div>

            <div class="col-lg-4 col-md-6 mb-4">
                <div class="card stat-card h-100">
                    <div class="card-body text-center">
                        <div class="mb-3">
                            <span style="font-size: 3rem;">🗺️</span>
                        </div>
                        <h5 class="card-title">Visualizar Mapa</h5>
                        <p class="card-text">Ver coletas e rotas no mapa interativo.</p>
                        <a href="/sistema/mapa" class="btn btn-secondary">Ver Mapa</a>
                    </div>
                </div>
            </div>

            <div class="col-lg-4 col-md-6 mb-4">
                <div class="card stat-card h-100">
                    <div class="card-body text-center">
                        <div class="mb-3">
                            <span style="font-size: 3rem;">⚙️</span>
                        </div>
                        <h5 class="card-title">Configurações</h5>
                        <p class="card-text">Ajustar parâmetros e configurações do sistema.</p>
                        <a href="/sistema/configuracoes" class="btn btn-dark">Configurar</a>
                    </div>
                </div>
            </div>
        </div>

        <!-- Informações do Sistema -->
        <div class="row mb-5">
            <div class="col-12">
                <div class="card">
                    <div class="card-header">
                        <h5 class="mb-0">ℹ️ Informações do Sistema</h5>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6">
                                <h6>📊 Métricas de Performance</h6>
                                <ul class="list-unstyled">
                                    <li>✅ Sistema operacional</li>
                                    <li>✅ Banco de dados conectado</li>
                                    <li>✅ Serviços ativos</li>
                                    <li>✅ Algoritmos de roteamento funcionando</li>
                                </ul>
                            </div>
                            <div class="col-md-6">
                                <h6>🔧 Status dos Serviços</h6>
                                <ul class="list-unstyled">
                                    <li>✅ Spring Boot ativo</li>
                                    <li>✅ H2 Database rodando</li>
                                    <li>✅ JSP/JSF funcionando</li>
                                    <li>✅ APIs REST disponíveis</li>
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
                    <strong>Página Inicial do Sistema de Roteamento</strong><br>
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
        
        // Auto-refresh a cada 30 segundos
        setInterval(function() {
            location.reload();
        }, 30000);
    </script>
</body>
</html>
