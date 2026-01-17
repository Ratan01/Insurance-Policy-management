import { useEffect, useState, useContext } from "react";
import { getAllUsers, deleteUser } from "../services/UserService";
import UserCard from "../components/UserCard";
import { AuthContext } from "../context/AuthContext";

const AdminView = () => {
  const { token } = useContext(AuthContext);
  const [users, setUsers] = useState([]);

  // Move fetchUsers outside useEffect so it can be reused
  const fetchUsers = async () => {
    try {
      const res = await getAllUsers(token);
      setUsers(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    fetchUsers();
  }, [token]);

  const handleDelete = async (id) => {
    if (window.confirm("Delete this user?")) {
      try {
        await deleteUser(id, token);
        alert("User deleted successfully");
        fetchUsers(); // Refresh list
      } catch (err) {
        console.error(err);
        alert("Failed to delete user");
      }
    }
  };

  return (
    <div className="container mt-4">
      <h2 className="mb-4">Admin Dashboard - All Users</h2>
      {users.length === 0 ? (
        <p>No users found.</p>
      ) : (
        <div className="row">
          {users.map((user) => (
            <div key={user.id} className="col-md-4 col-sm-6 mb-4">
              <UserCard user={user} onDelete={() => handleDelete(user.id)} />
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default AdminView;
