package ru.sber.bootcamp.modelDao.repository;

import ru.sber.bootcamp.modelDao.entity.Account;

import java.util.List;

public interface AccountRepository {

    List<Account> findAll();

    Account getAccountByCardNumber(Long cardNumber);

    int updateAccount(Account account);

    Account getAccountById(long id);
}
