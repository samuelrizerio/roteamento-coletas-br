package br.com.roteamento.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * CONFIGURAÇÃO JACKSON - RESOLUÇÃO DE PROBLEMAS DE SERIALIZAÇÃO
 * 
 * CONCEITOS IMPLEMENTADOS:
 * 
 * 1. HIBERNATE MODULE:
 *    - Hibernate6Module: Módulo para integração com Hibernate 6
 *    - Lazy loading: Tratamento de carregamento preguiçoso
 *    - Proxy objects: Serialização de objetos proxy
 *    - LazyInitializationException: Prevenção de erros
 * 
 * 2. SERIALIZATION FEATURES:
 *    - FAIL_ON_EMPTY_BEANS: Desabilita falha em beans vazios
 *    - WRITE_DATES_AS_TIMESTAMPS: Formato de datas como timestamps
 *    - FAIL_ON_UNKNOWN_PROPERTIES: Falha em propriedades desconhecidas
 * 
 * 3. CONFIGURAÇÃO DE SERIALIZAÇÃO:
 *    - ObjectMapper: Configuração principal do Jackson
 *    - Hibernate integration: Integração com Hibernate
 *    - JSON serialization: Serialização para JSON
 *    - Error handling: Tratamento de erros de serialização
 * 
 * 4. MÓDULOS DE TEMPO:
 *    - JavaTimeModule: Suporte a LocalDateTime, LocalDate, etc.
 *    - Jdk8Module: Suporte a Optional e outros tipos do Java 8
 */
@Configuration
public class JacksonConfig {

    /**
     * CONFIGURAÇÃO PRINCIPAL DO JACKSON
     * 
     * RESOLVE PROBLEMAS:
     * - Serialização de proxies do Hibernate
     * - Lazy loading exceptions
     * - Beans vazios na serialização
     * - Propriedades não encontradas
     * - Serialização de LocalDateTime e outros tipos de tempo
     * 
     * @return ObjectMapper configurado
     */
    @Bean
    @Primary
    ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        
        // Configurações básicas de serialização
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        
        // Registra módulos para suporte a tipos de tempo
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new Jdk8Module());
        
        // Módulo Hibernate para integração
        Hibernate6Module hibernateModule = new Hibernate6Module();
        hibernateModule.configure(Hibernate6Module.Feature.FORCE_LAZY_LOADING, false);
        mapper.registerModule(hibernateModule);
        
        return mapper;
    }

    /**
     * CONFIGURAÇÃO ALTERNATIVA PARA SPRING BOOT
     * 
     * Usa Jackson2ObjectMapperBuilderCustomizer para garantir que
     * o Spring Boot use nossa configuração personalizada
     */
    @Bean
    Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomizer() {
        return builder -> {
            builder.modules(new JavaTimeModule(), new Jdk8Module());
            builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            builder.featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        };
    }
}
