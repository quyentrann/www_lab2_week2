import { useState } from "react";
import "../signUp/signUp.scss";
import 'react-toastify/dist/ReactToastify.css';
import { ToastContainer, toast } from "react-toastify";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import 'bootstrap/dist/css/bootstrap.min.css';
import { Form, Button } from "react-bootstrap";
const SignUp = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [passwordConfirm, setPasswordConfirm] = useState("");
  const [confirm, setConfirm] = useState(false);

  let navigate = useNavigate();

  let handleChangeComfirmPassword = (value) => {
    setConfirm(password === value ? true : false);
    setPasswordConfirm(value);
  };

  let handleChangePassword = (value) => {
    setConfirm(passwordConfirm === value ? true : false);
    setPassword(value);
  };

  let handleClickSignUp = async () => {
    let datas = await axios.get(
      `http://localhost:8080/api/accounts/checkExist/${email}`
    );
    if (!datas.data) {
      let dataCust = await axios.get(
        "http://localhost:8080/api/customers/getID"
      );
      await axios.post("http://localhost:8080/api/customers", {
        custId: dataCust.data + 1,
        custName: "",
        email: email,
        phone: "",
        address: "",
      });
      let datasGetCust = await axios.get(
        `http://localhost:8080/api/customers/${dataCust.data + 1}`
      );

      let dataAccountId = await axios.get(
        "http://localhost:8080/api/accounts/getID"
      );
      let dataPostAcocunt = await axios.post(
        "http://localhost:8080/api/accounts",
        {
          accountId: dataAccountId.data + 1,
          email: email,
          password: password,
          customer: datasGetCust.data,
        }
      );

      if (dataPostAcocunt) {
        navigate("/");
      }
    } else {
      toast.error("🦄 Email đã tồn tại!", {
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
    <div className="container-sign-up">
      <span className="text-sign-up">ĐĂNG KÝ</span>
      <Form>
        <Form.Group controlId="sign-up-email">
          <Form.Label>Email:</Form.Label>
          <Form.Control
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
        </Form.Group>

        <Form.Group controlId="sign-up-password">
          <Form.Label>Password:</Form.Label>
          <Form.Control
            type="password"
            value={password}
            onChange={(e) => handleChangePassword(e.target.value)}
          />
        </Form.Group>

        <Form.Group controlId="sign-up-confirm-password">
          <Form.Label>Confirm Password:</Form.Label>
          <Form.Control
            type="password"
            value={passwordConfirm}
            onChange={(e) => handleChangeComfirmPassword(e.target.value)}
          />
          {confirm ? (
            <i
              className="fa-solid fa-circle-check icon-confirm"
              style={{ color: "green" }}
            ></i>
          ) : (
            <i
              className="fa-solid fa-circle-xmark icon-confirm"
              style={{ color: "red" }}
            ></i>
          )}
        </Form.Group>

        <Button variant="primary" onClick={handleClickSignUp}>
          Đăng Ký
        </Button>
      </Form>

      <ToastContainer />
    </div>
  );
};

export default SignUp;
