<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Comparação de Tecnologias - Sistema de Roteamento</title>
    
    <!-- Bootstrap CSS para estilização -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <style>
        .tech-card {
            border-left: 4px solid #007bff;
            margin-bottom: 20px;
            padding: 15px;
        }
        .jsp-card { border-left-color: #ffc107; }
        .jsf-card { border-left-color: #28a745; }
        .rest-card { border-left-color: #17a2b8; }
        .react-card { border-left-color: #61dafb; }
        
        .complexity-low { color: #28a745; }
        .complexity-medium { color: #ffc107; }
        .complexity-high { color: #dc3545; }
        
        .timeline-item {
            position: relative;
            padding-left: 30px;
            margin-bottom: 20px;
        }
        .timeline-item::before {
            content: '';
            position: absolute;
            left: 0;
            top: 0;
            width: 12px;
            height: 12px;
            border-radius: 50%;
            background-color: #007bff;
        }
    </style>
</head>
<body>
    <div class="container mt-4">
        <!-- CONCEITO DIDÁTICO - Cabeçalho da comparação -->
        <div class="row">
            <div class="col-12">
                <h1 class="text-primary">Comparação de Tecnologias Java Web</h1>
                <p class="lead">Análise educativa das diferentes abordagens para desenvolvimento web em Java</p>
                <hr>
            </div>
        </div>

        <!-- CONCEITO DIDÁTICO - Navegação entre tecnologias -->
        <div class="row mb-4">
            <div class="col-12">
                <nav class="navbar navbar-expand-lg navbar-light bg-light">
                    <div class="container-fluid">
                        <span class="navbar-brand">Navegação Educativa:</span>
                        <div class="navbar-nav">
                            <a class="nav-link" href="/educativo">Início</a>
                            <a class="nav-link" href="/educativo/jsp">JSP</a>
                            <a class="nav-link" href="/educativo/jsf">JSF</a>
                            <a class="nav-link active" href="/educativo/comparacao">Comparação</a>
                            <a class="nav-link" href="/educativo/integracao">Integração</a>
                        </div>
                    </div>
                </nav>
            </div>
        </div>

        <!-- CONCEITO DIDÁTICO - Timeline histórico -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="card">
                    <div class="card-header">
                        <h5>📅 Evolução Histórica das Tecnologias</h5>
                    </div>
                    <div class="card-body">
                        <div class="timeline-item">
                            <h6><strong>1999 - JSP (JavaServer Pages)</strong></h6>
                            <p>Primeira tecnologia Java para desenvolvimento web, permitindo misturar HTML com código Java.</p>
                        </div>
                        <div class="timeline-item">
                            <h6><strong>2004 - JSF (JavaServer Faces)</strong></h6>
                            <p>Framework baseado em componentes, introduzindo conceitos de UI reutilizáveis.</p>
                        </div>
                        <div class="timeline-item">
                            <h6><strong>2000+ - REST APIs</strong></h6>
                            <p>Arquitetura para serviços web, separando frontend de backend.</p>
                        </div>
                        <div class="timeline-item">
                            <h6><strong>2013 - React</strong></h6>
                            <p>Biblioteca JavaScript para interfaces de usuário, revolucionando o desenvolvimento frontend.</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- CONCEITO DIDÁTICO - Tabela de comparação -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="card">
                    <div class="card-header">
                        <h5>📊 Comparação Detalhada</h5>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-striped">
                                <thead class="table-dark">
                                    <tr>
                                        <th>Aspecto</th>
                                        <th>JSP</th>
                                        <th>JSF</th>
                                        <th>REST API</th>
                                        <th>React</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td><strong>Tipo</strong></td>
                                        <td>Tradicional</td>
                                        <td>Componente</td>
                                        <td>Moderno</td>
                                        <td>Frontend</td>
                                    </tr>
                                    <tr>
                                        <td><strong>Complexidade</strong></td>
                                        <td><span class="complexity-low">Baixa</span></td>
                                        <td><span class="complexity-medium">Média</span></td>
                                        <td><span class="complexity-medium">Média</span></td>
                                        <td><span class="complexity-high">Alta</span></td>
                                    </tr>
                                    <tr>
                                        <td><strong>Performance</strong></td>
                                        <td>Limitada</td>
                                        <td>Média</td>
                                        <td>Alta</td>
                                        <td>Muito Alta</td>
                                    </tr>
                                    <tr>
                                        <td><strong>Manutenibilidade</td>
                                        <td>Baixa</td>
                                        <td>Média</td>
                                        <td>Alta</td>
                                        <td>Muito Alta</td>
                                    </tr>
                                    <tr>
                                        <td><strong>Flexibilidade</strong></td>
                                        <td>Baixa</td>
                                        <td>Média</td>
                                        <td>Alta</td>
                                        <td>Muito Alta</td>
                                    </tr>
                                    <tr>
                                        <td><strong>Integração Java</strong></td>
                                        <td>Nativa</td>
                                        <td>Nativa</td>
                                        <td>Nativa</td>
                                        <td>Via API</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- CONCEITO DIDÁTICO - Cards de cada tecnologia -->
        <div class="row">
            <div class="col-md-6 mb-4">
                <div class="card jsp-card">
                    <div class="card-header bg-warning text-dark">
                        <h5>🟡 JSP (JavaServer Pages)</h5>
                    </div>
                    <div class="card-body">
                        <h6>Características:</h6>
                        <ul>
                            <li>Código Java misturado com HTML</li>
                            <li>Scriptlets e Expression Language</li>
                            <li>JSTL para lógica de apresentação</li>
                            <li>Renderização no servidor</li>
                        </ul>
                        
                        <h6>Casos de Uso:</h6>
                        <ul>
                            <li>Aplicações web simples</li>
                            <li>Relatórios e páginas estáticas</li>
                            <li>Prototipagem rápida</li>
                            <li>Manutenção de sistemas legados</li>
                        </ul>
                        
                        <h6>Exemplo de Código:</h6>
                        <div class="bg-light p-2 rounded">
                            <code>&lt;% String nome = "João"; %&gt;<br>
                            &lt;p&gt;Olá, &lt;%= nome %&gt;!&lt;/p&gt;</code>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-md-6 mb-4">
                <div class="card jsf-card">
                    <div class="card-header bg-success text-white">
                        <h5>🟢 JSF (JavaServer Faces)</h5>
                    </div>
                    <div class="card-body">
                        <h6>Características:</h6>
                        <ul>
                            <li>Componentes reutilizáveis</li>
                            <li>Gerenciamento de estado</li>
                            <li>Validação integrada</li>
                            <li>Navegação declarativa</li>
                        </ul>
                        
                        <h6>Casos de Uso:</h6>
                        <ul>
                            <li>Aplicações empresariais</li>
                            <li>Formulários complexos</li>
                            <li>Sistemas internos</li>
                            <li>Portais corporativos</li>
                        </ul>
                        
                        <h6>Exemplo de Código:</h6>
                        <div class="bg-light p-2 rounded">
                            <code>&lt;h:form&gt;<br>
                            &nbsp;&nbsp;&lt;h:inputText value="#{bean.nome}" /&gt;<br>
                            &nbsp;&nbsp;&lt;h:commandButton action="#{bean.salvar}" /&gt;<br>
                            &lt;/h:form&gt;</code>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-md-6 mb-4">
                <div class="card rest-card">
                    <div class="card-header bg-info text-white">
                        <h5>🔵 REST API (Spring Boot)</h5>
                    </div>
                    <div class="card-body">
                        <h6>Características:</h6>
                        <ul>
                            <li>Separação clara de responsabilidades</li>
                            <li>Dados em formato JSON</li>
                            <li>Stateless (sem estado)</li>
                            <li>Escalável e flexível</li>
                        </ul>
                        
                        <h6>Casos de Uso:</h6>
                        <ul>
                            <li>APIs para aplicações móveis</li>
                            <li>Integração entre sistemas</li>
                            <li>Microserviços</li>
                            <li>Frontend moderno</li>
                        </ul>
                        
                        <h6>Exemplo de Código:</h6>
                        <div class="bg-light p-2 rounded">
                            <code>@RestController<br>
                            @RequestMapping("/api")<br>
                            public class Controller {<br>
                            &nbsp;&nbsp;@GetMapping("/dados")<br>
                            &nbsp;&nbsp;public ResponseEntity&lt;List&lt;Dados&gt;&gt; getDados()</code>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-md-6 mb-4">
                <div class="card react-card">
                    <div class="card-header bg-primary text-white">
                        <h5>🔷 React (Frontend Moderno)</h5>
                    </div>
                    <div class="card-body">
                        <h6>Características:</h6>
                        <ul>
                            <li>Componentes reutilizáveis</li>
                            <li>Virtual DOM para performance</li>
                            <li>Estado local e global</li>
                            <li>Ecosistema rico</li>
                        </ul>
                        
                        <h6>Casos de Uso:</h6>
                        <ul>
                            <li>Interfaces de usuário modernas</li>
                            <li>Aplicações single-page (SPA)</li>
                            <li>Dashboards interativos</li>
                            <li>Aplicações móveis híbridas</li>
                        </ul>
                        
                        <h6>Exemplo de Código:</h6>
                        <div class="bg-light p-2 rounded">
                            <code>function Component() {<br>
                            &nbsp;&nbsp;const [data, setData] = useState([]);<br>
                            &nbsp;&nbsp;return &lt;div&gt;{data.map(item =&gt;<br>
                            &nbsp;&nbsp;&nbsp;&nbsp;&lt;span key={item.id}&gt;{item.nome}&lt;/span&gt;)}&lt;/div&gt;;<br>
                            }</code>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- CONCEITO DIDÁTICO - Análise de arquitetura -->
        <div class="row mt-4">
            <div class="col-12">
                <div class="card">
                    <div class="card-header">
                        <h5>🏗️ Análise de Arquitetura</h5>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6">
                                <h6>Arquitetura Tradicional (JSP/JSF):</h6>
                                <div class="bg-light p-3 rounded">
                                    <pre><code>Browser → Servlet Container → JSP/JSF → Java Classes → Database</code></pre>
                                    <ul class="mt-2">
                                        <li><strong>Vantagens:</strong> Simples, integração nativa</li>
                                        <li><strong>Desvantagens:</strong> Acoplamento, difícil manutenção</li>
                                    </ul>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <h6>Arquitetura Moderna (REST + React):</h6>
                                <div class="bg-light p-3 rounded">
                                    <pre><code>Browser → React App → HTTP/JSON → Spring Boot API → Database</code></pre>
                                    <ul class="mt-2">
                                        <li><strong>Vantagens:</strong> Desacoplado, escalável, manutenível</li>
                                        <li><strong>Desvantagens:</strong> Complexidade inicial, curva de aprendizado</li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- CONCEITO DIDÁTICO - Recomendações -->
        <div class="row mt-4">
            <div class="col-12">
                <div class="card border-primary">
                    <div class="card-header bg-primary text-white">
                        <h5>💡 Recomendações por Cenário</h5>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-4">
                                <h6>🟡 Use JSP quando:</h6>
                                <ul>
                                    <li>Manter sistema legado</li>
                                    <li>Prototipagem rápida</li>
                                    <li>Equipe com conhecimento Java</li>
                                    <li>Orçamento limitado</li>
                                </ul>
                            </div>
                            <div class="col-md-4">
                                <h6>🟢 Use JSF quando:</h6>
                                <ul>
                                    <li>Sistema empresarial interno</li>
                                    <li>Formulários complexos</li>
                                    <li>Equipe Java EE experiente</li>
                                    <li>Integração com Java EE</li>
                                </ul>
                            </div>
                            <div class="col-md-4">
                                <h6>🔵 Use REST + React quando:</h6>
                                <ul>
                                    <li>Novo projeto</li>
                                    <li>Equipe full-stack</li>
                                    <li>Escalabilidade importante</li>
                                    <li>Experiência moderna</li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- CONCEITO DIDÁTICO - Rodapé educativo -->
        <div class="row mt-4">
            <div class="col-12">
                <div class="alert alert-info">
                    <h6>📚 Conceitos Educativos Demonstrados:</h6>
                    <ul class="mb-0">
                        <li><strong>Evolução Tecnológica:</strong> Como as tecnologias evoluíram ao longo do tempo</li>
                        <li><strong>Análise Comparativa:</strong> Comparação sistemática entre abordagens</li>
                        <li><strong>Casos de Uso:</strong> Quando usar cada tecnologia</li>
                        <li><strong>Arquitetura:</strong> Diferenças estruturais entre abordagens</li>
                        <li><strong>Tomada de Decisão:</strong> Critérios para escolha de tecnologia</li>
                    </ul>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
