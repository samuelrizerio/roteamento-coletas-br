package br.com.roteamento.config;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * CONFIGURAÇÃO DE MÉTRICAS
 * 
 * MELHORIAS IMPLEMENTADAS:
 * - Métricas de performance
 * - Monitoramento de algoritmos
 * - Health checks detalhados
 * - Métricas customizadas
 */
@Configuration
@EnableScheduling
public class MetricsConfig {

    /**
     * ASPECTO PARA TIMING AUTOMÁTICO
     * 
     * MELHORIAS:
     * - Timing automático de métodos
     * - Métricas de performance
     * - Monitoramento de latência
     */
    @Bean
    TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }

    /**
     * TIMER PARA ALGORITMOS DE ROTEAMENTO
     * 
     * MELHORIAS:
     * - Monitoramento de algoritmos críticos
     * - Métricas de performance
     * - Alertas de latência
     */
    @Bean
    Timer roteamentoTimer(MeterRegistry registry) {
        return Timer.builder("roteamento.algoritmo.tempo")
                .description("Tempo de execução dos algoritmos de roteamento")
                .register(registry);
    }

    /**
     * TIMER PARA OPERAÇÕES DE BANCO
     * 
     * MELHORIAS:
     * - Monitoramento de queries
     * - Métricas de performance
     * - Detecção de lentidão
     */
    @Bean
    Timer databaseTimer(MeterRegistry registry) {
        return Timer.builder("database.operacao.tempo")
                .description("Tempo de operações de banco de dados")
                .register(registry);
    }

    /**
     * TIMER PARA CACHE
     * 
     * MELHORIAS:
     * - Monitoramento de cache
     * - Métricas de hit/miss
     * - Performance de cache
     */
    @Bean
    Timer cacheTimer(MeterRegistry registry) {
        return Timer.builder("cache.operacao.tempo")
                .description("Tempo de operações de cache")
                .register(registry);
    }
}
