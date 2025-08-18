package br.com.roteamento.exception;

/**
 * EXCEÇÃO CUSTOMIZADA - EmailJaExisteException
 * 
 * CONCEITOS DIDÁTICOS EXPLICADOS:
 * 
 * 1. EXCEÇÕES DE VALIDAÇÃO:
 *    - Lançadas quando dados não atendem regras de negócio
 *    - Específicas para cada tipo de validação
 *    - Mensagens claras para o usuário
 *    - Úteis para feedback de formulários
 * 
 * 2. CONFLITOS DE DADOS:
 *    - Email duplicado: Violação de unicidade
 *    - Chave estrangeira: Violação de integridade referencial
 *    - Dados inconsistentes: Violação de regras de negócio
 *    - Tratamento específico para cada caso
 * 
 * 3. RESPOSTAS HTTP:
 *    - 409 Conflict: Para conflitos de dados
 *    - 400 Bad Request: Para dados inválidos
 *    - 422 Unprocessable Entity: Para validações de negócio
 *    - Headers apropriados para cada situação
 */
public class EmailJaExisteException extends RuntimeException {

    /**
     * CONSTRUTOR PADRÃO
     * 
     * CONCEITOS:
     * - super(): Chama construtor da classe pai
     * - String message: Mensagem descritiva do erro
     * - RuntimeException: Exceção não verificada
     */
    public EmailJaExisteException(String message) {
        super(message);
    }

    /**
     * CONSTRUTOR COM CAUSA
     * 
     * CONCEITOS:
     * - super(message, cause): Chama construtor da classe pai
     * - Throwable cause: Exceção que causou este erro
     * - Chain of exceptions: Rastreamento da causa raiz
     * 
     * @param message Mensagem descritiva do erro
     * @param cause Exceção que causou este erro
     */
    public EmailJaExisteException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * CONSTRUTOR COM CAUSA APENAS
     * 
     * CONCEITOS:
     * - super(cause): Chama construtor da classe pai
     * - Throwable cause: Exceção que causou este erro
     * 
     * @param cause Exceção que causou este erro
     */
    public EmailJaExisteException(Throwable cause) {
        super(cause);
    }
} 