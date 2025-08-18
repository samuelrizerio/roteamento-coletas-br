import { Router } from 'itty-router';

// Importar rotas
import { coletasRoutes } from './routes/coletas.js';
import { rotasRoutes } from './routes/rotas.js';
import { materiaisRoutes } from './routes/materiais.js';
import { usuariosRoutes } from './routes/usuarios.js';
import { dashboardRoutes } from './routes/dashboard.js';

// Configurar CORS para Cloudflare Workers
const corsMiddleware = (request) => {
    const origin = request.headers.get('Origin');
    const allowedOrigins = ['https://app.samuelchaves.com', 'https://samuelchaves.com', 'http://localhost:3000'];

    if (request.method === 'OPTIONS') {
        return new Response(null, {
            status: 200,
            headers: {
                'Access-Control-Allow-Origin': allowedOrigins.includes(origin) ? origin : allowedOrigins[0],
                'Access-Control-Allow-Methods': 'GET, POST, PUT, DELETE, OPTIONS',
                'Access-Control-Allow-Headers': 'Content-Type, Authorization',
                'Access-Control-Allow-Credentials': 'true',
            },
        });
    }

    return null; // Continue to next middleware
};

// Criar router
const router = Router();

// Middleware global
router.all('*', corsMiddleware);

// Rotas da API
router.use('/api/coletas', coletasRoutes);
router.use('/api/rotas', rotasRoutes);
router.use('/api/materiais', materiaisRoutes);
router.use('/api/usuarios', usuariosRoutes);
router.use('/api/dashboard', dashboardRoutes);

// Health check
router.get('/health', () => {
    return new Response(JSON.stringify({ status: 'OK', timestamp: new Date().toISOString() }), {
        headers: {
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': '*',
        },
    });
});

// Rota padrão
router.get('/', () => {
    return new Response(JSON.stringify({
        message: 'API Roteamento Coletas BR',
        version: '1.0.0',
        endpoints: [
            '/api/coletas',
            '/api/rotas',
            '/api/materiais',
            '/api/usuarios',
            '/api/dashboard'
        ]
    }), {
        headers: {
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': '*',
        },
    });
});

// Handler principal
export default {
    async fetch(request, env, ctx) {
        try {
            const response = await router.handle(request, env, ctx);

            // Add CORS headers to all responses
            if (response) {
                const origin = request.headers.get('Origin');
                const allowedOrigins = ['https://app.samuelchaves.com', 'https://samuelchaves.com', 'http://localhost:3000'];

                response.headers.set('Access-Control-Allow-Origin', allowedOrigins.includes(origin) ? origin : allowedOrigins[0]);
                response.headers.set('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS');
                response.headers.set('Access-Control-Allow-Headers', 'Content-Type, Authorization');
                response.headers.set('Access-Control-Allow-Credentials', 'true');
            }

            return response;
        } catch (error) {
            console.error('Error:', error);
            return new Response(JSON.stringify({
                error: 'Internal Server Error',
                message: error.message
            }), {
                status: 500,
                headers: {
                    'Content-Type': 'application/json',
                    'Access-Control-Allow-Origin': '*',
                },
            });
        }
    },
};
