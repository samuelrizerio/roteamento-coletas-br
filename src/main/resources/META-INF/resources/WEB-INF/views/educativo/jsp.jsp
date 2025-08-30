<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${tecnologia} - Sistema de Roteamento</title>
    
    <!-- Bootstrap CSS para estilização -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Tema de Cores do Java -->
    <link href="/css/basic-style.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <!-- CONCEITO DIDÁTICO - Cabeçalho JSP -->
        <div class="row">
            <div class="col-12">
                <h1 class="text-primary">${tecnologia}</h1>
                <p class="lead">Demonstração educativa dos conceitos básicos de JSP</p>
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
                            <a class="nav-link active" href="/educativo/jsp">JSP</a>
                            <a class="nav-link" href="/educativo/jsf">JSF</a>
                            <a class="nav-link" href="/educativo/comparacao">Comparação</a>
                            <a class="nav-link" href="/educativo/integracao">Integração</a>
                        </div>
                    </div>
                </nav>
            </div>
        </div>

        <!-- CONCEITO DIDÁTICO - Scriptlet JSP -->
        <div class="row">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header">
                        <h5>Scriptlet JSP - Código Java na Página</h5>
                    </div>
                    <div class="card-body">
                        <div class="code-example">
                            &lt;% 
                            // CONCEITO DIDÁTICO - Scriptlet JSP
                            // Código Java executado no servidor
                            String dataAtual = new java.util.Date().toString();
                            int hora = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY);
                            String saudacao = (hora < 12) ? "Bom dia" : 
                                           (hora < 18) ? "Boa tarde" : "Boa noite";
                            %&gt;
                        </div>
                        
                        <!-- CONCEITO DIDÁTICO - Expressão JSP -->
                        <p><strong>Data atual:</strong> <%= dataAtual %></p>
                        <p><strong>Saudação:</strong> <%= saudacao %></p>
                        
                        <!-- CONCEITO DIDÁTICO - Declaração JSP -->
                        <%! 
                        // CONCEITO DIDÁTICO - Declaração JSP
                        // Métodos e variáveis de classe
                        private String formatarData(java.util.Date data) {
                            return new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(data);
                        }
                        %>
                        
                        <p><strong>Data formatada:</strong> <%= formatarData(new java.util.Date()) %></p>
                    </div>
                </div>
            </div>

            <div class="col-md-6">
                <div class="card">
                    <div class="card-header">
                        <h5>JSTL e Expression Language (EL)</h5>
                    </div>
                    <div class="card-body">
                        <!-- CONCEITO DIDÁTICO - JSTL Core -->
                        <h6>Dados do Sistema (via EL):</h6>
                        <ul class="list-group">
                            <li class="list-group-item">
                                <strong>Total de Coletas:</strong> 
                                <span class="badge bg-primary">${totalColetas}</span>
                            </li>
                            <li class="list-group-item">
                                <strong>Tecnologia:</strong> ${tecnologia}
                            </li>
                        </ul>

                        <!-- CONCEITO DIDÁTICO - JSTL c:if -->
                        <div class="mt-3">
                            <h6>Condicionais com JSTL:</h6>
                            <c:if test="${totalColetas > 0}">
                                <div class="alert alert-success">
                                    ✓ Sistema possui ${totalColetas} coleta(s) cadastrada(s)
                                </div>
                            </c:if>
                            <c:if test="${totalColetas == 0}">
                                <div class="alert alert-warning">
                                    ⚠ Nenhuma coleta cadastrada no sistema
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- CONCEITO DIDÁTICO - JSTL c:forEach -->
        <div class="row mt-4">
            <div class="col-12">
                <div class="card">
                    <div class="card-header">
                        <h5>Listagem de Coletas com JSTL</h5>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${not empty coletas}">
                                <div class="table-responsive">
                                    <table class="table table-striped">
                                        <thead>
                                            <tr>
                                                <th>ID</th>
                                                <th>Usuário</th>
                                                <th>Endereço</th>
                                                <th>Status</th>
                                                <th>Data Criação</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <!-- CONCEITO DIDÁTICO - Loop com JSTL -->
                                            <c:forEach var="coleta" items="${coletas}" varStatus="status">
                                                <tr>
                                                    <td>${coleta.id}</td>
                                                    <td>${coleta.nomeUsuario}</td>
                                                    <td>${fn:substring(coleta.endereco, 0, 30)}...</td>
                                                    <td>
                                                        <span class="badge bg-${coleta.status == 'SOLICITADA' ? 'warning' : 
                                                                              coleta.status == 'ACEITA' ? 'info' : 
                                                                              coleta.status == 'COLETADA' ? 'success' : 'secondary'}">
                                                            ${coleta.status}
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <fmt:formatDate value="${coleta.dataCriacao}" pattern="dd/MM/yyyy HH:mm"/>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="alert alert-info">
                                    Nenhuma coleta encontrada para exibição.
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>

        <!-- CONCEITO DIDÁTICO - Vantagens e Desvantagens -->
        <div class="row mt-4">
            <div class="col-md-6">
                <div class="card border-success">
                    <div class="card-header bg-success text-white">
                        <h5>✓ Vantagens do JSP</h5>
                    </div>
                    <div class="card-body">
                        <ul class="list-group list-group-flush">
                            <c:forEach var="vantagem" items="${vantagens}">
                                <li class="list-group-item advantage">${vantagem}</li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>
            </div>

            <div class="col-md-6">
                <div class="card border-danger">
                    <div class="card-header bg-danger text-white">
                        <h5>✗ Desvantagens do JSP</h5>
                    </div>
                    <div class="card-body">
                        <ul class="list-group list-group-flush">
                            <c:forEach var="desvantagem" items="${desvantagens}">
                                <li class="list-group-item disadvantage">${desvantagem}</li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>
            </div>
        </div>

        <!-- CONCEITO DIDÁTICO - Exemplos de código -->
        <div class="row mt-4">
            <div class="col-12">
                <div class="card">
                    <div class="card-header">
                        <h5>Exemplos de Código JSP</h5>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6">
                                <h6>Scriptlet Básico:</h6>
                                <div class="code-example">
&lt;% 
String nome = "João";
int idade = 25;
if (idade >= 18) {
    out.println(nome + " é maior de idade");
}
%&gt;
                                </div>
                            </div>
                            <div class="col-md-6">
                                <h6>Expression Language (EL):</h6>
                                <div class="code-example">
&lt;!-- Acesso a atributos do modelo --&gt;
&lt;p&gt;Nome: ${usuario.nome}&lt;/p&gt;
&lt;p&gt;Idade: ${usuario.idade}&lt;/p&gt;

&lt;!-- Operadores lógicos --&gt;
&lt;c:if test="${usuario.idade >= 18}"&gt;
    Maior de idade
&lt;/c:if&gt;
                                </div>
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
                        <li><strong>Scriptlets:</strong> Código Java embutido na página JSP</li>
                        <li><strong>JSTL:</strong> Biblioteca de tags para lógica de apresentação</li>
                        <li><strong>EL:</strong> Expression Language para acesso a dados</li>
                        <li><strong>Declarações:</strong> Métodos e variáveis de classe</li>
                        <li><strong>Diretivas:</strong> Configurações da página JSP</li>
                    </ul>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
