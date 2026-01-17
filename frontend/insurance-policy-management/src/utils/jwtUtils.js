// src/utils/jwtUtils.js
import { jwtDecode } from "jwt-decode"; 

// Decode JWT and extract info
export const decodeToken = (token) => {
  if (!token) return null;
  try {
    return jwtDecode(token);
  } catch (error) {
    console.error("Invalid token:", error);
    return null;
  }
};

// Check if token is expired
export const isTokenExpired = (token) => {
  try {
    const decoded = jwtDecode(token);
    if (!decoded.exp) return true;
    const currentTime = Date.now() / 1000;
    return decoded.exp < currentTime;
  } catch (error) {
    console.error("Error checking token expiration:", error);
    return true;
  }
};

// Extract username from token
export const getUsernameFromToken = (token) => {
  const decoded = decodeToken(token);
  return decoded ? decoded.sub : null; // `sub` holds username in your JWTUtil
};
