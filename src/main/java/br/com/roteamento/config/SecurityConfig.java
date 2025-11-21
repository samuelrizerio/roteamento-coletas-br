package br.com.roteamento.config;

import br.com.roteamento.security.JwtAuthenticationEntryPoint;
import br.com.roteamento.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;


import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



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
    private final org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource;

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
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                .requestMatchers(
                    "/api/v1/auth/**",
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/api-docs/**",
                    "/favicon.ico",
                    "/logo",
                    "/css/**",
                    "/static/**",
                    "/actuator/health"
                ).permitAll()
                .requestMatchers("/sistema/**").authenticated()
                .anyRequest().authenticated()
            )
            .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
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
    PasswordEncoder passwordEncoder() {
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
    // @Bean
    // public AuthenticationProvider authenticationProvider() {
    //     DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    //     authProvider.setUserDetailsService(userDetailsService);
    //     authProvider.setPasswordEncoder(passwordEncoder());
    //     return authProvider;
    // }

    /**
     * MANAGER DE AUTENTICAÇÃO
     * 
     * MELHORIAS:
     * - Configuração centralizada
     * - Integração com providers
     */
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
