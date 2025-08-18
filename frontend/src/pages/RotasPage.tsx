import React, { useState } from 'react';
import {
    Box,
    Typography,
    Button,
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    TextField,
    FormControl,
    InputLabel,
    Select,
    MenuItem,
    Chip,
    Card,
    CardContent,
    Grid,
    Alert,
} from '@mui/material';
import {
    Add,
    Edit,
    Delete,
    Visibility,
    Route,
    Timeline,
} from '@mui/icons-material';
import { DataGrid, GridColDef, GridActionsCellItem } from '@mui/x-data-grid';
import { useQuery, useMutation, useQueryClient } from 'react-query';
import { useSnackbar } from 'notistack';
import { Rota, Usuario } from '../types';
import { rotasApi, usuariosApi } from '../services/api';

const RotasPage: React.FC = () => {
    const [openDialog, setOpenDialog] = useState(false);
    const [editingRota, setEditingRota] = useState<Rota | null>(null);
    const [formData, setFormData] = useState({
        nome: '',
        coletorId: '',
        descricao: '',
        observacoes: '',
        status: 'PLANEJADA',
        latitudeInicio: '',
        longitudeInicio: '',
        latitudeFim: '',
        longitudeFim: '',
        capacidadeMaxima: '',
        valorTotalEstimado: '',
    });

    const { enqueueSnackbar } = useSnackbar();
    const queryClient = useQueryClient();

    // Buscar dados
    const { data: rotas = [], isLoading, error } = useQuery('rotas', rotasApi.listar);
    const { data: coletores = [] } = useQuery('coletores', () =>
        usuariosApi.buscarPorTipo('COLETOR')
    );

    // Mutations
    const createMutation = useMutation(rotasApi.criar, {
        onSuccess: () => {
            queryClient.invalidateQueries('rotas');
            enqueueSnackbar('Rota criada com sucesso!', { variant: 'success' });
            handleCloseDialog();
        },
        onError: (error: any) => {
            enqueueSnackbar(`Erro ao criar rota: ${error.response?.data?.message || error.message}`, { variant: 'error' });
        },
    });

    const updateMutation = useMutation(
        ({ id, data }: { id: number; data: Partial<Rota> }) => rotasApi.atualizar(id, data),
        {
            onSuccess: () => {
                queryClient.invalidateQueries('rotas');
                enqueueSnackbar('Rota atualizada com sucesso!', { variant: 'success' });
                handleCloseDialog();
            },
            onError: (error: any) => {
                enqueueSnackbar(`Erro ao atualizar rota: ${error.response?.data?.message || error.message}`, { variant: 'error' });
            },
        }
    );

    const deleteMutation = useMutation(rotasApi.excluir, {
        onSuccess: () => {
            queryClient.invalidateQueries('rotas');
            enqueueSnackbar('Rota excluída com sucesso!', { variant: 'success' });
        },
        onError: (error: any) => {
            enqueueSnackbar(`Erro ao excluir rota: ${error.response?.data?.message || error.message}`, { variant: 'error' });
        },
    });

    const handleOpenDialog = (rota?: Rota) => {
        if (rota) {
            setEditingRota(rota);
            setFormData({
                nome: rota.nome,
                coletorId: rota.coletor.id.toString(),
                descricao: rota.descricao || '',
                observacoes: rota.observacoes || '',
                status: rota.status,
                latitudeInicio: rota.latitudeInicio?.toString() || '',
                longitudeInicio: rota.longitudeInicio?.toString() || '',
                latitudeFim: rota.latitudeFim?.toString() || '',
                longitudeFim: rota.longitudeFim?.toString() || '',
                capacidadeMaxima: rota.capacidadeMaxima?.toString() || '',
                valorTotalEstimado: rota.valorTotalEstimado?.toString() || '',
            });
        } else {
            setEditingRota(null);
            setFormData({
                nome: '',
                coletorId: '',
                descricao: '',
                observacoes: '',
                status: 'PLANEJADA',
                latitudeInicio: '',
                longitudeInicio: '',
                latitudeFim: '',
                longitudeFim: '',
                capacidadeMaxima: '',
                valorTotalEstimado: '',
            });
        }
        setOpenDialog(true);
    };

    const handleCloseDialog = () => {
        setOpenDialog(false);
        setEditingRota(null);
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();

        const rotaData = {
            nome: formData.nome,
            coletorId: parseInt(formData.coletorId),
            descricao: formData.descricao || undefined,
            observacoes: formData.observacoes || undefined,
            status: formData.status as "PLANEJADA" | "EM_EXECUCAO" | "FINALIZADA" | "CANCELADA" | "OTIMIZADA",
            latitudeInicio: formData.latitudeInicio ? parseFloat(formData.latitudeInicio) : undefined,
            longitudeInicio: formData.longitudeInicio ? parseFloat(formData.longitudeInicio) : undefined,
            latitudeFim: formData.latitudeFim ? parseFloat(formData.latitudeFim) : undefined,
            longitudeFim: formData.longitudeFim ? parseFloat(formData.longitudeFim) : undefined,
            capacidadeMaxima: formData.capacidadeMaxima ? parseFloat(formData.capacidadeMaxima) : undefined,
            valorTotalEstimado: formData.valorTotalEstimado ? parseFloat(formData.valorTotalEstimado) : undefined,
        };

        if (editingRota) {
            updateMutation.mutate({ id: editingRota.id, data: rotaData });
        } else {
            createMutation.mutate(rotaData);
        }
    };

    const handleDelete = (id: number) => {
        if (window.confirm('Tem certeza que deseja excluir esta rota?')) {
            deleteMutation.mutate(id);
        }
    };

    const getStatusColor = (status: string) => {
        switch (status) {
            case 'PLANEJADA':
                return 'info';
            case 'EM_EXECUCAO':
                return 'primary';
            case 'FINALIZADA':
                return 'success';
            case 'CANCELADA':
                return 'error';
            case 'OTIMIZADA':
                return 'success';
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

    const columns: GridColDef[] = [
        {
            field: 'id',
            headerName: 'ID',
            width: 80,
        },
        {
            field: 'nome',
            headerName: 'Nome',
            flex: 1,
            minWidth: 200,
        },
        {
            field: 'coletor.nome',
            headerName: 'Coletor',
            flex: 1,
            minWidth: 150,
            valueGetter: (params) => params.row.coletor?.nome || '',
        },
        {
            field: 'status',
            headerName: 'Status',
            width: 120,
            renderCell: (params) => (
                <Chip
                    label={params.value}
                    color={getStatusColor(params.value) as any}
                    size="small"
                />
            ),
        },
        {
            field: 'distanciaTotal',
            headerName: 'Distância (m)',
            width: 120,
            type: 'number',
            renderCell: (params) => params.value ? `${params.value}m` : '-',
        },
        {
            field: 'tempoEstimado',
            headerName: 'Tempo (min)',
            width: 120,
            type: 'number',
            renderCell: (params) => params.value ? `${params.value}min` : '-',
        },
        {
            field: 'valorTotalEstimado',
            headerName: 'Valor Estimado',
            width: 120,
            renderCell: (params) => params.value ? formatCurrency(params.value) : '-',
        },
        {
            field: 'actions',
            type: 'actions',
            headerName: 'Ações',
            width: 120,
            getActions: (params) => [
                <GridActionsCellItem
                    icon={<Visibility />}
                    label="Visualizar"
                    onClick={() => handleOpenDialog(params.row)}
                />,
                <GridActionsCellItem
                    icon={<Edit />}
                    label="Editar"
                    onClick={() => handleOpenDialog(params.row)}
                />,
                <GridActionsCellItem
                    icon={<Delete />}
                    label="Excluir"
                    onClick={() => handleDelete(params.row.id)}
                />,
            ],
        },
    ];

    if (error) {
        return (
            <Box sx={{ p: 3 }}>
                <Alert severity="error">
                    Erro ao carregar rotas: {(error as any).message}
                </Alert>
            </Box>
        );
    }

    return (
        <Box sx={{ p: 3 }}>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
                <Typography variant="h4">
                    Gerenciamento de Rotas
                </Typography>
                <Button
                    variant="contained"
                    startIcon={<Add />}
                    onClick={() => handleOpenDialog()}
                    disabled={coletores.length === 0}
                >
                    Nova Rota
                </Button>
            </Box>

            {coletores.length === 0 && (
                <Alert severity="warning" sx={{ mb: 2 }}>
                    É necessário cadastrar coletores (usuários do tipo COLETOR) antes de criar rotas.
                </Alert>
            )}

            <Card>
                <CardContent>
                    {rotas.length === 0 && !isLoading ? (
                        <Box sx={{
                            display: 'flex',
                            flexDirection: 'column',
                            alignItems: 'center',
                            py: 4,
                            textAlign: 'center'
                        }}>
                            <Route sx={{ fontSize: 64, color: 'text.secondary', mb: 2 }} />
                            <Typography variant="h6" color="text.secondary" gutterBottom>
                                Nenhuma rota cadastrada
                            </Typography>
                            <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                                {coletores.length === 0
                                    ? 'Cadastre coletores primeiro para poder criar rotas de coleta.'
                                    : 'Clique em "Nova Rota" para começar a cadastrar rotas de coleta.'
                                }
                            </Typography>
                            {coletores.length > 0 && (
                                <Button
                                    variant="contained"
                                    startIcon={<Add />}
                                    onClick={() => handleOpenDialog()}
                                >
                                    Cadastrar Primeira Rota
                                </Button>
                            )}
                        </Box>
                    ) : (
                        <DataGrid
                            rows={rotas || []}
                            columns={columns}
                            loading={isLoading}
                            autoHeight
                            pageSizeOptions={[10, 25, 50]}
                            initialState={{
                                pagination: {
                                    paginationModel: { page: 0, pageSize: 10 },
                                },
                            }}
                            sx={{
                                '& .MuiDataGrid-cell': {
                                    borderBottom: '1px solid',
                                    borderColor: 'divider',
                                },
                            }}
                        />
                    )}
                </CardContent>
            </Card>

            {/* Dialog para criar/editar rota */}
            <Dialog open={openDialog} onClose={handleCloseDialog} maxWidth="md" fullWidth>
                <DialogTitle>
                    {editingRota ? 'Editar Rota' : 'Nova Rota'}
                </DialogTitle>
                <form onSubmit={handleSubmit}>
                    <DialogContent>
                        <Grid container spacing={2}>
                            <Grid item xs={12} sm={6}>
                                <TextField
                                    fullWidth
                                    label="Nome da Rota"
                                    value={formData.nome}
                                    onChange={(e) => setFormData({ ...formData, nome: e.target.value })}
                                    required
                                    margin="normal"
                                />
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <FormControl fullWidth margin="normal">
                                    <InputLabel>Coletor</InputLabel>
                                    <Select
                                        value={formData.coletorId}
                                        onChange={(e) => setFormData({ ...formData, coletorId: e.target.value })}
                                        label="Coletor"
                                        required
                                    >
                                        {coletores?.map((coletor: Usuario) => (
                                            <MenuItem key={coletor.id} value={coletor.id}>
                                                {coletor.nome} ({coletor.email})
                                            </MenuItem>
                                        ))}
                                    </Select>
                                </FormControl>
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <FormControl fullWidth margin="normal">
                                    <InputLabel>Status</InputLabel>
                                    <Select
                                        value={formData.status}
                                        onChange={(e) => setFormData({ ...formData, status: e.target.value })}
                                        label="Status"
                                    >
                                        <MenuItem value="PLANEJADA">Planejada</MenuItem>
                                        <MenuItem value="EM_EXECUCAO">Em Execução</MenuItem>
                                        <MenuItem value="FINALIZADA">Finalizada</MenuItem>
                                        <MenuItem value="CANCELADA">Cancelada</MenuItem>
                                        <MenuItem value="OTIMIZADA">Otimizada</MenuItem>
                                    </Select>
                                </FormControl>
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <TextField
                                    fullWidth
                                    label="Capacidade Máxima (kg)"
                                    type="number"
                                    value={formData.capacidadeMaxima}
                                    onChange={(e) => setFormData({ ...formData, capacidadeMaxima: e.target.value })}
                                    margin="normal"
                                    inputProps={{ step: 0.01, min: 0 }}
                                />
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <TextField
                                    fullWidth
                                    label="Latitude Início"
                                    type="number"
                                    value={formData.latitudeInicio}
                                    onChange={(e) => setFormData({ ...formData, latitudeInicio: e.target.value })}
                                    margin="normal"
                                />
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <TextField
                                    fullWidth
                                    label="Longitude Início"
                                    type="number"
                                    value={formData.longitudeInicio}
                                    onChange={(e) => setFormData({ ...formData, longitudeInicio: e.target.value })}
                                    margin="normal"
                                />
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <TextField
                                    fullWidth
                                    label="Latitude Fim"
                                    type="number"
                                    value={formData.latitudeFim}
                                    onChange={(e) => setFormData({ ...formData, latitudeFim: e.target.value })}
                                    margin="normal"
                                />
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <TextField
                                    fullWidth
                                    label="Longitude Fim"
                                    type="number"
                                    value={formData.longitudeFim}
                                    onChange={(e) => setFormData({ ...formData, longitudeFim: e.target.value })}
                                    margin="normal"
                                />
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <TextField
                                    fullWidth
                                    label="Valor Total Estimado"
                                    type="number"
                                    value={formData.valorTotalEstimado}
                                    onChange={(e) => setFormData({ ...formData, valorTotalEstimado: e.target.value })}
                                    margin="normal"
                                    inputProps={{ step: 0.01, min: 0 }}
                                />
                            </Grid>
                            <Grid item xs={12}>
                                <TextField
                                    fullWidth
                                    label="Descrição"
                                    multiline
                                    rows={2}
                                    value={formData.descricao}
                                    onChange={(e) => setFormData({ ...formData, descricao: e.target.value })}
                                    margin="normal"
                                />
                            </Grid>
                            <Grid item xs={12}>
                                <TextField
                                    fullWidth
                                    label="Observações"
                                    multiline
                                    rows={3}
                                    value={formData.observacoes}
                                    onChange={(e) => setFormData({ ...formData, observacoes: e.target.value })}
                                    margin="normal"
                                />
                            </Grid>
                        </Grid>
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={handleCloseDialog}>Cancelar</Button>
                        <Button
                            type="submit"
                            variant="contained"
                            disabled={createMutation.isLoading || updateMutation.isLoading}
                        >
                            {editingRota ? 'Atualizar' : 'Criar'}
                        </Button>
                    </DialogActions>
                </form>
            </Dialog>
        </Box>
    );
};

export default RotasPage; 