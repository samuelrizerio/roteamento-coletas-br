export interface Usuario {
    id: number;
    nome: string;
    email: string;
    telefone?: string;
    endereco?: string;
    latitude?: number;
    longitude?: number;
    tipoUsuario: 'RESIDENCIAL' | 'COMERCIAL' | 'COLETOR';
    status: 'ATIVO' | 'INATIVO' | 'BLOQUEADO';
    dataCriacao: string;
    dataAtualizacao?: string;
}

export interface Material {
    id: number;
    nome: string;
    categoria: 'PAPEL' | 'PLASTICO' | 'VIDRO' | 'METAL' | 'ORGANICO' | 'ELETRONICO' | 'PERIGOSO';
    corIdentificacao?: 'AZUL' | 'VERMELHO' | 'VERDE' | 'AMARELO' | 'MARROM' | 'LARANJA' | 'ROXO';
    descricao?: string;
    instrucoesPreparacao?: string;
    valorPorQuilo: number;
    aceitoParaColeta: boolean;
    dataCriacao: string;
    dataAtualizacao?: string;
}

export interface Coleta {
    id: number;
    usuario: Usuario;
    material: Material;
    coletor?: Usuario;
    quantidade: number;
    status: 'SOLICITADA' | 'EM_ANALISE' | 'ACEITA' | 'EM_ROTA' | 'COLETADA' | 'CANCELADA' | 'REJEITADA';
    endereco?: string;
    latitude?: number;
    longitude?: number;
    observacoes?: string;
    valorEstimado?: number;
    valorFinal?: number;
    dataSolicitada?: string;
    dataAceitacao?: string;
    dataColeta?: string;
    dataCriacao: string;
    dataAtualizacao?: string;
}

export interface Rota {
    id: number;
    nome: string;
    coletor: Usuario;
    status: 'PLANEJADA' | 'EM_EXECUCAO' | 'FINALIZADA' | 'CANCELADA' | 'OTIMIZADA';
    descricao?: string;
    observacoes?: string;
    latitudeInicio?: number;
    longitudeInicio?: number;
    latitudeFim?: number;
    longitudeFim?: number;
    distanciaTotal?: number;
    tempoEstimado?: number;
    capacidadeMaxima?: number;
    capacidadeAtual?: number;
    valorTotalEstimado?: number;
    dataInicio?: string;
    dataFim?: string;
    dataCriacao: string;
    dataAtualizacao?: string;
    coletas?: Coleta[];
}

export interface ColetaRota {
    coleta: Coleta;
    rota: Rota;
    ordem: number;
    distanciaProxima?: number;
    tempoEstimado?: number;
    observacoes?: string;
    dataCriacao: string;
}

export interface DashboardStats {
    totalUsuarios: number;
    totalMateriais: number;
    totalColetas: number;
    totalRotas: number;
    coletasPendentes: number;
    rotasEmExecucao: number;
    valorTotalEstimado: number;
    materiaisPorCategoria: Record<string, number>;
    coletasPorStatus: Record<string, number>;
}

export interface MapPoint {
    id: number;
    nome: string;
    tipo: 'usuario' | 'coleta' | 'rota' | 'coletor';
    lat: number;
    lng: number;
    dados: any;
}

export interface ApiResponse<T> {
    data: T;
    message?: string;
    success: boolean;
}

export interface PaginatedResponse<T> {
    content: T[];
    totalElements: number;
    totalPages: number;
    size: number;
    number: number;
    first: boolean;
    last: boolean;
} 