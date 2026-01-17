import React from "react";

export const PolicyCard = ({ policy }) => (
  <div
    className="p-2 rounded shadow-sm"
    style={{
      backgroundColor: "#C0C0C0",
      borderRadius: "10px",
      minHeight: "150px"
    }}
  >
    <h5 className="mb-2">{policy.policyName}</h5>

    <p className="mb-1">
      <strong>Type:</strong> {policy.policyType}
    </p>

    <p className="mb-1">
      <strong>Premium:</strong> ${policy.premiumAmount}
    </p>

    {policy.user && (
      <p className="mb-0">
        <strong>User:</strong> {policy.user.username}
      </p>
    )}
  </div>
);
