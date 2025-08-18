package br.com.roteamento.exception;

/**
 * Exceção customizada para material não encontrado
 * 
 * CONCEITO DIDÁTICO - Exceções Customizadas:
 * - Herdam de RuntimeException para exceções não verificadas
 * - Fornecem mensagens específicas do domínio
 * - Facilitam tratamento específico de erros
 * - Melhoram a legibilidade do código
 * - Permitem internacionalização de mensagens
 * 
 * CONCEITO DIDÁTICO - Hierarquia de Exceções:
 * - RuntimeException: exceções não verificadas (não precisam ser declaradas)
 * - Exception: exceções verificadas (precisam ser declaradas ou tratadas)
 * - Error: erros graves do sistema (não devem ser capturados)
 */
public class MaterialNaoEncontradoException extends RuntimeException {

    /**
     * CONCEITO DIDÁTICO - Construtor com Mensagem:
     * Construtor que recebe apenas a mensagem de erro
     * 
     * @param message mensagem descritiva do erro
     */
    public MaterialNaoEncontradoException(String message) {
        super(message);
    }

    /**
     * CONCEITO DIDÁTICO - Construtor com Causa:
     * Construtor que recebe mensagem e causa da exceção
     * 
     * @param message mensagem descritiva do erro
     * @param cause exceção que causou este erro
     */
    public MaterialNaoEncontradoException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * CONCEITO DIDÁTICO - Construtor com ID:
     * Construtor conveniente que formata mensagem com ID
     * 
     * @param id ID do material não encontrado
     */
    public MaterialNaoEncontradoException(Long id) {
        super("Material com ID " + id + " não encontrado");
    }
} 