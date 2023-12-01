import axios from "axios";
import { useEffect, useRef, useState } from "react";
import { Line, Pie, getElementAtEvent } from "react-chartjs-2";
import "bootstrap/dist/css/bootstrap.min.css";
import "./homePage.scss";

const HomePage = () => {
  const [statisticYear, setStatisticYear] = useState([]);
  const [statisticMonth, setStatisticMonth] = useState([{ name: "Tổng Doanh Thu Năm", months: [], prices: [] }]);
  const [monthChosen, setMonthChosen] = useState("");
  const statisticsYearRef = useRef();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get("http://localhost:8080/api/orderdetails/statisticOrderDetailOfMonthAndYear");
        const data = response.data;
        setStatisticYear(data.map(item => ({ name: item[1], months: Array.from(new Set(item.slice(2).map(date => new Date(date).getMonth() + 1))), prices: item.slice(2).map(date => date[0]) })));
      } catch (error) {
        console.error("Error fetching data: ", error);
      }
    };

    fetchData();
  }, []);

  const onClick = async (event) => {
    const monthIndex = getElementAtEvent(statisticsYearRef.current, event).length > 0 ? getElementAtEvent(statisticsYearRef.current, event)[0].index : null;

    if (monthIndex !== null) {
      const response = await axios.get(`http://localhost:8080/api/orderdetails/statisticOrderDetailOfMonthAndYearByMonth/${monthIndex + 1}`);
      const data = response.data;

      setMonthChosen(`Tổng Doanh Thu Tháng ${monthIndex + 1} : ${data.map(o => o[2]).reduce((total, value) => total + value, 0).toLocaleString('vi-VN', { style: 'currency', currency: 'VND' })}`);
      setStatisticMonth(data.map(item => ({ name: item[1], months: [monthIndex + 1], prices: [item[0]] })));
    } else {
      const response = await axios.get("http://localhost:8080/api/orderdetails/getTotalPricesOfYear");
      const data = response.data;

      setMonthChosen("");
      setStatisticMonth([{ name: "Tổng Doanh Thu Năm", months: [], prices: data }]);
    }
  };

  const optionsLine = {
    responsive: true,
    plugins: { legend: { position: "top" }, title: { display: true, text: "THỐNG KÊ DOANH THU NĂM" } },
  };

  const dataLine = {
    labels: Array.from({ length: 12 }, (_, index) => index + 1),
    datasets: statisticYear.map(data => ({
      label: data.name,
      data: Array.from({ length: 12 }, (_, index) => data.months.includes(index + 1) ? data.prices[data.months.indexOf(index + 1)] : 0),
      borderColor: "rgba(75,192,192,1)",
      backgroundColor: "rgba(75,192,192,0.2)",
    })),
  };

  const dataPie = {
    labels: statisticMonth.map(object => (statisticMonth.length > 1 ? `${object.name}` : object.name)),
    datasets: [{ label: "Số lượng ", data: statisticMonth.map(object => object.prices), backgroundColor: ["#FF6384", "#36A2EB", "#FFCE56"], borderColor: ["#FF6384", "#36A2EB", "#FFCE56"], borderWidth: 1 }],
  };

  return (
    <div className="container mt-4">
      <div className="row">
        <div className="col-lg-6">
          <div className="card">
            <div className="card-body">
              <h5 className="card-title">Thống kê doanh thu năm</h5>
              <Line options={optionsLine} data={dataLine} ref={statisticsYearRef} onClick={onClick} />
            </div>
          </div>
        </div>
        <div className="col-lg-6">
          <div className="card">
            <div className="card-body">
              <h5 className="card-title">Thống kê doanh thu tháng</h5>
              <span>{monthChosen}</span>
              <Pie data={dataPie} />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default HomePage;
