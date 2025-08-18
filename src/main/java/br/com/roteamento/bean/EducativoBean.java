package br.com.roteamento.bean;

import br.com.roteamento.dto.ColetaDTO;
import br.com.roteamento.service.ColetaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.faces.bean.ManagedBean;
import jakarta.faces.bean.SessionScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.ValidatorException;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

/**
 * MANAGED BEAN EDUCATIVO - Demonstra conceitos básicos de JSF
 * 
 * CONCEITOS DIDÁTICOS EXPLICADOS:
 * 
 * 1. MANAGED BEAN:
 *    - @ManagedBean: Marca classe como bean gerenciado pelo JSF
 *    - @SessionScoped: Escopo de sessão (dados persistem durante a sessão)
 *    - @Component: Integração com Spring
 * 
 * 2. VALIDAÇÃO JSF:
 *    - Validação no lado cliente e servidor
 *    - Mensagens de erro customizadas
 *    - Validação programática
 * 
 * 3. GESTÃO DE ESTADO:
 *    - Dados mantidos entre requisições
 *    - Navegação entre páginas
 *    - Formulários com estado
 */
@ManagedBean
@SessionScoped
@Component
@RequiredArgsConstructor
@Slf4j
public class EducativoBean {

    private final ColetaService coletaService;

    // CONCEITO DIDÁTICO - Propriedades do Bean
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @Email(message = "Email deve ser válido")
    private String email;

    private String tecnologiaPreferida = "jsf";

    private String mensagem;

    // CONCEITO DIDÁTICO - Lista de dados para demonstração
    private List<ColetaDTO> coletas;

    /**
     * MÉTODO PARA PROCESSAR FORMULÁRIO
     * 
     * CONCEITOS:
     * - Action method: Chamado pelo botão do formulário
     * - Navegação: Retorna nome da página de destino
     * - Validação: Executada antes da execução
     * 
     * @return nome da página de destino
     */
    public String processarFormulario() {
        log.info("Processando formulário JSF - Nome: {}, Email: {}, Tecnologia: {}", 
                nome, email, tecnologiaPreferida);

        // CONCEITO DIDÁTICO - Lógica de negócio
        if (nome != null && !nome.trim().isEmpty()) {
            mensagem = "Formulário processado com sucesso!";
            log.info("Formulário processado: {}", mensagem);
            return "sucesso"; // Navegação para página de sucesso
        } else {
            mensagem = "Erro: Nome é obrigatório";
            log.warn("Erro no formulário: {}", mensagem);
            return null; // Permanece na mesma página
        }
    }

    /**
     * VALIDADOR CUSTOMIZADO PARA EMAIL
     * 
     * CONCEITOS:
     * - Validator: Validação programática
     * - FacesContext: Contexto JSF
     - UIComponent: Componente sendo validado
     * - ValidatorException: Exceção de validação
     * 
     * @param context contexto JSF
     * @param component componente sendo validado
     * @param value valor a ser validado
     * @throws ValidatorException se validação falhar
     */
    public void validarEmail(FacesContext context, UIComponent component, Object value) 
            throws ValidatorException {
        
        String email = (String) value;
        
        if (email != null && !email.isEmpty()) {
            // CONCEITO DIDÁTICO - Validação customizada
            if (!email.contains("@") || !email.contains(".")) {
                throw new ValidatorException(
                    new javax.faces.application.FacesMessage(
                        javax.faces.application.FacesMessage.SEVERITY_ERROR,
                        "Email inválido",
                        "O email deve conter @ e domínio válido"
                    )
                );
            }
            
            // Validação adicional: domínio deve ter pelo menos 2 caracteres
            String[] partes = email.split("@");
            if (partes.length != 2 || partes[1].length() < 2) {
                throw new ValidatorException(
                    new javax.faces.application.FacesMessage(
                        javax.faces.application.FacesMessage.SEVERITY_ERROR,
                        "Email inválido",
                        "O domínio do email deve ser válido"
                    )
                );
            }
        }
    }

    /**
     * MÉTODO PARA VER DETALHES DE UMA COLETA
     * 
     * CONCEITOS:
     * - Action method com parâmetro
     * - Navegação com dados
     * - Gerenciamento de estado
     * 
     * @param coleta coleta selecionada
     * @return nome da página de destino
     */
    public String verColeta(ColetaDTO coleta) {
        log.info("Visualizando coleta: {}", coleta.getId());
        
        // CONCEITO DIDÁTICO - Armazenar dados para próxima página
        // Em uma aplicação real, seria armazenado no escopo de sessão
        mensagem = "Visualizando coleta ID: " + coleta.getId();
        
        return "detalhes-coleta";
    }

    /**
     * MÉTODO PARA EDITAR UMA COLETA
     * 
     * CONCEITOS:
     * - Action method com parâmetro
     * - Preparação de dados para edição
     * - Navegação para formulário
     * 
     * @param coleta coleta a ser editada
     * @return nome da página de destino
     */
    public String editarColeta(ColetaDTO coleta) {
        log.info("Editando coleta: {}", coleta.getId());
        
        // CONCEITO DIDÁTICO - Preparar dados para edição
        // Em uma aplicação real, seria armazenado no escopo de sessão
        mensagem = "Editando coleta ID: " + coleta.getId();
        
        return "editar-coleta";
    }

    /**
     * MÉTODO PARA CARREGAR COLETAS
     * 
     * CONCEITOS:
     * - Lazy loading: Carrega dados quando necessário
     * - Integração com serviços Spring
     * - Tratamento de exceções
     * 
     * @return lista de coletas
     */
    public List<ColetaDTO> getColetas() {
        if (coletas == null) {
            try {
                log.info("Carregando coletas para demonstração JSF");
                coletas = coletaService.listarTodasColetas();
            } catch (Exception e) {
                log.error("Erro ao carregar coletas", e);
                mensagem = "Erro ao carregar coletas: " + e.getMessage();
            }
        }
        return coletas;
    }

    /**
     * MÉTODO PARA OBTER VANTAGENS DO JSF
     * 
     * @return lista de vantagens
     */
    public List<String> getVantagens() {
        return List.of(
            "Componentes reutilizáveis",
            "Gerenciamento de estado",
            "Validação integrada",
            "Eventos e listeners",
            "Integração com Java EE",
            "Templates e layouts",
            "Navegação declarativa"
        );
    }

    /**
     * MÉTODO PARA OBTER DESVANTAGENS DO JSF
     * 
     * @return lista de desvantagens
     */
    public List<String> getDesvantagens() {
        return List.of(
            "Curva de aprendizado",
            "Menos flexível que frameworks modernos",
            "Performance em aplicações complexas",
            "Integração limitada com JavaScript",
            "Debugging complexo",
            "Menos popular atualmente",
            "Dependência de servidor Java EE"
        );
    }

    /**
     * MÉTODO PARA LIMPAR FORMULÁRIO
     * 
     * CONCEITOS:
     * - Reset de dados
     * - Gerenciamento de estado
     * - Navegação
     * 
     * @return nome da página atual
     */
    public String limparFormulario() {
        log.info("Limpando formulário JSF");
        
        nome = null;
        email = null;
        tecnologiaPreferida = "jsf";
        mensagem = null;
        
        return null; // Permanece na mesma página
    }

    // CONCEITO DIDÁTICO - Getters e Setters
    // JSF usa reflection para acessar propriedades

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTecnologiaPreferida() {
        return tecnologiaPreferida;
    }

    public void setTecnologiaPreferida(String tecnologiaPreferida) {
        this.tecnologiaPreferida = tecnologiaPreferida;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public void setColetas(List<ColetaDTO> coletas) {
        this.coletas = coletas;
    }
}
