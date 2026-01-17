import { useState, useContext } from "react";
import axios from "axios";
import { AuthContext } from "../context/AuthContext";
import { decodeToken } from "../utils/jwtUtils";
import { useNavigate, Link } from "react-router-dom";
import "../styles/auth.css";

const Login = () => {
  const [credentials, setCredentials] = useState({ username: "", password: "" });
  const { loginUser } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const res = await axios.post("http://localhost:8090/api/auth/login", credentials);
      const token = res.data?.token;

      if (token) {
        localStorage.setItem("token", token);
        localStorage.setItem("role", res.data.role);
        localStorage.setItem("username", res.data.username);

        loginUser({ username: res.data.username, role: res.data.role }, token);

        alert(`Welcome ${res.data.username}`);

        // Always go to Home page after login
        navigate("/");
      }
    } catch (err) {
      alert("Invalid credentials");
    }
  };

  return (
    <div className="auth-bg">
      <div className="auth-card">
        <h2 className="auth-title" style={{ color: 'white' }}>Login</h2>

        <form onSubmit={handleSubmit}>
          <input
            className="form-control mb-3"
            placeholder="Username"
            value={credentials.username}
            onChange={(e) => setCredentials({ ...credentials, username: e.target.value })}
          />

          <input
            className="form-control mb-3"
            type="password"
            placeholder="Password"
            value={credentials.password}
            onChange={(e) => setCredentials({ ...credentials, password: e.target.value })}
          />

          <button type="submit" className="btn btn-primary w-100">
            Login
          </button>

          <Link to="/register" className="auth-link">
            Don't have an account?
          </Link>
        </form>
      </div>
    </div>
  );
};

export default Login;
