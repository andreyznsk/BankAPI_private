package ru.sber.bootcamp.model.repository;

import ru.sber.bootcamp.model.entity.Account;
import ru.sber.bootcamp.model.entity.Client;
import ru.sber.bootcamp.service.DataConnectionService;

import java.math.BigInteger;
import java.util.List;

public class AccountRepoImpl implements AccountRepository {

    private final DataConnectionService dataService;

    public AccountRepoImpl(DataConnectionService dataService) {
        this.dataService = dataService;
    }

    @Override
    public List<Account> findAll() {
        return dataService.findAllAccuont();
    }

    @Override
    public Account getById(Long accountNumber ) {
        return dataService.getAccountByAccountNumber( accountNumber);
    }

    @Override
    public void balanceIncrement(BigInteger amount) {

    }

    @Override
    public void balanceDecrement(BigInteger amount) {

    }
}
