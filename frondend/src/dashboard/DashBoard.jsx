import { useState } from 'react';
import "../dashboard/dashboard.scss";
import { Link, Outlet } from 'react-router-dom';

const DashBoard = () => {
    const [navBar, setNavBar] = useState([
        {
            id: "",
            nav: "Thống kê"
        },
        {
            id: "account",
            nav: "Quản lý nhân viên"
        },
        {
            id: "customer",
            nav: "Quản lý khách hàng"
        },
       
    ]);

    return (
        <div className='dashboard'>
            <div className='nav-bar-dashboard'>
                <div className='nav-bar'>
                    {navBar.map(nav => (
                        <Link to={nav.id} key={nav.id} className='nav'>
                            {nav.nav}
                        </Link>
                    ))}
                </div>
            </div>
            <div className='container-dashboard'>
                <Outlet />
            </div>
        </div>
    );
};

export default DashBoard;
