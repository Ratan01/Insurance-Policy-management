import axios from "axios";

const BASE_URL = "http://localhost:8090/api/users";

// Get all users (ADMIN only)
export const getAllUsers = async (token) => {
  return await axios.get(BASE_URL, {
    headers: { Authorization: `Bearer ${token}` }
  });
};

// Get user by ID (ADMIN only)
export const getUserById = async (userId, token) => {
  return await axios.get(`${BASE_URL}/${userId}`, {
    headers: { Authorization: `Bearer ${token}` }
  });
};

// Delete user by ID (ADMIN only)
export const deleteUser = async (userId, token) => {
  return await axios.delete(`${BASE_URL}/${userId}`, {
    headers: { Authorization: `Bearer ${token}` }
  });
};

// Get all policies of a user (ADMIN only)
export const getUserPolicies = async (userId, token) => {
  return await axios.get(`${BASE_URL}/${userId}/policies`, {
    headers: { Authorization: `Bearer ${token}` }
  });
};
