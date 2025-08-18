import React from 'react';
import {
    Box,
    Card,
    CardContent,
    Typography,
    List,
    ListItem,
    ListItemIcon,
    ListItemText,
    Chip,
    IconButton,
    Collapse,
    Alert,
    Grid,
} from '@mui/material';
import {
    Warning,
    Info,
    CheckCircle,
    Error,
    ExpandMore,
    ExpandLess,
    LocalShipping,
    Schedule,
    PriorityHigh,
} from '@mui/icons-material';
import Nature from '@mui/icons-material/Nature';

interface AlertaColeta {
    id: string;
    tipo: 'URGENTE' | 'ATENCAO' | 'INFO' | 'SUCESSO';
    titulo: string;
    descricao: string;
    timestamp: string;
    acao?: string;
}

interface AlertasColetaProps {
    alertas: AlertaColeta[];
    rotasAtrasadas: number;
    coletasPendentes: number;
    materiaisCriticos: string[];
}

const AlertasColeta: React.FC<AlertasColetaProps> = ({
    alertas,
    rotasAtrasadas,
    coletasPendentes,
    materiaisCriticos,
}) => {
    const [expanded, setExpanded] = React.useState(true);

    const getTipoIcon = (tipo: string) => {
        switch (tipo) {
            case 'URGENTE':
                return <Error color="error" />;
            case 'ATENCAO':
                return <Warning color="warning" />;
            case 'INFO':
                return <Info color="info" />;
            case 'SUCESSO':
                return <CheckCircle color="success" />;
            default:
                return <Info color="info" />;
        }
    };

    const getTipoColor = (tipo: string) => {
        switch (tipo) {
            case 'URGENTE':
                return 'error';
            case 'ATENCAO':
                return 'warning';
            case 'INFO':
                return 'info';
            case 'SUCESSO':
                return 'success';
            default:
                return 'default';
        }
    };

    const formatarTimestamp = (timestamp: string) => {
        return new Date(timestamp).toLocaleString('pt-BR', {
            day: '2-digit',
            month: '2-digit',
            hour: '2-digit',
            minute: '2-digit',
        });
    };

    return (
        <Card sx={{ height: '100%' }}>
            <CardContent>
                <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', mb: 2 }}>
                    <Typography variant="h6" sx={{ display: 'flex', alignItems: 'center' }}>
                        <PriorityHigh sx={{ mr: 1, color: 'warning.main' }} />
                        Alertas e Notificações
                    </Typography>
                    <IconButton
                        size="small"
                        onClick={() => setExpanded(!expanded)}
                    >
                        {expanded ? <ExpandLess /> : <ExpandMore />}
                    </IconButton>
                </Box>

                <Collapse in={expanded}>
                    {/* Resumo de Alertas */}
                    <Box sx={{ mb: 2 }}>
                        <Grid container spacing={1}>
                            <Grid item xs={4}>
                                <Box sx={{ textAlign: 'center', p: 1, backgroundColor: 'error.light', borderRadius: 1 }}>
                                    <Typography variant="h6" color="error.dark">
                                        {rotasAtrasadas}
                                    </Typography>
                                    <Typography variant="caption" color="error.dark">
                                        Rotas Atrasadas
                                    </Typography>
                                </Box>
                            </Grid>
                            <Grid item xs={4}>
                                <Box sx={{ textAlign: 'center', p: 1, backgroundColor: 'warning.light', borderRadius: 1 }}>
                                    <Typography variant="h6" color="warning.dark">
                                        {coletasPendentes}
                                    </Typography>
                                    <Typography variant="caption" color="warning.dark">
                                        Coletas Pendentes
                                    </Typography>
                                </Box>
                            </Grid>
                            <Grid item xs={4}>
                                <Box sx={{ textAlign: 'center', p: 1, backgroundColor: 'info.light', borderRadius: 1 }}>
                                    <Typography variant="h6" color="info.dark">
                                        {materiaisCriticos.length}
                                    </Typography>
                                    <Typography variant="caption" color="info.dark">
                                        Materiais Críticos
                                    </Typography>
                                </Box>
                            </Grid>
                        </Grid>
                    </Box>

                    {/* Lista de Alertas */}
                    <List sx={{ maxHeight: 300, overflow: 'auto' }}>
                        {alertas.map((alerta) => (
                            <ListItem
                                key={alerta.id}
                                sx={{
                                    border: 1,
                                    borderColor: 'divider',
                                    borderRadius: 1,
                                    mb: 1,
                                    backgroundColor: `${getTipoColor(alerta.tipo)}.light`,
                                }}
                            >
                                <ListItemIcon>
                                    {getTipoIcon(alerta.tipo)}
                                </ListItemIcon>
                                <ListItemText
                                    primary={
                                        <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                                            <Typography variant="subtitle2" fontWeight="bold">
                                                {alerta.titulo}
                                            </Typography>
                                            <Chip
                                                label={alerta.tipo}
                                                size="small"
                                                color={getTipoColor(alerta.tipo) as any}
                                            />
                                        </Box>
                                    }
                                    secondary={
                                        <Box>
                                            <Typography variant="body2" color="text.secondary">
                                                {alerta.descricao}
                                            </Typography>
                                            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mt: 1 }}>
                                                <Typography variant="caption" color="text.secondary">
                                                    {formatarTimestamp(alerta.timestamp)}
                                                </Typography>
                                                {alerta.acao && (
                                                    <Chip
                                                        label={alerta.acao}
                                                        size="small"
                                                        variant="outlined"
                                                        color="primary"
                                                    />
                                                )}
                                            </Box>
                                        </Box>
                                    }
                                />
                            </ListItem>
                        ))}
                    </List>

                    {/* Materiais Críticos */}
                    {materiaisCriticos.length > 0 && (
                        <Box sx={{ mt: 2 }}>
                            <Typography variant="subtitle2" sx={{ mb: 1, display: 'flex', alignItems: 'center' }}>
                                <Nature sx={{ mr: 1, fontSize: 16 }} />
                                Materiais Críticos
                            </Typography>
                            <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1 }}>
                                {materiaisCriticos.map((material) => (
                                    <Chip
                                        key={material}
                                        label={material}
                                        size="small"
                                        color="error"
                                        variant="outlined"
                                    />
                                ))}
                            </Box>
                        </Box>
                    )}

                    {/* Alertas de Sustentabilidade */}
                    <Alert severity="info" sx={{ mt: 2 }}>
                        <Typography variant="body2">
                            <strong>Dica Sustentável:</strong> Separe corretamente os materiais para maximizar a reciclagem e reduzir o impacto ambiental.
                        </Typography>
                    </Alert>
                </Collapse>
            </CardContent>
        </Card>
    );
};

export default AlertasColeta; 