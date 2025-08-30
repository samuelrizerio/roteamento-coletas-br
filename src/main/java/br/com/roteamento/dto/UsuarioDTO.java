package br.com.roteamento.dto;

import br.com.roteamento.model.Usuario;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO (DATA TRANSFER OBJECT) - UsuarioDTO
 * 
 * CONCEITOS DIDÁTICOS EXPLICADOS:
 * 
 * 1. PADRÃO DTO (DATA TRANSFER OBJECT):
 *    - Objeto usado para transferir dados entre camadas
 *    - Separa a camada de apresentação da camada de domínio
 *    - Evita exposição direta das entidades JPA
 *    - Controle de quais dados são enviados/recebidos
 * 
 * 2. VALIDAÇÃO DE DADOS:
 *    - @NotBlank: Valida que o campo não pode ser nulo ou vazio
 *    - @Email: Valida se o campo contém um email válido
 *    - @Size: Define o tamanho mínimo e máximo do campo
 *    - @NotNull: Campo obrigatório
 * 
 * 3. ANOTAÇÕES JACKSON:
 *    - @JsonProperty: Define o nome do campo no JSON
 *    - @JsonIgnore: Ignora o campo na serialização/deserialização
 *    - @JsonFormat: Define o formato de data/hora
 * 
 * 4. SEGURANÇA:
 *    - Senha não é retornada nas consultas
 *    - Dados sensíveis são ocultados
 *    - Controle de acesso aos dados
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    /**
     * ID do usuário
     * 
     * CONCEITOS:
     * - Long: Tipo primitivo para números inteiros grandes
     * - Pode ser nulo em operações de criação
     * - Preenchido automaticamente pelo banco
     */
    private Long id;

    /**
     * Nome completo do usuário
     * 
     * CONCEITOS:
     * - @NotBlank: Validação que impede valores nulos ou vazios
     * - @Size: Define tamanho mínimo e máximo
     * - Mensagem de erro personalizada
     */
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    /**
     * Email do usuário (usado para login)
     * 
     * CONCEITOS:
     * - @Email: Validação de formato de email
     * - @NotBlank: Campo obrigatório
     * - Deve ser único no sistema
     */
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;

    /**
     * Senha do usuário (apenas para criação/atualização)
     * 
     * CONCEITOS:
     * - @JsonProperty(access = JsonProperty.Access.WRITE_ONLY): Permite escrita mas não leitura
     * - @Size: Define tamanho mínimo para segurança
     * - Será criptografada antes de salvar
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
    private String senha;

    /**
     * Telefone do usuário
     * 
     * CONCEITOS:
     * - Campo opcional
     * - Pode ser nulo
     * - Útil para contato
     */
    private String telefone;

    /**
     * Endereço do usuário
     * 
     * CONCEITOS:
     * - Campo opcional
     * - Pode ser nulo
     * - Útil para geolocalização
     */
    private String endereco;

    /**
     * Latitude da localização do usuário
     * 
     * CONCEITOS:
     * - Double: Tipo primitivo para números decimais
     * - Pode ser nulo
     * - Usado para cálculos de rota
     */
    private Double latitude;

    /**
     * Longitude da localização do usuário
     * 
     * CONCEITOS:
     * - Double: Tipo primitivo para números decimais
     * - Pode ser nulo
     * - Usado junto com latitude
     */
    private Double longitude;

    /**
     * Tipo de usuário
     * 
     * CONCEITOS:
     * - @NotNull: Campo obrigatório
     * - Enum: Tipo especial que define constantes
     * - Define o papel do usuário no sistema
     */
    @NotNull(message = "Tipo de usuário é obrigatório")
    private Usuario.TipoUsuario tipoUsuario;

    /**
     * Status do usuário
     * 
     * CONCEITOS:
     * - Enum: Tipo especial que define constantes
     * - Pode ser nulo (usa valor padrão)
     * - Controla o acesso do usuário
     */
    private Usuario.StatusUsuario status;

    /**
     * Data de criação do usuário
     * 
     * CONCEITOS:
     * - @JsonFormat: Define formato de data/hora
     * - LocalDateTime: Tipo moderno para datas e horas
     * - Pode ser nulo em operações de criação
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataCriacao;

    /**
     * Data da última atualização
     * 
     * CONCEITOS:
     * - @JsonFormat: Define formato de data/hora
     * - LocalDateTime: Tipo moderno para datas e horas
     * - Pode ser nulo em operações de criação
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataAtualizacao;

    /**
     * MÉTODO CONSTRUTOR COM PARÂMETROS ESSENCIAIS
     * 
     * CONCEITOS:
     * - CONSTRUTOR: Método especial que inicializa o objeto
     * - Sobrecarga: Múltiplos construtores com diferentes parâmetros
     */
    public UsuarioDTO(String nome, String email, String senha, Usuario.TipoUsuario tipoUsuario) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
    }

    /**
     * MÉTODO PARA CONVERTER ENTIDADE PARA DTO
     * 
     * CONCEITOS:
     * - MÉTODO ESTÁTICO: Pode ser chamado sem instanciar a classe
     * - FACTORY METHOD: Padrão para criação de objetos
     * - MAPEAMENTO: Converte entidade para DTO
     */
    public static UsuarioDTO fromEntity(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setTelefone(usuario.getTelefone());
        dto.setEndereco(usuario.getEndereco());
        dto.setLatitude(usuario.getLatitude());
        dto.setLongitude(usuario.getLongitude());
        dto.setTipoUsuario(usuario.getTipoUsuario());
        dto.setStatus(usuario.getStatus());
        dto.setDataCriacao(usuario.getDataCriacao());
        dto.setDataAtualizacao(usuario.getDataAtualizacao());

        return dto;
    }

    /**
     * MÉTODO PARA CONVERTER DTO PARA ENTIDADE
     * 
     * CONCEITOS:
     * - MÉTODO: Função que define comportamento da classe
     * - MAPEAMENTO: Converte DTO para entidade
     * - VALIDAÇÃO: Verifica se os dados são válidos
     */
    public Usuario toEntity() {
        Usuario usuario = new Usuario();
        usuario.setId(this.id);
        usuario.setNome(this.nome);
        usuario.setEmail(this.email);
        usuario.setSenha(this.senha);
        usuario.setTelefone(this.telefone);
        usuario.setEndereco(this.endereco);
        usuario.setLatitude(this.latitude);
        usuario.setLongitude(this.longitude);
        usuario.setTipoUsuario(this.tipoUsuario);
        usuario.setStatus(this.status != null ? this.status : Usuario.StatusUsuario.ATIVO);

        return usuario;
    }

    /**
     * MÉTODO PARA VERIFICAR SE É UM COLETOR
     * 
     * CONCEITOS:
     * - MÉTODO: Função que define comportamento da classe
     * - Boolean: Tipo de retorno para valores true/false
     * - LÓGICA DE NEGÓCIO: Define regras do sistema
     */
    public boolean isColetor() {
        return this.tipoUsuario == Usuario.TipoUsuario.COLETOR;
    }

    /**
     * MÉTODO PARA VERIFICAR SE É UM USUÁRIO RESIDENCIAL
     * 
     * CONCEITOS:
     * - MÉTODO: Função que define comportamento da classe
     * - Boolean: Tipo de retorno para valores true/false
     */
    public boolean isResidencial() {
        return this.tipoUsuario == Usuario.TipoUsuario.RESIDENCIAL;
    }

    /**
     * MÉTODO PARA VERIFICAR SE É UM USUÁRIO COMERCIAL
     * 
     * CONCEITOS:
     * - MÉTODO: Função que define comportamento da classe
     * - Boolean: Tipo de retorno para valores true/false
     */
    public boolean isComercial() {
        return this.tipoUsuario == Usuario.TipoUsuario.COMERCIAL;
    }

    /**
     * MÉTODO PARA VERIFICAR SE O USUÁRIO ESTÁ ATIVO
     * 
     * CONCEITOS:
     * - MÉTODO: Função que define comportamento da classe
     * - Boolean: Tipo de retorno para valores true/false
     */
    public boolean isAtivo() {
        return this.status == Usuario.StatusUsuario.ATIVO;
    }

    /**
     * MÉTODO PARA VERIFICAR SE O USUÁRIO TEM LOCALIZAÇÃO
     * 
     * CONCEITOS:
     * - MÉTODO: Função que define comportamento da classe
     * - Boolean: Tipo de retorno para valores true/false
     * - VALIDAÇÃO: Verifica se as coordenadas estão preenchidas
     */
    public boolean temLocalizacao() {
        return this.latitude != null && this.longitude != null;
    }

    /**
     * MÉTODO TO STRING PERSONALIZADO
     * 
     * CONCEITOS:
     * - @Override: Indica que está sobrescrevendo método da classe pai
     * - toString(): Método herdado de Object para representação em string
     * - SEGURANÇA: Não inclui dados sensíveis como senha
     */
    @Override
    public String toString() {
        return String.format("UsuarioDTO{id=%d, nome='%s', email='%s', tipoUsuario=%s, status=%s}",
                id, nome, email, tipoUsuario, status);
    }
} 