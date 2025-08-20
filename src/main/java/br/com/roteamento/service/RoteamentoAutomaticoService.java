package br.com.roteamento.service;

import br.com.roteamento.dto.ColetaDTO;
import br.com.roteamento.dto.RotaDTO;
import br.com.roteamento.model.Coleta;
import br.com.roteamento.model.Rota;
import br.com.roteamento.model.Usuario;
import br.com.roteamento.repository.ColetaRepository;
import br.com.roteamento.repository.RotaRepository;
import br.com.roteamento.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Serviço de Roteamento Automático Inteligente
 * 
 * CONCEITO DIDÁTICO - Roteamento Automático:
 * - Agendamento automático de tarefas (@Scheduled)
 * - Algoritmos de clustering geográfico
 * - Otimização baseada em GPS
 * - Balanceamento de carga automático
 * - Consideração de capacidades e restrições
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoteamentoAutomaticoService {

    private final ColetaRepository coletaRepository;
    private final RotaRepository rotaRepository;
    private final UsuarioRepository usuarioRepository;
    private final RoteamentoService roteamentoService;
    private final AlgoritmosAvancadosService algoritmosAvancadosService;

    // Configurações de roteamento
    private static final double RAIO_MAXIMO_KM = 10.0; // Raio máximo para agrupar coletas
    private static final BigDecimal CAPACIDADE_MAXIMA_VEICULO = new BigDecimal("1000.0"); // kg
    private static final int MAX_COLETAS_POR_ROTA = 15; // Máximo de coletas por rota

    /**
     * EXECUTA ROTEAMENTO AUTOMÁTICO A CADA 30 MINUTOS
     * 
     * CONCEITOS:
     * - @Scheduled: Execução automática em intervalos
     * - Cron expression: Define quando executar
     * - Processamento em background
     */
    @Scheduled(fixedRate = 1800000) // 30 minutos
    @Transactional
    public void executarRoteamentoAutomatico() {
        log.info("Iniciando roteamento automático...");

        try {
            // 1. Buscar coletas pendentes
            List<Coleta> coletasPendentes = buscarColetasPendentes();
            
            if (coletasPendentes.isEmpty()) {
                log.info("Nenhuma coleta pendente encontrada");
                return;
            }

            // 2. Buscar coletores disponíveis
            List<Usuario> coletoresDisponiveis = buscarColetoresDisponiveis();
            
            if (coletoresDisponiveis.isEmpty()) {
                log.warn("Nenhum coletor disponível encontrado");
                return;
            }

            // 3. Agrupar coletas por proximidade geográfica usando K-means
            List<List<Coleta>> clusters = algoritmosAvancadosService.clusteringKMeans(coletasPendentes, 
                    Math.min(coletoresDisponiveis.size(), coletasPendentes.size()));

            // 4. Balancear carga entre coletores
            Map<Usuario, List<Coleta>> distribuicao = algoritmosAvancadosService.balancearCargaInteligente(
                    coletoresDisponiveis, clusters);

            // 5. Criar rotas otimizadas para cada grupo
            int rotasCriadas = 0;
            for (Map.Entry<Usuario, List<Coleta>> entry : distribuicao.entrySet()) {
                Usuario coletor = entry.getKey();
                List<Coleta> coletasGrupo = entry.getValue();
                
                if (!coletasGrupo.isEmpty()) {
                    // Aplicar algoritmo genético para otimização
                    List<Coleta> coletasOtimizadas = algoritmosAvancadosService.otimizarRotaComAlgoritmoGenetico(coletasGrupo);
                    
                    Rota rota = criarRotaOtimizada(coletor, coletasOtimizadas);
                    rotaRepository.save(rota);
                    rotasCriadas++;
                    log.info("Rota otimizada criada para coletor {} com {} coletas", coletor.getNome(), coletasOtimizadas.size());
                }
            }

            log.info("Roteamento automático concluído. {} rotas criadas", rotasCriadas);

        } catch (Exception e) {
            log.error("❌ Erro no roteamento automático", e);
        }
    }

    /**
     * EXECUTA ROTEAMENTO MANUAL SOB DEMANDA
     * 
     * @return resultado do roteamento
     */
    @Transactional
    public Map<String, Object> executarRoteamentoManual() {
        log.info("Executando roteamento manual agrupando por material...");

        Map<String, Object> resultado = new HashMap<>();
        List<Coleta> coletasPendentes = buscarColetasPendentes();
        List<Usuario> coletoresDisponiveis = buscarColetoresDisponiveis();
        List<Map<String, Object>> rotasDetalhadas = new ArrayList<>();
        int rotasCriadas = 0;

        if (coletasPendentes.isEmpty()) {
            resultado.put("mensagem", "Nenhuma coleta pendente encontrada");
            resultado.put("rotasCriadas", 0);
            resultado.put("rotas", rotasDetalhadas);
            return resultado;
        }
        if (coletoresDisponiveis.isEmpty()) {
            resultado.put("mensagem", "Nenhum coletor disponível encontrado");
            resultado.put("rotasCriadas", 0);
            resultado.put("rotas", rotasDetalhadas);
            return resultado;
        }

        // Agrupar coletas por material
        Map<Long, List<Coleta>> coletasPorMaterial = coletasPendentes.stream()
            .collect(Collectors.groupingBy(c -> c.getMaterial().getId()));

        for (Map.Entry<Long, List<Coleta>> entry : coletasPorMaterial.entrySet()) {
            Long materialId = entry.getKey();
            List<Coleta> coletasMaterial = entry.getValue();
            String nomeMaterial = coletasMaterial.get(0).getMaterial().getNome();

            // Usar K-means para clustering geográfico avançado
            int numClusters = Math.min(coletoresDisponiveis.size(), coletasMaterial.size());
            List<List<Coleta>> clusters = algoritmosAvancadosService.clusteringKMeans(coletasMaterial, numClusters);
            
            // Balancear carga entre coletores
            Map<Usuario, List<Coleta>> distribuicao = algoritmosAvancadosService.balancearCargaInteligente(
                    coletoresDisponiveis, clusters);
            
            for (Map.Entry<Usuario, List<Coleta>> distribuicaoEntry : distribuicao.entrySet()) {
                Usuario coletor = distribuicaoEntry.getKey();
                List<Coleta> coletasGrupo = distribuicaoEntry.getValue();
                
                if (!coletasGrupo.isEmpty()) {
                    // Aplicar algoritmo genético para otimização
                    List<Coleta> coletasOtimizadas = algoritmosAvancadosService.otimizarRotaComAlgoritmoGenetico(coletasGrupo);
                    
                    Rota rota = criarRotaOtimizada(coletor, coletasOtimizadas);
                    rotaRepository.save(rota);
                    rotasCriadas++;
                    
                    // Detalhar rota para resposta
                    Map<String, Object> rotaDetalhe = new HashMap<>();
                    rotaDetalhe.put("id", rota.getId());
                    rotaDetalhe.put("materialId", materialId);
                    rotaDetalhe.put("material", nomeMaterial);
                    rotaDetalhe.put("coletor", coletor.getNome());
                    rotaDetalhe.put("algoritmo", "Genético + K-means");
                    rotaDetalhe.put("coletas", coletasOtimizadas.stream().map(coleta -> Map.of(
                        "id", coleta.getId(),
                        "endereco", coleta.getEndereco(),
                        "latitude", coleta.getLatitude(),
                        "longitude", coleta.getLongitude(),
                        "usuario", coleta.getUsuario().getNome(),
                        "quantidade", coleta.getQuantidade()
                    )).collect(Collectors.toList()));
                    rotasDetalhadas.add(rotaDetalhe);
                }
            }
        }

        resultado.put("mensagem", "Roteamento manual por material concluído com sucesso");
        resultado.put("rotasCriadas", rotasCriadas);
        resultado.put("rotas", rotasDetalhadas);
        resultado.put("coletasProcessadas", coletasPendentes.size());
        resultado.put("materiaisAgrupados", coletasPorMaterial.size());
        return resultado;
    }

    /**
     * BUSCA COLETAS PENDENTES
     * 
     * @return lista de coletas pendentes
     */
    private List<Coleta> buscarColetasPendentes() {
        return coletaRepository.findByStatusIn(List.of(
                Coleta.StatusColeta.SOLICITADA,
                Coleta.StatusColeta.EM_ANALISE
        ));
    }

    /**
     * BUSCA COLETORES DISPONÍVEIS
     * 
     * @return lista de coletores ativos
     */
    private List<Usuario> buscarColetoresDisponiveis() {
        return usuarioRepository.findByTipoUsuarioAndStatus(
                Usuario.TipoUsuario.COLETOR,
                Usuario.StatusUsuario.ATIVO
        );
    }

    /**
     * AGREGA COLETAS POR PROXIMIDADE GEOGRÁFICA
     * 
     * CONCEITOS:
     * - Clustering geográfico
     * - Cálculo de distâncias
     * - Agrupamento inteligente
     * 
     * @param coletas lista de coletas
     * @return grupos de coletas por proximidade
     */
    private List<GrupoColetas> agruparColetasPorProximidade(List<Coleta> coletas) {
        List<GrupoColetas> grupos = new ArrayList<>();
        Set<Coleta> coletasProcessadas = new HashSet<>();

        for (Coleta coleta : coletas) {
            if (coletasProcessadas.contains(coleta)) {
                continue;
            }

            // Criar novo grupo
            GrupoColetas grupo = new GrupoColetas();
            grupo.adicionarColeta(coleta);
            coletasProcessadas.add(coleta);

            // Buscar coletas próximas
            for (Coleta outraColeta : coletas) {
                if (coletasProcessadas.contains(outraColeta)) {
                    continue;
                }

                double distancia = calcularDistancia(
                        coleta.getLatitude(), coleta.getLongitude(),
                        outraColeta.getLatitude(), outraColeta.getLongitude()
                );

                // Se está dentro do raio e não excede capacidade
                if (distancia <= RAIO_MAXIMO_KM && 
                    grupo.getPesoTotal().add(outraColeta.getQuantidade()).compareTo(CAPACIDADE_MAXIMA_VEICULO) <= 0 &&
                    grupo.getColetas().size() < MAX_COLETAS_POR_ROTA) {
                    
                    grupo.adicionarColeta(outraColeta);
                    coletasProcessadas.add(outraColeta);
                }
            }

            grupos.add(grupo);
        }

        log.info("Criados {} grupos de coletas por proximidade", grupos.size());
        return grupos;
    }

    /**
     * CALCULA DISTÂNCIA ENTRE DOIS PONTOS (FÓRMULA DE HAVERSINE)
     * 
     * @param lat1 latitude do ponto 1
     * @param lon1 longitude do ponto 1
     * @param lat2 latitude do ponto 2
     * @param lon2 longitude do ponto 2
     * @return distância em quilômetros
     */
    private double calcularDistancia(Double lat1, Double lon1, Double lat2, Double lon2) {
        if (lat1 == null || lon1 == null || lat2 == null || lon2 == null) {
            return Double.MAX_VALUE; // Distância máxima se não há coordenadas
        }

        final int R = 6371; // Raio da Terra em km

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    /**
     * SELECIONA COLETOR PARA UM GRUPO DE COLETAS
     * 
     * @param coletores lista de coletores disponíveis
     * @param grupo grupo de coletas
     * @return coletor selecionado
     */
    private Usuario selecionarColetorParaGrupo(List<Usuario> coletores, GrupoColetas grupo) {
        if (coletores.isEmpty()) {
            return null;
        }

        // Estratégia: selecionar coletor com menos rotas ativas
        return coletores.stream()
                .min(Comparator.comparing(coletor -> 
                    rotaRepository.countByColetorIdAndStatus(coletor.getId(), Rota.StatusRota.EM_EXECUCAO)))
                .orElse(coletores.get(0));
    }

    /**
     * CRIA ROTA OTIMIZADA PARA UM GRUPO DE COLETAS
     * 
     * @param coletor coletor responsável
     * @param coletas coletas da rota
     * @return rota criada
     */
    private Rota criarRotaOtimizada(Usuario coletor, List<Coleta> coletas) {
        // Aplicar algoritmo TSP para otimizar ordem das coletas
        List<Coleta> coletasOtimizadas = aplicarAlgoritmoTSP(coletas);

        // Criar rota
        Rota rota = new Rota(coletor, "Rota Automática - " + LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        
        rota.setDescricao("Rota criada automaticamente pelo sistema");
        rota.setStatus(Rota.StatusRota.PLANEJADA);
        rota.setCapacidadeMaxima(CAPACIDADE_MAXIMA_VEICULO);
        rota.setColetas(coletasOtimizadas);

        // Calcular métricas
        BigDecimal pesoTotal = coletasOtimizadas.stream()
                .map(Coleta::getQuantidade)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        double distanciaTotal = calcularDistanciaTotal(coletasOtimizadas);
        int tempoEstimado = calcularTempoEstimado(coletasOtimizadas);

        log.info("Distância total calculada: {} km", distanciaTotal);
        log.info("Tempo estimado calculado: {} minutos", tempoEstimado);

        rota.setCapacidadeUtilizada(pesoTotal);
        rota.setDistanciaTotal(distanciaTotal > 0 ? (int) distanciaTotal : 1);
        rota.setTempoEstimado(tempoEstimado > 0 ? tempoEstimado : 1);

        log.info("Rota configurada - Distância: {}, Tempo: {}", rota.getDistanciaTotal(), rota.getTempoEstimado());

        return rota;
    }

    /**
     * APLICA ALGORITMO TSP (NEAREST NEIGHBOR)
     * 
     * @param coletas lista de coletas
     * @return coletas ordenadas otimamente
     */
    private List<Coleta> aplicarAlgoritmoTSP(List<Coleta> coletas) {
        if (coletas.size() <= 1) {
            return new ArrayList<>(coletas);
        }

        List<Coleta> rotaOtimizada = new ArrayList<>();
        Set<Coleta> coletasNaoVisitadas = new HashSet<>(coletas);
        
        // Começar com a primeira coleta
        Coleta coletaAtual = coletas.get(0);
        rotaOtimizada.add(coletaAtual);
        coletasNaoVisitadas.remove(coletaAtual);

        // Encontrar sempre a coleta mais próxima
        while (!coletasNaoVisitadas.isEmpty()) {
            Coleta proximaColeta = encontrarColetaMaisProxima(coletaAtual, coletasNaoVisitadas);
            rotaOtimizada.add(proximaColeta);
            coletasNaoVisitadas.remove(proximaColeta);
            coletaAtual = proximaColeta;
        }

        return rotaOtimizada;
    }

    /**
     * ENCONTRA A COLETA MAIS PRÓXIMA
     * 
     * @param coletaAtual coleta atual
     * @param coletasDisponiveis coletas disponíveis
     * @return coleta mais próxima
     */
    private Coleta encontrarColetaMaisProxima(Coleta coletaAtual, Set<Coleta> coletasDisponiveis) {
        return coletasDisponiveis.stream()
                .min(Comparator.comparingDouble(outraColeta -> 
                    calcularDistancia(
                        coletaAtual.getLatitude(), coletaAtual.getLongitude(),
                        outraColeta.getLatitude(), outraColeta.getLongitude()
                    )))
                .orElse(coletasDisponiveis.iterator().next());
    }

    /**
     * CALCULA DISTÂNCIA TOTAL DA ROTA
     * 
     * @param coletas coletas da rota
     * @return distância total em km
     */
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

    /**
     * CALCULA TEMPO ESTIMADO DA ROTA
     * 
     * @param coletas coletas da rota
     * @return tempo estimado em minutos
     */
    private int calcularTempoEstimado(List<Coleta> coletas) {
        // Estimativa: 5 minutos por coleta + tempo de deslocamento
        int tempoPorColeta = 5;
        double velocidadeMedia = 30.0; // km/h
        double distanciaTotal = calcularDistanciaTotal(coletas);
        
        int tempoDeslocamento = (int) (distanciaTotal / velocidadeMedia * 60); // minutos
        int tempoColetas = coletas.size() * tempoPorColeta;
        
        return tempoDeslocamento + tempoColetas;
    }

    /**
     * CLASSE INTERNA PARA REPRESENTAR GRUPO DE COLETAS
     */
    private static class GrupoColetas {
        private final List<Coleta> coletas = new ArrayList<>();
        private BigDecimal pesoTotal = BigDecimal.ZERO;

        public void adicionarColeta(Coleta coleta) {
            coletas.add(coleta);
            if (coleta.getQuantidade() != null) {
                pesoTotal = pesoTotal.add(coleta.getQuantidade());
            }
        }

        public List<Coleta> getColetas() {
            return coletas;
        }

        public BigDecimal getPesoTotal() {
            return pesoTotal;
        }
    }

    /**
     * MÉTODO PARA CALCULAR ROTA ENTRE PONTOS
     * 
     * CONCEITOS:
     * - Algoritmo de roteamento entre múltiplos pontos
     * - Otimização de caminho
     * - Cálculo de distância e tempo
     * 
     * @param pontos Lista de coordenadas (lat,lng)
     * @return Map com rota calculada
     */
    public Map<String, Object> calcularRotaEntrePontos(List<Map<String, Double>> pontos) {
        log.info("Calculando rota para {} pontos", pontos.size());

        if (pontos.size() < 2) {
            return Map.of(
                "erro", "São necessários pelo menos 2 pontos para calcular uma rota",
                "distanciaTotal", 0.0,
                "tempoEstimado", 0,
                "pontos", pontos
            );
        }

        // Converter pontos para lista de coordenadas
        List<Coordenada> coordenadas = pontos.stream()
            .map(ponto -> new Coordenada(ponto.get("latitude"), ponto.get("longitude")))
            .collect(Collectors.toList());

        // Aplicar algoritmo TSP
        List<Coordenada> rotaOtimizada = aplicarTSP(coordenadas);

        // Calcular métricas
        double distanciaTotal = calcularDistanciaTotalCoordenadas(rotaOtimizada);
        int tempoEstimado = calcularTempoEstimado(rotaOtimizada.size(), distanciaTotal);

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("distanciaTotal", distanciaTotal);
        resultado.put("tempoEstimado", tempoEstimado);
        resultado.put("pontos", rotaOtimizada.stream()
            .map(coord -> Map.of("latitude", coord.latitude, "longitude", coord.longitude))
            .collect(Collectors.toList()));
        resultado.put("mensagem", "Rota calculada com sucesso");

        return resultado;
    }

    /**
     * MÉTODO PARA GEOLOCALIZAR ENDEREÇO
     * 
     * CONCEITOS:
     * - Conversão de endereço para coordenadas
     * - Integração com serviço de geocoding
     * - Validação de endereços
     * 
     * @param endereco Endereço para geolocalizar
     * @return Map com coordenadas
     */
    public Map<String, Object> geolocalizarEndereco(String endereco) {
        log.info("📍 Geolocalizando endereço: {}", endereco);

        // Implementação simplificada - em produção usar API de geocoding
        // Por enquanto, retorna coordenadas fixas para teste
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("latitude", -23.5505);
        resultado.put("longitude", -46.6333);
        resultado.put("endereco", endereco);
        resultado.put("formatted_address", endereco);
        resultado.put("status", "OK");

        return resultado;
    }

    /**
     * MÉTODO PARA OBTER ENDEREÇO POR COORDENADAS
     * 
     * CONCEITOS:
     * - Conversão de coordenadas para endereço
     * - Reverse geocoding
     * - Validação de coordenadas
     * 
     * @param latitude Latitude
     * @param longitude Longitude
     * @return Map com endereço
     */
    public Map<String, Object> obterEnderecoPorCoordenadas(Double latitude, Double longitude) {
        log.info("📍 Obtendo endereço para coordenadas: lat={}, lng={}", latitude, longitude);

        // Implementação simplificada - em produção usar API de reverse geocoding
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("latitude", latitude);
        resultado.put("longitude", longitude);
        resultado.put("endereco", "Endereço calculado para as coordenadas");
        resultado.put("formatted_address", "São Paulo, SP, Brasil");
        resultado.put("status", "OK");

        return resultado;
    }

    /**
     * CLASSE INTERNA PARA REPRESENTAR COORDENADAS
     */
    private static class Coordenada {
        private final double latitude;
        private final double longitude;

        public Coordenada(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public double getLatitude() { return latitude; }
        public double getLongitude() { return longitude; }
    }

    /**
     * APLICA ALGORITMO TSP PARA LISTA DE COORDENADAS
     * 
     * @param coordenadas Lista de coordenadas
     * @return Coordenadas ordenadas otimamente
     */
    private List<Coordenada> aplicarTSP(List<Coordenada> coordenadas) {
        if (coordenadas.size() <= 1) {
            return new ArrayList<>(coordenadas);
        }

        List<Coordenada> rotaOtimizada = new ArrayList<>();
        Set<Coordenada> coordenadasNaoVisitadas = new HashSet<>(coordenadas);
        
        // Começar com a primeira coordenada
        Coordenada atual = coordenadas.get(0);
        rotaOtimizada.add(atual);
        coordenadasNaoVisitadas.remove(atual);

        // Encontrar sempre a coordenada mais próxima
        while (!coordenadasNaoVisitadas.isEmpty()) {
            Coordenada proxima = encontrarCoordenadaMaisProxima(atual, coordenadasNaoVisitadas);
            rotaOtimizada.add(proxima);
            coordenadasNaoVisitadas.remove(proxima);
            atual = proxima;
        }

        return rotaOtimizada;
    }

    /**
     * ENCONTRA A COORDENADA MAIS PRÓXIMA
     * 
     * @param atual Coordenada atual
     * @param disponiveis Coordenadas disponíveis
     * @return Coordenada mais próxima
     */
    private Coordenada encontrarCoordenadaMaisProxima(Coordenada atual, Set<Coordenada> disponiveis) {
        return disponiveis.stream()
                .min(Comparator.comparingDouble(outra -> 
                    calcularDistancia(
                        atual.latitude, atual.longitude,
                        outra.latitude, outra.longitude
                    )))
                .orElse(disponiveis.iterator().next());
    }

    /**
     * CALCULA DISTÂNCIA TOTAL PARA LISTA DE COORDENADAS
     * 
     * @param coordenadas Lista de coordenadas
     * @return Distância total em km
     */
    private double calcularDistanciaTotalCoordenadas(List<Coordenada> coordenadas) {
        if (coordenadas.size() <= 1) {
            return 0.0;
        }

        double distanciaTotal = 0.0;
        for (int i = 0; i < coordenadas.size() - 1; i++) {
            Coordenada atual = coordenadas.get(i);
            Coordenada proxima = coordenadas.get(i + 1);
            
            distanciaTotal += calcularDistancia(
                atual.latitude, atual.longitude,
                proxima.latitude, proxima.longitude
            );
        }

        return distanciaTotal;
    }

    /**
     * CALCULA TEMPO ESTIMADO PARA ROTA
     * 
     * @param numPontos Número de pontos
     * @param distanciaTotal Distância total em km
     * @return Tempo estimado em minutos
     */
    private int calcularTempoEstimado(int numPontos, double distanciaTotal) {
        // Estimativa: 5 minutos por ponto + tempo de deslocamento
        int tempoPorPonto = 5;
        double velocidadeMedia = 30.0; // km/h
        
        int tempoDeslocamento = (int) (distanciaTotal / velocidadeMedia * 60); // minutos
        int tempoPontos = numPontos * tempoPorPonto;
        
        return tempoDeslocamento + tempoPontos;
    }
} 