import { useEffect, useState } from "react";
import "../styles.scss";
import axios from "axios";
import { ToastContainer, toast } from "react-toastify";

const Account = () => {
  const [id, setId] = useState();
  const [email, setEmail] = useState();
  const [password, setPassword] = useState();
  const [role, setRole] = useState("user");
  const [customer, setCustomer] = useState();
  const [emailSearch, setEmailSearch] = useState("");

  const [customersNotAccount, setCustomersNotAccount] = useState([]);
  const [isClickIU, setIsClickIU] = useState(false);
  const [valueBtnIU, setValueBtnIU] = useState("insert");
  const [accounts, setAccounts] = useState([]);
  const [numberPage, setNumberPage] = useState([]);
  const [quantityNextPage, setQuantityNextPage] = useState(0);
  const [pageChosen, setPageChosen] = useState(1);

  useEffect(() => {
    let getApiQuantityAccounts = async () => {
      let datas = await axios.get(
        "http://localhost:8080/api/accounts/get-quantity-accounts"
      );
      setNumberPage(
        Array.from(
          { length: Math.ceil(datas.data / 10) },
          (_, index) => index + 1
        )
      );
    };
    getApiQuantityAccounts();
  }, [isClickIU]);

  useEffect(() => {
    let getApiCustomers = async () => {
      let datas = await axios.get(
        "http://localhost:8080/api/customers/customers-have-not-account"
      );
      setCustomersNotAccount(datas.data);
      setCustomer(datas.data[0]);
    };
    getApiCustomers();
  }, [isClickIU]);

  useEffect(() => {
    let getApiAccountId = async () => {
      let datas = await axios.get("http://localhost:8080/api/accounts/getID");
      setId(datas.data + 1);
    };
    getApiAccountId();
  }, [isClickIU]);

  useEffect(() => {
    let getApiAccounts = async () => {
      let datas = await axios.get("http://localhost:8080/api/accounts");
      setAccounts(datas.data);
    };
    getApiAccounts();
  }, [isClickIU]);

  let handleClickBtnIU = async () => {
    if (valueBtnIU === "insert") {
      await axios.post("http://localhost:8080/api/accounts", {
        accountId: id,
        email: email,
        password: password,
        role: role,
        customer: customer,
      });
      setEmail("");
      setPassword("");
      setIsClickIU(!isClickIU);
      toast.success("🦄 Thêm Thành Công!!!", {
        position: "top-right",
        autoClose: 5000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
        theme: "light",
      });
    } else {
      await axios.put(`http://localhost:8080/api/accounts/${id}`, {
        accountId: id,
        email: email,
        password: password,
        role: role,
        customer: customer,
      });
      setEmail("");
      setPassword("");
      setIsClickIU(!isClickIU);
      setValueBtnIU("insert");
      toast.success("🦄 Cập Nhật Thành Công!!!", {
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

  let handleClickBtnInsert = () => {
    setValueBtnIU("insert");
    setEmail("");
    setPassword("");
    setIsClickIU(!isClickIU);
  };

  let handleChangeSearch = async (email) => {
    setEmailSearch(email);
    let datas = await axios.get(
      `http://localhost:8080/api/accounts/account-by-email/${email ? email : "all"}`
    );
    setAccounts(datas.data);
  };

  let handleClickBtnDelete = async () => {
    await axios.delete(`http://localhost:8080/api/accounts/${id}`);
    toast.success("🦄 Xóa Thành Công!!!", {
      position: "top-right",
      autoClose: 5000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
      theme: "light",
    });
    setEmail("");
    setPassword("");
    setIsClickIU(!isClickIU);
  };

  let handleClickBtnRow = (a) => {
    setId(a.accountId);
    setEmail(a.email);
    setPassword(a.password);
    setRole(a.role);
    setCustomer(a.customer);
    setValueBtnIU("Cập Nhật");
  };

  let handleClickBackPage = () => {
    if (quantityNextPage !== 0) {
      setQuantityNextPage((prev) => prev - 1);
    }
  };

  let handleClickNextPage = () => {
    if (quantityNextPage < numberPage.length) {
      setQuantityNextPage((prev) => prev + 1);
    }
  };

  let handleClickNumberPage = (number) => {
    setPageChosen(number);
  };

  return (
    <div className="containers">
      <h1>QUẢN LÝ TÀI KHOẢN NHÂN VIÊN</h1>
      <div className="content-form">
        <div className="btns-crud">
         
          <div className="btn-r" style={{alignItems: "flex-end", marginTop: "30px"}}>
            <input
              type="text"
              className="input-search"
              placeholder="Nhập email tìm kiếm..."
              value={emailSearch}
              onChange={(e) => handleChangeSearch(e.target.value)}
            />
          </div>
        </div>
        <div className="form-crud">
          <div className="form">
            <div className="form-content">
              <label htmlFor="id" className="lbl-form">
                ID
              </label>
              <input
                type="number"
                className="input-form"
                style={{ width: "100px" }}
                value={id}
                onChange={(e) => setId(e.target.value)}
                disabled
              />
            </div>
            <div className="form-content">
              <label htmlFor="id" className="lbl-form">
                Email
              </label>
              <input
                type="email"
                className="input-form"
                style={{ width: "250px" }}
                value={email}
                onChange={(e) => setEmail(e.target.value)}
              />
            </div>
            <div className="form-content">
              <label htmlFor="id" className="lbl-form">
                Password
              </label>
              <input
                type="password"
                className="input-form"
                style={{ width: "250px" }}
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </div>
            <div className="form-content">
              <label htmlFor="id" className="lbl-form">
                Role
              </label>
              <select
                name=""
                id=""
                style={{ width: "100px", height: "30px" }}
                value={role}
                onChange={(e) => setRole(e.target.value)}
              >
                <option value="admin">Admin</option>
                <option value="user">User</option>
              </select>
            </div>
            <div className="form-content">
              <label htmlFor="id" className="lbl-form">
                Customer
              </label>
              <select
                name=""
                id=""
                style={{ width: "200px", height: "30px" }}
                value={customer}
                onChange={(e) => setCustomer(e.target.value)}
                disabled={valueBtnIU !== "insert"}
              >
                {customer !== undefined && (
                  <option
                  key={customer.custId}
                  value={customer}
                  >{`${customer.custId}-${customer.custName}`}</option>
                )}
                {customersNotAccount.map(
                  (c) =>
                    c.custId !== customer.custId && (
                      <option
                        key={c.custId}
                        value={c}
                      >{`${c.custId}-${c.custName}`}</option>
                    )
                )}
              </select>
            </div>
          </div>
          <div className="btn-insert-update">
            <button
              className="btnIU"
              onClick={handleClickBtnIU}
              style={{
                background:
                  valueBtnIU === "insert"
                    ? ""
                    : "",
              }}
            >
              {valueBtnIU === "insert" ? "Thêm" : "Cập Nhật"}
            </button>
          </div>
        </div>
      </div>
      <div className="content-table">
        <table className="table-accounts">
          <thead className="thead-accounts">
            <tr className="tr">
              <th className="th column-id">ID</th>
              <th className="th column-email">EMAIL</th>
              <th className="th column-password">PASSWORD</th>
              <th className="th column-role">ROLE</th>
              <th className="th column-customer">CUSTOMER</th>
            </tr>
          </thead>
          <tbody className="tbody-accounts">
            {accounts.map((a) => (
              <tr
                key={a.accountId}
                className="tr"
                onClick={() => handleClickBtnRow(a)}
              >
                <td className="td column-id">{a.accountId}</td>
                <td className="td column-email">{a.email}</td>
                <td className="td column-password">{a.password}</td>
                <td className="td column-role">{a.role}</td>
                <td className="td column-customer">{`${a.customer.custId} - ${a.customer.custName}`}</td>
              </tr>
            ))}
          </tbody>
        </table>
        
      </div>
      <ToastContainer />
    </div>
  );
};

export default Account;
