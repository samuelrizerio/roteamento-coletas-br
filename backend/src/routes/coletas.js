import { Router } from 'itty-router';

const router = Router();

// GET /api/coletas - Listar todas as coletas
router.get('/', async (request, env) => {
    try {
        // TODO: Implementar busca no banco D1
        return new Response(JSON.stringify({
            message: 'Lista de coletas',
            data: []
        }), {
            headers: { 'Content-Type': 'application/json' },
        });
    } catch (error) {
        return new Response(JSON.stringify({
            error: 'Erro ao buscar coletas',
            message: error.message
        }), {
            status: 500,
            headers: { 'Content-Type': 'application/json' },
        });
    }
});

// POST /api/coletas - Criar nova coleta
router.post('/', async (request, env) => {
    try {
        const body = await request.json();
        // TODO: Implementar criação no banco D1
        return new Response(JSON.stringify({
            message: 'Coleta criada com sucesso',
            data: body
        }), {
            status: 201,
            headers: { 'Content-Type': 'application/json' },
        });
    } catch (error) {
        return new Response(JSON.stringify({
            error: 'Erro ao criar coleta',
            message: error.message
        }), {
            status: 500,
            headers: { 'Content-Type': 'application/json' },
        });
    }
});

export { router as coletasRoutes };
