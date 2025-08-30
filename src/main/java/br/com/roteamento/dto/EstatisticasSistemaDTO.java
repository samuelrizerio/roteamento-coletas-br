package br.com.roteamento.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * DTO PARA ESTATÍSTICAS DO SISTEMA
 * 
 * CONCEITOS IMPLEMENTADOS:
 * 
 * 1. DTO PATTERN:
 *    - Data Transfer Object: Objeto para transferência de dados
 *    - Separação de responsabilidades: Modelo vs DTO
 *    - Serialização segura: Sem problemas de lazy loading
 * 
 * 2. ANOTAÇÕES JACKSON:
 *    - @JsonProperty: Nome da propriedade no JSON
 *    - @JsonFormat: Formato de serialização
 *    - Serialização controlada: Evita problemas de proxy
 * 
 * 3. ESTRUTURA DE DADOS:
 *    - Estatísticas básicas: Contadores e totais
 *    - Agrupamentos: Por categoria e status
 *    - Métricas calculadas: Valores derivados
 */
public class EstatisticasSistemaDTO {

    @JsonProperty("totalUsuarios")
    private Long totalUsuarios;

    @JsonProperty("totalMateriais")
    private Long totalMateriais;

    @JsonProperty("totalColetas")
    private Long totalColetas;

    @JsonProperty("totalRotas")
    private Long totalRotas;

    @JsonProperty("coletasPendentes")
    private Long coletasPendentes;

    @JsonProperty("rotasEmExecucao")
    private Long rotasEmExecucao;

    @JsonProperty("valorTotalEstimado")
    private Double valorTotalEstimado;

    @JsonProperty("materiaisPorCategoria")
    private Map<String, Long> materiaisPorCategoria;

    @JsonProperty("coletasPorStatus")
    private Map<String, Long> coletasPorStatus;

    // Construtor padrão
    public EstatisticasSistemaDTO() {}

    // Construtor com todos os campos
    public EstatisticasSistemaDTO(Long totalUsuarios, Long totalMateriais, Long totalColetas, 
                           Long totalRotas, Long coletasPendentes, Long rotasEmExecucao,
                           Double valorTotalEstimado, Map<String, Long> materiaisPorCategoria,
                           Map<String, Long> coletasPorStatus) {
        this.totalUsuarios = totalUsuarios;
        this.totalMateriais = totalMateriais;
        this.totalColetas = totalColetas;
        this.totalRotas = totalRotas;
        this.coletasPendentes = coletasPendentes;
        this.rotasEmExecucao = rotasEmExecucao;
        this.valorTotalEstimado = valorTotalEstimado;
        this.materiaisPorCategoria = materiaisPorCategoria;
        this.coletasPorStatus = coletasPorStatus;
    }

    // Getters e Setters
    public Long getTotalUsuarios() { return totalUsuarios; }
    public void setTotalUsuarios(Long totalUsuarios) { this.totalUsuarios = totalUsuarios; }

    public Long getTotalMateriais() { return totalMateriais; }
    public void setTotalMateriais(Long totalMateriais) { this.totalMateriais = totalMateriais; }

    public Long getTotalColetas() { return totalColetas; }
    public void setTotalColetas(Long totalColetas) { this.totalColetas = totalColetas; }

    public Long getTotalRotas() { return totalRotas; }
    public void setTotalRotas(Long totalRotas) { this.totalRotas = totalRotas; }

    public Long getColetasPendentes() { return coletasPendentes; }
    public void setColetasPendentes(Long coletasPendentes) { this.coletasPendentes = coletasPendentes; }

    public Long getRotasEmExecucao() { return rotasEmExecucao; }
    public void setRotasEmExecucao(Long rotasEmExecucao) { this.rotasEmExecucao = rotasEmExecucao; }

    public Double getValorTotalEstimado() { return valorTotalEstimado; }
    public void setValorTotalEstimado(Double valorTotalEstimado) { this.valorTotalEstimado = valorTotalEstimado; }

    public Map<String, Long> getMateriaisPorCategoria() { return materiaisPorCategoria; }
    public void setMateriaisPorCategoria(Map<String, Long> materiaisPorCategoria) { this.materiaisPorCategoria = materiaisPorCategoria; }

    public Map<String, Long> getColetasPorStatus() { return coletasPorStatus; }
    public void setColetasPorStatus(Map<String, Long> coletasPorStatus) { this.coletasPorStatus = coletasPorStatus; }

    @Override
    public String toString() {
        return "EstatisticasSistemaDTO{" +
                "totalUsuarios=" + totalUsuarios +
                ", totalMateriais=" + totalMateriais +
                ", totalColetas=" + totalColetas +
                ", totalRotas=" + totalRotas +
                ", coletasPendentes=" + coletasPendentes +
                ", rotasEmExecucao=" + rotasEmExecucao +
                ", valorTotalEstimado=" + valorTotalEstimado +
                ", materiaisPorCategoria=" + materiaisPorCategoria +
                ", coletasPorStatus=" + coletasPorStatus +
                '}';
    }
}
