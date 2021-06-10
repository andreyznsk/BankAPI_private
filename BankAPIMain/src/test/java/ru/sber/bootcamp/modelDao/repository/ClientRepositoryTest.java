package ru.sber.bootcamp.modelDao.repository;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.sber.bootcamp.modelDao.entity.Account;
import ru.sber.bootcamp.modelDao.entity.Client;
import ru.sber.bootcamp.service.DataConnectionService;
import ru.sber.bootcamp.service.H2ConnectionServiceImpl;

import java.math.BigDecimal;
import java.sql.Date;

public class ClientRepositoryTest {

    static DataConnectionService dataConnectionService;
    static ClientRepository clientRepository;

    @BeforeClass
    public static void init() {
        dataConnectionService = new H2ConnectionServiceImpl(false);
        clientRepository = new ClientRepositoryImpl(dataConnectionService);
        dataConnectionService.start();

    }

    @Test
    public void getClientByAccountNumber() {
        Account testAccount = new Account(1L,"1111", BigDecimal.valueOf(10000.10), Date.valueOf("2020-01-01"));
        Client testClient = new Client(1L,"1111","Ivan", "Ivanov",89008001234L,testAccount);
        System.out.println(testClient);
        Client client = dataConnectionService.getClientByAccountNumber("1111");
        Assert.assertEquals(client,testClient);
    }

    @AfterClass
    public static void stop(){
        dataConnectionService.stop();
    }
}