package br.com.roteamento.controller;

import br.com.roteamento.dto.EstatisticasSistemaDTO;
import br.com.roteamento.model.Coleta;
import br.com.roteamento.model.Rota;
import br.com.roteamento.model.Material;
import br.com.roteamento.repository.ColetaRepository;
import br.com.roteamento.repository.RotaRepository;
import br.com.roteamento.repository.UsuarioRepository;
import br.com.roteamento.repository.MaterialRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * CONTROLLER DE ESTATÍSTICAS - EstatisticasController
 * 
 * CONCEITOS DIDÁTICOS EXPLICADOS:
 * 
 * 1. CONTROLLER REST:
 *    - @RestController: Marca classe como controller REST
 *    - @RequestMapping: Define path base para endpoints
 *    - @GetMapping: Endpoint GET para estatísticas
 * 
 * 2. INJEÇÃO DE DEPENDÊNCIA:
 *    - @Autowired: Injeta dependências automaticamente
 *    - Repository Pattern: Acesso a dados via repositories
 * 
 * 3. MANIPULAÇÃO DE DADOS:
 *    - ResponseEntity: Resposta HTTP com status e dados
 *    - Map: Estrutura de dados para estatísticas
 *    - Stream API: Processamento funcional de coleções
 * 
 * 4. ESTATÍSTICAS E MÉTRICAS:
 *    - Contagem de entidades
 *    - Agrupamento por categorias
 *    - Cálculo de valores totais
 *    - Filtros por status e datas
 */
@RestController
@RequestMapping("/estatisticas")
@RequiredArgsConstructor
@Slf4j
public class EstatisticasController {

    private final UsuarioRepository usuarioRepository;
    private final MaterialRepository materialRepository;
    private final ColetaRepository coletaRepository;
    private final RotaRepository rotaRepository;

    /**
     * Endpoint de health check global
     */
    @GetMapping("/health")
    public String health() {
        return "UP";
    }

    /**
     * Endpoint de health check para API v1
     */
    @GetMapping("/api/v1/health")
    public String healthApi() {
        return "UP";
    }

    /**
     * Obter estatísticas gerais do sistema
     */
    @GetMapping("/gerais")
    public ResponseEntity<EstatisticasSistemaDTO> obterEstatisticas() {
        log.info("Recebida requisição para obter estatísticas do sistema");
        
        try {
            // Contadores básicos
            long totalUsuarios = usuarioRepository.count();
            long totalMateriais = materialRepository.count();
            long totalColetas = coletaRepository.count();
            long totalRotas = rotaRepository.count();
            
            // Coletas pendentes (SOLICITADA, EM_ANALISE)
            long coletasPendentes = coletaRepository.findAll().stream()
                    .filter(coleta -> "SOLICITADA".equals(coleta.getStatus()) || "EM_ANALISE".equals(coleta.getStatus()))
                    .count();
            
            // Rotas em execução
            long rotasEmExecucao = rotaRepository.findAll().stream()
                    .filter(rota -> "EM_EXECUCAO".equals(rota.getStatus()))
                    .count();
            
            // Valor total estimado das coletas (simulado)
            double valorTotalEstimado = 0.0;
            
            // Materiais por categoria
            Map<String, Long> materiaisPorCategoria = materialRepository.findAll().stream()
                    .collect(Collectors.groupingBy(
                            material -> material.getCategoria().toString(),
                            Collectors.counting()
                    ));
            
            // Coletas por status
            Map<String, Long> coletasPorStatus = coletaRepository.findAll().stream()
                    .collect(Collectors.groupingBy(
                            coleta -> coleta.getStatus() != null ? coleta.getStatus().toString() : "SEM_STATUS",
                            Collectors.counting()
                    ));
            
            // Criar DTO com as estatísticas
            EstatisticasSistemaDTO estatisticas = new EstatisticasSistemaDTO(
                totalUsuarios, totalMateriais, totalColetas, totalRotas,
                coletasPendentes, rotasEmExecucao, valorTotalEstimado,
                materiaisPorCategoria, coletasPorStatus
            );
            
            log.info("Estatísticas calculadas com sucesso");
            return ResponseEntity.ok(estatisticas);
        } catch (Exception e) {
            log.error("Erro ao calcular estatísticas: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Obter coletas recentes
     */
    @GetMapping("/coletas-recentes")
    public ResponseEntity<List<Coleta>> obterColetasRecentes() {
        log.info("Recebida requisição para obter coletas recentes");
        
        try {
            // Buscar coletas dos últimos 7 dias
            LocalDateTime dataLimite = LocalDateTime.now().minusDays(7);
            List<Coleta> coletas = coletaRepository.findAll().stream()
                    .filter(coleta -> coleta.getDataCriacao() != null && coleta.getDataCriacao().isAfter(dataLimite))
                    .sorted((c1, c2) -> c2.getDataCriacao().compareTo(c1.getDataCriacao()))
                    .limit(10)
                    .collect(Collectors.toList());
            
            log.info("Coletas recentes encontradas: {}", coletas.size());
            return ResponseEntity.ok(coletas);
        } catch (Exception e) {
            log.error("Erro ao buscar coletas recentes: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Obter rotas ativas
     */
    @GetMapping("/rotas-ativas")
    public ResponseEntity<List<Rota>> obterRotasAtivas() {
        log.info("Recebida requisição para obter rotas ativas");
        
        try {
            List<Rota> rotas = rotaRepository.findAll().stream()
                    .filter(rota -> "PLANEJADA".equals(rota.getStatus()) || "EM_EXECUCAO".equals(rota.getStatus()))
                    .limit(10)
                    .collect(Collectors.toList());
            
            log.info("Rotas ativas encontradas: {}", rotas.size());
            return ResponseEntity.ok(rotas);
        } catch (Exception e) {
            log.error("Erro ao buscar rotas ativas: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Obter materiais populares
     */
    @GetMapping("/materiais-populares")
    public ResponseEntity<List<Material>> obterMateriaisPopulares() {
        log.info("Recebida requisição para obter materiais populares");
        
        try {
            List<Material> materiais = materialRepository.findAll().stream()
                    .filter(material -> material.getAtivo() != null && material.getAtivo())
                    .limit(10)
                    .collect(Collectors.toList());
            
            log.info("Materiais populares encontrados: {}", materiais.size());
            return ResponseEntity.ok(materiais);
        } catch (Exception e) {
            log.error("Erro ao buscar materiais populares: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Obter pontos para o mapa
     */
    @GetMapping("/mapa-pontos")
    public ResponseEntity<Map<String, Object>> obterMapaPontos() {
        log.info("Recebida requisição para obter pontos do mapa");
        
        try {
            Map<String, Object> dados = new HashMap<>();
            
            // Buscar usuários com coordenadas
            List<Map<String, Object>> usuarios = usuarioRepository.findAll().stream()
                    .filter(usuario -> usuario.getLatitude() != null && usuario.getLongitude() != null)
                    .map(usuario -> {
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("id", usuario.getId());
                        userData.put("nome", usuario.getNome());
                        userData.put("latitude", usuario.getLatitude());
                        userData.put("longitude", usuario.getLongitude());
                        userData.put("tipo", usuario.getTipoUsuario() != null ? usuario.getTipoUsuario().toString() : "USUARIO");
                        userData.put("status", usuario.getStatus() != null ? usuario.getStatus().toString() : "ATIVO");
                        return userData;
                    })
                    .collect(Collectors.toList());
            
            // Buscar coletas com coordenadas
            List<Map<String, Object>> coletas = coletaRepository.findAll().stream()
                    .filter(coleta -> coleta.getLatitude() != null && coleta.getLongitude() != null)
                    .map(coleta -> {
                        Map<String, Object> coletaData = new HashMap<>();
                        coletaData.put("id", coleta.getId());
                        coletaData.put("latitude", coleta.getLatitude());
                        coletaData.put("longitude", coleta.getLongitude());
                        coletaData.put("status", coleta.getStatus() != null ? coleta.getStatus().toString() : "SOLICITADA");
                        coletaData.put("endereco", coleta.getEndereco());
                        if (coleta.getMaterial() != null) {
                            coletaData.put("material", coleta.getMaterial().getNome());
                            coletaData.put("categoria", coleta.getMaterial().getCategoria().toString());
                        }
                        return coletaData;
                    })
                    .collect(Collectors.toList());
            
            // Buscar rotas com coordenadas
            List<Map<String, Object>> rotas = rotaRepository.findAll().stream()
                    .filter(rota -> rota.getLatitudeInicio() != null && rota.getLongitudeInicio() != null)
                    .map(rota -> {
                        Map<String, Object> rotaData = new HashMap<>();
                        rotaData.put("id", rota.getId());
                        rotaData.put("nome", rota.getNome());
                        rotaData.put("latitudeInicio", rota.getLatitudeInicio());
                        rotaData.put("longitudeInicio", rota.getLongitudeInicio());
                        rotaData.put("latitudeFim", rota.getLatitudeFim());
                        rotaData.put("longitudeFim", rota.getLongitudeFim());
                        rotaData.put("status", rota.getStatus() != null ? rota.getStatus().toString() : "PLANEJADA");
                        return rotaData;
                    })
                    .collect(Collectors.toList());
            
            dados.put("usuarios", usuarios);
            dados.put("coletas", coletas);
            dados.put("rotas", rotas);
            
            log.info("Pontos do mapa encontrados - Usuários: {}, Coletas: {}, Rotas: {}", 
                     usuarios.size(), coletas.size(), rotas.size());
            
            return ResponseEntity.ok(dados);
        } catch (Exception e) {
            log.error("Erro ao buscar pontos do mapa: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
} 