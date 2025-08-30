package br.com.roteamento.exception;

/**
 * Exceção customizada para nome de material já existente
 * 
 * CONCEITO DIDÁTICO - Exceções de Validação de Negócio:
 * - Representam violações de regras de negócio
 * - Diferem de exceções técnicas (IOException, SQLException)
 * - São tratadas de forma específica na camada de apresentação
 * - Podem ser mapeadas para códigos HTTP específicos
 * - Facilitam feedback claro para o usuário
 * 
 * CONCEITO DIDÁTICO - Nomenclatura de Exceções:
 * - Sufixo "Exception" para identificar como exceção
 * - Nome descritivo que indica o problema
 * - Seguir convenções de nomenclatura Java
 * - Ser específico sobre o contexto do erro
 */
public class NomeMaterialJaExisteException extends RuntimeException {

    /**
     * CONCEITO DIDÁTICO - Construtor com Mensagem:
     * Construtor que recebe apenas a mensagem de erro
     * 
     * @param message mensagem descritiva do erro
     */
    public NomeMaterialJaExisteException(String message) {
        super(message);
    }

    /**
     * CONCEITO DIDÁTICO - Construtor com Causa:
     * Construtor que recebe mensagem e causa da exceção
     * 
     * @param message mensagem descritiva do erro
     * @param cause exceção que causou este erro
     */
    public NomeMaterialJaExisteException(String message, Throwable cause) {
        super(message, cause);
    }



    /**
     * CONCEITO DIDÁTICO - Método de Acesso:
     * Método para obter o nome do material que causou o conflito
     * 
     * @return nome do material em conflito
     */
    public String getNomeMaterial() {
        String message = getMessage();
        if (message != null && message.contains("'")) {
            int start = message.indexOf("'") + 1;
            int end = message.lastIndexOf("'");
            if (start > 0 && end > start) {
                return message.substring(start, end);
            }
        }
        return null;
    }
} 