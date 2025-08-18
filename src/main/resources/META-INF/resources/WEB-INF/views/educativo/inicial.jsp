<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sistema de Roteamento - Página Educativa</title>
    
    <!-- Bootstrap CSS para estilização -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <style>
        .hero-section {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 80px 0;
            margin-bottom: 40px;
        }
        .tech-card {
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            cursor: pointer;
        }
        .tech-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 25px rgba(0,0,0,0.15);
        }
        .jsp-card { border-left: 4px solid #ffc107; }
        .jsf-card { border-left: 4px solid #28a745; }
        .rest-card { border-left: 4px solid #17a2b8; }
        .react-card { border-left: 4px solid #61dafb; }
        .feature-icon {
            font-size: 3rem;
            margin-bottom: 1rem;
        }
    </style>
</head>
<body>
    <!-- CONCEITO DIDÁTICO - Seção hero -->
    <section class="hero-section">
        <div class="container text-center">
            <h1 class="display-4 mb-4">${titulo}</h1>
            <p class="lead mb-4">${descricao}</p>
            <p class="h5">Demonstração prática de diferentes tecnologias Java Web</p>
        </div>
    </section>

    <div class="container">
        <!-- CONCEITO DIDÁTICO - Navegação entre tecnologias -->
        <div class="row mb-4">
            <div class="col-12">
                <nav class="navbar navbar-expand-lg navbar-light bg-light rounded">
                    <div class="container-fluid">
                        <span class="navbar-brand fw-bold">🚀 Navegação Educativa:</span>
                        <div class="navbar-nav ms-auto">
                            <a class="nav-link active" href="/educativo">🏠 Início</a>
                            <a class="nav-link" href="/educativo/jsp">🟡 JSP</a>
                            <a class="nav-link" href="/educativo/jsf">🟢 JSF</a>
                            <a class="nav-link" href="/educativo/comparacao">📊 Comparação</a>
                            <a class="nav-link" href="/educativo/integracao">🔗 Integração</a>
                        </div>
                    </div>
                </nav>
            </div>
        </div>

        <!-- CONCEITO DIDÁTICO - Cards das tecnologias -->
        <div class="row mb-5">
            <div class="col-12">
                <h2 class="text-center mb-4">🛠️ Tecnologias Demonstradas</h2>
                <p class="text-center text-muted mb-5">Clique em cada tecnologia para explorar seus conceitos</p>
            </div>
        </div>

        <div class="row">
            <div class="col-lg-3 col-md-6 mb-4">
                <div class="card tech-card jsp-card h-100" onclick="window.location.href='/educativo/jsp'">
                    <div class="card-body text-center">
                        <div class="feature-icon">🟡</div>
                        <h5 class="card-title">JSP</h5>
                        <h6 class="card-subtitle mb-2 text-muted">JavaServer Pages</h6>
                        <p class="card-text">Tecnologia tradicional Java EE para desenvolvimento web, permitindo misturar HTML com código Java.</p>
                        <div class="mt-3">
                            <span class="badge bg-warning text-dark">Tradicional</span>
                            <span class="badge bg-success">Baixa Complexidade</span>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-lg-3 col-md-6 mb-4">
                <div class="card tech-card jsf-card h-100" onclick="window.location.href='/educativo/jsf'">
                    <div class="card-body text-center">
                        <div class="feature-icon">🟢</div>
                        <h5 class="card-title">JSF</h5>
                        <h6 class="card-subtitle mb-2 text-muted">JavaServer Faces</h6>
                        <p class="card-text">Framework baseado em componentes para desenvolvimento de interfaces de usuário em Java EE.</p>
                        <div class="mt-3">
                            <span class="badge bg-success">Componente</span>
                            <span class="badge bg-warning">Média Complexidade</span>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-lg-3 col-md-6 mb-4">
                <div class="card tech-card rest-card h-100" onclick="window.location.href='/educativo/comparacao'">
                    <div class="card-body text-center">
                        <div class="feature-icon">🔵</div>
                        <h5 class="card-title">REST API</h5>
                        <h6 class="card-subtitle mb-2 text-muted">Spring Boot</h6>
                        <p class="card-text">Arquitetura moderna para serviços web, separando frontend de backend com APIs RESTful.</p>
                        <div class="mt-3">
                            <span class="badge bg-info">Moderno</span>
                            <span class="badge bg-warning">Média Complexidade</span>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-lg-3 col-md-6 mb-4">
                <div class="card tech-card react-card h-100" onclick="window.location.href='/educativo/comparacao'">
                    <div class="card-body text-center">
                        <div class="feature-icon">🔷</div>
                        <h5 class="card-title">React</h5>
                        <h6 class="card-subtitle mb-2 text-muted">Frontend Moderno</h6>
                        <p class="card-text">Biblioteca JavaScript para construção de interfaces de usuário modernas e interativas.</p>
                        <div class="mt-3">
                            <span class="badge bg-primary">Frontend</span>
                            <span class="badge bg-danger">Alta Complexidade</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- CONCEITO DIDÁTICO - Objetivos educativos -->
        <div class="row mb-5">
            <div class="col-12">
                <div class="card border-primary">
                    <div class="card-header bg-primary text-white">
                        <h4>🎯 Objetivos Educativos</h4>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6">
                                <h6>📚 Conceitos Demonstrados:</h6>
                                <ul>
                                    <li><strong>JSP:</strong> Scriptlets, JSTL, Expression Language</li>
                                    <li><strong>JSF:</strong> Managed Beans, Componentes, Validação</li>
                                    <li><strong>REST API:</strong> Controllers, DTOs, ResponseEntity</li>
                                    <li><strong>Arquitetura:</strong> Separação de responsabilidades</li>
                                </ul>
                            </div>
                            <div class="col-md-6">
                                <h6>🔍 Habilidades Desenvolvidas:</h6>
                                <ul>
                                    <li>Comparação entre tecnologias</li>
                                    <li>Análise de casos de uso</li>
                                    <li>Tomada de decisão técnica</li>
                                    <li>Migração entre abordagens</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- CONCEITO DIDÁTICO - Fluxo de aprendizado -->
        <div class="row mb-5">
            <div class="col-12">
                <div class="card border-success">
                    <div class="card-header bg-success text-white">
                        <h4>🔄 Fluxo de Aprendizado Recomendado</h4>
                    </div>
                    <div class="card-body">
                        <div class="row text-center">
                            <div class="col-md-2">
                                <div class="bg-light rounded-circle d-inline-flex align-items-center justify-content-center" style="width: 60px; height: 60px;">
                                    <span class="h4 mb-0">1</span>
                                </div>
                                <p class="mt-2"><strong>JSP</strong><br>Conceitos básicos</p>
                            </div>
                            <div class="col-md-2">
                                <div class="bg-light rounded-circle d-inline-flex align-items-center justify-content-center" style="width: 60px; height: 60px;">
                                    <span class="h4 mb-0">2</span>
                                </div>
                                <p class="mt-2"><strong>JSF</strong><br>Componentes</p>
                            </div>
                            <div class="col-md-2">
                                <div class="bg-light rounded-circle d-inline-flex align-items-center justify-content-center" style="width: 60px; height: 60px;">
                                    <span class="h4 mb-0">3</span>
                                </div>
                                <p class="mt-2"><strong>REST</strong><br>APIs modernas</p>
                            </div>
                            <div class="col-md-2">
                                <div class="bg-light rounded-circle d-inline-flex align-items-center justify-content-center" style="width: 60px; height: 60px;">
                                    <span class="h4 mb-0">4</span>
                                </div>
                                <p class="mt-2"><strong>Comparação</strong><br>Análise crítica</p>
                            </div>
                            <div class="col-md-2">
                                <div class="bg-light rounded-circle d-inline-flex align-items-center justify-content-center" style="width: 60px; height: 60px;">
                                    <span class="h4 mb-0">5</span>
                                </div>
                                <p class="mt-2"><strong>Integração</strong><br>Casos híbridos</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- CONCEITO DIDÁTICO - Recursos adicionais -->
        <div class="row mb-5">
            <div class="col-12">
                <div class="card border-info">
                    <div class="card-header bg-info text-white">
                        <h4>📖 Recursos Adicionais</h4>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-4">
                                <h6>📚 Documentação Oficial:</h6>
                                <ul>
                                    <li><a href="https://docs.oracle.com/javaee/5/tutorial/doc/bnagx.html" target="_blank">JSP Tutorial</a></li>
                                    <li><a href="https://docs.oracle.com/javaee/7/tutorial/jsf-intro.htm" target="_blank">JSF Tutorial</a></li>
                                    <li><a href="https://spring.io/guides" target="_blank">Spring Boot Guides</a></li>
                                    <li><a href="https://react.dev/" target="_blank">React Documentation</a></li>
                                </ul>
                            </div>
                            <div class="col-md-4">
                                <h6>🎥 Vídeos Educativos:</h6>
                                <ul>
                                    <li>JSP para Iniciantes</li>
                                    <li>JSF Componentes</li>
                                    <li>Spring Boot REST</li>
                                    <li>React Hooks</li>
                                </ul>
                            </div>
                            <div class="col-md-4">
                                <h6>💻 Projetos Práticos:</h6>
                                <ul>
                                    <li>Sistema de Blog</li>
                                    <li>E-commerce Básico</li>
                                    <li>Dashboard Admin</li>
                                    <li>API de Usuários</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- CONCEITO DIDÁTICO - Rodapé educativo -->
        <div class="row">
            <div class="col-12">
                <div class="alert alert-info">
                    <h6>🎓 Sistema Educativo de Roteamento de Coletas</h6>
                    <p class="mb-0">
                        Este projeto demonstra diferentes abordagens para desenvolvimento web em Java, 
                        permitindo comparar tecnologias tradicionais (JSP/JSF) com abordagens modernas (REST API + React). 
                        Use a navegação acima para explorar cada tecnologia e entender suas características, 
                        vantagens e casos de uso apropriados.
                    </p>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
