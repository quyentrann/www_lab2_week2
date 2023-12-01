// Cart.jsx

import "../cart/cart.scss";
import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";

const Cart = () => {
  let location = useLocation();
  let navigate = useNavigate();
  const [productsChosen, setProductsChosen] = useState([]);
  const [total, setTotal] = useState(0);
  const [customer, setCustomer] = useState();

  useEffect(() => {
    setProductsChosen(location.state.productsChosen);
    setCustomer(location.state.customer);
  }, [JSON.stringify(location.state)]);

  useEffect(() => {
    let newTotal = productsChosen.reduce(
      (sum, product) => sum + product.price * product.quantity,
      0
    );
    setTotal(newTotal);
  }, [productsChosen]);

  let handleChangeQuantity = (id, value) => {
    productsChosen.find((pro) => pro.product_id === id).quantity = value;
    setProductsChosen([...productsChosen]);

    setTotal(
      productsChosen.reduce(
        (sum, product) =>
          product.product_id !== id
            ? sum + product.price * product.quantity
            : sum + product.price * value,
        0
      )
    );
  };

  return (
    <div className="container-cart">
      <button className="btn-back" onClick={() => navigate(-1)}>
        <i className="fa-solid fa-arrow-left icon-back"></i>
      </button>
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
            <span style={{ marginLeft: "50px", fontSize: "18px" }}>
              Số lượng:
              <input
                value={pro.quantity}
                type="number"
                name=""
                id=""
                className="quantity-product-chosen"
                min={0}
                onChange={(e) =>
                  handleChangeQuantity(pro.product_id, e.target.value)
                }
              />
            </span>
          </div>
        </div>
      ))}
      <span className="total" style={{ fontSize: "30px", paddingRight: "200px" }}>
        {`Tổng : ${new Intl.NumberFormat("vi-VN", {
          style: "currency",
          currency: "VND",
        }).format(total)}`}
      </span>
      <div className="payment" style={{ marginTop: "20px" }}>
        <button
          className="btn-payment"
          onClick={() =>
            navigate("/payment", {
              state: {
                productsChosen: productsChosen,
                customer: customer,
              },
            })
          }
        >
          Thanh Toán
        </button>
      </div>
    </div>
  );
};

export default Cart;
