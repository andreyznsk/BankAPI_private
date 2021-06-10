package ru.sber.bootcamp.modelDao.repository;

import ru.sber.bootcamp.modelDao.entity.Account;
import ru.sber.bootcamp.service.DataConnectionService;

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
    public Account getAccountByCardNumber(String cardNumber) {
        return dataService.getAccountByCardNumber(cardNumber) ;
    }

    @Override
    public int updateAccount(Account account) {
        return dataService.updateAccount(account);
    }

    @Override
    public Account getAccountById(long id) {
        return dataService.getAccountById(id);
    }
}
