import React, { useState, useContext, useEffect } from "react";
import { AuthContext } from "../context/AuthContext";
import { createPolicy, updatePolicy, getPolicyById } from "../services/PolicyService";
import { useNavigate, useParams } from "react-router-dom";

const PolicyForm = () => {
  const { token } = useContext(AuthContext);
  const navigate = useNavigate();
  const { id } = useParams(); // edit mode
  const [policy, setPolicy] = useState({
    policyName: "",
    policyType: "",
    premiumAmount: "",
  });
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");

  // Fetch existing policy for edit mode
  useEffect(() => {
    if (id) {
      setLoading(true);
      getPolicyById(id, token)
        .then((res) => {
          setPolicy({
            policyName: res.data.policyName,
            policyType: res.data.policyType,
            premiumAmount: res.data.premiumAmount,
          });
        })
        .catch((err) => {
          console.error("Error fetching policy:", err);
          setMessage("Failed to fetch policy.");
        })
        .finally(() => setLoading(false));
    }
  }, [id, token]);

  const handleChange = (e) => {
    setPolicy({ ...policy, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!policy.policyName || !policy.policyType || !policy.premiumAmount) {
      setMessage("Please fill all fields.");
      return;
    }

    try {
      if (id) {
        await updatePolicy(id, policy, token);
        setMessage("Policy updated successfully!");
      } else {
        await createPolicy(policy, token);
        setMessage("Policy created successfully!");
      }

      setTimeout(() => navigate("/dashboard"), 800);
    } catch (err) {
      console.error("Error saving policy:", err);
      setMessage("Failed to save policy.");
    }
  };

  if (loading) return <p className="text-center mt-4">Loading...</p>;

  return (
    <div className="d-flex justify-content-center mt-5">
      <div className="card shadow p-4" style={{ width: "450px", background: "#a7f5ecff" }}>
        <h3 className="text-center mb-3">
          {id ? "Edit Policy" : "Create Policy"}
        </h3>

        {message && (
          <div className="alert alert-info text-center py-2">{message}</div>
        )}

        <form onSubmit={handleSubmit}>
          {/* Policy Name */}
          <div className="mb-3">
            <label className="form-label fw-semibold">Policy Name</label>
            <input
              type="text"
              name="policyName"
              className="form-control"
              value={policy.policyName}
              onChange={handleChange}
              placeholder="Enter policy name"
            />
          </div>

          {/* Policy Type */}
          <div className="mb-3">
            <label className="form-label fw-semibold">Policy Type</label>
            <input
              type="text"
              name="policyType"
              className="form-control"
              value={policy.policyType}
              onChange={handleChange}
              placeholder="Enter policy type"
            />
          </div>

          {/* Premium Amount */}
          <div className="mb-3">
            <label className="form-label fw-semibold">Premium Amount</label>
            <input
              type="number"
              name="premiumAmount"
              className="form-control"
              value={policy.premiumAmount}
              onChange={handleChange}
              placeholder="Enter premium amount"
            />
          </div>

          <button className="btn btn-primary w-100 mt-2">
            {id ? "Update Policy" : "Create Policy"}
          </button>
        </form>
      </div>
    </div>
  );
};

export default PolicyForm;
