package ru.sber.bootcamp.modelDao.repository;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.sber.bootcamp.modelDao.entity.Account;
import ru.sber.bootcamp.service.DataConnectionService;
import ru.sber.bootcamp.service.H2ConnectionServiceImpl;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class AccountRepositoryTest {
    static DataConnectionService dataConnectionService;
    static AccountRepository accountRepository;


    @BeforeClass
    public static void init() {
        dataConnectionService = new H2ConnectionServiceImpl(false);
        accountRepository = new AccountRepoImpl(dataConnectionService);
        dataConnectionService.start();

    }

    @Test
    public void findAll() {
        List<Account> accountsTest = new ArrayList<>();
        accountsTest.add( new Account(1L,"1111", BigDecimal.valueOf(10000.10), Date.valueOf("2020-01-01")));
        accountsTest.add( new Account(2L,"1112", BigDecimal.valueOf(2000.25), Date.valueOf("2020-01-01")));
        List<Account> accounts = accountRepository.findAll();
        Assert.assertEquals(accounts,accountsTest);
    }

    @Test
    public void getAccountByCardNumber() {
        Account accountTest = new Account(2l,"1112",BigDecimal.valueOf(2000.25), Date.valueOf("2020-01-01"));
        Account account = accountRepository.getAccountByCardNumber("1112222233334441");
        Assert.assertEquals(account,accountTest);
    }

    @Test
    public void updateAccount() {
        Account accountTest = new Account(1l,"1111",BigDecimal.valueOf(100010.10), Date.valueOf("2020-01-01"));
        Account accountBefore = accountRepository.getAccountById(1L);
        int result = accountRepository.updateAccount(accountTest);
        Account accountAfter = accountRepository.getAccountById(1L);
        System.out.println(accountBefore);
        System.out.println("PS result:" + result);
        System.out.println(accountAfter);
        Assert.assertNotEquals(accountAfter,accountBefore);
    }

    @AfterClass
    public static void stop(){
        dataConnectionService.stop();
    }
}