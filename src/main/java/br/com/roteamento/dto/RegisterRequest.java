package br.com.roteamento.dto;

import br.com.roteamento.model.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO PARA REQUISIÇÃO DE REGISTRO
 * 
 * MELHORIAS IMPLEMENTADAS:
 * - Validação de entrada
 * - Campos obrigatórios
 * - Tamanho mínimo de senha
 * - Formato de email
 * - Tipo de usuário
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
    private String senha;

    private String telefone;
    private String endereco;
    private Double latitude;
    private Double longitude;
    private Usuario.TipoUsuario tipoUsuario;
}
