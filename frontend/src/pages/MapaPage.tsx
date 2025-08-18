import React, { useState } from 'react';
import {
    Box,
    Typography,
    Card,
    CardContent,
    Grid,
    FormControl,
    FormControlLabel,
    Checkbox,
    Chip,
    Alert,
    CircularProgress,
    Paper,
    Tabs,
    Tab,
    Button,
    IconButton,
    Tooltip,
    Slider,
    TextField,
    Divider,
    List,
    ListItem,
    ListItemText,
    ListItemIcon,
} from '@mui/material';
import {
    Map,
    People,
    LocalShipping,
    Route,
    Refresh,
    Fullscreen,
    FullscreenExit,
    ZoomIn,
    ZoomOut,
    MyLocation,
    AutoAwesome,
    FilterList,
    Calculate,
    Timeline,
    LocationOn,
    Directions,
    BugReport,
} from '@mui/icons-material';
import { useQuery, useMutation, useQueryClient } from 'react-query';
import { usuariosApi, coletasApi, rotasApi } from '../services/api';
import GoogleMapComponent from '../components/Map/GoogleMapComponent';
import { Usuario, Coleta, Rota, MapPoint } from '../types';

interface TabPanelProps {
    children?: React.ReactNode;
    index: number;
    value: number;
}

function TabPanel(props: TabPanelProps) {
    const { children, value, index, ...other } = props;

    return (
        <div
            role="tabpanel"
            hidden={value !== index}
            id={`map-tabpanel-${index}`}
            aria-labelledby={`map-tab-${index}`}
            {...other}
        >
            {value === index && <Box sx={{ p: 3 }}>{children}</Box>}
        </div>
    );
}

const MapaPage: React.FC = () => {
    const [tabValue, setTabValue] = useState(0);
    const [selectedPoint, setSelectedPoint] = useState<MapPoint | null>(null);
    const [mapHeight, setMapHeight] = useState('calc(100vh - 200px)');
    const [isFullscreen, setIsFullscreen] = useState(false);
    const [mapError, setMapError] = useState(false);
    
    // Estados para filtros e roteamento
    const [raioBusca, setRaioBusca] = useState<number>(10);
    const [showUsuarios, setShowUsuarios] = useState(true);
    const [showColetas, setShowColetas] = useState(true);
    const [showRotas, setShowRotas] = useState(true);
    const [coletasFiltradas, setColetasFiltradas] = useState<any[]>([]);
    const [rotasFiltradas, setRotasFiltradas] = useState<any[]>([]);
    const [centroMapa, setCentroMapa] = useState({ lat: -23.5505, lng: -46.6333 });

    const queryClient = useQueryClient();

    // Buscar dados
    const { data: usuarios = [], isLoading: loadingUsuarios } = useQuery('usuarios', usuariosApi.listar);
    const { data: coletas = [], isLoading: loadingColetas } = useQuery('coletas', coletasApi.listar);
    const { data: rotas = [], isLoading: loadingRotas } = useQuery('rotas', rotasApi.listar);

    // Buscar dados do mapa
    const { data: dadosMapa, isLoading: loadingDadosMapa } = useQuery(
        'dadosMapa',
        () => fetch('/api/v1/mapa/dados').then(res => res.json()),
        { enabled: !loadingColetas && !loadingRotas }
    );

    // Buscar estatísticas geográficas
    const { data: estatisticasGeograficas } = useQuery(
        'estatisticasGeograficas',
        () => fetch('/api/v1/mapa/estatisticas').then(res => res.json())
    );

    // Mutação para roteamento automático
    const roteamentoMutation = useMutation(
        () => fetch('/api/v1/roteamento-automatico/executar', { method: 'POST' }).then(res => res.json()),
        {
            onSuccess: () => {
                queryClient.invalidateQueries(['coletas', 'rotas', 'dadosMapa']);
            }
        }
    );

    // Mutação para calcular rota
    const calcularRotaMutation = useMutation(
        (pontos: any[]) => fetch('/api/v1/mapa/calcular-rota', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(pontos)
        }).then(res => res.json())
    );

    const isLoading = loadingUsuarios || loadingColetas || loadingRotas || loadingDadosMapa;

    const handleTabChange = (event: React.SyntheticEvent, newValue: number) => {
        setTabValue(newValue);
    };

    const handlePointClick = (point: MapPoint) => {
        setSelectedPoint(point);
    };

    const handleMapError = () => {
        setMapError(true);
    };

    const handleRetryMap = () => {
        setMapError(false);
    };

    const toggleFullscreen = () => {
        if (isFullscreen) {
            setMapHeight('calc(100vh - 200px)');
            setIsFullscreen(false);
        } else {
            setMapHeight('calc(100vh - 100px)');
            setIsFullscreen(true);
        }
    };

    const handleRoteamentoAutomatico = () => {
        roteamentoMutation.mutate();
    };

    const handleCalcularRota = () => {
        // Exemplo: calcular rota entre pontos selecionados
        const pontos = coletas.slice(0, 5).map(coleta => ({
            latitude: coleta.latitude,
            longitude: coleta.longitude
        }));
        
        calcularRotaMutation.mutate(pontos);
    };

    const handleFiltrarPorRegiao = () => {
        // Filtrar coletas e rotas por região
        const coletasNaRegiao = coletas.filter(coleta => {
            if (!coleta.latitude || !coleta.longitude) return false;
            
            const distancia = calcularDistancia(
                centroMapa.lat, centroMapa.lng,
                coleta.latitude, coleta.longitude
            );
            
            return distancia <= raioBusca;
        });

        const rotasNaRegiao = rotas.filter(rota => {
            if (!rota.latitudeInicio || !rota.longitudeInicio) return false;
            
            const distancia = calcularDistancia(
                centroMapa.lat, centroMapa.lng,
                rota.latitudeInicio, rota.longitudeInicio
            );
            
            return distancia <= raioBusca;
        });

        setColetasFiltradas(coletasNaRegiao);
        setRotasFiltradas(rotasNaRegiao);
    };

    const calcularDistancia = (lat1: number, lng1: number, lat2: number, lng2: number) => {
        const R = 6371; // Raio da Terra em km
        const dLat = (lat2 - lat1) * Math.PI / 180;
        const dLng = (lng2 - lng1) * Math.PI / 180;
        const a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                  Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
                  Math.sin(dLng/2) * Math.sin(dLng/2);
        const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R * c;
    };

    const getStatusColor = (status: string) => {
        switch (status) {
            case 'ATIVO':
            case 'ACEITA':
            case 'EM_EXECUCAO':
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

    if (isLoading) {
        return (
            <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '50vh' }}>
                <CircularProgress />
            </Box>
        );
    }

    return (
        <Box sx={{ height: '100%' }}>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
                <Typography variant="h4" component="h1">
                    Mapa Interativo
                </Typography>
                <Box>
                    <Tooltip title="Roteamento Automático">
                        <IconButton 
                            onClick={handleRoteamentoAutomatico}
                            disabled={roteamentoMutation.isLoading}
                            color="primary"
                        >
                            <AutoAwesome />
                        </IconButton>
                    </Tooltip>
                    <Tooltip title="Calcular Rota">
                        <IconButton 
                            onClick={handleCalcularRota}
                            disabled={calcularRotaMutation.isLoading}
                            color="secondary"
                        >
                            <Calculate />
                        </IconButton>
                    </Tooltip>
                    <Tooltip title={isFullscreen ? "Sair da tela cheia" : "Tela cheia"}>
                        <IconButton onClick={toggleFullscreen}>
                            {isFullscreen ? <FullscreenExit /> : <Fullscreen />}
                        </IconButton>
                    </Tooltip>
                </Box>
            </Box>

            <Grid container spacing={2} sx={{ height: 'calc(100vh - 120px)' }}>
                {/* Painel lateral */}
                <Grid item xs={12} md={3}>
                    <Card sx={{ height: '100%', overflow: 'auto' }}>
                        <CardContent>
                            <Typography variant="h6" gutterBottom>
                                Controles do Mapa
                            </Typography>

                            <Tabs value={tabValue} onChange={handleTabChange} sx={{ mb: 2 }}>
                                <Tab label="Visão Geral" />
                                <Tab label="Filtros" />
                                <Tab label="Roteamento" />
                                <Tab label="Detalhes" />
                            </Tabs>

                            <TabPanel value={tabValue} index={0}>
                                <Box sx={{ mb: 2 }}>
                                    <Typography variant="subtitle2" gutterBottom>
                                        Estatísticas
                                    </Typography>
                                    <Grid container spacing={1}>
                                        <Grid item xs={6}>
                                            <Chip
                                                label={`${usuarios.length} Usuários`}
                                                color="primary"
                                                size="small"
                                                icon={<People />}
                                            />
                                        </Grid>
                                        <Grid item xs={6}>
                                            <Chip
                                                label={`${coletas.length} Coletas`}
                                                color="warning"
                                                size="small"
                                                icon={<LocalShipping />}
                                            />
                                        </Grid>
                                        <Grid item xs={6}>
                                            <Chip
                                                label={`${rotas.length} Rotas`}
                                                color="success"
                                                size="small"
                                                icon={<Route />}
                                            />
                                        </Grid>
                                    </Grid>
                                </Box>

                                {estatisticasGeograficas && (
                                    <Box sx={{ mb: 2 }}>
                                        <Typography variant="subtitle2" gutterBottom>
                                            Estatísticas Geográficas
                                        </Typography>
                                        <List dense>
                                            <ListItem>
                                                <ListItemIcon><LocationOn /></ListItemIcon>
                                                <ListItemText 
                                                    primary="Regiões Ativas" 
                                                    secondary={estatisticasGeograficas.regioesAtivas} 
                                                />
                                            </ListItem>
                                            <ListItem>
                                                <ListItemIcon><Timeline /></ListItemIcon>
                                                <ListItemText 
                                                    primary="Densidade Média" 
                                                    secondary={estatisticasGeograficas.densidadeMedia} 
                                                />
                                            </ListItem>
                                            <ListItem>
                                                <ListItemIcon><Directions /></ListItemIcon>
                                                <ListItemText 
                                                    primary="Distância Média" 
                                                    secondary={estatisticasGeograficas.distanciaMedia} 
                                                />
                                            </ListItem>
                                        </List>
                                    </Box>
                                )}

                                <Box sx={{ mb: 2 }}>
                                    <Typography variant="subtitle2" gutterBottom>
                                        Status das Coletas
                                    </Typography>
                                    {Object.entries(coletas.reduce((acc, coleta) => {
                                        acc[coleta.status] = (acc[coleta.status] || 0) + 1;
                                        return acc;
                                    }, {} as Record<string, number>)).map(([status, count]) => (
                                        <Box key={status} sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                                            <Typography variant="body2">{status}</Typography>
                                            <Chip
                                                label={count}
                                                size="small"
                                                color={getStatusColor(status) as any}
                                            />
                                        </Box>
                                    ))}
                                </Box>
                            </TabPanel>

                            <TabPanel value={tabValue} index={1}>
                                <Box sx={{ mb: 2 }}>
                                    <Typography variant="subtitle2" gutterBottom>
                                        Filtros de Visualização
                                    </Typography>
                                    <FormControl component="fieldset">
                                        <FormControlLabel
                                            control={
                                                <Checkbox
                                                    checked={showUsuarios}
                                                    onChange={(e) => setShowUsuarios(e.target.checked)}
                                                />
                                            }
                                            label="Mostrar Usuários"
                                        />
                                        <FormControlLabel
                                            control={
                                                <Checkbox
                                                    checked={showColetas}
                                                    onChange={(e) => setShowColetas(e.target.checked)}
                                                />
                                            }
                                            label="Mostrar Coletas"
                                        />
                                        <FormControlLabel
                                            control={
                                                <Checkbox
                                                    checked={showRotas}
                                                    onChange={(e) => setShowRotas(e.target.checked)}
                                                />
                                            }
                                            label="Mostrar Rotas"
                                        />
                                    </FormControl>
                                </Box>

                                <Box sx={{ mb: 2 }}>
                                    <Typography variant="subtitle2" gutterBottom>
                                        Filtro por Região
                                    </Typography>
                                    <Typography variant="body2" gutterBottom>
                                        Raio de busca: {raioBusca} km
                                    </Typography>
                                    <Slider
                                        value={raioBusca}
                                        onChange={(_, value) => setRaioBusca(value as number)}
                                        min={1}
                                        max={50}
                                        marks
                                        valueLabelDisplay="auto"
                                    />
                                    <Button
                                        variant="outlined"
                                        startIcon={<FilterList />}
                                        onClick={handleFiltrarPorRegiao}
                                        fullWidth
                                        sx={{ mt: 1 }}
                                    >
                                        Filtrar por Região
                                    </Button>
                                </Box>

                                {coletasFiltradas.length > 0 && (
                                    <Box sx={{ mb: 2 }}>
                                        <Typography variant="subtitle2" gutterBottom>
                                            Coletas na Região ({coletasFiltradas.length})
                                        </Typography>
                                        <List dense>
                                            {coletasFiltradas.slice(0, 5).map((coleta) => (
                                                <ListItem key={coleta.id}>
                                                    <ListItemText
                                                        primary={coleta.endereco}
                                                        secondary={`${coleta.quantidade}kg - ${coleta.status}`}
                                                    />
                                                </ListItem>
                                            ))}
                                        </List>
                                    </Box>
                                )}
                            </TabPanel>

                            <TabPanel value={tabValue} index={2}>
                                <Box sx={{ mb: 2 }}>
                                    <Typography variant="subtitle2" gutterBottom>
                                        Algoritmos de Roteamento
                                    </Typography>
                                    
                                    <Button
                                        variant="contained"
                                        startIcon={<AutoAwesome />}
                                        onClick={handleRoteamentoAutomatico}
                                        disabled={roteamentoMutation.isLoading}
                                        fullWidth
                                        sx={{ mb: 2 }}
                                    >
                                        {roteamentoMutation.isLoading ? 'Executando...' : 'Roteamento Automático'}
                                    </Button>

                                    <Button
                                        variant="outlined"
                                        startIcon={<Calculate />}
                                        onClick={handleCalcularRota}
                                        disabled={calcularRotaMutation.isLoading}
                                        fullWidth
                                        sx={{ mb: 2 }}
                                    >
                                        {calcularRotaMutation.isLoading ? 'Calculando...' : 'Calcular Rota'}
                                    </Button>

                                    {roteamentoMutation.isSuccess && (
                                        <Alert severity="success" sx={{ mb: 2 }}>
                                            Roteamento executado com sucesso!
                                        </Alert>
                                    )}

                                    {calcularRotaMutation.isSuccess && (
                                        <Box sx={{ mb: 2 }}>
                                            <Typography variant="subtitle2" gutterBottom>
                                                Rota Calculada
                                            </Typography>
                                            <Typography variant="body2">
                                                Distância: {calcularRotaMutation.data?.distanciaTotal?.toFixed(2)} km
                                            </Typography>
                                            <Typography variant="body2">
                                                Tempo: {calcularRotaMutation.data?.tempoEstimado} min
                                            </Typography>
                                        </Box>
                                    )}
                                </Box>

                                <Box sx={{ mb: 2 }}>
                                    <Typography variant="subtitle2" gutterBottom>
                                        Configurações de Otimização
                                    </Typography>
                                    <TextField
                                        label="Capacidade Máxima (kg)"
                                        type="number"
                                        defaultValue="1000"
                                        size="small"
                                        fullWidth
                                        sx={{ mb: 1 }}
                                    />
                                    <TextField
                                        label="Máximo de Coletas por Rota"
                                        type="number"
                                        defaultValue="15"
                                        size="small"
                                        fullWidth
                                        sx={{ mb: 1 }}
                                    />
                                    <TextField
                                        label="Velocidade Média (km/h)"
                                        type="number"
                                        defaultValue="30"
                                        size="small"
                                        fullWidth
                                    />
                                </Box>
                            </TabPanel>

                            <TabPanel value={tabValue} index={3}>
                                {selectedPoint ? (
                                    <Box>
                                        <Typography variant="subtitle2" gutterBottom>
                                            Ponto Selecionado
                                        </Typography>
                                        <Card variant="outlined" sx={{ mb: 2 }}>
                                            <CardContent>
                                                <Typography variant="h6" gutterBottom>
                                                    {selectedPoint.nome}
                                                </Typography>
                                                <Typography variant="body2" color="text.secondary">
                                                    Tipo: {selectedPoint.tipo}
                                                </Typography>
                                                <Typography variant="body2" color="text.secondary">
                                                    Coordenadas: {selectedPoint.lat.toFixed(6)}, {selectedPoint.lng.toFixed(6)}
                                                </Typography>

                                                {selectedPoint.tipo === 'usuario' && (
                                                    <Box sx={{ mt: 1 }}>
                                                        <Chip
                                                            label={selectedPoint.dados.tipoUsuario}
                                                            color="primary"
                                                            size="small"
                                                            sx={{ mr: 1 }}
                                                        />
                                                        <Chip
                                                            label={selectedPoint.dados.status}
                                                            color={getStatusColor(selectedPoint.dados.status) as any}
                                                            size="small"
                                                        />
                                                    </Box>
                                                )}

                                                {selectedPoint.tipo === 'coleta' && (
                                                    <Box sx={{ mt: 1 }}>
                                                        <Typography variant="body2">
                                                            Material: {selectedPoint.dados.material?.nome}
                                                        </Typography>
                                                        <Typography variant="body2">
                                                            Quantidade: {selectedPoint.dados.quantidade}kg
                                                        </Typography>
                                                        <Chip
                                                            label={selectedPoint.dados.status}
                                                            color={getStatusColor(selectedPoint.dados.status) as any}
                                                            size="small"
                                                        />
                                                    </Box>
                                                )}

                                                {selectedPoint.tipo === 'rota' && (
                                                    <Box sx={{ mt: 1 }}>
                                                        <Typography variant="body2">
                                                            Coletor: {selectedPoint.dados.coletor?.nome}
                                                        </Typography>
                                                        <Typography variant="body2">
                                                            Distância: {selectedPoint.dados.distanciaTotal}m
                                                        </Typography>
                                                        <Chip
                                                            label={selectedPoint.dados.status}
                                                            color={getStatusColor(selectedPoint.dados.status) as any}
                                                            size="small"
                                                        />
                                                    </Box>
                                                )}
                                            </CardContent>
                                        </Card>
                                    </Box>
                                ) : (
                                    <Box>
                                        <Typography variant="subtitle2" gutterBottom>
                                            Instruções
                                        </Typography>
                                        <Alert severity="info" sx={{ mb: 2 }}>
                                            Clique em um ponto no mapa para ver os detalhes.
                                        </Alert>
                                    </Box>
                                )}
                            </TabPanel>
                        </CardContent>
                    </Card>
                </Grid>

                {/* Mapa */}
                <Grid item xs={12} md={9}>
                    <Paper
                        elevation={3}
                        sx={{
                            height: mapHeight,
                            overflow: 'hidden',
                            position: 'relative'
                        }}
                    >
                        {mapError ? (
                            <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100%' }}>
                                <Alert severity="error" sx={{ width: '100%' }}>
                                    Ocorreu um erro ao carregar o mapa. Tente novamente.
                                </Alert>
                            </Box>
                        ) : (
                            <GoogleMapComponent
                                usuarios={usuarios}
                                coletas={coletas}
                                rotas={rotas}
                                height="100%"
                                showUsuarios={showUsuarios}
                                showColetas={showColetas}
                                showRotas={showRotas}
                                onPointClick={handlePointClick}
                            />
                        )}
                    </Paper>
                </Grid>
            </Grid>
        </Box>
    );
};

export default MapaPage; 