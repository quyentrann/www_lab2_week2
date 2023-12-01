import { useState } from "react";
import 'react-toastify/dist/ReactToastify.css';
import axios from "axios";
import { Link, useNavigate } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";
import "bootstrap/dist/css/bootstrap.min.css"; // Import Bootstrap CSS
import "./login.scss";

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleClickLogin = async () => {
    try {
      const datas = await axios.post("http://localhost:8080/api/accounts/login", {
        email: email,
        password: password,
      });

      if (datas.data.role === "user") {
        const dataGetCust = await axios.get(`http://localhost:8080/api/customers/CustomerByEmail/${email}`);
        navigate("/home", {
          state: {
            customer: {
              ...dataGetCust.data,
              password: password,
              accountId: datas.data.accountId,
            },
          },
        });
      } else if (datas.data.role === "admin") {
        navigate("/dashboard");
      } else {
        toast.error('🦄 Email Hoặc Password Không Đúng!', {
          position: "top-right",
          autoClose: 5000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
          theme: "light",
        });
      }
    } catch (error) {
      toast.error('🚨 Something went wrong. Please try again later.', {
        position: "top-right",
        autoClose: 5000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
        theme: "light",
      });
    }
  };

  return (
    <div className="container login-container">
      <h2 className="text-login">ĐĂNG NHẬP</h2>
      <div className="form-group">
        <label htmlFor="login-email">Email :</label>
        <input
          type="email"
          value={email}
          id="login-email"
          className="form-control"
          onChange={(e) => setEmail(e.target.value)}
        />
      </div>
      <div className="form-group">
        <label htmlFor="login-password">Password :</label>
        <input
          type="password"
          value={password}
          id="login-password"
          className="form-control"
          onChange={(e) => setPassword(e.target.value)}
        />
      </div>
      <div className="ac">
        <button className="btn btn-primary" onClick={handleClickLogin}>
          Đăng Nhập
        </button>
        <Link style={{ marginTop: '10px', marginLeft: '30px' }} to={"/sign-up"} className="btn btn-link">
          Đăng ký
        </Link>
      </div>
      <ToastContainer />
    </div>
  );
};

export default Login;
