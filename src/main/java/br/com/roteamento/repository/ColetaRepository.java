package br.com.roteamento.repository;

import br.com.roteamento.model.Coleta;
import br.com.roteamento.model.Coleta.StatusColeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositório para entidade Coleta
 * 
 * CONCEITO DIDÁTICO - Consultas Espaciais:
 * - Consultas baseadas em coordenadas geográficas
 * - Cálculo de distâncias entre pontos
 * - Busca por proximidade geográfica
 * - Otimização de rotas baseada em localização
 * 
 * CONCEITO DIDÁTICO - Relacionamentos em Consultas:
 * - JOIN entre entidades relacionadas
 * - Fetch de dados relacionados
 * - Consultas com múltiplas entidades
 * - Otimização de performance com fetch strategies
 */
@Repository
public interface ColetaRepository extends JpaRepository<Coleta, Long> {

    /**
     * CONCEITO DIDÁTICO - Query Method por Status:
     * Busca coletas por status
     * 
     * @param status status da coleta
     * @return lista de coletas com o status
     */
    List<Coleta> findByStatus(StatusColeta status);

    /**
     * CONCEITO DIDÁTICO - Query Method com Relacionamento:
     * Busca coletas por usuário
     * 
     * @param usuarioId ID do usuário
     * @return lista de coletas do usuário
     */
    List<Coleta> findByUsuarioId(Long usuarioId);

    /**
     * CONCEITO DIDÁTICO - Query Method com Múltiplos Critérios:
     * Busca coletas por usuário e status
     * 
     * @param usuarioId ID do usuário
     * @param status status da coleta
     * @return lista de coletas filtradas
     */
    List<Coleta> findByUsuarioIdAndStatus(Long usuarioId, StatusColeta status);

    /**
     * CONCEITO DIDÁTICO - Query Method com Range de Datas:
     * Busca coletas em período específico
     * 
     * @param dataInicio data de início
     * @param dataFim data de fim
     * @return lista de coletas no período
     */
    List<Coleta> findByDataCriacaoBetween(LocalDateTime dataInicio, LocalDateTime dataFim);

    /**
     * CONCEITO DIDÁTICO - Query Method com Range de Quantidade:
     * Busca coletas por faixa de quantidade
     * 
     * @param quantidadeMin quantidade mínima
     * @param quantidadeMax quantidade máxima
     * @return lista de coletas na faixa de quantidade
     */
    List<Coleta> findByQuantidadeBetween(BigDecimal quantidadeMin, BigDecimal quantidadeMax);

    /**
     * CONCEITO DIDÁTICO - Query Method com Ordenação:
     * Busca coletas ordenadas por data de criação
     * 
     * @param status status da coleta
     * @return lista ordenada de coletas
     */
    List<Coleta> findByStatusOrderByDataCriacaoDesc(StatusColeta status);

    /**
     * CONCEITO DIDÁTICO - Query Method com Paginação:
     * Busca coletas com paginação
     * 
     * @param status status da coleta
     * @param pageable configuração de paginação
     * @return página de coletas
     */
    Page<Coleta> findByStatus(StatusColeta status, Pageable pageable);

    /**
     * CONCEITO DIDÁTICO - Query Customizada com JOIN FETCH:
     * Busca todas as coletas carregando relacionamentos (material, usuario, coletor)
     * 
     * @return lista de coletas com relacionamentos carregados
     */
    @Query("SELECT DISTINCT c FROM Coleta c " +
           "LEFT JOIN FETCH c.material " +
           "LEFT JOIN FETCH c.usuario " +
           "LEFT JOIN FETCH c.coletor")
    List<Coleta> findAllWithRelacionamentos();

    /**
     * CONCEITO DIDÁTICO - Query Method com Exists:
     * Verifica se usuário tem coletas pendentes
     * 
     * @param usuarioId ID do usuário
     * @return true se tem coletas pendentes
     */
    boolean existsByUsuarioIdAndStatus(Long usuarioId, StatusColeta status);

    /**
     * CONCEITO DIDÁTICO - Query Method com Count:
     * Conta coletas por status
     * 
     * @param status status da coleta
     * @return quantidade de coletas com o status
     */
    long countByStatus(StatusColeta status);

    /**
     * CONCEITO DIDÁTICO - Query Method com Lista de Status:
     * Busca coletas por múltiplos status
     * 
     * @param statuses lista de status
     * @return lista de coletas com os status especificados
     */
    List<Coleta> findByStatusIn(List<StatusColeta> statuses);

    /**
     * CONCEITO DIDÁTICO - JPQL Query com JOIN:
     * Busca coletas com dados do usuário
     * 
     * @param status status da coleta
     * @return lista de coletas com dados do usuário
     */
    @Query("SELECT c, u.nome, u.email FROM Coleta c JOIN c.usuario u WHERE c.status = :status")
    List<Object[]> findColetasComDadosUsuario(@Param("status") StatusColeta status);

    /**
     * CONCEITO DIDÁTICO - JPQL Query com Agregação:
     * Calcula quantidade total por status
     * 
     * @param status status da coleta
     * @return quantidade total das coletas com o status
     */
    @Query("SELECT SUM(c.quantidade) FROM Coleta c WHERE c.status = :status")
    BigDecimal calcularQuantidadeTotalPorStatus(@Param("status") StatusColeta status);

    /**
     * CONCEITO DIDÁTICO - JPQL Query com Subconsulta:
     * Busca coletas com quantidade acima da média
     * 
     * @return lista de coletas com quantidade acima da média
     */
    @Query("SELECT c FROM Coleta c WHERE c.quantidade > (SELECT AVG(c2.quantidade) FROM Coleta c2)")
    List<Coleta> findColetasComQuantidadeAcimaDaMedia();

    /**
     * CONCEITO DIDÁTICO - JPQL Query com Funções de Data:
     * Busca coletas criadas hoje
     * 
     * @return lista de coletas criadas hoje
     */
    @Query("SELECT c FROM Coleta c WHERE c.dataCriacao >= :inicioHoje AND c.dataCriacao < :inicioAmanha")
    List<Coleta> findColetasCriadasHoje(@Param("inicioHoje") LocalDateTime inicioHoje, @Param("inicioAmanha") LocalDateTime inicioAmanha);

    /**
     * CONCEITO DIDÁTICO - JPQL Query com Case:
     * Busca coletas com classificação de quantidade
     * 
     * @return lista de coletas com classificação
     */
    @Query("SELECT c, " +
           "CASE " +
           "  WHEN c.quantidade < 10.0 THEN 'Pequena' " +
           "  WHEN c.quantidade < 50.0 THEN 'Média' " +
           "  ELSE 'Grande' " +
           "END as classificacao " +
           "FROM Coleta c WHERE c.status = 'SOLICITADA'")
    List<Object[]> findColetasComClassificacaoQuantidade();

    /**
     * CONCEITO DIDÁTICO - JPQL Query com Projeção:
     * Busca apenas dados essenciais das coletas
     * 
     * @param status status da coleta
     * @return lista de arrays com dados essenciais
     */
    @Query("SELECT c.id, c.endereco, c.quantidade, c.dataCriacao FROM Coleta c WHERE c.status = :status")
    List<Object[]> findDadosEssenciaisColetas(@Param("status") StatusColeta status);

    /**
     * CONCEITO DIDÁTICO - JPQL Query com Group By:
     * Agrupa coletas por status e conta
     * 
     * @return lista de arrays com status e contagem
     */
    @Query("SELECT c.status, COUNT(c) FROM Coleta c GROUP BY c.status")
    List<Object[]> contarColetasPorStatus();

    /**
     * CONCEITO DIDÁTICO - JPQL Query com Having:
     * Busca status com mais de 5 coletas
     * 
     * @return lista de status com muitas coletas
     */
    @Query("SELECT c.status, COUNT(c) FROM Coleta c GROUP BY c.status HAVING COUNT(c) > 5")
    List<Object[]> findStatusComMuitasColetas();

    /**
     * CONCEITO DIDÁTICO - JPQL Query com Order By:
     * Busca coletas ordenadas por múltiplos critérios
     * 
     * @param status status da coleta
     * @return lista ordenada de coletas
     */
    @Query("SELECT c FROM Coleta c WHERE c.status = :status ORDER BY c.quantidade DESC, c.dataCriacao ASC")
    List<Coleta> findColetasOrdenadasPorQuantidadeEData(@Param("status") StatusColeta status);

    /**
     * CONCEITO DIDÁTICO - JPQL Query com Distinct:
     * Busca endereços únicos de coletas
     * 
     * @return lista de endereços únicos
     */
    @Query("SELECT DISTINCT c.endereco FROM Coleta c WHERE c.status = 'SOLICITADA'")
    List<String> findEnderecosUnicosSolicitadas();

    /**
     * CONCEITO DIDÁTICO - JPQL Query com Coalesce:
     * Busca coletas com data de aceitação ou coleta
     * 
     * @return lista de coletas com data
     */
    @Query("SELECT c FROM Coleta c WHERE COALESCE(c.dataAceitacao, c.dataColeta) IS NOT NULL")
    List<Coleta> findColetasComDataDefinida();
} 