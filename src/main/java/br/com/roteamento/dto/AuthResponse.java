package br.com.roteamento.dto;

import br.com.roteamento.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO PARA RESPOSTA DE AUTENTICAÇÃO
 * 
 * MELHORIAS IMPLEMENTADAS:
 * - Token de acesso
 * - Refresh token
 * - Informações do usuário
 * - Tempo de expiração
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;
    private Usuario usuario;
}
