package vn.edu.iuh.fit.models;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "accounts")
public class Account implements Serializable {
    @Id
    @Column(name = "account_id")
    private long accountId;
    private String email;
    private String password;
    private String role;
    @OneToOne
    @JoinColumn(name = "cust_id")
    private Customer customer;

    public Account() {
    }

    public Account(long accountId, String email, String password, String role, Customer customer) {
        this.accountId = accountId;
        this.email = email;
        this.password = password;
        this.role = role;
        this.customer = customer;
    }

    public Account(long accountId, String email, String password, String role) {
        this.accountId = accountId;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Account(long accountId, String email, String password) {
        this.accountId = accountId;
        this.email = email;
        this.password = password;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", customer=" + customer +
                '}';
    }
}
