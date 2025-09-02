package br.com.roteamento.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.roteamento.dto.ColetaDTO;
import br.com.roteamento.dto.MaterialDTO;
import br.com.roteamento.dto.RotaDTO;
import br.com.roteamento.dto.UsuarioDTO;
import br.com.roteamento.model.Coleta;
import br.com.roteamento.model.Rota;
import br.com.roteamento.service.ColetaService;
import br.com.roteamento.service.MaterialService;
import br.com.roteamento.service.RotaService;

import br.com.roteamento.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * CONTROLLER PRINCIPAL DO SISTEMA - Frontend 100% Java
 * 
 * CONCEITOS IMPLEMENTADOS:
 * 
 * 1. FRONTEND JAVA NATIVO:
 *    - JSP/JSF para interface principal
 *    - Thymeleaf como template engine
 *    - 100% Java sem dependências externas
 * 
 * 2. ARQUITETURA TRADICIONAL:
 *    - Renderização no servidor
 *    - Model-View-Controller (MVC)
 *    - Integração direta com serviços Java
 * 
 * 3. VANTAGENS:
 *    - 100% Java (sem tecnologias externas)
 *    - Deploy único (JAR contém tudo)
 *    - Manutenção simplificada
 *    - Performance otimizada para Java
 */
@Controller
@RequestMapping("/sistema")
@RequiredArgsConstructor
@Slf4j
public class SistemaController {

    private final ColetaService coletaService;
    private final RotaService rotaService;
    private final UsuarioService usuarioService;
    private final MaterialService materialService;

    /**
     * PÁGINA INICIAL DO SISTEMA (DASHBOARD)
     * 
     * @param model modelo para a view
     * @return nome da view inicial
     */
    @GetMapping("/inicial")
    public String paginaInicial(Model model) {
        log.info("Acessando página inicial do sistema");
        
        // Estatísticas para a página inicial
        List<ColetaDTO> coletas = coletaService.listarTodasColetas();
        List<RotaDTO> rotas = rotaService.listarTodasRotas();
        
        model.addAttribute("totalColetas", coletas.size());
        model.addAttribute("totalRotas", rotas.size());
        model.addAttribute("coletasPendentes", coletas.stream()
            .filter(c -> "PENDENTE".equals(c.getStatus())).count());
        model.addAttribute("rotasAtivas", rotas.stream()
            .filter(r -> "ATIVA".equals(r.getStatus())).count());
        
        return "sistema/inicial";
    }

    /**
     * ROTA PRINCIPAL DO SISTEMA - REDIRECIONA PARA PÁGINA INICIAL
     * 
     * @return redirecionamento para página inicial
     */
    @GetMapping("")
    public String sistema() {
        log.info("Acessando rota principal do sistema");
        return "redirect:/sistema/inicial";
    }

    /**
     * PÁGINA DE COLETAS
     * 
     * @param model modelo para a view
     * @param status filtro por status (opcional)
     * @return nome da view de coletas
     */
    @GetMapping("/coletas")
    public String coletas(Model model, @RequestParam(required = false) String status) {
        log.info("Acessando página de coletas, status: {}", status);
        
        List<ColetaDTO> coletas;
        if (status != null && !status.isEmpty()) {
            try {
                Coleta.StatusColeta statusEnum = Coleta.StatusColeta.valueOf(status.toUpperCase());
                coletas = coletaService.buscarColetasPorStatus(statusEnum);
            } catch (IllegalArgumentException e) {
                coletas = coletaService.listarTodasColetas();
            }
        } else {
            coletas = coletaService.listarTodasColetas();
        }
        
        model.addAttribute("coletas", coletas);
        model.addAttribute("statusFiltro", status);
        model.addAttribute("totalColetas", coletas.size());
        
        return "sistema/coletas";
    }

    /**
     * PÁGINA DE ROTAS
     * 
     * @param model modelo para a view
     * @param status filtro por status (opcional)
     * @return nome da view de rotas
     */
    @GetMapping("/rotas")
    public String rotas(Model model, @RequestParam(required = false) String status) {
        log.info("Acessando página de rotas, status: {}", status);
        
        List<RotaDTO> rotas;
        if (status != null && !status.isEmpty()) {
            try {
                Rota.StatusRota statusEnum = Rota.StatusRota.valueOf(status.toUpperCase());
                rotas = rotaService.buscarRotasPorStatus(statusEnum);
            } catch (IllegalArgumentException e) {
                rotas = rotaService.listarTodasRotas();
            }
        } else {
            rotas = rotaService.listarTodasRotas();
        }
        
        model.addAttribute("rotas", rotas);
        model.addAttribute("statusFiltro", status);
        model.addAttribute("totalRotas", rotas.size());
        
        return "sistema/rotas";
    }

    /**
     * PÁGINA DE USUÁRIOS
     * 
     * @param model modelo para a view
     * @return nome da view de usuários
     */
    @GetMapping("/usuarios")
    public String usuarios(Model model) {
        log.info("Acessando página de usuários");
        
        List<UsuarioDTO> usuarios = usuarioService.listarTodosUsuarios();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("totalUsuarios", usuarios.size());
        
        return "sistema/usuarios";
    }

    /**
     * PÁGINA DE MATERIAIS
     * 
     * @param model modelo para a view
     * @return nome da view de materiais
     */
    @GetMapping("/materiais")
    public String materiais(Model model) {
        log.info("Acessando página de materiais");
        
        List<MaterialDTO> materiais = materialService.listarMateriaisAtivos();
        model.addAttribute("materiais", materiais);
        model.addAttribute("totalMateriais", materiais.size());
        
        return "sistema/materiais";
    }

    /**
     * PÁGINA DO MAPA
     * 
     * @param model modelo para a view
     * @return nome da view do mapa
     */
    @GetMapping("/mapa")
    public String mapa(Model model) {
        log.info("Acessando página do mapa");
        
        // Dados para o mapa (coletas e rotas)
        List<ColetaDTO> coletas = coletaService.listarTodasColetas();
        List<RotaDTO> rotas = rotaService.listarTodasRotas();
        
        model.addAttribute("coletas", coletas);
        model.addAttribute("rotas", rotas);
        model.addAttribute("totalPontos", coletas.size() + rotas.size());
        
        return "sistema/mapa";
    }

    /**
     * PÁGINA DE CONFIGURAÇÕES
     * 
     * @param model modelo para a view
     * @return nome da view de configurações
     */
    @GetMapping("/configuracoes")
    public String configuracoes(Model model) {
        log.info("Acessando página de configurações");
        
        // Configurações do sistema
        model.addAttribute("configuracoes", Map.of(
            "maxSearchRadius", "5000",
            "maxRoutingTime", "30",
            "expirationTime", "30",
            "maxRetryAttempts", "3"
        ));
        
        return "sistema/configuracoes";
    }




    
    /**
     * PÁGINA INICIAL DO SISTEMA - VERSÃO HTML DIRETA
     * 
     * @return HTML da página inicial
     */
    @GetMapping("/html")
    @ResponseBody
    public String paginaInicialHtml() {
        log.info("Acessando página inicial do sistema (HTML direto)");
        
        return """
            <!DOCTYPE html>
            <html lang="pt-BR">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Sistema de Roteamento - Coletas</title>
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
                <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
            </head>
            <body class="bg-light">
                <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
                    <div class="container">
                        <a class="navbar-brand" href="#">
                            <i class="fas fa-route me-2"></i>
                            Sistema de Roteamento
                        </a>
                    </div>
                </nav>
                
                <div class="container mt-5">
                    <div class="row">
                        <div class="col-12 text-center mb-5">
                            <h1 class="display-4 text-primary">
                                <i class="fas fa-route me-3"></i>
                                Sistema de Roteamento Programado de Coletas
                            </h1>
                            <p class="lead text-muted">Solução 100% Java para otimização de rotas</p>
                        </div>
                    </div>
                    
                    <div class="row">
                        <div class="col-md-4 mb-4">
                            <div class="card h-100 border-0 shadow-sm">
                                <div class="card-body text-center">
                                    <i class="fas fa-users fa-3x text-primary mb-3"></i>
                                    <h5 class="card-title">Usuários</h5>
                                    <p class="card-text">Gerenciamento de usuários e coletores</p>
                                    <a href="/api/v1/sistema/usuarios" class="btn btn-primary">Acessar</a>
                                </div>
                            </div>
                        </div>
                        
                        <div class="col-md-4 mb-4">
                            <div class="card h-100 border-0 shadow-sm">
                                <div class="card-body text-center">
                                    <i class="fas fa-box fa-3x text-success mb-3"></i>
                                    <h5 class="card-title">Coletas</h5>
                                    <p class="card-text">Controle de coletas e materiais</p>
                                    <a href="/api/v1/sistema/coletas" class="btn btn-success">Acessar</a>
                                </div>
                            </div>
                        </div>
                        
                        <div class="col-md-4 mb-4">
                            <div class="card h-100 border-0 shadow-sm">
                                <div class="card-body text-center">
                                    <i class="fas fa-route fa-3x text-info mb-3"></i>
                                    <h5 class="card-title">Rotas</h5>
                                    <p class="card-text">Otimização e gerenciamento de rotas</p>
                                    <a href="/api/v1/sistema/rotas" class="btn btn-info">Acessar</a>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="row">
                        <div class="col-md-4 mb-4">
                            <div class="card h-100 border-0 shadow-sm">
                                <div class="card-body text-center">
                                    <i class="fas fa-recycle fa-3x text-warning mb-3"></i>
                                    <h5 class="card-title">Materiais</h5>
                                    <p class="card-text">Catálogo de materiais recicláveis</p>
                                    <a href="/api/v1/sistema/materiais" class="btn btn-warning">Acessar</a>
                                </div>
                            </div>
                        </div>
                        
                        <div class="col-md-4 mb-4">
                            <div class="card h-100 border-0 shadow-sm">
                                <div class="card-body text-center">
                                    <i class="fas fa-map fa-3x text-danger mb-3"></i>
                                    <h5 class="card-title">Mapa</h5>
                                    <p class="card-text">Visualização geográfica das coletas</p>
                                    <a href="/api/v1/sistema/mapa" class="btn btn-danger">Acessar</a>
                                </div>
                            </div>
                        </div>
                        
                        <div class="col-md-4 mb-4">
                            <div class="card h-100 border-0 shadow-sm">
                                <div class="card-body text-center">
                                    <i class="fas fa-cogs fa-3x text-secondary mb-3"></i>
                                    <h5 class="card-title">Configurações</h5>
                                    <p class="card-text">Configurações do sistema</p>
                                    <a href="/api/v1/sistema/configuracoes" class="btn btn-secondary">Acessar</a>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="row mt-5">
                        <div class="col-12 text-center">
                            <h3 class="text-muted">Tecnologias Utilizadas</h3>
                            <div class="d-flex justify-content-center flex-wrap gap-2 mt-3">
                                <span class="badge bg-primary fs-6">Spring Boot</span>
                                <span class="badge bg-success fs-6">Java 21</span>
                                <span class="badge bg-info fs-6">H2 Database</span>
                                <span class="badge bg-warning fs-6">Bootstrap 5</span>
                                <span class="badge bg-danger fs-6">Font Awesome</span>
                            </div>
                        </div>
                    </div>
                </div>
                
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
            </body>
            </html>
            """;
    }
}
