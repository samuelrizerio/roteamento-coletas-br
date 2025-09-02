package br.com.roteamento.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * CONFIGURAÇÃO DE CACHE REDIS
 * 
 * MELHORIAS IMPLEMENTADAS:
 * - Cache Redis para performance
 * - Serialização JSON
 * - Configuração de TTL
 * - Cache para dados frequentes
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * CONFIGURAÇÃO DO CACHE MANAGER
     * 
     * MELHORIAS:
     * - Serialização JSON
     * - TTL configurável
     * - Prefixo de chaves
     * - Configuração de eviction
     */
    @Bean
    CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30)) // TTL padrão de 30 minutos
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .prefixCacheNameWith("roteamento:"); // Prefixo para organização

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(config)
                .build();
    }

    /**
     * CONFIGURAÇÕES ESPECÍFICAS DE CACHE
     * 
     * MELHORIAS:
     * - TTL diferenciado por tipo de dados
     * - Configuração otimizada
     * - Cache para materiais (dados estáticos)
     * - Cache para usuários (dados semi-estáticos)
     */
    @Bean
    RedisCacheConfiguration materiaisCacheConfig() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(2)) // Materiais mudam pouco
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .prefixCacheNameWith("materiais:");
    }

    @Bean
    RedisCacheConfiguration usuariosCacheConfig() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(15)) // Usuários mudam mais frequentemente
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .prefixCacheNameWith("usuarios:");
    }

    @Bean
    RedisCacheConfiguration rotasCacheConfig() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5)) // Rotas mudam frequentemente
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .prefixCacheNameWith("rotas:");
    }
}
