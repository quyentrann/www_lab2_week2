// Payment.jsx

import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import "../payment/payment.scss";
import axios from "axios";
import { add, format } from "date-fns";

const Payment = () => {
  let location = useLocation();
  let navigate = useNavigate();
  const [productsChosen, setProductsChosen] = useState([]);
  const [employees, setemployees] = useState([]);
  const [customer, setCustomer] = useState();
  const [payments, setPayments] = useState([
    {
      id: 1,
      type: "Thanh Toán Khi Nhận Hàng",
    },
    {
      id: 2,
      type: "Thanh Toán Bằng MoMo",
    },
  ]);
  const [typePaymentChosen, setTypePaymentChosen] = useState();
  const [employeeChosen, setemployeeChosen] = useState(1);
  const [name, setName] = useState("");
  const [address, setAddress] = useState("");
  const [email, setEmail] = useState("");
  const [phone, setPhone] = useState("");

  useEffect(() => {
    setProductsChosen(location.state.productsChosen);
    setCustomer({
      custId: location.state.customer.custId,
      custName: location.state.customer.custName,
      email: location.state.customer.email,
      phone: location.state.customer.phone,
      address: location.state.customer.address,
    });
    setName(location.state.customer.custName);
    setEmail(location.state.customer.email);
    setPhone(location.state.customer.phone);
    setAddress(location.state.customer.address);
    console.log(location.state);
    let getApiEmployees = async () => {
      let datas = await axios.get("http://localhost:8080/api/employees");
      setemployees(datas.data);
    };
    getApiEmployees();
  }, [JSON.stringify(location.state)]);

  let handleClickFinishPayment = async () => {
    alert("Thanh Toán Thành Công!!!");
    navigate("/home", { state: { customer: location.state.customer } });
  };

  return (
    <div className="container-payment">
      <button className="btn-back" onClick={() => navigate(-1)}>
        <i className="fa-solid fa-arrow-left icon-back"></i>
      </button>
      <h3>Thông Tin Khách Hàng</h3>
      <div className="form-customer">
        <div className="content-form">
          <label className="lblForm" htmlFor="name">
            Name :{" "}
          </label>
          <input
            value={name}
            className="inputForm"
            type="text"
            name=""
            id="name"
            onChange={(e) => setName(e.target.value)}
          />
        </div>
        <div className="content-form">
          <label className="lblForm" htmlFor="address">
            Address :{" "}
          </label>
          <input
            value={address}
            className="inputForm"
            type="text"
            name=""
            id="address"
            onChange={(e) => setAddress(e.target.value)}
          />
        </div>
        <div className="content-form">
          <label className="lblForm" htmlFor="phone">
            Phone :{" "}
          </label>
          <input
            value={phone}
            className="inputForm"
            type="tel"
            name=""
            id="phone"
            onChange={(e) => setPhone(e.target.value)}
          />
        </div>

        <div className="product-payment">
          {productsChosen.map((pro) => (
            <div className="content-cart" key={pro.product_id}>
              <img
                src={`/${pro.path}`}
                alt=""
                className="image-product-chosen"
              />
              <div className="infor">
                <span className="name-product-chosen">{pro.name}</span>
                <span className="price-product-chosen">
                  {new Intl.NumberFormat("vi-VN", {
                    style: "currency",
                    currency: "VND",
                  }).format(pro.price)}
                </span>
                <div style={{ flexDirection: "row" }}>
                  <span className="quantity-product-chosen">Số Lượng : </span>
                  <input
                    value={pro.quantity}
                    type="number"
                    name=""
                    id=""
                    className="quantity-product-chosen"
                    min={0}
                    disabled
                  />
                </div>
              </div>
            </div>
          ))}
        </div>

        <div className="finish-payment">
          <button
            className="btn-finish-payment"
            onClick={handleClickFinishPayment}
          >
            Thanh Toán
          </button>
        </div>
      </div>
    </div>
  );
};

export default Payment;
