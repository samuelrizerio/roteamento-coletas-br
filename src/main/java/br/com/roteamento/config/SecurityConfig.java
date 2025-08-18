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
            // CONCEITO DIDÁTICO - Desabilita CSRF para APIs REST:
            // APIs REST não precisam de proteção CSRF pois não usam cookies
            .csrf(csrf -> csrf.disable())
            
            // CONCEITO DIDÁTICO - Configuração de CORS:
            // Permite requisições de origens diferentes
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            
            // CONCEITO DIDÁTICO - Configuração de Sessão:
            // APIs REST são stateless, não criam sessões
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // CONCEITO DIDÁTICO - Autorização de Endpoints:
            // Define quais endpoints são públicos e quais precisam de autenticação
            .authorizeHttpRequests(authz -> authz
                // Endpoints públicos (sem autenticação)
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/api-docs/**").permitAll()
                .requestMatchers("/swagger-ui.html").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers("/api/v1/materiais/**").permitAll()
                .requestMatchers("/api/v1/usuarios/**").permitAll()
                .requestMatchers("/api/v1/roteamento/**").permitAll()
                .requestMatchers("/api/v1/coletas/**").permitAll()
                .requestMatchers("/api/v1/rotas/**").permitAll()
                .requestMatchers("/api/v1/dashboard/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/").permitAll()
                
                // CONCEITO DIDÁTICO - Qualquer outra requisição:
                // Por enquanto, permite todas as requisições
                // Em produção, seria necessário autenticação
                .anyRequest().permitAll()
            );
        
        return http.build();
    }
} 