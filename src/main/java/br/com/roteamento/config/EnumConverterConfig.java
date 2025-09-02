package br.com.roteamento.config;

import br.com.roteamento.model.Coleta;
import br.com.roteamento.model.Material;
import br.com.roteamento.model.Rota;
import br.com.roteamento.model.Usuario;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuração de conversores customizados para enums
 * 
 * CONCEITO DIDÁTICO - Conversores Customizados:
 * - Converter<String, Enum>: Converte String para Enum
 * - WebMvcConfigurer: Configuração do Spring MVC
 * - FormatterRegistry: Registro de conversores
 * - Tratamento de valores case-insensitive
 */
@Configuration
public class EnumConverterConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(@NonNull FormatterRegistry registry) {
        // Conversor para StatusColeta
        registry.addConverter(new Converter<String, Coleta.StatusColeta>() {
            @Override
            public Coleta.StatusColeta convert(@NonNull String source) {
                if (source == null || source.trim().isEmpty()) {
                    return null;
                }
                try {
                    return Coleta.StatusColeta.valueOf(source.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Status de coleta inválido: " + source);
                }
            }
        });

        // Conversor para StatusRota
        registry.addConverter(new Converter<String, Rota.StatusRota>() {
            @Override
            public Rota.StatusRota convert(@NonNull String source) {
                if (source == null || source.trim().isEmpty()) {
                    return null;
                }
                try {
                    return Rota.StatusRota.valueOf(source.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Status de rota inválido: " + source);
                }
            }
        });

        // Conversor para TipoUsuario
        registry.addConverter(new Converter<String, Usuario.TipoUsuario>() {
            @Override
            public Usuario.TipoUsuario convert(@NonNull String source) {
                if (source == null || source.trim().isEmpty()) {
                    return null;
                }
                try {
                    return Usuario.TipoUsuario.valueOf(source.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Tipo de usuário inválido: " + source);
                }
            }
        });

        // Conversor para CategoriaMaterial
        registry.addConverter(new Converter<String, Material.CategoriaMaterial>() {
            @Override
            public Material.CategoriaMaterial convert(@NonNull String source) {
                if (source == null || source.trim().isEmpty()) {
                    return null;
                }
                try {
                    return Material.CategoriaMaterial.valueOf(source.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Categoria de material inválida: " + source);
                }
            }
        });
    }
} 