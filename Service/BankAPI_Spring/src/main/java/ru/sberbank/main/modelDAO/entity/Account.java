package ru.sberbank.main.modelDAO.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "account")
@Data
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "open_date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date openDate;

    @OneToOne(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "account_number",
            referencedColumnName = "account_number",
            insertable = false, updatable = false)
    private Client client;

    public Account(Long id, String accountNumber, BigDecimal balance, Date openDate, Client client) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.openDate = openDate;
        this.client = client;
    }

    public Account() {
    }
}
