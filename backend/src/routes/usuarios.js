import { Router } from 'itty-router';

const router = Router();

// GET /api/usuarios - Listar todos os usuários
router.get('/', async (request, env) => {
    try {
        // TODO: Implementar busca no banco D1
        return new Response(JSON.stringify({
            message: 'Lista de usuários',
            data: []
        }), {
            headers: { 'Content-Type': 'application/json' },
        });
    } catch (error) {
        return new Response(JSON.stringify({
            error: 'Erro ao buscar usuários',
            message: error.message
        }), {
            status: 500,
            headers: { 'Content-Type': 'application/json' },
        });
    }
});

// POST /api/usuarios - Criar novo usuário
router.post('/', async (request, env) => {
    try {
        const body = await request.json();
        // TODO: Implementar criação no banco D1
        return new Response(JSON.stringify({
            message: 'Usuário criado com sucesso',
            data: body
        }), {
            status: 201,
            headers: { 'Content-Type': 'application/json' },
        });
    } catch (error) {
        return new Response(JSON.stringify({
            error: 'Erro ao criar usuário',
            message: error.message
        }), {
            status: 500,
            headers: { 'Content-Type': 'application/json' },
        });
    }
});

export { router as usuariosRoutes };
