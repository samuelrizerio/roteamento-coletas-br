package br.com.roteamento.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

/**
 * Configuração do RestTemplate para integração com APIs externas
 * 
 * CONCEITO DIDÁTICO - Configuração de Beans:
 * - @Configuration: marca classe como fonte de definições de beans
 * - @Bean: define um bean gerenciado pelo Spring
 * - Dependency Injection automática de beans
 * - Configuração centralizada de componentes
 * - Reutilização de beans em toda aplicação
 * 
 * CONCEITO DIDÁTICO - RestTemplate:
 * - Cliente HTTP síncrono para consumir APIs REST
 * - Suporte a diferentes tipos de requisições (GET, POST, PUT, DELETE)
 * - Configuração de timeouts e interceptors
 * - Tratamento de erros HTTP
 * - Serialização/deserialização automática de JSON
 */
@Configuration
public class RestTemplateConfig {

    @Value("${app.rest.timeout.connect:5000}")
    private int connectTimeout;

    @Value("${app.rest.timeout.read:10000}")
    private int readTimeout;

    /**
     * CONCEITO DIDÁTICO - Bean de RestTemplate:
     * Configura RestTemplate com timeouts personalizados
     * 
     * @return RestTemplate configurado
     */
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(clientHttpRequestFactory());
        return restTemplate;
    }

    /**
     * CONCEITO DIDÁTICO - Configuração de Timeouts:
     * Define timeouts para conexão e leitura
     * 
     * @return ClientHttpRequestFactory configurado
     */
    private ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        
        // CONCEITO DIDÁTICO - Timeout de Conexão:
        // Tempo máximo para estabelecer conexão
        factory.setConnectTimeout(connectTimeout);
        
        // CONCEITO DIDÁTICO - Timeout de Leitura:
        // Tempo máximo para receber resposta
        factory.setReadTimeout(readTimeout);
        
        return factory;
    }

    /**
     * CONCEITO DIDÁTICO - Bean de PasswordEncoder:
     * Configura encoder de senhas usando BCrypt
     * 
     * @return PasswordEncoder configurado
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
} 