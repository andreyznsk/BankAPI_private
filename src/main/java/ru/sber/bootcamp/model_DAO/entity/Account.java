package ru.sber.bootcamp.model_DAO.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Account {

    private Long id;
    private Long accountNumber;
    private BigDecimal balance;
    private Date openDate;

    public Account() {
    }

    public Account(Long id, Long accountNumber, BigDecimal balance, Date openDate) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.openDate = openDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }
}
