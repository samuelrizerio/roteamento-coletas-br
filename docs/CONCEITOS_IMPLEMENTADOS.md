# Conceitos Implementados no Projeto Roteamento de Coletas

Este documento detalha os conceitos fundamentais de programação, arquitetura de software e tecnologias implementados no projeto, servindo como guia didático completo.

## 📚 Índice

1. [Conceitos Básicos de Java](#conceitos-básicos-de-java)
2. [Programação Orientada a Objetos (POO)](#programação-orientada-a-objetos-poo)
3. [Spring Boot Framework](#spring-boot-framework)
4. [Banco de Dados e JPA](#banco-de-dados-e-jpa)
5. [APIs REST e JSON](#apis-rest-e-json)
6. [Validação e Tratamento de Erros](#validação-e-tratamento-de-erros)
7. [Algoritmos e Lógica de Negócio](#algoritmos-e-lógica-de-negócio)
8. [Integração de APIs Externas](#integração-de-apis-externas)
9. [Testes e Qualidade](#testes-e-qualidade)
10. [DevOps e Automação](#devops-e-automação)

---

## 🍃 Conceitos Básicos de Java

### Variáveis e Tipos Primitivos

```java
// CONCEITO DIDÁTICO - Tipos Primitivos:
// int: números inteiros de 32 bits
int quantidadeColetas = 10;

// double: números decimais de 64 bits
double distancia = 25.5;

// boolean: valores verdadeiro/falso
boolean ativo = true;

// String: sequência de caracteres (não é primitivo, mas muito usado)
String nome = "João Silva";
```

### Operadores

```java
// CONCEITO DIDÁTICO - Operadores Aritméticos:
int a = 10, b = 3;
int soma = a + b;        // 13
int subtracao = a - b;   // 7
int multiplicacao = a * b; // 30
int divisao = a / b;     // 3
int resto = a % b;       // 1

// CONCEITO DIDÁTICO - Operadores de Comparação:
boolean igual = (a == b);        // false
boolean diferente = (a != b);    // true
boolean maior = (a > b);         // true
boolean menor = (a < b);         // false
boolean maiorIgual = (a >= b);   // true
boolean menorIgual = (a <= b);   // false

// CONCEITO DIDÁTICO - Operadores Lógicos:
boolean and = (a > 5) && (b < 5);  // true
boolean or = (a < 5) || (b > 5);   // false
boolean not = !(a == b);           // true
```

### Estruturas de Controle

```java
// CONCEITO DIDÁTICO - Estrutura if-else:
if (pesoEstimado > 100) {
    System.out.println("Coleta pesada");
} else if (pesoEstimado > 50) {
    System.out.println("Coleta média");
} else {
    System.out.println("Coleta leve");
}

// CONCEITO DIDÁTICO - Estrutura switch:
switch (status) {
    case "PENDENTE":
        System.out.println("Aguardando processamento");
        break;
    case "AGENDADA":
        System.out.println("Coleta agendada");
        break;
    case "CONCLUIDA":
        System.out.println("Coleta finalizada");
        break;
    default:
        System.out.println("Status desconhecido");
}

// CONCEITO DIDÁTICO - Loops:
// For tradicional
for (int i = 0; i < coletas.size(); i++) {
    Coleta coleta = coletas.get(i);
    processarColeta(coleta);
}

// For-each (enhanced for)
for (Coleta coleta : coletas) {
    processarColeta(coleta);
}

// While
int i = 0;
while (i < coletas.size()) {
    processarColeta(coletas.get(i));
    i++;
}

// Do-while
int j = 0;
do {
    processarColeta(coletas.get(j));
    j++;
} while (j < coletas.size());
```

---

## 🏗️ Programação Orientada a Objetos (POO)

### Classes e Objetos

```java
// CONCEITO DIDÁTICO - Classe:
// Uma classe é um modelo/template para criar objetos
public class Usuario {
    // Atributos (características do objeto)
    private String nome;
    private String email;
    private String senha;
    
    // Construtor (método especial para criar objetos)
    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }
    
    // Métodos (comportamentos do objeto)
    public void alterarSenha(String novaSenha) {
        this.senha = novaSenha;
    }
    
    public boolean validarEmail() {
        return email.contains("@");
    }
}

// CONCEITO DIDÁTICO - Objeto:
// Um objeto é uma instância de uma classe
Usuario joao = new Usuario("João", "joao@email.com", "123456");
joao.alterarSenha("654321");
```

### Encapsulamento

```java
// CONCEITO DIDÁTICO - Encapsulamento:
// Protege dados e controla acesso através de métodos
public class Material {
    // Atributos privados (não acessíveis externamente)
    private String nome;
    private BigDecimal preco;
    
    // Métodos públicos para acessar/modificar dados
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        if (nome != null && !nome.trim().isEmpty()) {
            this.nome = nome;
        }
    }
    
    public BigDecimal getPreco() {
        return preco;
    }
    
    public void setPreco(BigDecimal preco) {
        if (preco != null && preco.compareTo(BigDecimal.ZERO) > 0) {
            this.preco = preco;
        }
    }
}
```

### Herança

```java
// CONCEITO DIDÁTICO - Herança:
// Uma classe herda características de outra classe
public class Pessoa {
    protected String nome;
    protected String email;
    
    public void exibirInformacoes() {
        System.out.println("Nome: " + nome + ", Email: " + email);
    }
}

// Usuario herda de Pessoa
public class Usuario extends Pessoa {
    private String senha;
    private TipoUsuario tipoUsuario;
    
    // Método específico de Usuario
    public void fazerLogin() {
        System.out.println("Usuário " + nome + " fez login");
    }
    
    // Sobrescreve método da classe pai
    @Override
    public void exibirInformacoes() {
        super.exibirInformacoes(); // Chama método da classe pai
        System.out.println("Tipo: " + tipoUsuario);
    }
}
```

### Polimorfismo

```java
// CONCEITO DIDÁTICO - Polimorfismo:
// Mesmo método pode ter comportamentos diferentes
public interface Calculavel {
    BigDecimal calcularValor();
}

public class Material implements Calculavel {
    private BigDecimal precoPorKg;
    private BigDecimal peso;
    
    @Override
    public BigDecimal calcularValor() {
        return precoPorKg.multiply(peso);
    }
}

public class Coleta implements Calculavel {
    private BigDecimal pesoEstimado;
    private BigDecimal taxaColeta;
    
    @Override
    public BigDecimal calcularValor() {
        return pesoEstimado.multiply(taxaColeta);
    }
}

// Uso do polimorfismo
List<Calculavel> itens = Arrays.asList(new Material(), new Coleta());
for (Calculavel item : itens) {
    BigDecimal valor = item.calcularValor(); // Comportamento diferente para cada tipo
}
```

---

## 🌱 Spring Boot Framework

### Injeção de Dependência

```java
// CONCEITO DIDÁTICO - Injeção de Dependência:
// O Spring gerencia a criação e injeção de objetos automaticamente

@Service // Marca como serviço gerenciado pelo Spring
public class UsuarioService {
    
    // Spring injeta automaticamente o repositório
    private final UsuarioRepository usuarioRepository;
    
    // Construtor com injeção de dependência
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    
    public UsuarioDTO criarUsuario(UsuarioDTO usuarioDTO) {
        // Lógica de negócio
        return usuarioDTO;
    }
}

@RestController // Marca como controlador REST
public class UsuarioController {
    
    // Spring injeta automaticamente o serviço
    private final UsuarioService usuarioService;
    
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
}
```

### Anotações Spring

```java
// CONCEITO DIDÁTICO - Anotações Spring:
// @Component: marca classe como componente gerenciado pelo Spring
@Component
public class EmailService {
    public void enviarEmail(String destinatario, String assunto) {
        // Lógica de envio de email
    }
}

// @Service: especialização de @Component para camada de serviço
@Service
public class MaterialService {
    // Lógica de negócio
}

// @Repository: especialização de @Component para camada de dados
@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    // Métodos de acesso a dados
}

// @Controller: especialização de @Component para camada de apresentação
@Controller
public class HomeController {
    // Controla requisições web
}

// @RestController: combina @Controller e @ResponseBody
@RestController
public class UsuarioController {
    // Controla requisições REST
}
```

---

## 🗄️ Banco de Dados e JPA

### Entidades JPA

```java
// CONCEITO DIDÁTICO - Entidade JPA:
// Representa uma tabela no banco de dados
@Entity // Marca como entidade JPA
@Table(name = "usuarios") // Nome da tabela
public class Usuario {
    
    @Id // Chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremento
    private Long id;
    
    @Column(name = "nome", nullable = false, length = 100) // Coluna obrigatória
    private String nome;
    
    @Column(unique = true) // Valor único
    private String email;
    
    @Enumerated(EnumType.STRING) // Enum como string
    private TipoUsuario tipoUsuario;
    
    @CreatedDate // Data de criação automática
    private LocalDateTime dataCriacao;
    
    @LastModifiedDate // Data de atualização automática
    private LocalDateTime dataAtualizacao;
}
```

### Relacionamentos

```java
// CONCEITO DIDÁTICO - Relacionamentos JPA:

// Relacionamento One-to-Many
@Entity
public class Usuario {
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Coleta> coletas;
}

@Entity
public class Coleta {
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}

// Relacionamento Many-to-Many
@Entity
public class Rota {
    @ManyToMany
    @JoinTable(
        name = "rota_coleta",
        joinColumns = @JoinColumn(name = "rota_id"),
        inverseJoinColumns = @JoinColumn(name = "coleta_id")
    )
    private List<Coleta> coletas;
}
```

### Repositórios Spring Data

```java
// CONCEITO DIDÁTICO - Spring Data JPA:
// Interface que herda métodos CRUD automáticos
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Query methods por convenção de nomenclatura
    List<Usuario> findByTipoUsuario(TipoUsuario tipoUsuario);
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
    long countByTipoUsuario(TipoUsuario tipoUsuario);
    
    // JPQL personalizado
    @Query("SELECT u FROM Usuario u WHERE u.nome LIKE %:nome%")
    List<Usuario> buscarPorNomeContendo(@Param("nome") String nome);
    
    // Consulta com agregação
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.tipoUsuario = :tipo")
    long contarPorTipo(@Param("tipo") TipoUsuario tipo);
}
```

---

## 🌐 APIs REST e JSON

### Endpoints REST

```java
// CONCEITO DIDÁTICO - Endpoints REST:
// Seguem convenções HTTP para operações CRUD

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    
    // CREATE - POST
    @PostMapping
    public ResponseEntity<UsuarioDTO> criar(@RequestBody UsuarioDTO usuario) {
        // Cria novo usuário
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }
    
    // READ - GET
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable Long id) {
        // Busca usuário por ID
        return ResponseEntity.ok(usuario);
    }
    
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarTodos() {
        // Lista todos os usuários
        return ResponseEntity.ok(usuarios);
    }
    
    // UPDATE - PUT
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> atualizar(@PathVariable Long id, 
                                               @RequestBody UsuarioDTO usuario) {
        // Atualiza usuário existente
        return ResponseEntity.ok(usuario);
    }
    
    // DELETE - DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        // Exclui usuário
        return ResponseEntity.noContent().build();
    }
}
```

### JSON e Serialização

```java
// CONCEITO DIDÁTICO - JSON:
// Formato de troca de dados entre cliente e servidor

// Exemplo de JSON de entrada (request body):
{
    "nome": "João Silva",
    "email": "joao@email.com",
    "telefone": "(11) 99999-9999",
    "tipoUsuario": "CIDADAO"
}

// Exemplo de JSON de saída (response body):
{
    "id": 1,
    "nome": "João Silva",
    "email": "joao@email.com",
    "telefone": "(11) 99999-9999",
    "tipoUsuario": "CIDADAO",
    "dataCriacao": "2024-01-15T10:30:00",
    "ativo": true
}

// Anotações para controle de serialização
public class UsuarioDTO {
    @JsonProperty("nome_completo") // Nome customizado no JSON
    private String nome;
    
    @JsonIgnore // Campo não aparece no JSON
    private String senha;
    
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm") // Formato de data
    private LocalDateTime dataCriacao;
}
```

---

## ✅ Validação e Tratamento de Erros

### Bean Validation

```java
// CONCEITO DIDÁTICO - Bean Validation:
// Validação automática de dados de entrada

public class UsuarioDTO {
    @NotNull(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;
    
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ter formato válido")
    private String email;
    
    @Pattern(regexp = "^\\(\\d{2}\\) \\d{5}-\\d{4}$", 
             message = "Telefone deve ter formato (99) 99999-9999")
    private String telefone;
    
    @Min(value = 18, message = "Idade deve ser maior que 18")
    @Max(value = 120, message = "Idade deve ser menor que 120")
    private Integer idade;
    
    @DecimalMin(value = "0.01", message = "Peso deve ser maior que zero")
    private BigDecimal peso;
}
```

### Tratamento de Exceções

```java
// CONCEITO DIDÁTICO - Exceções Customizadas:
public class UsuarioNaoEncontradoException extends RuntimeException {
    public UsuarioNaoEncontradoException(String message) {
        super(message);
    }
    
    public UsuarioNaoEncontradoException(Long id) {
        super("Usuário com ID " + id + " não encontrado");
    }
}

// CONCEITO DIDÁTICO - Tratamento Global de Exceções:
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleUsuarioNaoEncontrado(
            UsuarioNaoEncontradoException e) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            e.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidacao(
            MethodArgumentNotValidException e) {
        List<String> erros = e.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.toList());
        
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Dados inválidos",
            LocalDateTime.now(),
            erros
        );
        return ResponseEntity.badRequest().body(error);
    }
}
```

---

## 🧮 Algoritmos e Lógica de Negócio

### Algoritmo do Caixeiro Viajante (TSP)

```java
// CONCEITO DIDÁTICO - Algoritmo TSP (Nearest Neighbor):
// Encontra rota otimizada visitando sempre o ponto mais próximo

public List<Coleta> aplicarAlgoritmoTSP(List<Coleta> coletas) {
    if (coletas.size() <= 1) {
        return coletas;
    }
    
    List<Coleta> rotaOtimizada = new ArrayList<>();
    Set<Coleta> coletasNaoVisitadas = new HashSet<>(coletas);
    
    // Ponto de partida (coleta mais central)
    Coleta coletaAtual = encontrarColetaMaisCentral(coletas);
    rotaOtimizada.add(coletaAtual);
    coletasNaoVisitadas.remove(coletaAtual);
    
    // Sempre vai para a coleta mais próxima não visitada
    while (!coletasNaoVisitadas.isEmpty()) {
        Coleta proximaColeta = encontrarColetaMaisProxima(coletaAtual, coletasNaoVisitadas);
        rotaOtimizada.add(proximaColeta);
        coletasNaoVisitadas.remove(proximaColeta);
        coletaAtual = proximaColeta;
    }
    
    return rotaOtimizada;
}

// CONCEITO DIDÁTICO - Cálculo de Distância (Fórmula de Haversine):
public double calcularDistancia(Coleta coleta1, Coleta coleta2) {
    double lat1 = coleta1.getLatitude().doubleValue();
    double lon1 = coleta1.getLongitude().doubleValue();
    double lat2 = coleta2.getLatitude().doubleValue();
    double lon2 = coleta2.getLongitude().doubleValue();
    
    double R = 6371; // Raio da Terra em km
    double dLat = Math.toRadians(lat2 - lat1);
    double dLon = Math.toRadians(lon2 - lon1);
    
    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
               Math.sin(dLon / 2) * Math.sin(dLon / 2);
    
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    
    return R * c;
}
```

### Streams e Programação Funcional

```java
// CONCEITO DIDÁTICO - Streams:
// Processamento funcional de coleções

public class MaterialService {
    
    // Filtrar materiais ativos
    public List<MaterialDTO> listarMateriaisAtivos() {
        return materialRepository.findAll()
            .stream()
            .filter(Material::getAtivo) // Filtra apenas ativos
            .map(MaterialDTO::fromEntity) // Converte para DTO
            .collect(Collectors.toList()); // Coleta resultado
    }
    
    // Calcular estatísticas
    public MaterialEstatisticasDTO calcularEstatisticas() {
        List<Material> materiais = materialRepository.findByAtivo(true);
        
        BigDecimal precoMedio = materiais.stream()
            .map(Material::getPrecoPorKg)
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(BigDecimal.valueOf(materiais.size()), 2, BigDecimal.ROUND_HALF_UP);
        
        long totalMateriais = materiais.size();
        long categoriasDiferentes = materiais.stream()
            .map(Material::getCategoria)
            .distinct()
            .count();
        
        return MaterialEstatisticasDTO.builder()
            .totalMateriais(totalMateriais)
            .categoriasDiferentes(categoriasDiferentes)
            .precoMedio(precoMedio)
            .build();
    }
}
```

---

## 🔗 Integração de APIs Externas

### RestTemplate

```java
// CONCEITO DIDÁTICO - RestTemplate:
// Cliente HTTP para consumir APIs REST externas

@Service
public class RoteamentoService {
    
    private final RestTemplate restTemplate;
    
    public Map<String, Object> obterInformacoesRota(String origem, String destino) {
        try {
            String url = String.format(
                "%s/directions/json?origin=%s&destination=%s&key=%s",
                mapsApiUrl, origem, destino, mapsApiKey
            );
            
            // Faz requisição GET para API externa
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            return response;
        } catch (Exception e) {
            // Fallback se API externa falhar
            return criarInformacoesRotaMock(origem, destino);
        }
    }
}

// CONCEITO DIDÁTICO - Configuração do RestTemplate:
@Configuration
public class RestTemplateConfig {
    
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        
        // Configura timeouts
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000); // 5 segundos
        factory.setReadTimeout(10000);   // 10 segundos
        
        restTemplate.setRequestFactory(factory);
        return restTemplate;
    }
}
```

---

## 🧪 Testes e Qualidade

### Testes Unitários

```java
// CONCEITO DIDÁTICO - Testes Unitários:
// Testam unidades isoladas de código

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
    
    @Mock
    private UsuarioRepository usuarioRepository;
    
    @InjectMocks
    private UsuarioService usuarioService;
    
    @Test
    @DisplayName("Deve criar usuário com dados válidos")
    void deveCriarUsuarioComDadosValidos() {
        // Arrange (Preparar)
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNome("João Silva");
        usuarioDTO.setEmail("joao@email.com");
        
        Usuario usuarioSalvo = new Usuario();
        usuarioSalvo.setId(1L);
        usuarioSalvo.setNome("João Silva");
        
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvo);
        
        // Act (Executar)
        UsuarioDTO resultado = usuarioService.criarUsuario(usuarioDTO);
        
        // Assert (Verificar)
        assertThat(resultado).isNotNull();
        assertThat(resultado.getNome()).isEqualTo("João Silva");
        verify(usuarioRepository).save(any(Usuario.class));
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando email já existe")
    void deveLancarExcecaoQuandoEmailJaExiste() {
        // Arrange
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setEmail("joao@email.com");
        
        when(usuarioRepository.existsByEmail("joao@email.com")).thenReturn(true);
        
        // Act & Assert
        assertThatThrownBy(() -> usuarioService.criarUsuario(usuarioDTO))
            .isInstanceOf(EmailJaExisteException.class)
            .hasMessageContaining("Email já existe");
    }
}
```

### Testes de Integração

```java
// CONCEITO DIDÁTICO - Testes de Integração:
// Testam integração entre componentes

@SpringBootTest
@AutoConfigureTestDatabase
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class UsuarioControllerIntegrationTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Test
    @DisplayName("Deve criar usuário via API REST")
    void deveCriarUsuarioViaApi() {
        // Arrange
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNome("João Silva");
        usuarioDTO.setEmail("joao@email.com");
        usuarioDTO.setSenha("123456");
        
        // Act
        ResponseEntity<UsuarioDTO> response = restTemplate.postForEntity(
            "/api/usuarios",
            usuarioDTO,
            UsuarioDTO.class
        );
        
        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getNome()).isEqualTo("João Silva");
        
        // Verifica se foi salvo no banco
        assertThat(usuarioRepository.findByEmail("joao@email.com")).isPresent();
    }
}
```

---

## 🚀 DevOps e Automação

### Scripts de Automação

```bash
#!/bin/bash
# CONCEITO DIDÁTICO - Scripts de Automação:
# Automatizam tarefas repetitivas

# Configurações
BASE_URL="http://localhost:8080"
API_BASE="$BASE_URL/api"

# Função para fazer requisições HTTP
make_request() {
    local method=$1
    local endpoint=$2
    local data=$3
    local expected_status=$4
    local description=$5
    
    echo "Testando: $description"
    
    local response
    if [ -n "$data" ]; then
        response=$(curl -s -w "\n%{http_code}" -X "$method" \
            -H "Content-Type: application/json" \
            -d "$data" \
            "$API_BASE$endpoint")
    else
        response=$(curl -s -w "\n%{http_code}" -X "$method" \
            "$API_BASE$endpoint")
    fi
    
    local status_code=$(echo "$response" | tail -n1)
    local response_body=$(echo "$response" | head -n -1)
    
    if [ "$status_code" -eq "$expected_status" ]; then
        echo "✓ $description (Status: $status_code)"
        echo "$response_body" | jq '.' 2>/dev/null || echo "$response_body"
    else
        echo "✗ $description (Esperado: $expected_status, Obtido: $status_code)"
        echo "$response_body"
        return 1
    fi
}

# Executar testes
echo "Iniciando testes da API"
make_request "GET" "/usuarios" "" 200 "Listar usuários"
make_request "POST" "/usuarios" '{"nome":"João","email":"joao@email.com"}' 201 "Criar usuário"
```

### Docker e Containerização

```dockerfile
# CONCEITO DIDÁTICO - Dockerfile:
# Define como construir imagem da aplicação

# Imagem base
FROM openjdk:17-jdk-slim

# Define diretório de trabalho
WORKDIR /app

# Copia arquivo JAR da aplicação
COPY target/roteamento-coletas-*.jar app.jar

# Expõe porta da aplicação
EXPOSE 8080

# Comando para executar aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
```

```yaml
# CONCEITO DIDÁTICO - Docker Compose:
# Orquestra múltiplos containers

version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=docker
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/roteamento
    depends_on:
      - db
  
  db:
    image: postgres:15
    environment:
      - POSTGRES_DB=roteamento
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=postgres
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

volumes:
  postgres_data:
```

---

## 📊 Monitoramento e Observabilidade

### Logs Estruturados

```java
// CONCEITO DIDÁTICO - Logs Estruturados:
// Logs organizados para facilitar análise

@Slf4j
@Service
public class UsuarioService {
    
    public UsuarioDTO criarUsuario(UsuarioDTO usuarioDTO) {
        log.info("Criando usuário: {}", usuarioDTO.getEmail());
        
        try {
            // Validação
            if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
                log.warn("Tentativa de criar usuário com email já existente: {}", usuarioDTO.getEmail());
                throw new EmailJaExisteException("Email já existe");
            }
            
            // Criação
            Usuario usuario = usuarioDTO.toEntity();
            Usuario usuarioSalvo = usuarioRepository.save(usuario);
            
            log.info("Usuário criado com sucesso. ID: {}", usuarioSalvo.getId());
            
            return UsuarioDTO.fromEntity(usuarioSalvo);
            
        } catch (Exception e) {
            log.error("Erro ao criar usuário: {}", e.getMessage(), e);
            throw e;
        }
    }
}
```

### Métricas e Health Checks

```java
// CONCEITO DIDÁTICO - Health Checks:
// Verificam saúde da aplicação

@Component
public class DatabaseHealthIndicator implements HealthIndicator {
    
    private final DataSource dataSource;
    
    public DatabaseHealthIndicator(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public Health health() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(1000)) {
                return Health.up()
                    .withDetail("database", "PostgreSQL")
                    .withDetail("status", "Connected")
                    .build();
            } else {
                return Health.down()
                    .withDetail("database", "PostgreSQL")
                    .withDetail("status", "Connection failed")
                    .build();
            }
        } catch (SQLException e) {
            return Health.down()
                .withDetail("database", "PostgreSQL")
                .withDetail("error", e.getMessage())
                .build();
        }
    }
}
```

---

## 🔐 Segurança

### Validação de Entrada

```java
// CONCEITO DIDÁTICO - Validação de Segurança:
// Previne ataques e dados maliciosos

@RestController
public class UsuarioController {
    
    @PostMapping("/usuarios")
    public ResponseEntity<UsuarioDTO> criarUsuario(
            @Valid @RequestBody UsuarioDTO usuarioDTO) {
        // @Valid ativa validações automáticas
        return ResponseEntity.ok(usuarioService.criarUsuario(usuarioDTO));
    }
}

public class UsuarioDTO {
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s]+$", message = "Nome deve conter apenas letras")
    private String nome;
    
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ter formato válido")
    private String email;
    
    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", 
             message = "Senha deve conter letra maiúscula, minúscula e número")
    private String senha;
}
```

### Sanitização de Dados

```java
// CONCEITO DIDÁTICO - Sanitização:
// Remove caracteres perigosos dos dados

@Component
public class DataSanitizer {
    
    public String sanitizeString(String input) {
        if (input == null) {
            return null;
        }
        
        // Remove caracteres de controle
        String sanitized = input.replaceAll("[\\x00-\\x1F\\x7F]", "");
        
        // Remove tags HTML
        sanitized = sanitized.replaceAll("<[^>]*>", "");
        
        // Remove caracteres especiais perigosos
        sanitized = sanitized.replaceAll("[<>\"'&]", "");
        
        return sanitized.trim();
    }
    
    public String sanitizeEmail(String email) {
        if (email == null) {
            return null;
        }
        
        // Converte para minúsculas
        email = email.toLowerCase().trim();
        
        // Remove espaços
        email = email.replaceAll("\\s", "");
        
        return email;
    }
}
```

---

## 📈 Performance e Otimização

### Cache

```java
// CONCEITO DIDÁTICO - Cache:
// Armazena dados frequentemente acessados

@Service
public class MaterialService {
    
    @Cacheable("materiais")
    public List<MaterialDTO> listarMateriaisAtivos() {
        log.debug("Buscando materiais ativos no banco de dados");
        return materialRepository.findByAtivo(true)
            .stream()
            .map(MaterialDTO::fromEntity)
            .collect(Collectors.toList());
    }
    
    @CacheEvict("materiais")
    public MaterialDTO criarMaterial(MaterialDTO materialDTO) {
        // Lógica de criação
        // Cache será limpo automaticamente
        return materialDTO;
    }
}
```

### Paginação

```java
// CONCEITO DIDÁTICO - Paginação:
// Divide resultados em páginas para melhor performance

@RestController
public class UsuarioController {
    
    @GetMapping("/usuarios")
    public ResponseEntity<Page<UsuarioDTO>> listarUsuarios(
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        
        Page<Usuario> usuarios = usuarioRepository.findAll(pageable);
        Page<UsuarioDTO> usuariosDTO = usuarios.map(UsuarioDTO::fromEntity);
        
        return ResponseEntity.ok(usuariosDTO);
    }
}
```

---

## 🎯 Conclusão

Este projeto implementa uma ampla gama de conceitos fundamentais de programação e desenvolvimento de software, servindo como um excelente recurso didático para:

- **Estudantes**: Aprendem conceitos práticos de Java, Spring Boot e desenvolvimento web
- **Desenvolvedores**: Veem exemplos de boas práticas e padrões de projeto
- **Arquitetos**: Analisam decisões de arquitetura e design de sistemas
- **DevOps**: Entendem automação, containerização e monitoramento

O projeto demonstra como conceitos teóricos se aplicam em um sistema real, fornecendo uma base sólida para o desenvolvimento de aplicações empresariais modernas.

---

## 📚 Recursos Adicionais

- [Documentação Spring Boot](https://spring.io/projects/spring-boot)
- [Java Documentation](https://docs.oracle.com/en/java/)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [REST API Design](https://restfulapi.net/)
- [Docker Documentation](https://docs.docker.com/)
