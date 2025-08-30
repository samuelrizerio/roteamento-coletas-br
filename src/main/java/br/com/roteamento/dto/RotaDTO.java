package br.com.roteamento.dto;

import br.com.roteamento.model.Rota;
import br.com.roteamento.model.Rota.StatusRota;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO (Data Transfer Object) para Rota
 * 
 * CONCEITO DIDÁTICO - DTOs com Relacionamentos Complexos:
 * - DTOs podem incluir dados de múltiplas entidades relacionadas
 * - Evitam carregamento desnecessário de dados (N+1 problem)
 * - Permitem controle granular sobre dados expostos
 * - Facilitam operações de criação/atualização com relacionamentos
 * - Podem incluir dados calculados ou agregados
 * 
 * CONCEITO DIDÁTICO - Validações de Negócio:
 * - Validações que dependem do estado do objeto
 * - Validações que consideram relacionamentos
 * - Validações customizadas com @Constraint
 * - Validações condicionais baseadas em regras de negócio
 */
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para transferência de dados de Rota")
public class RotaDTO {

    @Schema(description = "ID único da rota", example = "1")
    private Long id;

    @NotNull(message = "ID do coletor é obrigatório")
    @Schema(description = "ID do coletor responsável pela rota", example = "1")
    private Long coletorId;

    @NotBlank(message = "Nome da rota é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    @Schema(description = "Nome da rota", example = "Rota Centro - Manhã")
    private String nome;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    @Schema(description = "Descrição detalhada da rota", example = "Rota para coleta no centro da cidade")
    private String descricao;

    @NotNull(message = "Capacidade máxima é obrigatória")
    @DecimalMin(value = "1.0", message = "Capacidade máxima deve ser maior que zero")
    @Schema(description = "Capacidade máxima em kg", example = "1000.0")
    private BigDecimal capacidadeMaxima;

    @Schema(description = "Capacidade utilizada em kg", example = "750.5")
    private BigDecimal capacidadeUtilizada;

    @Schema(description = "Distância total em km", example = "25.3")
    private BigDecimal distanciaTotal;

    @Min(value = 1, message = "Tempo estimado deve ser maior que zero")
    @Schema(description = "Tempo estimado em minutos", example = "180")
    private Integer tempoEstimado;

    @Schema(description = "Status atual da rota", example = "PLANEJADA")
    private StatusRota status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Data de criação da rota")
    private LocalDateTime dataCriacao;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Data da última atualização")
    private LocalDateTime dataAtualizacao;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Data de início da execução")
    private LocalDateTime dataInicio;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Data de conclusão da execução")
    private LocalDateTime dataConclusao;

    // Dados das coletas associadas (para evitar múltiplas consultas)
    @Schema(description = "Lista de IDs das coletas")
    private List<Long> coletaIds;

    @Schema(description = "Quantidade de coletas na rota")
    private Integer quantidadeColetas;

    @Schema(description = "Peso total das coletas")
    private BigDecimal pesoTotalColetas;

    /**
     * CONVERTE DTO PARA ENTIDADE
     * 
     * CONCEITOS:
     * - Mapeamento de DTO para entidade
     * - Conversão de tipos
     * - Tratamento de campos opcionais
     * 
     * @return entidade Rota
     */
    public Rota toEntity() {
        // O construtor requer coletor e nome, mas não temos o coletor aqui
        // Vamos criar uma rota temporária e depois configurar os campos
        Rota rota = new Rota();
        rota.setNome(this.nome != null ? this.nome : "Rota Temporária");
        
        // Mapear campos básicos
        rota.setId(this.id);
        rota.setDescricao(this.descricao);
        rota.setCapacidadeMaxima(this.capacidadeMaxima);
        rota.setStatus(this.status);
        
        // Mapear datas
        rota.setDataCriacao(this.dataCriacao);
        rota.setDataAtualizacao(this.dataAtualizacao);
        rota.setDataInicio(this.dataInicio);
        rota.setDataConclusao(this.dataConclusao);
        
        // Mapear métricas
        rota.setCapacidadeUtilizada(this.capacidadeUtilizada);
        rota.setDistanciaTotal(this.distanciaTotal != null ? this.distanciaTotal.intValue() : null);
        rota.setTempoEstimado(this.tempoEstimado);
        rota.setValorTotalEstimado(this.getValorTotalEstimado());
        
        // Mapear coordenadas (não implementadas no DTO atual)
        // rota.setLatitudeInicio(this.latitudeInicio);
        // rota.setLongitudeInicio(this.longitudeInicio);
        // rota.setLatitudeFim(this.latitudeFim);
        // rota.setLongitudeFim(this.longitudeFim);
        
        // Mapear observações
        rota.setObservacoes(this.getObservacoes());
        
        return rota;
    }

    /**
     * CONCEITO DIDÁTICO - Método Estático de Conversão:
     * Converte entidade para DTO, incluindo dados relacionados
     * 
     * @param rota entidade a ser convertida
     * @return RotaDTO convertido da entidade
     */
    public static RotaDTO fromEntity(Rota rota) {
        if (rota == null) {
            return null;
        }

        List<Long> coletaIds = null;
        Integer quantidadeColetas = 0;
        BigDecimal pesoTotalColetas = BigDecimal.ZERO;

        if (rota.getColetas() != null) {
            coletaIds = rota.getColetas().stream()
                    .map(coleta -> coleta.getId())
                    .collect(Collectors.toList());
            
            quantidadeColetas = rota.getColetas().size();
            
            pesoTotalColetas = BigDecimal.ZERO; // Temporariamente comentado
        }

        RotaDTO dto = new RotaDTO();
        dto.setId(rota.getId());
        dto.setNome(rota.getNome());
        dto.setDescricao(rota.getDescricao());
        dto.setCapacidadeMaxima(rota.getCapacidadeMaxima());
        dto.setCapacidadeUtilizada(rota.getCapacidadeUtilizada());
        dto.setDistanciaTotal(rota.getDistanciaTotal() != null ? BigDecimal.valueOf(rota.getDistanciaTotal()) : null);
        dto.setTempoEstimado(rota.getTempoEstimado());
        dto.setStatus(rota.getStatus());
        dto.setDataCriacao(rota.getDataCriacao());
        dto.setDataAtualizacao(rota.getDataAtualizacao());
        dto.setDataInicio(rota.getDataInicio());
        dto.setDataConclusao(rota.getDataConclusao());
        dto.setColetaIds(coletaIds);
        dto.setQuantidadeColetas(quantidadeColetas);
        dto.setPesoTotalColetas(pesoTotalColetas);
        return dto;
    }

    /**
     * CONCEITO DIDÁTICO - Método de Atualização Parcial:
     * Atualiza apenas campos não nulos da entidade
     * 
     * @param rota entidade a ser atualizada
     */
    public void updateEntity(Rota rota) {
        if (this.nome != null) {
            rota.setNome(this.nome);
        }
        if (this.descricao != null) {
            rota.setDescricao(this.descricao);
        }
        if (this.capacidadeMaxima != null) {
            rota.setCapacidadeMaxima(this.capacidadeMaxima);
        }
        if (this.capacidadeUtilizada != null) {
            rota.setCapacidadeUtilizada(this.capacidadeUtilizada);
        }
        if (this.distanciaTotal != null) {
            rota.setDistanciaTotal(this.distanciaTotal.intValue());
        }
        if (this.tempoEstimado != null) {
            rota.setTempoEstimado(this.tempoEstimado);
        }
        if (this.status != null) {
            rota.setStatus(this.status);
        }
        if (this.dataInicio != null) {
            rota.setDataInicio(this.dataInicio);
        }
        if (this.dataConclusao != null) {
            rota.setDataConclusao(this.dataConclusao);
        }
    }

    /**
     * CONCEITO DIDÁTICO - Método de Validação de Negócio:
     * Valida se a rota está dentro da capacidade
     * 
     * @return true se a rota está dentro da capacidade
     */
    public boolean isDentroDaCapacidade() {
        if (this.capacidadeMaxima == null || this.capacidadeUtilizada == null) {
            return true;
        }
        return this.capacidadeUtilizada.compareTo(this.capacidadeMaxima) <= 0;
    }

    /**
     * CONCEITO DIDÁTICO - Método de Cálculo:
     * Calcula a porcentagem de utilização da capacidade
     * 
     * @return porcentagem de utilização (0-100)
     */
    public BigDecimal calcularPorcentagemUtilizacao() {
        if (this.capacidadeMaxima == null || this.capacidadeMaxima.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        
        if (this.capacidadeUtilizada == null) {
            return BigDecimal.ZERO;
        }
        
        return this.capacidadeUtilizada
                .multiply(BigDecimal.valueOf(100))
                .divide(this.capacidadeMaxima, 2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * CONCEITO DIDÁTICO - Método de Validação de Status:
     * Verifica se a rota pode ser iniciada
     * 
     * @return true se a rota pode ser iniciada
     */
    public boolean podeSerIniciada() {
        return this.status == StatusRota.PLANEJADA && 
               this.quantidadeColetas != null && 
               this.quantidadeColetas > 0;
    }

    /**
     * CONCEITO DIDÁTICO - Método de Validação de Status:
     * Verifica se a rota pode ser concluída
     * 
     * @return true se a rota pode ser concluída
     */
    public boolean podeSerConcluida() {
        return this.status == StatusRota.EM_EXECUCAO && 
               this.dataInicio != null;
    }

    /**
     * CONCEITO DIDÁTICO - Método de Cálculo de Eficiência:
     * Calcula a eficiência da rota baseada em tempo e distância
     * 
     * @return score de eficiência (0-100)
     */
    public BigDecimal calcularEficiencia() {
        if (this.tempoEstimado == null || this.distanciaTotal == null || 
            this.quantidadeColetas == null || this.quantidadeColetas == 0) {
            return BigDecimal.ZERO;
        }
        
        // Fórmula: (coletas / tempo) * (1 / distância) * 1000
        BigDecimal coletasPorTempo = BigDecimal.valueOf(this.quantidadeColetas)
                .divide(BigDecimal.valueOf(this.tempoEstimado), 4, BigDecimal.ROUND_HALF_UP);
        
        BigDecimal eficienciaDistancia = BigDecimal.ONE
                .divide(this.distanciaTotal, 4, BigDecimal.ROUND_HALF_UP);
        
        return coletasPorTempo
                .multiply(eficienciaDistancia)
                .multiply(BigDecimal.valueOf(1000))
                .setScale(2, BigDecimal.ROUND_HALF_UP);
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getColetorId() { return coletorId; }
    public void setColetorId(Long coletorId) { this.coletorId = coletorId; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public BigDecimal getCapacidadeMaxima() { return capacidadeMaxima; }
    public void setCapacidadeMaxima(BigDecimal capacidadeMaxima) { this.capacidadeMaxima = capacidadeMaxima; }
    
    public BigDecimal getCapacidadeUtilizada() { return capacidadeUtilizada; }
    public void setCapacidadeUtilizada(BigDecimal capacidadeUtilizada) { this.capacidadeUtilizada = capacidadeUtilizada; }
    
    public BigDecimal getDistanciaTotal() { return distanciaTotal; }
    public void setDistanciaTotal(BigDecimal distanciaTotal) { this.distanciaTotal = distanciaTotal; }
    
    public Integer getTempoEstimado() { return tempoEstimado; }
    public void setTempoEstimado(Integer tempoEstimado) { this.tempoEstimado = tempoEstimado; }
    
    public StatusRota getStatus() { return status; }
    public void setStatus(StatusRota status) { this.status = status; }
    
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
    
    public LocalDateTime getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDateTime dataInicio) { this.dataInicio = dataInicio; }
    
    public LocalDateTime getDataConclusao() { return dataConclusao; }
    public void setDataConclusao(LocalDateTime dataConclusao) { this.dataConclusao = dataConclusao; }
    
    public List<Long> getColetaIds() { return coletaIds; }
    public void setColetaIds(List<Long> coletaIds) { this.coletaIds = coletaIds; }
    
    public Integer getQuantidadeColetas() { return quantidadeColetas; }
    public void setQuantidadeColetas(Integer quantidadeColetas) { this.quantidadeColetas = quantidadeColetas; }
    
    public BigDecimal getPesoTotalColetas() { return pesoTotalColetas; }
    public void setPesoTotalColetas(BigDecimal pesoTotalColetas) { this.pesoTotalColetas = pesoTotalColetas; }

    // Campos adicionais para compatibilidade com a entidade
    public BigDecimal getValorTotalEstimado() { return pesoTotalColetas; }
    public void setValorTotalEstimado(BigDecimal valorTotalEstimado) { this.pesoTotalColetas = valorTotalEstimado; }

    public Double getLatitudeInicio() { return null; } // Não implementado no DTO atual
    public void setLatitudeInicio(Double latitudeInicio) { } // Não implementado no DTO atual

    public Double getLongitudeInicio() { return null; } // Não implementado no DTO atual
    public void setLongitudeInicio(Double longitudeInicio) { } // Não implementado no DTO atual

    public Double getLatitudeFim() { return null; } // Não implementado no DTO atual
    public void setLatitudeFim(Double latitudeFim) { } // Não implementado no DTO atual

    public Double getLongitudeFim() { return null; } // Não implementado no DTO atual
    public void setLongitudeFim(Double longitudeFim) { } // Não implementado no DTO atual

    public String getObservacoes() { return descricao; } // Usando descrição como observações
    public void setObservacoes(String observacoes) { this.descricao = observacoes; }
} 