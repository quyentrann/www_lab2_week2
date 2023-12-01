package vn.edu.iuh.fit.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import vn.edu.iuh.fit.db.ConnectionDB;
import vn.edu.iuh.fit.models.OrderDetail;

import java.util.List;

public class OrderDetailRepository {
    private final EntityManagerFactory entityManagerFactory;

    public OrderDetailRepository() {
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

    public boolean createOrderDetail(OrderDetail orderDetail) {
        return performTransaction(entityManager -> entityManager.persist(orderDetail));
    }

    public boolean updateOrderDetail(OrderDetail orderDetail) {
        return performTransaction(entityManager -> entityManager.merge(orderDetail));
    }

    public boolean deleteOrderDetail(long orderDetailId) {
        return performTransaction(entityManager -> {
            OrderDetail orderDetail = entityManager.find(OrderDetail.class, orderDetailId);
            entityManager.remove(orderDetail);
        });
    }

    public OrderDetail getOrderDetailById(long orderDetailId) {
        return performQuery(entityManager -> entityManager.find(OrderDetail.class, orderDetailId));
    }

    public List<OrderDetail> getAllOrderDetails() {
        return performQuery(entityManager -> {
            Query query = entityManager.createQuery("SELECT od FROM OrderDetail od");
            return query.getResultList();
        });
    }

    public long getOrderDetailId() {
        return performQuery(entityManager -> {
            Query query = entityManager.createNativeQuery("SELECT order_detail_id FROM order_detail ORDER BY order_detail_id DESC LIMIT 1");
            Object result = query.getSingleResult();
            return result != null ? ((Number) result).longValue() : 0;
        });
    }

    public List<Object[]> statisticOrderDetailOfMonthAndYear() {
        return performQuery(entityManager -> {
            String queryString = "SELECT od.order_detail_id, pr.name, SUM(od.price) AS price, SUM(od.quantity) AS quantity, o.order_date\n" +
                    "FROM order_detail AS od \n" +
                    "INNER JOIN orders AS o ON o.order_id = od.order_id\n" +
                    "INNER JOIN product AS pr ON pr.product_id = od.product_id\n" +
                    "WHERE YEAR(o.order_date) = YEAR(CURRENT_DATE()) \n" +
                    "GROUP BY pr.name, MONTH(o.order_date), YEAR(o.order_date)\n" +
                    "ORDER BY pr.name, YEAR(o.order_date), MONTH(o.order_date)";

            Query query = entityManager.createNativeQuery(queryString);
            return query.getResultList();
        });
    }

    public List<Object[]> statisticOrderDetailOfMonthAndYearByMonth(int month) {
        return performQuery(entityManager -> {
            String queryString = "SELECT od.order_detail_id, pr.name, SUM(od.price) AS price, SUM(od.quantity) AS quantity, o.order_date\n" +
                    "FROM order_detail AS od \n" +
                    "INNER JOIN orders AS o ON o.order_id = od.order_id\n" +
                    "INNER JOIN product AS pr ON pr.product_id = od.product_id\n" +
                    "WHERE YEAR(o.order_date) = YEAR(CURRENT_DATE()) AND MONTH(o.order_date) = ?\n" +
                    "GROUP BY pr.name, MONTH(o.order_date), YEAR(o.order_date)\n" +
                    "ORDER BY pr.name, YEAR(o.order_date), MONTH(o.order_date)";

            Query query = entityManager.createNativeQuery(queryString);
            query.setParameter(1, month);
            return query.getResultList();
        });
    }

    public double getTotalPricesOfYear() {
        return performQuery(entityManager -> {
            String queryString = "SELECT SUM(od.price) AS price\n" +
                    "FROM order_detail AS od \n" +
                    "INNER JOIN orders AS o ON o.order_id = od.order_id\n" +
                    "INNER JOIN product AS pr ON pr.product_id = od.product_id\n" +
                    "WHERE YEAR(o.order_date) = YEAR(CURRENT_DATE()) \n" +
                    "GROUP BY YEAR(o.order_date)";

            Query query = entityManager.createNativeQuery(queryString);
            Object result = query.getSingleResult();
            return result != null ? ((Number) result).doubleValue() : 0.0;
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
