import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import Navbar from "./components/Navbar";
import { Home } from "./components/Home";
import { About } from "./components/About";
import Login from "./pages/Login";
import Register from "./pages/Register";
import AdminView from "./pages/AdminView";
import { UserView } from "./pages/UserView";
import PolicyForm from "./pages/PolicyForm";
import { useContext } from "react";
import { AuthContext } from "./context/AuthContext";

const ProtectedRoute = ({ children, role }) => {
  const { user } = useContext(AuthContext);
  if (!user) return <Navigate to="/login" />;
  if (role && user.role !== role) return <Navigate to="/" />;
  return children;
};

function App() {
  return (
    <Router>
      <Navbar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/about" element={<About />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        <Route path="/admin" element={<ProtectedRoute role="ADMIN"><AdminView /></ProtectedRoute>} />
        <Route path="/dashboard" element={<ProtectedRoute role="USER"><UserView /></ProtectedRoute>} />
        <Route path="/policies/new" element={<ProtectedRoute role="USER"><PolicyForm /></ProtectedRoute>} />
        <Route path="/policies/:id/edit" element={<ProtectedRoute role="USER"><PolicyForm /></ProtectedRoute>} />
      </Routes>
    </Router>
  );
}

export default App;
