import { useState, useEffect } from 'react';
import { api } from '../api/client';
import type { Customer } from '../types';
import { UserPlus, Loader2 } from 'lucide-react';

const Customers = () => {
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [error, setError] = useState<string>('');
  
  // Form State
  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState({ name: '', email: '', phone: '' });

  // 1. Fetch data on mount
  useEffect(() => {
    fetchCustomers();
  }, []);

  const fetchCustomers = async () => {
    try {
      setIsLoading(true);
      const data = await api.getCustomers();
      setCustomers(data);
    } catch (err: any) {
      setError(err.message);
    } finally {
      setIsLoading(false);
    }
  };

  // 2. Handle Form Submit
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.createCustomer(formData);
      setFormData({ name: '', email: '', phone: '' }); // Reset form
      setShowForm(false); // Hide form
      fetchCustomers(); // Refresh table
    } catch (err: any) {
      alert(err.message); // Show backend error (e.g., duplicate email)
    }
  };

  return (
    <div>
      {/* Header & Button */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px' }}>
        <h1 style={{ fontSize: '1.5rem', fontWeight: 600 }}>Customers</h1>
        <button 
          onClick={() => setShowForm(!showForm)}
          style={{ 
            display: 'flex', alignItems: 'center', gap: '8px', 
            padding: '10px 16px', backgroundColor: 'var(--primary)', 
            color: 'white', border: 'none', borderRadius: '8px', 
            cursor: 'pointer', fontWeight: 500, transition: 'all 0.2s' 
          }}
        >
          <UserPlus size={18} /> {showForm ? 'Cancel' : 'Add Customer'}
        </button>
      </div>

      {/* Add Customer Form */}
      {showForm && (
        <form onSubmit={handleSubmit} style={{ 
          backgroundColor: 'white', padding: '20px', borderRadius: '12px', 
          boxShadow: '0 4px 6px rgba(0,0,0,0.05)', marginBottom: '24px',
          display: 'flex', gap: '16px', alignItems: 'flex-end' 
        }}>
          <div style={{ flex: 1 }}>
            <label style={{ fontSize: '0.85rem', fontWeight: 500, color: 'var(--text-muted)' }}>Name</label>
            <input required type="text" value={formData.name} onChange={e => setFormData({...formData, name: e.target.value})}
              style={{ width: '100%', padding: '8px', marginTop: '4px', border: '1px solid #ddd', borderRadius: '6px' }} />
          </div>
          <div style={{ flex: 1 }}>
            <label style={{ fontSize: '0.85rem', fontWeight: 500, color: 'var(--text-muted)' }}>Email</label>
            <input required type="email" value={formData.email} onChange={e => setFormData({...formData, email: e.target.value})}
              style={{ width: '100%', padding: '8px', marginTop: '4px', border: '1px solid #ddd', borderRadius: '6px' }} />
          </div>
          <div style={{ flex: 1 }}>
            <label style={{ fontSize: '0.85rem', fontWeight: 500, color: 'var(--text-muted)' }}>Phone</label>
            <input required type="text" value={formData.phone} onChange={e => setFormData({...formData, phone: e.target.value})}
              style={{ width: '100%', padding: '8px', marginTop: '4px', border: '1px solid #ddd', borderRadius: '6px' }} />
          </div>
          <button type="submit" style={{ 
            padding: '9px 16px', backgroundColor: '#10B981', color: 'white', 
            border: 'none', borderRadius: '6px', cursor: 'pointer', fontWeight: 500, height: '38px'
          }}>
            Save
          </button>
        </form>
      )}

      {/* Data Table */}
      {isLoading ? (
        <div style={{ display: 'flex', justifyContent: 'center', marginTop: '50px', color: 'var(--text-muted)' }}>
          <Loader2 size={32} className="animate-spin" />
        </div>
      ) : error ? (
        <div style={{ padding: '20px', backgroundColor: '#fee2e2', color: '#b91c1c', borderRadius: '8px' }}>
          {error}
        </div>
      ) : (
        <div style={{ backgroundColor: 'white', borderRadius: '12px', boxShadow: '0 4px 6px rgba(0,0,0,0.05)', overflow: 'hidden' }}>
          <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left' }}>
            <thead style={{ backgroundColor: '#F9FAFB', borderBottom: '1px solid #E5E7EB' }}>
              <tr>
                <th style={{ padding: '16px', fontWeight: 600, color: 'var(--text-muted)', fontSize: '0.85rem' }}>ID</th>
                <th style={{ padding: '16px', fontWeight: 600, color: 'var(--text-muted)', fontSize: '0.85rem' }}>NAME</th>
                <th style={{ padding: '16px', fontWeight: 600, color: 'var(--text-muted)', fontSize: '0.85rem' }}>EMAIL</th>
                <th style={{ padding: '16px', fontWeight: 600, color: 'var(--text-muted)', fontSize: '0.85rem' }}>PHONE</th>
              </tr>
            </thead>
            <tbody>
              {customers.map((customer) => (
                <tr key={customer.id} style={{ borderBottom: '1px solid #F3F4F6', transition: 'background-color 0.2s' }}>
                  <td style={{ padding: '16px', color: 'var(--text-muted)' }}>#{customer.id}</td>
                  <td style={{ padding: '16px', fontWeight: 500 }}>{customer.name}</td>
                  <td style={{ padding: '16px', color: 'var(--text-muted)' }}>{customer.email}</td>
                  <td style={{ padding: '16px', color: 'var(--text-muted)' }}>{customer.phone}</td>
                </tr>
              ))}
            </tbody>
          </table>
          {customers.length === 0 && (
            <div style={{ padding: '40px', textAlign: 'center', color: 'var(--text-muted)' }}>
              No customers found. Click "Add Customer" to create one.
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default Customers;