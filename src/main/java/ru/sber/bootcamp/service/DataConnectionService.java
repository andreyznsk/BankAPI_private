package ru.sber.bootcamp.service;

import ru.sber.bootcamp.model.entity.Account;
import ru.sber.bootcamp.model.entity.Card;
import ru.sber.bootcamp.model.entity.Client;

import java.util.List;

public interface DataConnectionService {

//-------------Start & Stop methods------------
    void start();

    void stop();
//-------------Accounts methods--------------------
    List<Account> findAllAccuont();

    Account getAccountByAccountNumber(Long accountNumber);
//--------------Clients methods-------------------
    Client getClientByAccountNumber(Long id);

    List<Card> findAllCards();

    Card getCardByCardId(Long id);

    Card getCardByCardNumber(Long cardNumber);
}
