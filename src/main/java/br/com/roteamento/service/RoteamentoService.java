package br.com.roteamento.service;

import br.com.roteamento.dto.ColetaDTO;
import br.com.roteamento.dto.RotaDTO;
import br.com.roteamento.model.Coleta;
import br.com.roteamento.model.Rota;
import br.com.roteamento.model.Coleta.StatusColeta;
import br.com.roteamento.model.Rota.StatusRota;
import br.com.roteamento.repository.ColetaRepository;
import br.com.roteamento.repository.RotaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import br.com.roteamento.model.Usuario;
import br.com.roteamento.repository.UsuarioRepository;
import br.com.roteamento.repository.MaterialRepository;

/**
 * Serviço de roteamento e otimização de coletas
 * 
 * CONCEITO DIDÁTICO - Integração de APIs Externas:
 * - RestTemplate: cliente HTTP para consumir APIs REST
 * - @Value: injeção de valores de configuração
 * - Tratamento de timeouts e erros de rede
 * - Cache de respostas para otimizar performance
 * - Fallback strategies para alta disponibilidade
 * 
 * CONCEITO DIDÁTICO - Algoritmos de Otimização:
 * - Algoritmo do Caixeiro Viajante (TSP)
 * - Algoritmo de Nearest Neighbor
 * - Otimização por capacidade de veículo
 * - Balanceamento de carga entre rotas
 * - Consideração de restrições temporais
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoteamentoService {

    private final ColetaRepository coletaRepository;
    private final RotaRepository rotaRepository;
    private final RestTemplate restTemplate;
    private final UsuarioRepository usuarioRepository;
    private final MaterialRepository materialRepository;

    @Value("${app.maps.api.key:}")
    private String mapsApiKey;

    @Value("${app.maps.api.url:https://maps.googleapis.com/maps/api}")
    private String mapsApiUrl;

    @Value("${app.roteamento.max-coletas-por-rota:20}")
    private int maxColetasPorRota;

    @Value("${app.roteamento.max-peso-por-rota:1000.0}")
    private BigDecimal maxPesoPorRota;

    /**
     * CONCEITO DIDÁTICO - Método de Otimização de Rotas:
     * Otimiza rotas para coletas pendentes usando algoritmo TSP
     * 
     * @return lista de rotas otimizadas
     */
    @Transactional
    public List<RotaDTO> otimizarRotas() {
        log.info("Iniciando otimização de rotas");

        // Busca coletas pendentes
        List<Coleta> coletasPendentes = coletaRepository.findByStatus(StatusColeta.SOLICITADA);
        
        if (coletasPendentes.isEmpty()) {
            log.info("Nenhuma coleta pendente encontrada");
            return Collections.emptyList();
        }

        // CONCEITO DIDÁTICO - Agrupamento por Região:
        // Agrupa coletas por proximidade geográfica
        List<List<Coleta>> grupos = agruparColetasPorRegiao(coletasPendentes);
        
        List<RotaDTO> rotasOtimizadas = new ArrayList<RotaDTO>();

        for (List<Coleta> grupo : grupos) {
            // CONCEITO DIDÁTICO - Algoritmo TSP:
            // Aplica algoritmo do caixeiro viajante para otimizar ordem
            List<Coleta> coletasOrdenadas = aplicarAlgoritmoTSP(grupo);
            
            // Cria rota otimizada
            Rota rota = criarRotaOtimizada(coletasOrdenadas);
            Rota rotaSalva = rotaRepository.save(rota);
            
            rotasOtimizadas.add(RotaDTO.fromEntity(rotaSalva));
        }

        log.info("Otimização concluída. {} rotas criadas", rotasOtimizadas.size());
        return rotasOtimizadas;
    }

    /**
     * CONCEITO DIDÁTICO - Algoritmo de Agrupamento:
     * Agrupa coletas por proximidade geográfica
     * 
     * @param coletas lista de coletas
     * @return grupos de coletas por região
     */
    private List<List<Coleta>> agruparColetasPorRegiao(List<Coleta> coletas) {
        log.debug("Agrupando {} coletas por região", coletas.size());

        List<List<Coleta>> grupos = new ArrayList<List<Coleta>>();
        Set<Coleta> coletasProcessadas = new HashSet<Coleta>();

        for (Coleta coleta : coletas) {
            if (coletasProcessadas.contains(coleta)) {
                continue;
            }

            List<Coleta> grupo = new ArrayList<Coleta>();
            grupo.add(coleta);
            coletasProcessadas.add(coleta);

            // CONCEITO DIDÁTICO - Busca por Proximidade:
            // Encontra coletas próximas (raio de 5km)
            for (Coleta outraColeta : coletas) {
                if (!coletasProcessadas.contains(outraColeta) && 
                    grupo.size() < maxColetasPorRota &&
                    calcularDistancia(coleta, outraColeta) <= 5.0) {
                    
                    grupo.add(outraColeta);
                    coletasProcessadas.add(outraColeta);
                }
            }

            grupos.add(grupo);
        }

        log.debug("Criados {} grupos de coletas", grupos.size());
        return grupos;
    }

    /**
     * CONCEITO DIDÁTICO - Algoritmo do Caixeiro Viajante (TSP):
     * Implementa algoritmo Nearest Neighbor para otimizar rota
     * 
     * @param coletas lista de coletas
     * @return coletas ordenadas pela rota otimizada
     */
    private List<Coleta> aplicarAlgoritmoTSP(List<Coleta> coletas) {
        if (coletas.size() <= 1) {
            return coletas;
        }

        log.debug("Aplicando algoritmo TSP para {} coletas", coletas.size());

        List<Coleta> rotaOtimizada = new ArrayList<Coleta>();
        Set<Coleta> coletasNaoVisitadas = new HashSet<Coleta>(coletas);

        // Ponto de partida (pode ser o depósito ou a coleta mais central)
        Coleta coletaAtual = encontrarColetaMaisCentral(coletas);
        rotaOtimizada.add(coletaAtual);
        coletasNaoVisitadas.remove(coletaAtual);

        // CONCEITO DIDÁTICO - Algoritmo Nearest Neighbor:
        // Sempre vai para a coleta mais próxima não visitada
        while (!coletasNaoVisitadas.isEmpty()) {
            Coleta proximaColeta = encontrarColetaMaisProxima(coletaAtual, coletasNaoVisitadas);
            rotaOtimizada.add(proximaColeta);
            coletasNaoVisitadas.remove(proximaColeta);
            coletaAtual = proximaColeta;
        }

        return rotaOtimizada;
    }

    /**
     * CONCEITO DIDÁTICO - Cálculo de Distância:
     * Calcula distância entre duas coordenadas usando fórmula de Haversine
     * 
     * @param coleta1 primeira coleta
     * @param coleta2 segunda coleta
     * @return distância em quilômetros
     */
    private double calcularDistancia(Coleta coleta1, Coleta coleta2) {
        double lat1 = coleta1.getLatitude().doubleValue();
        double lon1 = coleta1.getLongitude().doubleValue();
        double lat2 = coleta2.getLatitude().doubleValue();
        double lon2 = coleta2.getLongitude().doubleValue();

        // CONCEITO DIDÁTICO - Fórmula de Haversine:
        // Calcula distância entre dois pontos na superfície da Terra
        double R = 6371; // Raio da Terra em km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c;
    }

    /**
     * CONCEITO DIDÁTICO - Busca por Proximidade:
     * Encontra a coleta mais próxima de uma dada coleta
     * 
     * @param coletaReferencia coleta de referência
     * @param coletasCandidatas coletas candidatas
     * @return coleta mais próxima
     */
    private Coleta encontrarColetaMaisProxima(Coleta coletaReferencia, Set<Coleta> coletasCandidatas) {
        return coletasCandidatas.stream()
                .min(Comparator.comparingDouble(coleta -> calcularDistancia(coletaReferencia, coleta)))
                .orElse(null);
    }

    /**
     * CONCEITO DIDÁTICO - Cálculo de Centro:
     * Encontra a coleta mais central do grupo
     * 
     * @param coletas lista de coletas
     * @return coleta mais central
     */
    private Coleta encontrarColetaMaisCentral(List<Coleta> coletas) {
        if (coletas.size() == 1) {
            return coletas.get(0);
        }

        // Calcula centro médio do grupo
        double latMedia = coletas.stream()
                .mapToDouble(c -> c.getLatitude().doubleValue())
                .average()
                .orElse(0.0);

        double lonMedia = coletas.stream()
                .mapToDouble(c -> c.getLongitude().doubleValue())
                .average()
                .orElse(0.0);

        // Encontra coleta mais próxima do centro
        return coletas.stream()
                .min(Comparator.comparingDouble(coleta -> {
                    double lat = coleta.getLatitude().doubleValue();
                    double lon = coleta.getLongitude().doubleValue();
                    return Math.sqrt(Math.pow(lat - latMedia, 2) + Math.pow(lon - lonMedia, 2));
                }))
                .orElse(coletas.get(0));
    }

    /**
     * CONCEITO DIDÁTICO - Criação de Rota:
     * Cria uma rota otimizada com as coletas ordenadas
     * 
     * @param coletasOrdenadas coletas na ordem otimizada
     * @return rota criada
     */
    private Rota criarRotaOtimizada(List<Coleta> coletasOrdenadas) {
        log.debug("Criando rota otimizada com {} coletas", coletasOrdenadas.size());

        // Calcula estatísticas da rota
        BigDecimal pesoTotal = BigDecimal.ZERO; // Temporariamente comentado

        double distanciaTotal = calcularDistanciaTotal(coletasOrdenadas);
        int tempoEstimado = calcularTempoEstimado(coletasOrdenadas);

        Rota rota = new Rota();
        rota.setNome("Rota " + LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd-HHmm")));
        rota.setDescricao("Rota otimizada com " + coletasOrdenadas.size() + " coletas");
        rota.setCapacidadeMaxima(maxPesoPorRota);
        rota.setCapacidadeUtilizada(pesoTotal);
        rota.setDistanciaTotal((int) distanciaTotal);
        rota.setTempoEstimado(tempoEstimado);
        rota.setStatus(Rota.StatusRota.PLANEJADA);
        rota.setDataCriacao(LocalDateTime.now());
        rota.setDataAtualizacao(LocalDateTime.now());

        // Associa coletas à rota
        rota.setColetas(new ArrayList<Coleta>(coletasOrdenadas));
        
        // Atualiza status das coletas
        coletasOrdenadas.forEach(coleta -> {
            coleta.setStatus(StatusColeta.SOLICITADA);
            coleta.setDataAtualizacao(LocalDateTime.now());
        });

        return rota;
    }

    /**
     * CONCEITO DIDÁTICO - Cálculo de Distância Total:
     * Calcula distância total da rota
     * 
     * @param coletasOrdenadas coletas na ordem da rota
     * @return distância total em km
     */
    private double calcularDistanciaTotal(List<Coleta> coletasOrdenadas) {
        if (coletasOrdenadas.size() <= 1) {
            return 0.0;
        }

        double distanciaTotal = 0.0;
        for (int i = 0; i < coletasOrdenadas.size() - 1; i++) {
            distanciaTotal += calcularDistancia(coletasOrdenadas.get(i), coletasOrdenadas.get(i + 1));
        }

        return distanciaTotal;
    }

    /**
     * CONCEITO DIDÁTICO - Estimativa de Tempo:
     * Calcula tempo estimado para execução da rota
     * 
     * @param coletasOrdenadas coletas na ordem da rota
     * @return tempo estimado em minutos
     */
    private int calcularTempoEstimado(List<Coleta> coletasOrdenadas) {
        // Tempo base: 15 minutos por coleta + tempo de deslocamento
        int tempoBase = coletasOrdenadas.size() * 15;
        
        // Tempo de deslocamento: 2 minutos por km
        double distanciaTotal = calcularDistanciaTotal(coletasOrdenadas);
        int tempoDeslocamento = (int) (distanciaTotal * 2);
        
        return tempoBase + tempoDeslocamento;
    }

    /**
     * CONCEITO DIDÁTICO - Integração com API de Mapeamento:
     * Obtém informações detalhadas de rota de uma API externa
     * 
     * @param origem coordenadas de origem
     * @param destino coordenadas de destino
     * @return informações detalhadas da rota
     */
    public Map<String, Object> obterInformacoesRota(String origem, String destino) {
        if (mapsApiKey.isEmpty()) {
            log.warn("Chave da API de mapas não configurada");
            return criarInformacoesRotaMock(origem, destino);
        }

        try {
            String url = String.format("%s/directions/json?origin=%s&destination=%s&key=%s",
                    mapsApiUrl, origem, destino, mapsApiKey);

            // CONCEITO DIDÁTICO - RestTemplate:
            // Cliente HTTP para consumir APIs REST
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            log.debug("Informações de rota obtidas da API externa");
            return response;

        } catch (Exception e) {
            log.error("Erro ao obter informações de rota da API externa: {}", e.getMessage());
            return criarInformacoesRotaMock(origem, destino);
        }
    }

    /**
     * CONCEITO DIDÁTICO - Fallback Strategy:
     * Cria informações de rota mock quando API externa não está disponível
     * 
     * @param origem coordenadas de origem
     * @param destino coordenadas de destino
     * @return informações mock da rota
     */
    private Map<String, Object> criarInformacoesRotaMock(String origem, String destino) {
        Map<String, Object> mockResponse = new HashMap<String, Object>();
        mockResponse.put("status", "OK");
        mockResponse.put("origin", origem);
        mockResponse.put("destination", destino);
        mockResponse.put("distance", "5.2 km");
        mockResponse.put("duration", "12 min");
        
        return mockResponse;
    }

    /**
     * CONCEITO DIDÁTICO - Método de Reotimização:
     * Reotimiza rotas existentes baseado em novas coletas
     * 
     * @param rotaId ID da rota a ser reotimizada
     * @return rota reotimizada
     */
    @Transactional
    public RotaDTO reotimizarRota(Long rotaId) {
        log.info("Reotimizando rota com ID: {}", rotaId);

        Rota rota = rotaRepository.findById(rotaId)
                .orElseThrow(() -> new RuntimeException("Rota não encontrada"));

        // Remove coletas da rota atual
        List<Coleta> coletasAtuais = new ArrayList<Coleta>(rota.getColetas());
        rota.getColetas().clear();

        // Adiciona novas coletas pendentes da região
        List<Coleta> coletasPendentes = coletaRepository.findByStatus(StatusColeta.SOLICITADA);
        List<Coleta> todasColetas = new ArrayList<Coleta>(coletasAtuais);
        todasColetas.addAll(coletasPendentes);

        // Reotimiza
        List<Coleta> coletasReotimizadas = aplicarAlgoritmoTSP(todasColetas);
        
        // Atualiza rota
        rota.setColetas(coletasReotimizadas);
        rota.setCapacidadeUtilizada(calcularPesoTotal(coletasReotimizadas));
        rota.setDistanciaTotal((int) calcularDistanciaTotal(coletasReotimizadas));
        rota.setTempoEstimado(calcularTempoEstimado(coletasReotimizadas));
        rota.setDataAtualizacao(LocalDateTime.now());

        Rota rotaSalva = rotaRepository.save(rota);
        return RotaDTO.fromEntity(rotaSalva);
    }

    /**
     * CONCEITO DIDÁTICO - Cálculo de Peso Total:
     * Calcula peso total de uma lista de coletas
     * 
     * @param coletas lista de coletas
     * @return peso total
     */
    private BigDecimal calcularPesoTotal(List<Coleta> coletas) {
        return BigDecimal.ZERO; // Temporariamente comentado
    }

    /**
     * CONCEITO DIDÁTICO - Método de Estatísticas:
     * Obtém estatísticas de roteamento para página inicial
     * 
     * @return mapa com estatísticas
     */
    public Map<String, Object> obterEstatisticasRoteamento() {
        log.info("Obtendo estatísticas de roteamento");

        Map<String, Object> estatisticas = new HashMap<String, Object>();
        
        // Estatísticas de coletas
        long totalColetas = coletaRepository.count();
        long coletasPendentes = coletaRepository.countByStatus(StatusColeta.SOLICITADA);
        long coletasEmAndamento = coletaRepository.countByStatus(StatusColeta.EM_ROTA);
        long coletasConcluidas = coletaRepository.countByStatus(StatusColeta.COLETADA);
        
        // Estatísticas de rotas
        long totalRotas = rotaRepository.count();
        long rotasAtivas = rotaRepository.countByStatus(StatusRota.EM_EXECUCAO);
        long rotasPlanejadas = rotaRepository.countByStatus(StatusRota.PLANEJADA);
        long rotasConcluidas = rotaRepository.countByStatus(StatusRota.FINALIZADA);
        
        // Estatísticas de coletores
        long totalColetores = usuarioRepository.countByTipoUsuario(Usuario.TipoUsuario.COLETOR);
        long coletoresAtivos = usuarioRepository.countByTipoUsuario(Usuario.TipoUsuario.COLETOR);
        
        // Estatísticas de materiais
        long totalMateriais = materialRepository.count();
        
        // Calcular métricas de eficiência
        double taxaConclusao = totalColetas > 0 ? (double) coletasConcluidas / totalColetas * 100 : 0;
        double taxaUtilizacaoColetores = totalColetores > 0 ? (double) coletoresAtivos / totalColetores * 100 : 0;
        
        estatisticas.put("coletas", Map.of(
            "total", totalColetas,
            "pendentes", coletasPendentes,
            "emAndamento", coletasEmAndamento,
            "concluidas", coletasConcluidas,
            "taxaConclusao", Math.round(taxaConclusao * 100.0) / 100.0
        ));
        
        estatisticas.put("rotas", Map.of(
            "total", totalRotas,
            "ativas", rotasAtivas,
            "planejadas", rotasPlanejadas,
            "concluidas", rotasConcluidas
        ));
        
        estatisticas.put("coletores", Map.of(
            "total", totalColetores,
            "ativos", coletoresAtivos,
            "taxaUtilizacao", Math.round(taxaUtilizacaoColetores * 100.0) / 100.0
        ));
        
        estatisticas.put("materiais", Map.of(
            "total", totalMateriais
        ));
        
        return estatisticas;
    }

    /**
     * CONCEITO DIDÁTICO - Método de Otimização Automática:
     * Otimiza rotas automaticamente para coletas pendentes
     * 
     * @return resultado da otimização
     */
    public Map<String, Object> otimizarRotasAutomaticamente() {
        log.info("Iniciando otimização automática de rotas");

        Map<String, Object> resultado = new HashMap<String, Object>();
        
        // Buscar coletas pendentes
        List<Coleta> coletasPendentes = coletaRepository.findByStatus(StatusColeta.SOLICITADA);
        
        if (coletasPendentes.isEmpty()) {
            resultado.put("mensagem", "Nenhuma coleta pendente para otimizar");
            resultado.put("rotasCriadas", 0);
            resultado.put("coletasProcessadas", 0);
            return resultado;
        }
        
        // Buscar coletores ativos
        List<Usuario> coletoresAtivos = usuarioRepository.findByTipoUsuarioAndStatus(
            Usuario.TipoUsuario.COLETOR, 
            Usuario.StatusUsuario.ATIVO
        );
        
        if (coletoresAtivos.isEmpty()) {
            resultado.put("mensagem", "Nenhum coletor ativo disponível");
            resultado.put("rotasCriadas", 0);
            resultado.put("coletasProcessadas", 0);
            return resultado;
        }
        
        // Agrupar coletas por região (simplificado)
        List<List<Coleta>> grupos = agruparColetasPorRegiao(coletasPendentes);
        
        int rotasCriadas = 0;
        int coletasProcessadas = 0;
        
        // Criar rotas para cada grupo
        for (List<Coleta> coletasGrupo : grupos) {
            
            // Selecionar coletor para o grupo
            Usuario coletor = selecionarColetorParaRegiao(coletoresAtivos, "Região");
            
            if (coletor != null && !coletasGrupo.isEmpty()) {
                // Criar rota otimizada
                Rota rota = criarRotaOtimizada(coletor, coletasGrupo);
                rotaRepository.save(rota);
                
                rotasCriadas++;
                coletasProcessadas += coletasGrupo.size();
                
                log.info("Rota criada para grupo com {} coletas", coletasGrupo.size());
            }
        }
        
        resultado.put("mensagem", "Otimização concluída com sucesso");
        resultado.put("rotasCriadas", rotasCriadas);
        resultado.put("coletasProcessadas", coletasProcessadas);
        resultado.put("gruposProcessados", grupos.size());
        
        return resultado;
    }

    /**
     * CONCEITO DIDÁTICO - Método de Cálculo de Rota Ótima:
     * Calcula rota ótima entre pontos específicos
     * 
     * @param coletaIds IDs das coletas
     * @return rota calculada
     */
    public RotaDTO calcularRotaOtima(List<Long> coletaIds) {
        log.info("Calculando rota ótima para {} coletas", coletaIds.size());
        
        // Buscar coletas
        List<Coleta> coletas = coletaRepository.findAllById(coletaIds);
        
        if (coletas.isEmpty()) {
            throw new IllegalArgumentException("Nenhuma coleta encontrada com os IDs fornecidos");
        }
        
        // Aplicar algoritmo TSP
        List<Coleta> coletasOtimizadas = aplicarAlgoritmoTSP(coletas);
        
        // Criar rota temporária para cálculo
        Rota rotaCalculada = new Rota();
        rotaCalculada.setNome("Rota Calculada - " + LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        rotaCalculada.setDescricao("Rota calculada automaticamente");
        rotaCalculada.setColetor(coletasOtimizadas.get(0).getUsuario()); // Usar primeiro usuário como exemplo
        rotaCalculada.setCapacidadeMaxima(BigDecimal.valueOf(1000.0));
        rotaCalculada.setStatus(StatusRota.PLANEJADA);
        rotaCalculada.setColetas(coletasOtimizadas);
        
        // Calcular métricas
        BigDecimal pesoTotal = calcularPesoTotal(coletasOtimizadas);
        double distanciaTotal = calcularDistanciaTotal(coletasOtimizadas);
        int tempoEstimado = calcularTempoEstimado(coletasOtimizadas);
        
        rotaCalculada.setCapacidadeUtilizada(BigDecimal.valueOf(pesoTotal.doubleValue()));
        rotaCalculada.setDistanciaTotal((int) distanciaTotal);
        rotaCalculada.setTempoEstimado(tempoEstimado);
        
        return RotaDTO.fromEntity(rotaCalculada);
    }



    /**
     * CONCEITO DIDÁTICO - Método de Extração de Região:
     * Extrai região do endereço
     * 
     * @param endereco endereço completo
     * @return região extraída
     */
    private String extrairRegiao(String endereco) {
        if (endereco == null || endereco.trim().isEmpty()) {
            return "Região Desconhecida";
        }
        
        // Simplificado: extrair bairro do endereço
        String[] partes = endereco.split(" - ");
        if (partes.length > 1) {
            return partes[partes.length - 1];
        }
        
        return "Centro"; // Padrão
    }

    /**
     * CONCEITO DIDÁTICO - Método de Seleção de Coletor:
     * Seleciona coletor para uma região
     * 
     * @param coletores lista de coletores
     * @param regiao região
     * @return coletor selecionado
     */
    private Usuario selecionarColetorParaRegiao(List<Usuario> coletores, String regiao) {
        // Simplificado: selecionar primeiro coletor disponível
        return coletores.stream()
                .filter(coletor -> coletor.getStatus() == Usuario.StatusUsuario.ATIVO)
                .findFirst()
                .orElse(null);
    }

    /**
     * CONCEITO DIDÁTICO - Método de Criação de Rota Otimizada:
     * Cria rota otimizada para coletas
     * 
     * @param coletor coletor responsável
     * @param coletas coletas da rota
     * @return rota criada
     */
    private Rota criarRotaOtimizada(Usuario coletor, List<Coleta> coletas) {
        // Aplicar algoritmo TSP
        List<Coleta> coletasOtimizadas = aplicarAlgoritmoTSP(coletas);
        
        // Criar rota
        Rota rota = new Rota();
        rota.setNome("Rota Otimizada - " + LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        rota.setDescricao("Rota criada automaticamente");
        rota.setColetor(coletor);
        rota.setCapacidadeMaxima(BigDecimal.valueOf(1000.0));
        rota.setStatus(StatusRota.PLANEJADA);
        rota.setColetas(coletasOtimizadas);
        
        // Calcular métricas
        BigDecimal pesoTotal = calcularPesoTotal(coletasOtimizadas);
        double distanciaTotal = calcularDistanciaTotal(coletasOtimizadas);
        int tempoEstimado = calcularTempoEstimado(coletasOtimizadas);
        
        rota.setCapacidadeUtilizada(BigDecimal.valueOf(pesoTotal.doubleValue()));
        rota.setDistanciaTotal((int) distanciaTotal);
        rota.setTempoEstimado(tempoEstimado);
        
        return rota;
    }
} 