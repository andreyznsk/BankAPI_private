package ru.sber.bootcamp.service;

import ru.sber.bootcamp.exception.BankApiException;
import ru.sber.bootcamp.modelDao.entity.Account;
import ru.sber.bootcamp.modelDao.entity.Card;
import ru.sber.bootcamp.modelDao.entity.Client;

import java.util.List;

public interface DataConnectionService {

//-------------Start & Stop methods------------
    void start();

    void stop();
//-------------Accounts methods--------------------
    List<Account> findAllAccuont();

    Account getAccountByAccountNumber(String accountNumber);
//--------------Clients methods-------------------
    Client getClientByAccountNumber(String accountNumber) throws BankApiException;

    List<Card> findAllCards();

    Card getCardByCardId(Long id);
//----------------Card Methods------------------
    Card getCardByCardNumber(String cardNumber);

    List<Card> getAllCardByAccountNumber(String accountNumber) throws BankApiException;

    Account getAccountByCardNumber(String cardNumber);

    int updateAccount(Account account);

    int addCardByAccountNumber(Card card);

    Card getCardWithMaxNumber();

    void setDisableAutocommit();

    Account getAccountById(long id);

    boolean isCardExist(String cartNumber);
}
