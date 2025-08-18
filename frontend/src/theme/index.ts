import { createTheme } from '@mui/material/styles';

export const darkTheme = createTheme({
    palette: {
        mode: 'dark',
        primary: {
            main: '#00c853',
            light: '#5efc82',
            dark: '#009624',
            contrastText: '#000000',
        },
        secondary: {
            main: '#2196f3',
            light: '#6ec6ff',
            dark: '#0069c0',
            contrastText: '#ffffff',
        },
        background: {
            default: '#0a0a0a',
            paper: '#1a1a1a',
        },
        text: {
            primary: '#ffffff',
            secondary: '#b0b0b0',
        },
        success: {
            main: '#4caf50',
            light: '#81c784',
            dark: '#388e3c',
        },
        warning: {
            main: '#ff9800',
            light: '#ffb74d',
            dark: '#f57c00',
        },
        error: {
            main: '#f44336',
            light: '#e57373',
            dark: '#d32f2f',
        },
        info: {
            main: '#2196f3',
            light: '#64b5f6',
            dark: '#1976d2',
        },
    },
    typography: {
        fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
        h1: {
            fontSize: '2.5rem',
            fontWeight: 600,
            color: '#ffffff',
        },
        h2: {
            fontSize: '2rem',
            fontWeight: 600,
            color: '#ffffff',
        },
        h3: {
            fontSize: '1.75rem',
            fontWeight: 500,
            color: '#ffffff',
        },
        h4: {
            fontSize: '1.5rem',
            fontWeight: 500,
            color: '#ffffff',
        },
        h5: {
            fontSize: '1.25rem',
            fontWeight: 500,
            color: '#ffffff',
        },
        h6: {
            fontSize: '1rem',
            fontWeight: 500,
            color: '#ffffff',
        },
        body1: {
            fontSize: '1rem',
            color: '#b0b0b0',
        },
        body2: {
            fontSize: '0.875rem',
            color: '#b0b0b0',
        },
    },
    shape: {
        borderRadius: 12,
    },
    components: {
        MuiAppBar: {
            styleOverrides: {
                root: {
                    backgroundColor: '#1a1a1a',
                    boxShadow: '0 2px 8px rgba(0,0,0,0.3)',
                },
            },
        },
        MuiDrawer: {
            styleOverrides: {
                paper: {
                    backgroundColor: '#1a1a1a',
                    borderRight: '1px solid #333',
                },
            },
        },
        MuiCard: {
            styleOverrides: {
                root: {
                    backgroundColor: '#2a2a2a',
                    border: '1px solid #333',
                    boxShadow: '0 4px 12px rgba(0,0,0,0.3)',
                },
            },
        },
        MuiPaper: {
            styleOverrides: {
                root: {
                    backgroundColor: '#2a2a2a',
                    border: '1px solid #333',
                },
            },
        },
        MuiButton: {
            styleOverrides: {
                root: {
                    textTransform: 'none',
                    borderRadius: 8,
                    fontWeight: 500,
                },
                contained: {
                    boxShadow: '0 2px 8px rgba(0,0,0,0.2)',
                },
            },
        },
        MuiTextField: {
            styleOverrides: {
                root: {
                    '& .MuiOutlinedInput-root': {
                        backgroundColor: '#3a3a3a',
                        '& fieldset': {
                            borderColor: '#555',
                        },
                        '&:hover fieldset': {
                            borderColor: '#777',
                        },
                        '&.Mui-focused fieldset': {
                            borderColor: '#00c853',
                        },
                    },
                },
            },
        },
        MuiChip: {
            styleOverrides: {
                root: {
                    borderRadius: 16,
                },
            },
        },
    },
}); 