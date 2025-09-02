package br.com.roteamento.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.roteamento.model.Coleta;
import br.com.roteamento.model.Material;
import br.com.roteamento.model.Usuario;
import br.com.roteamento.repository.ColetaRepository;
import br.com.roteamento.repository.MaterialRepository;
import br.com.roteamento.repository.RotaRepository;
import br.com.roteamento.repository.UsuarioRepository;

/**
 * Serviço de inicialização de dados para o sistema
 */
@Configuration
public class DataInitializationService {

    private static final Logger log = LoggerFactory.getLogger(DataInitializationService.class);
    
    private final MaterialRepository materialRepository;
    private final UsuarioRepository usuarioRepository;
    // private final RotaRepository rotaRepository; // Campo não utilizado no momento
    private final ColetaRepository coletaRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializationService(MaterialRepository materialRepository, 
                                   UsuarioRepository usuarioRepository, 
                                   RotaRepository rotaRepository, 
                                   ColetaRepository coletaRepository, 
                                   PasswordEncoder passwordEncoder) {
        this.materialRepository = materialRepository;
        this.usuarioRepository = usuarioRepository;
        // this.rotaRepository = rotaRepository; // Campo não utilizado no momento
        this.coletaRepository = coletaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * INICIALIZADOR DE MATERIAIS (AMBOS OS AMBIENTES)
     * 
     * CONCEITOS:
     * - @Bean: Define um bean gerenciado pelo Spring
     * - @Profile("!test"): Executa em todos os perfis exceto teste
     * - @Order(1): Primeira prioridade de execução
     * - CommandLineRunner: Executa após inicialização da aplicação
     */
    @Bean
    @Profile("!test")
    @Order(1)
    CommandLineRunner inicializarMateriais() {
        return args -> {
            log.info("🚀 INICIANDO CARREGAMENTO DE MATERIAIS PADRÃO...");
            
            try {
                // Verifica se já existem materiais
                if (materialRepository.count() > 0) {
                    log.info("✅ Materiais já existem no banco. Pulando inicialização.");
                    return;
                }

                // Lista de materiais padrão para ambos os ambientes
                List<Material> materiaisPadrao = Arrays.asList(
                    // PAPEL E PAPELÃO
                    criarMaterial("Papel Branco", 
                        "Papel de escritório limpo e seco", 
                        Material.CategoriaMaterial.PAPEL, 
                        new BigDecimal("0.80"), 
                        Material.CorIdentificacao.AZUL,
                        "Separar por tipo, manter seco, amassar para economizar espaço"),
                    
                    criarMaterial("Papelão", 
                        "Caixas de papelão limpas e secas", 
                        Material.CategoriaMaterial.PAPEL, 
                        new BigDecimal("0.60"), 
                        Material.CorIdentificacao.AZUL,
                        "Desmontar caixas, remover fitas e plásticos, manter seco"),
                    
                    criarMaterial("Jornal", 
                        "Jornais e revistas antigas", 
                        Material.CategoriaMaterial.PAPEL, 
                        new BigDecimal("0.40"), 
                        Material.CorIdentificacao.AZUL,
                        "Empilhar por tamanho, manter organizado, evitar umidade"),
                    
                    // PLÁSTICO
                    criarMaterial("PET (Garrafas)", 
                        "Garrafas PET de refrigerantes e água", 
                        Material.CategoriaMaterial.PLASTICO, 
                        new BigDecimal("1.20"), 
                        Material.CorIdentificacao.VERMELHO,
                        "Lavar, remover rótulos, amassar, separar por cor"),
                    
                    criarMaterial("PEAD (Embalagens)", 
                        "Embalagens de produtos de limpeza", 
                        Material.CategoriaMaterial.PLASTICO, 
                        new BigDecimal("0.90"), 
                        Material.CorIdentificacao.VERMELHO,
                        "Lavar bem, remover resíduos, verificar símbolo de reciclagem"),
                    
                    criarMaterial("PP (Tampas)", 
                        "Tampas de plástico e embalagens rígidas", 
                        Material.CategoriaMaterial.PLASTICO, 
                        new BigDecimal("0.70"), 
                        Material.CorIdentificacao.VERMELHO,
                        "Separar por tipo, lavar, verificar se é reciclável"),
                    
                    // VIDRO
                    criarMaterial("Vidro Transparente", 
                        "Garrafas e potes de vidro transparente", 
                        Material.CategoriaMaterial.VIDRO, 
                        new BigDecimal("0.50"), 
                        Material.CorIdentificacao.VERDE,
                        "Lavar bem, remover rótulos, separar por cor, não quebrar"),
                    
                    criarMaterial("Vidro Colorido", 
                        "Garrafas e potes de vidro colorido", 
                        Material.CategoriaMaterial.VIDRO, 
                        new BigDecimal("0.45"), 
                        Material.CorIdentificacao.VERDE,
                        "Lavar bem, separar por cor, manter intacto"),
                    
                    // METAL
                    criarMaterial("Alumínio (Latas)", 
                        "Latas de alumínio de bebidas", 
                        Material.CategoriaMaterial.METAL, 
                        new BigDecimal("3.50"), 
                        Material.CorIdentificacao.AMARELO,
                        "Lavar bem, amassar para economizar espaço, separar por tipo"),
                    
                    criarMaterial("Ferro (Latas)", 
                        "Latas de ferro de alimentos", 
                        Material.CategoriaMaterial.METAL, 
                        new BigDecimal("0.80"), 
                        Material.CorIdentificacao.AMARELO,
                        "Lavar bem, remover rótulos, verificar se não está enferrujado"),
                    
                    criarMaterial("Cobre", 
                        "Fios e objetos de cobre", 
                        Material.CategoriaMaterial.METAL, 
                        new BigDecimal("25.00"), 
                        Material.CorIdentificacao.AMARELO,
                        "Separar por pureza, remover isolamento, verificar qualidade"),
                    
                    // MATERIAL ORGÂNICO
                    criarMaterial("Cascas de Frutas", 
                        "Cascas e restos de frutas", 
                        Material.CategoriaMaterial.ORGANICO, 
                        new BigDecimal("0.10"), 
                        Material.CorIdentificacao.MARROM,
                        "Separar por tipo, manter seco, ideal para compostagem"),
                    
                    criarMaterial("Restos de Verduras", 
                        "Folhas e restos de verduras", 
                        Material.CategoriaMaterial.ORGANICO, 
                        new BigDecimal("0.15"), 
                        Material.CorIdentificacao.MARROM,
                        "Lavar bem, separar por tipo, ideal para compostagem"),
                    
                    // LIXO ELETRÔNICO
                    criarMaterial("Pilhas", 
                        "Pilhas e baterias usadas", 
                        Material.CategoriaMaterial.ELETRONICO, 
                        new BigDecimal("2.00"), 
                        Material.CorIdentificacao.LARANJA,
                        "Separar por tipo, não abrir, entregar em pontos específicos"),
                    
                    criarMaterial("Celulares", 
                        "Telefones celulares antigos", 
                        Material.CategoriaMaterial.ELETRONICO, 
                        new BigDecimal("15.00"), 
                        Material.CorIdentificacao.LARANJA,
                        "Remover bateria, limpar dados pessoais, entregar em pontos específicos"),
                    
                    // MATERIAL PERIGOSO
                    criarMaterial("Óleo de Cozinha", 
                        "Óleo usado de frituras", 
                        Material.CategoriaMaterial.PERIGOSO, 
                        new BigDecimal("1.50"), 
                        Material.CorIdentificacao.ROXO,
                        "Coletar em garrafas PET, não descartar no ralo, entregar em pontos específicos")
                );

                // Salva todos os materiais no banco
                materialRepository.saveAll(materiaisPadrao);
                
                log.info("✅ {} MATERIAIS PADRÃO CARREGADOS COM SUCESSO!", materiaisPadrao.size());
                log.info("📊 Categorias disponíveis: PAPEL, PLÁSTICO, VIDRO, METAL, ORGÂNICO, ELETRÔNICO, PERIGOSO");
                log.info("🎨 Cores de identificação: AZUL, VERMELHO, VERDE, AMARELO, MARROM, LARANJA, ROXO");
                
            } catch (Exception e) {
                log.error("❌ ERRO AO CARREGAR MATERIAIS PADRÃO: {}", e.getMessage(), e);
                // Não interrompe a aplicação em caso de erro
            }
        };
    }

    /**
     * INICIALIZADOR DE DADOS COMPLETOS (APENAS DESENVOLVIMENTO)
     * 
     * CONCEITOS:
     * - @Profile({"dev", "local"}): Executa nos perfis de desenvolvimento e local
     * - @Order(2): Segunda prioridade (após materiais)
     * - Cria usuários e coletas para testes (rotas serão geradas pelos algoritmos)
     */
    @Bean
    @Profile({"dev", "local"})
    @Order(2)
    CommandLineRunner inicializarDadosCompletos() {
        return args -> {
            log.info("🚀 INICIANDO CARREGAMENTO DE DADOS COMPLETOS PARA DESENVOLVIMENTO...");
            
            try {
                // Verifica se já existem usuários
                if (usuarioRepository.count() > 0) {
                    log.info("✅ Usuários já existem no banco. Pulando inicialização.");
                    return;
                }

                // 1. CRIAR USUÁRIOS DE TESTE
                log.info("👥 Criando usuários de teste...");
                List<Usuario> usuarios = criarUsuariosTeste();
                usuarioRepository.saveAll(usuarios);
                log.info("✅ {} usuários criados com sucesso!", usuarios.size());

                // NOTA: Rotas NÃO são criadas previamente - serão geradas pelos algoritmos de otimização
                log.info("🛣️ NOTA: Rotas serão geradas automaticamente pelos algoritmos de otimização!");

                // 3. CRIAR COLETAS DE TESTE
                log.info("📦 Criando coletas de teste...");
                List<Coleta> coletas = criarColetasTeste(usuarios);
                coletaRepository.saveAll(coletas);
                log.info("✅ {} coletas criadas com sucesso!", coletas.size());

                log.info("🎉 DADOS COMPLETOS CARREGADOS COM SUCESSO PARA DESENVOLVIMENTO!");
                
            } catch (Exception e) {
                log.error("❌ ERRO AO CARREGAR DADOS COMPLETOS: {}", e.getMessage(), e);
                // Não interrompe a aplicação em caso de erro
            }
        };
    }

    /**
     * INICIALIZADOR DE DADOS PARA HOMOLOGAÇÃO - BELO HORIZONTE
     * 
     * CONCEITOS:
     * - @Profile("homo"): Executa apenas no perfil de homologação
     * - @Order(2): Segunda prioridade (após materiais)
     * - Cria usuários e coletas com coordenadas reais de BH para testes de roteamento
     */
    @Bean
    @Profile("homo")
    @Order(2)
    CommandLineRunner inicializarDadosHomologacao() {
        return args -> {
            log.info("🚀 INICIANDO CARREGAMENTO DE DADOS PARA HOMOLOGAÇÃO - BELO HORIZONTE...");
            
            try {
                // Verifica se já existem usuários
                if (usuarioRepository.count() > 0) {
                    log.info("✅ Usuários já existem no banco. Pulando inicialização.");
                    return;
                }

                // 1. CRIAR USUÁRIOS DE TESTE COM COORDENADAS DE BH
                log.info("👥 Criando usuários de teste para Belo Horizonte...");
                List<Usuario> usuarios = criarUsuariosTesteBH();
                usuarioRepository.saveAll(usuarios);
                log.info("✅ {} usuários criados com sucesso para BH!", usuarios.size());

                // 2. CRIAR COLETAS DE TESTE COM COORDENADAS REAIS DE BH
                log.info("📦 Criando coletas de teste para Belo Horizonte...");
                List<Coleta> coletas = criarColetasTesteBH(usuarios);
                coletaRepository.saveAll(coletas);
                log.info("✅ {} coletas criadas com sucesso para BH!", coletas.size());

                log.info("🎉 DADOS DE HOMOLOGAÇÃO CARREGADOS COM SUCESSO PARA BELO HORIZONTE!");
                
            } catch (Exception e) {
                log.error("❌ ERRO AO CARREGAR DADOS DE HOMOLOGAÇÃO: {}", e.getMessage(), e);
                // Não interrompe a aplicação em caso de erro
            }
        };
    }

    /**
     * MÉTODO AUXILIAR PARA CRIAR MATERIAIS
     */
    private Material criarMaterial(String nome, String descricao, Material.CategoriaMaterial categoria, 
                                 BigDecimal valorPorQuilo, Material.CorIdentificacao cor, String instrucoes) {
        Material material = new Material(nome, categoria, valorPorQuilo);
        material.setDescricao(descricao);
        material.setAtivo(true);
        return material;
    }

    /**
     * MÉTODO PARA CRIAR USUÁRIOS DE TESTE
     */
    private List<Usuario> criarUsuariosTeste() {
        return Arrays.asList(
            criarUsuarioBH("admin", "admin@srpc.com", "Administrador", "COLETOR", "123456", -19.9167, -43.9345),
            criarUsuarioBH("coletor1", "coletor1@srpc.com", "João Silva", "COLETOR", "123456", -19.9208, -43.9376),
            criarUsuarioBH("coletor2", "coletor2@srpc.com", "Maria Santos", "COLETOR", "123456", -19.8519, -43.9695),
            criarUsuarioBH("coletor3", "coletor3@srpc.com", "Pedro Oliveira", "COLETOR", "123456", -19.9667, -44.0167),
            criarUsuarioBH("residencial1", "residencial1@srpc.com", "Ana Costa", "RESIDENCIAL", "123456", -19.8167, -43.9500),
            criarUsuarioBH("residencial2", "residencial2@srpc.com", "Carlos Ferreira", "RESIDENCIAL", "123456", -19.9333, -44.0500),
            criarUsuarioBH("comercial1", "comercial1@srpc.com", "Lucia Rodrigues", "COMERCIAL", "123456", -19.9667, -44.2000),
            criarUsuarioBH("comercial2", "comercial2@srpc.com", "Roberto Almeida", "COMERCIAL", "123456", -20.0167, -44.0667),
            criarUsuarioBH("residencial3", "residencial3@srpc.com", "Fernanda Lima", "RESIDENCIAL", "123456", -19.9100, -43.9400),
            criarUsuarioBH("comercial3", "comercial3@srpc.com", "Marcos Souza", "COMERCIAL", "123456", -19.9250, -43.9300)
        );
    }

    /**
     * MÉTODO PARA CRIAR USUÁRIO INDIVIDUAL
     */
    @SuppressWarnings("unused")
    private Usuario criarUsuario(String username, String email, String nome, String tipo, String senha) {
        Usuario usuario = new Usuario(nome, email, passwordEncoder.encode(senha), Usuario.TipoUsuario.valueOf(tipo));
        usuario.setStatus(Usuario.StatusUsuario.ATIVO);
        usuario.setDataCriacao(LocalDateTime.now());
        usuario.setDataAtualizacao(LocalDateTime.now());
        return usuario;
    }



    /**
     * MÉTODO PARA CRIAR COLETAS DE TESTE
     */
    private List<Coleta> criarColetasTeste(List<Usuario> usuarios) {
        List<Material> materiais = materialRepository.findAll();
        
        // Verificar se há materiais disponíveis
        if (materiais.isEmpty()) {
            log.warn("⚠️ Nenhum material encontrado para criar coletas de teste");
            return new ArrayList<>();
        }
        
        // Filtrar apenas usuários do tipo COLETOR para serem coletores
        List<Usuario> coletores = usuarios.stream()
                .filter(usuario -> usuario.getTipoUsuario() == Usuario.TipoUsuario.COLETOR)
                .collect(Collectors.toList());
        
        if (coletores.isEmpty()) {
            log.warn("⚠️ Nenhum coletor encontrado para criar coletas de teste");
            return new ArrayList<>();
        }
        
        // Usar índices seguros para materiais e coletores
        List<Coleta> coletas = Arrays.asList(
            criarColetaBH("Coleta Centro", "Coleta de materiais no centro", coletores.get(0 % coletores.size()), materiais.get(0 % materiais.size()), 15.5, "SOLICITADA", -19.9167, -43.9345),
            criarColetaBH("Coleta Norte", "Coleta de materiais no norte", coletores.get(1 % coletores.size()), materiais.get(1 % materiais.size()), 22.3, "SOLICITADA", -19.9208, -43.9376),
            criarColetaBH("Coleta Sul", "Coleta de materiais no sul", coletores.get(2 % coletores.size()), materiais.get(2 % materiais.size()), 18.7, "SOLICITADA", -19.8519, -43.9695),
            criarColetaBH("Coleta Leste", "Coleta de materiais no leste", coletores.get(0 % coletores.size()), materiais.get(1 % materiais.size()), 12.9, "SOLICITADA", -19.9667, -44.0167),
            criarColetaBH("Coleta Oeste", "Coleta de materiais no oeste", coletores.get(1 % coletores.size()), materiais.get(2 % materiais.size()), 25.1, "SOLICITADA", -19.8167, -43.9500),
            criarColetaBH("Coleta Especial", "Coleta de materiais especiais", coletores.get(2 % coletores.size()), materiais.get(3 % materiais.size()), 8.4, "SOLICITADA", -19.9333, -44.0500),
            criarColetaBH("Coleta Express", "Coleta rápida de emergência", coletores.get(0 % coletores.size()), materiais.get(4 % materiais.size()), 16.8, "SOLICITADA", -19.9667, -44.2000),
            criarColetaBH("Coleta Noturna", "Coleta noturna", coletores.get(1 % coletores.size()), materiais.get(5 % materiais.size()), 19.2, "SOLICITADA", -20.0167, -44.0667),
            criarColetaBH("Coleta Fim de Semana", "Coleta aos fins de semana", coletores.get(2 % coletores.size()), materiais.get(6 % materiais.size()), 14.6, "SOLICITADA", -19.9100, -43.9400),
            criarColetaBH("Coleta Industrial", "Coleta na zona industrial", coletores.get(0 % coletores.size()), materiais.get(7 % materiais.size()), 31.5, "SOLICITADA", -19.9250, -43.9300)
        );
        return coletas;
    }

    /**
     * MÉTODO PARA CRIAR COLETA INDIVIDUAL
     */
    @SuppressWarnings("unused")
    private Coleta criarColeta(String nome, String descricao, Usuario coletor, Material material, double peso, String status) {
        Coleta coleta = new Coleta(coletor, material, new BigDecimal(peso));
        coleta.setEndereco(descricao);
        coleta.setObservacoes(nome);
        coleta.setStatus(Coleta.StatusColeta.valueOf(status));
        coleta.setDataCriacao(LocalDateTime.now());
        coleta.setDataAtualizacao(LocalDateTime.now());
        return coleta;
    }

    /**
     * MÉTODO PARA CRIAR USUÁRIOS DE TESTE PARA BELO HORIZONTE
     */
    private List<Usuario> criarUsuariosTesteBH() {
        return Arrays.asList(
            criarUsuarioBH("admin-bh", "admin@srpc-bh.com", "Administrador BH", "COLETOR", "123456", -19.9167, -43.9345),
            criarUsuarioBH("coletor-bh1", "coletor1@srpc-bh.com", "João Silva BH", "COLETOR", "123456", -19.9208, -43.9376),
            criarUsuarioBH("coletor-bh2", "coletor2@srpc-bh.com", "Maria Santos BH", "COLETOR", "123456", -19.8519, -43.9695),
            criarUsuarioBH("coletor-bh3", "coletor3@srpc-bh.com", "Pedro Oliveira BH", "COLETOR", "123456", -19.9667, -44.0167),
            criarUsuarioBH("residencial-bh1", "residencial1@srpc-bh.com", "Ana Costa BH", "RESIDENCIAL", "123456", -19.8167, -43.9500),
            criarUsuarioBH("residencial-bh2", "residencial2@srpc-bh.com", "Carlos Ferreira BH", "RESIDENCIAL", "123456", -19.9333, -44.0500),
            criarUsuarioBH("comercial-bh1", "comercial1@srpc-bh.com", "Lucia Rodrigues BH", "COMERCIAL", "123456", -19.9667, -44.2000),
            criarUsuarioBH("comercial-bh2", "comercial2@srpc-bh.com", "Roberto Almeida BH", "COMERCIAL", "123456", -20.0167, -44.0667),
            criarUsuarioBH("residencial-bh3", "residencial3@srpc-bh.com", "Fernanda Lima BH", "RESIDENCIAL", "123456", -19.9167, -43.9345),
            criarUsuarioBH("comercial-bh3", "comercial3@srpc-bh.com", "Marcos Souza BH", "COMERCIAL", "123456", -19.9208, -43.9376)
        );
    }

    /**
     * MÉTODO PARA CRIAR USUÁRIO INDIVIDUAL COM COORDENADAS DE BH
     */
    private Usuario criarUsuarioBH(String username, String email, String nome, String tipo, String senha, double latitude, double longitude) {
        Usuario usuario = new Usuario(nome, email, passwordEncoder.encode(senha), Usuario.TipoUsuario.valueOf(tipo));
        usuario.setStatus(Usuario.StatusUsuario.ATIVO);
        usuario.setDataCriacao(LocalDateTime.now());
        usuario.setDataAtualizacao(LocalDateTime.now());
        usuario.setLatitude(latitude);
        usuario.setLongitude(longitude);
        return usuario;
    }

    /**
     * MÉTODO PARA CRIAR COLETAS DE TESTE PARA BELO HORIZONTE
     */
    private List<Coleta> criarColetasTesteBH(List<Usuario> usuarios) {
        List<Material> materiais = materialRepository.findAll();
        
        // Verificar se há materiais disponíveis
        if (materiais.isEmpty()) {
            log.warn("⚠️ Nenhum material encontrado para criar coletas de teste para BH");
            return new ArrayList<>();
        }
        
        // Filtrar apenas usuários do tipo COLETOR para serem coletores
        List<Usuario> coletores = usuarios.stream()
                .filter(usuario -> usuario.getTipoUsuario() == Usuario.TipoUsuario.COLETOR)
                .collect(Collectors.toList());
        
        if (coletores.isEmpty()) {
            log.warn("⚠️ Nenhum coletor encontrado para criar coletas de teste para BH");
            return new ArrayList<>();
        }
        
        // Criar coletas com coordenadas reais de BH
        List<Coleta> coletas = Arrays.asList(
            criarColetaBH("Coleta Centro BH", "Coleta no Centro de Belo Horizonte", coletores.get(0 % coletores.size()), materiais.get(0 % materiais.size()), 15.5, "SOLICITADA", -19.9167, -43.9345),
            criarColetaBH("Coleta Savassi BH", "Coleta na Savassi", coletores.get(1 % coletores.size()), materiais.get(1 % materiais.size()), 22.3, "SOLICITADA", -19.9208, -43.9376),
            criarColetaBH("Coleta Pampulha BH", "Coleta na Pampulha", coletores.get(2 % coletores.size()), materiais.get(2 % materiais.size()), 18.7, "SOLICITADA", -19.8519, -43.9695),
            criarColetaBH("Coleta Barreiro BH", "Coleta no Barreiro", coletores.get(0 % coletores.size()), materiais.get(1 % materiais.size()), 12.9, "SOLICITADA", -19.9667, -44.0167),
            criarColetaBH("Coleta Venda Nova BH", "Coleta na Venda Nova", coletores.get(1 % coletores.size()), materiais.get(2 % materiais.size()), 25.1, "SOLICITADA", -19.8167, -43.9500),
            criarColetaBH("Coleta Contagem BH", "Coleta em Contagem", coletores.get(2 % coletores.size()), materiais.get(3 % materiais.size()), 8.4, "SOLICITADA", -19.9333, -44.0500),
            criarColetaBH("Coleta Betim BH", "Coleta em Betim", coletores.get(0 % coletores.size()), materiais.get(4 % materiais.size()), 16.8, "SOLICITADA", -19.9667, -44.2000),
            criarColetaBH("Coleta Ibirité BH", "Coleta em Ibirité", coletores.get(1 % coletores.size()), materiais.get(5 % materiais.size()), 19.2, "SOLICITADA", -20.0167, -44.0667),
            criarColetaBH("Coleta Especial BH", "Coleta especial no Centro", coletores.get(2 % coletores.size()), materiais.get(6 % materiais.size()), 14.6, "SOLICITADA", -19.9167, -43.9345),
            criarColetaBH("Coleta Industrial BH", "Coleta na zona industrial", coletores.get(0 % coletores.size()), materiais.get(7 % materiais.size()), 31.5, "SOLICITADA", -19.9667, -44.2000)
        );
        return coletas;
    }

    /**
     * MÉTODO PARA CRIAR COLETA INDIVIDUAL COM COORDENADAS DE BH
     */
    private Coleta criarColetaBH(String nome, String descricao, Usuario coletor, Material material, double peso, String status, double latitude, double longitude) {
        Coleta coleta = new Coleta(coletor, material, new BigDecimal(peso));
        coleta.setEndereco(descricao);
        coleta.setObservacoes(nome);
        coleta.setStatus(Coleta.StatusColeta.valueOf(status));
        coleta.setDataCriacao(LocalDateTime.now());
        coleta.setDataAtualizacao(LocalDateTime.now());
        coleta.setLatitude(latitude);
        coleta.setLongitude(longitude);
        return coleta;
    }
}
