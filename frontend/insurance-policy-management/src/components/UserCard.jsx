import React, { useState, useContext } from "react";
import { getUserPolicies } from "../services/UserService";
import { PolicyCard } from "./PolicyCard";
import { AuthContext } from "../context/AuthContext";
import { FiEye, FiEyeOff } from "react-icons/fi";

const UserCard = ({ user, onDelete }) => {
  const [showPolicies, setShowPolicies] = useState(false);
  const [policies, setPolicies] = useState([]);
  const [loading, setLoading] = useState(false);
  const { token } = useContext(AuthContext);

  const togglePolicies = async () => {
    if (!showPolicies) {
      setLoading(true);
      const res = await getUserPolicies(user.id, token);
      setPolicies(res.data);
      setLoading(false);
    }
    setShowPolicies(!showPolicies);
  };

  return (
    <div className="card mb-3 position-relative" style={{ width: "18rem", background: '#adbbdb' }}>
      <div className="card-body">
        <h5>{user.username}</h5>
        <p>Email: {user.email}</p>

        {/* Buttons side by side */}
        <div className="d-flex justify-content-between mt-2">
          {/* Show/Hide Policies */}
          <button className="btn btn-primary btn-sm" onClick={togglePolicies}>
            {showPolicies ? <FiEyeOff size={18} /> : <FiEye size={18} />}
          </button>

          {/* Delete button */}
          {onDelete && (
            <button
              className="btn btn-outline-danger btn-sm"
              onClick={() => onDelete(user.id)}
              title="Delete User"
            >
              <i className="fa-solid fa-trash me-1"></i> Delete
            </button>
          )}
        </div>

        {loading && <p className="mt-2">Loading...</p>}

        {showPolicies && policies.map((p) => (
          <PolicyCard key={p.id} policy={p} />
        ))}
      </div>
    </div>
  );
};

export default UserCard;
