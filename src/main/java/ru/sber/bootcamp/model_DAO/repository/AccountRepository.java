package ru.sber.bootcamp.model_DAO.repository;

import ru.sber.bootcamp.model_DAO.entity.Account;

import java.math.BigInteger;
import java.util.List;

public interface AccountRepository {

    List<Account> findAll();
    Account getById(Long accountNumber);
    void balanceIncrement(BigInteger amount);
    void balanceDecrement(BigInteger amount);

    Account getAccountByCardNumber(Long cardNumber);
}
