package ru.sber.bootcamp.model_DAO.repository;

import ru.sber.bootcamp.model_DAO.entity.Account;

import java.math.BigInteger;
import java.util.List;

public interface AccountRepository {

    List<Account> findAll();
    Account getById(Long accountNumber);

    Account getAccountByCardNumber(Long cardNumber);

    void updateAccount(Account account);
}
