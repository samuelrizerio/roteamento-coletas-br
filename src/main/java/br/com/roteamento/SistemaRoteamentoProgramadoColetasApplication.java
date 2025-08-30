package br.com.roteamento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * APLICAÇÃO PRINCIPAL - SISTEMA DE ROTEAMENTO PROGRAMADO DE COLETAS
 * 
 * CONCEITOS DIDÁTICOS EXPLICADOS:
 * 
 * 1. SPRING BOOT APPLICATION:
 *    - @SpringBootApplication: Anotação principal do Spring Boot
 *    - Auto-configuração: Configura automaticamente beans necessários
 *    - Component scanning: Escaneia componentes na base package
 *    - Embedded server: Inclui servidor Tomcat embutido
 * 
 * 2. ENABLE SCHEDULING:
 *    - @EnableScheduling: Habilita execução de tarefas agendadas
 *    - @Scheduled: Permite agendar métodos para execução automática
 *    - Background tasks: Execução em background
 *    - Cron expressions: Definição de horários complexos
 * 
 * 3. ARQUITETURA SPRING BOOT:
 *    - Convention over configuration: Convenções sobre configuração
 *    - Starter dependencies: Dependências pré-configuradas
 *    - Auto-configuration: Configuração automática baseada em classpath
 *    - Embedded containers: Containers embutidos (Tomcat, Jetty, etc.)
 */
@SpringBootApplication
@EnableScheduling
public class SistemaRoteamentoProgramadoColetasApplication {

    /**
     * MÉTODO MAIN - PONTO DE ENTRADA DA APLICAÇÃO
     * 
     * CONCEITOS:
     * - SpringApplication.run(): Inicia a aplicação Spring Boot
     * - Auto-configuração: Configura automaticamente o contexto
     * - Embedded server: Inicia servidor web embutido
     * - Component scanning: Escaneia e registra componentes
     * 
     * @param args argumentos da linha de comando
     */
    public static void main(String[] args) {
        SpringApplication.run(SistemaRoteamentoProgramadoColetasApplication.class, args);
    }
} 