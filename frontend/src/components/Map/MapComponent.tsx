import React, { useEffect, useState, useCallback } from 'react';
import { MapContainer, TileLayer, Marker, Popup, Polyline, CircleMarker, useMap } from 'react-leaflet';
import { Icon, LatLngExpression, Map as LeafletMap } from 'leaflet';
import { Box, Typography, Chip, Card, CardContent, IconButton, Tooltip, Alert, CircularProgress } from '@mui/material';
import {
    LocationOn,
    Person,
    LocalShipping,
    Recycling,
    Visibility,
    VisibilityOff,
    Refresh,
    MyLocation,
    ZoomIn,
    ZoomOut,
} from '@mui/icons-material';
import { Usuario, Coleta, Rota, MapPoint } from '../../types';

// Componente para controlar o mapa
const MapController: React.FC<{ center: LatLngExpression; zoom: number }> = ({ center, zoom }) => {
    const map = useMap();

    useEffect(() => {
        map.setView(center, zoom);
    }, [map, center, zoom]);

    return null;
};

// Ícones personalizados para o mapa
const createCustomIcon = (color: string, type: string) => {
    return new Icon({
        iconUrl: `data:image/svg+xml;base64,${btoa(`
      <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
        <circle cx="12" cy="12" r="10" fill="${color}" stroke="white" stroke-width="2"/>
        <text x="12" y="16" text-anchor="middle" fill="white" font-size="12" font-weight="bold">${type}</text>
      </svg>
    `)}`,
        iconSize: [24, 24],
        iconAnchor: [12, 12],
        popupAnchor: [0, -12],
    });
};

interface MapComponentProps {
    usuarios?: Usuario[];
    coletas?: Coleta[];
    rotas?: Rota[];
    height?: string;
    showUsuarios?: boolean;
    showColetas?: boolean;
    showRotas?: boolean;
    onPointClick?: (point: MapPoint) => void;
}

const MapComponent: React.FC<MapComponentProps> = ({
    usuarios = [],
    coletas = [],
    rotas = [],
    height = '600px',
    showUsuarios = true,
    showColetas = true,
    showRotas = true,
    onPointClick,
}) => {
    const [mapPoints, setMapPoints] = useState<MapPoint[]>([]);
    const [visibleLayers, setVisibleLayers] = useState({
        usuarios: showUsuarios,
        coletas: showColetas,
        rotas: showRotas,
    });
    const [mapError, setMapError] = useState<string | null>(null);
    const [isLoading, setIsLoading] = useState(true);

    // Centro padrão (São Paulo)
    const defaultCenter: LatLngExpression = [-23.5505, -46.6333];
    const defaultZoom = 12;

    // Calcular centro baseado nos dados
    const calculateCenter = useCallback(() => {
        const allPoints = [
            ...usuarios.filter(u => u.latitude && u.longitude).map(u => [u.latitude!, u.longitude!]),
            ...coletas.filter(c => c.latitude && c.longitude).map(c => [c.latitude!, c.longitude!]),
            ...rotas.filter(r => r.latitudeInicio && r.longitudeInicio).map(r => [r.latitudeInicio!, r.longitudeInicio!]),
        ];

        if (allPoints.length === 0) {
            return defaultCenter;
        }

        const avgLat = allPoints.reduce((sum, [lat]) => sum + lat, 0) / allPoints.length;
        const avgLng = allPoints.reduce((sum, [_, lng]) => sum + lng, 0) / allPoints.length;

        return [avgLat, avgLng] as LatLngExpression;
    }, [usuarios, coletas, rotas]);

    useEffect(() => {
        try {
            setIsLoading(true);
            setMapError(null);

            const points: MapPoint[] = [];

            // Adicionar usuários
            if (visibleLayers.usuarios) {
                usuarios.forEach((usuario) => {
                    if (usuario.latitude && usuario.longitude) {
                        points.push({
                            id: usuario.id,
                            nome: usuario.nome,
                            tipo: 'usuario',
                            lat: usuario.latitude,
                            lng: usuario.longitude,
                            dados: usuario,
                        });
                    }
                });
            }

            // Adicionar coletas
            if (visibleLayers.coletas) {
                coletas.forEach((coleta) => {
                    if (coleta.latitude && coleta.longitude) {
                        points.push({
                            id: coleta.id,
                            nome: `Coleta #${coleta.id}`,
                            tipo: 'coleta',
                            lat: coleta.latitude,
                            lng: coleta.longitude,
                            dados: coleta,
                        });
                    }
                });
            }

            // Adicionar rotas
            if (visibleLayers.rotas) {
                rotas.forEach((rota) => {
                    if (rota.latitudeInicio && rota.longitudeInicio) {
                        points.push({
                            id: rota.id,
                            nome: rota.nome,
                            tipo: 'rota',
                            lat: rota.latitudeInicio,
                            lng: rota.longitudeInicio,
                            dados: rota,
                        });
                    }
                });
            }

            setMapPoints(points);
        } catch (error) {
            setMapError('Erro ao processar dados do mapa');
            console.error('Erro no mapa:', error);
        } finally {
            setIsLoading(false);
        }
    }, [usuarios, coletas, rotas, visibleLayers]);

    const getIconForPoint = (tipo: string) => {
        switch (tipo) {
            case 'usuario':
                return createCustomIcon('#2196f3', 'U');
            case 'coleta':
                return createCustomIcon('#ff9800', 'C');
            case 'rota':
                return createCustomIcon('#4caf50', 'R');
            case 'coletor':
                return createCustomIcon('#9c27b0', 'T');
            default:
                return createCustomIcon('#757575', '?');
        }
    };

    const getStatusColor = (status: string) => {
        switch (status) {
            case 'ATIVO':
            case 'ACEITA':
            case 'EM_EXECUCAO':
                return 'success';
            case 'PENDENTE':
            case 'SOLICITADA':
            case 'PLANEJADA':
                return 'warning';
            case 'INATIVO':
            case 'CANCELADA':
            case 'REJEITADA':
                return 'error';
            default:
                return 'default';
        }
    };

    const handlePointClick = (point: MapPoint) => {
        if (onPointClick) {
            onPointClick(point);
        }
    };

    const toggleLayer = (layer: keyof typeof visibleLayers) => {
        setVisibleLayers(prev => ({
            ...prev,
            [layer]: !prev[layer],
        }));
    };

    const handleMapError = () => {
        setMapError('Erro ao carregar o mapa. Verifique sua conexão com a internet.');
    };

    if (mapError) {
        return (
            <Box sx={{
                height,
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                backgroundColor: 'background.paper',
                borderRadius: 1
            }}>
                <Alert severity="error" sx={{ maxWidth: 400 }}>
                    {mapError}
                </Alert>
            </Box>
        );
    }

    if (isLoading) {
        return (
            <Box sx={{
                height,
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                backgroundColor: 'background.paper',
                borderRadius: 1
            }}>
                <CircularProgress />
            </Box>
        );
    }

    return (
        <Box sx={{ position: 'relative', height }}>
            {/* Controles de camadas */}
            <Box
                sx={{
                    position: 'absolute',
                    top: 10,
                    right: 10,
                    zIndex: 1000,
                    backgroundColor: 'background.paper',
                    borderRadius: 2,
                    p: 1,
                    boxShadow: 3,
                }}
            >
                <Tooltip title="Usuários">
                    <IconButton
                        size="small"
                        onClick={() => toggleLayer('usuarios')}
                        color={visibleLayers.usuarios ? 'primary' : 'default'}
                    >
                        {visibleLayers.usuarios ? <Visibility /> : <VisibilityOff />}
                    </IconButton>
                </Tooltip>
                <Tooltip title="Coletas">
                    <IconButton
                        size="small"
                        onClick={() => toggleLayer('coletas')}
                        color={visibleLayers.coletas ? 'primary' : 'default'}
                    >
                        {visibleLayers.coletas ? <Visibility /> : <VisibilityOff />}
                    </IconButton>
                </Tooltip>
                <Tooltip title="Rotas">
                    <IconButton
                        size="small"
                        onClick={() => toggleLayer('rotas')}
                        color={visibleLayers.rotas ? 'primary' : 'default'}
                    >
                        {visibleLayers.rotas ? <Visibility /> : <VisibilityOff />}
                    </IconButton>
                </Tooltip>
            </Box>

            <MapContainer
                center={calculateCenter()}
                zoom={defaultZoom}
                style={{ height: '100%', width: '100%' }}
                zoomControl={true}
                attributionControl={true}
            >
                <MapController center={calculateCenter()} zoom={defaultZoom} />

                {/* TileLayer com fallback */}
                <TileLayer
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                    attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                    eventHandlers={{
                        error: handleMapError,
                    }}
                />

                {/* Marcadores de pontos */}
                {mapPoints.map((point) => (
                    <Marker
                        key={`${point.tipo}-${point.id}`}
                        position={[point.lat, point.lng]}
                        icon={getIconForPoint(point.tipo)}
                        eventHandlers={{
                            click: () => handlePointClick(point),
                        }}
                    >
                        <Popup>
                            <Card sx={{ minWidth: 200 }}>
                                <CardContent>
                                    <Typography variant="h6" gutterBottom>
                                        {point.nome}
                                    </Typography>

                                    {point.tipo === 'usuario' && (
                                        <Box>
                                            <Typography variant="body2" color="text.secondary">
                                                {(point.dados as Usuario).email}
                                            </Typography>
                                            <Typography variant="body2" color="text.secondary">
                                                {(point.dados as Usuario).tipoUsuario}
                                            </Typography>
                                            <Chip
                                                label={(point.dados as Usuario).status}
                                                color={getStatusColor((point.dados as Usuario).status) as any}
                                                size="small"
                                                sx={{ mt: 1 }}
                                            />
                                        </Box>
                                    )}

                                    {point.tipo === 'coleta' && (
                                        <Box>
                                            <Typography variant="body2" color="text.secondary">
                                                Material: {(point.dados as Coleta).material?.nome || 'N/A'}
                                            </Typography>
                                            <Typography variant="body2" color="text.secondary">
                                                Quantidade: {(point.dados as Coleta).quantidade}kg
                                            </Typography>
                                            <Chip
                                                label={(point.dados as Coleta).status}
                                                color={getStatusColor((point.dados as Coleta).status) as any}
                                                size="small"
                                                sx={{ mt: 1 }}
                                            />
                                        </Box>
                                    )}

                                    {point.tipo === 'rota' && (
                                        <Box>
                                            <Typography variant="body2" color="text.secondary">
                                                {(point.dados as Rota).descricao || 'Sem descrição'}
                                            </Typography>
                                            <Typography variant="body2" color="text.secondary">
                                                Coletor: {(point.dados as Rota).coletor?.nome || 'N/A'}
                                            </Typography>
                                            <Chip
                                                label={(point.dados as Rota).status}
                                                color={getStatusColor((point.dados as Rota).status) as any}
                                                size="small"
                                                sx={{ mt: 1 }}
                                            />
                                        </Box>
                                    )}
                                </CardContent>
                            </Card>
                        </Popup>
                    </Marker>
                ))}

                {/* Linhas das rotas */}
                {visibleLayers.rotas && rotas.map((rota) => {
                    if (rota.latitudeInicio && rota.longitudeInicio && rota.latitudeFim && rota.longitudeFim) {
                        const positions: LatLngExpression[] = [
                            [rota.latitudeInicio, rota.longitudeInicio],
                            [rota.latitudeFim, rota.longitudeFim],
                        ];

                        return (
                            <Polyline
                                key={`rota-${rota.id}`}
                                positions={positions}
                                color="#4caf50"
                                weight={3}
                                opacity={0.7}
                            >
                                <Popup>
                                    <Typography variant="h6">{rota.nome}</Typography>
                                    <Typography variant="body2">
                                        Distância: {rota.distanciaTotal || 0}m
                                    </Typography>
                                    <Typography variant="body2">
                                        Tempo: {rota.tempoEstimado || 0}min
                                    </Typography>
                                </Popup>
                            </Polyline>
                        );
                    }
                    return null;
                })}

                {/* Círculos de raio para coletas */}
                {visibleLayers.coletas && coletas.map((coleta) => {
                    if (coleta.latitude && coleta.longitude) {
                        return (
                            <CircleMarker
                                key={`raio-${coleta.id}`}
                                center={[coleta.latitude, coleta.longitude]}
                                radius={50}
                                pathOptions={{
                                    color: '#ff9800',
                                    fillColor: '#ff9800',
                                    fillOpacity: 0.1,
                                }}
                            />
                        );
                    }
                    return null;
                })}
            </MapContainer>
        </Box>
    );
};

export default MapComponent; 