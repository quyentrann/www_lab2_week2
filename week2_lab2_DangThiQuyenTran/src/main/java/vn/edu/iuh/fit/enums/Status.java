package vn.edu.iuh.fit.enums;

public enum Status {
    ACTIVE(1),
    DEACTIVE(0),
    DELETE(-1);

    private int status;

    Status(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Status{" +
                "status=" + status +
                '}';
    }
}
