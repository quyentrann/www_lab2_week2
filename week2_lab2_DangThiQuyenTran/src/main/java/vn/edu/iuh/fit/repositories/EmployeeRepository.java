package vn.edu.iuh.fit.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import vn.edu.iuh.fit.db.ConnectionDB;
import vn.edu.iuh.fit.models.Employee;

import java.util.List;

public class EmployeeRepository {
    private final EntityManagerFactory entityManagerFactory;

    public EmployeeRepository() {
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

    public boolean createEmployee(Employee employee) {
        return performTransaction(entityManager -> entityManager.persist(employee));
    }

    public boolean updateEmployee(Employee employee) {
        return performTransaction(entityManager -> entityManager.merge(employee));
    }

    public boolean deleteEmployee(long empId) {
        return performTransaction(entityManager -> {
            Employee employee = entityManager.find(Employee.class, empId);
            entityManager.remove(employee);
        });
    }

    public Employee getEmployeeById(long empId) {
        return performQuery(entityManager -> entityManager.find(Employee.class, empId));
    }

    public List<Employee> getAllEmployees() {
        return performQuery(entityManager -> {
            Query query = entityManager.createQuery("SELECT e FROM Employee e");
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
