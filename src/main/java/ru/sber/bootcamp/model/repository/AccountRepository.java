package ru.sber.bootcamp.model.repository;

import ru.sber.bootcamp.model.entity.Account;

import java.math.BigInteger;
import java.util.List;

public interface AccountRepository {

    List<Account> findAll();
    Account getById();
    void balanceIncrement(BigInteger amount);
    void balanceDecrement(BigInteger amount);
}
