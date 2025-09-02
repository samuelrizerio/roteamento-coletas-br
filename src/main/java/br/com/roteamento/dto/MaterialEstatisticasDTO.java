package br.com.roteamento.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para estatísticas de materiais
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialEstatisticasDTO {
    
    private Long totalMateriais;
    private Long materiaisAtivos;
    private Long materiaisInativos;
    private BigDecimal valorMedioPorKg;
    private String materialMaisComum;
    private Long totalColetas;
}
