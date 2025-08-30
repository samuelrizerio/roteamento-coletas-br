package br.com.roteamento.repository;

import br.com.roteamento.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * REPOSITÓRIO USUÁRIO - Interface para acesso a dados de usuários
 * 
 * CONCEITOS DIDÁTICOS EXPLICADOS:
 * 
 * 1. SPRING DATA JPA:
 *    - JpaRepository: Interface que fornece métodos CRUD básicos
 *    - Herança: Estende funcionalidades de CrudRepository e PagingAndSortingRepository
 *    - Implementação automática: Spring gera a implementação automaticamente
 *    - Query Methods: Métodos que geram queries automaticamente
 * 
 * 2. MÉTODOS DE CONSULTA:
 *    - findBy: Busca por campo específico
 *    - findByAnd: Combina múltiplos critérios
 *    - findByOr: Busca com condição OR
 *    - findByOrderBy: Ordena os resultados
 *    - countBy: Conta registros que atendem critérios
 * 
 * 3. ANOTAÇÕES:
 *    - @Repository: Indica que é um componente de repositório
 *    - @Query: Define queries personalizadas
 *    - @Param: Mapeia parâmetros da query
 * 
 * 4. PAGINAÇÃO E ORDENAÇÃO:
 *    - Pageable: Interface para paginação
 *    - Page: Interface que encapsula resultados paginados
 *    - Sort: Interface para ordenação
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * MÉTODO PARA BUSCAR USUÁRIO POR EMAIL
     * 
     * CONCEITOS:
     * - findByEmail: Método que gera query automaticamente
     * - Optional: Wrapper que pode conter valor ou ser vazio
     * - Evita NullPointerException
     * - Útil para login e validação de unicidade
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * MÉTODO PARA VERIFICAR SE EMAIL EXISTE
     * 
     * CONCEITOS:
     * - existsByEmail: Método que gera query de existência
     * - Boolean: Retorna true se existe, false caso contrário
     * - Útil para validação antes de criar usuário
     */
    boolean existsByEmail(String email);

    /**
     * MÉTODO PARA BUSCAR USUÁRIOS POR TIPO
     * 
     * CONCEITOS:
     * - findByTipoUsuario: Método que gera query automaticamente
     * - List: Coleção ordenada de elementos
     * - Útil para filtrar usuários por papel
     */
    List<Usuario> findByTipoUsuario(Usuario.TipoUsuario tipoUsuario);

    /**
     * MÉTODO PARA BUSCAR USUÁRIOS POR STATUS
     * 
     * CONCEITOS:
     * - findByStatus: Método que gera query automaticamente
     * - List: Coleção ordenada de elementos
     * - Útil para filtrar usuários por status
     */
    List<Usuario> findByStatus(Usuario.StatusUsuario status);

    /**
     * MÉTODO PARA BUSCAR USUÁRIOS POR TIPO E STATUS
     * 
     * CONCEITOS:
     * - findByTipoUsuarioAndStatus: Combina múltiplos critérios
     * - List: Coleção ordenada de elementos
     * - Útil para filtrar usuários ativos por tipo
     */
    List<Usuario> findByTipoUsuarioAndStatus(Usuario.TipoUsuario tipoUsuario, Usuario.StatusUsuario status);

    /**
     * MÉTODO PARA BUSCAR USUÁRIOS POR NOME (IGNORANDO MAIÚSCULAS/MINÚSCULAS)
     * 
     * CONCEITOS:
     * - findByNomeContainingIgnoreCase: Busca parcial case-insensitive
     * - List: Coleção ordenada de elementos
     * - Útil para busca por nome
     */
    List<Usuario> findByNomeContainingIgnoreCase(String nome);

    /**
     * MÉTODO PARA BUSCAR USUÁRIOS POR NOME COM PAGINAÇÃO
     * 
     * CONCEITOS:
     * - findByNomeContainingIgnoreCase: Busca parcial case-insensitive
     * - Pageable: Interface para paginação
     * - Page: Interface que encapsula resultados paginados
     * - Útil para listagens grandes
     */
    Page<Usuario> findByNomeContainingIgnoreCase(String nome, Pageable pageable);



    /**
     * MÉTODO PARA BUSCAR USUÁRIOS COM LOCALIZAÇÃO
     * 
     * CONCEITOS:
     * - @Query: Define query personalizada em JPQL
     * - JPQL: Java Persistence Query Language
     * - WHERE: Cláusula de filtro
     * - IS NOT NULL: Verifica se campo não é nulo
     * - Útil para encontrar usuários com coordenadas
     */
    @Query("SELECT u FROM Usuario u WHERE u.latitude IS NOT NULL AND u.longitude IS NOT NULL")
    List<Usuario> findUsuariosComLocalizacao();

    /**
     * MÉTODO PARA BUSCAR COLETORES ATIVOS COM LOCALIZAÇÃO
     * 
     * CONCEITOS:
     * - @Query: Define query personalizada em JPQL
     * - @Param: Mapeia parâmetro da query
     * - AND: Combina múltiplas condições
     * - IS NOT NULL: Verifica se campo não é nulo
     * - Útil para encontrar coletores disponíveis próximos
     */
    @Query("SELECT u FROM Usuario u WHERE u.tipoUsuario = :tipoUsuario AND u.status = :status " +
           "AND u.latitude IS NOT NULL AND u.longitude IS NOT NULL")
    List<Usuario> findColetoresAtivosComLocalizacao(@Param("tipoUsuario") Usuario.TipoUsuario tipoUsuario,
                                                   @Param("status") Usuario.StatusUsuario status);

    /**
     * MÉTODO PARA BUSCAR USUÁRIOS EM RAIO ESPECÍFICO
     * 
     * CONCEITOS:
     * - @Query: Define query personalizada em JPQL
     * - @Param: Mapeia parâmetros da query
     * - Fórmula simplificada: Calcula distância aproximada entre coordenadas
     * - Útil para encontrar usuários próximos
     */
    @Query("SELECT u FROM Usuario u WHERE " +
           "u.latitude BETWEEN :lat - :raio AND :lat + :raio AND " +
           "u.longitude BETWEEN :lng - :raio AND :lng + :raio")
    List<Usuario> findUsuariosEmRaio(@Param("lat") Double latitude,
                                    @Param("lng") Double longitude,
                                    @Param("raio") Double raioEmMilhas);

    /**
     * MÉTODO PARA CONTAR USUÁRIOS POR TIPO
     * 
     * CONCEITOS:
     * - countByTipoUsuario: Método que gera query de contagem
     * - Long: Tipo primitivo para números inteiros grandes
     * - Útil para estatísticas
     */
    Long countByTipoUsuario(Usuario.TipoUsuario tipoUsuario);

    /**
     * MÉTODO PARA CONTAR USUÁRIOS POR STATUS
     * 
     * CONCEITOS:
     * - countByStatus: Método que gera query de contagem
     * - Long: Tipo primitivo para números inteiros grandes
     * - Útil para estatísticas
     */
    Long countByStatus(Usuario.StatusUsuario status);

    /**
     * MÉTODO PARA BUSCAR USUÁRIOS ORDENADOS POR NOME
     * 
     * CONCEITOS:
     * - findAllByOrderByNomeAsc: Método que gera query com ordenação
     * - List: Coleção ordenada de elementos
     * - ASC: Ordenação ascendente
     * - Útil para listagens ordenadas
     */
    List<Usuario> findAllByOrderByNomeAsc();

    /**
     * MÉTODO PARA BUSCAR USUÁRIOS ORDENADOS POR DATA DE CRIAÇÃO
     * 
     * CONCEITOS:
     * - findAllByOrderByDataCriacaoDesc: Método que gera query com ordenação
     * - List: Coleção ordenada de elementos
     * - DESC: Ordenação descendente
     * - Útil para listagens por data
     */
    List<Usuario> findAllByOrderByDataCriacaoDesc();

    /**
     * MÉTODO PARA BUSCAR USUÁRIOS POR EMAIL OU NOME
     * 
     * CONCEITOS:
     * - findByEmailContainingIgnoreCaseOrNomeContainingIgnoreCase: Busca com OR
     * - List: Coleção ordenada de elementos
     * - OR: Condição lógica OR
     * - Útil para busca geral
     */
    List<Usuario> findByEmailContainingIgnoreCaseOrNomeContainingIgnoreCase(String email, String nome);

    /**
     * MÉTODO PARA BUSCAR USUÁRIOS POR TELEFONE
     * 
     * CONCEITOS:
     * - findByTelefone: Método que gera query automaticamente
     * - Optional: Wrapper que pode conter valor ou ser vazio
     * - Útil para busca por telefone
     */
    Optional<Usuario> findByTelefone(String telefone);

    /**
     * MÉTODO PARA VERIFICAR SE TELEFONE EXISTE
     * 
     * CONCEITOS:
     * - existsByTelefone: Método que gera query de existência
     * - Boolean: Retorna true se existe, false caso contrário
     * - Útil para validação antes de criar usuário
     */
    boolean existsByTelefone(String telefone);

    /**
     * MÉTODO PARA BUSCAR USUÁRIOS CRIADOS EM PERÍODO ESPECÍFICO
     * 
     * CONCEITOS:
     * - @Query: Define query personalizada em JPQL
     * - @Param: Mapeia parâmetros da query
     * - BETWEEN: Cláusula para intervalo de datas
     * - List: Coleção ordenada de elementos
     * - Útil para relatórios por período
     */
    @Query("SELECT u FROM Usuario u WHERE u.dataCriacao BETWEEN :dataInicio AND :dataFim")
    List<Usuario> findUsuariosCriadosEntre(@Param("dataInicio") java.time.LocalDateTime dataInicio,
                                          @Param("dataFim") java.time.LocalDateTime dataFim);


} 