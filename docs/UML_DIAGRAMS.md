# Diagramas UML - Sistema de Roteamento Programado de Coletas

## Objetivo

Este documento apresenta os diagramas UML do sistema de roteamento programado de coletas, demonstrando a arquitetura, estrutura de classes e fluxos do sistema.

## Visão Geral

Os diagramas UML fornecem uma representação visual da arquitetura do sistema, facilitando o entendimento da estrutura de dados, relacionamentos entre entidades e fluxos de processamento.

## Conceitos UML Explicados

UML (Unified Modeling Language) é uma linguagem de modelagem visual que permite representar sistemas de software através de diagramas padronizados. Os principais tipos de diagramas utilizados neste projeto são:

- **Diagrama de Classes**: Mostra a estrutura estática do sistema
- **Diagrama de Estados**: Representa o comportamento dinâmico das entidades
- **Diagrama de Sequência**: Ilustra a interação entre componentes
- **Diagrama de Atividades**: Demonstra fluxos de trabalho e processos

## Benefícios da Modelagem UML

- **Comunicação**: Facilita a comunicação entre stakeholders
- **Documentação**: Serve como documentação técnica do sistema
- **Análise**: Permite análise e validação de requisitos
- **Implementação**: Guia o desenvolvimento do código
- **Manutenção**: Facilita futuras modificações no sistema

## Diagrama de Classes

O diagrama de classes representa a estrutura estática do sistema, mostrando as entidades principais, seus atributos, métodos e relacionamentos.

### Entidades Principais

#### Usuario

```java
@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nome;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String senha;
    
    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario;
    
    @Column(nullable = false)
    private boolean ativo = true;
    
    @CreatedDate
    private LocalDateTime dataCriacao;
    
    @LastModifiedDate
    private LocalDateTime dataAtualizacao;
    
    // Relacionamentos
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Coleta> coletas;
    
    // Getters e Setters
    // Métodos de negócio
}
```

#### Material

```java
@Entity
@Table(name = "materiais")
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nome;
    
    @Column(length = 500)
    private String descricao;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precoPorKg;
    
    @Enumerated(EnumType.STRING)
    private CategoriaMaterial categoria;
    
    @Column(nullable = false)
    private boolean ativo = true;
    
    @CreatedDate
    private LocalDateTime dataCriacao;
    
    @LastModifiedDate
    private LocalDateTime dataAtualizacao;
    
    // Relacionamentos
    @OneToMany(mappedBy = "material")
    private List<Coleta> coletas;
    
    // Getters e Setters
    // Métodos de negócio
}
```

#### Coleta

```java
@Entity
@Table(name = "coletas")
public class Coleta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String endereco;
    
    @Column(precision = 10, scale = 8)
    private BigDecimal latitude;
    
    @Column(precision = 11, scale = 8)
    private BigDecimal longitude;
    
    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal pesoEstimado;
    
    @Enumerated(EnumType.STRING)
    private StatusColeta status;
    
    @Column(length = 500)
    private String observacoes;
    
    @Column(nullable = false)
    private LocalDateTime dataAgendamento;
    
    @CreatedDate
    private LocalDateTime dataCriacao;
    
    @LastModifiedDate
    private LocalDateTime dataAtualizacao;
    
    // Relacionamentos
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;
    
    @OneToMany(mappedBy = "coleta", cascade = CascadeType.ALL)
    private List<ColetaRota> rotas;
    
    // Getters e Setters
    // Métodos de negócio
}
```

#### Rota

```java
@Entity
@Table(name = "rotas")
public class Rota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nome;
    
    @Column(length = 500)
    private String descricao;
    
    @Column(nullable = false)
    private LocalDateTime dataInicio;
    
    @Column(nullable = false)
    private LocalDateTime dataFim;
    
    @Enumerated(EnumType.STRING)
    private StatusRota status;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal distanciaTotal;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal tempoEstimado;
    
    @CreatedDate
    private LocalDateTime dataCriacao;
    
    @LastModifiedDate
    private LocalDateTime dataAtualizacao;
    
    // Relacionamentos
    @OneToMany(mappedBy = "rota", cascade = CascadeType.ALL)
    private List<ColetaRota> coletasRota;
    
    // Getters e Setters
    // Métodos de negócio
}
```

#### ColetaRota (Tabela de Junção)

```java
@Entity
@Table(name = "coleta_rota")
public class ColetaRota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Integer ordem;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal distanciaAnterior;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal tempoAnterior;
    
    @CreatedDate
    private LocalDateTime dataCriacao;
    
    // Relacionamentos
    @ManyToOne
    @JoinColumn(name = "coleta_id", nullable = false)
    private Coleta coleta;
    
    @ManyToOne
    @JoinColumn(name = "rota_id", nullable = false)
    private Rota rota;
    
    // Getters e Setters
}
```

### Enums do Sistema

#### TipoUsuario

```java
public enum TipoUsuario {
    CIDADAO("Cidadão"),
    COLETOR("Coletor"),
    ADMINISTRADOR("Administrador");
    
    private final String descricao;
    
    TipoUsuario(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}
```

#### StatusColeta

```java
public enum StatusColeta {
    PENDENTE("Pendente"),
    AGENDADA("Agendada"),
    EM_COLETA("Em Coleta"),
    CONCLUIDA("Concluída"),
    CANCELADA("Cancelada");
    
    private final String descricao;
    
    StatusColeta(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}
```

#### StatusRota

```java
public enum StatusRota {
    PLANEJADA("Planejada"),
    EM_EXECUCAO("Em Execução"),
    CONCLUIDA("Concluída"),
    CANCELADA("Cancelada");
    
    private final String descricao;
    
    StatusRota(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}
```

#### CategoriaMaterial

```java
public enum CategoriaMaterial {
    PAPEL("Papel"),
    PLASTICO("Plástico"),
    METAL("Metal"),
    VIDRO("Vidro"),
    ORGANICO("Orgânico"),
    ELETRONICO("Eletrônico"),
    OUTROS("Outros");
    
    private final String descricao;
    
    CategoriaMaterial(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}
```

### Relacionamentos entre Entidades

#### Relacionamento Usuario-Coleta

- **Tipo**: One-to-Many (Um usuário pode ter múltiplas coletas)
- **Mapeamento**: `@OneToMany` em Usuario, `@ManyToOne` em Coleta
- **Cascade**: ALL (operações em cascata para coletas)

#### Relacionamento Material-Coleta

- **Tipo**: One-to-Many (Um material pode estar em múltiplas coletas)
- **Mapeamento**: `@OneToMany` em Material, `@ManyToOne` em Coleta
- **Cascade**: NONE (sem operações em cascata)

#### Relacionamento Rota-Coleta (via ColetaRota)

- **Tipo**: Many-to-Many (Múltiplas rotas podem ter múltiplas coletas)
- **Mapeamento**: Tabela de junção `ColetaRota`
- **Cascade**: ALL para ColetaRota

### Padrões de Projeto Aplicados

#### Repository Pattern

```java
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Usuario> findByTipoUsuario(TipoUsuario tipoUsuario);
    List<Usuario> findByAtivo(boolean ativo);
}
```

#### Service Layer Pattern

```java
@Service
@Transactional
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    
    public UsuarioDTO criarUsuario(UsuarioDTO usuarioDTO) {
        // Lógica de negócio
    }
    
    public UsuarioDTO atualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        // Lógica de negócio
    }
}
```

#### DTO Pattern

```java
public class UsuarioDTO {
    private Long id;
    private String nome;
    private String email;
    private TipoUsuario tipoUsuario;
    private boolean ativo;
    private LocalDateTime dataCriacao;
    
    // Construtores, getters e setters
    // Métodos de conversão toEntity() e fromEntity()
}
```

## Diagrama de Estados

O diagrama de estados representa o comportamento dinâmico das entidades do sistema, mostrando as transições entre diferentes estados.

### Estados da Coleta

#### Estado Inicial: PENDENTE

- **Descrição**: Coleta criada mas ainda não processada
- **Ações permitidas**: Agendar, Cancelar
- **Transições**:
  - Agendar → AGENDADA
  - Cancelar → CANCELADA

#### Estado: AGENDADA

- **Descrição**: Coleta agendada para coleta
- **Ações permitidas**: Iniciar coleta, Cancelar, Reagendar
- **Transições**:
  - Iniciar coleta → EM_COLETA
  - Cancelar → CANCELADA
  - Reagendar → AGENDADA (novo horário)

#### Estado: EM_COLETA

- **Descrição**: Coleta em processo de execução
- **Ações permitidas**: Concluir, Cancelar
- **Transições**:
  - Concluir → CONCLUIDA
  - Cancelar → CANCELADA

#### Estado: CONCLUIDA

- **Descrição**: Coleta finalizada com sucesso
- **Ações permitidas**: Nenhuma (estado final)
- **Transições**: Nenhuma

#### Estado: CANCELADA

- **Descrição**: Coleta cancelada
- **Ações permitidas**: Nenhuma (estado final)
- **Transições**: Nenhuma

### Estados da Rota

#### Estado Inicial: PLANEJADA

- **Descrição**: Rota criada mas não iniciada
- **Ações permitidas**: Iniciar execução, Cancelar
- **Transições**:
  - Iniciar execução → EM_EXECUCAO
  - Cancelar → CANCELADA

#### Estado: EM_EXECUCAO

- **Descrição**: Rota em processo de execução
- **Ações permitidas**: Concluir, Cancelar
- **Transições**:
  - Concluir → CONCLUIDA
  - Cancelar → CANCELADA

#### Estado: CONCLUIDA

- **Descrição**: Rota finalizada com sucesso
- **Ações permitidas**: Nenhuma (estado final)
- **Transições**: Nenhuma

#### Estado: CANCELADA

- **Descrição**: Rota cancelada
- **Ações permitidas**: Nenhuma (estado final)
- **Transições**: Nenhuma

### Transições de Estado

#### Triggers de Transição

- **Ações do usuário**: Agendar, iniciar, concluir, cancelar
- **Tempo**: Agendamentos automáticos, timeouts
- **Sistema**: Validações automáticas, regras de negócio

#### Validações de Transição

- **Regras de negócio**: Verificações antes de permitir transição
- **Permissões**: Controle de acesso baseado em tipo de usuário
- **Consistência**: Validação de dados antes da mudança de estado

## Conceitos de Modelagem Aplicados

### Abstração

- **Entidades**: Representam conceitos do mundo real
- **Atributos**: Características essenciais das entidades
- **Métodos**: Comportamentos e operações das entidades

### Encapsulamento

- **Visibilidade**: Atributos privados, métodos públicos
- **Validação**: Controle de acesso e validação de dados
- **Consistência**: Manutenção da integridade dos dados

### Herança

- **Classes base**: Funcionalidades comuns compartilhadas
- **Especialização**: Classes específicas para diferentes tipos
- **Reutilização**: Código compartilhado entre entidades

### Polimorfismo

- **Interfaces**: Contratos para diferentes implementações
- **Métodos**: Comportamentos específicos por tipo
- **Flexibilidade**: Adaptação a diferentes contextos

## Benefícios da Modelagem UML

### Para Desenvolvedores

- **Clareza**: Entendimento claro da estrutura do sistema
- **Implementação**: Guia para desenvolvimento do código
- **Manutenção**: Facilita modificações futuras

### Para Arquitetos

- **Análise**: Validação de requisitos e design
- **Documentação**: Registro da arquitetura do sistema
- **Comunicação**: Compartilhamento de visão técnica

### Para Stakeholders

- **Compreensão**: Visão clara do sistema proposto
- **Validação**: Confirmação de requisitos atendidos
- **Decisões**: Base para tomada de decisões técnicas

### Para o Projeto

- **Qualidade**: Melhora a qualidade do código desenvolvido
- **Consistência**: Mantém consistência na implementação
- **Escalabilidade**: Facilita crescimento futuro do sistema

## Conclusão

Os diagramas UML apresentados fornecem uma visão completa e estruturada do sistema de roteamento programado de coletas, demonstrando:

- **Arquitetura robusta** com separação clara de responsabilidades
- **Modelagem de dados** bem estruturada e normalizada
- **Fluxos de trabalho** claros e bem definidos
- **Padrões de projeto** aplicados consistentemente
- **Documentação técnica** completa e atualizada

Esta modelagem serve como base sólida para o desenvolvimento, manutenção e evolução do sistema, garantindo que todos os stakeholders tenham uma compreensão clara da arquitetura e funcionamento do sistema.
