package vn.edu.iuh.fit.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import vn.edu.iuh.fit.db.ConnectionDB;
import vn.edu.iuh.fit.entities.InformationProduct;
import vn.edu.iuh.fit.models.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class ProductRepository {
    private final EntityManagerFactory entityManagerFactory;

    public ProductRepository() {
        entityManagerFactory = ConnectionDB.getInstance().getEntityManagerFactory();
    }

    private boolean performTransaction(Consumer<EntityManager> action) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            action.accept(entityManager);
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

    private <T> T performQuery(Function<EntityManager, T> action) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return action.apply(entityManager);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return null;
    }

    public boolean createProduct(Product product) {
        return performTransaction(entityManager -> entityManager.persist(product));
    }

    public boolean updateProduct(Product product) {
        return performTransaction(entityManager -> entityManager.merge(product));
    }

    public boolean deleteProduct(long productId) {
        return performTransaction(entityManager -> {
            Product product = entityManager.find(Product.class, productId);
            entityManager.remove(product);
        });
    }

    public Product getProductById(long productId) {
        return performQuery(entityManager -> entityManager.find(Product.class, productId));
    }

    public List<Product> getAllProducts() {
        return performQuery(entityManager -> {
            Query query = entityManager.createQuery("SELECT p FROM Product p");
            return query.getResultList();
        });
    }

    public List<InformationProduct> getInfoProduct() {
        return performQuery(entityManager -> {
            String queryString = "SELECT p.product_id, p.name, pm.path, pr.price FROM product AS p " +
                    "INNER JOIN product_image AS pm ON p.product_id = pm.product_id " +
                    "INNER JOIN (SELECT price_id, note, price, price_date_time, product_id " +
                    "FROM ( SELECT *, ROW_NUMBER() OVER (PARTITION BY product_id ORDER BY price_date_time DESC) AS rn " +
                    "FROM product_price ) AS subquery WHERE rn = 1 ) AS pr ON p.product_id = pr.product_id";

            Query query = entityManager.createNativeQuery(queryString);
            List<Object[]> results = query.getResultList();

            List<InformationProduct> informationProducts = new ArrayList<>();
            for (Object[] result : results) {
                long productId = (long) result[0];
                String productName = (String) result[1];
                String imagePath = (String) result[2];
                double price = (double) result[3];

                InformationProduct informationProduct = new InformationProduct(productId, productName, imagePath, price);
                informationProducts.add(informationProduct);
            }

            return informationProducts;
        });
    }

    public List<Object[]> getAllNameOfProductOrdered() {
        return performQuery(entityManager -> {
            String queryString = "SELECT pr.name FROM order_detail AS od INNER JOIN product AS pr ON od.product_id = pr.product_id GROUP BY pr.name ORDER BY pr.name";

            Query query = entityManager.createNativeQuery(queryString);
            return query.getResultList();
        });
    }
}
