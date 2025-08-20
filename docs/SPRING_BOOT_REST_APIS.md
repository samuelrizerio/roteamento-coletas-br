# Spring Boot REST APIs - Detalhamento Completo

## 📋 Visão Geral

Este projeto implementa um sistema completo de **REST APIs** usando **Spring Boot 3.2.0** para gerenciamento de roteamento de coletas seletivas. O sistema demonstra as melhores práticas de desenvolvimento de APIs RESTful com Spring Boot.

## 🏗️ Arquitetura da Aplicação

### 1. **Classe Principal - RoteamentoColetasApplication**

```java
@SpringBootApplication
public class RoteamentoColetasApplication {
    public static void main(String[] args) {
        SpringApplication.run(RoteamentoColetasApplication.class, args);
    }
}
```

**Conceitos Aplicados:**
- **@SpringBootApplication**: Anotação composta que combina:
  - `@Configuration`: Indica fonte de definições de beans
  - `@EnableAutoConfiguration`: Habilita configuração automática
  - `@ComponentScan`: Habilita varredura de componentes

### 2. **Configuração da Aplicação**

#### **application.yml** - Configurações Principais:

```yaml
server:
  port: 8080
  servlet:
    context-path: /api/v1

spring:
  application:
    name: roteamento-coletas-br
  datasource:
    url: jdbc:postgresql://localhost:5432/roteamento_coletas
    username: postgres
    password: postgres
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    format-sql: true
```

## 🎯 Dependências Principais (pom.xml)

### **Spring Boot Starters:**

```xml
<!-- Web - Para APIs RESTful -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Data JPA - Para persistência -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- Validation - Para validação de dados -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>

<!-- Security - Para autenticação -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

### **Dependências de Documentação:**

```xml
<!-- OpenAPI/Swagger -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.2.0</version>
</dependency>
```

## 🎮 Controllers REST

### 1. **MaterialController** - Gerenciamento de Materiais

#### **Estrutura Base:**
```java
@Slf4j
@RestController
@RequestMapping("/api/materiais")
@RequiredArgsConstructor
@Tag(name = "Materiais", description = "API para gerenciamento de materiais recicláveis")
public class MaterialController {
    private final MaterialService materialService;
}
```

#### **Endpoints Implementados:**

| Método HTTP | Endpoint | Descrição | Status Codes |
|-------------|----------|-----------|--------------|
| `POST` | `/api/materiais` | Criar material | 201, 400, 409 |
| `GET` | `/api/materiais/{id}` | Buscar por ID | 200, 404 |
| `GET` | `/api/materiais` | Listar todos | 200 |
| `GET` | `/api/materiais/paginados` | Listar paginado | 200 |
| `GET` | `/api/materiais/categoria/{categoria}` | Buscar por categoria | 200 |
| `GET` | `/api/materiais/busca` | Buscar por nome | 200 |
| `GET` | `/api/materiais/preco` | Buscar por faixa de preço | 200 |
| `PUT` | `/api/materiais/{id}` | Atualizar material | 200, 400, 404, 409 |
| `PATCH` | `/api/materiais/{id}/status` | Alterar status | 200, 404 |
| `DELETE` | `/api/materiais/{id}` | Excluir material | 204, 404 |
| `GET` | `/api/materiais/estatisticas` | Obter estatísticas | 200 |

#### **Exemplo de Endpoint Completo:**

```java
@PostMapping
@Operation(summary = "Criar material", description = "Cria um novo material reciclável")
@ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Material criado com sucesso",
                content = @Content(schema = @Schema(implementation = MaterialDTO.class))),
    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
    @ApiResponse(responseCode = "409", description = "Nome do material já existe")
})
public ResponseEntity<MaterialDTO> criarMaterial(
        @Parameter(description = "Dados do material", required = true)
        @Valid @RequestBody MaterialDTO materialDTO) {
    
    log.info("Recebida requisição para criar material: {}", materialDTO.getNome());
    
    MaterialDTO materialCriado = materialService.criarMaterial(materialDTO);
    
    return ResponseEntity.status(HttpStatus.CREATED).body(materialCriado);
}
```

### 2. **UsuarioController** - Gerenciamento de Usuários

#### **Endpoints Principais:**

| Método HTTP | Endpoint | Descrição |
|-------------|----------|-----------|
| `POST` | `/api/usuarios` | Criar usuário |
| `GET` | `/api/usuarios/{id}` | Buscar usuário |
| `GET` | `/api/usuarios` | Listar usuários |
| `GET` | `/api/usuarios/tipo` | Buscar por tipo |
| `GET` | `/api/usuarios/coletores-ativos` | Coletores ativos |
| `PUT` | `/api/usuarios/{id}` | Atualizar usuário |
| `DELETE` | `/api/usuarios/{id}` | Excluir usuário |
| `PATCH` | `/api/usuarios/{id}/ativar` | Ativar usuário |
| `GET` | `/api/usuarios/busca` | Buscar por nome |
| `GET` | `/api/usuarios/verificar-email` | Verificar email |

### 3. **RoteamentoController** - Otimização de Rotas

#### **Endpoints de Processamento:**

```java
@PostMapping("/otimizar")
@Operation(summary = "Otimizar rotas", description = "Otimiza rotas para coletas pendentes usando algoritmo TSP")
public ResponseEntity<List<RotaDTO>> otimizarRotas() {
    log.info("Recebida requisição para otimizar rotas");
    
    List<RotaDTO> rotasOtimizadas = roteamentoService.otimizarRotas();
    
    if (rotasOtimizadas.isEmpty()) {
        return ResponseEntity.noContent().build();
    }
    
    return ResponseEntity.ok(rotasOtimizadas);
}
```

## 🔧 Configurações Avançadas

### 1. **RestTemplateConfig** - Cliente HTTP

```java
@Configuration
public class RestTemplateConfig {
    
    @Value("${app.rest.timeout.connect:5000}")
    private int connectTimeout;
    
    @Value("${app.rest.timeout.read:10000}")
    private int readTimeout;
    
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(clientHttpRequestFactory());
        return restTemplate;
    }
    
    private ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(connectTimeout);
        factory.setReadTimeout(readTimeout);
        return factory;
    }
}
```

### 2. **Documentação OpenAPI/Swagger**

#### **Configuração no application.yml:**
```yaml
springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    path: /swagger-ui.html
    operations-sorter: method
    tags-sorter: alpha
```

#### **Acesso à Documentação:**
- **Swagger UI**: `http://localhost:8080/api/v1/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/api/v1/api-docs`

## 📊 Padrões REST Implementados

### 1. **Métodos HTTP Semânticos**

| Método | Uso | Exemplo |
|--------|-----|---------|
| `GET` | Recuperar dados | `GET /api/materiais/{id}` |
| `POST` | Criar novo recurso | `POST /api/materiais` |
| `PUT` | Atualizar recurso completo | `PUT /api/materiais/{id}` |
| `PATCH` | Atualizar parcialmente | `PATCH /api/materiais/{id}/status` |
| `DELETE` | Excluir recurso | `DELETE /api/materiais/{id}` |

### 2. **Códigos de Status HTTP**

| Código | Significado | Uso |
|--------|-------------|-----|
| `200` | OK | Sucesso na operação |
| `201` | Created | Recurso criado com sucesso |
| `204` | No Content | Sucesso sem retorno |
| `400` | Bad Request | Dados inválidos |
| `404` | Not Found | Recurso não encontrado |
| `409` | Conflict | Conflito (ex: email já existe) |
| `500` | Internal Server Error | Erro interno |

### 3. **Estrutura de Resposta Padrão**

```java
// Sucesso com dados
return ResponseEntity.ok(dados);

// Sucesso sem dados
return ResponseEntity.noContent().build();

// Erro com mensagem
return ResponseEntity.status(HttpStatus.NOT_FOUND)
    .body("Material não encontrado");

// Criação com dados
return ResponseEntity.status(HttpStatus.CREATED)
    .body(materialCriado);
```

## 🛡️ Validação e Tratamento de Erros

### 1. **Validação com Bean Validation**

```java
@PostMapping
public ResponseEntity<MaterialDTO> criarMaterial(
        @Valid @RequestBody MaterialDTO materialDTO) {
    // Validação automática dos campos anotados
}
```

### 2. **Tratamento de Exceções**

```java
@ExceptionHandler(MaterialNaoEncontradoException.class)
public ResponseEntity<String> handleMaterialNaoEncontrado(
        MaterialNaoEncontradoException e) {
    log.warn("Material não encontrado: {}", e.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(e.getMessage());
}
```

## 🔍 Recursos Avançados

### 1. **Paginação**

```java
@GetMapping("/paginados")
public ResponseEntity<Page<MaterialDTO>> listarMateriaisPaginados(
        @RequestParam(required = false) Boolean ativo,
        @PageableDefault(size = 10) Pageable pageable) {
    
    Page<MaterialDTO> materiais = materialService
        .listarMateriaisPaginados(ativo, pageable);
    
    return ResponseEntity.ok(materiais);
}
```

### 2. **Busca e Filtros**

```java
@GetMapping("/busca")
public ResponseEntity<List<MaterialDTO>> buscarPorNome(
        @RequestParam String nome) {
    
    List<MaterialDTO> materiais = materialService.buscarPorNome(nome);
    return ResponseEntity.ok(materiais);
}
```

### 3. **Logging Estruturado**

```java
@Slf4j
public class MaterialController {
    
    public ResponseEntity<MaterialDTO> criarMaterial(MaterialDTO dto) {
        log.info("Recebida requisição para criar material: {}", dto.getNome());
        // ...
        log.debug("Material criado com ID: {}", materialCriado.getId());
    }
}
```

## 🚀 Como Executar

### 1. **Pré-requisitos**
- Java 17+
- Maven 3.6+
- PostgreSQL

### 2. **Configuração do Banco**
```sql
CREATE DATABASE roteamento_coletas;
```

### 3. **Execução**
```bash
mvn spring-boot:run
```

### 4. **Acesso**
- **API Base**: `http://localhost:8080/api/v1`
- **Swagger UI**: `http://localhost:8080/api/v1/swagger-ui.html`
- **Health Check**: `http://localhost:8080/api/v1/actuator/health`

## 📈 Monitoramento e Métricas

### 1. **Actuator Endpoints**
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
```

### 2. **Métricas Disponíveis**
- `/actuator/health` - Status da aplicação
- `/actuator/info` - Informações da aplicação
- `/actuator/metrics` - Métricas de performance

## 🎯 Melhores Práticas Implementadas

### 1. **Separação de Responsabilidades**
- **Controllers**: Apenas roteamento e validação
- **Services**: Lógica de negócio
- **Repositories**: Acesso a dados
- **DTOs**: Transferência de dados

### 2. **Documentação Automática**
- Anotações OpenAPI em todos os endpoints
- Exemplos de requisição/resposta
- Descrições detalhadas

### 3. **Tratamento de Erros**
- Exceções customizadas
- Respostas HTTP apropriadas
- Logging estruturado

### 4. **Performance**
- Paginação para grandes volumes
- Cache configurável
- Timeouts configurados

## 🔮 Funcionalidades Futuras

### 1. **Autenticação JWT**
- Implementação completa de segurança
- Tokens de acesso
- Controle de permissões

### 2. **Cache Redis**
- Cache distribuído
- Melhoria de performance
- Sessões compartilhadas

### 3. **Mensageria**
- Processamento assíncrono
- Notificações em tempo real
- Integração com filas

Este projeto demonstra um uso completo e profissional do Spring Boot para desenvolvimento de REST APIs, seguindo as melhores práticas da indústria e padrões RESTful. 