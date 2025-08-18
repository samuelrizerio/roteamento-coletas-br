import React, { useState } from 'react';
import {
    Box,
    AppBar,
    Toolbar,
    Typography,
    IconButton,
    Drawer,
    List,
    ListItem,
    ListItemButton,
    ListItemIcon,
    ListItemText,
    Divider,
    useTheme,
    Avatar,
    Menu,
    MenuItem,
    Tooltip,
} from '@mui/material';
import {
    Menu as MenuIcon,
    Dashboard,
    People,
    Recycling,
    LocalShipping,
    Route,
    Map,
    Settings,
    AccountCircle,
    Notifications,
    Logout,
} from '@mui/icons-material';
import { useNavigate, useLocation } from 'react-router-dom';

interface AppLayoutProps {
    children: React.ReactNode;
}

const drawerWidth = 240;

const menuItems = [
    { text: 'Dashboard', icon: <Dashboard />, path: '/' },
    { text: 'Usuários', icon: <People />, path: '/usuarios' },
    { text: 'Materiais', icon: <Recycling />, path: '/materiais' },
    { text: 'Coletas', icon: <LocalShipping />, path: '/coletas' },
    { text: 'Rotas', icon: <Route />, path: '/rotas' },
    { text: 'Mapa', icon: <Map />, path: '/mapa' },
    { text: 'Configurações', icon: <Settings />, path: '/configuracoes' },
];

const AppLayout: React.FC<AppLayoutProps> = ({ children }) => {
    const [mobileOpen, setMobileOpen] = useState(false);
    const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
    const theme = useTheme();
    const navigate = useNavigate();
    const location = useLocation();

    const handleDrawerToggle = () => {
        setMobileOpen(!mobileOpen);
    };

    const handleMenuOpen = (event: React.MouseEvent<HTMLElement>) => {
        setAnchorEl(event.currentTarget);
    };

    const handleMenuClose = () => {
        setAnchorEl(null);
    };

    const handleNavigation = (path: string) => {
        navigate(path);
        setMobileOpen(false);
    };

    const drawer = (
        <Box>
            <Box sx={{ p: 2, textAlign: 'center' }}>
                <Typography variant="h1" color="primary">
                    SRPC
                </Typography>
            </Box>
            <Divider />
            <List>
                {menuItems.map((item) => (
                    <ListItem key={item.text} disablePadding>
                        <ListItemButton
                            onClick={() => handleNavigation(item.path)}
                            selected={location.pathname === item.path}
                            sx={{
                                '&.Mui-selected': {
                                    backgroundColor: 'primary.main',
                                    color: 'primary.contrastText',
                                    '&:hover': {
                                        backgroundColor: 'primary.dark',
                                    },
                                },
                            }}
                        >
                            <ListItemIcon sx={{ color: 'inherit' }}>
                                {item.icon}
                            </ListItemIcon>
                            <ListItemText primary={item.text} />
                        </ListItemButton>
                    </ListItem>
                ))}
            </List>
        </Box>
    );

    return (
        <Box sx={{ display: 'flex' }}>
            <AppBar
                position="fixed"
                sx={{
                    width: { sm: `calc(100% - ${drawerWidth}px)` },
                    ml: { sm: `${drawerWidth}px` },
                }}
            >
                <Toolbar>
                    <IconButton
                        color="inherit"
                        aria-label="open drawer"
                        edge="start"
                        onClick={handleDrawerToggle}
                        sx={{ mr: 2, display: { sm: 'none' } }}
                    >
                        <MenuIcon />
                    </IconButton>
                    <Typography variant="h6" noWrap component="div" sx={{ flexGrow: 1 }}>
                        Sistema de Roteamento Programado de Coletas
                    </Typography>

                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                        <Tooltip title="Notificações">
                            <IconButton color="inherit">
                                <Notifications />
                            </IconButton>
                        </Tooltip>

                        <Tooltip title="Perfil">
                            <IconButton
                                color="inherit"
                                onClick={handleMenuOpen}
                            >
                                <AccountCircle />
                            </IconButton>
                        </Tooltip>
                    </Box>

                    <Menu
                        anchorEl={anchorEl}
                        open={Boolean(anchorEl)}
                        onClose={handleMenuClose}
                        anchorOrigin={{
                            vertical: 'bottom',
                            horizontal: 'right',
                        }}
                        transformOrigin={{
                            vertical: 'top',
                            horizontal: 'right',
                        }}
                    >
                        <MenuItem onClick={handleMenuClose}>
                            <AccountCircle sx={{ mr: 1 }} />
                            Perfil
                        </MenuItem>
                        <MenuItem onClick={handleMenuClose}>
                            <Logout sx={{ mr: 1 }} />
                            Sair
                        </MenuItem>
                    </Menu>
                </Toolbar>
            </AppBar>

            <Box
                component="nav"
                sx={{ width: { sm: drawerWidth }, flexShrink: { sm: 0 } }}
            >
                <Drawer
                    variant="temporary"
                    open={mobileOpen}
                    onClose={handleDrawerToggle}
                    ModalProps={{
                        keepMounted: true,
                    }}
                    sx={{
                        display: { xs: 'block', sm: 'none' },
                        '& .MuiDrawer-paper': { boxSizing: 'border-box', width: drawerWidth },
                    }}
                >
                    {drawer}
                </Drawer>
                <Drawer
                    variant="permanent"
                    sx={{
                        display: { xs: 'none', sm: 'block' },
                        '& .MuiDrawer-paper': { boxSizing: 'border-box', width: drawerWidth },
                    }}
                    open
                >
                    {drawer}
                </Drawer>
            </Box>

            <Box
                component="main"
                sx={{
                    flexGrow: 1,
                    p: 3,
                    width: { sm: `calc(100% - ${drawerWidth}px)` },
                    mt: 8,
                }}
            >
                {children}
            </Box>
        </Box>
    );
};

export default AppLayout; 