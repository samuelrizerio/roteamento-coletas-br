import { Router } from 'itty-router';

const router = Router();

// GET /api/rotas - Listar todas as rotas
router.get('/', async (request, env) => {
    try {
        // TODO: Implementar busca no banco D1
        return new Response(JSON.stringify({
            message: 'Lista de rotas',
            data: []
        }), {
            headers: { 'Content-Type': 'application/json' },
        });
    } catch (error) {
        return new Response(JSON.stringify({
            error: 'Erro ao buscar rotas',
            message: error.message
        }), {
            status: 500,
            headers: { 'Content-Type': 'application/json' },
        });
    }
});

// POST /api/rotas - Criar nova rota
router.post('/', async (request, env) => {
    try {
        const body = await request.json();
        // TODO: Implementar criação no banco D1
        return new Response(JSON.stringify({
            message: 'Rota criada com sucesso',
            data: body
        }), {
            status: 201,
            headers: { 'Content-Type': 'application/json' },
        });
    } catch (error) {
        return new Response(JSON.stringify({
            error: 'Erro ao criar rota',
            message: error.message
        }), {
            status: 500,
            headers: { 'Content-Type': 'application/json' },
        });
    }
});

export { router as rotasRoutes };
