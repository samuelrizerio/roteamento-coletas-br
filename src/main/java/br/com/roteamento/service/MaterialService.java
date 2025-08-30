package br.com.roteamento.service;

import br.com.roteamento.dto.MaterialDTO;
import br.com.roteamento.exception.MaterialNaoEncontradoException;
import br.com.roteamento.exception.NomeMaterialJaExisteException;
import br.com.roteamento.model.Material;
import br.com.roteamento.model.Material.CategoriaMaterial;
import br.com.roteamento.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Serviço para gerenciamento de materiais
 * 
 * CONCEITO DIDÁTICO - Camada de Serviço:
 * - Contém a lógica de negócio da aplicação
 * - Orquestra operações entre repositórios
 * - Implementa regras de validação e negócio
 * - Gerencia transações e rollbacks
 * - Trata exceções de negócio
 * 
 * CONCEITO DIDÁTICO - Transações:
 * - @Transactional: define escopo de transação
 * - REQUIRED: usa transação existente ou cria nova
 * - REQUIRES_NEW: sempre cria nova transação
 * - READ_ONLY: otimiza para operações de leitura
 * - Rollback automático em caso de exceção
 */
@Service
@RequiredArgsConstructor
public class MaterialService {

    private static final Logger log = LoggerFactory.getLogger(MaterialService.class);
    private final MaterialRepository materialRepository;

    /**
     * CONCEITO DIDÁTICO - Método de Criação:
     * Cria um novo material com validações de negócio
     * 
     * @param materialDTO dados do material
     * @return material criado
     * @throws NomeMaterialJaExisteException se nome já existe
     */
    @Transactional
    public MaterialDTO criarMaterial(MaterialDTO materialDTO) {
        log.info("Criando material: {}", materialDTO.getNome());

        // CONCEITO DIDÁTICO - Validação de Negócio:
        // Verifica se já existe material com o mesmo nome
        if (materialRepository.existsByNomeIgnoreCase(materialDTO.getNome())) {
            throw new NomeMaterialJaExisteException("Material com nome '" + materialDTO.getNome() + "' já existe");
        }

        // CONCEITO DIDÁTICO - Conversão DTO para Entidade:
        Material material = materialDTO.toEntity();
        material.setDataCriacao(LocalDateTime.now());
        material.setDataAtualizacao(LocalDateTime.now());

        Material materialSalvo = materialRepository.save(material);
        log.info("Material criado com ID: {}", materialSalvo.getId());

        return MaterialDTO.fromEntity(materialSalvo);
    }

    /**
     * CONCEITO DIDÁTICO - Método de Busca por ID:
     * Busca material por ID com tratamento de exceção
     * 
     * @param id ID do material
     * @return material encontrado
     * @throws MaterialNaoEncontradoException se não encontrado
     */
    @Transactional(readOnly = true)
    public MaterialDTO buscarPorId(Long id) {
        log.debug("Buscando material por ID: {}", id);

        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new MaterialNaoEncontradoException("Material com ID " + id + " não encontrado"));

        return MaterialDTO.fromEntity(material);
    }

    /**
     * CONCEITO DIDÁTICO - Método de Listagem:
     * Lista todos os materiais ativos
     * 
     * @return lista de materiais ativos
     */
    @Transactional(readOnly = true)
    public List<MaterialDTO> listarMateriaisAtivos() {
        log.debug("Listando materiais ativos");

        return materialRepository.findByAceitoParaColetaOrderByNome(true)
                .stream()
                .map(MaterialDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * CONCEITO DIDÁTICO - Método com Paginação:
     * Lista materiais com paginação
     * 
     * @param ativo status de ativação
     * @param pageable configuração de paginação
     * @return página de materiais
     */
    @Transactional(readOnly = true)
    public Page<MaterialDTO> listarMateriaisPaginados(Boolean aceitoParaColeta, Pageable pageable) {
        log.debug("Listando materiais paginados, aceitoParaColeta: {}", aceitoParaColeta);

        return materialRepository.findByAceitoParaColeta(aceitoParaColeta, pageable)
                .map(MaterialDTO::fromEntity);
    }

    /**
     * CONCEITO DIDÁTICO - Método de Busca por Categoria:
     * Busca materiais por categoria
     * 
     * @param categoria categoria do material
     * @return lista de materiais da categoria
     */
    @Transactional(readOnly = true)
    public List<MaterialDTO> buscarPorCategoria(CategoriaMaterial categoria) {
        log.debug("Buscando materiais por categoria: {}", categoria);

        return materialRepository.findByCategoriaAndAceitoParaColeta(categoria, true)
                .stream()
                .map(MaterialDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * CONCEITO DIDÁTICO - Método de Busca por Nome:
     * Busca materiais por nome (busca parcial)
     * 
     * @param nome parte do nome do material
     * @return lista de materiais que contêm o nome
     */
    @Transactional(readOnly = true)
    public List<MaterialDTO> buscarPorNome(String nome) {
        log.debug("Buscando materiais por nome: {}", nome);

        return materialRepository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(MaterialDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * CONCEITO DIDÁTICO - Método de Busca por Faixa de Preço:
     * Busca materiais por faixa de preço
     * 
     * @param precoMin preço mínimo
     * @param precoMax preço máximo
     * @return lista de materiais na faixa de preço
     */
    @Transactional(readOnly = true)
    public List<MaterialDTO> buscarPorFaixaPreco(BigDecimal precoMin, BigDecimal precoMax) {
        log.debug("Buscando materiais por faixa de preço: {} - {}", precoMin, precoMax);

        return materialRepository.findByValorPorQuiloBetween(precoMin, precoMax)
                .stream()
                .map(MaterialDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * CONCEITO DIDÁTICO - Método de Atualização:
     * Atualiza material existente
     * 
     * @param id ID do material
     * @param materialDTO dados para atualização
     * @return material atualizado
     * @throws MaterialNaoEncontradoException se não encontrado
     */
    @Transactional
    public MaterialDTO atualizarMaterial(Long id, MaterialDTO materialDTO) {
        log.info("Atualizando material com ID: {}", id);

        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new MaterialNaoEncontradoException("Material com ID " + id + " não encontrado"));

        // CONCEITO DIDÁTICO - Validação de Negócio:
        // Verifica se o novo nome já existe em outro material
        if (materialDTO.getNome() != null && 
            !materialDTO.getNome().equalsIgnoreCase(material.getNome()) &&
            materialRepository.existsByNomeIgnoreCase(materialDTO.getNome())) {
            throw new NomeMaterialJaExisteException("Material com nome '" + materialDTO.getNome() + "' já existe");
        }

        materialDTO.updateEntity(material);
        material.setDataAtualizacao(LocalDateTime.now());

        Material materialAtualizado = materialRepository.save(material);
        log.info("Material atualizado com ID: {}", materialAtualizado.getId());

        return MaterialDTO.fromEntity(materialAtualizado);
    }

    /**
     * CONCEITO DIDÁTICO - Método de Ativação/Desativação:
     * Ativa ou desativa material
     * 
     * @param id ID do material
     * @param ativo status de ativação
     * @return material atualizado
     * @throws MaterialNaoEncontradoException se não encontrado
     */
    @Transactional
    public MaterialDTO alterarStatus(Long id, Boolean aceitoParaColeta) {
        log.info("Alterando status do material com ID: {} para: {}", id, aceitoParaColeta);

        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new MaterialNaoEncontradoException("Material com ID " + id + " não encontrado"));

        material.setAtivo(aceitoParaColeta);
        material.setDataAtualizacao(LocalDateTime.now());

        Material materialAtualizado = materialRepository.save(material);
        log.info("Status do material alterado com ID: {}", materialAtualizado.getId());

        return MaterialDTO.fromEntity(materialAtualizado);
    }

    /**
     * CONCEITO DIDÁTICO - Método de Exclusão Lógica:
     * Desativa material (exclusão lógica)
     * 
     * @param id ID do material
     * @throws MaterialNaoEncontradoException se não encontrado
     */
    @Transactional
    public void excluirMaterial(Long id) {
        log.info("Excluindo material com ID: {}", id);

        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new MaterialNaoEncontradoException("Material com ID " + id + " não encontrado"));

        material.setAtivo(false);
        material.setDataAtualizacao(LocalDateTime.now());

        materialRepository.save(material);
        log.info("Material excluído com ID: {}", id);
    }

    /**
     * CONCEITO DIDÁTICO - Método de Estatísticas:
     * Calcula estatísticas dos materiais
     * 
     * @return estatísticas dos materiais
     */
    @Transactional(readOnly = true)
    public MaterialEstatisticasDTO calcularEstatisticas() {
        log.debug("Calculando estatísticas dos materiais");

        List<Material> materiais = materialRepository.findByAceitoParaColeta(Boolean.TRUE, org.springframework.data.domain.Pageable.unpaged()).getContent();
        
        BigDecimal precoMedio = materiais.stream()
                .map(Material::getPrecoPorKg)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(materiais.size()), 2, BigDecimal.ROUND_HALF_UP);

        long totalMateriais = materiais.size();
        long materiaisPorCategoria = materiais.stream()
                .map(Material::getCategoria)
                .distinct()
                .count();

        return MaterialEstatisticasDTO.builder()
                .totalMateriais(totalMateriais)
                .categoriasDiferentes(materiaisPorCategoria)
                .precoMedio(precoMedio)
                .build();
    }

    /**
     * CONCEITO DIDÁTICO - DTO Interno para Estatísticas:
     * DTO específico para retornar estatísticas
     */
    public static class MaterialEstatisticasDTO {
        private long totalMateriais;
        private long categoriasDiferentes;
        private BigDecimal precoMedio;

        // Getters, setters e builder
        public long getTotalMateriais() { return totalMateriais; }
        public void setTotalMateriais(long totalMateriais) { this.totalMateriais = totalMateriais; }
        
        public long getCategoriasDiferentes() { return categoriasDiferentes; }
        public void setCategoriasDiferentes(long categoriasDiferentes) { this.categoriasDiferentes = categoriasDiferentes; }
        
        public BigDecimal getPrecoMedio() { return precoMedio; }
        public void setPrecoMedio(BigDecimal precoMedio) { this.precoMedio = precoMedio; }

        public static MaterialEstatisticasDTOBuilder builder() {
            return new MaterialEstatisticasDTOBuilder();
        }

        public static class MaterialEstatisticasDTOBuilder {
            private long totalMateriais;
            private long categoriasDiferentes;
            private BigDecimal precoMedio;

            public MaterialEstatisticasDTOBuilder totalMateriais(long totalMateriais) {
                this.totalMateriais = totalMateriais;
                return this;
            }

            public MaterialEstatisticasDTOBuilder categoriasDiferentes(long categoriasDiferentes) {
                this.categoriasDiferentes = categoriasDiferentes;
                return this;
            }

            public MaterialEstatisticasDTOBuilder precoMedio(BigDecimal precoMedio) {
                this.precoMedio = precoMedio;
                return this;
            }

            public MaterialEstatisticasDTO build() {
                MaterialEstatisticasDTO dto = new MaterialEstatisticasDTO();
                dto.setTotalMateriais(totalMateriais);
                dto.setCategoriasDiferentes(categoriasDiferentes);
                dto.setPrecoMedio(precoMedio);
                return dto;
            }
        }
    }
} 