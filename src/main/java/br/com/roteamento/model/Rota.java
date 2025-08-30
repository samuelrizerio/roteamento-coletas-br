package br.com.roteamento.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * ENTIDADE ROTA - Representa uma rota otimizada para coleta de materiais
 * 
 * CONCEITOS DIDÁTICOS EXPLICADOS:
 * 
 * 1. ALGORITMOS DE ROTEAMENTO:
 *    - Algoritmo do Caixeiro Viajante (TSP - Traveling Salesman Problem)
 *    - Algoritmo de Dijkstra para caminhos mais curtos
 *    - Algoritmo A* para busca heurística
 *    - Otimização de rotas com múltiplas paradas
 * 
 * 2. GEOLOCALIZAÇÃO E CÁLCULOS:
 *    - Fórmula de Haversine para cálculo de distâncias
 *    - Coordenadas geográficas (latitude/longitude)
 *    - Tempo estimado de viagem
 *    - Distância total da rota
 * 
 * 3. RELACIONAMENTOS COMPLEXOS:
 *    - @OneToMany: Uma rota contém várias coletas
 *    - @ManyToOne: Uma rota pertence a um coletor
 *    - Ordenação das coletas na rota
 * 
 * 4. OTIMIZAÇÃO DE PERFORMANCE:
 *    - Cálculo de distâncias em cache
 *    - Tempo estimado de execução
 *    - Capacidade máxima da rota
 */
@Entity
@Table(name = "rotas")
@NoArgsConstructor
@AllArgsConstructor
public class Rota {

    /**
     * ID da rota - Chave primária
     * 
     * CONCEITOS:
     * - Chave primária: Identifica unicamente cada registro
     * - Auto-incremento: Valor gerado automaticamente pelo banco
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Coletor responsável pela rota
     * 
     * CONCEITOS:
     * - @ManyToOne: Relacionamento N:1 (várias rotas, um coletor)
     * - @JoinColumn: Define a coluna de chave estrangeira
     * - nullable = false: Campo obrigatório
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coletor_id", nullable = false)
    @NotNull(message = "Coletor é obrigatório")
    private Usuario coletor;

    /**
     * Nome ou identificação da rota
     * 
     * CONCEITOS:
     * - @Column: Define propriedades da coluna
     * - length = 100: Tamanho máximo para nomes
     * - nullable = false: Campo obrigatório
     */
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    /**
     * Descrição da rota
     * 
     * CONCEITOS:
     * - @Column: Define propriedades da coluna
     * - length = 500: Tamanho para descrições detalhadas
     */
    @Column(name = "descricao", length = 500)
    private String descricao;

    /**
     * Status atual da rota
     * 
     * CONCEITOS:
     * - @Enumerated: Define como o enum será persistido
     * - EnumType.STRING: Armazena o nome do enum como string
     * - defaultValue = StatusRota.PLANEJADA
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusRota status = StatusRota.PLANEJADA;

    /**
     * Distância total da rota (em metros)
     * 
     * CONCEITOS:
     * - @Positive: Validação que garante valor positivo
     * - Integer: Tipo primitivo para números inteiros
     * - Calculado automaticamente baseado nas coordenadas
     */
    @Positive(message = "Distância deve ser positiva")
    @Column(name = "distancia_total")
    private Integer distanciaTotal;

    /**
     * Tempo estimado da rota (em minutos)
     * 
     * CONCEITOS:
     * - @Positive: Validação que garante valor positivo
     * - Integer: Tipo primitivo para números inteiros
     * - Calculado considerando trânsito e paradas
     */
    @Positive(message = "Tempo estimado deve ser positivo")
    @Column(name = "tempo_estimado")
    private Integer tempoEstimado;

    /**
     * Capacidade máxima da rota (em quilos)
     * 
     * CONCEITOS:
     * - @Positive: Validação que garante valor positivo
     * - BigDecimal: Para precisão decimal
     * - Limita o peso total das coletas na rota
     */
    @Positive(message = "Capacidade deve ser positiva")
    @Column(name = "capacidade_maxima", precision = 8, scale = 2)
    private BigDecimal capacidadeMaxima;

    /**
     * Capacidade atual utilizada (em quilos)
     * 
     * CONCEITOS:
     * - Calculado automaticamente baseado nas coletas
     * - BigDecimal: Para precisão decimal
     * - Não pode exceder a capacidade máxima
     */
    @Column(name = "capacidade_atual", precision = 8, scale = 2)
    private BigDecimal capacidadeAtual = BigDecimal.ZERO;

    /**
     * Valor total estimado da rota (em reais)
     * 
     * CONCEITOS:
     * - Calculado automaticamente baseado nas coletas
     * - BigDecimal: Para valores monetários
     * - precision = 10, scale = 2: Para valores monetários
     */
    @Column(name = "valor_total_estimado", precision = 10, scale = 2)
    private BigDecimal valorTotalEstimado = BigDecimal.ZERO;

    /**
     * Data e hora de início da rota
     * 
     * CONCEITOS:
     * - LocalDateTime: Tipo moderno para datas e horas
     * - nullable = true: Pode ser agendada para depois
     */
    @Column(name = "data_inicio")
    private LocalDateTime dataInicio;

    /**
     * Data e hora de fim da rota
     * 
     * CONCEITOS:
     * - LocalDateTime: Tipo moderno para datas e horas
     * - Preenchido quando a rota é finalizada
     */
    @Column(name = "data_fim")
    private LocalDateTime dataFim;

    /**
     * Coordenadas de início da rota (latitude)
     * 
     * CONCEITOS:
     * - Double: Tipo primitivo para números decimais
     * - Usado para cálculos de rota e geolocalização
     */
    @Column(name = "latitude_inicio")
    private Double latitudeInicio;

    /**
     * Coordenadas de início da rota (longitude)
     * 
     * CONCEITOS:
     * - Double: Tipo primitivo para números decimais
     * - Usado junto com latitude para geolocalização
     */
    @Column(name = "longitude_inicio")
    private Double longitudeInicio;

    /**
     * Coordenadas de fim da rota (latitude)
     * 
     * CONCEITOS:
     * - Double: Tipo primitivo para números decimais
     * - Pode ser diferente do início (rota circular ou linear)
     */
    @Column(name = "latitude_fim")
    private Double latitudeFim;

    /**
     * Coordenadas de fim da rota (longitude)
     * 
     * CONCEITOS:
     * - Double: Tipo primitivo para números decimais
     * - Usado junto com latitude para geolocalização
     */
    @Column(name = "longitude_fim")
    private Double longitudeFim;

    /**
     * Observações sobre a rota
     * 
     * CONCEITOS:
     * - @Column: Define propriedades da coluna
     * - length = 1000: Tamanho para observações detalhadas
     */
    @Column(name = "observacoes", length = 1000)
    private String observacoes;

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
     * Lista de coletas na rota (ordenadas)
     * 
     * CONCEITOS:
     * - @OneToMany: Relacionamento 1:N (uma rota, várias coletas)
     * - @OrderBy: Define a ordem das coletas na rota
     * - cascade: Define operações em cascata
     * - fetch: Define como os dados serão carregados
     */
    @OneToMany(mappedBy = "rota", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("ordem ASC")
    private List<ColetaRota> coletasRota = new ArrayList<>();

    /**
     * ENUM STATUS ROTA - Define os status possíveis da rota
     * 
     * CONCEITOS:
     * - ENUM: Tipo especial que define constantes
     * - Útil para controlar o fluxo da rota
     */
    public enum StatusRota {
        PLANEJADA("Rota planejada"),
        EM_EXECUCAO("Rota em execução"),
        FINALIZADA("Rota finalizada"),
        CANCELADA("Rota cancelada"),
        OTIMIZADA("Rota otimizada");

        private final String descricao;

        StatusRota(String descricao) {
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
    public Rota(Usuario coletor, String nome) {
        this.coletor = coletor;
        this.nome = nome;
    }

    /**
     * MÉTODO PARA ADICIONAR UMA COLETA À ROTA
     * 
     * CONCEITOS:
     * - MÉTODO: Função que define comportamento da classe
     * - Validação: Verifica se há capacidade disponível
     * - Atualização: Recalcula valores da rota
     */
    public void adicionarColeta(Coleta coleta, int ordem) {
        // Verifica se há capacidade disponível
        if (this.capacidadeMaxima != null) {
            BigDecimal novaCapacidade = this.capacidadeAtual.add(coleta.getQuantidade());
            if (novaCapacidade.compareTo(this.capacidadeMaxima) > 0) {
                throw new IllegalStateException("Capacidade máxima da rota seria excedida");
            }
        }

        // Cria a relação entre rota e coleta
        ColetaRota coletaRota = new ColetaRota(this, coleta, ordem);
        this.coletasRota.add(coletaRota);
        
        // Atualiza valores da rota
        this.atualizarValoresRota();
    }

    /**
     * MÉTODO PARA REMOVER UMA COLETA DA ROTA
     * 
     * CONCEITOS:
     * - MÉTODO: Função que define comportamento da classe
     * - Atualização: Recalcula valores da rota
     */
    public void removerColeta(Coleta coleta) {
        this.coletasRota.removeIf(cr -> cr.getColeta().equals(coleta));
        this.atualizarValoresRota();
    }

    /**
     * MÉTODO PARA ATUALIZAR OS VALORES DA ROTA
     * 
     * CONCEITOS:
     * - MÉTODO: Função que define comportamento da classe
     * - Cálculos: Soma valores das coletas
     * - Encapsulamento: Lógica de negócio dentro da entidade
     */
    public void atualizarValoresRota() {
        this.capacidadeAtual = this.coletasRota.stream()
                .map(cr -> cr.getColeta().getQuantidade())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.valorTotalEstimado = this.coletasRota.stream()
                .map(cr -> cr.getColeta().getValorEstimado())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * MÉTODO PARA INICIAR A ROTA
     * 
     * CONCEITOS:
     * - MÉTODO: Função que define comportamento da classe
     * - Validação: Verifica se a rota pode ser iniciada
     * - Atualização de estado: Muda o status da rota
     */
    public void iniciarRota() {
        if (this.status == StatusRota.PLANEJADA || this.status == StatusRota.OTIMIZADA) {
            this.status = StatusRota.EM_EXECUCAO;
            this.dataInicio = LocalDateTime.now();
        } else {
            throw new IllegalStateException("Rota não pode ser iniciada no status atual: " + this.status);
        }
    }

    /**
     * MÉTODO PARA FINALIZAR A ROTA
     * 
     * CONCEITOS:
     * - MÉTODO: Função que define comportamento da classe
     * - Validação: Verifica se a rota pode ser finalizada
     * - Atualização de estado: Muda o status da rota
     */
    public void finalizarRota() {
        if (this.status == StatusRota.EM_EXECUCAO) {
            this.status = StatusRota.FINALIZADA;
            this.dataFim = LocalDateTime.now();
        } else {
            throw new IllegalStateException("Rota não pode ser finalizada no status atual: " + this.status);
        }
    }

    /**
     * MÉTODO PARA CALCULAR A DURAÇÃO DA ROTA
     * 
     * CONCEITOS:
     * - MÉTODO: Função que define comportamento da classe
     * - Duration: Classe para cálculos de duração
     * - Validação: Verifica se as datas estão preenchidas
     */
    public Duration calcularDuracao() {
        if (this.dataInicio != null && this.dataFim != null) {
            return Duration.between(this.dataInicio, this.dataFim);
        }
        return null;
    }

    /**
     * MÉTODO PARA VERIFICAR SE A ROTA ESTÁ CHEIA
     * 
     * CONCEITOS:
     * - MÉTODO: Função que define comportamento da classe
     * - Boolean: Tipo de retorno para valores true/false
     * - Lógica de negócio: Define regras do sistema
     */
    public boolean estaCheia() {
        if (this.capacidadeMaxima == null) {
            return false;
        }
        return this.capacidadeAtual.compareTo(this.capacidadeMaxima) >= 0;
    }

    /**
     * MÉTODO PARA VERIFICAR SE A ROTA PODE SER INICIADA
     * 
     * CONCEITOS:
     * - MÉTODO: Função que define comportamento da classe
     * - Boolean: Tipo de retorno para valores true/false
     */
    public boolean podeSerIniciada() {
        return this.status == StatusRota.PLANEJADA || this.status == StatusRota.OTIMIZADA;
    }

    /**
     * MÉTODO PARA VERIFICAR SE A ROTA ESTÁ EM EXECUÇÃO
     * 
     * CONCEITOS:
     * - MÉTODO: Função que define comportamento da classe
     * - Boolean: Tipo de retorno para valores true/false
     */
    public boolean estaEmExecucao() {
        return this.status == StatusRota.EM_EXECUCAO;
    }

    /**
     * MÉTODO PARA VERIFICAR SE A ROTA FOI FINALIZADA
     * 
     * CONCEITOS:
     * - MÉTODO: Função que define comportamento da classe
     * - Boolean: Tipo de retorno para valores true/false
     */
    public boolean foiFinalizada() {
        return this.status == StatusRota.FINALIZADA;
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
        return String.format("Rota{id=%d, nome='%s', coletor=%s, status=%s, coletas=%d}",
                id, nome, coletor != null ? coletor.getNome() : "null", 
                status, coletasRota.size());
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Usuario getColetor() { return coletor; }
    public void setColetor(Usuario coletor) { this.coletor = coletor; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public StatusRota getStatus() { return status; }
    public void setStatus(StatusRota status) { this.status = status; }
    
    public Integer getDistanciaTotal() { return distanciaTotal; }
    public void setDistanciaTotal(Integer distanciaTotal) { this.distanciaTotal = distanciaTotal; }
    
    public Integer getTempoEstimado() { return tempoEstimado; }
    public void setTempoEstimado(Integer tempoEstimado) { this.tempoEstimado = tempoEstimado; }
    
    public BigDecimal getCapacidadeMaxima() { return capacidadeMaxima; }
    public void setCapacidadeMaxima(BigDecimal capacidadeMaxima) { this.capacidadeMaxima = capacidadeMaxima; }
    
    public BigDecimal getCapacidadeAtual() { return capacidadeAtual; }
    public void setCapacidadeAtual(BigDecimal capacidadeAtual) { this.capacidadeAtual = capacidadeAtual; }
    
    public BigDecimal getValorTotalEstimado() { return valorTotalEstimado; }
    public void setValorTotalEstimado(BigDecimal valorTotalEstimado) { this.valorTotalEstimado = valorTotalEstimado; }
    
    public LocalDateTime getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDateTime dataInicio) { this.dataInicio = dataInicio; }
    
    public LocalDateTime getDataFim() { return dataFim; }
    public void setDataFim(LocalDateTime dataFim) { this.dataFim = dataFim; }
    
    public Double getLatitudeInicio() { return latitudeInicio; }
    public void setLatitudeInicio(Double latitudeInicio) { this.latitudeInicio = latitudeInicio; }
    
    public Double getLongitudeInicio() { return longitudeInicio; }
    public void setLongitudeInicio(Double longitudeInicio) { this.longitudeInicio = longitudeInicio; }
    
    public Double getLatitudeFim() { return latitudeFim; }
    public void setLatitudeFim(Double latitudeFim) { this.latitudeFim = latitudeFim; }
    
    public Double getLongitudeFim() { return longitudeFim; }
    public void setLongitudeFim(Double longitudeFim) { this.longitudeFim = longitudeFim; }
    
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
    
    public List<ColetaRota> getColetasRota() { return coletasRota; }
    public void setColetasRota(List<ColetaRota> coletasRota) { this.coletasRota = coletasRota; }
    
    // Métodos para compatibilidade
    public List<Coleta> getColetas() {
        return coletasRota.stream()
                .map(ColetaRota::getColeta)
                .collect(java.util.stream.Collectors.toList());
    }
    
    public void setColetas(List<Coleta> coletas) {
        this.coletasRota.clear();
        for (int i = 0; i < coletas.size(); i++) {
            ColetaRota cr = new ColetaRota(this, coletas.get(i), i + 1);
            this.coletasRota.add(cr);
        }
    }
    
    public BigDecimal getCapacidadeUtilizada() { return capacidadeAtual; }
    public void setCapacidadeUtilizada(BigDecimal capacidadeUtilizada) { this.capacidadeAtual = capacidadeUtilizada; }
    
    public LocalDateTime getDataConclusao() { return dataFim; }
    public void setDataConclusao(LocalDateTime dataConclusao) { this.dataFim = dataConclusao; }
} 