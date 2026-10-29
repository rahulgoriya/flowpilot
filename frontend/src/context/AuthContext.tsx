import { createContext, useState, useEffect, type ReactNode } from 'react';

interface AuthContextType {
  token: string | null;
  login: (token: string) => void;
  logout: () => void;
  isAuthenticated: boolean;
}

export const AuthContext = createContext<AuthContextType>({
  token: null,
  login: () => {},
  logout: () => {},
  isAuthenticated: false,
});

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [token, setToken] = useState<string | null>(null);

  // Load token from localStorage on first load
  useEffect(() => {
    const storedToken = localStorage.getItem('flowpilot_token');
    if (storedToken) {
      setToken(storedToken);
    }
  }, []);

  const login = (newToken: string) => {
    localStorage.setItem('flowpilot_token', newToken);
    setToken(newToken);
  };

  const logout = () => {
    localStorage.removeItem('flowpilot_token');
    setToken(null);
  };

  return (
    <AuthContext.Provider value={{ token, login, logout, isAuthenticated: !!token }}>
      {children}
    </AuthContext.Provider>
  );
};