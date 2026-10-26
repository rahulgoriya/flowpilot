import { useState, useEffect } from 'react';
import { createPortal } from 'react-dom'; // Ensures Modal renders on top
import { api } from '../api/client';
import type { Enquiry, Quote } from '../types';
import { Send, Loader2, Sparkles, Receipt, CheckCircle } from 'lucide-react';

const Enquiries = () => {
  const [enquiries, setEnquiries] = useState<Enquiry[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState('');

  // Form State
  const [customerId, setCustomerId] = useState('1');
  const [rawMessage, setRawMessage] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [aiResult, setAiResult] = useState<Enquiry | null>(null);

  // Quote State
  const [selectedQuote, setSelectedQuote] = useState<Quote | null>(null);
  const [isGeneratingQuote, setIsGeneratingQuote] = useState<number | null>(null);
  const [isAccepting, setIsAccepting] = useState(false);

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
      setAiResult(null);
      const newEnquiry = await api.createEnquiry({ customerId: parseInt(customerId), rawMessage });
      setAiResult(newEnquiry);
      setRawMessage('');
      fetchEnquiries();
    } catch (err: any) {
      alert(err.message);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleGenerateQuote = async (enquiryId: number) => {
    try {
      setIsGeneratingQuote(enquiryId);
      
      // 30 Second Timeout
      const timeoutPromise = new Promise((_, reject) => 
        setTimeout(() => reject(new Error("Request timed out. AI is taking too long.")), 30000)
      );
      
      const quote = await Promise.race([api.generateQuote(enquiryId), timeoutPromise]) as Quote;
      
      console.log("Quote Data Received:", quote);
      setSelectedQuote(quote);
    } catch (err: any) {
      alert(err.message);
    } finally {
      setIsGeneratingQuote(null);
    }
  };

  const handleAcceptQuote = async () => {
    if (!selectedQuote) return;
    try {
      setIsAccepting(true);
      await api.acceptQuote(selectedQuote.quoteId);
      alert('Quote Accepted! A booking has been created.');
      setSelectedQuote(null);
      fetchEnquiries();
    } catch (err: any) {
      alert(err.message);
    } finally {
      setIsAccepting(false);
    }
  };

  return (
    <div>
      {/* Header */}
      <div style={{ marginBottom: '24px' }}>
        <h1 style={{ fontSize: '1.5rem', fontWeight: 600, display: 'flex', alignItems: 'center', gap: '8px' }}>
          <Sparkles color="var(--primary)" /> Enquiries
        </h1>
        <p style={{ color: 'var(--text-muted)' }}>Type a customer message and let the AI extract the data.</p>
      </div>

      {/* Input Form */}
      <form onSubmit={handleSubmit} style={{ backgroundColor: 'white', padding: '20px', borderRadius: '12px', boxShadow: '0 4px 6px rgba(0,0,0,0.05)', marginBottom: '24px', display: 'flex', gap: '16px', alignItems: 'flex-start' }}>
        <div style={{ width: '100px' }}>
          <label style={{ fontSize: '0.85rem', fontWeight: 500, color: 'var(--text-muted)' }}>Customer ID</label>
          <input type="number" value={customerId} onChange={e => setCustomerId(e.target.value)} style={{ width: '100%', padding: '8px', marginTop: '4px', border: '1px solid #ddd', borderRadius: '6px' }} />
        </div>
        <div style={{ flex: 1 }}>
          <label style={{ fontSize: '0.85rem', fontWeight: 500, color: 'var(--text-muted)' }}>Customer Message</label>
          <textarea required placeholder="e.g., I need a 3BHK deep clean..." value={rawMessage} onChange={e => setRawMessage(e.target.value)} rows={2} style={{ width: '100%', padding: '8px', marginTop: '4px', border: '1px solid #ddd', borderRadius: '6px', resize: 'vertical' }} />
        </div>
        <button type="submit" disabled={isSubmitting} style={{ marginTop: '24px', display: 'flex', alignItems: 'center', gap: '8px', padding: '10px 16px', backgroundColor: 'var(--primary)', color: 'white', border: 'none', borderRadius: '8px', cursor: isSubmitting ? 'not-allowed' : 'pointer', fontWeight: 500, opacity: isSubmitting ? 0.7 : 1 }}>
          {isSubmitting ? <Loader2 size={18} className="animate-spin" /> : <Send size={18} />}
          {isSubmitting ? 'AI Extracting...' : 'Create Enquiry'}
        </button>
      </form>

      {/* AI Result Card */}
      {aiResult && (
        <div style={{ backgroundColor: '#e0e7ff', border: '1px solid #4F46E5', padding: '20px', borderRadius: '12px', marginBottom: '24px' }}>
          <h3 style={{ color: '#4F46E5', marginBottom: '12px', display: 'flex', alignItems: 'center', gap: '8px' }}><Sparkles size={18} /> AI Extraction Successful</h3>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(150px, 1fr))', gap: '12px' }}>
            <div><strong>Service:</strong> {aiResult.extractedServiceType || 'N/A'}</div>
            <div><strong>Property:</strong> {aiResult.extractedPropertyType || 'N/A'}</div>
            <div><strong>Bedrooms:</strong> {aiResult.extractedBedrooms || 'N/A'}</div>
            <div><strong>Location:</strong> {aiResult.extractedLocation || 'N/A'}</div>
            <div><strong>Intent:</strong> {aiResult.extractedIntent || 'N/A'}</div>
          </div>
        </div>
      )}

      {/* Enquiries List */}
      {isLoading ? (
        <div style={{ display: 'flex', justifyContent: 'center', marginTop: '50px' }}><Loader2 size={32} className="animate-spin" color="var(--primary)" /></div>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
          {enquiries.map((enq) => {
            const canGenerateQuote = enq.status === 'EXTRACTED' && ((enq as any).extractedPropertyType || (enq as any).extractedServiceType);
            const displayMessage = (enq as any).messagePreview || enq.rawMessage || 'No message';

            return (
              <div key={enq.id} style={{ backgroundColor: 'white', padding: '20px', borderRadius: '12px', boxShadow: '0 2px 4px rgba(0,0,0,0.05)', borderLeft: '4px solid #4F46E5' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '12px' }}>
                  <strong>Enquiry #{enq.id} - {enq.customerName}</strong>
                  <span style={{ fontSize: '0.75rem', padding: '4px 8px', borderRadius: '12px', backgroundColor: enq.status === 'EXTRACTED' ? '#d1fae5' : enq.status === 'ACCEPTED' ? '#bfdbfe' : enq.status === 'QUOTED' ? '#fef3c7' : '#fee2e2', color: enq.status === 'EXTRACTED' ? '#065f46' : enq.status === 'ACCEPTED' ? '#1e3a8a' : enq.status === 'QUOTED' ? '#92400e' : '#991b1b' }}>
                    {enq.status}
                  </span>
                </div>
                
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-end' }}>
                  <div>
                    <p style={{ color: 'var(--text-muted)', marginBottom: '8px' }}>"{displayMessage}"</p>
                    {(enq as any).extractedServiceType && (
                      <div style={{ display: 'flex', gap: '8px', flexWrap: 'wrap' }}>
                        <span style={tagStyle}>{(enq as any).extractedServiceType}</span>
                        <span style={tagStyle}>{(enq as any).extractedPropertyType}</span>
                        {(enq as any).extractedLocation && <span style={tagStyle}>📍 {(enq as any).extractedLocation}</span>}
                      </div>
                    )}
                  </div>

                  {canGenerateQuote && (
                    <button 
                      onClick={() => handleGenerateQuote(enq.id)}
                      disabled={isGeneratingQuote === enq.id}
                      style={{ display: 'flex', alignItems: 'center', gap: '6px', padding: '8px 12px', backgroundColor: '#F59E0B', color: 'white', border: 'none', borderRadius: '6px', cursor: 'pointer', fontWeight: 500, fontSize: '0.85rem' }}
                    >
                      {isGeneratingQuote === enq.id ? <Loader2 size={16} className="animate-spin" /> : <Receipt size={16} />}
                      {isGeneratingQuote === enq.id ? 'Calculating...' : 'Generate Quote'}
                    </button>
                  )}
                </div>
              </div>
            );
          })}
        </div>
      )}

      {/* QUOTE MODAL (Rendered via Portal to avoid CSS stacking issues) */}
      {selectedQuote && createPortal(
        <div style={{ 
          position: 'fixed', 
          top: 0, left: 0, width: '100vw', height: '100vh', 
          backgroundColor: 'rgba(0,0,0,0.6)', 
          display: 'flex', justifyContent: 'center', alignItems: 'center', 
          zIndex: 99999,
          padding: '20px'
        }}>
          <div style={{ 
            backgroundColor: 'white', 
            padding: '32px', 
            borderRadius: '16px', 
            width: '100%', 
            maxWidth: '450px', 
            boxShadow: '0 25px 50px -12px rgba(0,0,0,0.5)',
            position: 'relative'
          }}>
            <h2 style={{ marginBottom: '8px', textAlign: 'center', color: '#1F2937', fontSize: '1.5rem' }}>Quote Generated</h2>
            <p style={{ color: '#6B7280', textAlign: 'center', marginBottom: '24px' }}>For Enquiry #{selectedQuote.enquiryId}</p>
            
            <div style={{ backgroundColor: '#F9FAFB', padding: '20px', borderRadius: '12px', marginBottom: '24px', border: '1px solid #E5E7EB' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '12px', color: '#374151', fontSize: '1rem' }}>
                <span>Subtotal:</span> <strong>₹{selectedQuote.subtotal}</strong>
              </div>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '12px', color: '#374151', fontSize: '1rem' }}>
                <span>Add-ons:</span> <strong>₹{selectedQuote.addOns}</strong>
              </div>
              <hr style={{ margin: '12px 0', border: 'none', borderTop: '1px dashed #ccc' }} />
              <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '1.25rem', color: '#1F2937', fontWeight: 'bold' }}>
                <span>Total:</span> <strong style={{ color: '#4F46E5' }}>₹{selectedQuote.totalAmount}</strong>
              </div>
            </div>

            <div style={{ display: 'flex', gap: '16px' }}>
              <button onClick={() => setSelectedQuote(null)} style={{ flex: 1, padding: '14px', backgroundColor: 'white', border: '2px solid #E5E7EB', borderRadius: '8px', cursor: 'pointer', fontWeight: 'bold', color: '#4B5563' }}>
                Cancel
              </button>
              <button onClick={handleAcceptQuote} disabled={isAccepting} style={{ flex: 1, padding: '14px', backgroundColor: '#10B981', color: 'white', border: 'none', borderRadius: '8px', cursor: isAccepting ? 'not-allowed' : 'pointer', fontWeight: 'bold', display: 'flex', justifyContent: 'center', alignItems: 'center', gap: '8px' }}>
                {isAccepting ? <Loader2 size={20} className="animate-spin" /> : <CheckCircle size={20} />}
                {isAccepting ? 'Accepting...' : 'Accept Quote'}
              </button>
            </div>
          </div>
        </div>,
        document.body
      )}
    </div>
  );
};

const tagStyle: React.CSSProperties = { backgroundColor: '#F3F4F6', padding: '4px 10px', borderRadius: '6px', fontSize: '0.85rem', fontWeight: 500, color: '#4B5563' };

export default Enquiries;