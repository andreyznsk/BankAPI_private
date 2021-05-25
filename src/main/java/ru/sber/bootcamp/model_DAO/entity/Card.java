package ru.sber.bootcamp.model_DAO.entity;

import java.util.Date;
import java.util.Objects;

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
