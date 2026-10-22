import { Link, useLocation } from 'react-router-dom';
import { LayoutDashboard, Users, Mail, Calendar } from 'lucide-react';

const Sidebar = () => {
  const location = useLocation();

  const navItems = [
    { path: '/', label: 'Dashboard', icon: <LayoutDashboard size={20} /> },
    { path: '/customers', label: 'Customers', icon: <Users size={20} /> },
    { path: '/enquiries', label: 'Enquiries', icon: <Mail size={20} /> },
    { path: '/bookings', label: 'Bookings', icon: <Calendar size={20} /> },
  ];

  return (
    <div style={{ 
      width: '260px', 
      backgroundColor: 'var(--sidebar-bg)', 
      color: 'white', 
      padding: '24px 16px', 
      height: '100vh',
      display: 'flex',
      flexDirection: 'column',
      boxShadow: '4px 0 24px rgba(0,0,0,0.1)',
      zIndex: 10
    }}>
      
      {/* Logo Area */}
      <div style={{ display: 'flex', alignItems: 'center', gap: '10px', marginBottom: '40px', paddingLeft: '10px' }}>
        <div style={{ 
          width: '32px', height: '32px', 
          background: 'linear-gradient(135deg, #4F46E5, #9333EA)', 
          borderRadius: '8px',
          display: 'flex', alignItems: 'center', justifyContent: 'center',
          fontSize: '18px'
        }}>🧹</div>
        <h2 style={{ fontSize: '1.25rem', fontWeight: 700, letterSpacing: '-0.5px' }}>FlowPilot</h2>
      </div>
      
      <nav style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
        {navItems.map((item) => {
          const isActive = location.pathname === item.path;
          
          return (
            <Link 
              key={item.path} 
              to={item.path} 
              style={{
                display: 'flex',
                alignItems: 'center',
                gap: '12px',
                padding: '12px 16px',
                color: isActive ? 'white' : '#9CA3AF',
                textDecoration: 'none',
                borderRadius: '12px',
                backgroundColor: isActive ? 'var(--primary)' : 'transparent',
                transition: 'all 0.3s ease',
                fontWeight: isActive ? 600 : 500,
                boxShadow: isActive ? '0 4px 14px rgba(79, 70, 229, 0.4)' : 'none',
                transform: isActive ? 'translateX(4px)' : 'none'
              }}
              // Add hover effect via JS (React state or simple CSS class if preferred)
              onMouseEnter={(e) => {
                if (!isActive) {
                  e.currentTarget.style.backgroundColor = 'rgba(255,255,255,0.05)';
                  e.currentTarget.style.color = 'white';
                }
              }}
              onMouseLeave={(e) => {
                if (!isActive) {
                  e.currentTarget.style.backgroundColor = 'transparent';
                  e.currentTarget.style.color = '#9CA3AF';
                }
              }}
            >
              {item.icon}
              {item.label}
            </Link>
          );
        })}
      </nav>
    </div>
  );
};

export default Sidebar;