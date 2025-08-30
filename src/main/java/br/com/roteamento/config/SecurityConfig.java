package br.com.roteamento.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * CONFIGURAÇÃO DE SEGURANÇA - SecurityConfig
 * 
 * CONCEITOS DIDÁTICOS EXPLICADOS:
 * 
 * 1. CONFIGURAÇÃO DE SEGURANÇA:
 *    - @EnableWebSecurity: Habilita configuração de segurança web
 *    - @Configuration: Marca classe como fonte de configuração
 *    - SecurityFilterChain: Define cadeia de filtros de segurança
 * 
 * 2. AUTORIZAÇÃO DE ENDPOINTS:
 *    - permitAll(): Permite acesso sem autenticação
 *    - authenticated(): Requer autenticação
 *    - hasRole(): Verifica papel específico
 * 
 * 3. CONFIGURAÇÕES DE SESSÃO:
 *    - SessionCreationPolicy.STATELESS: Não cria sessões
 *    - Ideal para APIs REST com JWT
 * 
 * 4. CORS E CSRF:
 *    - CORS: Cross-Origin Resource Sharing
 *    - CSRF: Cross-Site Request Forgery
 *    - APIs REST geralmente desabilitam CSRF
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    /**
     * CONFIGURAÇÃO DO FILTRO DE SEGURANÇA
     * 
     * CONCEITOS:
     * - SecurityFilterChain: Define regras de segurança
     * - HttpSecurity: Configuração de segurança HTTP
     * - authorizeHttpRequests: Define autorizações
     * - anyRequest: Aplica a todas as requisições
     * 
     * @param http Configuração de segurança HTTP
     * @return SecurityFilterChain configurado
     * @throws Exception Se houver erro na configuração
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Desabilitar todas as proteções de segurança para desenvolvimento
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                .anyRequest().permitAll()
            )
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable())
            .logout(logout -> logout.disable());
        
        return http.build();
    }
} 