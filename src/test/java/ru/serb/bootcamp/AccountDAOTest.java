package ru.serb.bootcamp;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.sber.bootcamp.configuration.DataBaseConfig;
import ru.sber.bootcamp.model_DAO.entity.Account;
import ru.sber.bootcamp.model_DAO.entity.Card;
import ru.sber.bootcamp.model_DAO.entity.Client;
import ru.sber.bootcamp.model_DAO.repository.AccountRepoImpl;
import ru.sber.bootcamp.model_DAO.repository.AccountRepository;
import ru.sber.bootcamp.service.DataConnectionService;
import ru.sber.bootcamp.service.H2ConnectionServiceImpl;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;

public class AccountDAOTest {

    static DataConnectionService dataConnectionService;
    static AccountRepository accountRepository;
    @BeforeClass
    public static void init() {
        dataConnectionService = new H2ConnectionServiceImpl(DataBaseConfig.getConfig(),false);
        accountRepository = new AccountRepoImpl(dataConnectionService);
        dataConnectionService.setDisableAutocommit();
        dataConnectionService.start();

    }



   /* @Test
    public void getClientByAccountNumber(){
        Account testAccount = new Account(1L,1111L, BigDecimal.valueOf(10000.00),Date.valueOf("2020-01-01"));
        Client testClient = new Client(1L,1111L,"Ivan", "Ivanov",89008001234L,testAccount);
        System.out.println(testClient);
        Client client = dataConnectionService.getClientByAccountNumber(1111L);
        Assert.assertEquals(testClient,client);
    }

    @Test
    public void getClientByAccountNumberNull(){
        Client testClient = new Client();
        Client client = dataConnectionService.getClientByAccountNumber(0L);
        Assert.assertEquals(testClient,client);
    }

    @Test
    public void getAccountByAccountNumber(){
        Account testAccount = new Account(1L,1111L,BigDecimal.valueOf(10000.00),Date.valueOf("2020-01-01"));
        Account account = dataConnectionService.getAccountByAccountNumber(1111L);
        Assert.assertEquals(testAccount,account);
        Assert.assertNotEquals(new Account(),account);
    }

    @Test
    public void getAllCardByAccountNumber(){
       List<Card> testCards = new ArrayList<>();
       testCards.add(new Card(1L,1111L,1111222233334441L,Date.valueOf("2023-01-01"),111));
        testCards.add(new Card(2L,1111L,1111222233334442L,Date.valueOf("2023-01-01"),112));
        List<Card> cards = dataConnectionService.getAllCardByAccountNumber(1111L);
        Assert.assertEquals(cards,testCards);
    }

    @Test
    public void getAccountByCardNumber(){
        Account testAccount = new Account(1L,1111L,BigDecimal.valueOf(10000.00),Date.valueOf("2020-01-01"));
       Account account = dataConnectionService.getAccountByCardNumber(1111222233334441L);
       Assert.assertEquals(testAccount,account);
    }

    @Test
    public void p(){
        dataConnectionServic
    }*/

    @AfterClass
    public static void stop(){
        dataConnectionService.stop();
    }

}
