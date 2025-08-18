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
    Switch,
    FormControlLabel,
} from '@mui/material';
import {
    Add,
    Edit,
    Delete,
    Visibility,
    Recycling,
} from '@mui/icons-material';
import { DataGrid, GridColDef, GridActionsCellItem } from '@mui/x-data-grid';
import { useQuery, useMutation, useQueryClient } from 'react-query';
import { useSnackbar } from 'notistack';
import { Material } from '../types';
import { materiaisApi } from '../services/api';

const MateriaisPage: React.FC = () => {
    const [openDialog, setOpenDialog] = useState(false);
    const [editingMaterial, setEditingMaterial] = useState<Material | null>(null);
    const [formData, setFormData] = useState({
        nome: '',
        categoria: 'PAPEL',
        corIdentificacao: '',
        descricao: '',
        instrucoesPreparacao: '',
        valorPorQuilo: '',
        aceitoParaColeta: true,
    });

    const { enqueueSnackbar } = useSnackbar();
    const queryClient = useQueryClient();

    // Buscar materiais
    const { data: materiais = [], isLoading, error } = useQuery('materiais', materiaisApi.listar);

    // Mutations
    const createMutation = useMutation(materiaisApi.criar, {
        onSuccess: () => {
            queryClient.invalidateQueries('materiais');
            enqueueSnackbar('Material criado com sucesso!', { variant: 'success' });
            handleCloseDialog();
        },
        onError: (error: any) => {
            enqueueSnackbar(`Erro ao criar material: ${error.response?.data?.message || error.message}`, { variant: 'error' });
        },
    });

    const updateMutation = useMutation(
        ({ id, data }: { id: number; data: Partial<Material> }) => materiaisApi.atualizar(id, data),
        {
            onSuccess: () => {
                queryClient.invalidateQueries('materiais');
                enqueueSnackbar('Material atualizado com sucesso!', { variant: 'success' });
                handleCloseDialog();
            },
            onError: (error: any) => {
                enqueueSnackbar(`Erro ao atualizar material: ${error.response?.data?.message || error.message}`, { variant: 'error' });
            },
        }
    );

    const deleteMutation = useMutation(materiaisApi.excluir, {
        onSuccess: () => {
            queryClient.invalidateQueries('materiais');
            enqueueSnackbar('Material excluído com sucesso!', { variant: 'success' });
        },
        onError: (error: any) => {
            enqueueSnackbar(`Erro ao excluir material: ${error.response?.data?.message || error.message}`, { variant: 'error' });
        },
    });

    const handleOpenDialog = (material?: Material) => {
        if (material) {
            setEditingMaterial(material);
            setFormData({
                nome: material.nome,
                categoria: material.categoria,
                corIdentificacao: material.corIdentificacao || '',
                descricao: material.descricao || '',
                instrucoesPreparacao: material.instrucoesPreparacao || '',
                valorPorQuilo: material.valorPorQuilo.toString(),
                aceitoParaColeta: material.aceitoParaColeta,
            });
        } else {
            setEditingMaterial(null);
            setFormData({
                nome: '',
                categoria: 'PAPEL',
                corIdentificacao: '',
                descricao: '',
                instrucoesPreparacao: '',
                valorPorQuilo: '',
                aceitoParaColeta: true,
            });
        }
        setOpenDialog(true);
    };

    const handleCloseDialog = () => {
        setOpenDialog(false);
        setEditingMaterial(null);
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();

        const materialData: Partial<Material> = {
            ...formData,
            valorPorQuilo: parseFloat(formData.valorPorQuilo),
            categoria: formData.categoria as "PAPEL" | "PLASTICO" | "VIDRO" | "METAL" | "ORGANICO" | "ELETRONICO" | "PERIGOSO",
            corIdentificacao: formData.corIdentificacao as "AZUL" | "VERMELHO" | "VERDE" | "AMARELO" | "MARROM" | "LARANJA" | "ROXO" | undefined,
        };

        if (editingMaterial) {
            updateMutation.mutate({ id: editingMaterial.id, data: materialData });
        } else {
            createMutation.mutate(materialData);
        }
    };

    const handleDelete = (id: number) => {
        if (window.confirm('Tem certeza que deseja excluir este material?')) {
            deleteMutation.mutate(id);
        }
    };

    const getCategoriaColor = (categoria: string) => {
        switch (categoria) {
            case 'PAPEL':
                return 'primary';
            case 'PLASTICO':
                return 'secondary';
            case 'VIDRO':
                return 'info';
            case 'METAL':
                return 'warning';
            case 'ORGANICO':
                return 'success';
            case 'ELETRONICO':
                return 'error';
            case 'PERIGOSO':
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
            field: 'nome',
            headerName: 'Nome',
            flex: 1,
            minWidth: 200,
        },
        {
            field: 'categoria',
            headerName: 'Categoria',
            width: 150,
            renderCell: (params) => (
                <Chip
                    label={params.value}
                    color={getCategoriaColor(params.value) as any}
                    size="small"
                />
            ),
        },
        {
            field: 'valorPorQuilo',
            headerName: 'Valor/kg',
            width: 120,
            renderCell: (params) => formatCurrency(params.value),
        },
        {
            field: 'aceitoParaColeta',
            headerName: 'Aceito',
            width: 100,
            renderCell: (params) => (
                <Chip
                    label={params.value ? 'Sim' : 'Não'}
                    color={params.value ? 'success' : 'error'}
                    size="small"
                />
            ),
        },
        {
            field: 'corIdentificacao',
            headerName: 'Cor',
            width: 100,
            renderCell: (params) => params.value ? (
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                    <Box
                        sx={{
                            width: 20,
                            height: 20,
                            borderRadius: '50%',
                            backgroundColor: params.value.toLowerCase(),
                            border: '1px solid #ccc',
                        }}
                    />
                    <Typography variant="body2">{params.value}</Typography>
                </Box>
            ) : '-',
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
                    Erro ao carregar materiais: {(error as any).message}
                </Alert>
            </Box>
        );
    }

    return (
        <Box sx={{ p: 3 }}>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
                <Typography variant="h4">
                    Gerenciamento de Materiais
                </Typography>
                <Button
                    variant="contained"
                    startIcon={<Add />}
                    onClick={() => handleOpenDialog()}
                >
                    Novo Material
                </Button>
            </Box>

            <Card>
                <CardContent>
                    {materiais.length === 0 && !isLoading ? (
                        <Box sx={{
                            display: 'flex',
                            flexDirection: 'column',
                            alignItems: 'center',
                            py: 4,
                            textAlign: 'center'
                        }}>
                            <Recycling sx={{ fontSize: 64, color: 'text.secondary', mb: 2 }} />
                            <Typography variant="h6" color="text.secondary" gutterBottom>
                                Nenhum material cadastrado
                            </Typography>
                            <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                                Clique em "Novo Material" para começar a cadastrar materiais recicláveis.
                            </Typography>
                            <Button
                                variant="contained"
                                startIcon={<Add />}
                                onClick={() => handleOpenDialog()}
                            >
                                Cadastrar Primeiro Material
                            </Button>
                        </Box>
                    ) : (
                        <DataGrid
                            rows={materiais || []}
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

            {/* Dialog para criar/editar material */}
            <Dialog open={openDialog} onClose={handleCloseDialog} maxWidth="md" fullWidth>
                <DialogTitle>
                    {editingMaterial ? 'Editar Material' : 'Novo Material'}
                </DialogTitle>
                <form onSubmit={handleSubmit}>
                    <DialogContent>
                        <Grid container spacing={2}>
                            <Grid item xs={12} sm={6}>
                                <TextField
                                    fullWidth
                                    label="Nome"
                                    value={formData.nome}
                                    onChange={(e) => setFormData({ ...formData, nome: e.target.value })}
                                    required
                                    margin="normal"
                                />
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <FormControl fullWidth margin="normal">
                                    <InputLabel>Categoria</InputLabel>
                                    <Select
                                        value={formData.categoria}
                                        onChange={(e) => setFormData({ ...formData, categoria: e.target.value })}
                                        label="Categoria"
                                    >
                                        <MenuItem value="PAPEL">Papel</MenuItem>
                                        <MenuItem value="PLASTICO">Plástico</MenuItem>
                                        <MenuItem value="VIDRO">Vidro</MenuItem>
                                        <MenuItem value="METAL">Metal</MenuItem>
                                        <MenuItem value="ORGANICO">Orgânico</MenuItem>
                                        <MenuItem value="ELETRONICO">Eletrônico</MenuItem>
                                        <MenuItem value="PERIGOSO">Perigoso</MenuItem>
                                    </Select>
                                </FormControl>
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <TextField
                                    fullWidth
                                    label="Valor por Quilo"
                                    type="number"
                                    value={formData.valorPorQuilo}
                                    onChange={(e) => setFormData({ ...formData, valorPorQuilo: e.target.value })}
                                    required
                                    margin="normal"
                                    inputProps={{ step: 0.01, min: 0 }}
                                />
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <FormControl fullWidth margin="normal">
                                    <InputLabel>Cor de Identificação</InputLabel>
                                    <Select
                                        value={formData.corIdentificacao}
                                        onChange={(e) => setFormData({ ...formData, corIdentificacao: e.target.value })}
                                        label="Cor de Identificação"
                                    >
                                        <MenuItem value="">Nenhuma</MenuItem>
                                        <MenuItem value="AZUL">Azul</MenuItem>
                                        <MenuItem value="VERMELHO">Vermelho</MenuItem>
                                        <MenuItem value="VERDE">Verde</MenuItem>
                                        <MenuItem value="AMARELO">Amarelo</MenuItem>
                                        <MenuItem value="MARROM">Marrom</MenuItem>
                                        <MenuItem value="LARANJA">Laranja</MenuItem>
                                        <MenuItem value="ROXO">Roxo</MenuItem>
                                    </Select>
                                </FormControl>
                            </Grid>
                            <Grid item xs={12}>
                                <TextField
                                    fullWidth
                                    label="Descrição"
                                    multiline
                                    rows={3}
                                    value={formData.descricao}
                                    onChange={(e) => setFormData({ ...formData, descricao: e.target.value })}
                                    margin="normal"
                                />
                            </Grid>
                            <Grid item xs={12}>
                                <TextField
                                    fullWidth
                                    label="Instruções de Preparação"
                                    multiline
                                    rows={3}
                                    value={formData.instrucoesPreparacao}
                                    onChange={(e) => setFormData({ ...formData, instrucoesPreparacao: e.target.value })}
                                    margin="normal"
                                />
                            </Grid>
                            <Grid item xs={12}>
                                <FormControlLabel
                                    control={
                                        <Switch
                                            checked={formData.aceitoParaColeta}
                                            onChange={(e) => setFormData({ ...formData, aceitoParaColeta: e.target.checked })}
                                        />
                                    }
                                    label="Aceito para Coleta"
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
                            {editingMaterial ? 'Atualizar' : 'Criar'}
                        </Button>
                    </DialogActions>
                </form>
            </Dialog>
        </Box>
    );
};

export default MateriaisPage; 