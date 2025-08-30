package br.com.roteamento.controller;

import br.com.roteamento.dto.RotaDTO;
import br.com.roteamento.model.Rota;
import br.com.roteamento.service.RotaService;
import br.com.roteamento.exception.RotaNaoEncontradaException;
import br.com.roteamento.exception.UsuarioNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * CONTROLLER ROTA - Classe que expõe endpoints REST para rotas
 * 
 * CONCEITOS DIDÁTICOS EXPLICADOS:
 * 
 * 1. CAMADA DE APRESENTAÇÃO:
 *    - Recebe requisições HTTP
 *    - Valida dados de entrada
 *    - Chama serviços de negócio
 *    - Retorna respostas HTTP
 *    - Trata exceções da aplicação
 * 
 * 2. ANOTAÇÕES SPRING:
 *    - @RestController: Marca como controller REST
 *    - @RequestMapping: Define path base
 *    - @GetMapping: Endpoint GET
 *    - @PostMapping: Endpoint POST
 *    - @PutMapping: Endpoint PUT
 *    - @DeleteMapping: Endpoint DELETE
 *    - @PathVariable: Parâmetro da URL
 *    - @RequestParam: Parâmetro de query
 *    - @RequestBody: Corpo da requisição
 * 
 * 3. RESPOSTAS HTTP:
 *    - ResponseEntity: Wrapper para resposta HTTP
 *    - HttpStatus: Códigos de status HTTP
 *    - 200 OK: Sucesso
 *    - 201 Created: Recurso criado
 *    - 400 Bad Request: Dados inválidos
 *    - 404 Not Found: Recurso não encontrado
 *    - 500 Internal Server Error: Erro interno
 * 
 * 4. VALIDAÇÃO:
 *    - @Valid: Valida dados de entrada
 *    - Bean Validation: Anotações de validação
 *    - Tratamento de erros de validação
 */
@RestController
@RequestMapping("/rotas")
@RequiredArgsConstructor
public class RotaController {

    private static final Logger log = LoggerFactory.getLogger(RotaController.class);

    /**
     * SERVIÇO DE ROTAS
     * 
     * CONCEITOS:
     * - final: Campo imutável após inicialização
     * - Injeção via construtor (recomendado)
     * - Acesso à lógica de negócio
     */
    private final RotaService rotaService;

    /**
     * ENDPOINT PARA CRIAR UMA NOVA ROTA
     * 
     * CONCEITOS:
     * - @PostMapping: Método HTTP POST
     * - @RequestBody: Corpo da requisição JSON
     * - @Valid: Valida dados de entrada
     * - ResponseEntity: Resposta HTTP customizada
     * - HttpStatus.CREATED: Status 201
     * 
     * @param rotaDTO Dados da rota a ser criada
     * @return ResponseEntity com a rota criada
     */
    @PostMapping
    public ResponseEntity<RotaDTO> criarRota(@Valid @RequestBody RotaDTO rotaDTO) {
        log.info("Recebida requisição para criar rota. Coletor: {}", rotaDTO.getColetorId());

        try {
            RotaDTO rotaCriada = rotaService.criarRota(rotaDTO);
            log.info("Rota criada com sucesso. ID: {}", rotaCriada.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(rotaCriada);
        } catch (UsuarioNaoEncontradoException e) {
            log.error("Erro ao criar rota: coletor não encontrado. {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            log.error("Erro ao criar rota: usuário não é coletor. {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("Erro interno ao criar rota", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ENDPOINT PARA BUSCAR ROTA POR ID
     * 
     * CONCEITOS:
     * - @GetMapping("/{id}"): Path com parâmetro
     * - @PathVariable: Extrai parâmetro da URL
     * - HttpStatus.OK: Status 200
     * - HttpStatus.NOT_FOUND: Status 404
     * 
     * @param id ID da rota
     * @return ResponseEntity com a rota encontrada
     */
    @GetMapping("/{id}")
    public ResponseEntity<RotaDTO> buscarRotaPorId(@PathVariable Long id) {
        log.info("Recebida requisição para buscar rota por ID: {}", id);

        try {
            RotaDTO rota = rotaService.buscarRotaPorId(id);
            return ResponseEntity.ok(rota);
        } catch (RotaNaoEncontradaException e) {
            log.warn("Rota não encontrada. ID: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Erro interno ao buscar rota por ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ENDPOINT PARA LISTAR TODAS AS ROTAS
     * 
     * CONCEITOS:
     * - @GetMapping: Método HTTP GET
     * - List<RotaDTO>: Lista de DTOs
     * - ResponseEntity.ok(): Status 200
     * 
     * @return ResponseEntity com lista de rotas
     */
    @GetMapping
    public ResponseEntity<List<RotaDTO>> listarTodasRotas() {
        log.info("Recebida requisição para listar todas as rotas");

        try {
            List<RotaDTO> rotas = rotaService.listarTodasRotas();
            return ResponseEntity.ok(rotas);
        } catch (Exception e) {
            log.error("Erro interno ao listar rotas", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ENDPOINT PARA BUSCAR ROTAS POR COLETOR
     * 
     * CONCEITOS:
     * - @RequestParam: Parâmetro de query string
     * - Filtro por coletor específico
     * 
     * @param coletorId ID do coletor
     * @return ResponseEntity com lista de rotas do coletor
     */
    @GetMapping("/coletor")
    public ResponseEntity<List<RotaDTO>> buscarRotasPorColetor(@RequestParam("coletorId") Long coletorId) {
        log.info("Recebida requisição para buscar rotas do coletor: {}", coletorId);

        try {
            List<RotaDTO> rotas = rotaService.buscarRotasPorColetor(coletorId);
            return ResponseEntity.ok(rotas);
        } catch (Exception e) {
            log.error("Erro interno ao buscar rotas do coletor: {}", coletorId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ENDPOINT PARA BUSCAR ROTAS POR STATUS
     * 
     * CONCEITOS:
     * - @RequestParam: Parâmetro de query string
     * - Enum como parâmetro com conversor automático
     * 
     * @param status Status da rota
     * @return ResponseEntity com lista de rotas com o status
     */
    @GetMapping("/status")
    public ResponseEntity<List<RotaDTO>> buscarRotasPorStatus(@RequestParam("status") Rota.StatusRota status) {
        log.info("Recebida requisição para buscar rotas com status: {}", status);

        try {
            List<RotaDTO> rotas = rotaService.buscarRotasPorStatus(status);
            return ResponseEntity.ok(rotas);
        } catch (Exception e) {
            log.error("Erro interno ao buscar rotas por status: {}", status, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ENDPOINT PARA BUSCAR ROTAS ATIVAS
     * 
     * CONCEITOS:
     * - Endpoint específico para rotas ativas
     * - Filtro por status específicos
     * 
     * @return ResponseEntity com lista de rotas ativas
     */
    @GetMapping("/ativas")
    public ResponseEntity<List<RotaDTO>> buscarRotasAtivas() {
        log.info("Recebida requisição para buscar rotas ativas");

        try {
            List<RotaDTO> rotas = rotaService.buscarRotasAtivas();
            return ResponseEntity.ok(rotas);
        } catch (Exception e) {
            log.error("Erro interno ao buscar rotas ativas", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ENDPOINT PARA BUSCAR ROTAS POR COLETOR E STATUS
     * 
     * CONCEITOS:
     * - Múltiplos parâmetros de query
     * - Filtro combinado
     * 
     * @param coletorId ID do coletor
     * @param status Status da rota
     * @return ResponseEntity com lista de rotas
     */
    @GetMapping("/coletor-status")
    public ResponseEntity<List<RotaDTO>> buscarRotasPorColetorEStatus(
                        @RequestParam("coletorId") Long coletorId,
            @RequestParam("status") Rota.StatusRota status) {
        log.info("Recebida requisição para buscar rotas do coletor {} com status: {}", coletorId, status);

        try {
            List<RotaDTO> rotas = rotaService.buscarRotasPorColetorEStatus(coletorId, status);
            return ResponseEntity.ok(rotas);
        } catch (Exception e) {
            log.error("Erro interno ao buscar rotas do coletor {} com status: {}", coletorId, status, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ENDPOINT PARA ATUALIZAR ROTA
     * 
     * CONCEITOS:
     * - @PutMapping("/{id}"): Método HTTP PUT
     * - Atualização de recurso existente
     * - HttpStatus.OK: Status 200
     * 
     * @param id ID da rota
     * @param rotaDTO Novos dados da rota
     * @return ResponseEntity com a rota atualizada
     */
    @PutMapping("/{id}")
    public ResponseEntity<RotaDTO> atualizarRota(@PathVariable Long id, @Valid @RequestBody RotaDTO rotaDTO) {
        log.info("Recebida requisição para atualizar rota. ID: {}", id);

        try {
            RotaDTO rotaAtualizada = rotaService.atualizarRota(id, rotaDTO);
            return ResponseEntity.ok(rotaAtualizada);
        } catch (RotaNaoEncontradaException e) {
            log.warn("Rota não encontrada para atualização. ID: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Erro interno ao atualizar rota. ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ENDPOINT PARA EXCLUIR ROTA
     * 
     * CONCEITOS:
     * - @DeleteMapping("/{id}"): Método HTTP DELETE
     * - HttpStatus.NO_CONTENT: Status 204
     * 
     * @param id ID da rota
     * @return ResponseEntity vazio
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirRota(@PathVariable Long id) {
        log.info("Recebida requisição para excluir rota. ID: {}", id);

        try {
            rotaService.excluirRota(id);
            return ResponseEntity.noContent().build();
        } catch (RotaNaoEncontradaException e) {
            log.warn("Rota não encontrada para exclusão. ID: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Erro interno ao excluir rota. ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ENDPOINT PARA INICIAR ROTA
     * 
     * CONCEITOS:
     * - @PostMapping: Método HTTP POST
     * - Endpoint específico para ação de negócio
     * 
     * @param rotaId ID da rota
     * @return ResponseEntity com a rota iniciada
     */
    @PostMapping("/{rotaId}/iniciar")
    public ResponseEntity<RotaDTO> iniciarRota(@PathVariable Long rotaId) {
        log.info("Recebida requisição para iniciar rota. Rota ID: {}", rotaId);

        try {
            RotaDTO rotaIniciada = rotaService.iniciarRota(rotaId);
            return ResponseEntity.ok(rotaIniciada);
        } catch (RotaNaoEncontradaException e) {
            log.warn("Rota não encontrada para iniciar. ID: {}", rotaId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Erro interno ao iniciar rota. Rota ID: {}", rotaId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ENDPOINT PARA FINALIZAR ROTA
     * 
     * CONCEITOS:
     * - @PostMapping: Método HTTP POST
     * - Endpoint específico para ação de negócio
     * 
     * @param rotaId ID da rota
     * @return ResponseEntity com a rota finalizada
     */
    @PostMapping("/{rotaId}/finalizar")
    public ResponseEntity<RotaDTO> finalizarRota(@PathVariable Long rotaId) {
        log.info("Recebida requisição para finalizar rota. Rota ID: {}", rotaId);

        try {
            RotaDTO rotaFinalizada = rotaService.finalizarRota(rotaId);
            return ResponseEntity.ok(rotaFinalizada);
        } catch (RotaNaoEncontradaException e) {
            log.warn("Rota não encontrada para finalizar. ID: {}", rotaId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Erro interno ao finalizar rota. Rota ID: {}", rotaId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ENDPOINT PARA CANCELAR ROTA
     * 
     * CONCEITOS:
     * - @PostMapping: Método HTTP POST
     * - Endpoint específico para ação de negócio
     * 
     * @param rotaId ID da rota
     * @return ResponseEntity com a rota cancelada
     */
    @PostMapping("/{rotaId}/cancelar")
    public ResponseEntity<RotaDTO> cancelarRota(@PathVariable Long rotaId) {
        log.info("Recebida requisição para cancelar rota. Rota ID: {}", rotaId);

        try {
            RotaDTO rotaCancelada = rotaService.cancelarRota(rotaId);
            return ResponseEntity.ok(rotaCancelada);
        } catch (RotaNaoEncontradaException e) {
            log.warn("Rota não encontrada para cancelar. ID: {}", rotaId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Erro interno ao cancelar rota. Rota ID: {}", rotaId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ENDPOINT PARA CONTAR ROTAS POR STATUS
     * 
     * CONCEITOS:
     * - @GetMapping: Método HTTP GET
     * - Enum como parâmetro com conversor automático
     * - Retorna contagem (Long)
     * 
     * @param status Status da rota
     * @return ResponseEntity com a contagem
     */
    @GetMapping("/contar/status")
    public ResponseEntity<Long> contarRotasPorStatus(@RequestParam("status") Rota.StatusRota status) {
        log.info("Recebida requisição para contar rotas com status: {}", status);

        try {
            Long contagem = rotaService.contarRotasPorStatus(status);
            return ResponseEntity.ok(contagem);
        } catch (Exception e) {
            log.error("Erro interno ao contar rotas por status: {}", status, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ENDPOINT PARA ACEITAR UMA ROTA (COLETOR)
     * 
     * CONCEITOS:
     * - Aceitar rota muda status para EM_EXECUCAO
     * - Rota fica "bloqueada" para o coletor que aceitou
     * - Outros coletores não podem mais ver essa rota
     * 
     * @param rotaId ID da rota
     * @param coletorId ID do coletor que está aceitando
     * @return ResponseEntity com a rota atualizada
     */
    @PostMapping("/{rotaId}/aceitar")
    public ResponseEntity<RotaDTO> aceitarRota(@PathVariable Long rotaId) {
        // Por enquanto, usar coletor fixo para teste
        Long coletorId = 2L;
        log.info("Recebida requisição para aceitar rota. Rota: {}, Coletor: {}", rotaId, coletorId);

        try {
            RotaDTO rotaAtualizada = rotaService.aceitarRota(rotaId, coletorId);
            log.info("Rota aceita com sucesso. ID: {}", rotaId);
            return ResponseEntity.ok(rotaAtualizada);
        } catch (RotaNaoEncontradaException e) {
            log.error("Erro ao aceitar rota: rota não encontrada. {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalStateException e) {
            log.error("Erro ao aceitar rota: rota não está disponível. {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            log.error("Erro interno ao aceitar rota", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ENDPOINT PARA DESISTIR DE UMA ROTA (COLETOR)
     * 
     * CONCEITOS:
     * - Desistir só é permitido dentro de 10 segundos após aceitar
     * - Status volta para PLANEJADA (disponível para outros)
     * 
     * @param rotaId ID da rota
     * @param coletorId ID do coletor que está desistindo
     * @return ResponseEntity com a rota atualizada
     */
    @PostMapping("/{rotaId}/desistir")
    public ResponseEntity<RotaDTO> desistirRota(@PathVariable Long rotaId) {
        // Por enquanto, usar coletor fixo para teste
        Long coletorId = 2L;
        log.info("Recebida requisição para desistir rota. Rota: {}, Coletor: {}", rotaId, coletorId);

        try {
            RotaDTO rotaAtualizada = rotaService.desistirRota(rotaId, coletorId);
            log.info("Rota desistida com sucesso. ID: {}", rotaId);
            return ResponseEntity.ok(rotaAtualizada);
        } catch (RotaNaoEncontradaException e) {
            log.error("Erro ao desistir rota: rota não encontrada. {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalStateException e) {
            log.error("Erro ao desistir rota: tempo limite excedido. {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            log.error("Erro interno ao desistir rota", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ENDPOINT PARA CONCLUIR UMA ROTA (COLETOR)
     * 
     * CONCEITOS:
     * - Concluir rota muda status para CONCLUIDA
     * - Só o coletor que aceitou pode concluir
     * 
     * @param rotaId ID da rota
     * @param coletorId ID do coletor que está concluindo
     * @return ResponseEntity com a rota atualizada
     */
    @PostMapping("/{rotaId}/concluir")
    public ResponseEntity<RotaDTO> concluirRota(@PathVariable Long rotaId) {
        // Por enquanto, usar coletor fixo para teste
        Long coletorId = 2L;
        log.info("Recebida requisição para concluir rota. Rota: {}, Coletor: {}", rotaId, coletorId);

        try {
            RotaDTO rotaAtualizada = rotaService.concluirRota(rotaId, coletorId);
            log.info("Rota concluída com sucesso. ID: {}", rotaId);
            return ResponseEntity.ok(rotaAtualizada);
        } catch (RotaNaoEncontradaException e) {
            log.error("Erro ao concluir rota: rota não encontrada. {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalStateException e) {
            log.error("Erro ao concluir rota: operação não permitida. {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            log.error("Erro interno ao concluir rota", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ENDPOINT PARA LISTAR ROTAS DISPONÍVEIS PARA ACEITE
     * 
     * CONCEITOS:
     * - Mostra apenas rotas PLANEJADA (disponíveis para aceite)
     * - Não mostra rotas EM_EXECUCAO de outros coletores
     * 
     * @return ResponseEntity com lista de rotas disponíveis
     */
    @GetMapping("/disponiveis")
    public ResponseEntity<List<RotaDTO>> listarRotasDisponiveis() {
        log.info("Recebida requisição para listar rotas disponíveis");

        try {
            List<RotaDTO> rotasDisponiveis = rotaService.listarRotasDisponiveis();
            log.info("Rotas disponíveis listadas com sucesso. Total: {}", rotasDisponiveis.size());
            return ResponseEntity.ok(rotasDisponiveis);
        } catch (Exception e) {
            log.error("Erro interno ao listar rotas disponíveis", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * ENDPOINT PARA LISTAR ROTAS DE UM COLETOR
     * 
     * CONCEITOS:
     * - Mostra rotas PLANEJADA (disponíveis para aceite)
     * - Mostra rotas EM_EXECUCAO do próprio coletor
     * - Não mostra rotas EM_EXECUCAO de outros coletores
     * 
     * @param coletorId ID do coletor
     * @return ResponseEntity com lista de rotas do coletor
     */
    @GetMapping("/coletor/{coletorId}/minhas-rotas")
    public ResponseEntity<List<RotaDTO>> listarMinhasRotas(@PathVariable("coletorId") Long coletorId) {
        log.info("Recebida requisição para listar rotas do coletor. Coletor: {}", coletorId);

        try {
            List<RotaDTO> minhasRotas = rotaService.listarMinhasRotas(coletorId);
            log.info("Rotas do coletor listadas com sucesso. Total: {}", minhasRotas.size());
            return ResponseEntity.ok(minhasRotas);
        } catch (Exception e) {
            log.error("Erro interno ao listar rotas do coletor", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
} 