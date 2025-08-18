import React, { useState } from 'react';
import {
  Box,
  Typography,
  Card,
  CardContent,
  Grid,
  TextField,
  Button,
  Switch,
  FormControlLabel,
  Divider,
  Alert,
  Chip,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
} from '@mui/material';
import {
  Settings,
  Save,
  Refresh,
  Security,
  Notifications,
  Storage,
  Api,
} from '@mui/icons-material';
import { useSnackbar } from 'notistack';

const ConfiguracoesPage: React.FC = () => {
  const [configuracoes, setConfiguracoes] = useState({
    // Configurações gerais
    nomeSistema: 'Sistema de Roteamento Programado de Coletas',
    versao: '1.0.0',
    ambiente: 'Desenvolvimento',

    // Configurações de API
    apiUrl: 'http://localhost:8081/api/v1',
    timeout: '30',

    // Configurações de roteamento
    maxSearchRadius: '5000',
    maxRoutingTime: '30',

    // Configurações de notificação
    notificationExpiration: '30',
    maxRetryAttempts: '3',

    // Configurações de segurança
    enableSecurity: true,
    enableCors: true,
    enableCsrf: false,

    // Configurações de cache
    enableCache: true,
    cacheExpiration: '3600',

    // Configurações de logging
    enableLogging: true,
    logLevel: 'INFO',
  });

  const { enqueueSnackbar } = useSnackbar();

  const handleConfigChange = (key: string, value: string | boolean) => {
    setConfiguracoes(prev => ({
      ...prev,
      [key]: value,
    }));
  };

  const handleSave = () => {
    // Aqui você salvaria as configurações no backend
    enqueueSnackbar('Configurações salvas com sucesso!', { variant: 'success' });
  };

  const handleReset = () => {
    setConfiguracoes({
      nomeSistema: 'Sistema de Roteamento Programado de Coletas',
      versao: '1.0.0',
      ambiente: 'Desenvolvimento',
      apiUrl: 'http://localhost:8081/api/v1',
      timeout: '30',
      maxSearchRadius: '5000',
      maxRoutingTime: '30',
      notificationExpiration: '30',
      maxRetryAttempts: '3',
      enableSecurity: true,
      enableCors: true,
      enableCsrf: false,
      enableCache: true,
      cacheExpiration: '3600',
      enableLogging: true,
      logLevel: 'INFO',
    });
    enqueueSnackbar('Configurações resetadas!', { variant: 'info' });
  };

  return (
    <Box sx={{ p: 3 }}>
      <Box sx={{ display: 'flex', alignItems: 'center', mb: 3 }}>
        <Settings sx={{ fontSize: 40, color: 'primary.main', mr: 2 }} />
        <Typography variant="h4">
          Configurações do Sistema
        </Typography>
      </Box>

      <Grid container spacing={3}>
        {/* Configurações Gerais */}
        <Grid item xs={12} md={6}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>
                Configurações Gerais
              </Typography>
              <Grid container spacing={2}>
                <Grid item xs={12}>
                  <TextField
                    fullWidth
                    label="Nome do Sistema"
                    value={configuracoes.nomeSistema}
                    onChange={(e) => handleConfigChange('nomeSistema', e.target.value)}
                    margin="normal"
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <TextField
                    fullWidth
                    label="Versão"
                    value={configuracoes.versao}
                    onChange={(e) => handleConfigChange('versao', e.target.value)}
                    margin="normal"
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <TextField
                    fullWidth
                    label="Ambiente"
                    value={configuracoes.ambiente}
                    onChange={(e) => handleConfigChange('ambiente', e.target.value)}
                    margin="normal"
                  />
                </Grid>
              </Grid>
            </CardContent>
          </Card>
        </Grid>

        {/* Configurações de API */}
        <Grid item xs={12} md={6}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>
                Configurações de API
              </Typography>
              <Grid container spacing={2}>
                <Grid item xs={12}>
                  <TextField
                    fullWidth
                    label="URL da API"
                    value={configuracoes.apiUrl}
                    onChange={(e) => handleConfigChange('apiUrl', e.target.value)}
                    margin="normal"
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <TextField
                    fullWidth
                    label="Timeout (segundos)"
                    type="number"
                    value={configuracoes.timeout}
                    onChange={(e) => handleConfigChange('timeout', e.target.value)}
                    margin="normal"
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <Chip
                    label="API Online"
                    color="success"
                    size="small"
                    sx={{ mt: 2 }}
                  />
                </Grid>
              </Grid>
            </CardContent>
          </Card>
        </Grid>

        {/* Configurações de Roteamento */}
        <Grid item xs={12} md={6}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>
                Configurações de Roteamento
              </Typography>
              <Grid container spacing={2}>
                <Grid item xs={12} sm={6}>
                  <TextField
                    fullWidth
                    label="Raio Máximo de Busca (m)"
                    type="number"
                    value={configuracoes.maxSearchRadius}
                    onChange={(e) => handleConfigChange('maxSearchRadius', e.target.value)}
                    margin="normal"
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <TextField
                    fullWidth
                    label="Tempo Máximo de Roteamento (s)"
                    type="number"
                    value={configuracoes.maxRoutingTime}
                    onChange={(e) => handleConfigChange('maxRoutingTime', e.target.value)}
                    margin="normal"
                  />
                </Grid>
              </Grid>
            </CardContent>
          </Card>
        </Grid>

        {/* Configurações de Notificação */}
        <Grid item xs={12} md={6}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>
                Configurações de Notificação
              </Typography>
              <Grid container spacing={2}>
                <Grid item xs={12} sm={6}>
                  <TextField
                    fullWidth
                    label="Expiração (minutos)"
                    type="number"
                    value={configuracoes.notificationExpiration}
                    onChange={(e) => handleConfigChange('notificationExpiration', e.target.value)}
                    margin="normal"
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <TextField
                    fullWidth
                    label="Tentativas Máximas"
                    type="number"
                    value={configuracoes.maxRetryAttempts}
                    onChange={(e) => handleConfigChange('maxRetryAttempts', e.target.value)}
                    margin="normal"
                  />
                </Grid>
              </Grid>
            </CardContent>
          </Card>
        </Grid>

        {/* Configurações de Segurança */}
        <Grid item xs={12} md={6}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>
                Configurações de Segurança
              </Typography>
              <Grid container spacing={2}>
                <Grid item xs={12}>
                  <FormControlLabel
                    control={
                      <Switch
                        checked={configuracoes.enableSecurity}
                        onChange={(e) => handleConfigChange('enableSecurity', e.target.checked)}
                      />
                    }
                    label="Habilitar Segurança"
                  />
                </Grid>
                <Grid item xs={12}>
                  <FormControlLabel
                    control={
                      <Switch
                        checked={configuracoes.enableCors}
                        onChange={(e) => handleConfigChange('enableCors', e.target.checked)}
                      />
                    }
                    label="Habilitar CORS"
                  />
                </Grid>
                <Grid item xs={12}>
                  <FormControlLabel
                    control={
                      <Switch
                        checked={configuracoes.enableCsrf}
                        onChange={(e) => handleConfigChange('enableCsrf', e.target.checked)}
                      />
                    }
                    label="Habilitar CSRF"
                  />
                </Grid>
              </Grid>
            </CardContent>
          </Card>
        </Grid>

        {/* Configurações de Cache */}
        <Grid item xs={12} md={6}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>
                Configurações de Cache
              </Typography>
              <Grid container spacing={2}>
                <Grid item xs={12}>
                  <FormControlLabel
                    control={
                      <Switch
                        checked={configuracoes.enableCache}
                        onChange={(e) => handleConfigChange('enableCache', e.target.checked)}
                      />
                    }
                    label="Habilitar Cache"
                  />
                </Grid>
                <Grid item xs={12}>
                  <TextField
                    fullWidth
                    label="Expiração do Cache (segundos)"
                    type="number"
                    value={configuracoes.cacheExpiration}
                    onChange={(e) => handleConfigChange('cacheExpiration', e.target.value)}
                    margin="normal"
                  />
                </Grid>
              </Grid>
            </CardContent>
          </Card>
        </Grid>

        {/* Configurações de Logging */}
        <Grid item xs={12}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>
                Configurações de Logging
              </Typography>
              <Grid container spacing={2}>
                <Grid item xs={12} sm={6}>
                  <FormControlLabel
                    control={
                      <Switch
                        checked={configuracoes.enableLogging}
                        onChange={(e) => handleConfigChange('enableLogging', e.target.checked)}
                      />
                    }
                    label="Habilitar Logging"
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <TextField
                    fullWidth
                    label="Nível de Log"
                    select
                    value={configuracoes.logLevel}
                    onChange={(e) => handleConfigChange('logLevel', e.target.value)}
                    margin="normal"
                  >
                    <MenuItem value="DEBUG">DEBUG</MenuItem>
                    <MenuItem value="INFO">INFO</MenuItem>
                    <MenuItem value="WARN">WARN</MenuItem>
                    <MenuItem value="ERROR">ERROR</MenuItem>
                  </TextField>
                </Grid>
              </Grid>
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      {/* Ações */}
      <Box sx={{ mt: 3, display: 'flex', gap: 2, justifyContent: 'flex-end' }}>
        <Button
          variant="outlined"
          startIcon={<Refresh />}
          onClick={handleReset}
        >
          Resetar
        </Button>
        <Button
          variant="contained"
          startIcon={<Save />}
          onClick={handleSave}
        >
          Salvar Configurações
        </Button>
      </Box>

      {/* Status do Sistema */}
      <Box sx={{ mt: 3 }}>
        <Alert severity="info">
          <Typography variant="body2">
            <strong>Status do Sistema:</strong> Todas as configurações estão funcionando corretamente.
            O sistema está operacional e pronto para uso.
          </Typography>
        </Alert>
      </Box>
    </Box>
  );
};

export default ConfiguracoesPage; 