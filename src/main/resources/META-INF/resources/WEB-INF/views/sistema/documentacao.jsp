<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sistema de Roteamento - Documentação Arquitetural Java</title>
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    
    <!-- Tema Java -->
    <link href="/css/java-theme.css" rel="stylesheet">
    <link href="/css/java-theme-additional.css" rel="stylesheet">
    
    <!-- Syntax Highlighting -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/themes/prism-tomorrow.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <!-- Hero Section -->
    <section class="hero-section">
        <div class="container text-center">
            <h1 class="display-5 mb-3">📚 Documentação Arquitetural Java</h1>
            <p class="lead mb-0">Plataforma educativa demonstrando arquitetura empresarial, padrões de projeto e tecnologias Java modernas</p>
        </div>
    </section>

    <div class="container">
        <!-- Navegação -->
        <nav class="navbar navbar-expand-lg navbar-light bg-light rounded mb-4">
            <div class="container-fluid justify-content-center">
                <div class="navbar-nav">
                    <a class="nav-link" href="/sistema">🏠 Início</a>
                    <a class="nav-link" href="/sistema/coletas">📦 Coletas</a>
                    <a class="nav-link" href="/sistema/rotas">🗺️ Rotas</a>
                    <a class="nav-link" href="/sistema/usuarios">👥 Usuários</a>
                    <a class="nav-link" href="/sistema/materiais">♻️ Materiais</a>
                    <a class="nav-link" href="/sistema/mapa">🗺️ Mapa</a>
                    <a class="nav-link" href="/sistema/configuracoes">⚙️ Configurações</a>
                    <a class="nav-link active" href="/sistema/documentacao">📚 Documentação</a>
                </div>
            </div>
        </nav>

        <!-- Conceitos POO -->
        <div class="row mb-5">
            <div class="col-12">
                <h2 class="section-title">
                    <i class="fas fa-brain me-3"></i>Conceitos de Programação Orientada a Objetos
                </h2>
                <p class="text-center text-muted mb-5">Demonstração prática dos pilares fundamentais da POO</p>
            </div>
        </div>

        <div class="row mb-5">
            <div class="col-12 mb-4">
                <div class="concept-section">
                    <h3 class="concept-title">
                        <i class="fas fa-shield-alt text-success me-3"></i>Encapsulamento
                    </h3>
                    <div class="concept-content">
                        <p class="concept-description">
                            O encapsulamento é um dos pilares fundamentais da Programação Orientada a Objetos, 
                            que promove a ocultação de detalhes internos de implementação e exposição de uma 
                            interface controlada e bem definida. Este conceito é fundamental para criar sistemas 
                            robustos e manuteníveis.
                        </p>
                        
                        <div class="concept-details">
                            <h5><i class="fas fa-lightbulb text-warning me-2"></i>Como Funciona</h5>
                            <p>
                                No nosso sistema, o encapsulamento é implementado através de entidades JPA que 
                                possuem campos privados (<code>private</code>) e métodos públicos (<code>public</code>) 
                                para acessar e modificar esses campos. Isso garante que o estado interno dos 
                                objetos seja protegido e só possa ser alterado através de métodos controlados.
                            </p>
                            
                            <h5><i class="fas fa-code text-primary me-2"></i>Implementação Prática</h5>
                            <p>
                                Todas as entidades do sistema (Usuario, Coleta, Material, Rota) seguem este padrão. 
                                Por exemplo, a entidade <code>Coleta</code> possui campos como <code>id</code>, 
                                <code>endereco</code>, <code>status</code> que são privados, mas podem ser 
                                acessados através de getters e setters públicos.
                            </p>
                            
                            <h5><i class="fas fa-check-circle text-success me-2"></i>Benefícios</h5>
                            <ul>
                                <li><strong>Segurança:</strong> Controle total sobre como os dados são acessados e modificados</li>
                                <li><strong>Manutenibilidade:</strong> Mudanças internas não afetam o código que usa a classe</li>
                                <li><strong>Validação:</strong> Possibilidade de adicionar regras de negócio nos setters</li>
                                <li><strong>Flexibilidade:</strong> Facilita futuras refatorações e melhorias</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="col-12 mb-4">
                <div class="concept-section">
                    <h3 class="concept-title">
                        <i class="fas fa-sitemap text-primary me-3"></i>Herança
                    </h3>
                    <div class="concept-content">
                        <p class="concept-description">
                            A herança permite que uma classe herde características e comportamentos de outra classe, 
                            promovendo a reutilização de código e estabelecendo uma hierarquia natural entre as 
                            classes. Este mecanismo é essencial para criar estruturas de código organizadas e 
                            extensíveis.
                        </p>
                        
                        <div class="concept-details">
                            <h5><i class="fas fa-lightbulb text-warning me-2"></i>Estrutura Hierárquica</h5>
                            <p>
                                No nosso sistema, utilizamos herança para criar uma hierarquia de exceções 
                                customizadas. Todas as exceções específicas do domínio herdam de 
                                <code>RuntimeException</code>, aproveitando o comportamento padrão do Java 
                                para exceções não verificadas.
                            </p>
                            
                            <h5><i class="fas fa-code text-primary me-2"></i>Exemplos de Implementação</h5>
                            <p>
                                Criamos exceções como <code>ColetaNaoEncontradaException</code>, 
                                <code>MaterialNaoEncontradoException</code> e <code>UsuarioNaoEncontradoException</code> 
                                que herdam de <code>RuntimeException</code>. Isso nos permite:
                            </p>
                            <ul>
                                <li>Reutilizar toda a funcionalidade de exceções do Java</li>
                                <li>Adicionar comportamentos específicos para cada tipo de erro</li>
                                <li>Manter consistência no tratamento de erros</li>
                                <li>Facilitar o debugging e logging</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="col-12 mb-4">
                <div class="concept-section">
                    <h3 class="concept-title">
                        <i class="fas fa-magic text-warning me-3"></i>Polimorfismo
                    </h3>
                    <div class="concept-content">
                        <p class="concept-description">
                            O polimorfismo permite que objetos de diferentes classes respondam de formas 
                            diferentes ao mesmo método, proporcionando flexibilidade e desacoplamento no código. 
                            Este conceito é fundamental para criar sistemas modulares e extensíveis.
                        </p>
                        
                        <div class="concept-details">
                            <h5><i class="fas fa-lightbulb text-warning me-2"></i>Tipos de Polimorfismo</h5>
                            <p>
                                No nosso sistema, implementamos principalmente o polimorfismo de interface, 
                                onde diferentes implementações podem ser usadas de forma intercambiável através 
                                de uma interface comum.
                            </p>
                            
                            <h5><i class="fas fa-code text-primary me-2"></i>Implementação no Sistema</h5>
                            <p>
                                O padrão Repository é um excelente exemplo de polimorfismo. Todos os repositórios 
                                implementam a interface <code>JpaRepository</code>, permitindo que o código cliente 
                                trabalhe com qualquer implementação específica sem conhecer os detalhes internos.
                            </p>
                            
                            <h5><i class="fas fa-cogs text-info me-2"></i>Como Funciona na Prática</h5>
                            <p>
                                Quando o <code>ColetaService</code> chama <code>coletaRepository.findByStatus()</code>, 
                                ele não precisa saber se está trabalhando com uma implementação JPA, uma implementação 
                                em memória, ou qualquer outra implementação. O polimorfismo garante que o método 
                                correto seja chamado automaticamente.
                            </p>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="col-12 mb-4">
                <div class="concept-section">
                    <h3 class="concept-title">
                        <i class="fas fa-layer-group text-danger me-3"></i>Abstração
                    </h3>
                    <div class="concept-content">
                        <p class="concept-description">
                            A abstração é o processo de simplificar a complexidade através da criação de 
                            interfaces e classes abstratas que escondem detalhes de implementação. Este conceito 
                            é essencial para criar sistemas organizados, manuteníveis e escaláveis.
                        </p>
                        
                        <div class="concept-details">
                            <h5><i class="fas fa-lightbulb text-warning me-2"></i>Arquitetura em Camadas</h5>
                            <p>
                                Nosso sistema implementa uma arquitetura em camadas bem definida, onde cada 
                                camada tem responsabilidades específicas e se comunica apenas com as camadas 
                                adjacentes através de interfaces bem definidas.
                            </p>
                            
                            <h5><i class="fas fa-code text-primary me-2"></i>Estrutura das Camadas</h5>
                            <div class="layer-explanation">
                                <div class="layer-item">
                                    <h6><i class="fas fa-desktop text-primary me-2"></i>Controller (Apresentação)</h6>
                                    <p>
                                        Responsável por receber requisições HTTP, validar dados de entrada e 
                                        delegar a lógica de negócio para a camada de serviço. Utiliza anotações 
                                        como <code>@RestController</code>, <code>@RequestMapping</code> e 
                                        <code>@GetMapping</code>.
                                    </p>
                                </div>
                                
                                <div class="layer-item">
                                    <h6><i class="fas fa-cogs text-success me-2"></i>Service (Lógica de Negócio)</h6>
                                    <p>
                                        Contém toda a lógica de negócio, validações complexas e orquestração 
                                        de operações. Esta camada é independente de tecnologias específicas 
                                        e pode ser facilmente testada e reutilizada.
                                    </p>
                                </div>
                                
                                <div class="layer-item">
                                    <h6><i class="fas fa-database text-warning me-2"></i>Repository (Acesso a Dados)</h6>
                                    <p>
                                        Abstrai o acesso aos dados, seja através de banco de dados, APIs 
                                        externas ou arquivos. Implementa o padrão Repository para desacoplar 
                                        a lógica de negócio da persistência.
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Exemplos de Código - POO -->
        <div class="row mb-5">
            <div class="col-12">
                <h3 class="subsection-title">
                    <i class="fas fa-code me-2"></i>Exemplos de Código - POO
                </h3>
                <p class="text-center text-muted mb-4">Trechos de código que demonstram a implementação prática dos conceitos</p>
            </div>
        </div>

        <div class="row mb-5">
            <div class="col-md-6 mb-4">
                <div class="code-example-card">
                    <h5><i class="fas fa-shield-alt text-success me-2"></i>Encapsulamento</h5>
                    <div class="code-block">
                        <pre><code class="language-java">@Entity
@Table(name = "coletas")
public class Coleta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String endereco;
    
    @Enumerated(EnumType.STRING)
    private StatusColeta status;
    
    // Getters e Setters encapsulados
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { 
        this.endereco = endereco; 
    }
}</code></pre>
                    </div>
                    <div class="alert alert-info border-0 mt-2">
                        <strong>Explicação:</strong> Campos privados com acesso controlado via métodos públicos
                    </div>
                </div>
            </div>
            
            <div class="col-md-6 mb-4">
                <div class="code-example-card">
                    <h5><i class="fas fa-sitemap text-primary me-2"></i>Herança</h5>
                    <div class="code-block">
                        <pre><code class="language-java">public class ColetaNaoEncontradaException 
    extends RuntimeException {
    
    public ColetaNaoEncontradaException(Long id) {
        super("Coleta não encontrada com ID: " + id);
    }
}

public class MaterialNaoEncontradoException 
    extends RuntimeException {
    
    public MaterialNaoEncontradoException(Long id) {
        super("Material não encontrado com ID: " + id);
    }
}</code></pre>
                    </div>
                    <div class="alert alert-info border-0 mt-2">
                        <strong>Explicação:</strong> Exceções customizadas herdando de RuntimeException
                    </div>
                </div>
            </div>
            
            <div class="col-md-6 mb-4">
                <div class="code-example-card">
                    <h5><i class="fas fa-magic text-warning me-2"></i>Polimorfismo</h5>
                    <div class="code-block">
                        <pre><code class="language-java">public interface ColetaRepository 
    extends JpaRepository&lt;Coleta, Long&gt; {
    
    List&lt;Coleta&gt; findByStatus(StatusColeta status);
    List&lt;Coleta&gt; findByUsuarioId(Long usuarioId);
}

@Service
public class ColetaService {
    @Autowired
    private ColetaRepository coletaRepository;
    
    // Método polimórfico que funciona com qualquer implementação
    public List&lt;Coleta&gt; buscarPorStatus(StatusColeta status) {
        return coletaRepository.findByStatus(status);
    }
}</code></pre>
                    </div>
                    <div class="alert alert-info border-0 mt-2">
                        <strong>Explicação:</strong> Interface Repository com implementações específicas
                    </div>
                </div>
            </div>
            
            <div class="col-md-6 mb-4">
                <div class="code-example-card">
                    <h5><i class="fas fa-layer-group text-danger me-2"></i>Abstração</h5>
                    <div class="code-block">
                        <pre><code class="language-java">@RestController
@RequestMapping("/api/v1/coletas")
public class ColetaController {
    
    @Autowired
    private ColetaService coletaService;
    
    @GetMapping
    public ResponseEntity&lt;List&lt;ColetaDTO&gt;&gt; listarColetas() {
        return ResponseEntity.ok(coletaService.listarTodas());
    }
}

@Service
public class ColetaService {
    @Autowired
    private ColetaRepository coletaRepository;
    
    public List&lt;ColetaDTO&gt; listarTodas() {
        return coletaRepository.findAll()
            .stream()
            .map(this::converterParaDTO)
            .collect(Collectors.toList());
    }
}</code></pre>
                    </div>
                    <div class="alert alert-info border-0 mt-2">
                        <strong>Explicação:</strong> Separação clara entre Controller, Service e Repository
                    </div>
                </div>
            </div>
        </div>

        <!-- Padrões Arquiteturais -->
        <div class="row mb-5">
            <div class="col-12">
                <h2 class="section-title">
                    <i class="fas fa-building me-3"></i>Padrões Arquiteturais Implementados
                </h2>
                <p class="text-center text-muted mb-5">Padrões de projeto e arquitetura para código limpo e manutenível</p>
            </div>
        </div>

        <div class="row mb-5">
            <div class="col-12 mb-4">
                <div class="concept-section">
                    <h3 class="concept-title">
                        <i class="fas fa-database text-primary me-3"></i>Repository Pattern
                    </h3>
                    <div class="concept-content">
                        <p class="concept-description">
                            O Repository Pattern é um padrão arquitetural que abstrai a camada de acesso a dados, 
                            fornecendo uma interface limpa e consistente para operações de persistência. Este padrão 
                            é fundamental para desacoplar a lógica de negócio da tecnologia de persistência utilizada.
                        </p>
                        
                        <div class="concept-details">
                            <h5><i class="fas fa-lightbulb text-warning me-2"></i>Problema Resolvido</h5>
                            <p>
                                Em sistemas tradicionais, a lógica de negócio frequentemente se mistura com código 
                                de acesso a dados, criando acoplamento forte e dificultando a manutenção e os testes. 
                                O Repository Pattern resolve isso criando uma abstração que esconde os detalhes de 
                                implementação da persistência.
                            </p>
                            
                            <h5><i class="fas fa-code text-primary me-2"></i>Implementação no Sistema</h5>
                            <p>
                                No nosso sistema, cada entidade possui seu próprio repositório que estende 
                                <code>JpaRepository&lt;T, ID&gt;</code>. Por exemplo:
                            </p>
                            <ul>
                                <li><code>UsuarioRepository</code> para operações com usuários</li>
                                <li><code>ColetaRepository</code> para operações com coletas</li>
                                <li><code>MaterialRepository</code> para operações com materiais</li>
                                <li><code>RotaRepository</code> para operações com rotas</li>
                            </ul>
                            
                            <h5><i class="fas fa-cogs text-info me-2"></i>Funcionalidades Automáticas</h5>
                            <p>
                                O Spring Data JPA fornece automaticamente métodos CRUD básicos como:
                            </p>
                            <ul>
                                <li><code>save()</code> - Salvar/atualizar entidades</li>
                                <li><code>findById()</code> - Buscar por ID</li>
                                <li><code>findAll()</code> - Listar todas as entidades</li>
                                <li><code>delete()</code> - Remover entidades</li>
                                <li><code>count()</code> - Contar entidades</li>
                            </ul>
                            
                            <h5><i class="fas fa-check-circle text-success me-2"></i>Vantagens do Repository Pattern</h5>
                            <ul>
                                <li><strong>Desacoplamento:</strong> Lógica de negócio não depende de detalhes de persistência</li>
                                <li><strong>Testabilidade:</strong> Fácil criação de mocks para testes unitários</li>
                                <li><strong>Flexibilidade:</strong> Possibilidade de trocar implementações de persistência</li>
                                <li><strong>Consistência:</strong> Interface padronizada para todas as operações de dados</li>
                                <li><strong>Manutenibilidade:</strong> Mudanças na persistência não afetam o resto do sistema</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="col-12 mb-4">
                <div class="concept-section">
                    <h3 class="concept-title">
                        <i class="fas fa-cogs text-success me-3"></i>Service Layer
                    </h3>
                    <div class="concept-content">
                        <p class="concept-description">
                            A Service Layer é uma camada intermediária que contém toda a lógica de negócio do sistema, 
                            atuando como um intermediário entre os controllers (camada de apresentação) e os repositories 
                            (camada de persistência). Esta camada é essencial para manter o código organizado e reutilizável.
                        </p>
                        
                        <div class="concept-details">
                            <h5><i class="fas fa-lightbulb text-warning me-2"></i>Responsabilidades da Camada</h5>
                            <p>
                                A Service Layer é responsável por implementar regras de negócio complexas, validações 
                                que envolvem múltiplas entidades, orquestração de operações e transformação de dados 
                                entre diferentes formatos (entidades, DTOs, etc.).
                            </p>
                            
                            <h5><i class="fas fa-code text-primary me-2"></i>Implementação no Sistema</h5>
                            <p>
                                Cada funcionalidade principal possui seu próprio service:
                            </p>
                            <div class="service-examples">
                                <div class="service-item">
                                    <h6><i class="fas fa-users text-primary me-2"></i>UsuarioService</h6>
                                    <p>
                                        Gerencia operações de usuários, incluindo autenticação, validação de 
                                        permissões e transformação de dados sensíveis.
                                    </p>
                                </div>
                                
                                <div class="service-item">
                                    <h6><i class="fas fa-box text-success me-2"></i>ColetaService</h6>
                                    <p>
                                        Orquestra o processo de coleta, desde a solicitação até a finalização, 
                                        incluindo validações de status e regras de negócio.
                                    </p>
                                </div>
                                
                                <div class="service-item">
                                    <h6><i class="fas fa-route text-warning me-2"></i>RoteamentoService</h6>
                                    <p>
                                        Implementa lógica de negócio para roteamento, calculando distâncias, 
                                        tempos e sequências ideais de coleta usando padrões de projeto.
                                    </p>
                                </div>
                            </div>
                            
                            <h5><i class="fas fa-cogs text-info me-2"></i>Características Técnicas</h5>
                            <ul>
                                <li><strong>Anotação @Service:</strong> Identifica a classe como um service do Spring</li>
                                <li><strong>Injeção de Dependência:</strong> Recebe repositories e outros services via @Autowired</li>
                                <li><strong>Transações:</strong> Pode gerenciar transações com @Transactional</li>
                                <li><strong>Validações:</strong> Implementa regras de negócio e validações complexas</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="col-12 mb-4">
                <div class="concept-section">
                    <h3 class="concept-title">
                        <i class="fas fa-exchange-alt text-warning me-3"></i>DTO Pattern
                    </h3>
                    <div class="concept-content">
                        <p class="concept-description">
                            O DTO (Data Transfer Object) Pattern é um padrão que define objetos específicos para 
                            transferir dados entre diferentes camadas do sistema. Este padrão é essencial para 
                            controlar quais dados são expostos externamente e para otimizar a transferência de 
                            informações.
                        </p>
                        
                        <div class="concept-details">
                            <h5><i class="fas fa-lightbulb text-warning me-2"></i>Problema Resolvido</h5>
                            <p>
                                Quando expomos entidades JPA diretamente através de APIs REST, podemos enfrentar 
                                problemas como:
                            </p>
                            <ul>
                                <li>Exposição acidental de dados sensíveis</li>
                                <li>Problemas de serialização (referências circulares)</li>
                                <li>Falta de controle sobre quais campos são retornados</li>
                                <li>Dificuldade para versionar APIs</li>
                            </ul>
                            
                            <h5><i class="fas fa-code text-primary me-2"></i>Implementação no Sistema</h5>
                            <p>
                                Criamos DTOs específicos para cada operação:
                            </p>
                            <div class="dto-examples">
                                <div class="dto-item">
                                    <h6><i class="fas fa-user text-primary me-2"></i>UsuarioDTO</h6>
                                    <p>
                                        Contém apenas informações públicas do usuário, excluindo senhas e dados 
                                        sensíveis. Usado para listagem e exibição pública.
                                    </p>
                                </div>
                                
                                <div class="dto-item">
                                    <h6><i class="fas fa-box text-success me-2"></i>ColetaDTO</h6>
                                    <p>
                                        Representa uma coleta com informações essenciais, incluindo dados do 
                                        usuário solicitante e material, mas sem detalhes internos do sistema.
                                    </p>
                                </div>
                                
                                <div class="dto-item">
                                    <h6><i class="fas fa-route text-warning me-2"></i>RotaDTO</h6>
                                    <p>
                                        Contém informações de rota otimizada, incluindo pontos de coleta, 
                                        distâncias e tempos estimados, formatado para consumo externo.
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Exemplos de Código - Padrões Arquiteturais -->
        <div class="row mb-5">
            <div class="col-12">
                <h3 class="subsection-title">
                    <i class="fas fa-code me-2"></i>Exemplos de Código - Padrões Arquiteturais
                </h3>
                <p class="text-center text-muted mb-4">Implementação prática dos padrões de projeto utilizados</p>
            </div>
        </div>

        <div class="row mb-5">
            <div class="col-md-6 mb-4">
                <div class="code-example-card">
                    <h5><i class="fas fa-database text-primary me-2"></i>Repository Pattern</h5>
                    <div class="code-block">
                        <pre><code class="language-java">public interface ColetaRepository 
    extends JpaRepository&lt;Coleta, Long&gt; {
    
    // Queries automáticas do Spring Data
    List&lt;Coleta&gt; findByStatus(StatusColeta status);
    List&lt;Coleta&gt; findByUsuarioId(Long usuarioId);
    
    // Query customizada com JPQL
    @Query("SELECT c FROM Coleta c WHERE c.endereco LIKE %:endereco%")
    List&lt;Coleta&gt; buscarPorEndereco(@Param("endereco") String endereco);
    
    // Query nativa SQL
    @Query(value = "SELECT * FROM coletas WHERE peso > :peso", 
           nativeQuery = true)
    List&lt;Coleta&gt; buscarPorPesoMinimo(@Param("peso") Double peso);
}</code></pre>
                    </div>
                    <div class="alert alert-info border-0 mt-2">
                        <strong>Benefício:</strong> Abstração da camada de dados com queries automáticas
                    </div>
                </div>
            </div>
            
            <div class="col-md-6 mb-4">
                <div class="code-example-card">
                    <h5><i class="fas fa-cogs text-success me-2"></i>Service Layer</h5>
                    <div class="code-block">
                        <pre><code class="language-java">@Service
@Transactional
public class ColetaService {
    
    @Autowired
    private ColetaRepository coletaRepository;
    
    @Autowired
    private UsuarioService usuarioService;
    
    public ColetaDTO criarColeta(ColetaDTO coletaDTO) {
        // Validações de negócio
        if (coletaDTO.getPeso() <= 0) {
            throw new IllegalArgumentException("Peso deve ser maior que zero");
        }
        
        // Conversão para entidade
        Coleta coleta = new Coleta();
        coleta.setEndereco(coletaDTO.getEndereco());
        coleta.setPeso(coletaDTO.getPeso());
        coleta.setStatus(StatusColeta.PENDENTE);
        
        // Persistência
        Coleta coletaSalva = coletaRepository.save(coleta);
        
        // Retorno como DTO
        return converterParaDTO(coletaSalva);
    }
}</code></pre>
                    </div>
                    <div class="alert alert-info border-0 mt-2">
                        <strong>Benefício:</strong> Lógica de negócio centralizada e transacional
                    </div>
                </div>
            </div>
            
            <div class="col-md-6 mb-4">
                <div class="code-example-card">
                    <h5><i class="fas fa-exchange-alt text-warning me-2"></i>DTO Pattern</h5>
                    <div class="code-block">
                        <pre><code class="language-java">public class ColetaDTO {
    private Long id;
    private String endereco;
    private Double peso;
    private String status;
    private String material;
    private String nomeUsuario;
    
    // Construtor
    public ColetaDTO(Long id, String endereco, Double peso, 
                     String status, String material, String nomeUsuario) {
        this.id = id;
        this.endereco = endereco;
        this.peso = peso;
        this.status = status;
        this.material = material;
        this.nomeUsuario = nomeUsuario;
    }
    
    // Getters e Setters
    // ... métodos de acesso
}

// Conversão na camada de serviço
private ColetaDTO converterParaDTO(Coleta coleta) {
    return new ColetaDTO(
        coleta.getId(),
        coleta.getEndereco(),
        coleta.getPeso(),
        coleta.getStatus().name(),
        coleta.getMaterial().getNome(),
        coleta.getUsuario().getNome()
    );
}</code></pre>
                    </div>
                    <div class="alert alert-info border-0 mt-2">
                        <strong>Benefício:</strong> Controle de exposição de dados e performance
                    </div>
                </div>
            </div>
            
            <div class="col-md-6 mb-4">
                <div class="code-example-card">
                    <h5><i class="fas fa-exclamation-triangle text-danger me-2"></i>Exception Handling</h5>
                    <div class="code-block">
                        <pre><code class="language-java">@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ColetaNaoEncontradaException.class)
    public ResponseEntity&lt;ErrorResponse&gt; 
        handleColetaNaoEncontrada(ColetaNaoEncontradaException ex) {
        
        ErrorResponse error = new ErrorResponse(
            "COLETA_NAO_ENCONTRADA",
            ex.getMessage(),
            LocalDateTime.now()
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                          .body(error);
    }
    
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity&lt;ErrorResponse&gt; 
        handleValidation(ValidationException ex) {
        
        ErrorResponse error = new ErrorResponse(
            "VALIDATION_ERROR",
            ex.getMessage(),
            LocalDateTime.now()
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                          .body(error);
    }
}</code></pre>
                    </div>
                    <div class="alert alert-info border-0 mt-2">
                        <strong>Benefício:</strong> Tratamento centralizado e consistente de erros
                    </div>
                </div>
            </div>
        </div>

        <!-- Exemplos de Código - Arquitetura Java -->
        <div class="row mb-5">
            <div class="col-12">
                <h3 class="subsection-title">
                    <i class="fas fa-code me-2"></i>Exemplos de Código - Arquitetura Java
                </h3>
                <p class="text-center text-muted mb-4">Implementação prática dos conceitos arquiteturais e padrões de projeto</p>
            </div>
        </div>

        <div class="row mb-5">
            <div class="col-md-6 mb-4">
                <div class="code-example-card">
                    <h5><i class="fas fa-layer-group text-success me-2"></i>Arquitetura em Camadas</h5>
                    <div class="code-block">
                        <pre><code class="language-java">// Presentation Layer
@RestController
@RequestMapping("/api/v1/coletas")
public class ColetaController {
    
    @Autowired
    private ColetaService coletaService;
    
    @GetMapping
    public ResponseEntity&lt;List&lt;ColetaDTO&gt;&gt; listarColetas() {
        return ResponseEntity.ok(coletaService.listarTodas());
    }
}

// Business Logic Layer
@Service
@Transactional
public class ColetaService {
    
    @Autowired
    private ColetaRepository coletaRepository;
    
    public List&lt;ColetaDTO&gt; listarTodas() {
        return coletaRepository.findAll()
            .stream()
            .map(this::converterParaDTO)
            .collect(Collectors.toList());
    }
}

// Data Access Layer
public interface ColetaRepository 
    extends JpaRepository&lt;Coleta, Long&gt; {
    
    List&lt;Coleta&gt; findByStatus(StatusColeta status);
}</code></pre>
                    </div>
                    <div class="alert alert-info border-0 mt-2">
                        <strong>Benefício:</strong> Separação clara de responsabilidades entre camadas
                    </div>
                </div>
            </div>
            
            <div class="col-md-6 mb-4">
                <div class="code-example-card">
                    <h5><i class="fas fa-puzzle-piece text-primary me-2"></i>Padrões de Projeto</h5>
                    <div class="code-block">
                        <pre><code class="language-java">// Repository Pattern
public interface UsuarioRepository 
    extends JpaRepository&lt;Usuario, Long&gt; {
    
    Optional&lt;Usuario&gt; findByEmail(String email);
    List&lt;Usuario&gt; findByAtivoTrue();
}

// DTO Pattern
public class UsuarioDTO {
    private Long id;
    private String nome;
    private String email;
    private String perfil;
    
    // Construtor, getters e setters
}

// Service Layer Pattern
@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    public UsuarioDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new UsuarioNaoEncontradoException(id));
        return converterParaDTO(usuario);
    }
}</code></pre>
                    </div>
                    <div class="alert alert-info border-0 mt-2">
                        <strong>Benefício:</strong> Código reutilizável e manutenível
                    </div>
                </div>
            </div>
            
            <div class="col-md-6 mb-4">
                <div class="code-example-card">
                    <h5><i class="fas fa-leaf text-success me-2"></i>Spring Framework</h5>
                    <div class="code-block">
                        <pre><code class="language-java">// Dependency Injection
@Configuration
public class AppConfig {
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

// AOP - Aspect Oriented Programming
@Aspect
@Component
public class LoggingAspect {
    
    @Around("@annotation(LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) 
            throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        log.info("{} executed in {}ms", 
                joinPoint.getSignature(), executionTime);
        return result;
    }
}

// Event Handling
@Component
public class ColetaEventListener {
    
    @EventListener
    public void handleColetaCriada(ColetaCriadaEvent event) {
        log.info("Nova coleta criada: {}", event.getColetaId());
        // Processar evento
    }
}</code></pre>
                    </div>
                    <div class="alert alert-info border-0 mt-2">
                        <strong>Benefício:</strong> Inversão de controle e programação orientada a aspectos
                    </div>
                </div>
            </div>
            
            <div class="col-md-6 mb-4">
                <div class="code-example-card">
                    <h5><i class="fas fa-database text-warning me-2"></i>JPA/Hibernate</h5>
                    <div class="code-block">
                        <pre><code class="language-java">// Entity Mapping
@Entity
@Table(name = "coletas")
public class Coleta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String endereco;
    
    @Enumerated(EnumType.STRING)
    private StatusColeta status;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    
    @OneToMany(mappedBy = "coleta", cascade = CascadeType.ALL)
    private List&lt;Material&gt; materiais = new ArrayList&lt;&gt;();
}

// Custom Queries
@Query("SELECT c FROM Coleta c WHERE c.status = :status " +
       "AND c.usuario.ativo = true")
List&lt;Coleta&gt; findByStatusAndUsuarioAtivo(
    @Param("status") StatusColeta status);

// Native Query
@Query(value = "SELECT * FROM coletas WHERE " +
       "ST_Distance_Sphere(point(longitude, latitude), " +
       "point(:lng, :lat)) &lt; :radius", nativeQuery = true)
List&lt;Coleta&gt; findByProximidade(
    @Param("lat") double lat, 
    @Param("lng") double lng, 
    @Param("radius") double radius);</code></pre>
                    </div>
                    <div class="alert alert-info border-0 mt-2">
                        <strong>Benefício:</strong> Mapeamento objeto-relacional eficiente e flexível
                    </div>
                </div>
            </div>
            
            <div class="col-md-6 mb-4">
                <div class="code-example-card">
                    <h5><i class="fas fa-cogs text-info me-2"></i>Gerenciamento de Transações</h5>
                    <div class="code-block">
                        <pre><code class="language-java">// Transação Declarativa
@Service
@Transactional
public class ColetaService {
    
    @Transactional(readOnly = true)
    public List&lt;ColetaDTO&gt; listarTodas() {
        return coletaRepository.findAll()
            .stream()
            .map(this::converterParaDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional(rollbackFor = Exception.class)
    public ColetaDTO criarColeta(ColetaDTO coletaDTO) {
        // Validações e lógica de negócio
        Coleta coleta = converterParaEntidade(coletaDTO);
        Coleta coletaSalva = coletaRepository.save(coleta);
        return converterParaDTO(coletaSalva);
    }
}

// Transação Programática
@Autowired
private TransactionTemplate transactionTemplate;

public void processarColetasEmLote(List&lt;Coleta&gt; coletas) {
    transactionTemplate.execute(status -&gt; {
        try {
            for (Coleta coleta : coletas) {
                processarColeta(coleta);
            }
            return null;
        } catch (Exception e) {
            status.setRollbackOnly();
            throw e;
        }
    });
}</code></pre>
                    </div>
                    <div class="alert alert-info border-0 mt-2">
                        <strong>Benefício:</strong> Controle granular de transações e rollback automático
                    </div>
                </div>
            </div>
            
            <div class="col-md-6 mb-4">
                <div class="code-example-card">
                    <h5><i class="fas fa-shield-alt text-danger me-2"></i>Spring Security</h5>
                    <div class="code-block">
                        <pre><code class="language-java">// Configuração de Segurança
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) 
            throws Exception {
        http.authorizeHttpRequests(auth -&gt; auth
            .requestMatchers("/api/v1/auth/**").permitAll()
            .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated()
        )
        .csrf(csrf -&gt; csrf.disable())
        .sessionManagement(session -&gt; 
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        
        return http.build();
    }
}

// JWT Authentication
@Component
public class JwtAuthenticationFilter 
        extends OncePerRequestFilter {
    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String token = extractToken(request);
        if (token != null && jwtService.validateToken(token)) {
            String username = jwtService.extractUsername(token);
            // Configurar autenticação
        }
        filterChain.doFilter(request, response);
    }
}</code></pre>
                    </div>
                    <div class="alert alert-info border-0 mt-2">
                        <strong>Benefício:</strong> Segurança robusta com autenticação JWT
                    </div>
                </div>
            </div>
        </div>

        <!-- Tecnologias e Frameworks -->
        <div class="row mb-5">
            <div class="col-12">
                <h3 class="subsection-title">
                    <i class="fas fa-tools me-2"></i>Tecnologias e Frameworks
                </h3>
            </div>
        </div>

        <div class="row mb-5">
            <div class="col-md-6 mb-4">
                <div class="concept-card">
                    <h4><i class="fab fa-java fa-2x text-primary me-3"></i>Java JDK 17</h4>
                    <p>Versão LTS com recursos modernos</p>
                    <div class="alert alert-info border-0">
                        <strong>Recursos:</strong> Records, Pattern Matching, Sealed Classes
                    </div>
                </div>
            </div>
            
            <div class="col-md-6 mb-4">
                <div class="concept-card">
                    <h4><i class="fas fa-leaf fa-2x text-success me-3"></i>Spring Boot 3.2.0</h4>
                    <p>Framework principal com auto-configuração</p>
                    <div class="alert alert-info border-0">
                        <strong>Características:</strong> Auto-configuração e embedded server
                    </div>
                </div>
            </div>
            
            <div class="col-md-6 mb-4">
                <div class="concept-card">
                    <h4><i class="fas fa-database fa-2x text-warning me-3"></i>Spring Data JPA</h4>
                    <p>Mapeamento objeto-relacional</p>
                    <div class="alert alert-info border-0">
                        <strong>Funcionalidades:</strong> Repositories automáticos e queries
                    </div>
                </div>
            </div>
            
            <div class="col-md-6 mb-4">
                <div class="concept-card">
                    <h4><i class="fas fa-shield-alt fa-2x text-danger me-3"></i>Spring Security</h4>
                    <p>Sistema de segurança com JWT</p>
                    <div class="alert alert-info border-0">
                        <strong>Recursos:</strong> Autenticação, autorização e CORS
                    </div>
                </div>
            </div>
        </div>

        <!-- Arquitetura Empresarial e Padrões Avançados -->
        <div class="row mb-5">
            <div class="col-12">
                <h2 class="section-title">
                    <i class="fas fa-building me-3"></i>Arquitetura Empresarial e Padrões Avançados
                </h2>
                <p class="text-center text-muted mb-5">Implementação de padrões arquiteturais empresariais e conceitos avançados de engenharia de software</p>
            </div>
        </div>

        <div class="row mb-5">
            <div class="col-12 mb-4">
                <div class="concept-section">
                    <h3 class="concept-title">
                        <i class="fas fa-layer-group fa-2x text-success me-3"></i>Arquitetura em Camadas (Layered Architecture)
                    </h3>
                    <div class="concept-content">
                        <p class="concept-description">
                            A arquitetura em camadas é um padrão arquitetural fundamental que organiza o sistema em 
                            camadas horizontais bem definidas, cada uma com responsabilidades específicas. Esta 
                            arquitetura promove a separação de responsabilidades, facilita a manutenção e permite 
                            a evolução independente de cada camada.
                        </p>
                        
                        <div class="concept-details">
                            <h5><i class="fas fa-lightbulb text-warning me-2"></i>Princípios da Arquitetura em Camadas</h5>
                            <p>
                                A arquitetura em camadas segue o princípio de dependência unidirecional, onde cada 
                                camada só pode depender das camadas abaixo dela. Isso cria um sistema modular onde 
                                mudanças em uma camada não afetam as camadas superiores, desde que a interface 
                                seja mantida.
                            </p>
                            
                            <h5><i class="fas fa-code text-primary me-2"></i>Implementação no Sistema</h5>
                            <p>
                                Nosso sistema implementa uma arquitetura em 4 camadas principais:
                            </p>
                            <div class="layer-explanation">
                                <div class="layer-item">
                                    <h6><i class="fas fa-desktop text-primary me-2"></i>Presentation Layer (Controllers)</h6>
                                    <p>
                                        Responsável pela interface com o usuário, validação de entrada e formatação 
                                        de saída. Utiliza anotações Spring como <code>@RestController</code>, 
                                        <code>@RequestMapping</code> e <code>@Valid</code> para validação.
                                    </p>
                                </div>
                                
                                <div class="layer-item">
                                    <h6><i class="fas fa-cogs text-success me-2"></i>Business Logic Layer (Services)</h6>
                                    <p>
                                        Contém toda a lógica de negócio, regras de validação complexas e orquestração 
                                        de operações. Esta camada é independente de tecnologias específicas e pode 
                                        ser facilmente testada e reutilizada.
                                    </p>
                                </div>
                                
                                <div class="layer-item">
                                    <h6><i class="fas fa-database text-warning me-2"></i>Data Access Layer (Repositories)</h6>
                                    <p>
                                        Abstrai o acesso aos dados, implementando o padrão Repository. Esta camada 
                                        esconde os detalhes de implementação da persistência e fornece uma interface 
                                        limpa para operações de dados.
                                    </p>
                                </div>
                                
                                <div class="layer-item">
                                    <h6><i class="fas fa-server text-danger me-2"></i>Infrastructure Layer</h6>
                                    <p>
                                        Gerencia configurações, conexões de banco de dados, segurança e outros 
                                        aspectos técnicos. Inclui configurações Spring, beans de configuração e 
                                        interceptors de segurança.
                                    </p>
                                </div>
                            </div>
                            
                            <h5><i class="fas fa-check-circle text-success me-2"></i>Vantagens da Arquitetura em Camadas</h5>
                            <ul>
                                <li><strong>Separação de Responsabilidades:</strong> Cada camada tem uma função específica e bem definida</li>
                                <li><strong>Manutenibilidade:</strong> Mudanças em uma camada não afetam outras</li>
                                <li><strong>Testabilidade:</strong> Cada camada pode ser testada independentemente</li>
                                <li><strong>Reutilização:</strong> Camadas podem ser reutilizadas em diferentes contextos</li>
                                <li><strong>Escalabilidade:</strong> Facilita a distribuição de responsabilidades em diferentes servidores</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="col-12 mb-4">
                <div class="concept-section">
                    <h3 class="concept-title">
                        <i class="fas fa-puzzle-piece fa-2x text-primary me-3"></i>Padrões de Projeto (Design Patterns)
                    </h3>
                    <div class="concept-content">
                        <p class="concept-description">
                            Os padrões de projeto são soluções reutilizáveis para problemas comuns de design de software. 
                            Eles representam as melhores práticas desenvolvidas pela comunidade de desenvolvedores ao 
                            longo dos anos e são essenciais para criar código limpo, manutenível e extensível.
                        </p>
                        
                        <div class="concept-details">
                            <h5><i class="fas fa-lightbulb text-warning me-2"></i>Padrões Criacionais</h5>
                            <p>
                                Padrões criacionais lidam com a criação de objetos, fornecendo flexibilidade na 
                                criação e reutilização de objetos complexos:
                            </p>
                            <ul>
                                <li><strong>Singleton:</strong> Garante que uma classe tenha apenas uma instância</li>
                                <li><strong>Factory Method:</strong> Define uma interface para criar objetos</li>
                                <li><strong>Builder:</strong> Constrói objetos complexos passo a passo</li>
                                <li><strong>Dependency Injection:</strong> Inverte o controle de criação de dependências</li>
                            </ul>
                            
                            <h5><i class="fas fa-code text-primary me-2"></i>Padrões Estruturais</h5>
                            <p>
                                Padrões estruturais lidam com a composição de classes e objetos:
                            </p>
                            <ul>
                                <li><strong>Adapter:</strong> Permite que interfaces incompatíveis trabalhem juntas</li>
                                <li><strong>Decorator:</strong> Adiciona responsabilidades dinamicamente</li>
                                <li><strong>Facade:</strong> Fornece uma interface simplificada para um subsistema complexo</li>
                                <li><strong>Proxy:</strong> Controla o acesso a um objeto</li>
                            </ul>
                            
                            <h5><i class="fas fa-cogs text-info me-2"></i>Padrões Comportamentais</h5>
                            <p>
                                Padrões comportamentais lidam com a comunicação entre objetos:
                            </p>
                            <ul>
                                <li><strong>Observer:</strong> Define uma dependência um-para-muitos entre objetos</li>
                                <li><strong>Strategy:</strong> Define uma família de algoritmos intercambiáveis</li>
                                <li><strong>Command:</strong> Encapsula uma requisição como um objeto</li>
                                <li><strong>Template Method:</strong> Define o esqueleto de um algoritmo</li>
                            </ul>
                            
                            <h5><i class="fas fa-check-circle text-success me-2"></i>Implementação no Sistema</h5>
                            <p>
                                Nosso sistema implementa vários padrões de projeto:
                            </p>
                            <div class="pattern-examples">
                                <div class="pattern-item">
                                    <h6><i class="fas fa-database text-primary me-2"></i>Repository Pattern</h6>
                                    <p>
                                        Abstrai a camada de acesso a dados, fornecendo uma interface consistente 
                                        para operações de persistência.
                                    </p>
                                </div>
                                
                                <div class="pattern-item">
                                    <h6><i class="fas fa-exchange-alt text-success me-2"></i>DTO Pattern</h6>
                                    <p>
                                        Transfere dados entre camadas sem expor a estrutura interna das entidades.
                                    </p>
                                </div>
                                
                                <div class="pattern-item">
                                    <h6><i class="fas fa-cogs text-warning me-2"></i>Service Layer Pattern</h6>
                                    <p>
                                        Centraliza a lógica de negócio e coordena operações entre diferentes componentes.
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="col-12 mb-4">
                <div class="concept-section">
                    <h3 class="concept-title">
                        <i class="fas fa-leaf fa-2x text-success me-3"></i>Spring Framework e Ecossistema
                    </h3>
                    <div class="concept-content">
                        <p class="concept-description">
                            O Spring Framework é um dos frameworks mais populares para desenvolvimento Java empresarial. 
                            Ele fornece uma infraestrutura abrangente para o desenvolvimento de aplicações Java, 
                            simplificando o desenvolvimento e promovendo boas práticas de programação.
                        </p>
                        
                        <div class="concept-details">
                            <h5><i class="fas fa-lightbulb text-warning me-2"></i>Core Spring Framework</h5>
                            <p>
                                O núcleo do Spring Framework fornece funcionalidades fundamentais:
                            </p>
                            <ul>
                                <li><strong>IoC Container:</strong> Gerencia o ciclo de vida dos beans e suas dependências</li>
                                <li><strong>Dependency Injection:</strong> Inverte o controle de criação de dependências</li>
                                <li><strong>AOP (Aspect-Oriented Programming):</strong> Permite separar preocupações transversais</li>
                                <li><strong>Event Handling:</strong> Sistema de eventos para comunicação entre componentes</li>
                            </ul>
                            
                            <h5><i class="fas fa-code text-primary me-2"></i>Spring Boot</h5>
                            <p>
                                Spring Boot simplifica a configuração e inicialização de aplicações Spring:
                            </p>
                            <ul>
                                <li><strong>Auto-configuração:</strong> Configura automaticamente beans baseado no classpath</li>
                                <li><strong>Starter POMs:</strong> Dependências pré-configuradas para diferentes cenários</li>
                                <li><strong>Embedded Servers:</strong> Servidores web integrados (Tomcat, Jetty, Undertow)</li>
                                <li><strong>Actuator:</strong> Monitoramento e métricas da aplicação</li>
                            </ul>
                            
                            <h5><i class="fas fa-cogs text-info me-2"></i>Spring Data JPA</h5>
                            <p>
                                Simplifica o acesso a dados com JPA:
                            </p>
                            <ul>
                                <li><strong>Repository Pattern:</strong> Interface para operações CRUD básicas</li>
                                <li><strong>Query Methods:</strong> Geração automática de queries baseada no nome do método</li>
                                <li><strong>JPQL Support:</strong> Suporte a queries JPQL customizadas</li>
                                <li><strong>Native Queries:</strong> Execução de queries SQL nativas</li>
                            </ul>
                            
                            <h5><i class="fas fa-shield-alt text-danger me-2"></i>Spring Security</h5>
                            <p>
                                Framework de segurança robusto e extensível:
                            </p>
                            <ul>
                                <li><strong>Authentication:</strong> Múltiplos mecanismos de autenticação</li>
                                <li><strong>Authorization:</strong> Controle de acesso baseado em roles e permissões</li>
                                <li><strong>CSRF Protection:</strong> Proteção contra ataques Cross-Site Request Forgery</li>
                                <li><strong>Session Management:</strong> Gerenciamento seguro de sessões</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="col-12 mb-4">
                <div class="concept-section">
                    <h3 class="concept-title">
                        <i class="fas fa-database fa-2x text-warning me-3"></i>JPA/Hibernate e Persistência de Dados
                    </h3>
                    <div class="concept-content">
                        <p class="concept-description">
                            JPA (Java Persistence API) é a especificação padrão para mapeamento objeto-relacional em Java. 
                            Hibernate é a implementação mais popular desta especificação, fornecendo funcionalidades 
                            avançadas para persistência de dados.
                        </p>
                        
                        <div class="concept-details">
                            <h5><i class="fas fa-lightbulb text-warning me-2"></i>Conceitos Fundamentais do JPA</h5>
                            <p>
                                JPA introduz conceitos fundamentais para mapeamento objeto-relacional:
                            </p>
                            <ul>
                                <li><strong>Entity:</strong> Classes Java que representam tabelas do banco de dados</li>
                                <li><strong>EntityManager:</strong> Interface principal para operações de persistência</li>
                                <li><strong>Persistence Context:</strong> Conjunto de entidades gerenciadas</li>
                                <li><strong>Transaction:</strong> Unidade de trabalho atômica</li>
                            </ul>
                            
                            <h5><i class="fas fa-code text-primary me-2"></i>Anotações JPA Principais</h5>
                            <p>
                                O sistema utiliza várias anotações JPA para mapeamento:
                            </p>
                            <ul>
                                <li><strong>@Entity:</strong> Marca uma classe como entidade persistente</li>
                                <li><strong>@Table:</strong> Especifica o nome da tabela no banco</li>
                                <li><strong>@Id:</strong> Marca o campo como chave primária</li>
                                <li><strong>@GeneratedValue:</strong> Define a estratégia de geração de IDs</li>
                                <li><strong>@Column:</strong> Configura mapeamento de colunas</li>
                                <li><strong>@OneToMany/@ManyToOne:</strong> Define relacionamentos entre entidades</li>
                            </ul>
                            
                            <h5><i class="fas fa-cogs text-info me-2"></i>Funcionalidades Avançadas do Hibernate</h5>
                            <p>
                                Hibernate oferece funcionalidades além da especificação JPA:
                            </p>
                            <ul>
                                <li><strong>Lazy Loading:</strong> Carregamento sob demanda de relacionamentos</li>
                                <li><strong>Eager Loading:</strong> Carregamento antecipado de relacionamentos</li>
                                <li><strong>Batch Processing:</strong> Processamento em lote para grandes volumes</li>
                                <li><strong>Query Cache:</strong> Cache de queries para melhor performance</li>
                                <li><strong>Second Level Cache:</strong> Cache compartilhado entre sessões</li>
                            </ul>
                            
                            <h5><i class="fas fa-check-circle text-success me-2"></i>Boas Práticas de Persistência</h5>
                            <ul>
                                <li><strong>Lazy Loading:</strong> Use para relacionamentos que não são sempre necessários</li>
                                <li><strong>Batch Operations:</strong> Agrupe operações para melhor performance</li>
                                <li><strong>Connection Pooling:</strong> Configure adequadamente o pool de conexões</li>
                                <li><strong>Transaction Management:</strong> Mantenha transações curtas e focadas</li>
                                <li><strong>Query Optimization:</strong> Use índices e otimize queries complexas</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="col-12 mb-4">
                <div class="concept-section">
                    <h3 class="concept-title">
                        <i class="fas fa-cogs fa-2x text-info me-3"></i>Gerenciamento de Transações e Concorrência
                    </h3>
                    <div class="concept-content">
                        <p class="concept-description">
                            O gerenciamento de transações é fundamental em aplicações empresariais para garantir 
                            a consistência dos dados. O Spring Framework fornece um sistema robusto de gerenciamento 
                            de transações declarativas e programáticas.
                        </p>
                        
                        <div class="concept-details">
                            <h5><i class="fas fa-lightbulb text-warning me-2"></i>Propriedades ACID</h5>
                            <p>
                                Transações devem garantir as propriedades ACID:
                            </p>
                            <ul>
                                <li><strong>Atomicity:</strong> Todas as operações da transação são executadas ou nenhuma</li>
                                <li><strong>Consistency:</strong> A transação leva o banco de um estado válido para outro</li>
                                <li><strong>Isolation:</strong> Transações concorrentes não interferem entre si</li>
                                <li><strong>Durability:</strong> Mudanças são permanentes após commit</li>
                            </ul>
                            
                            <h5><i class="fas fa-code text-primary me-2"></i>Níveis de Isolamento</h5>
                            <p>
                                Diferentes níveis de isolamento para transações concorrentes:
                            </p>
                            <ul>
                                <li><strong>READ UNCOMMITTED:</strong> Permite leitura de dados não commitados (dirty read)</li>
                                <li><strong>READ COMMITTED:</strong> Só permite leitura de dados commitados</li>
                                <li><strong>REPEATABLE READ:</strong> Garante leituras consistentes durante a transação</li>
                                <li><strong>SERIALIZABLE:</strong> Nível mais alto de isolamento, execução serial</li>
                            </ul>
                            
                            <h5><i class="fas fa-cogs text-info me-2"></i>Implementação no Spring</h5>
                            <p>
                                O sistema utiliza gerenciamento de transações declarativo:
                            </p>
                            <ul>
                                <li><strong>@Transactional:</strong> Anotação para métodos transacionais</li>
                                <li><strong>Propagation:</strong> Define como a transação se propaga</li>
                                <li><strong>Isolation:</strong> Especifica o nível de isolamento</li>
                                <li><strong>Timeout:</strong> Define timeout para transações longas</li>
                                <li><strong>Rollback:</strong> Configura condições para rollback automático</li>
                            </ul>
                            
                            <h5><i class="fas fa-exclamation-triangle text-warning me-2"></i>Problemas de Concorrência</h5>
                            <p>
                                Aplicações concorrentes podem enfrentar problemas:
                            </p>
                            <ul>
                                <li><strong>Lost Update:</strong> Uma transação sobrescreve mudanças de outra</li>
                                <li><strong>Dirty Read:</strong> Leitura de dados não commitados</li>
                                <li><strong>Non-repeatable Read:</strong> Dados mudam durante a transação</li>
                                <li><strong>Phantom Read:</strong> Novas linhas aparecem durante a transação</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Funcionalidades Principais -->
        <div class="row mb-5">
            <div class="col-12">
                <h3 class="subsection-title">
                    <i class="fas fa-star me-2"></i>Funcionalidades Arquiteturais Implementadas
                </h3>
            </div>
        </div>

        <div class="row mb-5">
            <div class="col-md-6 mb-4">
                <div class="concept-card">
                    <h4><i class="fas fa-layer-group fa-2x text-primary me-3"></i>Arquitetura em Camadas</h4>
                    <p>Separação clara de responsabilidades entre Presentation, Business, Data e Infrastructure</p>
                    <div class="alert alert-success border-0">
                        <strong>Implementação:</strong> Controllers, Services, Repositories e Configurations
                    </div>
                </div>
            </div>
            
            <div class="col-md-6 mb-4">
                <div class="concept-card">
                    <h4><i class="fas fa-puzzle-piece fa-2x text-success me-3"></i>Padrões de Projeto</h4>
                    <p>Repository, DTO, Service Layer e Factory patterns implementados</p>
                    <div class="alert alert-success border-0">
                        <strong>Localização:</strong> Todas as camadas do sistema
                    </div>
                </div>
            </div>
            
            <div class="col-md-6 mb-4">
                <div class="concept-card">
                    <h4><i class="fas fa-leaf fa-2x text-warning me-3"></i>Spring Framework</h4>
                    <p>Dependency Injection, AOP, Event Handling e configuração automática</p>
                    <div class="alert alert-success border-0">
                        <strong>Localização:</strong> Configurações e anotações em todo o sistema
                    </div>
                </div>
            </div>
            
            <div class="col-md-6 mb-4">
                <div class="concept-card">
                    <h4><i class="fas fa-database fa-2x text-danger me-3"></i>JPA/Hibernate</h4>
                    <p>Mapeamento objeto-relacional com lazy loading e queries otimizadas</p>
                    <div class="alert alert-success border-0">
                        <strong>Localização:</strong> Entidades e Repositories
                    </div>
                </div>
            </div>
            
            <div class="col-md-6 mb-4">
                <div class="concept-card">
                    <h4><i class="fas fa-cogs fa-2x text-info me-3"></i>Gerenciamento de Transações</h4>
                    <p>Transações declarativas e programáticas com controle de isolamento</p>
                    <div class="alert alert-success border-0">
                        <strong>Localização:</strong> Services e métodos transacionais
                    </div>
                </div>
            </div>
            
            <div class="col-md-6 mb-4">
                <div class="concept-card">
                    <h4><i class="fas fa-shield-alt fa-2x text-dark me-3"></i>Spring Security</h4>
                    <p>Autenticação JWT, autorização baseada em roles e proteção CSRF</p>
                    <div class="alert alert-success border-0">
                        <strong>Localização:</strong> SecurityConfig e filtros de autenticação
                    </div>
                </div>
            </div>
        </div>

        <!-- Call to Action -->
        <div class="row mb-5">
            <div class="col-12">
                <div class="card border-0 shadow-lg" style="background: #4caf50; color: white;">
                    <div class="card-body text-center p-5">
                        <h2 class="mb-4">
                            <i class="fas fa-rocket me-3"></i>🚀 Explore a Arquitetura Java!
                        </h2>
                        <p class="lead mb-4 opacity-90">Navegue pelo sistema e descubra todos os conceitos arquiteturais implementados</p>
                        <div class="d-flex justify-content-center gap-3 flex-wrap">
                            <a href="/sistema/materiais" class="btn btn-light btn-lg">
                                <i class="fas fa-recycle me-2"></i>Gerenciar Materiais
                            </a>
                            <a href="/sistema/coletas" class="btn btn-light btn-lg">
                                <i class="fas fa-box me-2"></i>Gerenciar Coletas
                            </a>
                            <a href="/sistema/rotas" class="btn btn-light btn-lg">
                                <i class="fas fa-route me-2"></i>Otimizar Rotas
                            </a>
                            <a href="/sistema/mapa" class="btn btn-light btn-lg">
                                <i class="fas fa-map me-2"></i>Visualizar Mapa
                            </a>
                        </div>
                        <div class="mt-4">
                            <p class="mb-2 opacity-90"><strong>Conceitos Demonstrados:</strong></p>
                            <div class="d-flex justify-content-center gap-2 flex-wrap">
                                <span class="badge bg-light text-dark">Arquitetura em Camadas</span>
                                <span class="badge bg-light text-dark">Padrões de Projeto</span>
                                <span class="badge bg-light text-dark">Spring Framework</span>
                                <span class="badge bg-light text-dark">JPA/Hibernate</span>
                                <span class="badge bg-light text-dark">Transações</span>
                                <span class="badge bg-light text-dark">Spring Security</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Estilos CSS -->
    <style>
        .section-title {
            color: #2c3e50;
            border-bottom: 3px solid #007bff;
            padding-bottom: 10px;
            margin-bottom: 30px;
        }
        
        .subsection-title {
            color: #34495e;
            border-bottom: 2px solid #6c757d;
            padding-bottom: 8px;
            margin-bottom: 25px;
        }
        
        .concept-card, .algorithm-card {
            background: white;
            border-radius: 15px;
            padding: 25px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            border: 1px solid #e9ecef;
            transition: all 0.3s ease;
            height: 100%;
        }
        
        .concept-card:hover, .algorithm-card:hover {
            transform: translateY(-3px);
            box-shadow: 0 8px 25px rgba(0,0,0,0.15);
        }
        
        .concept-card h4, .algorithm-card h4 {
            color: #2c3e50;
            margin-bottom: 15px;
            font-weight: 600;
        }
        
        .concept-card p, .algorithm-card p {
            color: #555;
            line-height: 1.6;
            margin-bottom: 15px;
        }
        
        .code-example-card {
            background: white;
            border: 1px solid #dee2e6;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 20px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            transition: all 0.3s ease;
        }
        
        .code-example-card:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0,0,0,0.15);
        }
        
        .code-example-card h5 {
            color: #495057;
            margin-bottom: 15px;
            font-weight: 600;
            border-bottom: 2px solid #007bff;
            padding-bottom: 8px;
        }
        
        .code-block {
            background: #2d3748;
            border-radius: 6px;
            padding: 15px;
            margin-bottom: 10px;
            overflow-x: auto;
            border-left: 4px solid #007bff;
        }
        
        .code-block pre {
            margin: 0;
            color: #e2e8f0;
            font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
            font-size: 13px;
            line-height: 1.4;
        }
        
        .code-block code {
            background: none;
            color: inherit;
            padding: 0;
            font-size: inherit;
        }
        
        .alert {
            border-radius: 6px;
            font-size: 14px;
        }
        
        /* Novos estilos para seções de conceitos */
        .concept-section {
            background: white;
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            border: 1px solid #e9ecef;
            margin-bottom: 30px;
            transition: all 0.3s ease;
        }

        .concept-section:hover {
            transform: translateY(-3px);
            box-shadow: 0 8px 25px rgba(0,0,0,0.15);
        }

        .concept-title {
            color: #2c3e50;
            border-bottom: 3px solid #3498db;
            padding-bottom: 15px;
            margin-bottom: 25px;
            font-weight: 600;
            font-size: 1.5em;
        }

        .concept-content {
            color: #34495e;
        }

        .concept-description {
            font-size: 1.1em;
            line-height: 1.6;
            color: #34495e;
            margin-bottom: 25px;
            text-align: justify;
        }

        .concept-details h5 {
            color: #2c3e50;
            margin-top: 25px;
            margin-bottom: 15px;
            font-weight: 600;
            border-left: 4px solid #3498db;
            padding-left: 15px;
            font-size: 1.1em;
        }

        .concept-details p {
            color: #555;
            line-height: 1.6;
            margin-bottom: 15px;
            text-align: justify;
        }

        .concept-details ul {
            margin-bottom: 20px;
            padding-left: 20px;
        }

        .concept-details li {
            color: #555;
            line-height: 1.6;
            margin-bottom: 8px;
        }

        .concept-details code {
            background: #f8f9fa;
            color: #e74c3c;
            padding: 2px 6px;
            border-radius: 4px;
            font-family: 'Courier New', monospace;
            font-size: 0.9em;
        }

        /* Estilos para exemplos específicos */
        .layer-explanation, .service-examples, .dto-examples, .exception-examples {
            background: #f8f9fa;
            border-radius: 10px;
            padding: 20px;
            margin: 20px 0;
            border: 1px solid #e9ecef;
        }

        .layer-item, .service-item, .dto-item, .exception-item {
            background: white;
            border-radius: 8px;
            padding: 15px;
            margin-bottom: 15px;
            border-left: 4px solid #3498db;
            transition: all 0.2s ease;
        }

        .layer-item:hover, .service-item:hover, .dto-item:hover, .exception-item:hover {
            transform: translateX(5px);
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }

        .layer-item:last-child, .service-item:last-child, .dto-item:last-child, .exception-item:last-child {
            margin-bottom: 0;
        }

        .layer-item h6, .service-item h6, .dto-item h6, .exception-item h6 {
            color: #2c3e50;
            margin-bottom: 10px;
            font-weight: 600;
            font-size: 1em;
        }

        .layer-item p, .service-item p, .dto-item p, .exception-item p {
            color: #555;
            margin: 0;
            line-height: 1.5;
            font-size: 0.95em;
        }
        
        @media (max-width: 768px) {
            .code-block {
                font-size: 11px;
                padding: 10px;
            }
            
            .code-example-card {
                padding: 15px;
            }
        }
    </style>

    <!-- Scripts -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/components/prism-core.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/prism/1.29.0/plugins/autoloader/prism-autoloader.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Animar cards ao fazer scroll
            const observerOptions = {
                threshold: 0.1,
                rootMargin: '0px 0px -50px 0px'
            };
            
            const observer = new IntersectionObserver(function(entries) {
                entries.forEach(entry => {
                    if (entry.isIntersecting) {
                        entry.target.style.opacity = '1';
                        entry.target.style.transform = 'translateY(0)';
                    }
                });
            }, observerOptions);
            
            // Observar todos os cards
            document.querySelectorAll('.concept-card, .algorithm-card, .code-example-card').forEach(card => {
                card.style.opacity = '0';
                card.style.transform = 'translateY(20px)';
                card.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
                observer.observe(card);
            });
        });
    </script>
</body>
</html>
