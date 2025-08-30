<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mapa - Sistema de Roteamento</title>
    
    <!-- Bootstrap CSS para estilização -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Leaflet CSS para mapa -->
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
    
    <!-- Tema de Cores do Java -->
    <link href="/css/basic-style.css" rel="stylesheet">
    
    <style>
        #map {
            height: 600px;
            width: 100%;
            border-radius: 0.375rem;
            border: 2px solid #dee2e6;
        }
        
        .map-controls {
            background: white;
            padding: 10px;
            border-radius: 5px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        
        .legend-item {
            display: flex;
            align-items: center;
            margin-bottom: 8px;
        }
        
        .legend-color {
            width: 20px;
            height: 20px;
            border-radius: 50%;
            margin-right: 8px;
            border: 2px solid white;
            box-shadow: 0 0 5px rgba(0,0,0,0.3);
        }
    </style>
</head>
<body>
    <!-- Seção Hero -->
    <section class="hero-section">
        <div class="container text-center">
            <h1 class="display-5 mb-3">
                <img src="/images/SrPC.png" alt="SrPC" class="hero-icon" style="width: 1.2em; height: 1.2em; vertical-align: middle; margin-right: 0.3em;">
                Mapa Interativo
            </h1>
            <p class="lead mb-0">Visualização geográfica de coletas e rotas</p>
        </div>
    </section>

    <div class="container">
        <!-- Navegação Principal -->
        <div class="row mb-4">
            <div class="col-12">
                <nav class="navbar navbar-expand-lg navbar-light bg-light rounded">
                    <div class="container-fluid justify-content-center">
                        <div class="navbar-nav">
                            <a class="nav-link" href="/sistema">🏠 Início</a>
                            <a class="nav-link" href="/sistema/coletas">📦 Coletas</a>
                            <a class="nav-link" href="/sistema/rotas">🗺️ Rotas</a>
                            <a class="nav-link" href="/sistema/usuarios">👥 Usuários</a>
                            <a class="nav-link" href="/sistema/materiais">♻️ Materiais</a>
                            <a class="nav-link active" href="/sistema/mapa">🗺️ Mapa</a>
                            <a class="nav-link" href="/sistema/configuracoes">⚙️ Configurações</a>
            
                        </div>
                    </div>
                </nav>
            </div>
        </div>

        <!-- Estatísticas -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="stats-card">
                    <div class="row text-center">
                        <div class="col-md-3">
                            <h3 class="mb-0">${totalPontos}</h3>
                            <small>Total de Pontos</small>
                        </div>
                        <div class="col-md-3">
                            <h3 class="mb-0">${coletas.size()}</h3>
                            <small>Coletas</small>
                        </div>
                        <div class="col-md-3">
                            <h3 class="mb-0">${rotas.size()}</h3>
                            <small>Rotas</small>
                        </div>
                        <div class="col-md-3">
                            <h3 class="mb-0">🗺️</h3>
                            <small>Mapa Ativo</small>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Controles do Mapa -->
        <div class="row mb-4">
            <div class="col-12">
                <div class="d-flex justify-content-between align-items-center">
                    <h5 class="mb-0">🎯 Controles do Mapa</h5>
                    <div class="btn-group" role="group">
                        <button type="button" class="btn btn-primary btn-sm" id="centerMap">
                            <i class="fas fa-crosshairs"></i> Centralizar
                        </button>
                        <button type="button" class="btn btn-success btn-sm" id="reloadMap">
                            <i class="fas fa-sync-alt"></i> Recarregar
                        </button>
                        <button type="button" class="btn btn-info btn-sm" id="optimizeRoutes">
                            <i class="fas fa-route"></i> Otimizar Rotas
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Mapa e Legenda -->
        <div class="row">
            <div class="col-md-9">
                <div class="card">
                    <div class="card-header">
                        <h5 class="mb-0">🗺️ Mapa Interativo</h5>
                    </div>
                    <div class="card-body p-0">
                        <div id="map"></div>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card">
                    <div class="card-header">
                        <h6 class="mb-0">🎨 Legenda</h6>
                    </div>
                    <div class="card-body">
                        <div class="legend-item">
                            <div class="legend-color" style="background-color: #007bff;"></div>
                            <span class="small">📍 Sua Localização</span>
                        </div>
                        <div class="legend-item">
                            <div class="legend-color" style="background-color: #28a745;"></div>
                            <span class="small">👤 Usuário</span>
                        </div>
                        <div class="legend-item">
                            <div class="legend-color" style="background-color: #ffc107;"></div>
                            <span class="small">📦 Coleta</span>
                        </div>
                        <div class="legend-item">
                            <div class="legend-color" style="background-color: #0056b3;"></div>
                            <span class="small">🛣️ Rota</span>
                        </div>
                    </div>
                </div>
                
                <!-- Estatísticas do Mapa -->
                <div class="card mt-3">
                    <div class="card-header">
                        <h6 class="mb-0">📊 Estatísticas</h6>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-6">
                                <small class="text-muted">Usuários:</small>
                                <div class="h6" id="visibleUsuarios">0</div>
                            </div>
                            <div class="col-6">
                                <small class="text-muted">Coletas:</small>
                                <div class="h6" id="visibleColetas">0</div>
                            </div>
                        </div>
                        <div class="row mt-2">
                            <div class="col-6">
                                <small class="text-muted">Rotas:</small>
                                <div class="h6" id="visibleRotas">0</div>
                            </div>
                            <div class="col-6">
                                <small class="text-muted">Total:</small>
                                <div class="h6" id="totalVisible">0</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Ações Rápidas -->
        <div class="row mb-4 mt-4">
            <div class="col-12">
                <h5 class="mb-3">🚀 Ações Rápidas</h5>
            </div>
        </div>

        <div class="row">
            <div class="col-lg-3 col-md-6 mb-3">
                <a href="/sistema/coletas" class="btn btn-success w-100">
                    📦 Gerenciar Coletas
                </a>
            </div>
            <div class="col-lg-3 col-md-6 mb-3">
                <a href="/sistema/rotas" class="btn btn-primary w-100">
                    🗺️ Gerenciar Rotas
                </a>
            </div>
            <div class="col-lg-3 col-md-6 mb-3">
                <a href="/sistema/usuarios" class="btn btn-info w-100">
                    👥 Gerenciar Usuários
                </a>
            </div>
            <div class="col-lg-3 col-md-6 mb-3">
                <a href="/sistema" class="btn btn-secondary w-100">
                    🏠 Página Inicial
                </a>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <!-- Leaflet JS para mapa -->
    <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
    
    <script>
        // Variáveis globais do mapa
        let map;
        let userLat = -19.9167; // BH como fallback
        let userLng = -43.9345;
        let userLocation = null;
        let markers = [];

        // Função para inicializar o mapa
        function inicializarMapa() {
            console.log('Inicializando mapa...');
            
            // Limpar conteúdo de carregamento
            const mapElement = document.getElementById('map');
            mapElement.innerHTML = '';
            
            // Criar mapa
            map = L.map('map').setView([userLat, userLng], 10);
            
            // Adicionar camada de mapa base
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                attribution: '© OpenStreetMap contributors'
            }).addTo(map);
            
            // Forçar redimensionamento do mapa
            setTimeout(() => {
                if (map) {
                    map.invalidateSize();
                    console.log('Mapa redimensionado');
                }
            }, 100);
        }

        // Função para obter localização do usuário
        function getUserLocation() {
            if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition(
                    function(position) {
                        userLat = position.coords.latitude;
                        userLng = position.coords.longitude;
                        userLocation = [userLat, userLng];
                        
                        // Atualizar mapa para a localização do usuário
                        map.setView([userLat, userLng], 12);
                        
                        // Adicionar marcador da localização do usuário
                        L.marker([userLat, userLng])
                            .addTo(map)
                            .bindPopup('<strong>📍 Sua Localização</strong><br>Você está aqui!')
                            .setIcon(L.divIcon({
                                className: 'user-location-marker',
                                html: '<div style="background-color: #007bff; width: 25px; height: 25px; border-radius: 50%; border: 3px solid white; box-shadow: 0 0 10px rgba(0,123,255,0.5);"></div>',
                                iconSize: [25, 25]
                            }));
                        
                        console.log('Localização do usuário obtida:', userLat, userLng);
                        
                        // Carregar pontos do mapa
                        carregarPontosMapa();
                    },
                    function(error) {
                        console.log('⚠️ Erro ao obter localização:', error.message);
                        console.log('📍 Usando localização padrão (Belo Horizonte)');
                        
                        // Usar BH como fallback e continuar funcionando
                        userLat = -19.9167;
                        userLng = -43.9345;
                        userLocation = null;
                        
                        // Centralizar mapa na localização padrão
                        if (map) {
                            map.setView([userLat, userLng], 10);
                        }
                        
                        // Carregar pontos do mapa mesmo sem localização do usuário
                        carregarPontosMapa();
                    },
                    {
                        enableHighAccuracy: true,
                        timeout: 10000,
                        maximumAge: 60000
                    }
                );
            } else {
                console.log('Geolocalização não suportada pelo navegador');
                carregarPontosMapa();
            }
        }

        // Função para carregar pontos do mapa
        function carregarPontosMapa() {
            if (!map) {
                console.error('Mapa não inicializado!');
                return;
            }
            
            console.log('🔄 Carregando pontos do mapa...');
            
            // Fazer chamada para a API que retorna os pontos
            fetch('/estatisticas/mapa-pontos')
                .then(response => {
                    if (!response.ok) {
                        throw new Error(`HTTP error! status: ${response.status}`);
                    }
                    return response.json();
                })
                .then(data => {
                    console.log('📊 Dados recebidos da API:', data);
                    console.log('📊 Estrutura dos dados:', JSON.stringify(data, null, 2));
                    
                    // Verificar estrutura dos dados
                    if (data.coletas && data.coletas.length > 0) {
                        console.log('📦 Primeira coleta:', data.coletas[0]);
                        console.log('📦 Tipo do endereço:', typeof data.coletas[0].endereco);
                        console.log('📦 Valor do endereço:', data.coletas[0].endereco);
                        console.log('📦 Tipo do status:', typeof data.coletas[0].status);
                        console.log('📦 Valor do status:', data.coletas[0].status);
                    }
                    
                    // Limpar marcadores existentes
                    limparMarcadores();
                    
                    // Adicionar marcadores dos usuários
                    if (data.usuarios && data.usuarios.length > 0) {
                        console.log(`👥 Adicionando ${data.usuarios.length} usuários ao mapa`);
                        adicionarMarcadoresUsuarios(data.usuarios);
                    }
                    
                    // Adicionar marcadores das coletas
                    if (data.coletas && data.coletas.length > 0) {
                        console.log(`📦 Adicionando ${data.coletas.length} coletas ao mapa`);
                        adicionarMarcadoresColetas(data.coletas);
                    }
                    
                    // Adicionar marcadores das rotas
                    if (data.rotas && data.rotas.length > 0) {
                        console.log(`🛣️ Adicionando ${data.rotas.length} rotas ao mapa`);
                        adicionarMarcadoresRotas(data.rotas);
                    }
                    
                    // Atualizar estatísticas
                    atualizarEstatisticasMapa(data);
                    
                })
                .catch(error => {
                    console.error('❌ Erro ao carregar pontos do mapa:', error);
                    
                    // Em caso de erro, mostrar mensagem mas manter o mapa funcionando
                    const mapElement = document.getElementById('map');
                    if (map && mapElement) {
                        // Adicionar overlay de erro sobre o mapa em vez de substituir
                        const errorOverlay = document.createElement('div');
                        errorOverlay.id = 'error-overlay';
                        errorOverlay.style.cssText = `
                            position: absolute;
                            top: 10px;
                            right: 10px;
                            background: rgba(220, 53, 69, 0.9);
                            color: white;
                            padding: 15px;
                            border-radius: 8px;
                            z-index: 1000;
                            max-width: 300px;
                            box-shadow: 0 4px 8px rgba(0,0,0,0.3);
                        `;
                        errorOverlay.innerHTML = `
                            <h6>⚠️ Erro ao carregar dados</h6>
                            <p style="margin: 8px 0; font-size: 14px;">${error.message}</p>
                            <button class="btn btn-light btn-sm" onclick="retryLoadMap()">
                                🔄 Tentar Novamente
                            </button>
                            <button class="btn btn-outline-light btn-sm ms-2" onclick="hideErrorOverlay()">
                                ✕
                            </button>
                        `;
                        
                        // Remover overlay anterior se existir
                        const existingOverlay = document.getElementById('error-overlay');
                        if (existingOverlay) {
                            existingOverlay.remove();
                        }
                        
                        mapElement.appendChild(errorOverlay);
                    }
                });
        }

        // Função para limpar marcadores
        function limparMarcadores() {
            markers.forEach(marker => {
                if (map.hasLayer(marker)) {
                    map.removeLayer(marker);
                }
            });
            markers = [];
        }

        // Função para adicionar marcadores dos usuários
        function adicionarMarcadoresUsuarios(usuarios) {
            console.log('🔍 Processando usuários para marcadores:', usuarios);
            usuarios.forEach(usuario => {
                console.log('👤 Usuário completo:', usuario);
                console.log('👤 Usuário Nome:', usuario.nome);
                console.log('👤 Usuário Tipo:', usuario.tipoUsuario);
                console.log('👤 Usuário Status:', usuario.status);
                console.log('👤 Usuário Lat:', usuario.latitude);
                console.log('👤 Usuário Lng:', usuario.longitude);
                
                if (usuario.latitude && usuario.longitude) {
                    // Verificar se os campos têm valores válidos
                    const nome = usuario.nome && usuario.nome !== 'false' ? usuario.nome : 'Nome não informado';
                    const tipoUsuario = usuario.tipoUsuario && usuario.tipoUsuario !== 'false' ? usuario.tipoUsuario : 'Tipo não informado';
                    const status = usuario.status && usuario.status !== 'false' ? usuario.status : 'Status não informado';
                    
                    console.log('👤 Valores processados - Nome:', nome, 'Tipo:', tipoUsuario, 'Status:', status);
                    console.log('👤 Valores originais - Nome:', usuario.nome, 'Tipo:', usuario.tipoUsuario, 'Status:', usuario.status);
                    
                    const popupContent = `
                        <strong>👤 ${nome}</strong><br>
                        Tipo: ${tipoUsuario}<br>
                        Status: ${status}<br>
                        <small>Clique para mais detalhes</small>
                    `;
                    console.log('👤 Popup content:', popupContent);
                    
                    const marker = L.marker([usuario.latitude, usuario.longitude])
                        .addTo(map)
                        .bindPopup(popupContent);
                    
                    // Estilizar marcador
                    marker.setIcon(L.divIcon({
                        className: 'usuario-marker',
                        html: `<div style="background-color: #28a745; width: 18px; height: 18px; border-radius: 50%; border: 2px solid white; box-shadow: 0 0 5px rgba(0,0,0,0.3);"></div>`,
                        iconSize: [18, 18]
                    }));
                    
                    markers.push(marker);
                    console.log('✅ Marcador de usuário adicionado:', usuario.nome);
                } else {
                    console.log('❌ Usuário sem coordenadas válidas:', usuario.nome);
                }
            });
        }

        // Função para adicionar marcadores das coletas
        function adicionarMarcadoresColetas(coletas) {
            console.log('🔍 Processando coletas para marcadores:', coletas);
            coletas.forEach(coleta => {
                console.log('📦 Coleta completa:', coleta);
                console.log('📦 Coleta ID:', coleta.id);
                console.log('📦 Coleta Endereço:', coleta.endereco);
                console.log('📦 Coleta Status:', coleta.status);
                console.log('📦 Coleta Lat:', coleta.latitude);
                console.log('📦 Coleta Lng:', coleta.longitude);
                
                if (coleta.latitude && coleta.longitude) {
                    // Verificar se os campos têm valores válidos
                    const endereco = coleta.endereco && coleta.endereco !== 'false' ? coleta.endereco : 'Endereço não informado';
                    const status = coleta.status && coleta.status !== 'false' ? coleta.status : 'Status não informado';
                    
                    console.log('📦 Valores processados - Endereço:', endereco, 'Status:', status);
                    console.log('📦 Valores originais - Endereço:', coleta.endereco, 'Status:', coleta.status);
                    
                    const popupContent = `
                        <strong>📦 ${endereco}</strong><br>
                        Status: ${status}<br>
                        <small>Clique para mais detalhes</small>
                    `;
                    console.log('📦 Popup content:', popupContent);
                    
                    const marker = L.marker([coleta.latitude, coleta.longitude])
                        .addTo(map)
                        .bindPopup(popupContent);
                    
                    // Estilizar marcador
                    marker.setIcon(L.divIcon({
                        className: 'coleta-marker',
                        html: `<div style="background-color: #ffc107; width: 20px; height: 20px; border-radius: 50%; border: 2px solid white; box-shadow: 0 0 5px rgba(0,0,0,0.3);"></div>`,
                        iconSize: [20, 20]
                    }));
                    
                    markers.push(marker);
                    console.log('✅ Marcador de coleta adicionado:', coleta.id);
                } else {
                    console.log('❌ Coleta sem coordenadas válidas:', coleta.id);
                }
            });
        }

        // Função para adicionar marcadores das rotas
        function adicionarMarcadoresRotas(rotas) {
            rotas.forEach(rota => {
                if (rota.latitudeInicio && rota.longitudeInicio) {
                    const marker = L.marker([rota.latitudeInicio, rota.longitudeInicio])
                        .addTo(map)
                        .bindPopup(`
                            <strong>🛣️ ${rota.nome || 'Rota'}</strong><br>
                            Status: ${rota.status || 'N/A'}<br>
                            <small>Clique para mais detalhes</small>
                        `);
                    
                    // Estilizar marcador
                    marker.setIcon(L.divIcon({
                        className: 'rota-marker',
                        html: `<div style="background-color: #0056b3; width: 22px; height: 22px; border-radius: 50%; border: 2px solid white; box-shadow: 0 0 5px rgba(0,0,0,0.3);"></div>`,
                        iconSize: [22, 22]
                    }));
                    
                    markers.push(marker);
                }
            });
        }

        // Função para atualizar estatísticas do mapa
        function atualizarEstatisticasMapa(data) {
            const totalUsuarios = data.usuarios ? data.usuarios.length : 0;
            const totalColetas = data.coletas ? data.coletas.length : 0;
            const totalRotas = data.rotas ? data.rotas.length : 0;
            const total = totalUsuarios + totalColetas + totalRotas;
            
            document.getElementById('visibleUsuarios').textContent = totalUsuarios;
            document.getElementById('visibleColetas').textContent = totalColetas;
            document.getElementById('visibleRotas').textContent = totalRotas;
            document.getElementById('totalVisible').textContent = total;
            
            console.log(`📊 Estatísticas atualizadas: ${totalUsuarios} usuários, ${totalColetas} coletas, ${totalRotas} rotas`);
        }

        // Função para tentar carregar o mapa novamente
        function retryLoadMap() {
            console.log('🔄 Tentando carregar mapa novamente...');
            hideErrorOverlay();
            carregarPontosMapa();
        }

        // Função para esconder o overlay de erro
        function hideErrorOverlay() {
            const errorOverlay = document.getElementById('error-overlay');
            if (errorOverlay) {
                errorOverlay.remove();
            }
        }

        // Controles do mapa
        document.getElementById('centerMap').addEventListener('click', function() {
            if (!map) {
                console.error('Mapa não inicializado!');
                return;
            }
            
            if (userLocation) {
                map.setView(userLocation, 12);
            } else {
                map.setView([userLat, userLng], 10);
            }
        });

        document.getElementById('reloadMap').addEventListener('click', function() {
            if (!map) {
                console.error('Mapa não inicializado!');
                return;
            }
            
            console.log('🔄 Recarregando pontos do mapa...');
            carregarPontosMapa();
        });

        document.getElementById('optimizeRoutes').addEventListener('click', function() {
            if (!map) {
                console.error('Mapa não inicializado!');
                return;
            }
            
            if (userLocation) {
                alert('🧠 Otimização de rotas iniciada a partir da sua localização! O sistema está calculando as melhores rotas...');
            } else {
                alert('📍 Por favor, permita o acesso à sua localização para otimização personalizada de rotas.');
            }
        });

        // Inicializar mapa quando o DOM estiver pronto
        document.addEventListener('DOMContentLoaded', function() {
            console.log('DOM carregado, inicializando mapa...');
            
            try {
                inicializarMapa();
                
                // Aguardar um pouco para o mapa ser criado
                setTimeout(function() {
                    try {
                        // Inicializar localização do usuário
                        getUserLocation();
                    } catch (error) {
                        console.error('❌ Erro ao obter localização do usuário:', error);
                        // Continuar funcionando mesmo sem localização
                        carregarPontosMapa();
                    }
                }, 100);
            } catch (error) {
                console.error('❌ Erro ao inicializar mapa:', error);
                // Mostrar mensagem de erro mas permitir retry
                const mapElement = document.getElementById('map');
                if (mapElement) {
                    mapElement.innerHTML = `
                        <div class="text-center p-5">
                            <h4>❌ Erro ao inicializar mapa</h4>
                            <p>Não foi possível inicializar o mapa.</p>
                            <button class="btn btn-primary" onclick="location.reload()">
                                🔄 Recarregar Página
                            </button>
                        </div>
                    `;
                }
            }
        });
    </script>
</body>
</html>
