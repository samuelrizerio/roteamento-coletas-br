package br.com.roteamento.service;

import br.com.roteamento.dto.RotaDTO;
import br.com.roteamento.model.Rota;
import br.com.roteamento.model.Usuario;
import br.com.roteamento.repository.RotaRepository;
import br.com.roteamento.repository.UsuarioRepository;
import br.com.roteamento.exception.RotaNaoEncontradaException;
import br.com.roteamento.exception.UsuarioNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

/**
 * SERVIÇO ROTA - Classe que contém a lógica de negócio para rotas
 * 
 * CONCEITOS DIDÁTICOS EXPLICADOS:
 * 
 * 1. CAMADA DE SERVIÇO:
 *    - Contém a lógica de negócio da aplicação
 *    - Separa a camada de apresentação da camada de dados
 *    - Implementa regras de negócio
 *    - Gerencia transações
 * 
 * 2. INJEÇÃO DE DEPENDÊNCIA:
 *    - @RequiredArgsConstructor: Gera construtor com campos final
 *    - Dependency Injection: Padrão para desacoplamento
 *    - Inversão de Controle (IoC): Spring gerencia dependências
 * 
 * 3. TRANSAÇÕES:
 *    - @Transactional: Gerencia transações de banco de dados
 *    - ACID: Atomicidade, Consistência, Isolamento, Durabilidade
 *    - Rollback: Desfaz operações em caso de erro
 *    - Commit: Confirma operações bem-sucedidas
 * 
 * 4. TRATAMENTO DE EXCEÇÕES:
 *    - Exceções customizadas para casos específicos
 *    - Logging de erros para debug
 *    - Mensagens de erro amigáveis
 *    - Controle de fluxo da aplicação
 */
@Service
@RequiredArgsConstructor
public class RotaService {

    private static final Logger log = LoggerFactory.getLogger(RotaService.class);

    /**
     * REPOSITÓRIO DE ROTAS
     * 
     * CONCEITOS:
     * - final: Campo imutável após inicialização
     * - Injeção via construtor (recomendado)
     * - Acesso aos dados de rotas
     */
    private final RotaRepository rotaRepository;

    /**
     * REPOSITÓRIO DE USUÁRIOS
     * 
     * CONCEITOS:
     * - Necessário para validar coletores
     * - Verificar se coletor existe antes de criar rota
     */
    private final UsuarioRepository usuarioRepository;

    /**
     * MÉTODO PARA CRIAR UMA NOVA ROTA
     * 
     * CONCEITOS:
     * - @Transactional: Gerencia transação de banco de dados
     * - Validação: Verifica se coletor existe
     * - Cálculo: Calcula valores automaticamente
     * - Logging: Registra operações importantes
     * 
     * @param rotaDTO Dados da rota a ser criada
     * @return RotaDTO da rota criada
     * @throws UsuarioNaoEncontradoException Se o coletor não existir
     */
    @Transactional
    public RotaDTO criarRota(RotaDTO rotaDTO) {
        log.info("Criando nova rota para coletor: {}", rotaDTO.getColetorId());

        // Buscar coletor
        Usuario coletor = usuarioRepository.findById(rotaDTO.getColetorId())
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Coletor não encontrado: " + rotaDTO.getColetorId()));

        // Verificar se o usuário é um coletor
        if (coletor.getTipoUsuario() != Usuario.TipoUsuario.COLETOR) {
            throw new IllegalArgumentException("Usuário não é um coletor: " + rotaDTO.getColetorId());
        }

        // Criar entidade rota
        Rota rota = new Rota(coletor, rotaDTO.getNome());
        rota.setDescricao(rotaDTO.getDescricao());
        rota.setStatus(Rota.StatusRota.PLANEJADA);
        rota.setDistanciaTotal(rotaDTO.getDistanciaTotal() != null ? rotaDTO.getDistanciaTotal().intValue() : 0);
        rota.setTempoEstimado(rotaDTO.getTempoEstimado());
        rota.setCapacidadeMaxima(rotaDTO.getCapacidadeMaxima());
        rota.setValorTotalEstimado(rotaDTO.getValorTotalEstimado());
        rota.setObservacoes(rotaDTO.getObservacoes());

        // Salvar rota
        Rota rotaSalva = rotaRepository.save(rota);
        log.info("Rota criada com sucesso. ID: {}", rotaSalva.getId());

        return RotaDTO.fromEntity(rotaSalva);
    }

    /**
     * MÉTODO PARA BUSCAR ROTA POR ID
     * 
     * CONCEITOS:
     * - @Transactional(readOnly = true): Transação apenas de leitura
     * - Tratamento de exceções customizadas
     * 
     * @param id ID da rota
     * @return RotaDTO da rota encontrada
     * @throws RotaNaoEncontradaException Se a rota não existir
     */
    @Transactional(readOnly = true)
    public RotaDTO buscarRotaPorId(Long id) {
        log.debug("Buscando rota por ID: {}", id);

        Rota rota = rotaRepository.findById(id)
                .orElseThrow(() -> new RotaNaoEncontradaException("Rota não encontrada: " + id));

        return RotaDTO.fromEntity(rota);
    }

    /**
     * MÉTODO PARA LISTAR TODAS AS ROTAS
     * 
     * CONCEITOS:
     * - @Transactional(readOnly = true): Transação apenas de leitura
     * - Stream API: Processamento funcional de coleções
     * - Mapeamento: Converte entidades para DTOs
     * 
     * @return Lista de RotaDTO
     */
    @Transactional(readOnly = true)
    public List<RotaDTO> listarTodasRotas() {
        log.debug("Listando todas as rotas");

        return rotaRepository.findAll().stream()
                .map(RotaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * MÉTODO PARA BUSCAR ROTAS POR COLETOR
     * 
     * CONCEITOS:
     * - Filtro por coletor específico
     * - Stream API para processamento
     * 
     * @param coletorId ID do coletor
     * @return Lista de RotaDTO do coletor
     */
    @Transactional(readOnly = true)
    public List<RotaDTO> buscarRotasPorColetor(Long coletorId) {
        log.debug("Buscando rotas do coletor: {}", coletorId);

        return rotaRepository.findByColetorId(coletorId).stream()
                .map(RotaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * MÉTODO PARA BUSCAR ROTAS POR STATUS
     * 
     * CONCEITOS:
     * - Filtro por status específico
     * - Enum: Tipo especial que define constantes
     * 
     * @param status Status da rota
     * @return Lista de RotaDTO com o status especificado
     */
    @Transactional(readOnly = true)
    public List<RotaDTO> buscarRotasPorStatus(Rota.StatusRota status) {
        log.debug("Buscando rotas com status: {}", status);

        return rotaRepository.findByStatus(status).stream()
                .map(RotaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * MÉTODO PARA ATUALIZAR ROTA
     * 
     * CONCEITOS:
     * - @Transactional: Gerencia transação de banco de dados
     * - Validação: Verifica se rota existe
     * - Atualização: Modifica dados existentes
     * 
     * @param id ID da rota
     * @param rotaDTO Novos dados da rota
     * @return RotaDTO da rota atualizada
     * @throws RotaNaoEncontradaException Se a rota não existir
     */
    @Transactional
    public RotaDTO atualizarRota(Long id, RotaDTO rotaDTO) {
        log.info("Atualizando rota. ID: {}", id);

        Rota rota = rotaRepository.findById(id)
                .orElseThrow(() -> new RotaNaoEncontradaException("Rota não encontrada: " + id));

        // Atualizar campos permitidos
        if (rotaDTO.getNome() != null) {
            rota.setNome(rotaDTO.getNome());
        }
        if (rotaDTO.getDescricao() != null) {
            rota.setDescricao(rotaDTO.getDescricao());
        }
        if (rotaDTO.getStatus() != null) {
            rota.setStatus(rotaDTO.getStatus());
        }
        if (rotaDTO.getDistanciaTotal() != null) {
            rota.setDistanciaTotal(rotaDTO.getDistanciaTotal().intValue());
        }
        if (rotaDTO.getTempoEstimado() != null) {
            rota.setTempoEstimado(rotaDTO.getTempoEstimado());
        }
        if (rotaDTO.getCapacidadeMaxima() != null) {
            rota.setCapacidadeMaxima(rotaDTO.getCapacidadeMaxima());
        }
        if (rotaDTO.getValorTotalEstimado() != null) {
            rota.setValorTotalEstimado(rotaDTO.getValorTotalEstimado());
        }
        if (rotaDTO.getObservacoes() != null) {
            rota.setObservacoes(rotaDTO.getObservacoes());
        }

        Rota rotaAtualizada = rotaRepository.save(rota);
        log.info("Rota atualizada com sucesso. ID: {}", id);

        return RotaDTO.fromEntity(rotaAtualizada);
    }

    /**
     * MÉTODO PARA EXCLUIR ROTA
     * 
     * CONCEITOS:
     * - @Transactional: Gerencia transação de banco de dados
     * - Validação: Verifica se rota existe
     * 
     * @param id ID da rota
     * @throws RotaNaoEncontradaException Se a rota não existir
     */
    @Transactional
    public void excluirRota(Long id) {
        log.info("Excluindo rota. ID: {}", id);

        if (!rotaRepository.existsById(id)) {
            throw new RotaNaoEncontradaException("Rota não encontrada: " + id);
        }

        rotaRepository.deleteById(id);
        log.info("Rota excluída com sucesso. ID: {}", id);
    }

    /**
     * MÉTODO PARA INICIAR ROTA
     * 
     * CONCEITOS:
     * - Mudança de status da rota
     * - Validação de regras de negócio
     * - Data de início
     * 
     * @param rotaId ID da rota
     * @return RotaDTO da rota iniciada
     * @throws RotaNaoEncontradaException Se a rota não existir
     */
    @Transactional
    public RotaDTO iniciarRota(Long rotaId) {
        log.info("Iniciando rota. Rota ID: {}", rotaId);

        Rota rota = rotaRepository.findById(rotaId)
                .orElseThrow(() -> new RotaNaoEncontradaException("Rota não encontrada: " + rotaId));

        // Iniciar a rota
        rota.iniciarRota();

        Rota rotaIniciada = rotaRepository.save(rota);
        log.info("Rota iniciada com sucesso. Rota ID: {}", rotaId);

        return RotaDTO.fromEntity(rotaIniciada);
    }

    /**
     * MÉTODO PARA FINALIZAR ROTA
     * 
     * CONCEITOS:
     * - Mudança de status da rota
     * - Data de fim
     * - Cálculo de duração
     * 
     * @param rotaId ID da rota
     * @return RotaDTO da rota finalizada
     * @throws RotaNaoEncontradaException Se a rota não existir
     */
    @Transactional
    public RotaDTO finalizarRota(Long rotaId) {
        log.info("Finalizando rota. Rota ID: {}", rotaId);

        Rota rota = rotaRepository.findById(rotaId)
                .orElseThrow(() -> new RotaNaoEncontradaException("Rota não encontrada: " + rotaId));

        // Finalizar a rota
        rota.finalizarRota();

        Rota rotaFinalizada = rotaRepository.save(rota);
        log.info("Rota finalizada com sucesso. Rota ID: {}", rotaId);

        return RotaDTO.fromEntity(rotaFinalizada);
    }

    /**
     * MÉTODO PARA CANCELAR ROTA
     * 
     * CONCEITOS:
     * - Mudança de status da rota
     * - Validação de regras de negócio
     * 
     * @param rotaId ID da rota
     * @return RotaDTO da rota cancelada
     * @throws RotaNaoEncontradaException Se a rota não existir
     */
    @Transactional
    public RotaDTO cancelarRota(Long rotaId) {
        log.info("Cancelando rota. Rota ID: {}", rotaId);

        Rota rota = rotaRepository.findById(rotaId)
                .orElseThrow(() -> new RotaNaoEncontradaException("Rota não encontrada: " + rotaId));

        // Cancelar a rota
        rota.setStatus(Rota.StatusRota.CANCELADA);

        Rota rotaCancelada = rotaRepository.save(rota);
        log.info("Rota cancelada com sucesso. Rota ID: {}", rotaId);

        return RotaDTO.fromEntity(rotaCancelada);
    }

    /**
     * MÉTODO PARA CONTAR ROTAS POR STATUS
     * 
     * CONCEITOS:
     * - Contagem para estatísticas
     * - Método de agregação
     * 
     * @param status Status da rota
     * @return Número de rotas com o status especificado
     */
    @Transactional(readOnly = true)
    public Long contarRotasPorStatus(Rota.StatusRota status) {
        log.debug("Contando rotas com status: {}", status);
        return rotaRepository.countByStatus(status);
    }

    /**
     * MÉTODO PARA BUSCAR ROTAS ATIVAS
     * 
     * CONCEITOS:
     * - Filtro por rotas em execução
     * - Status específicos
     * 
     * @return Lista de RotaDTO ativas
     */
    @Transactional(readOnly = true)
    public List<RotaDTO> buscarRotasAtivas() {
        log.debug("Buscando rotas ativas");

        return rotaRepository.findByStatusIn(List.of(
                Rota.StatusRota.PLANEJADA,
                Rota.StatusRota.EM_EXECUCAO
        )).stream()
                .map(RotaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * MÉTODO PARA BUSCAR ROTAS POR COLETOR E STATUS
     * 
     * CONCEITOS:
     * - Filtro combinado
     * - Múltiplos critérios
     * 
     * @param coletorId ID do coletor
     * @param status Status da rota
     * @return Lista de RotaDTO
     */
    @Transactional(readOnly = true)
    public List<RotaDTO> buscarRotasPorColetorEStatus(Long coletorId, Rota.StatusRota status) {
        log.debug("Buscando rotas do coletor {} com status: {}", coletorId, status);

        return rotaRepository.findByColetorIdAndStatus(coletorId, status).stream()
                .map(RotaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * MÉTODO PARA ACEITAR UMA ROTA (COLETOR)
     * 
     * CONCEITOS:
     * - Aceitar rota muda status para EM_EXECUCAO
     * - Rota fica "bloqueada" para o coletor que aceitou
     * - Outros coletores não podem mais ver essa rota
     * - Registra timestamp de aceite para controle de 10 segundos
     * 
     * @param rotaId ID da rota
     * @param coletorId ID do coletor que está aceitando
     * @return RotaDTO da rota atualizada
     * @throws RotaNaoEncontradaException Se a rota não existir
     * @throws IllegalStateException Se a rota não estiver disponível para aceite
     */
    @Transactional
    public RotaDTO aceitarRota(Long rotaId, Long coletorId) {
        log.info("Aceitando rota. Rota: {}, Coletor: {}", rotaId, coletorId);

        // Buscar rota
        Rota rota = rotaRepository.findById(rotaId)
                .orElseThrow(() -> new RotaNaoEncontradaException("Rota não encontrada: " + rotaId));

        // Verificar se a rota está disponível para aceite
        if (rota.getStatus() != Rota.StatusRota.PLANEJADA) {
            throw new IllegalStateException("Rota não está disponível para aceite. Status atual: " + rota.getStatus());
        }

        // Buscar coletor
        Usuario coletor = usuarioRepository.findById(coletorId)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Coletor não encontrado: " + coletorId));

        // Verificar se o usuário é um coletor
        if (coletor.getTipoUsuario() != Usuario.TipoUsuario.COLETOR) {
            throw new IllegalArgumentException("Usuário não é um coletor: " + coletorId);
        }

        // Aceitar rota
        rota.setColetor(coletor);
        rota.setStatus(Rota.StatusRota.EM_EXECUCAO);
        rota.setDataInicio(LocalDateTime.now());
        rota.setDataAtualizacao(LocalDateTime.now());

        Rota rotaSalva = rotaRepository.save(rota);
        log.info("Rota aceita com sucesso. ID: {}", rotaId);

        return RotaDTO.fromEntity(rotaSalva);
    }

    /**
     * MÉTODO PARA DESISTIR DE UMA ROTA (COLETOR)
     * 
     * CONCEITOS:
     * - Desistir só é permitido dentro de 10 segundos após aceitar
     * - Status volta para PLANEJADA (disponível para outros)
     * - Remove coletor da rota
     * 
     * @param rotaId ID da rota
     * @param coletorId ID do coletor que está desistindo
     * @return RotaDTO da rota atualizada
     * @throws RotaNaoEncontradaException Se a rota não existir
     * @throws IllegalStateException Se o tempo limite foi excedido
     */
    @Transactional
    public RotaDTO desistirRota(Long rotaId, Long coletorId) {
        log.info("Desistindo rota. Rota: {}, Coletor: {}", rotaId, coletorId);

        // Buscar rota
        Rota rota = rotaRepository.findById(rotaId)
                .orElseThrow(() -> new RotaNaoEncontradaException("Rota não encontrada: " + rotaId));

        // Verificar se a rota está em execução pelo coletor
        if (rota.getStatus() != Rota.StatusRota.EM_EXECUCAO) {
            throw new IllegalStateException("Rota não está em execução. Status atual: " + rota.getStatus());
        }

        // Verificar se o coletor é o mesmo que aceitou a rota
        if (!rota.getColetor().getId().equals(coletorId)) {
            throw new IllegalStateException("Apenas o coletor que aceitou a rota pode desistir");
        }

        // Verificar se passaram menos de 10 segundos desde o aceite
        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime dataAceite = rota.getDataInicio();
        
        if (dataAceite != null && agora.isAfter(dataAceite.plusSeconds(10))) {
            throw new IllegalStateException("Tempo limite para desistir excedido (10 segundos)");
        }

        // Desistir rota
        rota.setStatus(Rota.StatusRota.PLANEJADA);
        rota.setColetor(null);
        rota.setDataInicio(null);
        rota.setDataAtualizacao(LocalDateTime.now());

        Rota rotaSalva = rotaRepository.save(rota);
        log.info("Rota desistida com sucesso. ID: {}", rotaId);

        return RotaDTO.fromEntity(rotaSalva);
    }

    /**
     * MÉTODO PARA CONCLUIR UMA ROTA (COLETOR)
     * 
     * CONCEITOS:
     * - Concluir rota muda status para CONCLUIDA
     * - Só o coletor que aceitou pode concluir
     * - Registra data de conclusão
     * 
     * @param rotaId ID da rota
     * @param coletorId ID do coletor que está concluindo
     * @return RotaDTO da rota atualizada
     * @throws RotaNaoEncontradaException Se a rota não existir
     * @throws IllegalStateException Se a operação não for permitida
     */
    @Transactional
    public RotaDTO concluirRota(Long rotaId, Long coletorId) {
        log.info("Concluindo rota. Rota: {}, Coletor: {}", rotaId, coletorId);

        // Buscar rota
        Rota rota = rotaRepository.findById(rotaId)
                .orElseThrow(() -> new RotaNaoEncontradaException("Rota não encontrada: " + rotaId));

        // Verificar se a rota está em execução
        if (rota.getStatus() != Rota.StatusRota.EM_EXECUCAO) {
            throw new IllegalStateException("Rota não está em execução. Status atual: " + rota.getStatus());
        }

        // Verificar se o coletor é o mesmo que aceitou a rota
        if (!rota.getColetor().getId().equals(coletorId)) {
            throw new IllegalStateException("Apenas o coletor que aceitou a rota pode concluí-la");
        }

        // Concluir rota
        rota.setStatus(Rota.StatusRota.FINALIZADA);
        rota.setDataFim(LocalDateTime.now());
        rota.setDataAtualizacao(LocalDateTime.now());

        Rota rotaSalva = rotaRepository.save(rota);
        log.info("Rota concluída com sucesso. ID: {}", rotaId);

        return RotaDTO.fromEntity(rotaSalva);
    }

    /**
     * MÉTODO PARA LISTAR ROTAS DISPONÍVEIS PARA ACEITE
     * 
     * CONCEITOS:
     * - Mostra apenas rotas PLANEJADA (disponíveis para aceite)
     * - Não mostra rotas EM_EXECUCAO de outros coletores
     * 
     * @return Lista de rotas disponíveis
     */
    @Transactional(readOnly = true)
    public List<RotaDTO> listarRotasDisponiveis() {
        log.info("Listando rotas disponíveis para aceite");

        List<Rota> rotasDisponiveis = rotaRepository.findByStatus(Rota.StatusRota.PLANEJADA);
        
        List<RotaDTO> rotasDTO = rotasDisponiveis.stream()
                .map(RotaDTO::fromEntity)
                .collect(Collectors.toList());

        log.info("Rotas disponíveis encontradas: {}", rotasDTO.size());
        return rotasDTO;
    }

    /**
     * MÉTODO PARA LISTAR ROTAS DE UM COLETOR
     * 
     * CONCEITOS:
     * - Mostra rotas PLANEJADA (disponíveis para aceite)
     * - Mostra rotas EM_EXECUCAO do próprio coletor
     * - Não mostra rotas EM_EXECUCAO de outros coletores
     * 
     * @param coletorId ID do coletor
     * @return Lista de rotas do coletor
     */
    @Transactional(readOnly = true)
    public List<RotaDTO> listarMinhasRotas(Long coletorId) {
        log.info("Listando rotas do coletor. Coletor: {}", coletorId);

        // Buscar rotas PLANEJADA (disponíveis para aceite)
        List<Rota> rotasPlanejadas = rotaRepository.findByStatus(Rota.StatusRota.PLANEJADA);
        
        // Buscar rotas EM_EXECUCAO do próprio coletor
        List<Rota> rotasEmExecucao = rotaRepository.findByColetorIdAndStatus(coletorId, Rota.StatusRota.EM_EXECUCAO);
        
        // Combinar as listas
        List<Rota> todasRotas = new ArrayList<>();
        todasRotas.addAll(rotasPlanejadas);
        todasRotas.addAll(rotasEmExecucao);
        
        List<RotaDTO> rotasDTO = todasRotas.stream()
                .map(RotaDTO::fromEntity)
                .collect(Collectors.toList());

        log.info("Rotas do coletor encontradas: {}", rotasDTO.size());
        return rotasDTO;
    }

    /**
     * MÉTODO PARA BUSCAR ROTAS POR REGIÃO
     * 
     * CONCEITOS:
     * - Filtro por coordenadas geográficas
     * - Cálculo de distância usando fórmula de Haversine
     * - Raio de busca configurável
     * 
     * @param latitude Latitude do centro da região
     * @param longitude Longitude do centro da região
     * @param raioKm Raio de busca em quilômetros
     * @return Lista de RotaDTO na região
     */
    @Transactional(readOnly = true)
    public List<RotaDTO> buscarRotasPorRegiao(Double latitude, Double longitude, Double raioKm) {
        log.debug("Buscando rotas na região: lat={}, lng={}, raio={}km", latitude, longitude, raioKm);

        return rotaRepository.findAll().stream()
                .filter(rota -> {
                    // Verificar se a rota tem coordenadas de início
                    if (rota.getLatitudeInicio() == null || rota.getLongitudeInicio() == null) {
                        return false;
                    }
                    
                    double distancia = calcularDistanciaHaversine(
                        latitude, longitude,
                        rota.getLatitudeInicio(), rota.getLongitudeInicio()
                    );
                    
                    return distancia <= raioKm;
                })
                .map(RotaDTO::fromEntity)
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