package vn.edu.iuh.fit.services;

import vn.edu.iuh.fit.models.ProductPrice;
import vn.edu.iuh.fit.repositories.ProductPriceRepository;
import java.util.List;

public class ProductPriceService {
    private final ProductPriceRepository productPriceRepository;

    public ProductPriceService() {
        productPriceRepository = new ProductPriceRepository();
    }

    public boolean createProductPrice(ProductPrice productPrice) {
        return productPriceRepository.createProductPrice(productPrice);
    }

    public boolean updateProductPrice(ProductPrice productPrice) {
        return productPriceRepository.updateProductPrice(productPrice);
    }

    public boolean deleteProductPrice(long priceId) {
        return productPriceRepository.deleteProductPrice(priceId);
    }

    public ProductPrice getProductPriceById(long priceId) {
        return productPriceRepository.getProductPriceById(priceId);
    }

    public List<ProductPrice> getAllProductPrices() {
        return productPriceRepository.getAllProductPrices();
    }
}