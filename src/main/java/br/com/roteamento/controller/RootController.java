package br.com.roteamento.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller da rota raiz - Redirecionamentos principais
 */
@Controller
public class RootController {

    /**
     * Redireciona para a página inicial do sistema
     * 
     * @return redirecionamento para página inicial
     */
    @GetMapping("/")
    public String rotaRaiz() {
        return "redirect:/sistema/inicial";
    }
}
