package vn.edu.iuh.fit.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import vn.edu.iuh.fit.db.ConnectionDB;
import vn.edu.iuh.fit.entities.Login;
import vn.edu.iuh.fit.models.Account;

import java.util.List;
import java.util.Optional;

public class AccountRepository {
    private final EntityManagerFactory entityManagerFactory;

    public AccountRepository() {
        entityManagerFactory = ConnectionDB.getInstance().getEntityManagerFactory();
    }

    public boolean createAccount(Account account) {
        return performTransaction(entityManager -> entityManager.persist(account));
    }

    public boolean updateAccount(Account account) {
        return performTransaction(entityManager -> entityManager.merge(account));
    }

    public boolean deleteAccount(long accountId) {
        return performTransaction(entityManager -> {
            Account account = entityManager.find(Account.class, accountId);
            entityManager.remove(account);
        });
    }

    public Account getAccountById(long accountId) {
        return performQuery(entityManager -> entityManager.find(Account.class, accountId));
    }

    public List<Account> getAllAccounts() {
        return performQuery(entityManager -> {
            Query query = entityManager.createQuery("SELECT a FROM Account a");
            return query.getResultList();
        });
    }

    public long getAccountId() {
        return performQuery(entityManager -> {
            Query query = entityManager.createNativeQuery("SELECT account_id FROM accounts ORDER BY account_id DESC LIMIT 1");
            Object result = query.getSingleResult();
            return result != null ? ((Number) result).longValue() : 0;
        });
    }

    public boolean checkAccountExist(String email) {
        return performQuery(entityManager -> {
            Query query = entityManager.createNativeQuery("SELECT * FROM accounts WHERE email = ?", Account.class);
            query.setParameter(1, email);
            return query.getResultList().size() > 0;
        });
    }

    public Account login(Login login) {
        return performQuery(entityManager -> {
            Query query = entityManager.createNativeQuery("SELECT account_id, email, `password`, role FROM accounts WHERE email = ? AND `password` = ?");
            query.setParameter(1, login.getEmail());
            query.setParameter(2, login.getPassword());
            Object[] object = (Object[]) query.getSingleResult();
            return new Account((Long) object[0], object[1] + "", object[2] + "", object[3] + "");
        });
    }

    public List<Account> getAccountsByEmail(String email) {
        return performQuery(entityManager -> {
            if (email.equalsIgnoreCase("all")) {
                return getAllAccounts();
            } else {
                Query query = entityManager.createQuery("SELECT a FROM Account a WHERE a.email LIKE :email", Account.class);
                query.setParameter("email", "%" + email + "%");
                return query.getResultList();
            }
        });
    }

    public long getQuantityAccount() {
        return performQuery(entityManager -> {
            Query query = entityManager.createNativeQuery("SELECT COUNT(*) FROM accounts");
            Object result = query.getSingleResult();
            return result != null ? ((Number) result).longValue() : 0;
        });
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

    @FunctionalInterface
    private interface TransactionFunction {
        void apply(EntityManager entityManager);
    }

    @FunctionalInterface
    private interface QueryFunction<T> {
        T apply(EntityManager entityManager);
    }
}
