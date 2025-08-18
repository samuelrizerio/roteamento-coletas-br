import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { ThemeProvider, CssBaseline } from '@mui/material';
import { QueryClient, QueryClientProvider } from 'react-query';
import { SnackbarProvider } from 'notistack';
import { darkTheme } from './theme';
import AppLayout from './components/Layout/AppLayout';
import DashboardComponent from './components/Dashboard/DashboardComponent';
import UsuariosPage from './pages/UsuariosPage';
import MateriaisPage from './pages/MateriaisPage';
import ColetasPage from './pages/ColetasPage';
import RotasPage from './pages/RotasPage';
import MapaPage from './pages/MapaPage';
import ConfiguracoesPage from './pages/ConfiguracoesPage';

// Criar cliente do React Query
const queryClient = new QueryClient({
    defaultOptions: {
        queries: {
            retry: 1,
            refetchOnWindowFocus: false,
        },
    },
});

const App: React.FC = () => {
    return (
        <QueryClientProvider client={queryClient}>
            <ThemeProvider theme={darkTheme}>
                <CssBaseline />
                <SnackbarProvider
                    maxSnack={3}
                    anchorOrigin={{
                        vertical: 'top',
                        horizontal: 'right',
                    }}
                >
                    <Router>
                        <AppLayout>
                            <Routes>
                                <Route path="/" element={<DashboardComponent />} />
                                <Route path="/usuarios" element={<UsuariosPage />} />
                                <Route path="/materiais" element={<MateriaisPage />} />
                                <Route path="/coletas" element={<ColetasPage />} />
                                <Route path="/rotas" element={<RotasPage />} />
                                <Route path="/mapa" element={<MapaPage />} />
                                <Route path="/configuracoes" element={<ConfiguracoesPage />} />
                            </Routes>
                        </AppLayout>
                    </Router>
                </SnackbarProvider>
            </ThemeProvider>
        </QueryClientProvider>
    );
};

export default App; 