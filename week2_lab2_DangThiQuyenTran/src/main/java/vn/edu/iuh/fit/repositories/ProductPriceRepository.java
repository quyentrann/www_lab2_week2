package vn.edu.iuh.fit.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import vn.edu.iuh.fit.db.ConnectionDB;
import vn.edu.iuh.fit.models.ProductPrice;

import java.util.List;

public class ProductPriceRepository {
    private final EntityManagerFactory entityManagerFactory;

    public ProductPriceRepository() {
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

    public boolean createProductPrice(ProductPrice productPrice) {
        return performTransaction(entityManager -> entityManager.persist(productPrice));
    }

    public boolean updateProductPrice(ProductPrice productPrice) {
        return performTransaction(entityManager -> entityManager.merge(productPrice));
    }

    public boolean deleteProductPrice(long productPriceId) {
        return performTransaction(entityManager -> {
            ProductPrice productPrice = entityManager.find(ProductPrice.class, productPriceId);
            entityManager.remove(productPrice);
        });
    }

    public ProductPrice getProductPriceById(long productPriceId) {
        return performQuery(entityManager -> entityManager.find(ProductPrice.class, productPriceId));
    }

    public List<ProductPrice> getAllProductPrices() {
        return performQuery(entityManager -> {
            Query query = entityManager.createQuery("SELECT pp FROM ProductPrice pp");
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
