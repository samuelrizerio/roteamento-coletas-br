export const environment = {
    production: process.env.NODE_ENV === 'production',

    // URLs da API
    apiUrl: process.env.NODE_ENV === 'production'
        ? 'https://api.samuelchaves.com'
        : 'http://localhost:8787',

    // URL do frontend
    frontendUrl: process.env.NODE_ENV === 'production'
        ? 'https://app.samuelchaves.com'
        : 'http://localhost:3000',

    // Configurações do Google Maps
    googleMapsApiKey: process.env.REACT_APP_GOOGLE_MAPS_API_KEY || '',

    // Configurações de ambiente
    isDevelopment: process.env.NODE_ENV === 'development',
    isStaging: process.env.NODE_ENV === 'staging',
    isProduction: process.env.NODE_ENV === 'production',

    // Configurações de mapa
    MAP_FALLBACK_ENABLED: process.env.REACT_APP_MAP_FALLBACK_ENABLED === 'true' || false,
    USE_OPENSTREETMAP_FALLBACK: process.env.REACT_APP_USE_OPENSTREETMAP_FALLBACK === 'true' || false,
};

// Exportar como ENV_CONFIG para compatibilidade
export const ENV_CONFIG = {
    ...environment,
    GOOGLE_MAPS_API_KEY: environment.googleMapsApiKey,
    MAP_FALLBACK_ENABLED: environment.MAP_FALLBACK_ENABLED,
    USE_OPENSTREETMAP_FALLBACK: environment.USE_OPENSTREETMAP_FALLBACK,
};
