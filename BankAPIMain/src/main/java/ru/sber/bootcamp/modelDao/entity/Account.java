package ru.sber.bootcamp.modelDao.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

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

    public void incBalance(Double amount) {
       this.balance =  this.balance.add(BigDecimal.valueOf(amount));
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", accountNumber=" + accountNumber +
                ", balance=" + balance +
                ", openDate=" + openDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id) && Objects.equals(accountNumber, account.accountNumber) && balance.compareTo(account.balance)==0
                && Objects.equals(openDate, account.openDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountNumber, balance, openDate);
    }
}
