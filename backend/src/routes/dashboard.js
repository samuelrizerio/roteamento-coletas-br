import { Router } from 'itty-router';

const router = Router();

// GET /api/dashboard - Estatísticas do dashboard
router.get('/', async (request, env) => {
    try {
        // TODO: Implementar busca de estatísticas no banco D1
        return new Response(JSON.stringify({
            message: 'Estatísticas do dashboard',
            data: {
                totalColetas: 0,
                coletasPendentes: 0,
                coletasConcluidas: 0,
                totalRotas: 0,
                totalMateriais: 0,
                totalUsuarios: 0
            }
        }), {
            headers: { 'Content-Type': 'application/json' },
        });
    } catch (error) {
        return new Response(JSON.stringify({
            error: 'Erro ao buscar estatísticas',
            message: error.message
        }), {
            status: 500,
            headers: { 'Content-Type': 'application/json' },
        });
    }
});

export { router as dashboardRoutes };
