package br.com.roteamento.dto;

import br.com.roteamento.model.Material;
import br.com.roteamento.model.Material.CategoriaMaterial;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) para Material
 * 
 * CONCEITO DIDÁTICO - DTO (Data Transfer Object):
 * - DTOs são objetos que transportam dados entre camadas da aplicação
 * - Separam a representação externa (API) da representação interna (entidade)
 * - Permitem controle sobre quais dados são expostos na API
 * - Facilitam validações específicas para entrada de dados
 * - Evitam exposição desnecessária de dados sensíveis
 * 
 * CONCEITO DIDÁTICO - Validação com Bean Validation:
 * - @NotNull: campo obrigatório
 * - @NotBlank: string não pode ser vazia ou apenas espaços
 * - @Size: define tamanho mínimo e máximo
 * - @DecimalMin/@DecimalMax: valores numéricos mínimos e máximos
 * - @Pattern: validação com expressão regular
 * - @Email: validação de formato de email
 */
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para transferência de dados de Material")
public class MaterialDTO {

    @Schema(description = "ID único do material", example = "1")
    private Long id;

    @NotBlank(message = "Nome do material é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    @Schema(description = "Nome do material", example = "Papelão")
    private String nome;

    @NotBlank(message = "Descrição do material é obrigatória")
    @Size(min = 10, max = 500, message = "Descrição deve ter entre 10 e 500 caracteres")
    @Schema(description = "Descrição detalhada do material", example = "Papelão ondulado usado em embalagens")
    private String descricao;

    @NotNull(message = "Categoria do material é obrigatória")
    @Schema(description = "Categoria do material", example = "PAPEL")
    private CategoriaMaterial categoria;

    @NotNull(message = "Peso unitário é obrigatório")
    @DecimalMin(value = "0.01", message = "Peso unitário deve ser maior que zero")
    @DecimalMax(value = "1000.0", message = "Peso unitário não pode exceder 1000kg")
    @Schema(description = "Peso unitário em kg", example = "0.5")
    private BigDecimal pesoUnitario;

    @NotNull(message = "Preço por kg é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço por kg deve ser maior que zero")
    @Schema(description = "Preço por kg em reais", example = "0.80")
    private BigDecimal precoPorKg;

    @Schema(description = "Indica se o material está ativo", example = "true")
    private Boolean ativo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Data de criação do material")
    private LocalDateTime dataCriacao;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Data da última atualização do material")
    private LocalDateTime dataAtualizacao;

    /**
     * CONCEITO DIDÁTICO - Método de Conversão:
     * Converte DTO para entidade (usado para criar/atualizar)
     * 
     * @return Material convertido do DTO
     */
    public Material toEntity() {
        Material material = new Material(this.nome, this.categoria, this.precoPorKg);
        material.setId(this.id);
        material.setDescricao(this.descricao);
        material.setAtivo(this.ativo != null ? this.ativo : true);
        return material;
    }

    /**
     * CONCEITO DIDÁTICO - Método Estático de Conversão:
     * Converte entidade para DTO (usado para retornar dados)
     * 
     * @param material entidade a ser convertida
     * @return MaterialDTO convertido da entidade
     */
    public static MaterialDTO fromEntity(Material material) {
        if (material == null) {
            return null;
        }

        MaterialDTO dto = new MaterialDTO();
        dto.setId(material.getId());
        dto.setNome(material.getNome());
        dto.setDescricao(material.getDescricao());
        dto.setCategoria(material.getCategoria());
        dto.setPrecoPorKg(material.getPrecoPorKg());
        dto.setAtivo(material.getAtivo());
        dto.setDataCriacao(material.getDataCriacao());
        dto.setDataAtualizacao(material.getDataAtualizacao());
        return dto;
    }

    /**
     * CONCEITO DIDÁTICO - Método de Atualização:
     * Atualiza uma entidade existente com dados do DTO
     * 
     * @param material entidade a ser atualizada
     */
    public void updateEntity(Material material) {
        if (this.nome != null) {
            material.setNome(this.nome);
        }
        if (this.descricao != null) {
            material.setDescricao(this.descricao);
        }
        if (this.categoria != null) {
            material.setCategoria(this.categoria);
        }
        if (this.precoPorKg != null) {
            material.setPrecoPorKg(this.precoPorKg);
        }
        if (this.ativo != null) {
            material.setAtivo(this.ativo);
        }
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public CategoriaMaterial getCategoria() { return categoria; }
    public void setCategoria(CategoriaMaterial categoria) { this.categoria = categoria; }
    
    public BigDecimal getPesoUnitario() { return pesoUnitario; }
    public void setPesoUnitario(BigDecimal pesoUnitario) { this.pesoUnitario = pesoUnitario; }
    
    public BigDecimal getPrecoPorKg() { return precoPorKg; }
    public void setPrecoPorKg(BigDecimal precoPorKg) { this.precoPorKg = precoPorKg; }
    
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
} 