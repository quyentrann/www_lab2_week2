import axios from "axios";
import "./statistic.scss";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  ArcElement,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";
import { useEffect, useState } from "react";
import { Line } from "react-chartjs-2";
// import faker from 'faker';

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

ChartJS.register(ArcElement, Tooltip, Legend);

const Statistic = () => {
  const [fromDate, setFromDate] = useState("2023-01-10");
  const [toDate, setToDate] = useState("2023-11-15");
  const [employeeChosen, setEmployeeChosen] = useState(0);
  const [employees, setEmployees] = useState([]);
  const [orderByDate, setOrderByDate] = useState([]);
  const [dateLabels, setDateLabels] = useState([]);

  useEffect(() => {
    let apiGetEmployyees = async () => {
      let datas = await axios.get("http://localhost:8080/api/employees");
      setEmployees(datas.data);
    };
    apiGetEmployyees();
  }, []);

  const getRandomColor = () => {
    const r = Math.floor(Math.random() * 256);
    const g = Math.floor(Math.random() * 256);
    const b = Math.floor(Math.random() * 256);
    const alpha = Math.random().toFixed(2);

    return `rgba(${r}, ${g}, ${b}, ${alpha})`;
  };

  const optionsLine = {
    responsive: true,
    plugins: {
      legend: {
        position: "top",
      },
      title: {
        display: true,
        text: "THỐNG KÊ DOANH THU THEO KHOẢNG THỜI GIAN",
      },
    },
  };

  const dataLine = {
    labels: dateLabels,
    datasets: orderByDate.map((data) => {
      let color = getRandomColor();
      return {
        label: data.name,
        data: data.prices,
        borderColor: color,
        backgroundColor: color,
      };
    }),
  };

  let handleClickStatistic = async () => {
    let datas = await axios.post(
      "http://localhost:8080/api/orders/orders-by-date-between",
      {
        empID: employeeChosen,
        fromDate: fromDate,
        toDate: toDate,
      }
    );
    setDateLabels(
      datas.data
        .map((o) => {
          let date = o.orderDate.split("-");
          return `${date[2]}-${date[1]}-${date[0]}`;
        })
        .filter((date, index, array) => array.indexOf(date) === index)
    );
    let dates = datas.data
      .map((o) => {
        let date = o.orderDate.split("-");
        return `${date[2]}-${date[1]}-${date[0]}`;
      })
      .filter((date, index, array) => array.indexOf(date) === index);
    let names = datas.data
      .map((o) => {
        return o.name;
      })
      .filter((name, index, array) => array.indexOf(name) === index);

    setOrderByDate(
      names.map(function (name) {
        return {
          name: name,
          prices: dates.map(function (date) {
            var price = 0;
            datas.data.forEach(function (data) {
              let dateFormat = data.orderDate.split("-");
              if (
                data.name === name &&
                `${dateFormat[2]}-${dateFormat[1]}-${dateFormat[0]}` === date
              ) {
                price = data.prices;
              }
            });
            return price;
          }),
        };
      })
    );
  };

  return (
    <div className="statistic-orders-employee">
      <div className="select-dates">
        <select
          value={employeeChosen}
          className="select-employee"
          onChange={(e) => setEmployeeChosen(e.target.value)}
        >
          <option value={0}>All</option>
          {employees.map((e) => (
            <option key={e.empId} value={e.empId}>
              {e.fullName}
            </option>
          ))}
        </select>
        From :{" "}
        <input
          type="date"
          value={fromDate}
          className="date"
          onChange={(e) => setFromDate(e.target.value)}
        />
        To :{" "}
        <input
          type="date"
          value={toDate}
          className="date"
          onChange={(e) => setToDate(e.target.value)}
        />
        <button className="statistic" onClick={handleClickStatistic}>
          Thống Kê
        </button>
      </div>
      <Line
        options={optionsLine}
        data={dataLine}
        height={"100%"}
        width={"330%"}
      />
    </div>
  );
};

export default Statistic;
