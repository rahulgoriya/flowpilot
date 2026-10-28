const BASE_URL = 'http://localhost:8080/api';

export const api = {
  // --- Customers ---
  getCustomers: async () => {
    const response = await fetch(`${BASE_URL}/customers`);
    if (!response.ok) throw new Error('Failed to fetch customers');
    return response.json();
  },

  createCustomer: async (data: { name: string; email: string; phone: string }) => {
    const response = await fetch(`${BASE_URL}/customers`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
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
    const response = await fetch(`${BASE_URL}/enquiries`);
    if (!response.ok) throw new Error('Failed to fetch enquiries');
    return response.json();
  },

  createEnquiry: async (data: { customerId: number; rawMessage: string }) => {
    const response = await fetch(`${BASE_URL}/enquiries`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
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
    const response = await fetch(`${BASE_URL}/enquiries/${enquiryId}/quote`);
    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Failed to generate quote');
    }
    return response.json();
  },

  acceptQuote: async (quoteId: number) => {
    const response = await fetch(`${BASE_URL}/quotes/${quoteId}/accept`, {
      method: 'PATCH',
    });
    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Failed to accept quote');
    }
    return response.json();
  },
    // --- Bookings ---
  getBookings: async () => {
    const response = await fetch(`${BASE_URL}/bookings`);
    if (!response.ok) throw new Error('Failed to fetch bookings');
    return response.json();
  },
};