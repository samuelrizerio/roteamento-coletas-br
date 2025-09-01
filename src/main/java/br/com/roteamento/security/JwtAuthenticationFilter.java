package br.com.roteamento.security;

import br.com.roteamento.service.JwtService;
import br.com.roteamento.service.UsuarioService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * FILTRO DE AUTENTICAÇÃO JWT
 * 
 * MELHORIAS IMPLEMENTADAS:
 * - Validação de token JWT
 * - Extração de usuário do token
 * - Configuração de contexto de segurança
 * - Logs de segurança
 * - Tratamento de erros
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    /**
     * FILTRO DE AUTENTICAÇÃO
     * 
     * MELHORIAS:
     * - Validação de header Authorization
     * - Verificação de token válido
     * - Carregamento de UserDetails
     * - Configuração de autenticação
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        
        try {
            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            final String userEmail;

            // Verificar se o header Authorization existe e começa com "Bearer "
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            // Extrair o token JWT
            jwt = authHeader.substring(7);
            
            // Extrair email do usuário do token
            userEmail = jwtService.extractUsername(jwt);

            // Verificar se o usuário não está autenticado e o token é válido
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = usuarioService.loadUserByUsername(userEmail);
                
                // Validar token e configurar autenticação
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    
                    log.debug("Usuário autenticado: {}", userEmail);
                } else {
                    log.warn("Token JWT inválido para usuário: {}", userEmail);
                }
            }
        } catch (Exception e) {
            log.error("Erro no filtro de autenticação JWT", e);
            // Continuar sem autenticação em caso de erro
        }

        filterChain.doFilter(request, response);
    }
}
