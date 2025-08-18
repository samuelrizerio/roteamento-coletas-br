import React, { useEffect, useRef, useState } from 'react';
import { Box, Typography, Alert, Button } from '@mui/material';
import { Refresh, Map } from '@mui/icons-material';
import { FALLBACK_CONFIG } from '../../config/maps';

interface MapFallbackProps {
    height?: string;
    onRetry?: () => void;
    error?: string;
}

const MapFallback: React.FC<MapFallbackProps> = ({ 
    height = '600px', 
    onRetry,
    error
}) => {
    const mapRef = useRef<HTMLDivElement>(null);
    const [mapInstance, setMapInstance] = useState<any>(null);
    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        if (FALLBACK_CONFIG.enabled && FALLBACK_CONFIG.useOpenStreetMap) {
            initializeOpenStreetMap();
        }
    }, []);

    const initializeOpenStreetMap = async () => {
        if (!mapRef.current) return;

        try {
            setIsLoading(true);

            // Carregar Leaflet (biblioteca para OpenStreetMap)
            if (!window.L) {
                await loadLeaflet();
            }

            if (window.L && mapRef.current) {
                const map = window.L.map(mapRef.current).setView([-23.5505, -46.6333], 13);

                // Adicionar camada do OpenStreetMap
                window.L.tileLayer(FALLBACK_CONFIG.openStreetMapUrl, {
                    attribution: FALLBACK_CONFIG.attribution,
                    maxZoom: 19,
                }).addTo(map);

                setMapInstance(map);
            }
        } catch (err) {
            console.error('Erro ao inicializar OpenStreetMap:', err);
        } finally {
            setIsLoading(false);
        }
    };

    const loadLeaflet = async () => {
        return new Promise<void>((resolve, reject) => {
            // Carregar CSS do Leaflet
            const link = document.createElement('link');
            link.rel = 'stylesheet';
            link.href = 'https://unpkg.com/leaflet@1.9.4/dist/leaflet.css';
            link.integrity = 'sha256-p4NxAoJBhIIN+hmNHrzRCf9tD/miZyoHS5obTRR9BMY=';
            link.crossOrigin = '';
            document.head.appendChild(link);

            // Carregar JavaScript do Leaflet
            const script = document.createElement('script');
            script.src = 'https://unpkg.com/leaflet@1.9.4/dist/leaflet.js';
            script.integrity = 'sha256-20nQCchB9co0qIjJZRGuk2/Z9VM+kNiyxNV1lvTlZBo=';
            script.crossOrigin = '';
            script.onload = () => resolve();
            script.onerror = () => reject(new Error('Falha ao carregar Leaflet'));
            document.head.appendChild(script);
        });
    };

    const handleRetry = () => {
        if (onRetry) {
            onRetry();
        } else {
            // Tentar recarregar o mapa
            if (mapInstance) {
                mapInstance.remove();
                setMapInstance(null);
            }
            initializeOpenStreetMap();
        }
    };

    if (error) {
        return (
            <Box
                sx={{
                    height,
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    justifyContent: 'center',
                    bgcolor: 'grey.100',
                    p: 3,
                }}
            >
                <Alert severity="error" sx={{ mb: 2, maxWidth: 400 }}>
                    <Typography variant="body1" gutterBottom>
                        Erro ao carregar o mapa
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                        {error}
                    </Typography>
                </Alert>

                <Button
                    variant="contained"
                    startIcon={<Refresh />}
                    onClick={handleRetry}
                    sx={{ mt: 2 }}
                >
                    Tentar Novamente
                </Button>
            </Box>
        );
    }

    if (isLoading) {
        return (
            <Box
                sx={{
                    height,
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    bgcolor: 'grey.100',
                }}
            >
                <Typography variant="body1" color="text.secondary">
                    Carregando mapa alternativo...
                </Typography>
            </Box>
        );
    }

    return (
        <Box sx={{ height, position: 'relative' }}>
            <Box
                ref={mapRef}
                sx={{
                    height: '100%',
                    width: '100%',
                    bgcolor: 'grey.200',
                }}
            />

            {/* Overlay informativo */}
            <Box
                sx={{
                    position: 'absolute',
                    top: 10,
                    left: 10,
                    bgcolor: 'rgba(255, 255, 255, 0.9)',
                    p: 1,
                    borderRadius: 1,
                    display: 'flex',
                    alignItems: 'center',
                    gap: 1,
                }}
            >
                <Map color="primary" fontSize="small" />
                <Typography variant="caption" color="text.secondary">
                    Mapa alternativo (OpenStreetMap)
                </Typography>
            </Box>
        </Box>
    );
};

export default MapFallback; 