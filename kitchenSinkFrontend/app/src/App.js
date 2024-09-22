import './App.css';
import LoginPage from './pages/login';
import Signup from './pages/signup';
import Member from './pages/Member';
import Navbar from './pages/Navbar';
import { Route, Routes } from 'react-router-dom';
import { AuthProvider } from './Context/LoginContext';
import { ProtectedRoute } from './pages/ProtectedRoute';
import MemberEdit from './pages/MemberEdit';

function App() {
  return (
    <div className="App">
      <AuthProvider>
      <Navbar />
      <Routes>
        <Route path="/" element={<LoginPage />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/members" element={<ProtectedRoute><Member /></ProtectedRoute>} />
        <Route path="/members/:memberId" element={<ProtectedRoute><MemberEdit /></ProtectedRoute>} />
      </Routes>
      </AuthProvider>
    </div>
  );
}

export default App;
