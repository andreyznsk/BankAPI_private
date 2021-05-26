package ru.sber.bootcamp.model_DAO.repository;

import ru.sber.bootcamp.model_DAO.entity.Account;

import java.math.BigInteger;
import java.util.List;

public interface AccountRepository {

    List<Account> findAll();

    Account getAccountByCardNumber(Long cardNumber);

    int updateAccount(Account account);

    Account getAccountById(long id);
}
