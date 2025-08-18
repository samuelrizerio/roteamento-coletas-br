package br.com.roteamento.exception;

/**
 * EXCEÇÃO ROTA NÃO ENCONTRADA
 * 
 * CONCEITOS DIDÁTICOS EXPLICADOS:
 * 
 * 1. EXCEÇÕES CUSTOMIZADAS:
 *    - Herdam de RuntimeException (não verificada)
 *    - Permitem controle de fluxo específico
 *    - Mensagens de erro claras e contextuais
 *    - Facilita debugging e tratamento de erros
 * 
 * 2. HIERARQUIA DE EXCEÇÕES:
 *    - RuntimeException: Exceção não verificada
 *    - Não precisa ser declarada no método
 *    - Pode ser tratada opcionalmente
 * 
 * 3. BOAS PRÁTICAS:
 *    - Nome descritivo da exceção
 *    - Mensagem clara sobre o problema
 *    - Construtor que aceita mensagem
 *    - Construtor que aceita causa
 */
public class RotaNaoEncontradaException extends RuntimeException {

    /**
     * CONSTRUTOR COM MENSAGEM
     * 
     * CONCEITOS:
     * - super(): Chama construtor da classe pai
     * - String message: Mensagem descritiva do erro
     */
    public RotaNaoEncontradaException(String message) {
        super(message);
    }

    /**
     * CONSTRUTOR COM MENSAGEM E CAUSA
     * 
     * CONCEITOS:
     * - Throwable cause: Exceção que causou este erro
     * - Encadeamento de exceções
     * - Preserva stack trace original
     */
    public RotaNaoEncontradaException(String message, Throwable cause) {
        super(message, cause);
    }
} 