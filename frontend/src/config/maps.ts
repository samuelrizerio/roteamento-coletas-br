import { ENV_CONFIG } from './environment';

// Configuração do Google Maps API
export const GOOGLE_MAPS_CONFIG = {
    // Chave pública de exemplo - em produção, use uma chave real
    // IMPORTANTE: Esta chave é apenas para desenvolvimento
    // Para produção, configure uma chave válida no arquivo .env
    apiKey: ENV_CONFIG.GOOGLE_MAPS_API_KEY,
    libraries: ['places', 'geometry'] as any,
    version: 'weekly',
    // Configurações de fallback
    fallbackEnabled: ENV_CONFIG.MAP_FALLBACK_ENABLED,
    retryAttempts: 3,
    retryDelay: 2000,
};

// Configuração de estilos do mapa
export const MAP_STYLES = [
    {
        featureType: 'poi',
        elementType: 'labels',
        stylers: [{ visibility: 'off' }]
    },
    {
        featureType: 'transit',
        elementType: 'labels',
        stylers: [{ visibility: 'off' }]
    }
];

// Configuração de ícones personalizados
export const CUSTOM_ICONS = {
    USUARIO: {
        color: '#2196f3',
        label: 'U'
    },
    COLETA: {
        color: '#ff9800',
        label: 'C'
    },
    ROTA: {
        color: '#4caf50',
        label: 'R'
    },
    COLETOR: {
        color: '#9c27b0',
        label: 'T'
    }
};

// Configuração de cores por status
export const STATUS_COLORS = {
    ATIVO: '#4caf50',
    INATIVO: '#f44336',
    SOLICITADA: '#ff9800',
    ACEITA: '#4caf50',
    EM_EXECUCAO: '#2196f3',
    PLANEJADA: '#2196f3',
    FINALIZADA: '#4caf50',
    CANCELADA: '#f44336',
    REJEITADA: '#f44336',
} as const;

// Configuração de fallback para quando o Google Maps não estiver disponível
export const FALLBACK_CONFIG = {
    enabled: ENV_CONFIG.MAP_FALLBACK_ENABLED,
    useOpenStreetMap: ENV_CONFIG.USE_OPENSTREETMAP_FALLBACK,
    openStreetMapUrl: 'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
    attribution: '© OpenStreetMap contributors'
}; 