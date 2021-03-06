package ru.sberbank.main.modelDAO.entity;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "client")
@Data
public class Client implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "account_number")
    private String accountId;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "phone_number")
    private Long phoneNumber;

   @OneToOne(mappedBy = "client",
   fetch = FetchType.EAGER)
    private Account account;

    public Client(Long id, String accountId, String firstName, String lastname, Long phoneNumber, Account account) {
        this.id = id;
        this.accountId = accountId;
        this.firstName = firstName;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
        this.account = account;
    }

    public Client() {
    }
}
