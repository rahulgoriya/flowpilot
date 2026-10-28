import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import Customers from './pages/Customers';
import Enquiries from './pages/Enquiries';
import Bookings from './pages/Bookings';
import DashboardLayout from './components/DashboardLayout';

// Helper: Check karega ki token hai ya nahi
const isAuthenticated = () => {
  return !!localStorage.getItem('token');
};

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Public Route */}
        <Route path="/login" element={<Login />} />

        {/* Protected Routes (Bina login ke andar nahi ja sakte) */}
        <Route 
          path="/" 
          element={isAuthenticated() ? <DashboardLayout /> : <Navigate to="/login" />}
        >
          <Route index element={<Navigate to="/dashboard" />} />
          <Route path="dashboard" element={<Dashboard />} />
          <Route path="customers" element={<Customers />} />
          <Route path="enquiries" element={<Enquiries />} />
          <Route path="bookings" element={<Bookings />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;