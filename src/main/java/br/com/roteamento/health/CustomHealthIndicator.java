package br.com.roteamento.health;

import br.com.roteamento.repository.ColetaRepository;
import br.com.roteamento.repository.MaterialRepository;
import br.com.roteamento.repository.RotaRepository;
import br.com.roteamento.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuator.health.Health;
import org.springframework.boot.actuator.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * HEALTH CHECK CUSTOMIZADO
 * 
 * MELHORIAS IMPLEMENTADAS:
 * - Verificação de conectividade do banco
 * - Verificação de integridade dos dados
 * - Métricas de sistema
 * - Status detalhado
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomHealthIndicator implements HealthIndicator {

    private final ColetaRepository coletaRepository;
    private final MaterialRepository materialRepository;
    private final RotaRepository rotaRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * VERIFICAÇÃO DE SAÚDE DO SISTEMA
     * 
     * MELHORIAS:
     * - Verificação de conectividade
     * - Verificação de integridade
     * - Métricas de sistema
     * - Status detalhado
     */
    @Override
    public Health health() {
        try {
            Map<String, Object> details = new HashMap<>();
            
            // Verificar conectividade do banco
            boolean databaseConnected = verificarConectividadeBanco();
            details.put("database", databaseConnected ? "UP" : "DOWN");
            
            if (databaseConnected) {
                // Verificar integridade dos dados
                long totalColetas = coletaRepository.count();
                long totalMateriais = materialRepository.count();
                long totalRotas = rotaRepository.count();
                long totalUsuarios = usuarioRepository.count();
                
                details.put("totalColetas", totalColetas);
                details.put("totalMateriais", totalMateriais);
                details.put("totalRotas", totalRotas);
                details.put("totalUsuarios", totalUsuarios);
                
                // Verificar se há dados básicos
                boolean hasBasicData = totalMateriais > 0 && totalUsuarios > 0;
                details.put("hasBasicData", hasBasicData);
                
                // Verificar performance do sistema
                long startTime = System.currentTimeMillis();
                coletaRepository.count(); // Teste de performance
                long endTime = System.currentTimeMillis();
                long responseTime = endTime - startTime;
                
                details.put("databaseResponseTime", responseTime + "ms");
                details.put("timestamp", LocalDateTime.now());
                
                // Determinar status baseado nas verificações
                if (responseTime > 1000) {
                    return Health.down()
                            .withDetails(details)
                            .withDetail("reason", "Database response time too high")
                            .build();
                }
                
                if (!hasBasicData) {
                    return Health.down()
                            .withDetails(details)
                            .withDetail("reason", "Missing basic data")
                            .build();
                }
                
                return Health.up()
                        .withDetails(details)
                        .build();
            } else {
                return Health.down()
                        .withDetails(details)
                        .withDetail("reason", "Database connection failed")
                        .build();
            }
            
        } catch (Exception e) {
            log.error("Erro na verificação de saúde do sistema", e);
            return Health.down()
                    .withDetail("error", e.getMessage())
                    .withDetail("timestamp", LocalDateTime.now())
                    .build();
        }
    }

    /**
     * VERIFICAR CONECTIVIDADE DO BANCO
     * 
     * MELHORIAS:
     * - Teste de conectividade
     * - Tratamento de exceções
     * - Logs de erro
     */
    private boolean verificarConectividadeBanco() {
        try {
            // Teste simples de conectividade
            coletaRepository.count();
            return true;
        } catch (Exception e) {
            log.error("Erro na conectividade do banco de dados", e);
            return false;
        }
    }
}
