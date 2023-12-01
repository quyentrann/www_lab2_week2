import { useEffect, useState } from "react";
import "../styles.scss"; // Make sure to import your styles or customize as needed
import axios from "axios";
import { ToastContainer, toast } from "react-toastify";

const CustomerManagement = () => {
  const [customers, setCustomers] = useState([]);
  const [selectedCustomer, setSelectedCustomer] = useState(null);

  useEffect(() => {
    // Fetch customer data from your API
    const fetchData = async () => {
      try {
        const response = await axios.get("http://localhost:8080/api/customers");
        setCustomers(response.data);
      } catch (error) {
        console.error("Error fetching customer data:", error);
      }
    };

    fetchData();
  }, []); // Fetch data on component mount

  const handleCustomerClick = (customer) => {
    setSelectedCustomer(customer);
    // Additional logic or actions when a customer is selected
  };

  return (
    <div className="customer-management-container">
      <h1>QUẢN LÝ KHÁCH HÀNG</h1>
      <div className="customer-list">
        <table className="customer-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Email</th>
              {/* Add more columns as needed */}
            </tr>
          </thead>
          <tbody>
            {customers.map((customer) => (
              <tr
                key={customer.custId}
                className={selectedCustomer === customer ? "selected-customer" : ""}
                onClick={() => handleCustomerClick(customer)}
              >
                <td>{customer.custId}</td>
                <td>{customer.custName}</td>
                <td>{customer.email}</td>
                {/* Add more cells as needed */}
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <div className="customer-details">
        {selectedCustomer && (
          <div>
            <h2>Chi Tiết Khách Hàng</h2>
            <p>ID: {selectedCustomer.custId}</p>
            <p>Name: {selectedCustomer.custName}</p>
            <p>Email: {selectedCustomer.email}</p>
            {/* Add more details as needed */}
          </div>
        )}
      </div>
      <ToastContainer />
    </div>
  );
};

export default Customer;
