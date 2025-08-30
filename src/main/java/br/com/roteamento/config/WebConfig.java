package br.com.roteamento.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CONFIGURAÇÃO WEB ESPECÍFICA PARA JSP
 * 
 * PROBLEMA IDENTIFICADO:
 * - Spring Boot 3.x não suporta JSP por padrão
 * - Configuração do application.yml não está sendo aplicada
 * - Necessário configurar manualmente o ViewResolver
 * 
 * SOLUÇÃO IMPLEMENTADA:
 * - Configuração manual do ViewResolver via código Java
 * - Mapeamento correto para pasta WEB-INF/views
 * - Configuração de recursos estáticos
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * CONFIGURAÇÃO DO VIEW RESOLVER PARA JSP
     * 
     * Esta configuração sobrescreve a configuração do application.yml
     * e garante que o JSP funcione corretamente no Spring Boot 3.x
     * 
     * @param registry registro de view resolvers
     */
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        // Configuração específica para JSP
        registry.jsp("/WEB-INF/views/", ".jsp");
    }

    /**
     * CONFIGURAÇÃO DE RECURSOS ESTÁTICOS
     * 
     * Garante que arquivos CSS, JS e outros recursos sejam servidos corretamente
     * 
     * @param registry registro de recursos
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Favicon específico - prioridade alta
        registry.addResourceHandler("/favicon.ico")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(3600)
                .resourceChain(true);
        
        // Imagens - prioridade alta
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/")
                .setCachePeriod(1800);
        
        // Recursos estáticos padrão (inclui imagens)
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        
        // Recursos das views JSP
        registry.addResourceHandler("/WEB-INF/views/**")
                .addResourceLocations("classpath:/META-INF/resources/WEB-INF/views/");
    }
}
