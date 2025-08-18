import React, { useEffect, useState, useCallback, useRef } from 'react';
import { Loader } from '@googlemaps/js-api-loader';
import { Box, IconButton, Tooltip, Alert, CircularProgress, Typography } from '@mui/material';
import {
    Visibility,
    VisibilityOff,
} from '@mui/icons-material';
import { Usuario, Coleta, Rota, MapPoint } from '../../types';
import { GOOGLE_MAPS_CONFIG, MAP_STYLES, CUSTOM_ICONS, STATUS_COLORS } from '../../config/maps';
import MapFallback from './MapFallback';

interface GoogleMapComponentProps {
    usuarios?: Usuario[];
    coletas?: Coleta[];
    rotas?: Rota[];
    height?: string;
    showUsuarios?: boolean;
    showColetas?: boolean;
    showRotas?: boolean;
    onPointClick?: (point: MapPoint) => void;
}

const GoogleMapComponent: React.FC<GoogleMapComponentProps> = ({
    usuarios = [],
    coletas = [],
    rotas = [],
    height = '600px',
    showUsuarios = true,
    showColetas = true,
    showRotas = true,
    onPointClick
}) => {
    const mapRef = useRef<HTMLDivElement>(null);
    const [map, setMap] = useState<google.maps.Map | null>(null);
    const [markers, setMarkers] = useState<google.maps.Marker[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [showDebug, setShowDebug] = useState(false);
    const [showUsuariosState, setShowUsuarios] = useState(showUsuarios);
    const [showColetasState, setShowColetas] = useState(showColetas);
    const [showRotasState, setShowRotas] = useState(showRotas);

    // Carregar Google Maps
    const loadGoogleMaps = useCallback(async () => {
        try {
            setIsLoading(true);
            setError(null);
            console.log('🔄 Iniciando carregamento do Google Maps...');

            if (window.google && window.google.maps) {
                console.log('✅ Google Maps já carregado');
                return;
            }

            console.log('📦 Criando loader do Google Maps...');
            const loader = new Loader(GOOGLE_MAPS_CONFIG);

            console.log('⏳ Aguardando carregamento...');
            await loader.load();

            // Aguardar um pouco para garantir que tudo foi carregado
            await new Promise(resolve => setTimeout(resolve, 100));

            console.log('✅ Google Maps carregado com sucesso');
            console.log('🔍 Verificando disponibilidade:', {
                google: !!window.google,
                maps: !!window.google?.maps,
                MapTypeId: !!window.google?.maps?.MapTypeId
            });
        } catch (err: any) {
            console.error('❌ Erro ao carregar Google Maps:', err);
            setError('Erro ao carregar Google Maps: ' + err.message);
        } finally {
            setIsLoading(false);
        }
    }, []);

    // Inicializar mapa
    const initializeMap = useCallback(() => {
        console.log('🔍 Tentando inicializar mapa...');
        console.log('🔍 mapRef.current:', !!mapRef.current);
        console.log('🔍 window.google:', !!window.google);
        console.log('🔍 window.google.maps:', !!window.google?.maps);

        if (!mapRef.current) {
            console.log('❌ mapRef.current não está disponível');
            return;
        }

        if (!window.google) {
            console.log('❌ window.google não está disponível');
            return;
        }

        if (!window.google.maps) {
            console.log('❌ window.google.maps não está disponível');
            return;
        }

        if (!window.google.maps.MapTypeId) {
            console.log('❌ window.google.maps.MapTypeId não está disponível');
            return;
        }

        const beloHorizonte = { lat: -19.9167, lng: -43.9345 };

        try {
            const newMap = new window.google.maps.Map(mapRef.current, {
                center: beloHorizonte,
                zoom: 12,
                styles: MAP_STYLES,
                mapTypeId: window.google.maps.MapTypeId.ROADMAP,
                disableDefaultUI: false,
                zoomControl: true,
                streetViewControl: false,
                fullscreenControl: true,
                mapTypeControl: true,
            });

            setMap(newMap);
            console.log('✅ Mapa inicializado com sucesso');
        } catch (error: any) {
            console.error('❌ Erro ao inicializar mapa:', error);
            setError('Erro ao inicializar mapa: ' + error.message);
        }
    }, []);

    // Criar marcadores
    const createMarkers = useCallback(() => {
        if (!map) return;

        // Limpar marcadores existentes
        markers.forEach(marker => marker.setMap(null));
        const newMarkers: google.maps.Marker[] = [];

        // Marcadores de usuários
        if (showUsuariosState) {
            usuarios.forEach(usuario => {
                if (usuario.latitude && usuario.longitude) {
                    const marker = new window.google.maps.Marker({
                        position: { lat: usuario.latitude, lng: usuario.longitude },
                        map,
                        title: usuario.nome,
                        // icon: CUSTOM_ICONS.USUARIO, // Removido temporariamente
                        label: {
                            text: usuario.nome.substring(0, 10),
                            color: 'white',
                            fontSize: '12px'
                        }
                    });

                    marker.addListener('click', () => {
                        onPointClick?.({
                            id: usuario.id,
                            nome: usuario.nome,
                            tipo: 'usuario',
                            lat: usuario.latitude!,
                            lng: usuario.longitude!,
                            dados: usuario
                        });
                    });

                    newMarkers.push(marker);
                }
            });
        }

        // Marcadores de coletas
        if (showColetasState) {
            coletas.forEach(coleta => {
                if (coleta.latitude && coleta.longitude) {
                    const marker = new window.google.maps.Marker({
                        position: { lat: coleta.latitude, lng: coleta.longitude },
                        map,
                        title: `Coleta #${coleta.id}`,
                        // icon: CUSTOM_ICONS.COLETA, // Removido temporariamente
                        label: {
                            text: `C${coleta.id}`,
                            color: 'white',
                            fontSize: '12px'
                        }
                    });

                    marker.addListener('click', () => {
                        onPointClick?.({
                            id: coleta.id,
                            nome: `Coleta #${coleta.id}`,
                            tipo: 'coleta',
                            lat: coleta.latitude!,
                            lng: coleta.longitude!,
                            dados: coleta
                        });
                    });

                    newMarkers.push(marker);
                }
            });
        }

        // Marcadores de rotas
        if (showRotasState) {
            rotas.forEach(rota => {
        // Usar coordenadas de início da rota se disponíveis
                if (rota.latitudeInicio && rota.longitudeInicio) {
                    const marker = new window.google.maps.Marker({
                        position: { lat: rota.latitudeInicio, lng: rota.longitudeInicio },
                        map,
                        title: `Rota #${rota.id}`,
                        // icon: CUSTOM_ICONS.ROTA, // Removido temporariamente
                        label: {
                            text: `R${rota.id}`,
                            color: 'white',
                            fontSize: '12px'
                        }
                    });

                    marker.addListener('click', () => {
                        onPointClick?.({
                            id: rota.id,
                            nome: `Rota #${rota.id}`,
                            tipo: 'rota',
                            lat: rota.latitudeInicio!,
                            lng: rota.longitudeInicio!,
                            dados: rota
                        });
                    });

                    newMarkers.push(marker);
                }
            });
        }

        setMarkers(newMarkers);
        console.log(`${newMarkers.length} marcadores criados`);
    }, [map, usuarios, coletas, rotas, showUsuariosState, showColetasState, showRotasState, onPointClick]);

    // Efeitos
    useEffect(() => {
        console.log('🚀 useEffect: Carregando Google Maps...');
        loadGoogleMaps();
    }, [loadGoogleMaps]);

    useEffect(() => {
        console.log('🔍 useEffect: Verificando condições para inicializar mapa...');
        console.log('   - isLoading:', isLoading);
        console.log('   - error:', error);
        console.log('   - window.google:', !!window.google);
        console.log('   - window.google.maps:', !!window.google?.maps);

        if (!isLoading && !error && window.google?.maps) {
            console.log('✅ Condições atendidas, inicializando mapa...');
            // Aguardar um pouco mais para garantir que tudo foi carregado
            setTimeout(() => {
                initializeMap();
            }, 200);
        } else {
            console.log('❌ Condições não atendidas para inicializar mapa');
        }
    }, [isLoading, error, initializeMap]);

    useEffect(() => {
        if (map) {
            createMarkers();
        }
    }, [map, createMarkers]);

    if (isLoading) {
        return (
            <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height }}>
                <CircularProgress />
            </Box>
        );
    }

    if (error) {
        return (
            <MapFallback
                height={height}
                error={error}
                onRetry={() => {
                    setError(null);
                    loadGoogleMaps();
                }}
            />
        );
    }

    return (
        <Box sx={{ position: 'relative', height }}>
            <div ref={mapRef} style={{ width: '100%', height: '100%' }} />

            {/* Controles de visibilidade */}
            <Box sx={{ position: 'absolute', top: 10, right: 10, zIndex: 1000 }}>
                <Tooltip title="Mostrar/Ocultar Usuários">
                    <IconButton
                        onClick={() => setShowUsuarios(!showUsuariosState)}
                        sx={{ 
                            backgroundColor: 'rgba(255,255,255,0.9)',
                            mr: 1,
                            color: showUsuariosState ? 'primary.main' : 'text.secondary'
                        }}
                    >
                        <Visibility />
                    </IconButton>
                </Tooltip>

                <Tooltip title="Mostrar/Ocultar Coletas">
                    <IconButton
                        onClick={() => setShowColetas(!showColetasState)}
                        sx={{
                            backgroundColor: 'rgba(255,255,255,0.9)',
                            mr: 1,
                            color: showColetasState ? 'primary.main' : 'text.secondary'
                        }}
                    >
                        <Visibility />
                    </IconButton>
                </Tooltip>

                <Tooltip title="Mostrar/Ocultar Rotas">
                    <IconButton
                        onClick={() => setShowRotas(!showRotasState)}
                        sx={{
                            backgroundColor: 'rgba(255,255,255,0.9)',
                            color: showRotasState ? 'primary.main' : 'text.secondary'
                        }}
                    >
                        <Visibility />
                    </IconButton>
                </Tooltip>
            </Box>
        </Box>
    );
};

export default GoogleMapComponent; 