import { useState, useEffect } from 'react';
import { api } from '../api/client';
import type { Enquiry } from '../types';
import { Send, Loader2, Sparkles } from 'lucide-react';

const Enquiries = () => {
  const [enquiries, setEnquiries] = useState<Enquiry[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState('');

  // Form State
  const [customerId, setCustomerId] = useState('1'); // Default to customer 1 for demo
  const [rawMessage, setRawMessage] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [aiResult, setAiResult] = useState<Enquiry | null>(null);

  useEffect(() => {
    fetchEnquiries();
  }, []);

  const fetchEnquiries = async () => {
    try {
      setIsLoading(true);
      const data = await api.getEnquiries();
      setEnquiries(data);
    } catch (err: any) {
      setError(err.message);
    } finally {
      setIsLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!rawMessage.trim()) return;

    try {
      setIsSubmitting(true);
      setAiResult(null); // Clear previous result

      const newEnquiry = await api.createEnquiry({
        customerId: parseInt(customerId),
        rawMessage: rawMessage
      });

      // Show the AI result card
      setAiResult(newEnquiry);
      setRawMessage(''); // Clear input
      
      // Refresh the list below
      fetchEnquiries();

    } catch (err: any) {
      alert(err.message);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div>
      <div style={{ marginBottom: '24px' }}>
        <h1 style={{ fontSize: '1.5rem', fontWeight: 600, display: 'flex', alignItems: 'center', gap: '8px' }}>
          <Sparkles color="var(--primary)" /> Enquiries
        </h1>
        <p style={{ color: 'var(--text-muted)' }}>Type a customer message and let the AI extract the data.</p>
      </div>

      {/* AI Input Form */}
      <form onSubmit={handleSubmit} style={{
        backgroundColor: 'white', padding: '20px', borderRadius: '12px',
        boxShadow: '0 4px 6px rgba(0,0,0,0.05)', marginBottom: '24px',
        display: 'flex', gap: '16px', alignItems: 'flex-start'
      }}>
        <div style={{ width: '100px' }}>
          <label style={{ fontSize: '0.85rem', fontWeight: 500, color: 'var(--text-muted)' }}>Customer ID</label>
          <input type="number" value={customerId} onChange={e => setCustomerId(e.target.value)}
            style={{ width: '100%', padding: '8px', marginTop: '4px', border: '1px solid #ddd', borderRadius: '6px' }} />
        </div>
        
        <div style={{ flex: 1 }}>
          <label style={{ fontSize: '0.85rem', fontWeight: 500, color: 'var(--text-muted)' }}>Customer Message (Natural Language)</label>
          <textarea 
            required 
            placeholder="e.g., I need a 3BHK deep clean in Koramangala this Saturday..."
            value={rawMessage} 
            onChange={e => setRawMessage(e.target.value)}
            rows={2}
            style={{ width: '100%', padding: '8px', marginTop: '4px', border: '1px solid #ddd', borderRadius: '6px', resize: 'vertical' }} 
          />
        </div>
        
        <button type="submit" disabled={isSubmitting} style={{
          marginTop: '24px',
          display: 'flex', alignItems: 'center', gap: '8px',
          padding: '10px 16px', backgroundColor: 'var(--primary)',
          color: 'white', border: 'none', borderRadius: '8px',
          cursor: isSubmitting ? 'not-allowed' : 'pointer', fontWeight: 500,
          opacity: isSubmitting ? 0.7 : 1
        }}>
          {isSubmitting ? <Loader2 size={18} className="animate-spin" /> : <Send size={18} />}
          {isSubmitting ? 'AI Extracting...' : 'Create Enquiry'}
        </button>
      </form>

      {/* AI Result Card */}
      {aiResult && (
        <div style={{ 
          backgroundColor: '#e0e7ff', border: '1px solid #4F46E5', 
          padding: '20px', borderRadius: '12px', marginBottom: '24px',
          animation: 'fadeIn 0.5s ease'
        }}>
          <h3 style={{ color: '#4F46E5', marginBottom: '12px', display: 'flex', alignItems: 'center', gap: '8px' }}>
            <Sparkles size={18} /> AI Extraction Successful
          </h3>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(150px, 1fr))', gap: '12px' }}>
            <div><strong>Service:</strong> {aiResult.extractedServiceType || 'N/A'}</div>
            <div><strong>Property:</strong> {aiResult.extractedPropertyType || 'N/A'}</div>
            <div><strong>Bedrooms:</strong> {aiResult.extractedBedrooms || 'N/A'}</div>
            <div><strong>Location:</strong> {aiResult.extractedLocation || 'N/A'}</div>
            <div><strong>Date/Time:</strong> {aiResult.extractedPreferredDateTime || 'N/A'}</div>
            <div><strong>Intent:</strong> {aiResult.extractedIntent || 'N/A'}</div>
          </div>
        </div>
      )}

      {/* Enquiries List */}
      {isLoading ? (
        <div style={{ display: 'flex', justifyContent: 'center', marginTop: '50px' }}>
          <Loader2 size={32} className="animate-spin" color="var(--primary)" />
        </div>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
          {enquiries.map((enq) => (
            <div key={enq.id} style={{
              backgroundColor: 'white', padding: '20px', borderRadius: '12px',
              boxShadow: '0 2px 4px rgba(0,0,0,0.05)', borderLeft: '4px solid #4F46E5'
            }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '12px' }}>
                <strong>Enquiry #{enq.id} - {enq.customerName}</strong>
                <span style={{ 
                  fontSize: '0.75rem', padding: '4px 8px', borderRadius: '12px',
                  backgroundColor: enq.status === 'EXTRACTED' ? '#d1fae5' : '#fee2e2',
                  color: enq.status === 'EXTRACTED' ? '#065f46' : '#991b1b'
                }}>
                  {enq.status}
                </span>
              </div>
              <p style={{ color: 'var(--text-muted)', marginBottom: '12px' }}>"{enq.rawMessage}"</p>
              
              {/* Show extracted tags if available */}
              {enq.status === 'EXTRACTED' && (
                <div style={{ display: 'flex', gap: '8px', flexWrap: 'wrap' }}>
                  {enq.extractedServiceType && <span style={tagStyle}>{enq.extractedServiceType}</span>}
                  {enq.extractedPropertyType && <span style={tagStyle}>{enq.extractedPropertyType}</span>}
                  {enq.extractedLocation && <span style={tagStyle}>📍 {enq.extractedLocation}</span>}
                  {enq.extractedIntent && <span style={tagStyle}>{enq.extractedIntent}</span>}
                </div>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

// Helper style for tags
const tagStyle = {
  backgroundColor: '#F3F4F6',
  padding: '4px 10px',
  borderRadius: '6px',
  fontSize: '0.85rem',
  fontWeight: 500,
  color: '#4B5563'
};

export default Enquiries;