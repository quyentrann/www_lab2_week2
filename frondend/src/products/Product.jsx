import "../products/product.scss";

const Product = ({ key, product, chooseProduct }) => {
  // const navigate = useNavigate();

  const handleBuyNow = () => {
    navigate("/payment", {
      state: {
        productsChosen: productsChosen,
        customer: customer,
      },
    })
  };
  return (
    <div className="container-product" key={key}>
      <div className="image-product">
        <img src={`/${product.path}`} alt="" className="image" />
      </div>
      <span className="name-product" style={{}}>{product.name}</span>
      <span className="price-product" style={{paddingRight: "420px", marginTop: "20px"}}>
        {new Intl.NumberFormat("vi-VN", {
          style: "currency",
          currency: "VND",
        }).format(product.price)}
      </span>
     <div className="pd" style={{justifyContent: "flex-end"}}>
     <button className="btn-insert-into-cart" onClick={handleBuyNow} >Mua</button>
     <button className="btn-insert-into-cart" onClick={() => chooseProduct(product)}>Thêm Vào Giỏ Hàng</button>
     </div>
    </div>
  );
};

export default Product;
