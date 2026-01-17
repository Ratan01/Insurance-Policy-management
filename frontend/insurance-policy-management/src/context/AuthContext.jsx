// src/context/AuthContext.js
import { createContext, useState, useEffect } from "react";
import { decodeToken, isTokenExpired } from "../utils/jwtUtils";

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(localStorage.getItem("token"));
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const storedToken = localStorage.getItem("token");

    if (storedToken) {
      if (isTokenExpired(storedToken)) {
        logoutUser();
      } else {
        const decoded = decodeToken(storedToken);
        setUser({
          username: decoded.sub,
          role: decoded.role,
        });
        setToken(storedToken);
      }
    }
    setLoading(false);
  }, []);

  const loginUser = (userData, tokenValue) => {
    const decoded = decodeToken(tokenValue);
    const loggedUser = { username: decoded.sub, role: decoded.role };
    setUser(loggedUser);
    setToken(tokenValue);
    localStorage.setItem("token", tokenValue);
  };

  const logoutUser = () => {
    setUser(null);
    setToken(null);
    localStorage.removeItem("token");
  };

  return (
    <AuthContext.Provider value={{ user, token, loginUser, logoutUser, loading }}>
      {!loading && children}
    </AuthContext.Provider>
  );
};
