import axios from "axios";

const API_URL = "http://localhost:8090/api/auth";

export const registerUser = (userData) => {
  return axios.post(`${API_URL}/register`, userData, {
    headers: { "Content-Type": "application/json" },
  });
};

export const loginUser = (userData) => {
  return axios.post(`${API_URL}/login`, userData, {
    headers: { "Content-Type": "application/json" },
  });
};