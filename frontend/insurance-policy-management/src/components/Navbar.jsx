import React, { useContext } from "react";
import { Link, useNavigate } from "react-router-dom";
import { FiLogOut, FiUser } from "react-icons/fi";
import { AuthContext } from "../context/AuthContext";

const Navbar = () => {
  const navigate = useNavigate();
  const { user, logoutUser } = useContext(AuthContext);

  const logout = () => {
    // clear localStorage and context
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("username");
    logoutUser(); // update context
    navigate("/login");
  };

  const username = user?.username || localStorage.getItem("username");
  const role = user?.role || localStorage.getItem("role");

  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-dark px-4 shadow">
      <img 
        src="/logo.png" 
        alt="Logo" 
        style={{ width: "28px", height: "28px", marginRight: "8px", borderRadius: "4px" }}
      />
      <Link className="navbar-brand fw-bold" to="/">IPM</Link>

      <button
        className="navbar-toggler"
        type="button"
        data-bs-toggle="collapse"
        data-bs-target="#navbarNav"
      >
        <span className="navbar-toggler-icon"></span>
      </button>

      <div className="collapse navbar-collapse" id="navbarNav">
        <ul className="navbar-nav me-auto">
          <li className="nav-item"><Link className="nav-link" to="/">Home</Link></li>
          <li className="nav-item"><Link className="nav-link" to="/about">About</Link></li>

          {role === "ADMIN" && (
            <li className="nav-item"><Link className="nav-link" to="/admin">Admin Dashboard</Link></li>
          )}
          {role === "USER" && (
            <li className="nav-item"><Link className="nav-link" to="/dashboard">User Dashboard</Link></li>
          )}
        </ul>

        {/* Right side: Welcome + Logout */}
        <ul className="navbar-nav ms-auto">
          {username && (
            <li className="nav-item d-flex align-items-center text-white me-3">
              <FiUser className="me-2" /> Welcome, <strong>{username}</strong>
            </li>
          )}

          {username && (
            <li className="nav-item">
              <button
                onClick={logout}
                className="btn btn-danger btn-sm d-flex align-items-center"
              >
                <FiLogOut className="me-1" /> Logout
              </button>
            </li>
          )}
        </ul>
      </div>
    </nav>
  );
};

export default Navbar;
