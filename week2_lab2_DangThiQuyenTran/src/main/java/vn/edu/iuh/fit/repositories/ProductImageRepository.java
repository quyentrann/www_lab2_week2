package vn.edu.iuh.fit.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import vn.edu.iuh.fit.db.ConnectionDB;
import vn.edu.iuh.fit.models.ProductImage;

import java.util.List;

public class ProductImageRepository {
    private final EntityManagerFactory entityManagerFactory;

    public ProductImageRepository() {
        entityManagerFactory = ConnectionDB.getInstance().getEntityManagerFactory();
    }

    private boolean performTransaction(TransactionFunction function) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            function.apply(entityManager);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return false;
    }

    private <T> T performQuery(QueryFunction<T> function) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return function.apply(entityManager);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return null;
    }

    public boolean createProductImage(ProductImage productImage) {
        return performTransaction(entityManager -> entityManager.persist(productImage));
    }

    public boolean updateProductImage(ProductImage productImage) {
        return performTransaction(entityManager -> entityManager.merge(productImage));
    }

    public boolean deleteProductImage(long imageId) {
        return performTransaction(entityManager -> {
            ProductImage productImage = entityManager.find(ProductImage.class, imageId);
            entityManager.remove(productImage);
        });
    }

    public ProductImage getProductImageById(long imageId) {
        return performQuery(entityManager -> entityManager.find(ProductImage.class, imageId));
    }

    public List<ProductImage> getAllProductImages() {
        return performQuery(entityManager -> {
            Query query = entityManager.createQuery("SELECT p FROM ProductImage p");
            return query.getResultList();
        });
    }

    @FunctionalInterface
    private interface TransactionFunction {
        void apply(EntityManager entityManager);
    }

    @FunctionalInterface
    private interface QueryFunction<T> {
        T apply(EntityManager entityManager);
    }
}
