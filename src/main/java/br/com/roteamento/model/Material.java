package br.com.roteamento.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;

import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * ENTIDADE MATERIAL - Representa os tipos de materiais recicláveis
 * 
 * CONCEITOS DIDÁTICOS EXPLICADOS:
 * 
 * 1. MODELAGEM DE DADOS:
 *    - ENTIDADE: Representa um conceito do mundo real no sistema
 *    - ATRIBUTOS: Características que descrevem a entidade
 *    - RELACIONAMENTOS: Como esta entidade se conecta com outras
 *    - NORMALIZAÇÃO: Evita redundância e inconsistência de dados
 * 
 * 2. TIPOS DE DADOS EM JAVA:
 *    - String: Para textos
 *    - BigDecimal: Para valores monetários (precisão decimal)
 *    - Boolean: Para valores verdadeiro/falso
 *    - LocalDateTime: Para datas e horários
 *    - Enum: Para valores constantes predefinidos
 * 
 * 3. RELACIONAMENTOS JPA:
 *    - @OneToMany: Um material pode estar em várias coletas
 *    - @ManyToMany: Relacionamento N:N entre entidades
 *    - mappedBy: Define qual entidade controla o relacionamento
 * 
 * 4. VALIDAÇÕES:
 *    - @NotNull: Campo obrigatório
 *    - @NotBlank: Não pode ser nulo ou vazio
 *    - @Positive: Deve ser um valor positivo
 */
@Entity
@Table(name = "materiais")
@NoArgsConstructor
@AllArgsConstructor
public class Material {

    /**
     * ID do material - Chave primária
     * 
     * CONCEITOS:
     * - Chave primária: Identifica unicamente cada registro
     * - Auto-incremento: Valor gerado automaticamente pelo banco
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome do material (ex: Papel, Plástico, Vidro, Metal)
     * 
     * CONCEITOS:
     * - @NotBlank: Validação que impede valores nulos ou vazios
     * - unique = true: Garante que não haverá nomes duplicados
     * - length = 50: Define tamanho máximo da coluna
     */
    @NotBlank(message = "Nome do material é obrigatório")
    @Column(name = "nome", nullable = false, unique = true, length = 50)
    private String nome;

    /**
     * Descrição detalhada do material
     * 
     * CONCEITOS:
     * - @Column: Define propriedades da coluna
     * - length = 500: Tamanho máximo para descrições longas
     * - nullable = true: Permite valores nulos
     */
    @Column(name = "descricao", length = 500)
    private String descricao;

    /**
     * Categoria do material (ex: PAPEL, PLASTICO, VIDRO, METAL, ORGANICO)
     * 
     * CONCEITOS:
     * - @Enumerated: Define como o enum será persistido
     * - EnumType.STRING: Armazena o nome do enum como string
     * - Mais legível no banco de dados
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false)
    private CategoriaMaterial categoria;

    /**
     * Valor por quilo do material (em reais)
     * 
     * CONCEITOS:
     * - BigDecimal: Tipo para valores monetários
     * - Precisão decimal para cálculos financeiros
     * - @Positive: Validação que garante valor positivo
     */
    @NotNull(message = "Valor por quilo é obrigatório")
    @Positive(message = "Valor por quilo deve ser positivo")
    @Column(name = "valor_por_quilo", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorPorQuilo;

    /**
     * Indica se o material é aceito para coleta
     * 
     * CONCEITOS:
     * - Boolean: Tipo primitivo para valores true/false
     * - defaultValue = true: Valor padrão quando não especificado
     */
    @Column(name = "aceito_para_coleta", nullable = false)
    private Boolean aceitoParaColeta = true;

    /**
     * Instruções de preparação do material
     * 
     * CONCEITOS:
     * - @Column: Define propriedades da coluna
     * - length = 1000: Tamanho para instruções detalhadas
     */
    @Column(name = "instrucoes_preparacao", length = 1000)
    private String instrucoesPreparacao;

    /**
     * Cor do material para identificação visual
     * 
     * CONCEITOS:
     * - @Enumerated: Define como o enum será persistido
     * - EnumType.STRING: Armazena o nome do enum como string
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "cor_identificacao")
    private CorIdentificacao corIdentificacao;

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
     * Lista de coletas que contêm este material
     * 
     * CONCEITOS:
     * - @OneToMany: Relacionamento 1:N (um material, várias coletas)
     * - mappedBy: Indica qual campo na entidade Coleta faz a referência
     * - cascade: Define operações em cascata
     * - fetch: Define como os dados serão carregados
     */
    @OneToMany(mappedBy = "material", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Coleta> coletas = new HashSet<>();

    /**
     * ENUM CATEGORIA MATERIAL - Define as categorias de materiais
     * 
     * CONCEITOS:
     * - ENUM: Tipo especial que define constantes
     * - Útil para categorizar e organizar materiais
     */
    public enum CategoriaMaterial {
        PAPEL("Papel e papelão"),
        PLASTICO("Plástico"),
        VIDRO("Vidro"),
        METAL("Metal"),
        ORGANICO("Material orgânico"),
        ELETRONICO("Lixo eletrônico"),
        PERIGOSO("Material perigoso");

        private final String descricao;

        CategoriaMaterial(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    /**
     * ENUM COR IDENTIFICACAO - Define as cores para identificação visual
     * 
     * CONCEITOS:
     * - ENUM: Tipo especial que define constantes
     * - Útil para identificação visual dos materiais
     */
    public enum CorIdentificacao {
        AZUL("Azul - Papel"),
        VERMELHO("Vermelho - Plástico"),
        VERDE("Verde - Vidro"),
        AMARELO("Amarelo - Metal"),
        MARROM("Marrom - Orgânico"),
        LARANJA("Laranja - Eletrônico"),
        ROXO("Roxo - Perigoso");

        private final String descricao;

        CorIdentificacao(String descricao) {
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
    public Material(String nome, CategoriaMaterial categoria, BigDecimal valorPorQuilo) {
        this.nome = nome;
        this.categoria = categoria;
        this.valorPorQuilo = valorPorQuilo;
    }

    /**
     * MÉTODO PARA CALCULAR O VALOR TOTAL BASEADO NO PESO
     * 
     * CONCEITOS:
     * - MÉTODO: Função que define comportamento da classe
     * - BigDecimal: Para cálculos precisos com valores monetários
     * - multiply(): Método para multiplicação
     */
    public BigDecimal calcularValorTotal(BigDecimal peso) {
        if (peso == null || peso.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        return this.valorPorQuilo.multiply(peso);
    }

    /**
     * MÉTODO PARA VERIFICAR SE O MATERIAL É ACEITO
     * 
     * CONCEITOS:
     * - MÉTODO: Função que define comportamento da classe
     * - Boolean: Tipo de retorno para valores true/false
     */
    public boolean isAceitoParaColeta() {
        return aceitoParaColeta != null && aceitoParaColeta;
    }

    /**
     * MÉTODO PARA ADICIONAR UMA COLETA AO MATERIAL
     * 
     * CONCEITOS:
     * - MÉTODO: Função que define comportamento da classe
     * - BIDIRECIONAL: Mantém consistência entre as entidades
     */
    public void adicionarColeta(Coleta coleta) {
        this.coletas.add(coleta);
        coleta.setMaterial(this);
    }

    /**
     * MÉTODO PARA REMOVER UMA COLETA DO MATERIAL
     */
    public void removerColeta(Coleta coleta) {
        this.coletas.remove(coleta);
        coleta.setMaterial(null);
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
    

    
    public BigDecimal getPrecoPorKg() { return valorPorQuilo; }
    public void setPrecoPorKg(BigDecimal precoPorKg) { this.valorPorQuilo = precoPorKg; }
    
    public BigDecimal getValorPorQuilo() { return valorPorQuilo; }
    public void setValorPorQuilo(BigDecimal valorPorQuilo) { this.valorPorQuilo = valorPorQuilo; }
    
    public Boolean getAtivo() { return aceitoParaColeta; }
    public void setAtivo(Boolean ativo) { this.aceitoParaColeta = ativo; }
    
    public Boolean getAceitoParaColeta() { return aceitoParaColeta; }
    public void setAceitoParaColeta(Boolean aceitoParaColeta) { this.aceitoParaColeta = aceitoParaColeta; }
    
    public CorIdentificacao getCorIdentificacao() { return corIdentificacao; }
    public void setCorIdentificacao(CorIdentificacao corIdentificacao) { this.corIdentificacao = corIdentificacao; }
    
    public String getInstrucoesPreparacao() { return instrucoesPreparacao; }
    public void setInstrucoesPreparacao(String instrucoesPreparacao) { this.instrucoesPreparacao = instrucoesPreparacao; }
    
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
    
    public Set<Coleta> getColetas() { return coletas; }
    public void setColetas(Set<Coleta> coletas) { this.coletas = coletas; }

    /**
     * MÉTODO TO STRING PERSONALIZADO
     * 
     * CONCEITOS:
     * - @Override: Indica que está sobrescrevendo método da classe pai
     * - toString(): Método herdado de Object para representação em string
     */
    @Override
    public String toString() {
        return "Material{id=%d, nome='%s', categoria=%s, valorPorQuilo=%s}".formatted(
                id, nome, categoria, valorPorQuilo);
    }
} 