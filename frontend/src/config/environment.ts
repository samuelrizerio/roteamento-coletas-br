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
};
