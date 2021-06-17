package ru.sberbank.main.modelDAO.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "card")
@Data
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "date_valid_thru")
    private Date dateValidThru;

    @Column(name = "cvc_code")
    private int CVC_code;

    public Card(Long id, String accountNumber, String cardNumber, Date dateValidThru, int CVC_code) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.cardNumber = cardNumber;
        this.dateValidThru = dateValidThru;
        this.CVC_code = CVC_code;
    }

    public Card() {
    }
}
