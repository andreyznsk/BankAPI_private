package ru.sber.bootcamp.modelDao.entity;


import java.util.Objects;

public class Client {
    private Long id;
    private String accountId;
    private String firstName;
    private String lastname;
    private Long phoneNumber;
    private Account account;

    public Client(){
    }

    public Client(Long id, String accountId, String firstName, String lastname, Long phoneNumber, Account account) {
        this.id = id;
        this.accountId = accountId;
        this.firstName = firstName;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", firstName='" + firstName + '\'' +
                ", lastname='" + lastname + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", account=" + account +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id) && Objects.equals(accountId, client.accountId) && Objects.equals(firstName, client.firstName) && Objects.equals(lastname, client.lastname) && Objects.equals(phoneNumber, client.phoneNumber) && Objects.equals(account, client.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountId, firstName, lastname, phoneNumber, account);
    }
}
