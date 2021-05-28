package ru.sber.bootcamp.service;

import ru.sber.bootcamp.model_DAO.entity.Account;
import ru.sber.bootcamp.model_DAO.entity.Card;
import ru.sber.bootcamp.model_DAO.entity.Client;

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
//----------------Card Methods------------------
    Card getCardByCardNumber(Long cardNumber);

    List<Card> getAllCardByAccountNumber(Long accountNumber);

    Account getAccountByCardNumber(Long cardNumber);

    int updateAccount(Account account);

    int addCardByAccountNumber(Card card);

    Card getCardWithMaxNumber();

    void setDisableAutocommit();

    Account getAccountById(long id);

    boolean isCardExist(Long cartNumber);
}
