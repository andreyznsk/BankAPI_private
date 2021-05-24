package ru.sber.bootcamp.model_DAO.entity;

import java.util.Date;

public class Card {
    private Long id;
    private Long accountNumber;
    private Long cardNumber;
    private Date dateValidThru;
    private int CVC_code;

    public Card() {
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

    public Long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Date getDateValidThru() {
        return dateValidThru;
    }

    public void setDateValidThru(Date dateValidThru) {
        this.dateValidThru = dateValidThru;
    }

    public int getCVC_code() {
        return CVC_code;
    }

    public void setCVC_code(int CVC_code) {
        this.CVC_code = CVC_code;
    }
}
