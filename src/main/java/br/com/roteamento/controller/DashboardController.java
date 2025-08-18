package br.com.roteamento.controller;

import br.com.roteamento.dto.DashboardStatsDTO;
import br.com.roteamento.model.Coleta;
import br.com.roteamento.model.Rota;
import br.com.roteamento.model.Usuario;
import br.com.roteamento.model.Material;
import br.com.roteamento.repository.ColetaRepository;
import br.com.roteamento.repository.RotaRepository;
import br.com.roteamento.repository.UsuarioRepository;
import br.com.roteamento.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * CONTROLLER DE DASHBOARD - DashboardController
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
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private ColetaRepository coletaRepository;

    @Autowired
    private RotaRepository rotaRepository;

    /**
     * Endpoint de health check
     */
    @GetMapping("/health")
    public String health() {
        return "UP";
    }

    /**
     * Obter estatísticas gerais do dashboard
     */
    @GetMapping("/estatisticas")
    public ResponseEntity<DashboardStatsDTO> obterEstatisticas() {
        System.out.println("Recebida requisição para obter estatísticas do dashboard");
        
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
            DashboardStatsDTO estatisticas = new DashboardStatsDTO(
                totalUsuarios, totalMateriais, totalColetas, totalRotas,
                coletasPendentes, rotasEmExecucao, valorTotalEstimado,
                materiaisPorCategoria, coletasPorStatus
            );
            
            System.out.println("Estatísticas calculadas com sucesso");
            return ResponseEntity.ok(estatisticas);
        } catch (Exception e) {
            System.err.println("Erro ao calcular estatísticas: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Obter coletas recentes
     */
    @GetMapping("/coletas-recentes")
    public ResponseEntity<List<Coleta>> obterColetasRecentes() {
        System.out.println("Recebida requisição para obter coletas recentes");
        
        try {
            // Buscar coletas dos últimos 7 dias
            LocalDateTime dataLimite = LocalDateTime.now().minusDays(7);
            List<Coleta> coletas = coletaRepository.findAll().stream()
                    .filter(coleta -> coleta.getDataCriacao() != null && coleta.getDataCriacao().isAfter(dataLimite))
                    .sorted((c1, c2) -> c2.getDataCriacao().compareTo(c1.getDataCriacao()))
                    .limit(10)
                    .collect(Collectors.toList());
            
            System.out.println("Coletas recentes encontradas: " + coletas.size());
            return ResponseEntity.ok(coletas);
        } catch (Exception e) {
            System.err.println("Erro ao buscar coletas recentes: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Obter rotas ativas
     */
    @GetMapping("/rotas-ativas")
    public ResponseEntity<List<Rota>> obterRotasAtivas() {
        System.out.println("Recebida requisição para obter rotas ativas");
        
        try {
            List<Rota> rotas = rotaRepository.findAll().stream()
                    .filter(rota -> "PLANEJADA".equals(rota.getStatus()) || "EM_EXECUCAO".equals(rota.getStatus()))
                    .limit(10)
                    .collect(Collectors.toList());
            
            System.out.println("Rotas ativas encontradas: " + rotas.size());
            return ResponseEntity.ok(rotas);
        } catch (Exception e) {
            System.err.println("Erro ao buscar rotas ativas: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Obter materiais populares
     */
    @GetMapping("/materiais-populares")
    public ResponseEntity<List<Material>> obterMateriaisPopulares() {
        System.out.println("Recebida requisição para obter materiais populares");
        
        try {
            List<Material> materiais = materialRepository.findAll().stream()
                    .filter(material -> material.getAtivo() != null && material.getAtivo())
                    .limit(10)
                    .collect(Collectors.toList());
            
            System.out.println("Materiais populares encontrados: " + materiais.size());
            return ResponseEntity.ok(materiais);
        } catch (Exception e) {
            System.err.println("Erro ao buscar materiais populares: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Obter pontos para o mapa
     */
    @GetMapping("/mapa-pontos")
    public ResponseEntity<Map<String, Object>> obterMapaPontos() {
        System.out.println("Recebida requisição para obter pontos do mapa");
        
        try {
            Map<String, Object> pontos = new HashMap<String, Object>();
            
            // Usuários com coordenadas
            List<Usuario> usuarios = usuarioRepository.findAll().stream()
                    .filter(usuario -> usuario.getLatitude() != null && usuario.getLongitude() != null)
                    .collect(Collectors.toList());
            
            List<Map<String, Object>> pontosUsuarios = usuarios.stream()
                    .map(this::converterUsuarioParaPonto)
                    .collect(Collectors.toList());
            
            // Coletas com coordenadas
            List<Coleta> coletas = coletaRepository.findAll().stream()
                    .filter(coleta -> coleta.getLatitude() != null && coleta.getLongitude() != null)
                    .collect(Collectors.toList());
            
            List<Map<String, Object>> pontosColetas = coletas.stream()
                    .map(this::converterColetaParaPonto)
                    .collect(Collectors.toList());
            
            // Rotas com coordenadas
            List<Rota> rotas = rotaRepository.findAll().stream()
                    .filter(rota -> rota.getLatitudeInicio() != null && rota.getLongitudeInicio() != null)
                    .collect(Collectors.toList());
            
            List<Map<String, Object>> pontosRotas = rotas.stream()
                    .map(this::converterRotaParaPonto)
                    .collect(Collectors.toList());
            
            pontos.put("usuarios", pontosUsuarios);
            pontos.put("coletas", pontosColetas);
            pontos.put("rotas", pontosRotas);
            
            System.out.println("Pontos do mapa calculados: " + 
                    pontosUsuarios.size() + " usuários, " + 
                    pontosColetas.size() + " coletas, " + 
                    pontosRotas.size() + " rotas");
            return ResponseEntity.ok(pontos);
        } catch (Exception e) {
            System.err.println("Erro ao calcular pontos do mapa: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Converter Usuario para ponto do mapa
     */
    private Map<String, Object> converterUsuarioParaPonto(Usuario usuario) {
        Map<String, Object> ponto = new HashMap<String, Object>();
        ponto.put("id", usuario.getId());
        ponto.put("lat", usuario.getLatitude());
        ponto.put("lng", usuario.getLongitude());
        ponto.put("title", usuario.getNome());
        ponto.put("type", "usuario");
        ponto.put("data", usuario);
        return ponto;
    }

    /**
     * Converter Coleta para ponto do mapa
     */
    private Map<String, Object> converterColetaParaPonto(Coleta coleta) {
        Map<String, Object> ponto = new HashMap<String, Object>();
        ponto.put("id", coleta.getId());
        ponto.put("lat", coleta.getLatitude());
        ponto.put("lng", coleta.getLongitude());
        ponto.put("title", "Coleta #" + coleta.getId());
        ponto.put("type", "coleta");
        ponto.put("data", coleta);
        return ponto;
    }

    /**
     * Converter Rota para ponto do mapa
     */
    private Map<String, Object> converterRotaParaPonto(Rota rota) {
        Map<String, Object> ponto = new HashMap<String, Object>();
        ponto.put("id", rota.getId());
        ponto.put("lat", rota.getLatitudeInicio());
        ponto.put("lng", rota.getLongitudeInicio());
        ponto.put("title", rota.getNome());
        ponto.put("type", "rota");
        ponto.put("data", rota);
        return ponto;
    }
} 