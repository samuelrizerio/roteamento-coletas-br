package br.com.roteamento.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * CONTROLLER DA ROTA RAIZ - Redirecionamentos principais
 * 
 * CONCEITOS IMPLEMENTADOS:
 * 
 * 1. ROTEAMENTO PRINCIPAL:
 *    - Rota raiz (/) redireciona para página inicial
 *    - Controle de navegação principal
 *    - Redirecionamentos automáticos
 * 
 * 2. ARQUITETURA MVC:
 *    - @Controller para views
 *    - Redirecionamentos com redirect:
 *    - Separação de responsabilidades
 */
@Controller
public class RootController {

    /**
     * ROTA RAIZ - REDIRECIONA PARA PÁGINA INICIAL
     * 
     * @return redirecionamento para página inicial
     */
    @GetMapping("/")
    public String rotaRaiz() {
        return "redirect:/sistema/inicial";
    }
}
