import React, { useContext } from "react";
import { Link } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";
import Login from "../pages/Login";

export const Home = () => {
  const { user } = useContext(AuthContext);

  return (
    <div className="auth-bg">

      {/* If NOT logged in → show login form */}
      {!user ? (
        <Login />
      ) : (
        // If logged in → show dashboard options
        <div className="auth-card text-center">
          <h2 className="auth-title" style={{color:'white'}}>Welcome back, {user.username}! 👋</h2>

          {user.role === "ADMIN" && (
            <Link className="btn btn-warning mt-3 w-100" to="/admin">
              Go to Admin Panel
            </Link>
          )}

          {user.role === "USER" && (
            <Link className="btn btn-primary mt-3 w-100" to="/dashboard">
              Go to Dashboard
            </Link>
          )}
        </div>
      )}

    </div>
  );
};
