package br.com.roteamento.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * ENTIDADE COLETA ROTA - Representa a relação entre uma coleta e uma rota
 * 
 * CONCEITOS DIDÁTICOS EXPLICADOS:
 * 
 * 1. RELACIONAMENTOS N:N (MUITOS PARA MUITOS):
 *    - Uma coleta pode estar em várias rotas
 *    - Uma rota pode conter várias coletas
 *    - Tabela de associação: Armazena as relações
 *    - Chave composta: Combinação de chaves estrangeiras
 * 
 * 2. TABELA DE ASSOCIAÇÃO:
 *    - Armazena informações sobre a relação
 *    - Ordem das coletas na rota
 *    - Dados específicos da associação
 *    - Evita redundância de dados
 * 
 * 3. CHAVE COMPOSTA:
 *    - @EmbeddedId: Chave primária composta
 *    - ColetaRotaId: Classe que define a chave
 *    - Combinação de rota_id e coleta_id
 * 
 * 4. ORDENAÇÃO:
 *    - Campo ordem: Define a sequência das coletas
 *    - Importante para algoritmos de roteamento
 *    - Otimização de rotas
 */
@Entity
@Table(name = "coletas_rotas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColetaRota {

    /**
     * Chave primária composta
     * 
     * CONCEITOS:
     * - @EmbeddedId: Indica que a chave primária é composta
     * - ColetaRotaId: Classe que define a chave composta
     * - Combinação de rota_id e coleta_id
     */
    @EmbeddedId
    private ColetaRotaId id;

    /**
     * Rota à qual a coleta pertence
     * 
     * CONCEITOS:
     * - @ManyToOne: Relacionamento N:1 (várias associações, uma rota)
     * - @MapsId: Mapeia para parte da chave composta
     * - @JoinColumn: Define a coluna de chave estrangeira
     * - nullable = false: Campo obrigatório
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("rotaId")
    @JoinColumn(name = "rota_id", nullable = false)
    @NotNull(message = "Rota é obrigatória")
    private Rota rota;

    /**
     * Coleta que pertence à rota
     * 
     * CONCEITOS:
     * - @ManyToOne: Relacionamento N:1 (várias associações, uma coleta)
     * - @MapsId: Mapeia para parte da chave composta
     * - @JoinColumn: Define a coluna de chave estrangeira
     * - nullable = false: Campo obrigatório
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("coletaId")
    @JoinColumn(name = "coleta_id", nullable = false)
    @NotNull(message = "Coleta é obrigatória")
    private Coleta coleta;

    /**
     * Ordem da coleta na rota
     * 
     * CONCEITOS:
     * - @Positive: Validação que garante valor positivo
     * - Integer: Tipo primitivo para números inteiros
     * - Define a sequência das coletas na rota
     * - Importante para algoritmos de otimização
     */
    @Positive(message = "Ordem deve ser positiva")
    @Column(name = "ordem", nullable = false)
    private Integer ordem;

    /**
     * Tempo estimado de permanência no local (em minutos)
     * 
     * CONCEITOS:
     * - @Positive: Validação que garante valor positivo
     * - Integer: Tipo primitivo para números inteiros
     * - Considera tempo de coleta e preparação
     */
    @Positive(message = "Tempo estimado deve ser positivo")
    @Column(name = "tempo_estimado")
    private Integer tempoEstimado;

    /**
     * Distância até a próxima coleta (em metros)
     * 
     * CONCEITOS:
     * - @Positive: Validação que garante valor positivo
     * - Integer: Tipo primitivo para números inteiros
     * - Calculado usando fórmula de Haversine
     */
    @Positive(message = "Distância deve ser positiva")
    @Column(name = "distancia_proxima")
    private Integer distanciaProxima;

    /**
     * Observações específicas para esta coleta na rota
     * 
     * CONCEITOS:
     * - @Column: Define propriedades da coluna
     * - length = 500: Tamanho para observações detalhadas
     * - nullable = true: Campo opcional
     */
    @Column(name = "observacoes", length = 500)
    private String observacoes;

    /**
     * Data de criação da associação
     * 
     * CONCEITOS:
     * - @CreationTimestamp: Preenche automaticamente na criação
     * - LocalDateTime: Tipo para datas e horas
     * - updatable = false: Impede atualização manual
     */
    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    /**
     * MÉTODO CONSTRUTOR COM PARÂMETROS ESSENCIAIS
     * 
     * CONCEITOS:
     * - CONSTRUTOR: Método especial que inicializa o objeto
     * - Sobrecarga: Múltiplos construtores com diferentes parâmetros
     * - Inicialização da chave composta
     */
    public ColetaRota(Rota rota, Coleta coleta, Integer ordem) {
        this.rota = rota;
        this.coleta = coleta;
        this.ordem = ordem;
        this.id = new ColetaRotaId();
        this.id.setRotaId(rota.getId());
        this.id.setColetaId(coleta.getId());
    }

    /**
     * MÉTODO CONSTRUTOR COM PARÂMETROS ESSENCIAIS E TEMPO ESTIMADO
     * 
     * CONCEITOS:
     * - CONSTRUTOR: Método especial que inicializa o objeto
     * - Sobrecarga: Múltiplos construtores com diferentes parâmetros
     */
    public ColetaRota(Rota rota, Coleta coleta, Integer ordem, Integer tempoEstimado) {
        this(rota, coleta, ordem);
        this.tempoEstimado = tempoEstimado;
    }

    /**
     * MÉTODO PARA CALCULAR O TEMPO TOTAL ESTIMADO
     * 
     * CONCEITOS:
     * - MÉTODO: Função que define comportamento da classe
     * - Cálculos: Soma tempo de permanência e deslocamento
     * - Validação: Verifica se os valores estão preenchidos
     */
    public Integer calcularTempoTotal() {
        int tempoTotal = 0;
        
        if (this.tempoEstimado != null) {
            tempoTotal += this.tempoEstimado;
        }
        
        // Adiciona tempo de deslocamento (estimativa: 1 minuto por 100 metros)
        if (this.distanciaProxima != null) {
            tempoTotal += this.distanciaProxima / 100;
        }
        
        return tempoTotal;
    }

    /**
     * MÉTODO PARA VERIFICAR SE É A PRIMEIRA COLETA DA ROTA
     * 
     * CONCEITOS:
     * - MÉTODO: Função que define comportamento da classe
     * - Boolean: Tipo de retorno para valores true/false
     * - Lógica de negócio: Define regras do sistema
     */
    public boolean isPrimeiraColeta() {
        return this.ordem != null && this.ordem == 1;
    }

    /**
     * MÉTODO PARA VERIFICAR SE É A ÚLTIMA COLETA DA ROTA
     * 
     * CONCEITOS:
     * - MÉTODO: Função que define comportamento da classe
     * - Boolean: Tipo de retorno para valores true/false
     * - Lógica de negócio: Define regras do sistema
     */
    public boolean isUltimaColeta() {
        if (this.rota != null && this.ordem != null) {
            return this.ordem.equals(this.rota.getColetasRota().size());
        }
        return false;
    }

    /**
     * MÉTODO TO STRING PERSONALIZADO
     * 
     * CONCEITOS:
     * - @Override: Indica que está sobrescrevendo método da classe pai
     * - toString(): Método herdado de Object para representação em string
     */
    @Override
    public String toString() {
        return "ColetaRota{rota=%s, coleta=%s, ordem=%d}".formatted(
                rota != null ? rota.getNome() : "null",
                coleta != null ? coleta.getId() : "null",
                ordem);
    }

    /**
     * CLASSE INTERNA PARA CHAVE PRIMÁRIA COMPOSTA
     * 
     * CONCEITOS DIDÁTICOS:
     * 
     * 1. CHAVE COMPOSTA:
     *    - @Embeddable: Indica que a classe pode ser embutida
     *    - Combinação de múltiplos campos como chave primária
     *    - Útil para tabelas de associação
     * 
     * 2. CONSTRUTOR:
     *    - Construtor padrão necessário para JPA
     *    - Construtor com parâmetros para facilitar criação
     * 
     * 3. EQUALS E HASHCODE:
     *    - Métodos obrigatórios para chaves primárias
     *    - Garantem unicidade e comparação correta
     */
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ColetaRotaId implements Serializable {
        
        /**
         * ID DA ROTA
         * 
         * CONCEITOS:
         * - @Column: Mapeia para coluna do banco
         * - nullable = false: Campo obrigatório
         * - Parte da chave primária composta
         */
        @Column(name = "rota_id", nullable = false)
        private Long rotaId;
        
        /**
         * ID DA COLETA
         * 
         * CONCEITOS:
         * - @Column: Mapeia para coluna do banco
         * - nullable = false: Campo obrigatório
         * - Parte da chave primária composta
         */
        @Column(name = "coleta_id", nullable = false)
        private Long coletaId;
    }
} 