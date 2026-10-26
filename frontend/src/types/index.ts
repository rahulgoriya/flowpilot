export interface Customer {
  id: number;
  name: string;
  email: string;
  phone: string;
  createdAt: string;
}
export interface Enquiry {
  id: number;
  customerId: number;
  customerName: string;
  messagePreview?: string; // Add this for list
  rawMessage?: string;
  status: string;
  createdAt: string;
  extractedServiceType: string | null;
  extractedPropertyType: string | null;
  extractedBedrooms: number | null;
  extractedLocation: string | null;
  extractedPreferredDateTime: string | null;
  extractedIntent: string | null;
}
export interface Quote {
  quoteId: number;
  enquiryId: number;
  subtotal: number;
  addOns: number;
  totalAmount: number;
  status: string;
  expiresAt: string;
  breakdown: string;
}