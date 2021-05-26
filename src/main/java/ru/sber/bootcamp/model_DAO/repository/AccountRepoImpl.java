package ru.sber.bootcamp.model_DAO.repository;

import ru.sber.bootcamp.model_DAO.entity.Account;
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
    public Account getAccountByCardNumber(Long cardNumber) {
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
