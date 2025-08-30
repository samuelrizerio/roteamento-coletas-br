package br.com.roteamento.service;

import br.com.roteamento.dto.UsuarioDTO;
import br.com.roteamento.model.Usuario;
import br.com.roteamento.repository.UsuarioRepository;
import br.com.roteamento.exception.UsuarioNaoEncontradoException;
import br.com.roteamento.exception.EmailJaExisteException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * SERVIÇO USUÁRIO - Classe que contém a lógica de negócio para usuários
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
 *    - @Autowired: Injeta dependências automaticamente
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
@Slf4j
public class UsuarioService {

    /**
     * REPOSITÓRIO DE USUÁRIOS
     * 
     * CONCEITOS:
     * - final: Campo imutável após inicialização
     * - Injeção via construtor (recomendado)
     * - Acesso aos dados de usuários
     */
    private final UsuarioRepository usuarioRepository;

    /**
     * ENCODER DE SENHAS
     * 
     * CONCEITOS:
     * - PasswordEncoder: Interface para criptografia de senhas
     * - BCrypt: Algoritmo de hash seguro
     * - Segurança: Senhas nunca são armazenadas em texto puro
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * MÉTODO PARA CRIAR UM NOVO USUÁRIO
     * 
     * CONCEITOS:
     * - @Transactional: Gerencia transação de banco de dados
     * - Validação: Verifica se email já existe
     * - Criptografia: Senha é criptografada antes de salvar
     * - Logging: Registra operações importantes
     * 
     * @param usuarioDTO Dados do usuário a ser criado
     * @return UsuarioDTO do usuário criado
     * @throws EmailJaExisteException Se o email já estiver em uso
     */
    @Transactional
    public UsuarioDTO criarUsuario(UsuarioDTO usuarioDTO) {
        log.info("Criando novo usuário: {}", usuarioDTO.getEmail());

        // Verifica se o email já existe
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            log.warn("Tentativa de criar usuário com email já existente: {}", usuarioDTO.getEmail());
            throw new EmailJaExisteException("Email já está em uso: " + usuarioDTO.getEmail());
        }

        // Converte DTO para entidade
        Usuario usuario = usuarioDTO.toEntity();

        // Criptografa a senha
        if (usuario.getSenha() != null) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        // Salva o usuário
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        log.info("Usuário criado com sucesso. ID: {}", usuarioSalvo.getId());

        // Retorna DTO do usuário criado
        return UsuarioDTO.fromEntity(usuarioSalvo);
    }

    /**
     * MÉTODO PARA BUSCAR USUÁRIO POR ID
     * 
     * CONCEITOS:
     * - Optional: Wrapper que pode conter valor ou ser vazio
     * - orElseThrow: Lança exceção se valor não estiver presente
     * - Exceção customizada: UsuarioNaoEncontradoException
     * 
     * @param id ID do usuário
     * @return UsuarioDTO do usuário encontrado
     * @throws UsuarioNaoEncontradoException Se o usuário não for encontrado
     */
    @Transactional(readOnly = true)
    public UsuarioDTO buscarUsuarioPorId(Long id) {
        log.debug("Buscando usuário por ID: {}", id);

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado com ID: " + id));

        return UsuarioDTO.fromEntity(usuario);
    }

    /**
     * MÉTODO PARA BUSCAR USUÁRIO POR EMAIL
     * 
     * CONCEITOS:
     * - @Transactional(readOnly = true): Otimização para consultas
     * - Optional: Tratamento seguro de valores nulos
     * - Logging: Registra operações de consulta
     * 
     * @param email Email do usuário
     * @return Optional<UsuarioDTO> contendo o usuário se encontrado
     */
    @Transactional(readOnly = true)
    public Optional<UsuarioDTO> buscarUsuarioPorEmail(String email) {
        log.debug("Buscando usuário por email: {}", email);

        return usuarioRepository.findByEmail(email)
                .map(UsuarioDTO::fromEntity);
    }

    /**
     * MÉTODO PARA LISTAR TODOS OS USUÁRIOS
     * 
     * CONCEITOS:
     * - Stream API: Manipulação funcional de coleções
     * - map: Transforma cada elemento da lista
     * - collect: Coleta resultados em nova lista
     * - @Transactional(readOnly = true): Otimização para consultas
     * 
     * @return Lista de UsuarioDTO
     */
    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarTodosUsuarios() {
        log.debug("Listando todos os usuários");

        return usuarioRepository.findAll().stream()
                .map(UsuarioDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * MÉTODO PARA BUSCAR USUÁRIOS POR TIPO
     * 
     * CONCEITOS:
     * - Stream API: Manipulação funcional de coleções
     * - map: Transforma cada elemento da lista
     * - collect: Coleta resultados em nova lista
     * - Filtro por tipo de usuário
     * 
     * @param tipoUsuario Tipo de usuário para filtrar
     * @return Lista de UsuarioDTO filtrada por tipo
     */
    @Transactional(readOnly = true)
    public List<UsuarioDTO> buscarUsuariosPorTipo(Usuario.TipoUsuario tipoUsuario) {
        log.debug("Buscando usuários por tipo: {}", tipoUsuario);

        return usuarioRepository.findByTipoUsuario(tipoUsuario).stream()
                .map(UsuarioDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * MÉTODO PARA BUSCAR COLETORES ATIVOS
     * 
     * CONCEITOS:
     * - Reutilização de código: Usa método existente
     * - Encapsulamento: Lógica de negócio centralizada
     * - Filtro específico para coletores ativos
     * 
     * @return Lista de UsuarioDTO de coletores ativos
     */
    @Transactional(readOnly = true)
    public List<UsuarioDTO> buscarColetoresAtivos() {
        log.debug("Buscando coletores ativos");

        return usuarioRepository.findByTipoUsuarioAndStatus(
                Usuario.TipoUsuario.COLETOR, 
                Usuario.StatusUsuario.ATIVO
        ).stream()
                .map(UsuarioDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * MÉTODO PARA ATUALIZAR USUÁRIO
     * 
     * CONCEITOS:
     * - @Transactional: Gerencia transação de banco de dados
     * - Validação: Verifica se usuário existe
     * - Criptografia: Senha é criptografada se fornecida
     * - Merge: Atualiza dados existentes
     * 
     * @param id ID do usuário a ser atualizado
     * @param usuarioDTO Novos dados do usuário
     * @return UsuarioDTO do usuário atualizado
     * @throws UsuarioNaoEncontradoException Se o usuário não for encontrado
     */
    @Transactional
    public UsuarioDTO atualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        log.info("Atualizando usuário com ID: {}", id);

        // Busca o usuário existente
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado com ID: " + id));

        // Atualiza os campos
        usuarioExistente.setNome(usuarioDTO.getNome());
        usuarioExistente.setTelefone(usuarioDTO.getTelefone());
        usuarioExistente.setEndereco(usuarioDTO.getEndereco());
        usuarioExistente.setLatitude(usuarioDTO.getLatitude());
        usuarioExistente.setLongitude(usuarioDTO.getLongitude());
        usuarioExistente.setStatus(usuarioDTO.getStatus());

        // Atualiza email se fornecido e diferente do atual
        if (usuarioDTO.getEmail() != null && !usuarioDTO.getEmail().equals(usuarioExistente.getEmail())) {
            if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
                throw new EmailJaExisteException("Email já está em uso: " + usuarioDTO.getEmail());
            }
            usuarioExistente.setEmail(usuarioDTO.getEmail());
        }

        // Atualiza senha se fornecida
        if (usuarioDTO.getSenha() != null && !usuarioDTO.getSenha().isEmpty()) {
            usuarioExistente.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        }

        // Salva as alterações
        Usuario usuarioAtualizado = usuarioRepository.save(usuarioExistente);
        log.info("Usuário atualizado com sucesso. ID: {}", usuarioAtualizado.getId());

        return UsuarioDTO.fromEntity(usuarioAtualizado);
    }

    /**
     * MÉTODO PARA EXCLUIR USUÁRIO
     * 
     * CONCEITOS:
     * - @Transactional: Gerencia transação de banco de dados
     * - Validação: Verifica se usuário existe
     * - Soft delete: Marca como inativo em vez de excluir
     * - Logging: Registra operações de exclusão
     * 
     * @param id ID do usuário a ser excluído
     * @throws UsuarioNaoEncontradoException Se o usuário não for encontrado
     */
    @Transactional
    public void excluirUsuario(Long id) {
        log.info("Excluindo usuário com ID: {}", id);

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado com ID: " + id));

        // Soft delete: marca como inativo
        usuario.setStatus(Usuario.StatusUsuario.INATIVO);
        usuarioRepository.save(usuario);

        log.info("Usuário marcado como inativo. ID: {}", id);
    }

    /**
     * MÉTODO PARA ATIVAR USUÁRIO
     * 
     * CONCEITOS:
     * - @Transactional: Gerencia transação de banco de dados
     * - Validação: Verifica se usuário existe
     * - Mudança de status: Ativa usuário inativo
     * 
     * @param id ID do usuário a ser ativado
     * @return UsuarioDTO do usuário ativado
     * @throws UsuarioNaoEncontradoException Se o usuário não for encontrado
     */
    @Transactional
    public UsuarioDTO ativarUsuario(Long id) {
        log.info("Ativando usuário com ID: {}", id);

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado com ID: " + id));

        usuario.setStatus(Usuario.StatusUsuario.ATIVO);
        Usuario usuarioAtivado = usuarioRepository.save(usuario);

        log.info("Usuário ativado com sucesso. ID: {}", id);
        return UsuarioDTO.fromEntity(usuarioAtivado);
    }

    /**
     * MÉTODO PARA BUSCAR USUÁRIOS POR NOME
     * 
     * CONCEITOS:
     * - Stream API: Manipulação funcional de coleções
     * - map: Transforma cada elemento da lista
     * - collect: Coleta resultados em nova lista
     * - Busca case-insensitive
     * 
     * @param nome Nome ou parte do nome para buscar
     * @return Lista de UsuarioDTO que contêm o nome
     */
    @Transactional(readOnly = true)
    public List<UsuarioDTO> buscarUsuariosPorNome(String nome) {
        log.debug("Buscando usuários por nome: {}", nome);

        return usuarioRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(UsuarioDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * MÉTODO PARA VERIFICAR SE EMAIL EXISTE
     * 
     * CONCEITOS:
     * - Método utilitário para validação
     * - Boolean: Tipo de retorno para valores true/false
     * - Reutilização: Pode ser usado em diferentes contextos
     * 
     * @param email Email a ser verificado
     * @return true se o email existe, false caso contrário
     */
    @Transactional(readOnly = true)
    public boolean emailExiste(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    /**
     * MÉTODO PARA CONTAR USUÁRIOS POR TIPO
     * 
     * CONCEITOS:
     * - Método utilitário para estatísticas
     * - Long: Tipo primitivo para números inteiros grandes
     * - Reutilização: Pode ser usado em relatórios
     * 
     * @param tipoUsuario Tipo de usuário para contar
     * @return Número de usuários do tipo especificado
     */
    @Transactional(readOnly = true)
    public Long contarUsuariosPorTipo(Usuario.TipoUsuario tipoUsuario) {
        return usuarioRepository.countByTipoUsuario(tipoUsuario);
    }
} 