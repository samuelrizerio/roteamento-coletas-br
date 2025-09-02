package br.com.roteamento.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * CONFIGURAÇÃO DE CORS - CorsConfig
 * 
 * CONCEITOS DIDÁTICOS EXPLICADOS:
 * 
 * 1. CORS (Cross-Origin Resource Sharing):
 *    - Permite requisições de diferentes origens
 *    - Necessário para aplicações web modernas
 *    - Controla quais origens podem acessar a API
 * 
 * 2. CONFIGURAÇÃO DE SEGURANÇA:
 *    - Define origens permitidas (apenas localhost:8081)
 *    - Define métodos HTTP permitidos
 *    - Define headers permitidos
 *    - Controla credenciais
 */
@Configuration
public class CorsConfig {

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Permitir origens específicas
        configuration.setAllowedOriginPatterns(Arrays.asList(
            "http://localhost:8888",
            "http://127.0.0.1:8888",
            "http://localhost:8889",
            "http://127.0.0.1:8889"
        ));
        
        // Permitir métodos HTTP
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));
        
        // Permitir headers
        configuration.setAllowedHeaders(Arrays.asList(
            "Origin", "Content-Type", "Accept", "Authorization", 
            "X-Requested-With", "Cache-Control"
        ));
        
        // Permitir credenciais
        configuration.setAllowCredentials(true);
        
        // Expor headers
        configuration.setExposedHeaders(Arrays.asList(
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials"
        ));
        
        // Tempo máximo de cache para preflight
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
} 