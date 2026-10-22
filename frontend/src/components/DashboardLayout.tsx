import { Outlet } from 'react-router-dom';
import Sidebar from './Sidebar';

const DashboardLayout = () => {
  return (
    <div style={{ display: 'flex', height: '100vh', backgroundColor: 'var(--bg-color)' }}>
      <Sidebar />

      <div style={{ flex: 1, display: 'flex', flexDirection: 'column', overflow: 'hidden' }}>
        
        {/* Glassmorphism Topbar */}
        <header style={{ 
          height: '70px', 
          backgroundColor: 'rgba(255, 255, 255, 0.8)', 
          backdropFilter: 'blur(10px)',
          borderBottom: '1px solid rgba(0,0,0,0.05)', 
          display: 'flex', 
          alignItems: 'center', 
          justifyContent: 'space-between',
          padding: '0 32px',
          zIndex: 5
        }}>
          <h3 style={{ margin: 0, color: 'var(--text-main)', fontWeight: 600, fontSize: '1.1rem' }}>
            Overview
          </h3>
          <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
             <div style={{ width: '36px', height: '36px', backgroundColor: '#E0E7FF', borderRadius: '50%', display: 'flex', alignItems: 'center', justifyContent: 'center', color: 'var(--primary)', fontWeight: 'bold' }}>
               A
             </div>
          </div>
        </header>

        {/* Animated Content Area */}
        <main style={{ padding: '32px', flex: 1, overflowY: 'auto' }}>
          <div className="animate-fade-in">
            <Outlet /> 
          </div>
        </main>
        
      </div>
    </div>
  );
};

export default DashboardLayout;