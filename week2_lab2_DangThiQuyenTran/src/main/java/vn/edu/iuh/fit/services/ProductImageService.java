package vn.edu.iuh.fit.services;

import vn.edu.iuh.fit.models.ProductImage;
import vn.edu.iuh.fit.repositories.ProductImageRepository;
import java.util.List;

public class ProductImageService {
    private final ProductImageRepository productImageRepository;

    public ProductImageService() {
        productImageRepository = new ProductImageRepository();
    }

    public boolean createProductImage(ProductImage productImage) {
        return productImageRepository.createProductImage(productImage);
    }

    public boolean updateProductImage(ProductImage productImage) {
        return productImageRepository.updateProductImage(productImage);
    }

    public boolean deleteProductImage(long imageId) {
        return productImageRepository.deleteProductImage(imageId);
    }

    public ProductImage getProductImageById(long imageId) {
        return productImageRepository.getProductImageById(imageId);
    }

    public List<ProductImage> getAllProductImages() {
        return productImageRepository.getAllProductImages();
    }
}