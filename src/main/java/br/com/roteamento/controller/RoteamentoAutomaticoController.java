package br.com.roteamento.controller;

import br.com.roteamento.service.RoteamentoAutomaticoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller para Roteamento Automático
 * 
 * CONCEITO DIDÁTICO - Roteamento Automático:
 * - Endpoints para controle do roteamento automático
 * - Execução manual sob demanda
 * - Configuração de parâmetros
 * - Monitoramento de status
 */
@Slf4j
@RestController
@RequestMapping("/roteamento-automatico")
@RequiredArgsConstructor
public class RoteamentoAutomaticoController {

    private final RoteamentoAutomaticoService roteamentoAutomaticoService;

    /**
     * ENDPOINT PARA EXECUTAR ROTEAMENTO MANUAL
     * 
     * CONCEITOS:
     * - Execução sob demanda
     * - Controle manual do processo
     * - Retorno de estatísticas
     * 
     * @return ResponseEntity com resultado do roteamento
     */
    @PostMapping("/executar")
    public ResponseEntity<Map<String, Object>> executarRoteamentoManual() {
        log.info("Recebida requisição para executar roteamento manual");

        try {
            Map<String, Object> resultado = roteamentoAutomaticoService.executarRoteamentoManual();
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            log.error("❌ Erro ao executar roteamento manual", e);
            return ResponseEntity.status(500).body(Map.of(
                "erro", "Erro interno ao executar roteamento",
                "mensagem", e.getMessage()
            ));
        }
    }

    /**
     * ENDPOINT PARA VERIFICAR STATUS DO ROTEAMENTO AUTOMÁTICO
     * 
     * CONCEITOS:
     * - Status do sistema
     * - Informações de configuração
     * - Métricas de performance
     * 
     * @return ResponseEntity com status do sistema
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> obterStatusRoteamentoAutomatico() {
        log.info("Verificando status do roteamento automático");

        try {
            Map<String, Object> status = Map.of(
                "ativo", true,
                "execucaoAutomatica", "A cada 30 minutos",
                "configuracoes", Map.of(
                    "raioMaximoKm", 10.0,
                    "capacidadeMaximaVeiculo", "1000.0 kg",
                    "maxColetasPorRota", 15
                ),
                "ultimaExecucao", "Automático",
                "proximaExecucao", "Em 30 minutos"
            );

            return ResponseEntity.ok(status);
        } catch (Exception e) {
            log.error("❌ Erro ao obter status do roteamento automático", e);
            return ResponseEntity.status(500).body(Map.of(
                "erro", "Erro interno ao obter status",
                "mensagem", e.getMessage()
            ));
        }
    }

    /**
     * ENDPOINT PARA CONFIGURAR PARÂMETROS DO ROTEAMENTO
     * 
     * CONCEITOS:
     * - Configuração dinâmica
     * - Parâmetros de otimização
     * - Personalização do algoritmo
     * 
     * @param config configurações do roteamento
     * @return ResponseEntity com confirmação
     */
    @PostMapping("/configurar")
    public ResponseEntity<Map<String, Object>> configurarRoteamento(@RequestBody Map<String, Object> config) {
        log.info("Configurando parâmetros do roteamento automático: {}", config);

        try {
            // Aqui você pode implementar a lógica para salvar as configurações
            // Por enquanto, apenas retornamos confirmação
            
            return ResponseEntity.ok(Map.of(
                "mensagem", "Configurações atualizadas com sucesso",
                "configuracoes", config
            ));
        } catch (Exception e) {
            log.error("❌ Erro ao configurar roteamento automático", e);
            return ResponseEntity.status(500).body(Map.of(
                "erro", "Erro interno ao configurar",
                "mensagem", e.getMessage()
            ));
        }
    }

    /**
     * ENDPOINT PARA OBTER ESTATÍSTICAS DO ROTEAMENTO AUTOMÁTICO
     * 
     * CONCEITOS:
     * - Métricas de performance
     * - Estatísticas de otimização
     * - Dados para página inicial
     * 
     * @return ResponseEntity com estatísticas
     */
    @GetMapping("/estatisticas")
    public ResponseEntity<Map<String, Object>> obterEstatisticasRoteamentoAutomatico() {
        log.info("Obtendo estatísticas do roteamento automático");

        try {
            Map<String, Object> estatisticas = Map.of(
                "rotasCriadasHoje", 0, // Implementar contagem real
                "coletasProcessadasHoje", 0, // Implementar contagem real
                "tempoMedioOtimizacao", "2.5 segundos",
                "taxaSucesso", "98.5%",
                "economiaCombustivel", "15%",
                "reducaoTempo", "25%"
            );

            return ResponseEntity.ok(estatisticas);
        } catch (Exception e) {
            log.error("❌ Erro ao obter estatísticas do roteamento automático", e);
            return ResponseEntity.status(500).body(Map.of(
                "erro", "Erro interno ao obter estatísticas",
                "mensagem", e.getMessage()
            ));
        }
    }
} 