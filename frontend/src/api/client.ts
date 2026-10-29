const BASE_URL = 'http://localhost:8080/api';

// Helper to get the token from localStorage
const getAuthHeaders = () => {
  // Make sure this matches the key used in AuthContext.tsx
  const token = localStorage.getItem('flowpilot_token'); 
  return {
    'Content-Type': 'application/json',
    ...(token ? { 'Authorization': `Bearer ${token}` } : {}),
  };
};

export const api = {
  // --- Authentication ---
  login: async (data: { email: string; password: string }) => {
    const response = await fetch(`${BASE_URL}/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(data),
    });
    if (!response.ok) {
      throw new Error('Invalid email or password');
    }
    return response.json();
  },

  // --- Customers ---
  getCustomers: async () => {
    const response = await fetch(`${BASE_URL}/customers`, {
      headers: getAuthHeaders(),
    });
    if (!response.ok) throw new Error('Failed to fetch customers');
    return response.json();
  },

  createCustomer: async (data: { name: string; email: string; phone: string }) => {
    const response = await fetch(`${BASE_URL}/customers`, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify(data),
    });
    
    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Failed to create customer');
    }
    return response.json();
  },

  // --- Enquiries ---
  getEnquiries: async () => {
    const response = await fetch(`${BASE_URL}/enquiries`, {
      headers: getAuthHeaders(),
    });
    if (!response.ok) throw new Error('Failed to fetch enquiries');
    return response.json();
  },

  createEnquiry: async (data: { customerId: number; rawMessage: string }) => {
    const response = await fetch(`${BASE_URL}/enquiries`, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify(data),
    });
    
    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Failed to create enquiry');
    }
    return response.json();
  },

  // --- Quotes ---
  generateQuote: async (enquiryId: number) => {
    const response = await fetch(`${BASE_URL}/enquiries/${enquiryId}/quote`, {
      headers: getAuthHeaders(),
    });
    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Failed to generate quote');
    }
    return response.json();
  },

  acceptQuote: async (quoteId: number) => {
    const response = await fetch(`${BASE_URL}/quotes/${quoteId}/accept`, {
      method: 'PATCH',
      headers: getAuthHeaders(),
    });
    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Failed to accept quote');
    }
    return response.json();
  },

  // --- Bookings ---
  getBookings: async () => {
    const response = await fetch(`${BASE_URL}/bookings`, {
      headers: getAuthHeaders(),
    });
    if (!response.ok) throw new Error('Failed to fetch bookings');
    return response.json();
  },
};