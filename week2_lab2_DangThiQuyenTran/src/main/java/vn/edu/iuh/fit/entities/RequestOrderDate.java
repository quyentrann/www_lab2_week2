package vn.edu.iuh.fit.entities;

public class RequestOrderDate {
    private String fromDate;
    private String toDate;
    private long empID;

    public RequestOrderDate(String fromDate, String toDate, long empID) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.empID = empID;
    }

    public RequestOrderDate() {
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public long getEmpID() {
        return empID;
    }

    public void setEmpID(long empID) {
        this.empID = empID;
    }

    @Override
    public String toString() {
        return "RequestOrderDate{" +
                "fromDate='" + fromDate + '\'' +
                ", toDate='" + toDate + '\'' +
                ", empID=" + empID +
                '}';
    }
}
