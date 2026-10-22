const BASE_URL = 'http://localhost:8080/api';

export const api = {
  // GET Customers
  getCustomers: async () => {
    const response = await fetch(`${BASE_URL}/customers`);
    if (!response.ok) throw new Error('Failed to fetch customers');
    return response.json();
  },

  // POST Create Customer
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
  }
};