import React from 'react';
import {
    Box,
    Card,
    CardContent,
    Typography,
    Grid,
    Chip,
    LinearProgress,
    IconButton,
    Tooltip,
} from '@mui/material';
import {
    TrendingUp,
    LocalShipping,
    Recycling,
    Visibility,
    VisibilityOff,
} from '@mui/icons-material';
import Nature from '@mui/icons-material/Nature';

interface SustentabilidadeStatsProps {
    totalColetado: number;
    totalReciclado: number;
    economiaCO2: number;
    economiaAgua: number;
    economiaEnergia: number;
    materiaisReciclados: {
        [key: string]: number;
    };
}

const SustentabilidadeStats: React.FC<SustentabilidadeStatsProps> = ({
    totalColetado,
    totalReciclado,
    economiaCO2,
    economiaAgua,
    economiaEnergia,
    materiaisReciclados,
}) => {
    const [showDetails, setShowDetails] = React.useState(true);

    const calcularPercentualReciclagem = () => {
        return totalColetado > 0 ? (totalReciclado / totalColetado) * 100 : 0;
    };

    const formatarValor = (valor: number) => {
        return valor.toLocaleString('pt-BR', {
            minimumFractionDigits: 1,
            maximumFractionDigits: 1,
        });
    };

    const getMaterialColor = (material: string) => {
        const colors: { [key: string]: string } = {
            'Papel e Papelão': '#4CAF50',
            'Plástico': '#2196F3',
            'Vidro': '#FF9800',
            'Metal': '#9C27B0',
            'Eletrônicos': '#F44336',
            'Óleo de Cozinha': '#795548',
            'Têxtil': '#607D8B',
        };
        return colors[material] || '#757575';
    };

    return (
        <Card sx={{ height: '100%' }}>
            <CardContent>
                <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', mb: 2 }}>
                    <Typography variant="h6" sx={{ display: 'flex', alignItems: 'center' }}>
                        <Nature sx={{ mr: 1, color: 'success.main' }} />
                        Impacto Sustentável
                    </Typography>
                    <Tooltip title={showDetails ? 'Ocultar detalhes' : 'Mostrar detalhes'}>
                        <IconButton
                            size="small"
                            onClick={() => setShowDetails(!showDetails)}
                        >
                            {showDetails ? <VisibilityOff /> : <Visibility />}
                        </IconButton>
                    </Tooltip>
                </Box>

                {/* Estatísticas Principais */}
                <Grid container spacing={2} sx={{ mb: 2 }}>
                    <Grid item xs={6}>
                        <Box sx={{ textAlign: 'center' }}>
                            <Typography variant="h4" color="primary">
                                {formatarValor(totalColetado)}kg
                            </Typography>
                            <Typography variant="caption" color="text.secondary">
                                Total Coletado
                            </Typography>
                        </Box>
                    </Grid>
                    <Grid item xs={6}>
                        <Box sx={{ textAlign: 'center' }}>
                            <Typography variant="h4" color="success.main">
                                {formatarValor(totalReciclado)}kg
                            </Typography>
                            <Typography variant="caption" color="text.secondary">
                                Total Reciclado
                            </Typography>
                        </Box>
                    </Grid>
                </Grid>

                {/* Barra de Progresso */}
                <Box sx={{ mb: 2 }}>
                    <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                        <Typography variant="body2">Taxa de Reciclagem</Typography>
                        <Typography variant="body2" color="primary">
                            {calcularPercentualReciclagem().toFixed(1)}%
                        </Typography>
                    </Box>
                    <LinearProgress
                        variant="determinate"
                        value={calcularPercentualReciclagem()}
                        sx={{ height: 8, borderRadius: 4 }}
                    />
                </Box>

                {showDetails && (
                    <>
                        {/* Impactos Ambientais */}
                        <Typography variant="subtitle2" sx={{ mb: 1, mt: 2 }}>
                            Impactos Ambientais
                        </Typography>
                        <Grid container spacing={1} sx={{ mb: 2 }}>
                            <Grid item xs={4}>
                                <Box sx={{ textAlign: 'center' }}>
                                    <Typography variant="h6" color="success.main">
                                        {formatarValor(economiaCO2)}kg
                                    </Typography>
                                    <Typography variant="caption" color="text.secondary">
                                        CO₂ Economizado
                                    </Typography>
                                </Box>
                            </Grid>
                            <Grid item xs={4}>
                                <Box sx={{ textAlign: 'center' }}>
                                    <Typography variant="h6" color="info.main">
                                        {formatarValor(economiaAgua)}L
                                    </Typography>
                                    <Typography variant="caption" color="text.secondary">
                                        Água Economizada
                                    </Typography>
                                </Box>
                            </Grid>
                            <Grid item xs={4}>
                                <Box sx={{ textAlign: 'center' }}>
                                    <Typography variant="h6" color="warning.main">
                                        {formatarValor(economiaEnergia)}kWh
                                    </Typography>
                                    <Typography variant="caption" color="text.secondary">
                                        Energia Economizada
                                    </Typography>
                                </Box>
                            </Grid>
                        </Grid>

                        {/* Materiais por Categoria */}
                        <Typography variant="subtitle2" sx={{ mb: 1 }}>
                            Materiais Reciclados
                        </Typography>
                        <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1 }}>
                            {Object.entries(materiaisReciclados).map(([material, quantidade]) => (
                                <Chip
                                    key={material}
                                    label={`${material}: ${formatarValor(quantidade)}kg`}
                                    size="small"
                                    sx={{
                                        backgroundColor: getMaterialColor(material),
                                        color: 'white',
                                        fontWeight: 'bold',
                                    }}
                                />
                            ))}
                        </Box>
                    </>
                )}

                {/* Indicadores de Sustentabilidade */}
                <Box sx={{ mt: 2, p: 1, backgroundColor: 'success.light', borderRadius: 1 }}>
                    <Typography variant="body2" color="success.dark" sx={{ display: 'flex', alignItems: 'center' }}>
                        <TrendingUp sx={{ mr: 1, fontSize: 16 }} />
                        Meta de Sustentabilidade: 85% de reciclagem
                    </Typography>
                </Box>
            </CardContent>
        </Card>
    );
};

export default SustentabilidadeStats; 