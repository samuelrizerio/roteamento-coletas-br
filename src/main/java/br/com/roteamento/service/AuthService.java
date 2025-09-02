package br.com.roteamento.service;

import br.com.roteamento.dto.AuthRequest;
import br.com.roteamento.dto.AuthResponse;
import br.com.roteamento.dto.RegisterRequest;
import br.com.roteamento.model.Usuario;
import br.com.roteamento.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Serviço de autenticação
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registra um novo usuário
     */
    public AuthResponse register(RegisterRequest request) {
        log.info("Registrando novo usuário: {}", request.getEmail());
        
        Usuario usuario = Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha()))
                .tipoUsuario(request.getTipoUsuario())
                .build();
        
        usuarioRepository.save(usuario);
        
        return AuthResponse.builder()
                .accessToken("jwt-token-placeholder")
                .build();
    }

    /**
     * Autentica um usuário
     */
    public AuthResponse authenticate(AuthRequest request) {
        log.info("Autenticando usuário: {}", request.getEmail());
        
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        if (!passwordEncoder.matches(request.getSenha(), usuario.getSenha())) {
            throw new RuntimeException("Senha incorreta");
        }
        
        return AuthResponse.builder()
                .accessToken("jwt-token-placeholder")
                .build();
    }

    /**
     * Renova o token de autenticação
     */
    public AuthResponse refreshToken(String token) {
        log.info("Renovando token");
        
        // Implementação simplificada - em produção, validar token atual
        return AuthResponse.builder()
                .accessToken("new-jwt-token-placeholder")
                .build();
    }

    /**
     * Faz logout do usuário
     */
    public void logout(String token) {
        log.info("Fazendo logout do usuário");
        
        // Implementação simplificada - em produção, invalidar token
    }

    /**
     * Valida um token
     */
    public boolean validateToken(String token) {
        log.debug("Validando token");
        
        // Implementação simplificada - em produção, validar JWT
        return token != null && !token.isEmpty();
    }
}
