import axios from "axios";

const BASE_URL = "http://localhost:8090/api/policies";

// Get all policies (ADMIN only)
export const getAllPolicies = async () => {
  return await axios.get(BASE_URL, {
    headers: {
      Authorization: `Bearer ${localStorage.getItem("token")}`
    }
  });
};

// Get a policy by ID (USER or ADMIN)
export const getPolicyById = async (policyId) => {
  return await axios.get(`${BASE_URL}/${policyId}`, {
    headers: {
      Authorization: `Bearer ${localStorage.getItem("token")}`
    }
  });
};

// Create a new policy (USER only)
export const createPolicy = async (policyData) => {
  return await axios.post(BASE_URL, policyData, {
    headers: {
      Authorization: `Bearer ${localStorage.getItem("token")}`
    }
  });
};

// Get logged-in user's policies (USER only)
export const getMyPolicies = async () => {
  return await axios.get(`${BASE_URL}/my`, {
    headers: {
      Authorization: `Bearer ${localStorage.getItem("token")}`
    }
  });
};

// Update a policy (USER only)
export const updatePolicy = async (policyId, policyData) => {
  return await axios.put(`${BASE_URL}/${policyId}`, policyData, {
    headers: {
      Authorization: `Bearer ${localStorage.getItem("token")}`
    }
  });
};

// Delete a policy (USER only)
export const deletePolicy = async (policyId) => {
  return await axios.delete(`${BASE_URL}/${policyId}`, {
    headers: {
      Authorization: `Bearer ${localStorage.getItem("token")}`
    }
  });
};