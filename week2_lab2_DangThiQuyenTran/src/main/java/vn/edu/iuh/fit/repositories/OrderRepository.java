package vn.edu.iuh.fit.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import vn.edu.iuh.fit.db.ConnectionDB;
import vn.edu.iuh.fit.entities.RequestOrderDate;
import vn.edu.iuh.fit.entities.ResponseOrderByDateBetween;
import vn.edu.iuh.fit.models.Customer;
import vn.edu.iuh.fit.models.Order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
    private final EntityManagerFactory entityManagerFactory;

    public OrderRepository() {
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

    public boolean createOrder(Order order) {
        return performTransaction(entityManager -> entityManager.persist(order));
    }

    public boolean updateOrder(Order order) {
        return performTransaction(entityManager -> entityManager.merge(order));
    }

    public boolean deleteOrder(long orderId) {
        return performTransaction(entityManager -> {
            Order order = entityManager.find(Order.class, orderId);
            entityManager.remove(order);
        });
    }

    public Order getOrderById(long orderId) {
        return performQuery(entityManager -> entityManager.find(Order.class, orderId));
    }

    public List<Order> getAllOrders() {
        return performQuery(entityManager -> {
            Query query = entityManager.createQuery("SELECT o FROM Order o");
            return query.getResultList();
        });
    }

    public Order getOrderByOrder(Order or) {
        return performQuery(entityManager -> {
            Query query = entityManager.createNativeQuery("SELECT * FROM orders WHERE order_date = ? AND cust_id = ? AND emp_id = ?", Order.class);
            query.setParameter(1, or.getOrderDate());
            query.setParameter(2, or.getCustomer().getCustId());
            query.setParameter(3, or.getEmployee().getEmpId());

            return (Order) query.getSingleResult();
        });
    }

    public long getOrderId() {
        return performQuery(entityManager -> {
            Query query = entityManager.createNativeQuery("SELECT order_id FROM orders ORDER BY order_id DESC LIMIT 1");
            Object result = query.getSingleResult();
            return result != null ? ((Number) result).longValue() : 0;
        });
    }

    public List<ResponseOrderByDateBetween> getOrderByDateBetween(RequestOrderDate requestOrderDate) {
        return performQuery(entityManager -> {
            String queryString = "SELECT od.order_detail_id, p.name, o.order_date, SUM(od.price) AS prices, SUM(od.quantity) AS quantity";
            queryString += (requestOrderDate.getEmpID() == 0) ? " FROM orders AS o" : ", e.emp_id FROM orders AS o INNER JOIN employee AS e ON o.emp_id = e.emp_id";
            queryString += " INNER JOIN order_detail AS od ON o.order_id = od.order_id INNER JOIN product AS p ON p.product_id = od.product_id";
            queryString += " WHERE " + ((requestOrderDate.getEmpID() == 0) ? "" : "e.emp_id = ? AND ") + "order_date BETWEEN ? AND ?";
            queryString += " GROUP BY p.name, o.order_date";
            queryString += " ORDER BY o.order_date";

            Query query = entityManager.createNativeQuery(queryString);

            int parameterIndex = 1;
            if (requestOrderDate.getEmpID() != 0) {
                query.setParameter(parameterIndex++, requestOrderDate.getEmpID());
            }
            query.setParameter(parameterIndex++, requestOrderDate.getFromDate());
            query.setParameter(parameterIndex, requestOrderDate.getToDate());

            List<Object[]> objects = query.getResultList();
            List<ResponseOrderByDateBetween> responseOrderByDateBetweens = new ArrayList<>();
            for (Object[] o : objects) {
                ResponseOrderByDateBetween responseOrderByDateBetween = new ResponseOrderByDateBetween((long) o[0], o[1] + "", o[2] + "", (double) o[3], (BigDecimal) o[4]);
                responseOrderByDateBetweens.add(responseOrderByDateBetween);
            }
            return responseOrderByDateBetweens;
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
