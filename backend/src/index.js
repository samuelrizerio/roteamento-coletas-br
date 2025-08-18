import { Router } from 'itty-router';
import { cors } from 'cors';

// Importar rotas
import { coletasRoutes } from './routes/coletas.js';
import { rotasRoutes } from './routes/rotas.js';
import { materiaisRoutes } from './routes/materiais.js';
import { usuariosRoutes } from './routes/usuarios.js';
import { dashboardRoutes } from './routes/dashboard.js';

// Configurar CORS
const corsMiddleware = cors({
    origin: ['https://app.samuelchaves.com', 'https://samuelchaves.com'],
    credentials: true,
});

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
        headers: { 'Content-Type': 'application/json' },
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
        headers: { 'Content-Type': 'application/json' },
    });
});

// Handler principal
export default {
    async fetch(request, env, ctx) {
        try {
            return await router.handle(request, env, ctx);
        } catch (error) {
            console.error('Error:', error);
            return new Response(JSON.stringify({
                error: 'Internal Server Error',
                message: error.message
            }), {
                status: 500,
                headers: { 'Content-Type': 'application/json' },
            });
        }
    },
};
