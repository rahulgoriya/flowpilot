import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { api } from '../api/client';
import { useAuth } from '../hooks/useAuth';
import { LogIn, Loader2, Sparkles } from 'lucide-react';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');
  
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      setIsLoading(true);
      setError('');
      
      // Use the api client (which handles the fetch correctly)
      const data = await api.login({ email, password });
      
      // Save the token using AuthContext (this sets 'flowpilot_token')
      login(data.token);
      
      // Redirect to the main Dashboard route
      navigate('/');
    } catch (err: any) {
      setError(err.message || 'Invalid credentials');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div style={{ display: 'flex', height: '100vh', justifyContent: 'center', alignItems: 'center', backgroundColor: '#F3F4F6' }}>
      <div style={{ 
        backgroundColor: 'white', padding: '40px', borderRadius: '16px', 
        boxShadow: '0 10px 25px rgba(0,0,0,0.05)', width: '100%', maxWidth: '400px' 
      }}>
        
        {/* Logo & Header */}
        <div style={{ textAlign: 'center', marginBottom: '32px' }}>
          <div style={{ 
            width: '48px', height: '48px', 
            background: 'linear-gradient(135deg, #4F46E5, #9333EA)', 
            borderRadius: '12px', display: 'flex', alignItems: 'center', 
            justifyContent: 'center', margin: '0 auto 16px' 
          }}>
            <Sparkles size={24} color="white" />
          </div>
          <h1 style={{ fontSize: '1.5rem', fontWeight: 700, color: '#1F2937' }}>Welcome Back</h1>
          <p style={{ color: '#6B7280', fontSize: '0.9rem', marginTop: '4px' }}>Sign in to FlowPilot</p>
        </div>

        {/* Error Message */}
        {error && (
          <div style={{ 
            backgroundColor: '#FEE2E2', color: '#B91C1C', padding: '12px', 
            borderRadius: '8px', marginBottom: '20px', fontSize: '0.85rem', textAlign: 'center' 
          }}>
            {error}
          </div>
        )}

        {/* Form */}
        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
          <div>
            <label style={{ fontSize: '0.85rem', fontWeight: 500, color: '#374151' }}>Email</label>
            <input 
              type="email" 
              required
              value={email} 
              onChange={e => setEmail(e.target.value)}
              placeholder="admin@flowpilot.com"
              style={{ 
                width: '100%', padding: '10px', marginTop: '6px', 
                border: '1px solid #D1D5DB', borderRadius: '8px', outline: 'none',
                fontSize: '0.95rem'
              }} 
            />
          </div>
          
          <div>
            <label style={{ fontSize: '0.85rem', fontWeight: 500, color: '#374151' }}>Password</label>
            <input 
              type="password" 
              required
              value={password} 
              onChange={e => setPassword(e.target.value)}
              placeholder="••••••••"
              style={{ 
                width: '100%', padding: '10px', marginTop: '6px', 
                border: '1px solid #D1D5DB', borderRadius: '8px', outline: 'none',
                fontSize: '0.95rem'
              }} 
            />
          </div>

          <button 
            type="submit" 
            disabled={isLoading}
            style={{ 
              marginTop: '8px', padding: '12px', backgroundColor: '#4F46E5', color: 'white', 
              border: 'none', borderRadius: '8px', fontWeight: 600, 
              cursor: isLoading ? 'not-allowed' : 'pointer',
              display: 'flex', justifyContent: 'center', alignItems: 'center', gap: '8px',
              transition: 'background-color 0.2s'
            }}
          >
            {isLoading ? <Loader2 size={18} className="animate-spin" /> : <LogIn size={18} />}
            {isLoading ? 'Signing in...' : 'Sign In'}
          </button>
        </form>
      </div>
    </div>
  );
};

export default Login;