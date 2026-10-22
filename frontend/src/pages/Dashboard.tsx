const Dashboard = () => {
  return (
    <div>
      <div style={{ marginBottom: '32px' }}>
        <h1 style={{ fontSize: '2rem', color: 'var(--text-main)', marginBottom: '8px' }}>Welcome back, Rahul! 👋</h1>
        <p style={{ color: 'var(--text-muted)' }}>Here is what's happening with FlowPilot today.</p>
      </div>

      {/* Stats Grid */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(240px, 1fr))', gap: '24px' }}>
        
        {/* Card 1 */}
        <div style={{ 
          backgroundColor: 'white', 
          padding: '24px', 
          borderRadius: '16px', 
          boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.05)', 
          border: '1px solid rgba(0,0,0,0.05)',
          transition: 'transform 0.2s ease, boxShadow 0.2s ease',
          cursor: 'pointer'
        }}
        onMouseEnter={(e) => { e.currentTarget.style.transform = 'translateY(-4px)'; e.currentTarget.style.boxShadow = '0 10px 15px -3px rgba(0, 0, 0, 0.1)'; }}
        onMouseLeave={(e) => { e.currentTarget.style.transform = 'translateY(0)'; e.currentTarget.style.boxShadow = '0 4px 6px -1px rgba(0, 0, 0, 0.05)'; }}
        >
          <h3 style={{ color: 'var(--text-muted)', fontSize: '0.9rem', fontWeight: 500 }}>Total Enquiries</h3>
          <p style={{ fontSize: '2rem', fontWeight: 700, color: 'var(--primary)', marginTop: '8px' }}>24</p>
        </div>

        {/* Card 2 */}
        <div style={{ 
          backgroundColor: 'white', padding: '24px', borderRadius: '16px', boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.05)', border: '1px solid rgba(0,0,0,0.05)', transition: 'all 0.2s ease', cursor: 'pointer'
        }}
        onMouseEnter={(e) => { e.currentTarget.style.transform = 'translateY(-4px)'; e.currentTarget.style.boxShadow = '0 10px 15px -3px rgba(0, 0, 0, 0.1)'; }}
        onMouseLeave={(e) => { e.currentTarget.style.transform = 'translateY(0)'; e.currentTarget.style.boxShadow = '0 4px 6px -1px rgba(0, 0, 0, 0.05)'; }}
        >
          <h3 style={{ color: 'var(--text-muted)', fontSize: '0.9rem', fontWeight: 500 }}>Quotes Generated</h3>
          <p style={{ fontSize: '2rem', fontWeight: 700, color: '#10B981', marginTop: '8px' }}>18</p>
        </div>

        {/* Card 3 */}
        <div style={{ 
          backgroundColor: 'white', padding: '24px', borderRadius: '16px', boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.05)', border: '1px solid rgba(0,0,0,0.05)', transition: 'all 0.2s ease', cursor: 'pointer'
        }}
        onMouseEnter={(e) => { e.currentTarget.style.transform = 'translateY(-4px)'; e.currentTarget.style.boxShadow = '0 10px 15px -3px rgba(0, 0, 0, 0.1)'; }}
        onMouseLeave={(e) => { e.currentTarget.style.transform = 'translateY(0)'; e.currentTarget.style.boxShadow = '0 4px 6px -1px rgba(0, 0, 0, 0.05)'; }}
        >
          <h3 style={{ color: 'var(--text-muted)', fontSize: '0.9rem', fontWeight: 500 }}>Active Bookings</h3>
          <p style={{ fontSize: '2rem', fontWeight: 700, color: '#F59E0B', marginTop: '8px' }}>7</p>
        </div>

      </div>
    </div>
  );
};

export default Dashboard;