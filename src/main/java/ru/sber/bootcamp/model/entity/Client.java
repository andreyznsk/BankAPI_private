package ru.sber.bootcamp.model.entity;


public class Client {
    private Long id;
    private Long accountId;
    private String firstName;
    private String lastname;
    private Long phoneNumber;
    //private Account account;

    public Client(){
    }

    public Client(Long id, Long accountId, String firstName, String lastname, Long phoneNumber) {
        this.id = id;
        this.accountId = accountId;
        this.firstName = firstName;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
    }

   /* public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
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
}
