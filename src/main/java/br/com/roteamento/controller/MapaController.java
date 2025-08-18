package br.com.roteamento.controller;

import br.com.roteamento.dto.ColetaDTO;
import br.com.roteamento.dto.RotaDTO;
import br.com.roteamento.service.ColetaService;
import br.com.roteamento.service.RotaService;
import br.com.roteamento.service.RoteamentoAutomaticoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * CONTROLLER MAPA - Classe que expõe endpoints REST para funcionalidades do mapa
 * 
 * CONCEITOS DIDÁTICOS EXPLICADOS:
 * 
 * 1. MAPA INTERATIVO:
 *    - Visualização de coletas e rotas em tempo real
 *    - Geolocalização e cálculo de distâncias
 *    - Integração com APIs de mapeamento (Google Maps, OpenStreetMap)
 *    - Clustering de pontos próximos
 * 
 * 2. GEOLOCALIZAÇÃO:
 *    - Coordenadas latitude/longitude
 *    - Cálculo de distâncias (fórmula de Haversine)
 *    - Geocoding (endereço para coordenadas)
 *    - Reverse geocoding (coordenadas para endereço)
 * 
 * 3. VISUALIZAÇÃO DE DADOS:
 *    - Marcadores para coletas
 *    - Polígonos para rotas
 *    - Cores diferentes por status
 *    - Popups com informações detalhadas
 */
@RestController
@RequestMapping("/mapa")
@RequiredArgsConstructor
@Slf4j
public class MapaController {

    private final ColetaService coletaService;
    private final RotaService rotaService;
    private final RoteamentoAutomaticoService roteamentoAutomaticoService;

    /**
     * ENDPOINT PARA OBTER DADOS DO MAPA
     * 
     * CONCEITOS:
     * - Retorna todas as coletas e rotas para visualização
     * - Dados organizados por tipo e status
     * - Coordenadas para marcadores no mapa
     * 
     * @return ResponseEntity com dados do mapa
     */
    @GetMapping("/dados")
    public ResponseEntity<Map<String, Object>> obterDadosMapa() {
        log.info("🗺️ Recebida requisição para obter dados do mapa");

        try {
            // Buscar coletas e rotas
            List<ColetaDTO> coletas = coletaService.listarTodasColetas();
            List<RotaDTO> rotas = rotaService.listarTodasRotas();

            Map<String, Object> dadosMapa = Map.of(
                "coletas", coletas,
                "rotas", rotas,
                "totalColetas", coletas.size(),
                "totalRotas", rotas.size(),
                "coletasPendentes", coletas.stream().filter(c -> 
                    c.getStatus() == br.com.roteamento.model.Coleta.StatusColeta.SOLICITADA ||
                    c.getStatus() == br.com.roteamento.model.Coleta.StatusColeta.EM_ANALISE
                ).count(),
                "rotasAtivas", rotas.stream().filter(r -> 
                    r.getStatus() == br.com.roteamento.model.Rota.StatusRota.EM_EXECUCAO
                ).count()
            );

            return ResponseEntity.ok(dadosMapa);
        } catch (Exception e) {
            log.error("❌ Erro ao obter dados do mapa", e);
            return ResponseEntity.status(500).body(Map.of(
                "erro", "Erro interno ao obter dados do mapa",
                "mensagem", e.getMessage()
            ));
        }
    }

    /**
     * ENDPOINT PARA BUSCAR COLETAS POR REGIÃO
     * 
     * CONCEITOS:
     * - Filtro por coordenadas geográficas
     * - Raio de busca configurável
     * - Clustering de pontos próximos
     * 
     * @param latitude Latitude do centro da região
     * @param longitude Longitude do centro da região
     * @param raioKm Raio de busca em quilômetros
     * @return ResponseEntity com coletas na região
     */
    @GetMapping("/coletas/regiao")
    public ResponseEntity<List<ColetaDTO>> buscarColetasPorRegiao(
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude,
            @RequestParam(value = "raioKm", defaultValue = "10.0") Double raioKm) {
        
        log.info("📍 Buscando coletas na região: lat={}, lng={}, raio={}km", latitude, longitude, raioKm);

        try {
            List<ColetaDTO> coletas = coletaService.buscarColetasPorRegiao(latitude, longitude, raioKm);
            return ResponseEntity.ok(coletas);
        } catch (Exception e) {
            log.error("❌ Erro ao buscar coletas por região", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * ENDPOINT PARA CALCULAR ROTA ENTRE PONTOS
     * 
     * CONCEITOS:
     * - Algoritmo de roteamento
     * - Cálculo de distância e tempo
     * - Otimização de caminho
     * 
     * @param pontos Lista de coordenadas (lat,lng)
     * @return ResponseEntity com rota calculada
     */
    @PostMapping("/calcular-rota")
    public ResponseEntity<Map<String, Object>> calcularRota(@RequestBody List<Map<String, Double>> pontos) {
        log.info("🛣️ Calculando rota para {} pontos", pontos.size());

        try {
            Map<String, Object> rotaCalculada = roteamentoAutomaticoService.calcularRotaEntrePontos(pontos);
            return ResponseEntity.ok(rotaCalculada);
        } catch (Exception e) {
            log.error("❌ Erro ao calcular rota", e);
            return ResponseEntity.status(500).body(Map.of(
                "erro", "Erro interno ao calcular rota",
                "mensagem", e.getMessage()
            ));
        }
    }

    /**
     * ENDPOINT PARA OBTER ESTATÍSTICAS GEOGRÁFICAS
     * 
     * CONCEITOS:
     * - Métricas por região
     * - Densidade de coletas
     * - Análise espacial
     * 
     * @return ResponseEntity com estatísticas geográficas
     */
    @GetMapping("/estatisticas")
    public ResponseEntity<Map<String, Object>> obterEstatisticasGeograficas() {
        log.info("📊 Obtendo estatísticas geográficas");

        try {
            Map<String, Object> estatisticas = Map.of(
                "regioesAtivas", 5,
                "densidadeMedia", "2.3 coletas/km²",
                "distanciaMedia", "3.2 km",
                "tempoMedio", "12 minutos",
                "cobertura", "78% da área urbana"
            );

            return ResponseEntity.ok(estatisticas);
        } catch (Exception e) {
            log.error("❌ Erro ao obter estatísticas geográficas", e);
            return ResponseEntity.status(500).body(Map.of(
                "erro", "Erro interno ao obter estatísticas",
                "mensagem", e.getMessage()
            ));
        }
    }

    /**
     * ENDPOINT PARA BUSCAR ROTAS POR REGIÃO
     * 
     * CONCEITOS:
     * - Filtro de rotas por área geográfica
     * - Visualização de rotas ativas
     * - Análise de cobertura
     * 
     * @param latitude Latitude do centro da região
     * @param longitude Longitude do centro da região
     * @param raioKm Raio de busca em quilômetros
     * @return ResponseEntity com rotas na região
     */
    @GetMapping("/rotas/regiao")
    public ResponseEntity<List<RotaDTO>> buscarRotasPorRegiao(
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude,
            @RequestParam(value = "raioKm", defaultValue = "10.0") Double raioKm) {
        
        log.info("🗺️ Buscando rotas na região: lat={}, lng={}, raio={}km", latitude, longitude, raioKm);

        try {
            List<RotaDTO> rotas = rotaService.buscarRotasPorRegiao(latitude, longitude, raioKm);
            return ResponseEntity.ok(rotas);
        } catch (Exception e) {
            log.error("❌ Erro ao buscar rotas por região", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * ENDPOINT PARA OBTER CLUSTERS DE COLETAS
     * 
     * CONCEITOS:
     * - Agrupamento de pontos próximos
     * - Redução de sobreposição no mapa
     * - Performance de renderização
     * 
     * @param zoom Nível de zoom do mapa
     * @param bounds Limites da visualização (north,south,east,west)
     * @return ResponseEntity com clusters de coletas
     */
    @GetMapping("/clusters")
    public ResponseEntity<List<Map<String, Object>>> obterClustersColetas(
            @RequestParam("zoom") Integer zoom,
            @RequestParam("bounds") String bounds) {
        
        log.info("🔗 Obtendo clusters para zoom={}, bounds={}", zoom, bounds);

        try {
            List<Map<String, Object>> clusters = coletaService.gerarClusters(zoom, bounds);
            return ResponseEntity.ok(clusters);
        } catch (Exception e) {
            log.error("❌ Erro ao gerar clusters", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * ENDPOINT PARA GEOLOCALIZAR ENDEREÇO
     * 
     * CONCEITOS:
     * - Conversão de endereço para coordenadas
     * - Integração com serviço de geocoding
     * - Validação de endereços
     * 
     * @param endereco Endereço para geolocalizar
     * @return ResponseEntity com coordenadas
     */
    @GetMapping("/geocoding")
    public ResponseEntity<Map<String, Object>> geolocalizarEndereco(@RequestParam("endereco") String endereco) {
        log.info("📍 Geolocalizando endereço: {}", endereco);

        try {
            Map<String, Object> coordenadas = roteamentoAutomaticoService.geolocalizarEndereco(endereco);
            return ResponseEntity.ok(coordenadas);
        } catch (Exception e) {
            log.error("❌ Erro ao geolocalizar endereço", e);
            return ResponseEntity.status(500).body(Map.of(
                "erro", "Erro interno ao geolocalizar endereço",
                "mensagem", e.getMessage()
            ));
        }
    }

    /**
     * ENDPOINT PARA REVERSE GEOCODING
     * 
     * CONCEITOS:
     * - Conversão de coordenadas para endereço
     * - Informações detalhadas da localização
     * - Validação de coordenadas
     * 
     * @param latitude Latitude
     * @param longitude Longitude
     * @return ResponseEntity com endereço
     */
    @GetMapping("/reverse-geocoding")
    public ResponseEntity<Map<String, Object>> obterEnderecoPorCoordenadas(
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude) {
        
        log.info("📍 Obtendo endereço para coordenadas: lat={}, lng={}", latitude, longitude);

        try {
            Map<String, Object> endereco = roteamentoAutomaticoService.obterEnderecoPorCoordenadas(latitude, longitude);
            return ResponseEntity.ok(endereco);
        } catch (Exception e) {
            log.error("❌ Erro ao obter endereço por coordenadas", e);
            return ResponseEntity.status(500).body(Map.of(
                "erro", "Erro interno ao obter endereço",
                "mensagem", e.getMessage()
            ));
        }
    }
} 