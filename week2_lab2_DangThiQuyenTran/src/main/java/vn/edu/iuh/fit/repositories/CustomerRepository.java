package vn.edu.iuh.fit.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import vn.edu.iuh.fit.db.ConnectionDB;
import vn.edu.iuh.fit.models.Customer;

import java.util.List;

public class CustomerRepository {
    private final EntityManagerFactory entityManagerFactory;

    public CustomerRepository() {
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

    public boolean createCustomer(Customer customer) {
        return performTransaction(entityManager -> entityManager.persist(customer));
    }

    public boolean updateCustomer(Customer customer) {
        return performTransaction(entityManager -> entityManager.merge(customer));
    }

    public boolean deleteCustomer(long customerId) {
        return performTransaction(entityManager -> {
            Customer customer = entityManager.find(Customer.class, customerId);
            entityManager.remove(customer);
        });
    }

    public Customer getCustomerById(long customerId) {
        return performQuery(entityManager -> entityManager.find(Customer.class, customerId));
    }

    public List<Customer> getAllCustomers() {
        return performQuery(entityManager -> {
            Query query = entityManager.createQuery("SELECT c FROM Customer c");
            return query.getResultList();
        });
    }

    public Customer getCustomerByCust(Customer cust) {
        return performQuery(entityManager -> {
            Query query = entityManager.createNativeQuery("SELECT * FROM customer WHERE cust_name = ? AND email = ? AND phone = ? AND address = ?", Customer.class);
            query.setParameter(1, cust.getCustName());
            query.setParameter(2, cust.getEmail());
            query.setParameter(3, cust.getPhone());
            query.setParameter(4, cust.getAddress());
            return (Customer) query.getSingleResult();
        });
    }

    public long getCustomerId() {
        return performQuery(entityManager -> {
            Query query = entityManager.createNativeQuery("SELECT cust_id FROM customer ORDER BY cust_id DESC LIMIT 1");
            Object result = query.getSingleResult();
            return result != null ? ((Number) result).longValue() : 0;
        });
    }

    public Customer getCustomerByEmail(String email) {
        return performQuery(entityManager -> {
            Query query = entityManager.createNativeQuery("SELECT * FROM customer WHERE email = ?", Customer.class);
            query.setParameter(1, email);
            return (Customer) query.getSingleResult();
        });
    }

    public List<Object[]> getProductByCustId(long id) {
        return performQuery(entityManager -> {
            Query query = entityManager.createNativeQuery("SELECT p.name, prm.path, od.price, od.quantity, o.order_date FROM customer AS c INNER JOIN orders AS o ON c.cust_id = o.cust_id INNER JOIN order_detail AS od ON o.order_id = od.order_id INNER JOIN product AS p ON od.product_id = p.product_id INNER JOIN product_image AS prm ON p.product_id = prm.product_id WHERE c.cust_id = ? ORDER BY o.order_date DESC");
            query.setParameter(1, id);
            return query.getResultList();
        });
    }

    public List<Customer> getCustomersHaveNotAccount() {
        return performQuery(entityManager -> {
            Query query = entityManager.createNativeQuery("SELECT * FROM customer WHERE cust_id NOT IN (SELECT cust_id FROM accounts)", Customer.class);
            return (List<Customer>) query.getResultList();
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
