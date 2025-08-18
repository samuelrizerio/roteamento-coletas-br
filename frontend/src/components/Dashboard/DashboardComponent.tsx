import React, { useState, useEffect } from 'react';
import {
    Box,
    Grid,
    Card,
    CardContent,
    Typography,
    Chip,
    IconButton,
    Tooltip,
    LinearProgress,
    Paper,
} from '@mui/material';
import {
    People,
    Recycling,
    LocalShipping,
    Route,
    TrendingUp,
    AttachMoney,
    Schedule,
    CheckCircle,
    Warning,
    Error,
} from '@mui/icons-material';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip as RechartsTooltip, ResponsiveContainer, PieChart, Pie, Cell } from 'recharts';
import { format } from 'date-fns';
import { ptBR } from 'date-fns/locale';
import { DashboardStats, Usuario, Material, Coleta, Rota } from '../../types';
import { dashboardApi } from '../../services/api';
import SustentabilidadeStats from './SustentabilidadeStats';
import AlertasColeta from './AlertasColeta';

const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#8884D8', '#82CA9D'];

const DashboardComponent: React.FC = () => {
    const [stats, setStats] = useState<DashboardStats | null>(null);
    const [coletasRecentes, setColetasRecentes] = useState<Coleta[]>([]);
    const [rotasAtivas, setRotasAtivas] = useState<Rota[]>([]);
    const [loading, setLoading] = useState(true);

    console.log('🏗️ Componente Dashboard renderizado, estado inicial:', { loading, stats, coletasRecentes, rotasAtivas });

    useEffect(() => {
        const loadDashboardData = async () => {
            try {
                setLoading(true);
                console.log('🔄 Iniciando carregamento do dashboard...');

                // Carregar dados em paralelo
                const [statsData, coletasData, rotasData] = await Promise.all([
                    dashboardApi.obterEstatisticas(),
                    dashboardApi.obterColetasRecentes(),
                    dashboardApi.obterRotasAtivas(),
                ]);

                console.log('📊 Dados carregados:', { statsData, coletasData, rotasData });

                setStats(statsData);
                setColetasRecentes(coletasData);
                setRotasAtivas(rotasData);

                console.log('✅ Dashboard carregado com sucesso');
            } catch (error: any) {
                console.error('❌ Erro ao carregar dados do dashboard:', error);
                console.error('Detalhes do erro:', {
                    message: error.message,
                    response: error.response?.data,
                    status: error.response?.status
                });
            } finally {
                setLoading(false);
            }
        };

        loadDashboardData();
    }, []);

    const getStatusColor = (status: string) => {
        switch (status) {
            case 'ATIVO':
            case 'ACEITA':
            case 'EM_EXECUCAO':
            case 'FINALIZADA':
                return 'success';
            case 'PENDENTE':
            case 'SOLICITADA':
            case 'PLANEJADA':
                return 'warning';
            case 'INATIVO':
            case 'CANCELADA':
            case 'REJEITADA':
                return 'error';
            default:
                return 'default';
        }
    };

    const formatCurrency = (value: number) => {
        return new Intl.NumberFormat('pt-BR', {
            style: 'currency',
            currency: 'BRL',
        }).format(value);
    };

    const formatDate = (dateString: string) => {
        return format(new Date(dateString), 'dd/MM/yyyy HH:mm', { locale: ptBR });
    };

    if (loading) {
        return (
            <Box sx={{ p: 3 }}>
                <LinearProgress />
                <Typography variant="h6" sx={{ mt: 2 }}>
                    Carregando dashboard...
                </Typography>
            </Box>
        );
    }

    console.log('🔍 Estado atual:', { loading, stats, coletasRecentes, rotasAtivas });

    if (!stats) {
        console.log('❌ Stats é null/undefined, mostrando erro');
        return (
            <Box sx={{ p: 3 }}>
                <Typography variant="h6" color="error">
                    Erro ao carregar dados do dashboard
                </Typography>
                <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
                    Status: {loading ? 'Carregando...' : 'Falha no carregamento'}
                </Typography>
            </Box>
        );
    }

    // Preparar dados para gráficos
    const materiaisData = Object.entries(stats.materiaisPorCategoria).map(([categoria, quantidade]) => ({
        name: categoria,
        value: quantidade,
    }));

    const coletasData = Object.entries(stats.coletasPorStatus).map(([status, quantidade]) => ({
        name: status,
        value: quantidade,
    }));

    return (
        <Box sx={{ p: 3 }}>


            {/* Cards de estatísticas */}
            <Grid container spacing={3} sx={{ mb: 4 }}>
                <Grid item xs={12} sm={6} md={3}>
                    <Card>
                        <CardContent>
                            <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                                <People sx={{ fontSize: 40, color: 'primary.main', mr: 2 }} />
                                <Box>
                                    <Typography variant="h4" component="div">
                                        {stats.totalUsuarios}
                                    </Typography>
                                    <Typography variant="body2" color="text.secondary">
                                        Usuários Cadastrados
                                    </Typography>
                                </Box>
                            </Box>
                        </CardContent>
                    </Card>
                </Grid>

                <Grid item xs={12} sm={6} md={3}>
                    <Card>
                        <CardContent>
                            <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                                <Recycling sx={{ fontSize: 40, color: 'secondary.main', mr: 2 }} />
                                <Box>
                                    <Typography variant="h4" component="div">
                                        {stats.totalMateriais}
                                    </Typography>
                                    <Typography variant="body2" color="text.secondary">
                                        Materiais Disponíveis
                                    </Typography>
                                </Box>
                            </Box>
                        </CardContent>
                    </Card>
                </Grid>

                <Grid item xs={12} sm={6} md={3}>
                    <Card>
                        <CardContent>
                            <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                                <LocalShipping sx={{ fontSize: 40, color: 'warning.main', mr: 2 }} />
                                <Box>
                                    <Typography variant="h4" component="div">
                                        {stats.totalColetas}
                                    </Typography>
                                    <Typography variant="body2" color="text.secondary">
                                        Coletas Realizadas
                                    </Typography>
                                </Box>
                            </Box>
                        </CardContent>
                    </Card>
                </Grid>

                <Grid item xs={12} sm={6} md={3}>
                    <Card>
                        <CardContent>
                            <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                                <Route sx={{ fontSize: 40, color: 'success.main', mr: 2 }} />
                                <Box>
                                    <Typography variant="h4" component="div">
                                        {stats.totalRotas}
                                    </Typography>
                                    <Typography variant="body2" color="text.secondary">
                                        Rotas Criadas
                                    </Typography>
                                </Box>
                            </Box>
                        </CardContent>
                    </Card>
                </Grid>
            </Grid>

            {/* Cards de status */}
            <Grid container spacing={3} sx={{ mb: 4 }}>
                <Grid item xs={12} sm={6} md={3}>
                    <Card>
                        <CardContent>
                            <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                                <Box>
                                    <Typography variant="h6" color="warning.main">
                                        {stats.coletasPendentes}
                                    </Typography>
                                    <Typography variant="body2" color="text.secondary">
                                        Coletas Pendentes
                                    </Typography>
                                </Box>
                                <Warning sx={{ fontSize: 30, color: 'warning.main' }} />
                            </Box>
                        </CardContent>
                    </Card>
                </Grid>

                <Grid item xs={12} sm={6} md={3}>
                    <Card>
                        <CardContent>
                            <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                                <Box>
                                    <Typography variant="h6" color="success.main">
                                        {stats.rotasEmExecucao}
                                    </Typography>
                                    <Typography variant="body2" color="text.secondary">
                                        Rotas em Execução
                                    </Typography>
                                </Box>
                                <CheckCircle sx={{ fontSize: 30, color: 'success.main' }} />
                            </Box>
                        </CardContent>
                    </Card>
                </Grid>

                <Grid item xs={12} sm={6} md={3}>
                    <Card>
                        <CardContent>
                            <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                                <Box>
                                    <Typography variant="h6" color="info.main">
                                        {formatCurrency(stats.valorTotalEstimado)}
                                    </Typography>
                                    <Typography variant="body2" color="text.secondary">
                                        Valor Total Estimado
                                    </Typography>
                                </Box>
                                <AttachMoney sx={{ fontSize: 30, color: 'info.main' }} />
                            </Box>
                        </CardContent>
                    </Card>
                </Grid>

                <Grid item xs={12} sm={6} md={3}>
                    <Card>
                        <CardContent>
                            <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                                <Box>
                                    <Typography variant="h6" color="primary.main">
                                        {stats.totalColetas > 0 ? Math.round((stats.coletasPendentes / stats.totalColetas) * 100) : 0}%
                                    </Typography>
                                    <Typography variant="body2" color="text.secondary">
                                        Taxa de Conclusão
                                    </Typography>
                                </Box>
                                <TrendingUp sx={{ fontSize: 30, color: 'primary.main' }} />
                            </Box>
                        </CardContent>
                    </Card>
                </Grid>
            </Grid>

            {/* Gráficos */}
            <Grid container spacing={3} sx={{ mb: 4 }}>
                <Grid item xs={12} md={6}>
                    <Card>
                        <CardContent>
                            <Typography variant="h6" gutterBottom>
                                Materiais por Categoria
                            </Typography>
                            <ResponsiveContainer width="100%" height={300}>
                                <PieChart>
                                    <Pie
                                        data={materiaisData}
                                        cx="50%"
                                        cy="50%"
                                        labelLine={false}
                                        label={({ name, percent }) => `${name} ${(percent * 100).toFixed(0)}%`}
                                        outerRadius={80}
                                        fill="#8884d8"
                                        dataKey="value"
                                    >
                                        {materiaisData.map((entry, index) => (
                                            <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                                        ))}
                                    </Pie>
                                    <RechartsTooltip />
                                </PieChart>
                            </ResponsiveContainer>
                        </CardContent>
                    </Card>
                </Grid>

                <Grid item xs={12} md={6}>
                    <Card>
                        <CardContent>
                            <Typography variant="h6" gutterBottom>
                                Coletas por Status
                            </Typography>
                            <ResponsiveContainer width="100%" height={300}>
                                <BarChart data={coletasData}>
                                    <CartesianGrid strokeDasharray="3 3" />
                                    <XAxis dataKey="name" />
                                    <YAxis />
                                    <RechartsTooltip />
                                    <Bar dataKey="value" fill="#8884d8" />
                                </BarChart>
                            </ResponsiveContainer>
                        </CardContent>
                    </Card>
                </Grid>
            </Grid>

            {/* Componentes Específicos do Negócio */}
            <Grid container spacing={3} sx={{ mb: 4 }}>
                <Grid item xs={12} md={6}>
                    <SustentabilidadeStats
                        totalColetado={stats.totalColetas * 10} // Simulação
                        totalReciclado={stats.totalColetas * 8.5} // Simulação
                        economiaCO2={stats.totalColetas * 2.5} // Simulação
                        economiaAgua={stats.totalColetas * 150} // Simulação
                        economiaEnergia={stats.totalColetas * 12} // Simulação
                        materiaisReciclados={{
                            'Papel e Papelão': stats.totalColetas * 3.2,
                            'Plástico': stats.totalColetas * 2.8,
                            'Vidro': stats.totalColetas * 1.5,
                            'Metal': stats.totalColetas * 0.8,
                            'Eletrônicos': stats.totalColetas * 0.2,
                        }}
                    />
                </Grid>

                <Grid item xs={12} md={6}>
                    <AlertasColeta
                        alertas={[
                            {
                                id: '1',
                                tipo: 'URGENTE',
                                titulo: 'Rota Atrasada',
                                descricao: 'Rota Vila Madalena - Manhã está 30 minutos atrasada',
                                timestamp: new Date().toISOString(),
                                acao: 'Reagendar',
                            },
                            {
                                id: '2',
                                tipo: 'ATENCAO',
                                titulo: 'Coleta Pendente',
                                descricao: '5 coletas aguardando aceitação há mais de 24h',
                                timestamp: new Date().toISOString(),
                                acao: 'Revisar',
                            },
                            {
                                id: '3',
                                tipo: 'INFO',
                                titulo: 'Material Crítico',
                                descricao: 'Eletrônicos com alta demanda - estoque baixo',
                                timestamp: new Date().toISOString(),
                            },
                            {
                                id: '4',
                                tipo: 'SUCESSO',
                                titulo: 'Meta Atingida',
                                descricao: 'Taxa de reciclagem atingiu 85% este mês',
                                timestamp: new Date().toISOString(),
                            },
                        ]}
                        rotasAtrasadas={2}
                        coletasPendentes={5}
                        materiaisCriticos={['Eletrônicos', 'Óleo de Cozinha']}
                    />
                </Grid>
            </Grid>

            {/* Coletas Recentes */}
            <Grid container spacing={3}>
                <Grid item xs={12} md={6}>
                    <Card>
                        <CardContent>
                            <Typography variant="h6" gutterBottom>
                                Coletas Recentes
                            </Typography>
                            <Box sx={{ maxHeight: 400, overflow: 'auto' }}>
                                {coletasRecentes.map((coleta) => (
                                    <Paper key={coleta.id} sx={{ p: 2, mb: 2 }}>
                                        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                                            <Box>
                                                <Typography variant="subtitle1">
                                                    Coleta #{coleta.id}
                                                </Typography>
                                                <Typography variant="body2" color="text.secondary">
                                                    {coleta.material.nome} - {coleta.quantidade}kg
                                                </Typography>
                                                <Typography variant="caption" color="text.secondary">
                                                    {formatDate(coleta.dataCriacao)}
                                                </Typography>
                                            </Box>
                                            <Chip
                                                label={coleta.status}
                                                color={getStatusColor(coleta.status) as any}
                                                size="small"
                                            />
                                        </Box>
                                    </Paper>
                                ))}
                            </Box>
                        </CardContent>
                    </Card>
                </Grid>

                <Grid item xs={12} md={6}>
                    <Card>
                        <CardContent>
                            <Typography variant="h6" gutterBottom>
                                Rotas Ativas
                            </Typography>
                            <Box sx={{ maxHeight: 400, overflow: 'auto' }}>
                                {rotasAtivas.map((rota) => (
                                    <Paper key={rota.id} sx={{ p: 2, mb: 2 }}>
                                        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                                            <Box>
                                                <Typography variant="subtitle1">
                                                    {rota.nome}
                                                </Typography>
                                                <Typography variant="body2" color="text.secondary">
                                                    Coletor: {rota.coletor.nome}
                                                </Typography>
                                                <Typography variant="caption" color="text.secondary">
                                                    {rota.distanciaTotal}m - {rota.tempoEstimado}min
                                                </Typography>
                                            </Box>
                                            <Chip
                                                label={rota.status}
                                                color={getStatusColor(rota.status) as any}
                                                size="small"
                                            />
                                        </Box>
                                    </Paper>
                                ))}
                            </Box>
                        </CardContent>
                    </Card>
                </Grid>
            </Grid>
        </Box>
    );
};

export default DashboardComponent; 