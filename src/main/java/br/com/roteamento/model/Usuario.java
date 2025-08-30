package br.com.roteamento.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * ENTIDADE USUÁRIO - Representa um usuário do sistema
 * 
 * CONCEITOS DIDÁTICOS EXPLICADOS:
 * 
 * 1. PROGRAMACAO ORIENTADA A OBJETOS (POO):
 *    - CLASSE: É um modelo/template que define as características e comportamentos
 *    - ENCAPSULAMENTO: Os dados (atributos) são protegidos e acessados através de métodos
 *    - HERANÇA: Esta classe pode ser estendida por outras classes (ex: UsuarioAdmin)
 *    - POLIMORFISMO: Pode ser tratada como um tipo mais genérico
 * 
 * 2. ANOTAÇÕES JPA (Java Persistence API):
 *    - @Entity: Indica que esta classe é uma entidade que será persistida no banco
 *    - @Table: Define o nome da tabela no banco de dados
 *    - @Id: Marca o campo como chave primária
 *    - @GeneratedValue: Define como a chave primária será gerada automaticamente
 *    - @Column: Define propriedades específicas da coluna no banco
 *    - @OneToMany: Define relacionamento 1:N (um usuário pode ter várias coletas)
 * 
 * 3. VALIDAÇÃO DE DADOS:
 *    - @NotBlank: Valida que o campo não pode ser nulo ou vazio
 *    - @Email: Valida se o campo contém um email válido
 *    - @Size: Define o tamanho mínimo e máximo do campo
 * 
 * 4. LOMBOK:
 *    - @Data: Gera automaticamente getters, setters, equals, hashCode e toString
 *    - @NoArgsConstructor: Gera construtor sem parâmetros
 *    - @AllArgsConstructor: Gera construtor com todos os parâmetros
 * 
 * 5. MODELAGEM DE DADOS:
 *    - CHAVE PRIMÁRIA: Campo único que identifica cada registro
 *    - CHAVE ESTRANGEIRA: Campo que referencia outra tabela
 *    - RELACIONAMENTOS: Como as entidades se relacionam entre si
 *    - NORMALIZAÇÃO: Evita redundância de dados
 */
@Entity
@Table(name = "usuarios")
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    /**
     * ID do usuário - Chave primária
     * 
     * CONCEITOS:
     * - @Id: Marca este campo como chave primária
     * - @GeneratedValue: Gera automaticamente valores únicos
     * - Strategy.IDENTITY: Usa auto-incremento do banco de dados
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome completo do usuário
     * 
     * CONCEITOS:
     * - @NotBlank: Validação que impede valores nulos ou vazios
     * - @Size: Define tamanho mínimo e máximo
     * - @Column: Define propriedades da coluna no banco
     */
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    /**
     * Email do usuário (usado para login)
     * 
     * CONCEITOS:
     * - @Email: Validação de formato de email
     * - unique = true: Garante que não haverá emails duplicados
     */
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    /**
     * Senha do usuário (será criptografada)
     * 
     * CONCEITOS:
     * - @Size: Define tamanho mínimo para segurança
     * - Senhas nunca devem ser armazenadas em texto puro
     */
    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
    @Column(name = "senha", nullable = false)
    private String senha;

    /**
     * Telefone do usuário
     * 
     * CONCEITOS:
     * - length = 20: Define tamanho máximo da coluna
     * - nullable = true: Permite valores nulos
     */
    @Column(name = "telefone", length = 20)
    private String telefone;

    /**
     * Endereço do usuário
     * 
     * CONCEITOS:
     * - @Column: Define propriedades da coluna
     * - length = 200: Tamanho máximo para endereços
     */
    @Column(name = "endereco", length = 200)
    private String endereco;

    /**
     * Latitude da localização do usuário
     * 
     * CONCEITOS:
     * - Double: Tipo primitivo para números decimais
     * - Usado para cálculos de rota e geolocalização
     */
    @Column(name = "latitude")
    private Double latitude;

    /**
     * Longitude da localização do usuário
     * 
     * CONCEITOS:
     * - Double: Tipo primitivo para números decimais
     * - Usado junto com latitude para geolocalização
     */
    @Column(name = "longitude")
    private Double longitude;

    /**
     * Tipo de usuário (RESIDENCIAL, COMERCIAL, COLETOR)
     * 
     * CONCEITOS:
     * - @Enumerated: Define como o enum será persistido
     * - EnumType.STRING: Armazena o nome do enum como string
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_usuario", nullable = false)
    private TipoUsuario tipoUsuario;

    /**
     * Status do usuário (ATIVO, INATIVO, BLOQUEADO)
     * 
     * CONCEITOS:
     * - Enum: Tipo especial que define um conjunto de valores constantes
     * - Útil para controlar o estado do usuário
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusUsuario status = StatusUsuario.ATIVO;

    /**
     * Data de criação do registro
     * 
     * CONCEITOS:
     * - @CreationTimestamp: Preenche automaticamente na criação
     * - LocalDateTime: Tipo moderno para datas e horas
     */
    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    /**
     * Data da última atualização
     * 
     * CONCEITOS:
     * - @UpdateTimestamp: Atualiza automaticamente a cada modificação
     * - updatable = false: Impede atualização manual
     */
    @UpdateTimestamp
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    /**
     * Lista de coletas associadas ao usuário
     * 
     * CONCEITOS:
     * - @OneToMany: Relacionamento 1:N (um usuário, várias coletas)
     * - mappedBy: Indica qual campo na entidade Coleta faz a referência
     * - cascade: Define operações em cascata
     * - fetch: Define como os dados serão carregados
     */
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Coleta> coletas = new HashSet<>();

    /**
     * ENUM TIPO USUÁRIO - Define os tipos possíveis de usuário
     * 
     * CONCEITOS:
     * - ENUM: Tipo especial que define constantes
     * - Útil para limitar valores possíveis
     */
    public enum TipoUsuario {
        RESIDENCIAL,    // Usuário residencial que solicita coletas
        COMERCIAL,      // Usuário comercial que solicita coletas
        COLETOR         // Usuário que realiza as coletas
    }

    /**
     * ENUM STATUS USUÁRIO - Define os status possíveis do usuário
     */
    public enum StatusUsuario {
        ATIVO,      // Usuário ativo no sistema
        INATIVO,    // Usuário inativo temporariamente
        BLOQUEADO   // Usuário bloqueado por violação
    }

    /**
     * MÉTODO CONSTRUTOR COM PARÂMETROS ESSENCIAIS
     * 
     * CONCEITOS:
     * - CONSTRUTOR: Método especial que inicializa o objeto
     * - Sobrecarga: Múltiplos construtores com diferentes parâmetros
     */
    public Usuario(String nome, String email, String senha, TipoUsuario tipoUsuario) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
    }

    /**
     * MÉTODO PARA ADICIONAR UMA COLETA AO USUÁRIO
     * 
     * CONCEITOS:
     * - MÉTODO: Função que define comportamento da classe
     * - BIDIRECIONAL: Mantém consistência entre as entidades
     */
    public void adicionarColeta(Coleta coleta) {
        this.coletas.add(coleta);
        coleta.setUsuario(this);
    }

    /**
     * MÉTODO PARA REMOVER UMA COLETA DO USUÁRIO
     */
    public void removerColeta(Coleta coleta) {
        this.coletas.remove(coleta);
        coleta.setUsuario(null);
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    
    public TipoUsuario getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(TipoUsuario tipoUsuario) { this.tipoUsuario = tipoUsuario; }
    
    public StatusUsuario getStatus() { return status; }
    public void setStatus(StatusUsuario status) { this.status = status; }
    
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
} 