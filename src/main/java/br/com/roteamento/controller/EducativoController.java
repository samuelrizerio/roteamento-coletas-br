package br.com.roteamento.controller;

import br.com.roteamento.dto.ColetaDTO;
import br.com.roteamento.service.ColetaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * CONTROLLER EDUCATIVO - Demonstra JSF e JSP para fins educativos
 * 
 * CONCEITOS DIDÁTICOS EXPLICADOS:
 * 
 * 1. DIFERENÇA ENTRE @Controller E @RestController:
 *    - @Controller: Retorna nomes de views (JSP/JSF)
 *    - @RestController: Retorna dados JSON (API REST)
 * 
 * 2. MODEL ATTRIBUTES:
 *    - model.addAttribute(): Adiciona dados para a view
 *    - View acessa dados via EL (Expression Language)
 * 
 * 3. NAVEGAÇÃO ENTRE TECNOLOGIAS:
 *    - JSP: Páginas tradicionais Java EE
 *    - JSF: Componentes Java EE modernos
 *    - REST: API para aplicações modernas
 */
@Controller
@RequestMapping("/educativo")
@RequiredArgsConstructor
@Slf4j
public class EducativoController {

    private final ColetaService coletaService;

    /**
     * PÁGINA INICIAL EDUCATIVA
     * 
     * CONCEITOS:
     * - @Controller: Retorna nome da view
     * - Model: Container para dados da view
     * - Thymeleaf: Template engine padrão do Spring Boot
     * 
     * @param model modelo para a view
     * @return nome da view a ser renderizada
     */
    @GetMapping
    public String paginaInicial(Model model) {
        log.info("Acessando página educativa inicial");
        
        model.addAttribute("titulo", "Sistema de Roteamento - Página Educativa");
        model.addAttribute("descricao", "Demonstração de diferentes tecnologias Java Web");
        model.addAttribute("tecnologias", List.of("JSP", "JSF", "REST API"));
        
        return "educativo/inicial";
    }

    /**
     * PÁGINA JSP EDUCATIVA
     * 
     * CONCEITOS JSP:
     * - JSP: JavaServer Pages (tecnologia tradicional)
     * - Scriptlets: Código Java embutido na página
     * - JSTL: JavaServer Pages Standard Tag Library
     * - EL: Expression Language para acesso a dados
     * 
     * @param model modelo para a view
     * @return nome da view JSP
     */
    @GetMapping("/jsp")
    public String paginaJSP(Model model) {
        log.info("Acessando página JSP educativa");
        
        // Simular dados para demonstrar JSP
        model.addAttribute("coletas", coletaService.listarTodasColetas());
        model.addAttribute("totalColetas", coletaService.listarTodasColetas().size());
        model.addAttribute("tecnologia", "JSP (JavaServer Pages)");
        model.addAttribute("vantagens", List.of(
            "Integração nativa com Java EE",
            "Código Java direto na página",
            "Fácil para desenvolvedores Java",
            "Renderização no servidor"
        ));
        model.addAttribute("desvantagens", List.of(
            "Mistura lógica e apresentação",
            "Difícil manutenção",
            "Menos flexível para frontend moderno",
            "Performance limitada"
        ));
        
        return "educativo/jsp";
    }

    /**
     * PÁGINA JSF EDUCATIVA
     * 
     * CONCEITOS JSF:
     * - JSF: JavaServer Faces (componentes Java EE)
     * - Managed Beans: Beans gerenciados pelo framework
     * - Facelets: Template engine do JSF
     * - Componentes reutilizáveis
     * 
     * @param model modelo para a view
     * @return nome da view JSF
     */
    @GetMapping("/jsf")
    public String paginaJSF(Model model) {
        log.info("Acessando página JSF educativa");
        
        // Dados para demonstrar JSF
        model.addAttribute("coletas", coletaService.listarTodasColetas());
        model.addAttribute("tecnologia", "JSF (JavaServer Faces)");
        model.addAttribute("vantagens", List.of(
            "Componentes reutilizáveis",
            "Gerenciamento de estado",
            "Validação integrada",
            "Eventos e listeners"
        ));
        model.addAttribute("desvantagens", List.of(
            "Curva de aprendizado",
            "Menos flexível que frameworks modernos",
            "Performance em aplicações complexas",
            "Integração limitada com JavaScript"
        ));
        
        return "educativo/jsf";
    }

    /**
     * COMPARAÇÃO ENTRE TECNOLOGIAS
     * 
     * CONCEITOS:
     * - Comparação lado a lado
     * - Demonstração de diferenças
     * - Casos de uso apropriados
     * 
     * @param model modelo para a view
     * @return nome da view de comparação
     */
    @GetMapping("/comparacao")
    public String comparacaoTecnologias(Model model) {
        log.info("Acessando página de comparação de tecnologias");
        
        model.addAttribute("tecnologias", List.of(
            Map.of(
                "nome", "JSP",
                "tipo", "Tradicional",
                "ano", "1999",
                "casoUso", "Aplicações web simples",
                "complexidade", "Baixa"
            ),
            Map.of(
                "nome", "JSF",
                "tipo", "Componente",
                "ano", "2004",
                "casoUso", "Aplicações empresariais",
                "complexidade", "Média"
            ),
            Map.of(
                "nome", "REST API",
                "tipo", "Moderno",
                "ano", "2000+",
                "casoUso", "APIs e aplicações modernas",
                "complexidade", "Média"
            )
        ));
        
        return "educativo/comparacao";
    }

    /**
     * DEMONSTRAÇÃO DE INTEGRAÇÃO
     * 
     * CONCEITOS:
     * - Como integrar diferentes tecnologias
     * - Casos de uso híbridos
     * - Migração gradual
     * 
     * @param model modelo para a view
     * @return nome da view de integração
     */
    @GetMapping("/integracao")
    public String integracaoTecnologias(Model model) {
        log.info("Acessando página de integração de tecnologias");
        
        model.addAttribute("estrategias", List.of(
            "Manter APIs REST para dados",
            "Usar JSP para relatórios",
            "Implementar JSF para formulários complexos"
        ));
        
        return "educativo/integracao";
    }
}
