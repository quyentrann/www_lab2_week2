import './App.css'
import Cart from './cart/Cart'
import Home from './home/Home'
import { BrowserRouter, Route, Routes } from 'react-router-dom'
import Payment from './payment/Payment'
import Login from './login/Login'
import SignUp from './signUp/SignUp'
import DashBoard from './dashboard/DashBoard'
import Account from './dashboard/account/Account'
import HomePage from './dashboard/homePage/HomePage'

function App() {

  return (
    <BrowserRouter>
      <Routes>
        <Route path='/' element={<Login />}/>
        <Route path='/sign-up' element={<SignUp />}/>
        <Route path='/dashboard' element={<DashBoard />}>
          <Route path='' element={<HomePage />}/>
          <Route path='account' element={<Account />}/>
        </Route>
        <Route path='/home' element={<Home />}/>
        <Route path='/cart' element={<Cart />}/>
        <Route path='/payment' element={<Payment />}/>
      </Routes>
    </BrowserRouter>
  )
}

export default App
