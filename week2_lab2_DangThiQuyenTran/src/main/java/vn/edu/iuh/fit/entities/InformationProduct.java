package vn.edu.iuh.fit.entities;

public class InformationProduct {
    private long productId;
    private String name;
    private String path;
    private double price;

    public InformationProduct(long productId, String name, String path, double price) {
        this.productId = productId;
        this.name = name;
        this.path = path;
        this.price = price;
    }

    public long getProduct_id() {
        return productId;
    }

    public void setProduct_id(long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "InformationProduct{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", price=" + price +
                '}';
    }
}
