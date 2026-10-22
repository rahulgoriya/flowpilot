import { BrowserRouter, Routes, Route } from 'react-router-dom';
import DashboardLayout from './components/DashboardLayout';
import Dashboard from './pages/Dashboard';
import Customers from './pages/Customers';
import Enquiries from './pages/Enquiries';
import Bookings from './pages/Bookings';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* The DashboardLayout wraps all child pages */}
        <Route path="/" element={<DashboardLayout />}>
          
          {/* Child routes render inside the <Outlet /> of DashboardLayout */}
          <Route index element={<Dashboard />} />       {/* Default page for "/" */}
          <Route path="customers" element={<Customers />} />
          <Route path="enquiries" element={<Enquiries />} />
          <Route path="bookings" element={<Bookings />} />
          
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;