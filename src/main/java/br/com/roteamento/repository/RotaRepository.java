package br.com.roteamento.repository;

import br.com.roteamento.model.Rota;
import br.com.roteamento.model.Rota.StatusRota;
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
 * Repositório para entidade Rota
 * 
 * CONCEITO DIDÁTICO - Consultas com Relacionamentos:
 * - JOIN entre entidades relacionadas
 * - Fetch de dados relacionados com @EntityGraph
 * - Consultas com múltiplas entidades
 * - Otimização de performance com fetch strategies
 * - Evitar N+1 problem com consultas otimizadas
 * 
 * CONCEITO DIDÁTICO - Consultas de Agregação:
 * - SUM, COUNT, AVG, MIN, MAX em consultas
 * - GROUP BY para agrupamento de resultados
 * - HAVING para filtros em agregações
 * - Subconsultas para cálculos complexos
 */
@Repository
public interface RotaRepository extends JpaRepository<Rota, Long> {

    /**
     * CONCEITO DIDÁTICO - Query Method por Status:
     * Busca rotas por status
     * 
     * @param status status da rota
     * @return lista de rotas com o status
     */
    List<Rota> findByStatus(StatusRota status);

    /**
     * CONCEITO DIDÁTICO - Query Method com Ordenação:
     * Busca rotas ordenadas por data de criação
     * 
     * @param status status da rota
     * @return lista ordenada de rotas
     */
    List<Rota> findByStatusOrderByDataCriacaoDesc(StatusRota status);

    /**
     * CONCEITO DIDÁTICO - Query Method com Paginação:
     * Busca rotas com paginação
     * 
     * @param status status da rota
     * @param pageable configuração de paginação
     * @return página de rotas
     */
    Page<Rota> findByStatus(StatusRota status, Pageable pageable);

    /**
     * CONCEITO DIDÁTICO - Query Method com Range de Capacidade:
     * Busca rotas por faixa de capacidade atual
     * 
     * @param capacidadeMin capacidade mínima
     * @param capacidadeMax capacidade máxima
     * @return lista de rotas na faixa de capacidade
     */
    List<Rota> findByCapacidadeAtualBetween(BigDecimal capacidadeMin, BigDecimal capacidadeMax);

    /**
     * CONCEITO DIDÁTICO - Query Method com Range de Distância:
     * Busca rotas por faixa de distância
     * 
     * @param distanciaMin distância mínima
     * @param distanciaMax distância máxima
     * @return lista de rotas na faixa de distância
     */
    List<Rota> findByDistanciaTotalBetween(BigDecimal distanciaMin, BigDecimal distanciaMax);

    /**
     * CONCEITO DIDÁTICO - Query Method com Range de Tempo:
     * Busca rotas por faixa de tempo estimado
     * 
     * @param tempoMin tempo mínimo
     * @param tempoMax tempo máximo
     * @return lista de rotas na faixa de tempo
     */
    List<Rota> findByTempoEstimadoBetween(Integer tempoMin, Integer tempoMax);

    /**
     * CONCEITO DIDÁTICO - Query Method com Range de Datas:
     * Busca rotas criadas em período específico
     * 
     * @param dataInicio data de início
     * @param dataFim data de fim
     * @return lista de rotas no período
     */
    List<Rota> findByDataCriacaoBetween(LocalDateTime dataInicio, LocalDateTime dataFim);

    /**
     * CONCEITO DIDÁTICO - Query Method com Exists:
     * Verifica se existem rotas em execução
     * 
     * @return true se existem rotas em execução
     */
    boolean existsByStatus(StatusRota status);

    /**
     * CONCEITO DIDÁTICO - Query Method com Count:
     * Conta rotas por status
     * 
     * @param status status da rota
     * @return quantidade de rotas com o status
     */
    long countByStatus(Rota.StatusRota status);

    /**
     * CONCEITO DIDÁTICO - Query Method com Relacionamento:
     * Busca rotas por coletor
     * 
     * @param coletorId ID do coletor
     * @return lista de rotas do coletor
     */
    List<Rota> findByColetorId(Long coletorId);

    /**
     * CONCEITO DIDÁTICO - Query Method com Lista de Status:
     * Busca rotas por múltiplos status
     * 
     * @param statuses lista de status
     * @return lista de rotas com os status especificados
     */
    List<Rota> findByStatusIn(List<StatusRota> statuses);

    /**
     * CONCEITO DIDÁTICO - Query Method com Múltiplos Critérios:
     * Busca rotas por coletor e status
     * 
     * @param coletorId ID do coletor
     * @param status status da rota
     * @return lista de rotas filtradas
     */
    List<Rota> findByColetorIdAndStatus(Long coletorId, StatusRota status);

    /**
     * CONCEITO DIDÁTICO - JPQL Query com JOIN:
     * Busca rotas com dados das coletas
     * 
     * @param status status da rota
     * @return lista de rotas com dados das coletas
     */
    @Query("SELECT r, COUNT(cr) as totalColetas FROM Rota r LEFT JOIN r.coletasRota cr WHERE r.status = :status GROUP BY r")
    List<Object[]> findRotasComContagemColetas(@Param("status") StatusRota status);

    /**
     * CONCEITO DIDÁTICO - JPQL Query com Agregação:
     * Calcula estatísticas das rotas
     * 
     * @return estatísticas das rotas
     */
    @Query("SELECT " +
           "COUNT(r) as totalRotas, " +
           "AVG(r.distanciaTotal) as distanciaMedia, " +
           "AVG(r.tempoEstimado) as tempoMedio, " +
           "SUM(r.capacidadeAtual) as capacidadeTotal " +
           "FROM Rota r WHERE r.status = 'FINALIZADA'")
    Object[] calcularEstatisticasRotas();

    /**
     * CONCEITO DIDÁTICO - JPQL Query com Subconsulta:
     * Busca rotas com maior eficiência
     * 
     * @return lista de rotas mais eficientes
     */
    @Query("SELECT r FROM Rota r WHERE SIZE(r.coletasRota) > (SELECT AVG(SIZE(r2.coletasRota)) FROM Rota r2)")
    List<Rota> findRotasComAltaEficiencia();

    /**
     * CONCEITO DIDÁTICO - JPQL Query com Funções de Data:
     * Busca rotas criadas hoje
     * 
     * @return lista de rotas criadas hoje
     */
    @Query("SELECT r FROM Rota r WHERE r.dataCriacao >= :inicioHoje AND r.dataCriacao < :inicioAmanha")
    List<Rota> findRotasCriadasHoje(@Param("inicioHoje") LocalDateTime inicioHoje, @Param("inicioAmanha") LocalDateTime inicioAmanha);

    /**
     * CONCEITO DIDÁTICO - JPQL Query com Case:
     * Busca rotas com classificação de eficiência
     * 
     * @return lista de rotas com classificação
     */
    @Query("SELECT r, " +
           "CASE " +
           "  WHEN r.tempoEstimado < 120 THEN 'Rápida' " +
           "  WHEN r.tempoEstimado < 240 THEN 'Média' " +
           "  ELSE 'Lenta' " +
           "END as classificacao " +
           "FROM Rota r WHERE r.status = 'CONCLUIDA'")
    List<Object[]> findRotasComClassificacaoTempo();

    /**
     * CONCEITO DIDÁTICO - JPQL Query com Projeção:
     * Busca apenas dados essenciais das rotas
     * 
     * @param status status da rota
     * @return lista de arrays com dados essenciais
     */
    @Query("SELECT r.id, r.nome, r.distanciaTotal, r.tempoEstimado FROM Rota r WHERE r.status = :status")
    List<Object[]> findDadosEssenciaisRotas(@Param("status") StatusRota status);

    /**
     * CONCEITO DIDÁTICO - JPQL Query com Group By:
     * Agrupa rotas por status e conta
     * 
     * @return lista de arrays com status e contagem
     */
    @Query("SELECT r.status, COUNT(r) FROM Rota r GROUP BY r.status")
    List<Object[]> contarRotasPorStatus();

    /**
     * CONCEITO DIDÁTICO - JPQL Query com Having:
     * Busca status com mais de 3 rotas
     * 
     * @return lista de status com muitas rotas
     */
    @Query("SELECT r.status, COUNT(r) FROM Rota r GROUP BY r.status HAVING COUNT(r) > 3")
    List<Object[]> findStatusComMuitasRotas();

    /**
     * CONCEITO DIDÁTICO - JPQL Query com Order By:
     * Busca rotas ordenadas por múltiplos critérios
     * 
     * @param status status da rota
     * @return lista ordenada de rotas
     */
    @Query("SELECT r FROM Rota r WHERE r.status = :status ORDER BY r.distanciaTotal ASC, r.tempoEstimado ASC")
    List<Rota> findRotasOrdenadasPorEficiencia(@Param("status") StatusRota status);

    /**
     * CONCEITO DIDÁTICO - JPQL Query com Distinct:
     * Busca nomes únicos de rotas
     * 
     * @return lista de nomes únicos
     */
    @Query("SELECT DISTINCT r.nome FROM Rota r WHERE r.status = 'PLANEJADA'")
    List<String> findNomesUnicosRotasPlanejadas();

    /**
     * CONCEITO DIDÁTICO - JPQL Query com Coalesce:
     * Busca rotas com data de início ou criação
     * 
     * @return lista de rotas com data
     */
    @Query("SELECT r FROM Rota r WHERE COALESCE(r.dataInicio, r.dataCriacao) IS NOT NULL")
    List<Rota> findRotasComDataDefinida();

    /**
     * CONCEITO DIDÁTICO - JPQL Query com Relacionamento:
     * Busca rotas com coletas de um usuário específico
     * 
     * @param usuarioId ID do usuário
     * @return lista de rotas com coletas do usuário
     */
    @Query("SELECT DISTINCT r FROM Rota r JOIN r.coletasRota cr JOIN cr.coleta c WHERE c.usuario.id = :usuarioId")
    List<Rota> findRotasPorUsuario(@Param("usuarioId") Long usuarioId);

    /**
     * CONCEITO DIDÁTICO - JPQL Query com Agregação Complexa:
     * Calcula média de coletas por rota por status
     * 
     * @return lista de arrays com status e média de coletas
     */
    @Query("SELECT r.status, AVG(SIZE(r.coletasRota)) FROM Rota r GROUP BY r.status")
    List<Object[]> calcularMediaColetasPorStatus();

    /**
     * CONCEITO DIDÁTICO - JPQL Query com Subconsulta Correlacionada:
     * Busca rotas com capacidade utilizada acima da média
     * 
     * @return lista de rotas com alta utilização
     */
    @Query("SELECT r FROM Rota r WHERE r.capacidadeAtual > (SELECT AVG(r2.capacidadeAtual) FROM Rota r2)")
    List<Rota> findRotasComAltaUtilizacao();

    /**
     * CONCEITO DIDÁTICO - Query Method com Count e Relacionamento:
     * Conta rotas por coletor e status
     * 
     * @param coletorId ID do coletor
     * @param status status da rota
     * @return quantidade de rotas do coletor com o status
     */
    long countByColetorIdAndStatus(Long coletorId, Rota.StatusRota status);
} 