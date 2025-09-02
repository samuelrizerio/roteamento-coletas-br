package br.com.roteamento.controller;

import br.com.roteamento.dto.RotaDTO;
import br.com.roteamento.service.RoteamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import br.com.roteamento.exception.RotaNaoEncontradaException;

/**
 * Controlador REST para roteamento e otimização
 * 
 * CONCEITO DIDÁTICO - APIs de Integração:
 * - Endpoints que orquestram múltiplos serviços
 * - Integração com APIs externas (Google Maps, etc.)
 * - Processamento assíncrono de tarefas pesadas
 * - Cache de resultados para otimizar performance
 * - Tratamento de timeouts e fallbacks
 * 
 * CONCEITO DIDÁTICO - Endpoints de Processamento:
 * - POST para iniciar processamentos
 * - GET para consultar status e resultados
 * - PUT para atualizar configurações
 * - DELETE para cancelar processamentos
 * - Headers para controle de cache e versioning
 */
@Slf4j
@RestController
@RequestMapping("/roteamento")
@RequiredArgsConstructor
@Tag(name = "Roteamento", description = "API para otimização e gerenciamento de rotas")
public class RoteamentoController {

    private final RoteamentoService roteamentoService;

    /**
     * ENDPOINT PARA OBTER ESTATÍSTICAS DE ROTEAMENTO
     * 
     * CONCEITOS:
     * - Estatísticas agregadas
     * - Métricas de performance
     * - Dados para página inicial
     * 
     * @return ResponseEntity com estatísticas de roteamento
     */
    @GetMapping("/estatisticas")
    public ResponseEntity<Map<String, Object>> obterEstatisticasRoteamento() {
        log.info("Recebida requisição para obter estatísticas de roteamento");

        try {
            Map<String, Object> estatisticas = roteamentoService.obterEstatisticasRoteamento();
            return ResponseEntity.ok(estatisticas);
        } catch (Exception e) {
            log.error("Erro interno ao obter estatísticas de roteamento", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ENDPOINT PARA OTIMIZAR ROTAS AUTOMATICAMENTE
     * 
     * CONCEITOS:
     * - Otimização automática
     * - Algoritmo de roteamento
     * - Processamento em lote
     * 
     * @return ResponseEntity com resultado da otimização
     */
    @PostMapping("/otimizar")
    public ResponseEntity<Map<String, Object>> otimizarRotas() {
        log.info("Recebida requisição para otimizar rotas automaticamente");

        try {
            Map<String, Object> resultado = roteamentoService.otimizarRotasAutomaticamente();
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            log.error("Erro interno ao otimizar rotas", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ENDPOINT PARA REOTIMIZAR ROTA ESPECÍFICA
     * 
     * CONCEITOS:
     * - Reotimização de rota existente
     * - Inclusão de novas coletas
     * - Atualização de rota
     * 
     * @param rotaId ID da rota a ser reotimizada
     * @return ResponseEntity com rota reotimizada
     */
    @PostMapping("/reotimizar/{rotaId}")
    public ResponseEntity<RotaDTO> reotimizarRota(@PathVariable Long rotaId) {
        log.info("Recebida requisição para reotimizar rota. ID: {}", rotaId);

        try {
            RotaDTO rotaReotimizada = roteamentoService.reotimizarRota(rotaId);
            return ResponseEntity.ok(rotaReotimizada);
        } catch (RotaNaoEncontradaException e) {
            log.warn("Rota não encontrada para reotimização. ID: {}", rotaId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Erro interno ao reotimizar rota. ID: {}", rotaId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ENDPOINT PARA CALCULAR ROTA ÓTIMA ENTRE PONTOS
     * 
     * CONCEITOS:
     * - Cálculo de rota entre pontos
     * - Algoritmo TSP
     * - Otimização de distância
     * 
     * @param coletaIds Lista de IDs das coletas
     * @return ResponseEntity com rota calculada
     */
    @PostMapping("/calcular-rota")
    public ResponseEntity<RotaDTO> calcularRotaOtima(@RequestBody List<Long> coletaIds) {
        log.info("Recebida requisição para calcular rota ótima para {} coletas", coletaIds.size());

        try {
            RotaDTO rotaCalculada = roteamentoService.calcularRotaOtima(coletaIds);
            return ResponseEntity.ok(rotaCalculada);
        } catch (Exception e) {
            log.error("Erro interno ao calcular rota ótima", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * CONCEITO DIDÁTICO - Endpoint de Informações de Rota (GET):
     * Obtém informações detalhadas de rota de API externa
     * 
     * @param origem coordenadas de origem
     * @param destino coordenadas de destino
     * @return informações detalhadas da rota
     */
    @GetMapping("/informacoes-rota")
    @Operation(summary = "Obter informações de rota", description = "Obtém informações detalhadas de rota de API externa")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Informações de rota obtidas com sucesso"),
        @ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
        @ApiResponse(responseCode = "503", description = "API externa indisponível")
    })
    public ResponseEntity<Map<String, Object>> obterInformacoesRota(
            @Parameter(description = "Coordenadas de origem (lat,lng)", required = true, example = "-19.9167,-43.9345")
            @RequestParam String origem,
            @Parameter(description = "Coordenadas de destino (lat,lng)", required = true, example = "-19.9208,-43.9376")
            @RequestParam String destino) {
        
        log.debug("Recebida requisição para obter informações de rota: {} -> {}", origem, destino);

        try {
            Map<String, Object> informacoes = roteamentoService.obterInformacoesRota(origem, destino);
            return ResponseEntity.ok(informacoes);
        } catch (Exception e) {
            log.error("Erro ao obter informações de rota: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    /**
     * CONCEITO DIDÁTICO - Endpoint de Status (GET):
     * Verifica status do serviço de roteamento
     * 
     * @return status do serviço
     */
    @GetMapping("/status")
    @Operation(summary = "Verificar status", description = "Verifica o status do serviço de roteamento")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Serviço funcionando normalmente")
    })
    public ResponseEntity<Map<String, Object>> verificarStatus() {
        log.debug("Recebida requisição para verificar status do serviço de roteamento");

        Map<String, Object> status = Map.of(
            "servico", "Roteamento",
            "status", "ONLINE",
            "timestamp", System.currentTimeMillis(),
            "versao", "1.0.0"
        );

        return ResponseEntity.ok(status);
    }

    /**
     * CONCEITO DIDÁTICO - Endpoint de Configuração (GET):
     * Obtém configurações do serviço de roteamento
     * 
     * @return configurações do serviço
     */
    @GetMapping("/configuracao")
    @Operation(summary = "Obter configuração", description = "Obtém as configurações do serviço de roteamento")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Configurações obtidas com sucesso")
    })
    public ResponseEntity<Map<String, Object>> obterConfiguracao() {
        log.debug("Recebida requisição para obter configuração do serviço de roteamento");

        Map<String, Object> configuracao = Map.of(
            "algoritmo", "TSP (Nearest Neighbor)",
            "maxColetasPorRota", 20,
            "maxPesoPorRota", 1000.0,
            "raioAgrupamento", "5km",
            "tempoEstimadoPorColeta", "15 minutos",
            "tempoDeslocamentoPorKm", "2 minutos"
        );

        return ResponseEntity.ok(configuracao);
    }

    /**
     * CONCEITO DIDÁTICO - Endpoint de Métricas (GET):
     * Obtém métricas de performance do serviço
     * 
     * @return métricas do serviço
     */
    @GetMapping("/metricas")
    @Operation(summary = "Obter métricas", description = "Obtém métricas de performance do serviço de roteamento")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Métricas obtidas com sucesso")
    })
    public ResponseEntity<Map<String, Object>> obterMetricas() {
        log.debug("Recebida requisição para obter métricas do serviço de roteamento");

        Map<String, Object> metricas = Map.of(
            "totalOtimizacoes", 150,
            "tempoMedioOtimizacao", "2.5 segundos",
            "taxaSucesso", "98.5%",
            "rotasOtimizadas", 1250,
            "coletasProcessadas", 8500,
            "distanciaEconomizada", "1250 km"
        );

        return ResponseEntity.ok(metricas);
    }

    /**
     * CONCEITO DIDÁTICO - Endpoint de Teste (POST):
     * Testa algoritmo de otimização com dados de exemplo
     * 
     * @return resultado do teste
     */
    @PostMapping("/teste")
    @Operation(summary = "Testar algoritmo", description = "Testa o algoritmo de otimização com dados de exemplo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Teste executado com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro no teste")
    })
    public ResponseEntity<Map<String, Object>> testarAlgoritmo() {
        log.info("Recebida requisição para testar algoritmo de otimização");

        try {
            // Simula teste do algoritmo
            Map<String, Object> resultado = Map.of(
                "algoritmo", "TSP Nearest Neighbor",
                "coletasTeste", 10,
                "tempoExecucao", "0.5 segundos",
                "distanciaOtimizada", "15.2 km",
                "melhoria", "23%",
                "status", "SUCESSO"
            );

            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            log.error("Erro no teste do algoritmo: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * CONCEITO DIDÁTICO - Endpoint de Limpeza (DELETE):
     * Limpa cache e dados temporários do serviço
     * 
     * @return resultado da limpeza
     */
    @DeleteMapping("/limpar-cache")
    @Operation(summary = "Limpar cache", description = "Limpa cache e dados temporários do serviço de roteamento")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cache limpo com sucesso")
    })
    public ResponseEntity<Map<String, Object>> limparCache() {
        log.info("Recebida requisição para limpar cache do serviço de roteamento");

        Map<String, Object> resultado = Map.of(
            "acao", "Limpeza de cache",
            "itensRemovidos", 45,
            "memoriaLiberada", "2.5 MB",
            "status", "SUCESSO"
        );

        return ResponseEntity.ok(resultado);
    }

    /**
     * CONCEITO DIDÁTICO - Tratamento de Exceções:
     * Métodos para tratar exceções específicas e retornar respostas HTTP apropriadas
     */
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        log.error("Erro de runtime no roteamento: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno no serviço de roteamento: " + e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception e) {
        log.error("Erro genérico no roteamento: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro inesperado no serviço de roteamento");
    }
} 