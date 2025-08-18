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
    IconButton,
    Tooltip,
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
    Person,
    Business,
    LocalShipping,
} from '@mui/icons-material';
import { DataGrid, GridColDef, GridActionsCellItem } from '@mui/x-data-grid';
import { useQuery, useMutation, useQueryClient } from 'react-query';
import { useSnackbar } from 'notistack';
import { Usuario } from '../types';
import { usuariosApi } from '../services/api';

const UsuariosPage: React.FC = () => {
    const [openDialog, setOpenDialog] = useState(false);
    const [editingUser, setEditingUser] = useState<Usuario | null>(null);
    const [formData, setFormData] = useState({
        nome: '',
        email: '',
        telefone: '',
        endereco: '',
        latitude: '',
        longitude: '',
        tipoUsuario: 'RESIDENCIAL',
        status: 'ATIVO',
    });

    const { enqueueSnackbar } = useSnackbar();
    const queryClient = useQueryClient();

    // Buscar usuários
    const { data: usuarios = [], isLoading, error } = useQuery('usuarios', usuariosApi.listar);

    // Mutations
    const createMutation = useMutation(usuariosApi.criar, {
        onSuccess: () => {
            queryClient.invalidateQueries('usuarios');
            enqueueSnackbar('Usuário criado com sucesso!', { variant: 'success' });
            handleCloseDialog();
        },
        onError: (error: any) => {
            enqueueSnackbar(`Erro ao criar usuário: ${error.response?.data?.message || error.message}`, { variant: 'error' });
        },
    });

    const updateMutation = useMutation(
        ({ id, data }: { id: number; data: Partial<Usuario> }) => usuariosApi.atualizar(id, data),
        {
            onSuccess: () => {
                queryClient.invalidateQueries('usuarios');
                enqueueSnackbar('Usuário atualizado com sucesso!', { variant: 'success' });
                handleCloseDialog();
            },
            onError: (error: any) => {
                enqueueSnackbar(`Erro ao atualizar usuário: ${error.response?.data?.message || error.message}`, { variant: 'error' });
            },
        }
    );

    const deleteMutation = useMutation(usuariosApi.excluir, {
        onSuccess: () => {
            queryClient.invalidateQueries('usuarios');
            enqueueSnackbar('Usuário excluído com sucesso!', { variant: 'success' });
        },
        onError: (error: any) => {
            enqueueSnackbar(`Erro ao excluir usuário: ${error.response?.data?.message || error.message}`, { variant: 'error' });
        },
    });

    const handleOpenDialog = (user?: Usuario) => {
        if (user) {
            setEditingUser(user);
            setFormData({
                nome: user.nome,
                email: user.email,
                telefone: user.telefone || '',
                endereco: user.endereco || '',
                latitude: user.latitude?.toString() || '',
                longitude: user.longitude?.toString() || '',
                tipoUsuario: user.tipoUsuario,
                status: user.status,
            });
        } else {
            setEditingUser(null);
            setFormData({
                nome: '',
                email: '',
                telefone: '',
                endereco: '',
                latitude: '',
                longitude: '',
                tipoUsuario: 'RESIDENCIAL',
                status: 'ATIVO',
            });
        }
        setOpenDialog(true);
    };

    const handleCloseDialog = () => {
        setOpenDialog(false);
        setEditingUser(null);
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();

        const userData: Partial<Usuario> = {
            ...formData,
            latitude: formData.latitude ? parseFloat(formData.latitude) : undefined,
            longitude: formData.longitude ? parseFloat(formData.longitude) : undefined,
            tipoUsuario: formData.tipoUsuario as "RESIDENCIAL" | "COMERCIAL" | "COLETOR",
            status: formData.status as "ATIVO" | "INATIVO" | "BLOQUEADO",
        };

        if (editingUser) {
            updateMutation.mutate({ id: editingUser.id, data: userData });
        } else {
            createMutation.mutate(userData);
        }
    };

    const handleDelete = (id: number) => {
        if (window.confirm('Tem certeza que deseja excluir este usuário?')) {
            deleteMutation.mutate(id);
        }
    };

    const getTipoUsuarioIcon = (tipo: string) => {
        switch (tipo) {
            case 'RESIDENCIAL':
                return <Person />;
            case 'COMERCIAL':
                return <Business />;
            case 'COLETOR':
                return <LocalShipping />;
            default:
                return <Person />;
        }
    };

    const getStatusColor = (status: string) => {
        switch (status) {
            case 'ATIVO':
                return 'success';
            case 'INATIVO':
                return 'warning';
            case 'BLOQUEADO':
                return 'error';
            default:
                return 'default';
        }
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
            field: 'email',
            headerName: 'Email',
            flex: 1,
            minWidth: 250,
        },
        {
            field: 'tipoUsuario',
            headerName: 'Tipo',
            width: 150,
            renderCell: (params) => (
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                    {getTipoUsuarioIcon(params.value)}
                    <Chip
                        label={params.value}
                        size="small"
                        variant="outlined"
                    />
                </Box>
            ),
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
            field: 'telefone',
            headerName: 'Telefone',
            width: 150,
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
                    Erro ao carregar usuários: {(error as any).message}
                </Alert>
            </Box>
        );
    }

    return (
        <Box sx={{ p: 3 }}>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
                <Typography variant="h4">
                    Gerenciamento de Usuários
                </Typography>
                <Button
                    variant="contained"
                    startIcon={<Add />}
                    onClick={() => handleOpenDialog()}
                >
                    Novo Usuário
                </Button>
            </Box>

            <Card>
                <CardContent>
                    <DataGrid
                        rows={usuarios || []}
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
                </CardContent>
            </Card>

            {/* Dialog para criar/editar usuário */}
            <Dialog open={openDialog} onClose={handleCloseDialog} maxWidth="md" fullWidth>
                <DialogTitle>
                    {editingUser ? 'Editar Usuário' : 'Novo Usuário'}
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
                                <TextField
                                    fullWidth
                                    label="Email"
                                    type="email"
                                    value={formData.email}
                                    onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                                    required
                                    margin="normal"
                                />
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <TextField
                                    fullWidth
                                    label="Telefone"
                                    value={formData.telefone}
                                    onChange={(e) => setFormData({ ...formData, telefone: e.target.value })}
                                    margin="normal"
                                />
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <FormControl fullWidth margin="normal">
                                    <InputLabel>Tipo de Usuário</InputLabel>
                                    <Select
                                        value={formData.tipoUsuario}
                                        onChange={(e) => setFormData({ ...formData, tipoUsuario: e.target.value })}
                                        label="Tipo de Usuário"
                                    >
                                        <MenuItem value="RESIDENCIAL">Residencial</MenuItem>
                                        <MenuItem value="COMERCIAL">Comercial</MenuItem>
                                        <MenuItem value="COLETOR">Coletor</MenuItem>
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
                            <Grid item xs={12} sm={6}>
                                <FormControl fullWidth margin="normal">
                                    <InputLabel>Status</InputLabel>
                                    <Select
                                        value={formData.status}
                                        onChange={(e) => setFormData({ ...formData, status: e.target.value })}
                                        label="Status"
                                    >
                                        <MenuItem value="ATIVO">Ativo</MenuItem>
                                        <MenuItem value="INATIVO">Inativo</MenuItem>
                                        <MenuItem value="BLOQUEADO">Bloqueado</MenuItem>
                                    </Select>
                                </FormControl>
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
                            {editingUser ? 'Atualizar' : 'Criar'}
                        </Button>
                    </DialogActions>
                </form>
            </Dialog>
        </Box>
    );
};

export default UsuariosPage; 