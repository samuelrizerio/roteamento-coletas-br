package br.com.roteamento.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;

import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * ENTIDADE COLETA - Representa uma solicitação de coleta de material reciclável
 * 
 * CONCEITOS DIDÁTICOS EXPLICADOS:
 * 
 * 1. RELACIONAMENTOS ENTRE ENTIDADES:
 *    - @ManyToOne: Relacionamento N:1 (várias coletas, um usuário)
 *    - @ManyToOne: Relacionamento N:1 (várias coletas, um material)
 *    - Chave estrangeira: Campo que referencia outra tabela
 *    - JoinColumn: Define como a chave estrangeira será mapeada
 * 
 * 2. CICLO DE VIDA DE UMA COLETA:
 *    - SOLICITADA: Usuário solicita a coleta
 *    - EM_ANALISE: Sistema analisa a solicitação
 *    - ACEITA: Coleta foi aceita pelo coletor
 *    - EM_ROTA: Coletor está a caminho
 *    - COLETADA: Material foi coletado
 *    - CANCELADA: Coleta foi cancelada
 *    - REJEITADA: Coleta foi rejeitada
 * 
 * 3. GEOLOCALIZAÇÃO:
 *    - Latitude e Longitude: Coordenadas geográficas
 *    - Cálculo de distâncias entre pontos
 *    - Integração com APIs de mapeamento
 * 
 * 4. CÁLCULOS FINANCEIROS:
 *    - Valor baseado no peso e tipo de material
 *    - Precificação dinâmica
 *    - Histórico de valores
 */
@Entity
@Table(name = "coletas")
@NoArgsConstructor
@AllArgsConstructor
public class Coleta {

    /**
     * ID da coleta - Chave primária
     * 
     * CONCEITOS:
     * - Chave primária: Identifica unicamente cada registro
     * - Auto-incremento: Valor gerado automaticamente pelo banco
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Usuário que solicitou a coleta
     * 
     * CONCEITOS:
     * - @ManyToOne: Relacionamento N:1 (várias coletas, um usuário)
     * - @JoinColumn: Define a coluna de chave estrangeira
     * - nullable = false: Campo obrigatório
     * - fetch = FetchType.LAZY: Carrega apenas quando necessário
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @NotNull(message = "Usuário é obrigatório")
    private Usuario usuario;

    /**
     * Material a ser coletado
     * 
     * CONCEITOS:
     * - @ManyToOne: Relacionamento N:1 (várias coletas, um material)
     * - @JoinColumn: Define a coluna de chave estrangeira
     * - nullable = false: Campo obrigatório
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", nullable = false)
    @NotNull(message = "Material é obrigatório")
    private Material material;

    /**
     * Coletor responsável pela coleta
     * 
     * CONCEITOS:
     * - Relacionamento opcional (pode ser nulo inicialmente)
     * - Será preenchido quando um coletor aceitar a coleta
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coletor_id")
    private Usuario coletor;

    /**
     * Quantidade de material (em quilos)
     * 
     * CONCEITOS:
     * - @Positive: Validação que garante valor positivo
     * - BigDecimal: Para precisão decimal em cálculos
     * - precision = 8, scale = 2: 8 dígitos total, 2 decimais
     */
    @NotNull(message = "Quantidade é obrigatória")
    @Positive(message = "Quantidade deve ser positiva")
    @Column(name = "quantidade", nullable = false, precision = 8, scale = 2)
    private BigDecimal quantidade;

    /**
     * Valor estimado da coleta (em reais)
     * 
     * CONCEITOS:
     * - Calculado automaticamente baseado no material e quantidade
     * - precision = 10, scale = 2: Para valores monetários
     */
    @Column(name = "valor_estimado", precision = 10, scale = 2)
    private BigDecimal valorEstimado;

    /**
     * Valor final da coleta (pode diferir do estimado)
     * 
     * CONCEITOS:
     * - Definido pelo coletor após avaliar o material
     * - Pode ser ajustado baseado na qualidade do material
     */
    @Column(name = "valor_final", precision = 10, scale = 2)
    private BigDecimal valorFinal;

    /**
     * Status atual da coleta
     * 
     * CONCEITOS:
     * - @Enumerated: Define como o enum será persistido
     * - EnumType.STRING: Armazena o nome do enum como string
     * - defaultValue = StatusColeta.SOLICITADA
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusColeta status = StatusColeta.SOLICITADA;

    /**
     * Endereço da coleta
     * 
     * CONCEITOS:
     * - length = 200: Tamanho máximo para endereços
     * - nullable = true: Pode usar coordenadas em vez de endereço
     */
    @Column(name = "endereco", length = 200)
    private String endereco;

    /**
     * Latitude da localização da coleta
     * 
     * CONCEITOS:
     * - Double: Tipo primitivo para números decimais
     * - Usado para cálculos de rota e geolocalização
     * - nullable = true: Pode usar endereço em vez de coordenadas
     */
    @Column(name = "latitude")
    private Double latitude;

    /**
     * Longitude da localização da coleta
     * 
     * CONCEITOS:
     * - Double: Tipo primitivo para números decimais
     * - Usado junto com latitude para geolocalização
     */
    @Column(name = "longitude")
    private Double longitude;

    /**
     * Observações sobre a coleta
     * 
     * CONCEITOS:
     * - @Column: Define propriedades da coluna
     * - length = 1000: Tamanho para observações detalhadas
     */
    @Column(name = "observacoes", length = 1000)
    private String observacoes;

    /**
     * Data e hora solicitada para a coleta
     * 
     * CONCEITOS:
     * - LocalDateTime: Tipo para datas e horas
     * - nullable = true: Pode ser agendada para depois
     */
    @Column(name = "data_solicitada")
    private LocalDateTime dataSolicitada;

    /**
     * Data e hora em que a coleta foi aceita
     * 
     * CONCEITOS:
     * - Preenchido quando um coletor aceita a coleta
     * - Útil para análise de performance
     */
    @Column(name = "data_aceitacao")
    private LocalDateTime dataAceitacao;

    /**
     * Data e hora em que a coleta foi realizada
     * 
     * CONCEITOS:
     * - Preenchido quando a coleta é finalizada
     * - Útil para análise de tempo de execução
     */
    @Column(name = "data_coleta")
    private LocalDateTime dataColeta;

    /**
     * Data de criação do registro
     * 
     * CONCEITOS:
     * - @CreationTimestamp: Preenche automaticamente na criação
     * - updatable = false: Impede atualização manual
     */
    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    /**
     * Data da última atualização
     * 
     * CONCEITOS:
     * - @UpdateTimestamp: Atualiza automaticamente a cada modificação
     */
    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    /**
     * ENUM STATUS COLETA - Define os status possíveis da coleta
     * 
     * CONCEITOS:
     * - ENUM: Tipo especial que define constantes
     * - Útil para controlar o fluxo da coleta
     */
    public enum StatusColeta {
        SOLICITADA("Solicitada pelo usuário"),
        EM_ANALISE("Em análise pelo sistema"),
        ACEITA("Aceita por um coletor"),
        EM_ROTA("Coletor está a caminho"),
        COLETADA("Material foi coletado"),
        CANCELADA("Coleta foi cancelada"),
        REJEITADA("Coleta foi rejeitada");

        private final String descricao;

        StatusColeta(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    /**
     * MÉTODO CONSTRUTOR COM PARÂMETROS ESSENCIAIS
     * 
     * CONCEITOS:
     * - CONSTRUTOR: Método especial que inicializa o objeto
     * - Sobrecarga: Múltiplos construtores com diferentes parâmetros
     */
    public Coleta(Usuario usuario, Material material, BigDecimal quantidade) {
        this.usuario = usuario;
        this.material = material;
        this.quantidade = quantidade;
        this.calcularValorEstimado();
    }

    /**
     * MÉTODO PARA CALCULAR O VALOR ESTIMADO DA COLETA
     * 
     * CONCEITOS:
     * - MÉTODO: Função que define comportamento da classe
     * - Encapsulamento: Lógica de negócio dentro da entidade
     * - Reutilização: Pode ser chamado em diferentes contextos
     */
    public void calcularValorEstimado() {
        if (this.material != null && this.quantidade != null) {
            this.valorEstimado = this.material.calcularValorTotal(this.quantidade);
        }
    }

    /**
     * MÉTODO PARA ACEITAR A COLETA POR UM COLETOR
     * 
     * CONCEITOS:
     * - MÉTODO: Função que define comportamento da classe
     * - Validação: Verifica se a coleta pode ser aceita
     * - Atualização de estado: Muda o status da coleta
     */
    public void aceitarColeta(Usuario coletor) {
        if (this.status == StatusColeta.SOLICITADA || this.status == StatusColeta.EM_ANALISE) {
            this.coletor = coletor;
            this.status = StatusColeta.ACEITA;
            this.dataAceitacao = LocalDateTime.now();
        } else {
            throw new IllegalStateException("Coleta não pode ser aceita no status atual: " + this.status);
        }
    }

    /**
     * MÉTODO PARA INICIAR A ROTA DA COLETA
     * 
     * CONCEITOS:
     * - MÉTODO: Função que define comportamento da classe
     * - Validação: Verifica se a coleta pode iniciar rota
     * - Atualização de estado: Muda o status da coleta
     */
    public void iniciarRota() {
        if (this.status == StatusColeta.ACEITA && this.coletor != null) {
            this.status = StatusColeta.EM_ROTA;
        } else {
            throw new IllegalStateException("Coleta não pode iniciar rota no status atual: " + this.status);
        }
    }

    /**
     * MÉTODO PARA FINALIZAR A COLETA
     * 
     * CONCEITOS:
     * - MÉTODO: Função que define comportamento da classe
     * - Validação: Verifica se a coleta pode ser finalizada
     * - Atualização de estado: Muda o status da coleta
     */
    public void finalizarColeta(BigDecimal valorFinal) {
        if (this.status == StatusColeta.EM_ROTA) {
            this.status = StatusColeta.COLETADA;
            this.valorFinal = valorFinal;
            this.dataColeta = LocalDateTime.now();
        } else {
            throw new IllegalStateException("Coleta não pode ser finalizada no status atual: " + this.status);
        }
    }

    /**
     * MÉTODO PARA CANCELAR A COLETA
     * 
     * CONCEITOS:
     * - MÉTODO: Função que define comportamento da classe
     * - Validação: Verifica se a coleta pode ser cancelada
     * - Atualização de estado: Muda o status da coleta
     */
    public void cancelarColeta() {
        if (this.status != StatusColeta.COLETADA && this.status != StatusColeta.CANCELADA) {
            this.status = StatusColeta.CANCELADA;
        } else {
            throw new IllegalStateException("Coleta não pode ser cancelada no status atual: " + this.status);
        }
    }

    /**
     * MÉTODO PARA VERIFICAR SE A COLETA PODE SER ACEITA
     * 
     * CONCEITOS:
     * - MÉTODO: Função que define comportamento da classe
     * - Boolean: Tipo de retorno para valores true/false
     * - Lógica de negócio: Define regras do sistema
     */
    public boolean podeSerAceita() {
        return this.status == StatusColeta.SOLICITADA || this.status == StatusColeta.EM_ANALISE;
    }

    /**
     * MÉTODO PARA VERIFICAR SE A COLETA ESTÁ EM ANDAMENTO
     * 
     * CONCEITOS:
     * - MÉTODO: Função que define comportamento da classe
     * - Boolean: Tipo de retorno para valores true/false
     */
    public boolean estaEmAndamento() {
        return this.status == StatusColeta.ACEITA || this.status == StatusColeta.EM_ROTA;
    }

    /**
     * MÉTODO PARA VERIFICAR SE A COLETA FOI FINALIZADA
     * 
     * CONCEITOS:
     * - MÉTODO: Função que define comportamento da classe
     * - Boolean: Tipo de retorno para valores true/false
     */
    public boolean foiFinalizada() {
        return this.status == StatusColeta.COLETADA;
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
        return "Coleta{id=%d, usuario=%s, material=%s, status=%s, quantidade=%s}".formatted(
                id, usuario != null ? usuario.getNome() : "null",
                material != null ? material.getNome() : "null",
                status, quantidade);
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    
    public Material getMaterial() { return material; }
    public void setMaterial(Material material) { this.material = material; }
    
    public Usuario getColetor() { return coletor; }
    public void setColetor(Usuario coletor) { this.coletor = coletor; }
    
    public BigDecimal getQuantidade() { return quantidade; }
    public void setQuantidade(BigDecimal quantidade) { this.quantidade = quantidade; }
    
    public BigDecimal getValorEstimado() { return valorEstimado; }
    public void setValorEstimado(BigDecimal valorEstimado) { this.valorEstimado = valorEstimado; }
    
    public BigDecimal getValorFinal() { return valorFinal; }
    public void setValorFinal(BigDecimal valorFinal) { this.valorFinal = valorFinal; }
    
    public StatusColeta getStatus() { return status; }
    public void setStatus(StatusColeta status) { this.status = status; }
    
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    
    public LocalDateTime getDataSolicitada() { return dataSolicitada; }
    public void setDataSolicitada(LocalDateTime dataSolicitada) { this.dataSolicitada = dataSolicitada; }
    
    public LocalDateTime getDataAceitacao() { return dataAceitacao; }
    public void setDataAceitacao(LocalDateTime dataAceitacao) { this.dataAceitacao = dataAceitacao; }
    
    public LocalDateTime getDataColeta() { return dataColeta; }
    public void setDataColeta(LocalDateTime dataColeta) { this.dataColeta = dataColeta; }
    
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
} 