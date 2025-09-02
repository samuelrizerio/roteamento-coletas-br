package br.com.roteamento.repository;

import br.com.roteamento.model.Material;
import br.com.roteamento.model.Material.CategoriaMaterial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repositório para entidade Material
 * 
 * CONCEITO DIDÁTICO - Spring Data JPA:
 * - JpaRepository estende CrudRepository e PagingAndSortingRepository
 * - Fornece métodos CRUD básicos automaticamente
 * - Permite consultas personalizadas com convenções de nomenclatura
 * - Suporta paginação e ordenação
 * - Integra com transações do Spring
 * 
 * CONCEITO DIDÁTICO - Query Methods:
 * - findBy[Property]: busca por propriedade específica
 * - findBy[Property]And[Property]: busca com múltiplos critérios
 * - findBy[Property]OrderBy[Property]: busca com ordenação
 * - findBy[Property]Between: busca com range de valores
 * - findBy[Property]In: busca com lista de valores
 * - findBy[Property]Like: busca com pattern matching
 * 
 * CONCEITO DIDÁTICO - JPQL (Java Persistence Query Language):
 * - Linguagem de consulta orientada a objetos
 * - Sintaxe similar ao SQL mas trabalha com entidades
 * - Suporta parâmetros nomeados com @Param
 * - Permite consultas complexas e agregações
 */
@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {

    /**
     * CONCEITO DIDÁTICO - Query Method por Convenção:
     * Busca materiais por categoria usando convenção de nomenclatura
     * 
     * @param categoria categoria do material
     * @return lista de materiais da categoria
     */
    List<Material> findByCategoria(CategoriaMaterial categoria);

    /**
     * CONCEITO DIDÁTICO - Query Method com Múltiplos Critérios:
     * Busca materiais ativos por categoria
     * 
     * @param categoria categoria do material
     * @param aceitoParaColeta status de aceitação para coleta
     * @return lista de materiais filtrados
     */
    List<Material> findByCategoriaAndAceitoParaColeta(CategoriaMaterial categoria, Boolean aceitoParaColeta);

    /**
     * CONCEITO DIDÁTICO - Query Method com Ordenação:
     * Busca materiais ordenados por nome
     * 
     * @param aceitoParaColeta status de aceitação para coleta
     * @return lista ordenada de materiais
     */
    List<Material> findByAceitoParaColetaOrderByNome(Boolean aceitoParaColeta);

    /**
     * CONCEITO DIDÁTICO - Query Method com Range:
     * Busca materiais por faixa de preço
     * 
     * @param precoMin preço mínimo
     * @param precoMax preço máximo
     * @return lista de materiais na faixa de preço
     */
    List<Material> findByValorPorQuiloBetween(BigDecimal precoMin, BigDecimal precoMax);

    /**
     * CONCEITO DIDÁTICO - Query Method com Pattern Matching:
     * Busca materiais por nome contendo texto
     * 
     * @param nome parte do nome do material
     * @return lista de materiais que contêm o nome
     */
    List<Material> findByNomeContainingIgnoreCase(String nome);

    /**
     * CONCEITO DIDÁTICO - Query Method com Paginação:
     * Busca materiais com paginação
     * 
     * @param aceitoParaColeta status de aceitação para coleta
     * @param pageable configuração de paginação
     * @return página de materiais
     */
    Page<Material> findByAceitoParaColeta(Boolean aceitoParaColeta, Pageable pageable);

    /**
     * CONCEITO DIDÁTICO - Query Method com Lista:
     * Busca materiais por lista de categorias
     * 
     * @param categorias lista de categorias
     * @return lista de materiais das categorias especificadas
     */
    List<Material> findByCategoriaIn(List<CategoriaMaterial> categorias);

    /**
     * CONCEITO DIDÁTICO - Query Method com Exists:
     * Verifica se existe material com nome específico
     * 
     * @param nome nome do material
     * @return true se existe material com o nome
     */
    boolean existsByNomeIgnoreCase(String nome);

    /**
     * CONCEITO DIDÁTICO - Query Method com Count:
     * Conta materiais por categoria
     * 
     * @param categoria categoria do material
     * @return quantidade de materiais da categoria
     */
    long countByCategoria(CategoriaMaterial categoria);

    /**
     * CONCEITO DIDÁTICO - JPQL Query Personalizada:
     * Busca materiais com preço acima da média
     * 
     * @return lista de materiais com preço acima da média
     */
    @Query("SELECT m FROM Material m WHERE m.valorPorQuilo > (SELECT AVG(m2.valorPorQuilo) FROM Material m2)")
    List<Material> findMateriaisComPrecoAcimaDaMedia();

    /**
     * CONCEITO DIDÁTICO - JPQL Query com Parâmetros:
     * Busca materiais por categoria e preço mínimo
     * 
     * @param categoria categoria do material
     * @param precoMin preço mínimo
     * @return lista de materiais filtrados
     */
    @Query("SELECT m FROM Material m WHERE m.categoria = :categoria AND m.valorPorQuilo >= :precoMin")
    List<Material> findMateriaisPorCategoriaEPrecoMinimo(
            @Param("categoria") CategoriaMaterial categoria,
            @Param("precoMin") BigDecimal precoMin
    );

    /**
     * CONCEITO DIDÁTICO - JPQL Query com Agregação:
     * Calcula o preço médio por categoria
     * 
     * @param categoria categoria do material
     * @return preço médio da categoria
     */
    @Query("SELECT AVG(m.valorPorQuilo) FROM Material m WHERE m.categoria = :categoria")
    Optional<BigDecimal> calcularPrecoMedioPorCategoria(@Param("categoria") CategoriaMaterial categoria);

    /**
     * CONCEITO DIDÁTICO - JPQL Query com Projeção:
     * Busca apenas nome e preço dos materiais
     * 
     * @return lista de arrays com nome e preço
     */
    @Query("SELECT m.nome, m.valorPorQuilo FROM Material m WHERE m.aceitoParaColeta = true")
    List<Object[]> findNomesEPrecosMateriaisAtivos();

    /**
     * CONCEITO DIDÁTICO - Query Method com Distinct:
     * Busca categorias únicas de materiais ativos
     * 
     * @return lista de categorias únicas
     */
    @Query("SELECT DISTINCT m.categoria FROM Material m WHERE m.aceitoParaColeta = true")
    List<CategoriaMaterial> findCategoriasUnicasAtivas();

    /**
     * CONCEITO DIDÁTICO - Query Method com Case:
     * Busca materiais com classificação de preço
     * 
     * @return lista de materiais com classificação
     */
    @Query("SELECT m, " +
           "CASE " +
           "  WHEN m.valorPorQuilo < 1.0 THEN 'Baixo' " +
           "  WHEN m.valorPorQuilo < 5.0 THEN 'Médio' " +
           "  ELSE 'Alto' " +
           "END as classificacao " +
           "FROM Material m WHERE m.aceitoParaColeta = true")
    List<Object[]> findMateriaisComClassificacaoPreco();

    /**
     * Busca materiais ativos
     */
    List<Material> findByAceitoParaColetaTrue();

    /**
     * Busca materiais inativos
     */
    List<Material> findByAceitoParaColetaFalse();
} 