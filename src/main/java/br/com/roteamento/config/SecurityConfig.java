package br.com.roteamento.config;

import br.com.roteamento.security.JwtAuthenticationEntryPoint;
import br.com.roteamento.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * CONFIGURAÇÃO DE SEGURANÇA ROBUSTA
 * 
 * MELHORIAS IMPLEMENTADAS:
 * - Autenticação JWT real
 * - Controle de acesso baseado em roles
 * - Criptografia de senhas com BCrypt
 * - Proteção contra ataques comuns
 * - Configuração CORS adequada
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;
    private final CorsConfigurationSource corsConfigurationSource;

    /**
     * CONFIGURAÇÃO DO FILTRO DE SEGURANÇA
     * 
     * MELHORIAS:
     * - Rotas públicas bem definidas
     * - Proteção de APIs administrativas
     * - Controle de acesso por roles
     * - Tratamento de exceções de autenticação
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Configuração CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            
            // Desabilitar CSRF para APIs REST (JWT é stateless)
            .csrf(csrf -> csrf.disable())
            
            // Configuração de sessão stateless
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Configuração de autorização
            .authorizeHttpRequests(authz -> authz
                // Rotas públicas
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                
                // Rotas do sistema (requer autenticação)
                .requestMatchers("/sistema/**").hasAnyRole("ADMIN", "OPERADOR")
                .requestMatchers("/api/v1/coletas/**").hasAnyRole("ADMIN", "OPERADOR", "COLETOR")
                .requestMatchers("/api/v1/rotas/**").hasAnyRole("ADMIN", "OPERADOR", "COLETOR")
                .requestMatchers("/api/v1/usuarios/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/materiais/**").hasAnyRole("ADMIN", "OPERADOR")
                .requestMatchers("/api/v1/algoritmos-avancados/**").hasRole("ADMIN")
                
                // Todas as outras requisições requerem autenticação
                .anyRequest().authenticated()
            )
            
            // Configuração de exceções
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            )
            
            // Adicionar filtro JWT
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            
            // Desabilitar login form (usamos JWT)
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())
            .logout(logout -> logout.disable());
        
        return http.build();
    }

    /**
     * ENCODER DE SENHAS
     * 
     * MELHORIAS:
     * - BCrypt com força 12 (recomendado)
     * - Salt automático
     * - Resistente a ataques de força bruta
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    /**
     * PROVIDER DE AUTENTICAÇÃO
     * 
     * MELHORIAS:
     * - Integração com UserDetailsService
     * - Criptografia de senhas
     * - Validação de credenciais
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * MANAGER DE AUTENTICAÇÃO
     * 
     * MELHORIAS:
     * - Configuração centralizada
     * - Integração com providers
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}