import { useEffect, useState, useContext } from "react";
import { getMyPolicies, deletePolicy } from "../services/PolicyService";
import { PolicyCard } from "../components/PolicyCard";
import { AuthContext } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";
import { FiEdit, FiTrash2 } from "react-icons/fi";

export const UserView = () => {
  const { token } = useContext(AuthContext);
  const [policies, setPolicies] = useState([]);
  const navigate = useNavigate();

  const fetchPolicies = async () => {
    try {
      const res = await getMyPolicies(token);
      setPolicies(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    fetchPolicies();
  }, [token]);

  const handleDelete = async (id) => {
    if (window.confirm("Delete this policy?")) {
      await deletePolicy(id);
      fetchPolicies();
    }
  };

  return (
    <div className="container mt-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>My Policies</h2>
        <button
          className="btn btn-success"
          onClick={() => navigate("/policies/new")}
        >
          + Create Policy
        </button>
      </div>

      {policies.length === 0 ? (
        <p>No policies found.</p>
      ) : (
        <div className="row">
          {policies.map((policy) => (
            <div key={policy.id} className="col-md-4 col-sm-6 mb-4">
              <div className="card shadow-sm" style={{ backgroundColor: "#490b58ff", borderRadius: "10px" }}>
                <div className="card-body">
                  <PolicyCard policy={policy} />

                  <div className="mt-3 d-flex justify-content-between">
                    <button
                      className="btn btn-outline-warning btn-sm"
                      onClick={() => navigate(`/policies/${policy.id}/edit`)}
                      title="Edit Policy"
                    >
                      <FiEdit size={18} />
                    </button>

                    <button
                      className="btn btn-outline-danger btn-sm"
                      onClick={() => handleDelete(policy.id)}
                      title="Delete Policy"
                    >
                      <FiTrash2 size={18} />
                    </button>
                  </div>

                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};
