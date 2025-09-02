package br.com.roteamento.dto;

import br.com.roteamento.model.Coleta;
import br.com.roteamento.model.Coleta.StatusColeta;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;

import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO (Data Transfer Object) para Coleta
 * 
 * CONCEITO DIDÁTICO - DTOs com Relacionamentos:
 * - DTOs podem incluir dados de entidades relacionadas
 * - Evitam carregamento desnecessário de dados (lazy loading)
 * - Permitem controle granular sobre dados expostos
 * - Facilitam operações de criação/atualização com relacionamentos
 * 
 * CONCEITO DIDÁTICO - Validações Complexas:
 * - @Valid: valida objetos aninhados
 * - @Validated: habilita validação em grupos específicos
 * - Validações customizadas com @Constraint
 * - Validações condicionais baseadas em estado
 */
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para transferência de dados de Coleta")
public class ColetaDTO {

    @Schema(description = "ID único da coleta", example = "1")
    private Long id;

    @NotNull(message = "ID do usuário solicitante é obrigatório")
    @Schema(description = "ID do usuário que solicitou a coleta", example = "1")
    private Long usuarioId;

    @NotBlank(message = "Endereço de coleta é obrigatório")
    @Size(min = 10, max = 200, message = "Endereço deve ter entre 10 e 200 caracteres")
    @Schema(description = "Endereço completo para coleta", example = "Rua das Flores, 123 - Centro, Belo Horizonte - MG")
    private String endereco;

    @NotNull(message = "Latitude é obrigatória")
    @DecimalMin(value = "-90.0", message = "Latitude deve estar entre -90 e 90")
    @DecimalMax(value = "90.0", message = "Latitude deve estar entre -90 e 90")
    @Schema(description = "Latitude da localização", example = "-19.9167")
    private BigDecimal latitude;

    @NotNull(message = "Longitude é obrigatória")
    @DecimalMin(value = "-180.0", message = "Longitude deve estar entre -180 e 180")
    @DecimalMax(value = "180.0", message = "Longitude deve estar entre -180 e 180")
    @Schema(description = "Longitude da localização", example = "-43.9345")
    private BigDecimal longitude;

    @NotNull(message = "Peso estimado é obrigatório")
    @DecimalMin(value = "0.1", message = "Peso estimado deve ser maior que zero")
    @DecimalMax(value = "1000.0", message = "Peso estimado não pode exceder 1000kg")
    @Schema(description = "Peso estimado em kg", example = "25.5")
    private BigDecimal pesoEstimado;

    @NotBlank(message = "Descrição dos materiais é obrigatória")
    @Size(min = 10, max = 500, message = "Descrição deve ter entre 10 e 500 caracteres")
    @Schema(description = "Descrição detalhada dos materiais", example = "Papelão, plástico PET e latas de alumínio")
    private String descricaoMateriais;

    @Schema(description = "Observações adicionais", example = "Material separado por tipo")
    private String observacoes;

    @Schema(description = "Status atual da coleta", example = "PENDENTE")
    private StatusColeta status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Data e hora preferencial para coleta")
    private LocalDateTime dataHoraPreferencial;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Data e hora agendada para coleta")
    private LocalDateTime dataHoraAgendada;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Data e hora de criação da solicitação")
    private LocalDateTime dataCriacao;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Data e hora da última atualização")
    private LocalDateTime dataAtualizacao;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Data e hora de conclusão da coleta")
    private LocalDateTime dataConclusao;

    // Dados do usuário (para evitar múltiplas consultas)
    @Schema(description = "Nome do usuário solicitante")
    private String nomeUsuario;

    @Schema(description = "Email do usuário solicitante")
    private String emailUsuario;

    // Lista de materiais específicos (opcional)
    @Schema(description = "Lista de IDs dos materiais")
    private List<Long> materialIds;

    /**
     * CONVERTE DTO PARA ENTIDADE
     * 
     * CONCEITOS:
     * - Mapeamento de DTO para entidade
     * - Conversão de tipos
     * - Tratamento de campos opcionais
     * 
     * @return entidade Coleta
     */
    public Coleta toEntity() {
        Coleta coleta = new Coleta();
        
        // Mapear campos básicos
        coleta.setId(this.id);
        coleta.setEndereco(this.endereco);
        coleta.setLatitude(this.latitude != null ? this.latitude.doubleValue() : 0.0);
        coleta.setLongitude(this.longitude != null ? this.longitude.doubleValue() : 0.0);
        coleta.setObservacoes(this.observacoes);
        coleta.setStatus(this.status);
        
        // Mapear pesoEstimado para quantidade
        if (this.pesoEstimado != null) {
            coleta.setQuantidade(this.pesoEstimado);
        }
        
        // Mapear dataSolicitada
        if (this.dataHoraPreferencial != null) {
            coleta.setDataSolicitada(this.dataHoraPreferencial);
        }
        
        // Mapear dataAceitacao
        if (this.dataHoraAgendada != null) {
            coleta.setDataAceitacao(this.dataHoraAgendada);
        }
        
        // Mapear dataColeta
        if (this.dataConclusao != null) {
            coleta.setDataColeta(this.dataConclusao);
        }
        
        // Mapear valorEstimado
        if (this.pesoEstimado != null) {
            coleta.setValorEstimado(this.pesoEstimado);
        }
        
        // Mapear valorFinal
        if (this.pesoEstimado != null) {
            coleta.setValorFinal(this.pesoEstimado);
        }
        
        // Mapear descricaoMateriais (não existe na entidade Coleta)
        // if (this.descricaoMateriais != null) {
        //     coleta.setDescricaoMateriais(this.descricaoMateriais);
        // }
        
        // Mapear coletorId (será usado pelo service para buscar o coletor)
        // O coletor será definido pelo service baseado no coletorId
        
        return coleta;
    }

    /**
     * CONVERTE ENTIDADE PARA DTO
     * 
     * CONCEITOS:
     * - Mapeamento de entidade para DTO
     * - Conversão de tipos
     * - Tratamento de relacionamentos
     * 
     * @param coleta entidade Coleta
     * @return ColetaDTO convertido da entidade
     */
    public static ColetaDTO fromEntity(Coleta coleta) {
        ColetaDTO dto = new ColetaDTO();
        
        // Mapear campos básicos
        dto.setId(coleta.getId());
        dto.setEndereco(coleta.getEndereco());
        dto.setLatitude(coleta.getLatitude() != null ? BigDecimal.valueOf(coleta.getLatitude()) : null);
        dto.setLongitude(coleta.getLongitude() != null ? BigDecimal.valueOf(coleta.getLongitude()) : null);
        dto.setObservacoes(coleta.getObservacoes());
        dto.setStatus(coleta.getStatus());
        
        // Mapear quantidade para pesoEstimado
        if (coleta.getQuantidade() != null) {
            dto.setPesoEstimado(coleta.getQuantidade());
        }
        
        // Mapear datas
        dto.setDataSolicitada(coleta.getDataSolicitada());
        dto.setDataAceitacao(coleta.getDataAceitacao());
        dto.setDataColeta(coleta.getDataColeta());
        dto.setDataConclusao(coleta.getDataColeta());
        
        // Mapear valores
        dto.setValorEstimado(coleta.getValorEstimado());
        dto.setValorFinal(coleta.getValorFinal());
        
        // Mapear descricaoMateriais (não existe na entidade Coleta)
        // dto.setDescricaoMateriais(coleta.getDescricaoMateriais());
        
        // Mapear relacionamentos
        if (coleta.getUsuario() != null) {
            dto.setUsuarioId(coleta.getUsuario().getId());
            dto.setNomeUsuario(coleta.getUsuario().getNome());
        }
        
        if (coleta.getMaterial() != null) {
            dto.setMaterialId(coleta.getMaterial().getId());
            dto.setNomeMaterial(coleta.getMaterial().getNome());
            // Criar lista com o material
            dto.setMaterialIds(List.of(coleta.getMaterial().getId()));
        }
        
        if (coleta.getColetor() != null) {
            dto.setColetorId(coleta.getColetor().getId());
            dto.setNomeColetor(coleta.getColetor().getNome());
        }
        
        return dto;
    }

    /**
     * CONCEITO DIDÁTICO - Método de Atualização Parcial:
     * Atualiza apenas campos não nulos da entidade
     * 
     * @param coleta entidade a ser atualizada
     */
    public void updateEntity(Coleta coleta) {
        if (this.endereco != null) {
            coleta.setEndereco(this.endereco);
        }
        if (this.latitude != null) {
            coleta.setLatitude(this.latitude.doubleValue());
        }
        if (this.longitude != null) {
            coleta.setLongitude(this.longitude.doubleValue());
        }
        if (this.observacoes != null) {
            coleta.setObservacoes(this.observacoes);
        }
        if (this.status != null) {
            coleta.setStatus(this.status);
        }
        if (this.dataHoraPreferencial != null) {
            coleta.setDataSolicitada(this.dataHoraPreferencial);
        }
    }

    /**
     * CONCEITO DIDÁTICO - Método de Validação de Negócio:
     * Valida regras específicas do domínio
     * 
     * @return true se a coleta é válida
     */
    public boolean isValidForScheduling() {
        return this.dataHoraPreferencial != null && 
               this.dataHoraPreferencial.isAfter(LocalDateTime.now().plusHours(2));
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    
    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }
    
    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }
    
    public BigDecimal getPesoEstimado() { return pesoEstimado; }
    public void setPesoEstimado(BigDecimal pesoEstimado) { this.pesoEstimado = pesoEstimado; }
    
    public String getDescricaoMateriais() { return descricaoMateriais; }
    public void setDescricaoMateriais(String descricaoMateriais) { this.descricaoMateriais = descricaoMateriais; }
    
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    
    public StatusColeta getStatus() { return status; }
    public void setStatus(StatusColeta status) { this.status = status; }
    
    public LocalDateTime getDataHoraPreferencial() { return dataHoraPreferencial; }
    public void setDataHoraPreferencial(LocalDateTime dataHoraPreferencial) { this.dataHoraPreferencial = dataHoraPreferencial; }
    
    public LocalDateTime getDataHoraAgendada() { return dataHoraAgendada; }
    public void setDataHoraAgendada(LocalDateTime dataHoraAgendada) { this.dataHoraAgendada = dataHoraAgendada; }
    
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
    
    public LocalDateTime getDataConclusao() { return dataConclusao; }
    public void setDataConclusao(LocalDateTime dataConclusao) { this.dataConclusao = dataConclusao; }
    
    public String getNomeUsuario() { return nomeUsuario; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }
    
    public String getEmailUsuario() { return emailUsuario; }
    public void setEmailUsuario(String emailUsuario) { this.emailUsuario = emailUsuario; }
    
    public List<Long> getMaterialIds() { return materialIds; }
    public void setMaterialIds(List<Long> materialIds) { this.materialIds = materialIds; }
    
    // Métodos que estão faltando
    public void setDataSolicitada(LocalDateTime dataSolicitada) { this.dataHoraPreferencial = dataSolicitada; }
    public void setDataAceitacao(LocalDateTime dataAceitacao) { this.dataHoraAgendada = dataAceitacao; }
    public void setDataColeta(LocalDateTime dataColeta) { this.dataConclusao = dataColeta; }
    public void setValorEstimado(BigDecimal valorEstimado) { /* Não implementado no DTO atual */ }
    public void setValorFinal(BigDecimal valorFinal) { /* Não implementado no DTO atual */ }
    public void setMaterialId(Long materialId) { /* Não implementado no DTO atual */ }
    public void setNomeMaterial(String nomeMaterial) { /* Não implementado no DTO atual */ }
    public void setColetorId(Long coletorId) { /* Não implementado no DTO atual */ }
    public void setNomeColetor(String nomeColetor) { /* Não implementado no DTO atual */ }
} 