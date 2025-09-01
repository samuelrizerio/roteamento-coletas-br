package br.com.roteamento.controller;

import br.com.roteamento.dto.AuthRequest;
import br.com.roteamento.dto.AuthResponse;
import br.com.roteamento.dto.RegisterRequest;
import br.com.roteamento.model.Usuario;
import br.com.roteamento.service.AuthService;
import br.com.roteamento.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * CONTROLLER DE AUTENTICAÇÃO
 * 
 * MELHORIAS IMPLEMENTADAS:
 * - Endpoints de login e registro
 * - Validação de entrada
 * - Geração de tokens JWT
 * - Tratamento de erros
 * - Logs de segurança
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    /**
     * ENDPOINT DE REGISTRO
     * 
     * MELHORIAS:
     * - Validação de dados
     * - Criptografia de senha
     * - Geração de token
     * - Logs de auditoria
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Tentativa de registro para email: {}", request.getEmail());
        
        try {
            Usuario usuario = authService.register(request);
            String jwtToken = jwtService.generateToken(usuario);
            String refreshToken = jwtService.generateRefreshToken(usuario);
            
            AuthResponse response = AuthResponse.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .expiresIn(3600) // 1 hora
                    .usuario(usuario)
                    .build();
            
            log.info("Registro realizado com sucesso para: {}", request.getEmail());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Erro no registro para email: {}", request.getEmail(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * ENDPOINT DE LOGIN
     * 
     * MELHORIAS:
     * - Autenticação segura
     * - Validação de credenciais
     * - Geração de tokens
     * - Logs de segurança
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        log.info("Tentativa de login para email: {}", request.getEmail());
        
        try {
            Usuario usuario = authService.authenticate(request);
            String jwtToken = jwtService.generateToken(usuario);
            String refreshToken = jwtService.generateRefreshToken(usuario);
            
            AuthResponse response = AuthResponse.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .expiresIn(3600) // 1 hora
                    .usuario(usuario)
                    .build();
            
            log.info("Login realizado com sucesso para: {}", request.getEmail());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Erro no login para email: {}", request.getEmail(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * ENDPOINT DE REFRESH TOKEN
     * 
     * MELHORIAS:
     * - Renovação de tokens
     * - Validação de refresh token
     * - Segurança aprimorada
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestParam String refreshToken) {
        log.info("Tentativa de refresh token");
        
        try {
            AuthResponse response = authService.refreshToken(refreshToken);
            log.info("Refresh token realizado com sucesso");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Erro no refresh token", e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * ENDPOINT DE LOGOUT
     * 
     * MELHORIAS:
     * - Invalidação de tokens
     * - Logs de auditoria
     * - Limpeza de sessão
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // Remove "Bearer "
        log.info("Logout realizado");
        
        // Em uma implementação mais robusta, adicionar token à blacklist
        authService.logout(token);
        
        return ResponseEntity.ok().build();
    }

    /**
     * ENDPOINT DE VALIDAÇÃO DE TOKEN
     * 
     * MELHORIAS:
     * - Verificação de validade
     * - Informações do usuário
     * - Status de autenticação
     */
    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateToken(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // Remove "Bearer "
        
        try {
            boolean isValid = authService.validateToken(token);
            Map<String, Object> response = new HashMap<>();
            response.put("valid", isValid);
            
            if (isValid) {
                String username = jwtService.extractUsername(token);
                response.put("username", username);
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Erro na validação do token", e);
            return ResponseEntity.badRequest().build();
        }
    }
}
