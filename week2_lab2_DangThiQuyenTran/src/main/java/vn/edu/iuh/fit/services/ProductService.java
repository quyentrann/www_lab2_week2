package vn.edu.iuh.fit.services;

import vn.edu.iuh.fit.entities.InformationProduct;
import vn.edu.iuh.fit.models.Product;
import vn.edu.iuh.fit.repositories.ProductRepository;
import java.util.List;

public class ProductService {
    private final ProductRepository productRepository;

    public ProductService() {
        productRepository = new ProductRepository();
    }

    public boolean createProduct(Product product) {
        return productRepository.createProduct(product);
    }

    public boolean updateProduct(Product product) {
        return productRepository.updateProduct(product);
    }

    public boolean deleteProduct(long productId) {
        return productRepository.deleteProduct(productId);
    }

    public Product getProductById(long productId) {
        return productRepository.getProductById(productId);
    }

    public List<Product> getAllProducts() {
        return productRepository.getAllProducts();
    }

    public List<InformationProduct> getInfoProduct(){
        return productRepository.getInfoProduct();
    }

    public List<Object[]> getAllNameOfProductOrdered(){
        return productRepository.getAllNameOfProductOrdered();
    }

}