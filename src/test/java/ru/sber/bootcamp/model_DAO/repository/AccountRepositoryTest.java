package ru.sber.bootcamp.model_DAO.repository;

import junit.framework.TestCase;
import junit.framework.TestResult;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.sber.bootcamp.configuration.DataBaseConfig;
import ru.sber.bootcamp.model_DAO.entity.Account;
import ru.sber.bootcamp.service.DataConnectionService;
import ru.sber.bootcamp.service.H2ConnectionServiceImpl;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AccountRepositoryTest {
    static DataConnectionService dataConnectionService;
    static AccountRepository accountRepository;


    @BeforeClass
    public static void init() {
        dataConnectionService = new H2ConnectionServiceImpl(DataBaseConfig.getConfig(),false);
        accountRepository = new AccountRepoImpl(dataConnectionService);
        //dataConnectionService.setDisableAutocommit();
        dataConnectionService.start();

    }

    @Test
    public void findAll() {
        List<Account> accountsTest = new ArrayList<>();
        accountsTest.add( new Account(1L,1111L, BigDecimal.valueOf(10000.00), Date.valueOf("2020-01-01")));
        accountsTest.add( new Account(2L,1112L, BigDecimal.valueOf(2000.00), Date.valueOf("2020-01-01")));
        List<Account> accounts = accountRepository.findAll();
        Assert.assertEquals(accounts,accountsTest);
    }

    @Test
    public void getAccountByCardNumber() {
        Account accountTest = new Account(2l,1112l,BigDecimal.valueOf(2000.00), Date.valueOf("2020-01-01"));
        Account account = accountRepository.getAccountByCardNumber(1112222233334441L);
        Assert.assertEquals(account,accountTest);
    }

    @Test
    public void updateAccount() {
        Account accountTest = new Account(1l,1111l,BigDecimal.valueOf(100010.00), Date.valueOf("2020-01-01"));
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