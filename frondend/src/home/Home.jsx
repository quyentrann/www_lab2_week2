import { useEffect, useState } from "react"
import "../home/home.scss"
import Product from "../products/Product"
import axios from "axios"
import { useLocation, useNavigate } from "react-router-dom"

const Home = () => {
    const [products, setProducts] = useState([])
    const [productsChosen, setProductsChosen] = useState([])
    const [customer, setCustomer] = useState()

    let location = useLocation()
    let navigate = useNavigate()

    useEffect(() => {
        let getApiProducts = async() => {
            let datas = await axios.get("http://localhost:8080/api/products/getInfoProduct");
            setProducts(datas.data)
            setCustomer(location.state.customer)
        }
        getApiProducts()
    }, [JSON.stringify(products)])

    let chooseProduct = (product) => {
        let proExist = productsChosen.find(pro => pro.product_id === product.product_id)
        if(!proExist){
            setProductsChosen([...productsChosen, {...product, quantity : 1}])
        }
        else{
            proExist.quantity += 1
            setProductsChosen([...productsChosen])
        }
    }

    let handleClickCart = () => {
        navigate("/cart", {state : {
            productsChosen : productsChosen,
            customer : customer
        }})
    }
  return (
    <div className='container-home'>
        <text style={{paddingLeft: '1000px', color: "green", fontSize: "50px", backgroundColor: "#f7afaf", height: "200px", alignItems: "center", justifyContent: "center", fontWeight:"bold", paddingTop: "50px"}}>WELCOM TO TRENDY SHOP</text>
        <div className='cart'>
            <i className="fa-solid fa-cart-shopping icon-cart" onClick={handleClickCart}>
                <span className="quantity-product-chosen">{productsChosen.length}</span>
            </i>
           
        </div>
        <div className='content-products'>
            {
                products.map(product => <Product key={product.productId} 
                product={product} chooseProduct={chooseProduct}/>)
            }
        </div>
    </div>
  )
}

export default Home