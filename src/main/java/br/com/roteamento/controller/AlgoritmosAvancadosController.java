package br.com.roteamento.controller;

import br.com.roteamento.model.Coleta;
import br.com.roteamento.model.Usuario;
import br.com.roteamento.service.AlgoritmosAvancadosService;
import br.com.roteamento.service.ColetaService;
import br.com.roteamento.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Controller para Algoritmos Avançados de Otimização
 * 
 * CONCEITO DIDÁTICO - Algoritmos Avançados:
 * - Endpoints para algoritmos de otimização avançados
 * - Comparação de diferentes algoritmos
 * - Métricas de performance
 * - Configuração de parâmetros
 */
@Slf4j
@RestController
@RequestMapping("/algoritmos-avancados")
@RequiredArgsConstructor
public class AlgoritmosAvancadosController {

    private final AlgoritmosAvancadosService algoritmosAvancadosService;
    private final ColetaService coletaService;
    private final UsuarioService usuarioService;

    /**
     * ENDPOINT PARA OTIMIZAÇÃO COM ALGORITMO GENÉTICO
     * 
     * CONCEITOS:
     * - Algoritmo genético para TSP
     * - População de soluções candidatas
     * - Evolução por gerações
     * 
     * @param coletaIds IDs das coletas para otimizar
     * @return rota otimizada com algoritmo genético
     */
    @PostMapping("/otimizar-genetico")
    public ResponseEntity<Map<String, Object>> otimizarComAlgoritmoGenetico(@RequestBody List<Long> coletaIds) {
        log.info("Recebida requisição para otimização com algoritmo genético para {} coletas", coletaIds.size());

        try {
            // Buscar coletas
            List<Coleta> coletas = buscarColetasPorIds(coletaIds);
            
            if (coletas.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "erro", "Nenhuma coleta encontrada com os IDs fornecidos"
                ));
            }

            // Aplicar algoritmo genético
            List<Coleta> rotaOtimizada = algoritmosAvancadosService.otimizarRotaComAlgoritmoGenetico(coletas);
            
            // Calcular métricas
            double distanciaOriginal = calcularDistanciaTotal(coletas);
            double distanciaOtimizada = calcularDistanciaTotal(rotaOtimizada);
            double melhoria = ((distanciaOriginal - distanciaOtimizada) / distanciaOriginal) * 100;

            Map<String, Object> resultado = Map.of(
                "algoritmo", "Algoritmo Genético",
                "coletasProcessadas", coletas.size(),
                "distanciaOriginal", distanciaOriginal,
                "distanciaOtimizada", distanciaOtimizada,
                "melhoria", melhoria,
                "rotaOtimizada", rotaOtimizada.stream().map(Coleta::getId).toList()
            );

            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            log.error("❌ Erro na otimização com algoritmo genético", e);
            return ResponseEntity.status(500).body(Map.of(
                "erro", "Erro interno na otimização",
                "mensagem", e.getMessage()
            ));
        }
    }

    /**
     * ENDPOINT PARA OTIMIZAÇÃO COM SIMULATED ANNEALING
     * 
     * CONCEITOS:
     * - Simulated annealing para TSP
     * - Escape de mínimos locais
     * - Otimização global
     * 
     * @param coletaIds IDs das coletas para otimizar
     * @return rota otimizada com simulated annealing
     */
    @PostMapping("/otimizar-simulated-annealing")
    public ResponseEntity<Map<String, Object>> otimizarComSimulatedAnnealing(@RequestBody List<Long> coletaIds) {
        log.info("Recebida requisição para otimização com simulated annealing para {} coletas", coletaIds.size());

        try {
            // Buscar coletas
            List<Coleta> coletas = buscarColetasPorIds(coletaIds);
            
            if (coletas.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "erro", "Nenhuma coleta encontrada com os IDs fornecidos"
                ));
            }

            // Aplicar simulated annealing
            List<Coleta> rotaOtimizada = algoritmosAvancadosService.otimizarRotaComSimulatedAnnealing(coletas);
            
            // Calcular métricas
            double distanciaOriginal = calcularDistanciaTotal(coletas);
            double distanciaOtimizada = calcularDistanciaTotal(rotaOtimizada);
            double melhoria = ((distanciaOriginal - distanciaOtimizada) / distanciaOriginal) * 100;

            Map<String, Object> resultado = Map.of(
                "algoritmo", "Simulated Annealing",
                "coletasProcessadas", coletas.size(),
                "distanciaOriginal", distanciaOriginal,
                "distanciaOtimizada", distanciaOtimizada,
                "melhoria", melhoria,
                "rotaOtimizada", rotaOtimizada.stream().map(Coleta::getId).toList()
            );

            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            log.error("❌ Erro na otimização com simulated annealing", e);
            return ResponseEntity.status(500).body(Map.of(
                "erro", "Erro interno na otimização",
                "mensagem", e.getMessage()
            ));
        }
    }

    /**
     * ENDPOINT PARA CLUSTERING K-MEANS
     * 
     * CONCEITOS:
     * - Clustering geográfico avançado
     * - Agrupamento por centroides
     * - Otimização de posicionamento
     * 
     * @param k número de clusters
     * @return grupos de coletas otimizados
     */
    @PostMapping("/clustering-kmeans")
    public ResponseEntity<Map<String, Object>> clusteringKMeans(@RequestParam("k") Integer k) {
        log.info("Recebida requisição para clustering K-means com {} clusters", k);

        try {
            // Buscar todas as coletas pendentes
            List<Coleta> coletas = coletaService.buscarColetasPendentes().stream()
                    .map(coletaDTO -> {
                        // Converter DTO para entidade (simplificado)
                        Coleta coleta = new Coleta();
                        coleta.setId(coletaDTO.getId());
                        coleta.setLatitude(coletaDTO.getLatitude() != null ? coletaDTO.getLatitude().doubleValue() : 0.0);
                        coleta.setLongitude(coletaDTO.getLongitude() != null ? coletaDTO.getLongitude().doubleValue() : 0.0);
                        return coleta;
                    })
                    .filter(coleta -> coleta.getLatitude() != 0.0 && coleta.getLongitude() != 0.0)
                    .collect(java.util.stream.Collectors.toList());

            if (coletas.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "erro", "Nenhuma coleta com coordenadas válidas encontrada"
                ));
            }

            // Aplicar K-means
            List<List<Coleta>> clusters = algoritmosAvancadosService.clusteringKMeans(coletas, k);
            
            Map<String, Object> resultado = Map.of(
                "algoritmo", "K-means Clustering",
                "coletasProcessadas", coletas.size(),
                "numeroClusters", clusters.size(),
                "clusters", clusters.stream().map(cluster -> Map.of(
                    "tamanho", cluster.size(),
                    "coletas", cluster.stream().map(Coleta::getId).toList()
                )).toList()
            );

            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            log.error("❌ Erro no clustering K-means", e);
            return ResponseEntity.status(500).body(Map.of(
                "erro", "Erro interno no clustering",
                "mensagem", e.getMessage()
            ));
        }
    }

    /**
     * ENDPOINT PARA BALANCEAMENTO DE CARGA INTELIGENTE
     * 
     * CONCEITOS:
     * - Distribuição equilibrada de trabalho
     * - Consideração de capacidades individuais
     * - Prevenção de sobrecarga
     * 
     * @return distribuição otimizada de trabalho
     */
    @PostMapping("/balancear-carga")
    public ResponseEntity<Map<String, Object>> balancearCargaInteligente() {
        log.info("Recebida requisição para balanceamento de carga inteligente");

        try {
            // Buscar coletores ativos
            List<Usuario> coletores = usuarioService.buscarColetoresAtivos().stream()
                    .map(usuarioDTO -> {
                        // Converter DTO para entidade (simplificado)
                        Usuario usuario = new Usuario();
                        usuario.setId(usuarioDTO.getId());
                        usuario.setNome(usuarioDTO.getNome());
                        return usuario;
                    })
                    .collect(java.util.stream.Collectors.toList());

            if (coletores.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "erro", "Nenhum coletor ativo encontrado"
                ));
            }

            // Buscar coletas pendentes e agrupar
            List<Coleta> coletasPendentes = coletaService.buscarColetasPendentes().stream()
                    .map(coletaDTO -> {
                        Coleta coleta = new Coleta();
                        coleta.setId(coletaDTO.getId());
                        coleta.setLatitude(coletaDTO.getLatitude() != null ? coletaDTO.getLatitude().doubleValue() : 0.0);
                        coleta.setLongitude(coletaDTO.getLongitude() != null ? coletaDTO.getLongitude().doubleValue() : 0.0);
                        coleta.setQuantidade(coletaDTO.getPesoEstimado());
                        return coleta;
                    })
                    .filter(coleta -> coleta.getLatitude() != 0.0 && coleta.getLongitude() != 0.0)
                    .collect(java.util.stream.Collectors.toList());

            if (coletasPendentes.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "erro", "Nenhuma coleta pendente encontrada"
                ));
            }

            // Agrupar coletas por proximidade (simplificado)
            List<List<Coleta>> gruposColetas = agruparColetasPorProximidade(coletasPendentes);

            // Aplicar balanceamento de carga
            Map<Usuario, List<Coleta>> distribuicao = algoritmosAvancadosService.balancearCargaInteligente(
                    coletores, gruposColetas);

            Map<String, Object> resultado = Map.of(
                "algoritmo", "Balanceamento de Carga Inteligente",
                "coletores", coletores.size(),
                "gruposColetas", gruposColetas.size(),
                "distribuicao", distribuicao.entrySet().stream().map(entry -> Map.of(
                    "coletor", entry.getKey().getNome(),
                    "coletas", entry.getValue().stream().map(Coleta::getId).toList(),
                    "carga", entry.getValue().stream()
                            .mapToDouble(c -> c.getQuantidade() != null ? c.getQuantidade().doubleValue() : 0.0)
                            .sum()
                )).toList()
            );

            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            log.error("❌ Erro no balanceamento de carga", e);
            return ResponseEntity.status(500).body(Map.of(
                "erro", "Erro interno no balanceamento",
                "mensagem", e.getMessage()
            ));
        }
    }

    /**
     * ENDPOINT PARA PREVISÃO DE DEMANDA
     * 
     * CONCEITOS:
     * - Análise de padrões históricos
     * - Previsão baseada em tendências
     * - Machine learning simplificado
     * 
     * @param diasPrevisao número de dias para prever
     * @return previsão de demanda
     */
    @GetMapping("/prever-demanda")
    public ResponseEntity<Map<String, Object>> preverDemanda(@RequestParam("dias") Integer diasPrevisao) {
        log.info("Recebida requisição para previsão de demanda para {} dias", diasPrevisao);

        try {
            // Buscar histórico de coletas (últimos 30 dias)
            List<Coleta> coletasHistorico = coletaService.listarTodasColetas().stream()
                    .map(coletaDTO -> {
                        Coleta coleta = new Coleta();
                        coleta.setId(coletaDTO.getId());
                        coleta.setLatitude(coletaDTO.getLatitude() != null ? coletaDTO.getLatitude().doubleValue() : 0.0);
                        coleta.setLongitude(coletaDTO.getLongitude() != null ? coletaDTO.getLongitude().doubleValue() : 0.0);
                        coleta.setQuantidade(coletaDTO.getPesoEstimado());
                        return coleta;
                    })
                    .collect(java.util.stream.Collectors.toList());

            // Aplicar previsão de demanda
            Map<String, Object> previsao = algoritmosAvancadosService.preverDemanda(coletasHistorico, diasPrevisao);

            return ResponseEntity.ok(previsao);
        } catch (Exception e) {
            log.error("❌ Erro na previsão de demanda", e);
            return ResponseEntity.status(500).body(Map.of(
                "erro", "Erro interno na previsão",
                "mensagem", e.getMessage()
            ));
        }
    }

    /**
     * ENDPOINT PARA COMPARAR ALGORITMOS
     * 
     * CONCEITOS:
     * - Comparação de diferentes algoritmos
     * - Métricas de performance
     * - Análise de resultados
     * 
     * @param coletaIds IDs das coletas para comparar
     * @return comparação de algoritmos
     */
    @PostMapping("/comparar-algoritmos")
    public ResponseEntity<Map<String, Object>> compararAlgoritmos(@RequestBody List<Long> coletaIds) {
        log.info("Recebida requisição para comparar algoritmos para {} coletas", coletaIds.size());

        try {
            // Buscar coletas
            List<Coleta> coletas = buscarColetasPorIds(coletaIds);
            
            if (coletas.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "erro", "Nenhuma coleta encontrada com os IDs fornecidos"
                ));
            }

            double distanciaOriginal = calcularDistanciaTotal(coletas);

            // Testar algoritmo genético
            long inicioGenetico = System.currentTimeMillis();
            List<Coleta> rotaGenetico = algoritmosAvancadosService.otimizarRotaComAlgoritmoGenetico(coletas);
            long tempoGenetico = System.currentTimeMillis() - inicioGenetico;
            double distanciaGenetico = calcularDistanciaTotal(rotaGenetico);

            // Testar simulated annealing
            long inicioSA = System.currentTimeMillis();
            List<Coleta> rotaSA = algoritmosAvancadosService.otimizarRotaComSimulatedAnnealing(coletas);
            long tempoSA = System.currentTimeMillis() - inicioSA;
            double distanciaSA = calcularDistanciaTotal(rotaSA);

            Map<String, Object> resultado = Map.of(
                "coletasProcessadas", coletas.size(),
                "distanciaOriginal", distanciaOriginal,
                "algoritmoGenetico", Map.of(
                    "distancia", distanciaGenetico,
                    "melhoria", ((distanciaOriginal - distanciaGenetico) / distanciaOriginal) * 100,
                    "tempoExecucao", tempoGenetico,
                    "rota", rotaGenetico.stream().map(Coleta::getId).toList()
                ),
                "simulatedAnnealing", Map.of(
                    "distancia", distanciaSA,
                    "melhoria", ((distanciaOriginal - distanciaSA) / distanciaOriginal) * 100,
                    "tempoExecucao", tempoSA,
                    "rota", rotaSA.stream().map(Coleta::getId).toList()
                ),
                "melhorAlgoritmo", distanciaGenetico < distanciaSA ? "Algoritmo Genético" : "Simulated Annealing"
            );

            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            log.error("❌ Erro na comparação de algoritmos", e);
            return ResponseEntity.status(500).body(Map.of(
                "erro", "Erro interno na comparação",
                "mensagem", e.getMessage()
            ));
        }
    }

    // ========== MÉTODOS AUXILIARES ==========

    private List<Coleta> buscarColetasPorIds(List<Long> coletaIds) {
        return coletaIds.stream()
                .map(id -> {
                    try {
                        return coletaService.buscarColetaPorId(id);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(coletaDTO -> coletaDTO != null)
                .map(coletaDTO -> {
                    Coleta coleta = new Coleta();
                    coleta.setId(coletaDTO.getId());
                    coleta.setLatitude(coletaDTO.getLatitude() != null ? coletaDTO.getLatitude().doubleValue() : 0.0);
                    coleta.setLongitude(coletaDTO.getLongitude() != null ? coletaDTO.getLongitude().doubleValue() : 0.0);
                    coleta.setQuantidade(coletaDTO.getPesoEstimado());
                    return coleta;
                })
                .filter(coleta -> coleta.getLatitude() != 0.0 && coleta.getLongitude() != 0.0)
                .collect(java.util.stream.Collectors.toList());
    }

    private List<List<Coleta>> agruparColetasPorProximidade(List<Coleta> coletas) {
        List<List<Coleta>> grupos = new ArrayList<>();
        Set<Coleta> coletasProcessadas = new HashSet<>();

        for (Coleta coleta : coletas) {
            if (coletasProcessadas.contains(coleta)) {
                continue;
            }

            List<Coleta> grupo = new ArrayList<>();
            grupo.add(coleta);
            coletasProcessadas.add(coleta);

            // Buscar coletas próximas (raio de 5km)
            for (Coleta outraColeta : coletas) {
                if (!coletasProcessadas.contains(outraColeta)) {
                    double distancia = calcularDistancia(
                            coleta.getLatitude(), coleta.getLongitude(),
                            outraColeta.getLatitude(), outraColeta.getLongitude()
                    );

                    if (distancia <= 5.0) {
                        grupo.add(outraColeta);
                        coletasProcessadas.add(outraColeta);
                    }
                }
            }

            grupos.add(grupo);
        }

        return grupos;
    }

    private double calcularDistanciaTotal(List<Coleta> coletas) {
        if (coletas.size() <= 1) {
            return 0.0;
        }

        double distanciaTotal = 0.0;
        for (int i = 0; i < coletas.size() - 1; i++) {
            Coleta atual = coletas.get(i);
            Coleta proxima = coletas.get(i + 1);
            
            distanciaTotal += calcularDistancia(
                atual.getLatitude(), atual.getLongitude(),
                proxima.getLatitude(), proxima.getLongitude()
            );
        }

        return distanciaTotal;
    }

    private double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Raio da Terra em km

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
} 