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
    LocalShipping,
} from '@mui/icons-material';
import { DataGrid, GridColDef, GridActionsCellItem } from '@mui/x-data-grid';
import { useQuery, useMutation, useQueryClient } from 'react-query';
import { useSnackbar } from 'notistack';
import { Coleta, Usuario, Material } from '../types';
import { coletasApi, usuariosApi, materiaisApi } from '../services/api';

const ColetasPage: React.FC = () => {
    const [openDialog, setOpenDialog] = useState(false);
    const [editingColeta, setEditingColeta] = useState<Coleta | null>(null);
    const [formData, setFormData] = useState({
        usuarioId: '',
        materialId: '',
        quantidade: '',
        endereco: '',
        latitude: '',
        longitude: '',
        observacoes: '',
        status: 'SOLICITADA',
    });

    const { enqueueSnackbar } = useSnackbar();
    const queryClient = useQueryClient();

    // Buscar dados
    const { data: coletas = [], isLoading, error } = useQuery('coletas', coletasApi.listar);
    const { data: usuarios = [] } = useQuery('usuarios', usuariosApi.listar);
    const { data: materiais = [] } = useQuery('materiais', materiaisApi.listar);

    // Mutations
    const createMutation = useMutation(coletasApi.criar, {
        onSuccess: () => {
            queryClient.invalidateQueries('coletas');
            enqueueSnackbar('Coleta criada com sucesso!', { variant: 'success' });
            handleCloseDialog();
        },
        onError: (error: any) => {
            enqueueSnackbar(`Erro ao criar coleta: ${error.response?.data?.message || error.message}`, { variant: 'error' });
        },
    });

    const updateMutation = useMutation(
        ({ id, data }: { id: number; data: Partial<Coleta> }) => coletasApi.atualizar(id, data),
        {
            onSuccess: () => {
                queryClient.invalidateQueries('coletas');
                enqueueSnackbar('Coleta atualizada com sucesso!', { variant: 'success' });
                handleCloseDialog();
            },
            onError: (error: any) => {
                enqueueSnackbar(`Erro ao atualizar coleta: ${error.response?.data?.message || error.message}`, { variant: 'error' });
            },
        }
    );

    const deleteMutation = useMutation(coletasApi.excluir, {
        onSuccess: () => {
            queryClient.invalidateQueries('coletas');
            enqueueSnackbar('Coleta excluída com sucesso!', { variant: 'success' });
        },
        onError: (error: any) => {
            enqueueSnackbar(`Erro ao excluir coleta: ${error.response?.data?.message || error.message}`, { variant: 'error' });
        },
    });

    const handleOpenDialog = (coleta?: Coleta) => {
        if (coleta) {
            setEditingColeta(coleta);
            setFormData({
                usuarioId: coleta.usuario.id.toString(),
                materialId: coleta.material.id.toString(),
                quantidade: coleta.quantidade.toString(),
                endereco: coleta.endereco || '',
                latitude: coleta.latitude?.toString() || '',
                longitude: coleta.longitude?.toString() || '',
                observacoes: coleta.observacoes || '',
                status: coleta.status,
            });
        } else {
            setEditingColeta(null);
            setFormData({
                usuarioId: '',
                materialId: '',
                quantidade: '',
                endereco: '',
                latitude: '',
                longitude: '',
                observacoes: '',
                status: 'SOLICITADA',
            });
        }
        setOpenDialog(true);
    };

    const handleCloseDialog = () => {
        setOpenDialog(false);
        setEditingColeta(null);
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();

        const coletaData = {
            usuarioId: parseInt(formData.usuarioId),
            materialId: parseInt(formData.materialId),
            quantidade: parseFloat(formData.quantidade),
            endereco: formData.endereco || undefined,
            latitude: formData.latitude ? parseFloat(formData.latitude) : undefined,
            longitude: formData.longitude ? parseFloat(formData.longitude) : undefined,
            observacoes: formData.observacoes || undefined,
            status: formData.status as "SOLICITADA" | "EM_ANALISE" | "ACEITA" | "EM_ROTA" | "COLETADA" | "CANCELADA" | "REJEITADA",
        };

        if (editingColeta) {
            updateMutation.mutate({ id: editingColeta.id, data: coletaData });
        } else {
            createMutation.mutate(coletaData);
        }
    };

    const handleDelete = (id: number) => {
        if (window.confirm('Tem certeza que deseja excluir esta coleta?')) {
            deleteMutation.mutate(id);
        }
    };

    const getStatusColor = (status: string) => {
        switch (status) {
            case 'SOLICITADA':
                return 'warning';
            case 'EM_ANALISE':
                return 'info';
            case 'ACEITA':
                return 'success';
            case 'EM_ROTA':
                return 'primary';
            case 'COLETADA':
                return 'success';
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

    const columns: GridColDef[] = [
        {
            field: 'id',
            headerName: 'ID',
            width: 80,
        },
        {
            field: 'usuario.nome',
            headerName: 'Usuário',
            flex: 1,
            minWidth: 200,
            valueGetter: (params) => params.row.usuario?.nome || '',
        },
        {
            field: 'material.nome',
            headerName: 'Material',
            flex: 1,
            minWidth: 150,
            valueGetter: (params) => params.row.material?.nome || '',
        },
        {
            field: 'quantidade',
            headerName: 'Quantidade (kg)',
            width: 120,
            type: 'number',
        },
        {
            field: 'valorEstimado',
            headerName: 'Valor Estimado',
            width: 120,
            renderCell: (params) => params.value ? formatCurrency(params.value) : '-',
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
            field: 'coletor.nome',
            headerName: 'Coletor',
            width: 150,
            valueGetter: (params) => params.row.coletor?.nome || '-',
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
                    Erro ao carregar coletas: {(error as any).message}
                </Alert>
            </Box>
        );
    }

    return (
        <Box sx={{ p: 3 }}>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
                <Typography variant="h4">
                    Gerenciamento de Coletas
                </Typography>
                <Button
                    variant="contained"
                    startIcon={<Add />}
                    onClick={() => handleOpenDialog()}
                    disabled={usuarios.length === 0 || materiais.length === 0}
                >
                    Nova Coleta
                </Button>
            </Box>

            {usuarios.length === 0 && (
                <Alert severity="warning" sx={{ mb: 2 }}>
                    É necessário cadastrar usuários antes de criar coletas.
                </Alert>
            )}

            {materiais.length === 0 && (
                <Alert severity="warning" sx={{ mb: 2 }}>
                    É necessário cadastrar materiais antes de criar coletas.
                </Alert>
            )}

            <Card>
                <CardContent>
                    {coletas.length === 0 && !isLoading ? (
                        <Box sx={{
                            display: 'flex',
                            flexDirection: 'column',
                            alignItems: 'center',
                            py: 4,
                            textAlign: 'center'
                        }}>
                            <LocalShipping sx={{ fontSize: 64, color: 'text.secondary', mb: 2 }} />
                            <Typography variant="h6" color="text.secondary" gutterBottom>
                                Nenhuma coleta cadastrada
                            </Typography>
                            <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                                {usuarios.length === 0 || materiais.length === 0
                                    ? 'Cadastre usuários e materiais primeiro para poder criar coletas.'
                                    : 'Clique em "Nova Coleta" para começar a cadastrar solicitações de coleta.'
                                }
                            </Typography>
                            {usuarios.length > 0 && materiais.length > 0 && (
                                <Button
                                    variant="contained"
                                    startIcon={<Add />}
                                    onClick={() => handleOpenDialog()}
                                >
                                    Cadastrar Primeira Coleta
                                </Button>
                            )}
                        </Box>
                    ) : (
                        <DataGrid
                            rows={coletas || []}
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

            {/* Dialog para criar/editar coleta */}
            <Dialog open={openDialog} onClose={handleCloseDialog} maxWidth="md" fullWidth>
                <DialogTitle>
                    {editingColeta ? 'Editar Coleta' : 'Nova Coleta'}
                </DialogTitle>
                <form onSubmit={handleSubmit}>
                    <DialogContent>
                        <Grid container spacing={2}>
                            <Grid item xs={12} sm={6}>
                                <FormControl fullWidth margin="normal">
                                    <InputLabel>Usuário</InputLabel>
                                    <Select
                                        value={formData.usuarioId}
                                        onChange={(e) => setFormData({ ...formData, usuarioId: e.target.value })}
                                        label="Usuário"
                                        required
                                    >
                                        {usuarios.map((usuario) => (
                                            <MenuItem key={usuario.id} value={usuario.id}>
                                                {usuario.nome} ({usuario.email})
                                            </MenuItem>
                                        ))}
                                    </Select>
                                </FormControl>
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <FormControl fullWidth margin="normal">
                                    <InputLabel>Material</InputLabel>
                                    <Select
                                        value={formData.materialId}
                                        onChange={(e) => setFormData({ ...formData, materialId: e.target.value })}
                                        label="Material"
                                        required
                                    >
                                        {materiais.map((material) => (
                                            <MenuItem key={material.id} value={material.id}>
                                                {material.nome} - {material.categoria}
                                            </MenuItem>
                                        ))}
                                    </Select>
                                </FormControl>
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <TextField
                                    fullWidth
                                    label="Quantidade (kg)"
                                    type="number"
                                    value={formData.quantidade}
                                    onChange={(e) => setFormData({ ...formData, quantidade: e.target.value })}
                                    required
                                    margin="normal"
                                    inputProps={{ step: 0.01, min: 0 }}
                                />
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <FormControl fullWidth margin="normal">
                                    <InputLabel>Status</InputLabel>
                                    <Select
                                        value={formData.status}
                                        onChange={(e) => setFormData({ ...formData, status: e.target.value })}
                                        label="Status"
                                    >
                                        <MenuItem value="SOLICITADA">Solicitada</MenuItem>
                                        <MenuItem value="EM_ANALISE">Em Análise</MenuItem>
                                        <MenuItem value="ACEITA">Aceita</MenuItem>
                                        <MenuItem value="EM_ROTA">Em Rota</MenuItem>
                                        <MenuItem value="COLETADA">Coletada</MenuItem>
                                        <MenuItem value="CANCELADA">Cancelada</MenuItem>
                                        <MenuItem value="REJEITADA">Rejeitada</MenuItem>
                                    </Select>
                                </FormControl>
                            </Grid>
                            <Grid item xs={12}>
                                <TextField
                                    fullWidth
                                    label="Endereço"
                                    value={formData.endereco}
                                    onChange={(e) => setFormData({ ...formData, endereco: e.target.value })}
                                    margin="normal"
                                />
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <TextField
                                    fullWidth
                                    label="Latitude"
                                    type="number"
                                    value={formData.latitude}
                                    onChange={(e) => setFormData({ ...formData, latitude: e.target.value })}
                                    margin="normal"
                                />
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <TextField
                                    fullWidth
                                    label="Longitude"
                                    type="number"
                                    value={formData.longitude}
                                    onChange={(e) => setFormData({ ...formData, longitude: e.target.value })}
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
                            {editingColeta ? 'Atualizar' : 'Criar'}
                        </Button>
                    </DialogActions>
                </form>
            </Dialog>
        </Box>
    );
};

export default ColetasPage; 