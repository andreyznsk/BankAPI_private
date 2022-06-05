package ru.sber.bootcamp.modelDao.entity;

import java.util.Date;
import java.util.Objects;

public class Card {
    private Long id;
    private String accountNumber;
    private String cardNumber;
    private Date dateValidThru;
    private int CVC_code;

    public Card() {
    }

    public Card(Long id, String accountNumber, String cardNumber, Date dateValidThru, int CVC_code) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.cardNumber = cardNumber;
        this.dateValidThru = dateValidThru;
        this.CVC_code = CVC_code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
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

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", accountNumber=" + accountNumber +
                ", cardNumber=" + cardNumber +
                ", dateValidThru=" + dateValidThru +
                ", CVC_code=" + CVC_code +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        Card card = (Card) o;
        return CVC_code == card.CVC_code && Objects.equals(cardNumber, card.cardNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardNumber, CVC_code);
    }
}
