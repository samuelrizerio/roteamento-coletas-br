package br.com.roteamento.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CONTROLLER GLOBAL - Endpoints de sistema
 * 
 * CONCEITOS IMPLEMENTADOS:
 * 
 * 1. ENDPOINTS GLOBAIS:
 *    - Health check para monitoramento
 *    - Status do sistema
 *    - Informações básicas de funcionamento
 * 
 * 2. ARQUITETURA REST:
 *    - @RestController para APIs JSON
 *    - Endpoints simples e diretos
 *    - Respostas padronizadas
 */
@RestController
public class GlobalController {

    /**
     * HEALTH CHECK GLOBAL
     * 
     * @return status do sistema
     */
    @GetMapping("/health")
    public String health() {
        return "UP";
    }

    /**
     * HEALTH CHECK PARA API V1
     * 
     * @return status do sistema
     */
    @GetMapping("/api/v1/health")
    public String healthApi() {
        return "UP";
    }

    /**
     * STATUS DO SISTEMA
     * 
     * @return informações básicas do sistema
     */
    @GetMapping("/status")
    public String status() {
        return "Sistema de Roteamento Programado de Coletas - FUNCIONANDO";
    }
}
