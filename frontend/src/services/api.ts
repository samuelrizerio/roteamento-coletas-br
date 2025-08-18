import axios from 'axios';
import { Usuario, Material, Coleta, Rota, DashboardStats, ApiResponse, PaginatedResponse } from '../types';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8081/api/v1';

const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

// Interceptor para logs
api.interceptors.request.use(
    (config) => {
        console.log(`🚀 API Request: ${config.method?.toUpperCase()} ${config.url}`);
        return config;
    },
    (error) => {
        console.error('❌ API Request Error:', error);
        return Promise.reject(error);
    }
);

api.interceptors.response.use(
    (response) => {
        console.log(`✅ API Response: ${response.status} ${response.config.url}`);
        return response;
    },
    (error) => {
        console.error('❌ API Response Error:', error.response?.data || error.message);
        return Promise.reject(error);
    }
);

// Usuários
export const usuariosApi = {
    listar: () => api.get<Usuario[]>('/usuarios').then(res => res.data),
    buscarPorId: (id: number) => api.get<Usuario>(`/usuarios/${id}`).then(res => res.data),
    criar: (usuario: Partial<Usuario>) => api.post<Usuario>('/usuarios', usuario).then(res => res.data),
    atualizar: (id: number, usuario: Partial<Usuario>) => api.put<Usuario>(`/usuarios/${id}`, usuario).then(res => res.data),
    excluir: (id: number) => api.delete(`/usuarios/${id}`).then(res => res.data),
    buscarPorTipo: (tipo: string) => api.get<Usuario[]>(`/usuarios/tipo/${tipo}`).then(res => res.data),
    buscarPorStatus: (status: string) => api.get<Usuario[]>(`/usuarios/status/${status}`).then(res => res.data),
};

// Materiais
export const materiaisApi = {
    listar: () => api.get<Material[]>('/materiais').then(res => res.data),
    buscarPorId: (id: number) => api.get<Material>(`/materiais/${id}`).then(res => res.data),
    criar: (material: Partial<Material>) => api.post<Material>('/materiais', material).then(res => res.data),
    atualizar: (id: number, material: Partial<Material>) => api.put<Material>(`/materiais/${id}`, material).then(res => res.data),
    excluir: (id: number) => api.delete(`/materiais/${id}`).then(res => res.data),
    buscarPorCategoria: (categoria: string) => api.get<Material[]>(`/materiais/categoria/${categoria}`).then(res => res.data),
    buscarPorNome: (nome: string) => api.get<Material[]>(`/materiais/busca?nome=${nome}`).then(res => res.data),
    buscarPorPreco: (precoMin: number, precoMax: number) =>
        api.get<Material[]>(`/materiais/preco?precoMin=${precoMin}&precoMax=${precoMax}`).then(res => res.data),
    alterarStatus: (id: number, aceitoParaColeta: boolean) =>
        api.patch<Material>(`/materiais/${id}/status`, { aceitoParaColeta }).then(res => res.data),
    obterEstatisticas: () => api.get('/materiais/estatisticas').then(res => res.data),
};

// Coletas
export const coletasApi = {
    listar: () => api.get<Coleta[]>('/coletas').then(res => res.data),
    buscarPorId: (id: number) => api.get<Coleta>(`/coletas/${id}`).then(res => res.data),
    criar: (coleta: Partial<Coleta>) => api.post<Coleta>('/coletas', coleta).then(res => res.data),
    atualizar: (id: number, coleta: Partial<Coleta>) => api.put<Coleta>(`/coletas/${id}`, coleta).then(res => res.data),
    excluir: (id: number) => api.delete(`/coletas/${id}`).then(res => res.data),
    buscarPorStatus: (status: string) => api.get<Coleta[]>(`/coletas/status/${status}`).then(res => res.data),
    buscarPorUsuario: (usuarioId: number) => api.get<Coleta[]>(`/coletas/usuario/${usuarioId}`).then(res => res.data),
    buscarPorMaterial: (materialId: number) => api.get<Coleta[]>(`/coletas/material/${materialId}`).then(res => res.data),
    buscarPorColetor: (coletorId: number) => api.get<Coleta[]>(`/coletas/coletor/${coletorId}`).then(res => res.data),
    alterarStatus: (id: number, status: string) => api.patch<Coleta>(`/coletas/${id}/status`, { status }).then(res => res.data),
    atribuirColetor: (id: number, coletorId: number) => api.patch<Coleta>(`/coletas/${id}/coletor`, { coletorId }).then(res => res.data),
};

// Rotas
export const rotasApi = {
    listar: () => api.get<Rota[]>('/rotas').then(res => res.data),
    buscarPorId: (id: number) => api.get<Rota>(`/rotas/${id}`).then(res => res.data),
    criar: (rota: Partial<Rota>) => api.post<Rota>('/rotas', rota).then(res => res.data),
    atualizar: (id: number, rota: Partial<Rota>) => api.put<Rota>(`/rotas/${id}`, rota).then(res => res.data),
    excluir: (id: number) => api.delete(`/rotas/${id}`).then(res => res.data),
    buscarPorStatus: (status: string) => api.get<Rota[]>(`/rotas/status/${status}`).then(res => res.data),
    buscarPorColetor: (coletorId: number) => api.get<Rota[]>(`/rotas/coletor/${coletorId}`).then(res => res.data),
    alterarStatus: (id: number, status: string) => api.patch<Rota>(`/rotas/${id}/status`, { status }).then(res => res.data),
    adicionarColeta: (rotaId: number, coletaId: number) =>
        api.post(`/rotas/${rotaId}/coletas/${coletaId}`).then(res => res.data),
    removerColeta: (rotaId: number, coletaId: number) =>
        api.delete(`/rotas/${rotaId}/coletas/${coletaId}`).then(res => res.data),
    otimizarRota: (id: number) => api.post<Rota>(`/rotas/${id}/otimizar`).then(res => res.data),
    calcularRota: (coletas: number[]) => api.post<Rota>('/rotas/calcular', { coletaIds: coletas }).then(res => res.data),
};

// Dashboard
export const dashboardApi = {
    obterEstatisticas: () => api.get<DashboardStats>('/dashboard/estatisticas').then(res => res.data),
    obterColetasRecentes: () => api.get<Coleta[]>('/dashboard/coletas-recentes').then(res => res.data),
    obterRotasAtivas: () => api.get<Rota[]>('/dashboard/rotas-ativas').then(res => res.data),
    obterMateriaisPopulares: () => api.get<Material[]>('/dashboard/materiais-populares'),
    obterMapaPontos: () => api.get('/dashboard/mapa-pontos'),
};

// Roteamento
export const roteamentoApi = {
    calcularRota: (coletas: number[]) => api.post<Rota>('/roteamento/calcular', { coletaIds: coletas }),
    otimizarRotas: () => api.post<Rota[]>('/roteamento/otimizar'),
    buscarColetasProximas: (latitude: number, longitude: number, raio: number) =>
        api.get<Coleta[]>(`/roteamento/coletas-proximas?lat=${latitude}&lng=${longitude}&raio=${raio}`),
    obterColetorDisponivel: (latitude: number, longitude: number) =>
        api.get<Usuario>(`/roteamento/coletor-disponivel?lat=${latitude}&lng=${longitude}`),
};

export default api; 