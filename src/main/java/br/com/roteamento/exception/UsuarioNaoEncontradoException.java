package br.com.roteamento.exception;

/**
 * EXCEÇÃO CUSTOMIZADA - UsuarioNaoEncontradoException
 * 
 * CONCEITOS DIDÁTICOS EXPLICADOS:
 * 
 * 1. TRATAMENTO DE EXCEÇÕES EM JAVA:
 *    - Exception: Classe base para exceções
 *    - RuntimeException: Exceção não verificada (não precisa ser declarada)
 *    - Exceções customizadas: Criam hierarquia específica para o domínio
 *    - try/catch: Blocos para capturar e tratar exceções
 * 
 * 2. HIERARQUIA DE EXCEÇÕES:
 *    - Throwable: Classe raiz para exceções e erros
 *    - Exception: Exceções que podem ser tratadas
 *    - RuntimeException: Exceções não verificadas
 *    - Exceções customizadas: Específicas do domínio
 * 
 * 3. BOAS PRÁTICAS:
 *    - Mensagens descritivas e úteis
 *    - Herança apropriada (RuntimeException para exceções de negócio)
 *    - Documentação clara do propósito
 *    - Reutilização em diferentes contextos
 * 
 * 4. USO NO SISTEMA:
 *    - Lançada quando usuário não é encontrado
 *    - Capturada pelo controlador REST
 *    - Convertida em resposta HTTP apropriada
 *    - Logging para debug e auditoria
 */
public class UsuarioNaoEncontradoException extends RuntimeException {

    /**
     * CONSTRUTOR PADRÃO
     * 
     * CONCEITOS:
     * - super(): Chama construtor da classe pai
     * - String message: Mensagem descritiva do erro
     * - RuntimeException: Exceção não verificada
     */
    public UsuarioNaoEncontradoException(String message) {
        super(message);
    }

    /**
     * CONSTRUTOR COM CAUSA
     * 
     * CONCEITOS:
     * - super(message, cause): Chama construtor da classe pai
     * - Throwable cause: Exceção que causou este erro
     * - Chain of exceptions: Rastreamento da causa raiz
     * - Útil para debugging e logging
     * 
     * @param message Mensagem descritiva do erro
     * @param cause Exceção que causou este erro
     */
    public UsuarioNaoEncontradoException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * CONSTRUTOR COM CAUSA APENAS
     * 
     * CONCEITOS:
     * - super(cause): Chama construtor da classe pai
     * - Throwable cause: Exceção que causou este erro
     * - Útil quando a causa já contém a mensagem
     * 
     * @param cause Exceção que causou este erro
     */
    public UsuarioNaoEncontradoException(Throwable cause) {
        super(cause);
    }
} 