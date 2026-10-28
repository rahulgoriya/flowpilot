import { useState, useEffect } from 'react';
import { api } from '../api/client';
import type { Booking } from '../types';
import { Calendar, Clock, Loader2, User } from 'lucide-react';

const Bookings = () => {
  const [bookings, setBookings] = useState<Booking[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchBookings();
  }, []);

  const fetchBookings = async () => {
    try {
      setIsLoading(true);
      const data = await api.getBookings();
      // Sort by scheduled date (newest first)
      const sorted = data.sort((a: Booking, b: Booking) => 
        new Date(b.scheduledDateTime).getTime() - new Date(a.scheduledDateTime).getTime()
      );
      setBookings(sorted);
    } catch (err: any) {
      setError(err.message);
    } finally {
      setIsLoading(false);
    }
  };

  // Helper to format date nicely
  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-IN', {
      weekday: 'short', year: 'numeric', month: 'short', day: 'numeric'
    });
  };

  const formatTime = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleTimeString('en-IN', {
      hour: '2-digit', minute: '2-digit'
    });
  };

  // Status Badge Colors
  const getStatusStyle = (status: string) => {
    switch (status) {
      case 'SCHEDULED': return { bg: '#d1fae5', text: '#065f46' };
      case 'COMPLETED': return { bg: '#e5e7eb', text: '#374151' };
      case 'CANCELLED': return { bg: '#fee2e2', text: '#991b1b' };
      default: return { bg: '#fef3c7', text: '#92400e' };
    }
  };

  return (
    <div>
      {/* Header */}
      <div style={{ marginBottom: '24px' }}>
        <h1 style={{ fontSize: '1.5rem', fontWeight: 600, display: 'flex', alignItems: 'center', gap: '8px' }}>
          <Calendar color="var(--primary)" /> Bookings
        </h1>
        <p style={{ color: 'var(--text-muted)' }}>Your confirmed appointments and schedule.</p>
      </div>

      {/* Loading & Error States */}
      {isLoading ? (
        <div style={{ display: 'flex', justifyContent: 'center', marginTop: '50px' }}>
          <Loader2 size={32} className="animate-spin" color="var(--primary)" />
        </div>
      ) : error ? (
        <div style={{ padding: '20px', backgroundColor: '#fee2e2', color: '#b91c1c', borderRadius: '8px' }}>
          {error}
        </div>
      ) : bookings.length === 0 ? (
        <div style={{ textAlign: 'center', padding: '60px', color: 'var(--text-muted)' }}>
          <Calendar size={48} style={{ margin: '0 auto 16px', opacity: 0.5 }} />
          <h3>No bookings yet</h3>
          <p>Accept a Quote to automatically generate a Booking.</p>
        </div>
      ) : (
        /* Bookings Grid */
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(320px, 1fr))', gap: '20px' }}>
          {bookings.map((booking) => {
            const statusStyle = getStatusStyle(booking.status);
            
            return (
              <div key={booking.bookingId} style={{ 
                backgroundColor: 'white', 
                borderRadius: '16px', 
                padding: '24px',
                boxShadow: '0 4px 6px -1px rgba(0,0,0,0.05)', 
                border: '1px solid #E5E7EB',
                borderLeft: '6px solid #4F46E5'
              }}>
                {/* Header: ID & Status */}
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
                  <span style={{ fontSize: '0.85rem', fontWeight: 600, color: '#6B7280' }}>
                    BOOKING #{booking.bookingId}
                  </span>
                  <span style={{ 
                    fontSize: '0.75rem', padding: '4px 10px', borderRadius: '12px',
                    backgroundColor: statusStyle.bg, color: statusStyle.text, fontWeight: 600
                  }}>
                    {booking.status}
                  </span>
                </div>

                {/* Customer Info */}
                <div style={{ display: 'flex', alignItems: 'center', gap: '12px', marginBottom: '20px' }}>
                  <div style={{ width: '40px', height: '40px', borderRadius: '50%', backgroundColor: '#EEF2FF', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                    <User size={20} color="#4F46E5" />
                  </div>
                  <div>
                    <h3 style={{ margin: 0, fontSize: '1.1rem', color: '#111827' }}>{booking.customerName}</h3>
                    <p style={{ margin: 0, fontSize: '0.85rem', color: '#6B7280' }}>Quote Ref: #{booking.quoteId}</p>
                  </div>
                </div>

                {/* Schedule Info */}
                <div style={{ 
                  backgroundColor: '#F9FAFB', borderRadius: '8px', padding: '16px', display: 'flex', gap: '24px'
                }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                    <Calendar size={18} color="#6B7280" />
                    <span style={{ fontSize: '0.9rem', fontWeight: 500, color: '#374151' }}>
                      {formatDate(booking.scheduledDateTime)}
                    </span>
                  </div>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                    <Clock size={18} color="#6B7280" />
                    <span style={{ fontSize: '0.9rem', fontWeight: 500, color: '#374151' }}>
                      {formatTime(booking.scheduledDateTime)}
                    </span>
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
};

export default Bookings;