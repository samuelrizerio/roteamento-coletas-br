package br.com.roteamento.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Handler global de exceções para a API
 * 
 * CONCEITO DIDÁTICO - Tratamento Global de Exceções:
 * - @ControllerAdvice: Aplica a todos os controllers
 * - @ExceptionHandler: Define método para tratar exceção específica
 * - Respostas HTTP padronizadas
 * - Logging de erros
 * - Validação de entrada
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Trata exceções de validação de argumentos
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Dados de entrada inválidos");
        response.put("message", "Verifique os campos obrigatórios");
        
        log.warn("Erro de validação: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Trata exceções de tipo de argumento incorreto (ex: enum inválido)
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Tipo de parâmetro inválido");
        response.put("message", "O valor '" + ex.getValue() + "' não é válido para o parâmetro '" + ex.getName() + "'");
        
        log.warn("Erro de tipo de parâmetro: {} = {}", ex.getName(), ex.getValue());
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Trata exceções de IllegalArgumentException (ex: enum inválido)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Argumento inválido");
        response.put("message", ex.getMessage());
        
        log.warn("Argumento inválido: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Trata exceções de recursos não encontrados
     */
    @ExceptionHandler({
        UsuarioNaoEncontradoException.class,
        MaterialNaoEncontradoException.class,
        ColetaNaoEncontradaException.class,
        RotaNaoEncontradaException.class
    })
    public ResponseEntity<Map<String, Object>> handleNotFoundException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("error", "Recurso não encontrado");
        response.put("message", ex.getMessage());
        
        log.warn("Recurso não encontrado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Trata exceções de dados duplicados
     */
    @ExceptionHandler({
        EmailJaExisteException.class,
        NomeMaterialJaExisteException.class
    })
    public ResponseEntity<Map<String, Object>> handleConflictException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.CONFLICT.value());
        response.put("error", "Conflito de dados");
        response.put("message", ex.getMessage());
        
        log.warn("Conflito de dados: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * Trata exceções genéricas não mapeadas
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error", "Erro interno do servidor");
        response.put("message", "Ocorreu um erro inesperado. Tente novamente mais tarde.");
        
        log.error("Erro interno não tratado", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
} 