import { Router } from 'itty-router';

const router = Router();

// GET /api/materiais - Listar todos os materiais
router.get('/', async (request, env) => {
    try {
        // TODO: Implementar busca no banco D1
        return new Response(JSON.stringify({
            message: 'Lista de materiais',
            data: []
        }), {
            headers: { 'Content-Type': 'application/json' },
        });
    } catch (error) {
        return new Response(JSON.stringify({
            error: 'Erro ao buscar materiais',
            message: error.message
        }), {
            status: 500,
            headers: { 'Content-Type': 'application/json' },
        });
    }
});

// POST /api/materiais - Criar novo material
router.post('/', async (request, env) => {
    try {
        const body = await request.json();
        // TODO: Implementar criação no banco D1
        return new Response(JSON.stringify({
            message: 'Material criado com sucesso',
            data: body
        }), {
            status: 201,
            headers: { 'Content-Type': 'application/json' },
        });
    } catch (error) {
        return new Response(JSON.stringify({
            error: 'Erro ao criar material',
            message: error.message
        }), {
            status: 500,
            headers: { 'Content-Type': 'application/json' },
        });
    }
});

export { router as materiaisRoutes };
