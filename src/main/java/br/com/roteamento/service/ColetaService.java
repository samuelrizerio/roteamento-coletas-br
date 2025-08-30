package br.com.roteamento.service;

import br.com.roteamento.dto.ColetaDTO;
import br.com.roteamento.exception.ColetaNaoEncontradaException;
import br.com.roteamento.exception.MaterialNaoEncontradoException;
import br.com.roteamento.exception.UsuarioNaoEncontradoException;
import br.com.roteamento.model.Coleta;
import br.com.roteamento.model.Material;
import br.com.roteamento.model.Usuario;
import br.com.roteamento.repository.ColetaRepository;
import br.com.roteamento.repository.MaterialRepository;
import br.com.roteamento.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Serviço de gerenciamento de coletas
 * 
 * CONCEITO DIDÁTICO - Service Layer:
 * - Lógica de negócio centralizada
 * - Transações de banco de dados
 * - Validações de regras de negócio
 * - Tratamento de exceções customizadas
 * - Logging de operações importantes
 */
@Service
@RequiredArgsConstructor
public class ColetaService {

    private static final Logger log = LoggerFactory.getLogger(ColetaService.class);
    private final ColetaRepository coletaRepository;
    private final UsuarioRepository usuarioRepository;
    private final MaterialRepository materialRepository;

    /**
     * MÉTODO PARA CRIAR UMA NOVA COLETA
     * 
     * CONCEITOS:
     * - @Transactional: Gerencia transação de banco de dados
     * - Validação: Verifica se usuário e material existem
     * - Cálculo: Calcula valor estimado automaticamente
     * - Logging: Registra operações importantes
     * 
     * @param coletaDTO Dados da coleta a ser criada
     * @return ColetaDTO da coleta criada
     * @throws UsuarioNaoEncontradoException Se o usuário não existir
     * @throws MaterialNaoEncontradoException Se o material não existir
     */
    @Transactional
    public ColetaDTO criarColeta(ColetaDTO coletaDTO) {
        log.info("Criando nova coleta para usuário: {}", coletaDTO.getUsuarioId());

        // Buscar usuário
        Usuario usuario = usuarioRepository.findById(coletaDTO.getUsuarioId())
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado: " + coletaDTO.getUsuarioId()));

        // Buscar material (usando o primeiro material da lista)
        Material material = null;
        if (coletaDTO.getMaterialIds() != null && !coletaDTO.getMaterialIds().isEmpty()) {
            material = materialRepository.findById(coletaDTO.getMaterialIds().get(0))
                    .orElseThrow(() -> new MaterialNaoEncontradoException("Material não encontrado: " + coletaDTO.getMaterialIds().get(0)));
        } else {
            throw new MaterialNaoEncontradoException("Pelo menos um material deve ser especificado");
        }

        // Criar entidade coleta usando construtor
        Coleta coleta = new Coleta(usuario, material, coletaDTO.getPesoEstimado());
        coleta.setEndereco(coletaDTO.getEndereco());
        coleta.setLatitude(coletaDTO.getLatitude() != null ? coletaDTO.getLatitude().doubleValue() : 0.0);
        coleta.setLongitude(coletaDTO.getLongitude() != null ? coletaDTO.getLongitude().doubleValue() : 0.0);
        coleta.setObservacoes(coletaDTO.getObservacoes());
        coleta.setStatus(Coleta.StatusColeta.SOLICITADA);
        coleta.setDataSolicitada(LocalDateTime.now());

        // Calcular valor estimado
        coleta.calcularValorEstimado();

        // Salvar coleta
        Coleta coletaSalva = coletaRepository.save(coleta);
        log.info("Coleta criada com sucesso. ID: {}", coletaSalva.getId());

        return ColetaDTO.fromEntity(coletaSalva);
    }

    /**
     * MÉTODO PARA BUSCAR COLETA POR ID
     * 
     * CONCEITOS:
     * - @Transactional(readOnly = true): Transação apenas de leitura
     * - Optional: Wrapper que pode conter valor ou ser vazio
     * - Tratamento de exceções customizadas
     * 
     * @param id ID da coleta
     * @return ColetaDTO da coleta encontrada
     * @throws ColetaNaoEncontradaException Se a coleta não existir
     */
    @Transactional(readOnly = true)
    public ColetaDTO buscarColetaPorId(Long id) {
        log.debug("Buscando coleta por ID: {}", id);

        return coletaRepository.findById(id)
                .map(ColetaDTO::fromEntity)
                .orElseThrow(() -> new ColetaNaoEncontradaException("Coleta não encontrada: " + id));
    }

    /**
     * MÉTODO PARA LISTAR TODAS AS COLETAS
     * 
     * CONCEITOS:
     * - @Transactional(readOnly = true): Transação apenas de leitura
     * - Stream API: Transformação de dados
     * - Mapeamento de entidades para DTOs
     * 
     * @return Lista de ColetaDTO
     */
    @Transactional(readOnly = true)
    public List<ColetaDTO> listarTodasColetas() {
        log.debug("Listando todas as coletas com relacionamentos");

        return coletaRepository.findAllWithRelacionamentos().stream()
                .map(ColetaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * MÉTODO PARA BUSCAR COLETAS POR USUÁRIO
     * 
     * CONCEITOS:
     * - Filtro por relacionamento
     * - Consulta específica
     * 
     * @param usuarioId ID do usuário
     * @return Lista de ColetaDTO do usuário
     */
    @Transactional(readOnly = true)
    public List<ColetaDTO> buscarColetasPorUsuario(Long usuarioId) {
        log.debug("Buscando coletas do usuário: {}", usuarioId);

        return coletaRepository.findAllWithRelacionamentos().stream()
                .filter(coleta -> coleta.getUsuario().getId().equals(usuarioId))
                .map(ColetaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * MÉTODO PARA BUSCAR COLETAS POR STATUS
     * 
     * CONCEITOS:
     * - Filtro por enum
     * - Consulta específica
     * 
     * @param status Status da coleta
     * @return Lista de ColetaDTO com o status
     */
    @Transactional(readOnly = true)
    public List<ColetaDTO> buscarColetasPorStatus(Coleta.StatusColeta status) {
        log.debug("Buscando coletas com status: {}", status);

        return coletaRepository.findAllWithRelacionamentos().stream()
                .filter(coleta -> coleta.getStatus().equals(status))
                .map(ColetaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * MÉTODO PARA ATUALIZAR COLETA
     * 
     * CONCEITOS:
     * - Atualização de entidade existente
     * - Validação de existência
     * - Merge de dados
     * 
     * @param id ID da coleta
     * @param coletaDTO Novos dados da coleta
     * @return ColetaDTO da coleta atualizada
     * @throws ColetaNaoEncontradaException Se a coleta não existir
     */
    @Transactional
    public ColetaDTO atualizarColeta(Long id, ColetaDTO coletaDTO) {
        log.info("Atualizando coleta. ID: {}", id);

        Coleta coleta = coletaRepository.findById(id)
                .orElseThrow(() -> new ColetaNaoEncontradaException("Coleta não encontrada: " + id));

        // Atualizar campos
        if (coletaDTO.getEndereco() != null) {
            coleta.setEndereco(coletaDTO.getEndereco());
        }
        if (coletaDTO.getLatitude() != null) {
            coleta.setLatitude(coletaDTO.getLatitude().doubleValue());
        }
        if (coletaDTO.getLongitude() != null) {
            coleta.setLongitude(coletaDTO.getLongitude().doubleValue());
        }
        if (coletaDTO.getObservacoes() != null) {
            coleta.setObservacoes(coletaDTO.getObservacoes());
        }
        if (coletaDTO.getStatus() != null) {
            coleta.setStatus(coletaDTO.getStatus());
        }
        if (coletaDTO.getPesoEstimado() != null) {
            coleta.setQuantidade(coletaDTO.getPesoEstimado());
        }
        if (coletaDTO.getDescricaoMateriais() != null) {
            // coleta.setDescricaoMateriais(coletaDTO.getDescricaoMateriais()); // Não existe na entidade Coleta
        }

        coleta.setDataAtualizacao(LocalDateTime.now());

        Coleta coletaAtualizada = coletaRepository.save(coleta);
        log.info("Coleta atualizada com sucesso. ID: {}", id);

        return ColetaDTO.fromEntity(coletaAtualizada);
    }

    /**
     * MÉTODO PARA EXCLUIR COLETA
     * 
     * CONCEITOS:
     * - Exclusão de entidade
     * - Validação de existência
     * - Soft delete (opcional)
     * 
     * @param id ID da coleta
     * @throws ColetaNaoEncontradaException Se a coleta não existir
     */
    @Transactional
    public void excluirColeta(Long id) {
        log.info("Excluindo coleta. ID: {}", id);

        if (!coletaRepository.existsById(id)) {
            throw new ColetaNaoEncontradaException("Coleta não encontrada: " + id);
        }

        coletaRepository.deleteById(id);
        log.info("Coleta excluída com sucesso. ID: {}", id);
    }

    /**
     * MÉTODO PARA ACEITAR COLETA
     * 
     * CONCEITOS:
     * - Mudança de status da coleta
     * - Validação de coletor
     * - Registro de data de aceitação
     * 
     * @param coletaId ID da coleta
     * @param coletorId ID do coletor
     * @return ColetaDTO da coleta aceita
     * @throws ColetaNaoEncontradaException Se a coleta não existir
     * @throws UsuarioNaoEncontradoException Se o coletor não existir
     * @throws IllegalArgumentException Se o usuário não for coletor
     */
    @Transactional
    public ColetaDTO aceitarColeta(Long coletaId, Long coletorId) {
        log.info("Aceitando coleta. Coleta ID: {}, Coletor ID: {}", coletaId, coletorId);

        Coleta coleta = coletaRepository.findById(coletaId)
                .orElseThrow(() -> new ColetaNaoEncontradaException("Coleta não encontrada: " + coletaId));

        Usuario coletor = usuarioRepository.findById(coletorId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Coletor não encontrado: " + coletorId));

        // Validar se o usuário é um coletor
        if (coletor.getTipoUsuario() != Usuario.TipoUsuario.COLETOR) {
            throw new IllegalArgumentException("Usuário não é um coletor: " + coletorId);
        }

        // Aceitar a coleta
        coleta.aceitarColeta(coletor);

        Coleta coletaAceita = coletaRepository.save(coleta);
        log.info("Coleta aceita com sucesso. Coleta ID: {}, Coletor ID: {}", coletaId, coletorId);

        return ColetaDTO.fromEntity(coletaAceita);
    }

    /**
     * MÉTODO PARA FINALIZAR COLETA
     * 
     * CONCEITOS:
     * - Mudança de status da coleta
     * - Registro de valor final
     * - Data de coleta
     * 
     * @param coletaId ID da coleta
     * @param valorFinal Valor final da coleta
     * @return ColetaDTO da coleta finalizada
     * @throws ColetaNaoEncontradaException Se a coleta não existir
     */
    @Transactional
    public ColetaDTO finalizarColeta(Long coletaId, BigDecimal valorFinal) {
        log.info("Finalizando coleta. Coleta ID: {}, Valor Final: {}", coletaId, valorFinal);

        Coleta coleta = coletaRepository.findById(coletaId)
                .orElseThrow(() -> new ColetaNaoEncontradaException("Coleta não encontrada: " + coletaId));

        // Finalizar a coleta
        coleta.finalizarColeta(valorFinal);

        Coleta coletaFinalizada = coletaRepository.save(coleta);
        log.info("Coleta finalizada com sucesso. Coleta ID: {}", coletaId);

        return ColetaDTO.fromEntity(coletaFinalizada);
    }

    /**
     * MÉTODO PARA CANCELAR COLETA
     * 
     * CONCEITOS:
     * - Mudança de status da coleta
     * - Validação de regras de negócio
     * 
     * @param coletaId ID da coleta
     * @return ColetaDTO da coleta cancelada
     * @throws ColetaNaoEncontradaException Se a coleta não existir
     */
    @Transactional
    public ColetaDTO cancelarColeta(Long coletaId) {
        log.info("Cancelando coleta. Coleta ID: {}", coletaId);

        Coleta coleta = coletaRepository.findById(coletaId)
                .orElseThrow(() -> new ColetaNaoEncontradaException("Coleta não encontrada: " + coletaId));

        // Cancelar a coleta
        coleta.cancelarColeta();

        Coleta coletaCancelada = coletaRepository.save(coleta);
        log.info("Coleta cancelada com sucesso. Coleta ID: {}", coletaId);

        return ColetaDTO.fromEntity(coletaCancelada);
    }

    /**
     * MÉTODO PARA CONTAR COLETAS POR STATUS
     * 
     * CONCEITOS:
     * - Contagem para estatísticas
     * - Método de agregação
     * 
     * @param status Status da coleta
     * @return Número de coletas com o status especificado
     */
    @Transactional(readOnly = true)
    public Long contarColetasPorStatus(Coleta.StatusColeta status) {
        log.debug("Contando coletas com status: {}", status);
        return coletaRepository.countByStatus(status);
    }

    /**
     * MÉTODO PARA BUSCAR COLETAS PENDENTES
     * 
     * CONCEITOS:
     * - Filtro por coletas que podem ser aceitas
     * - Status específicos
     * 
     * @return Lista de ColetaDTO pendentes
     */
    @Transactional(readOnly = true)
    public List<ColetaDTO> buscarColetasPendentes() {
        log.debug("Buscando coletas pendentes");

        return coletaRepository.findByStatusIn(List.of(
                Coleta.StatusColeta.SOLICITADA,
                Coleta.StatusColeta.EM_ANALISE
        )).stream()
                .map(ColetaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * MÉTODO PARA BUSCAR COLETAS POR REGIÃO
     * 
     * CONCEITOS:
     * - Filtro por coordenadas geográficas
     * - Cálculo de distância usando fórmula de Haversine
     * - Raio de busca configurável
     * 
     * @param latitude Latitude do centro da região
     * @param longitude Longitude do centro da região
     * @param raioKm Raio de busca em quilômetros
     * @return Lista de ColetaDTO na região
     */
    @Transactional(readOnly = true)
    public List<ColetaDTO> buscarColetasPorRegiao(Double latitude, Double longitude, Double raioKm) {
        log.debug("Buscando coletas na região: lat={}, lng={}, raio={}km", latitude, longitude, raioKm);

        return coletaRepository.findAll().stream()
                .filter(coleta -> {
                    if (coleta.getLatitude() == null || coleta.getLongitude() == null) {
                        return false;
                    }
                    
                    double distancia = calcularDistanciaHaversine(
                        latitude, longitude,
                        coleta.getLatitude(), coleta.getLongitude()
                    );
                    
                    return distancia <= raioKm;
                })
                .map(ColetaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * MÉTODO PARA GERAR CLUSTERS DE COLETAS
     * 
     * CONCEITOS:
     * - Agrupamento de pontos próximos
     * - Redução de sobreposição no mapa
     * - Performance de renderização
     * 
     * @param zoom Nível de zoom do mapa
     * @param bounds Limites da visualização (north,south,east,west)
     * @return Lista de clusters
     */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> gerarClusters(Integer zoom, String bounds) {
        log.debug("Gerando clusters para zoom={}, bounds={}", zoom, bounds);

        // Implementação simplificada - agrupa coletas por proximidade
        List<Coleta> coletas = coletaRepository.findAll();
        
        return coletas.stream()
                .filter(coleta -> coleta.getLatitude() != null && coleta.getLongitude() != null)
                .map(coleta -> {
                    Map<String, Object> cluster = Map.of(
                        "id", coleta.getId(),
                        "latitude", coleta.getLatitude(),
                        "longitude", coleta.getLongitude(),
                        "status", coleta.getStatus().toString(),
                        "quantidade", coleta.getQuantidade(),
                        "material", coleta.getMaterial() != null ? coleta.getMaterial().getNome() : "N/A"
                    );
                    return cluster;
                })
                .collect(Collectors.toList());
    }

    /**
     * MÉTODO PARA CALCULAR DISTÂNCIA USANDO FÓRMULA DE HAVERSINE
     * 
     * CONCEITOS:
     * - Fórmula matemática para cálculo de distância entre coordenadas
     * - Considera a curvatura da Terra
     * - Resultado em quilômetros
     * 
     * @param lat1 Latitude do primeiro ponto
     * @param lon1 Longitude do primeiro ponto
     * @param lat2 Latitude do segundo ponto
     * @param lon2 Longitude do segundo ponto
     * @return Distância em quilômetros
     */
    private double calcularDistanciaHaversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Raio da Terra em quilômetros

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c;
    }
} 