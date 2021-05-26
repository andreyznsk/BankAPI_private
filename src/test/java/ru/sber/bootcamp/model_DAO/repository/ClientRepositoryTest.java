package ru.sber.bootcamp.model_DAO.repository;

import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.sber.bootcamp.configuration.DataBaseConfig;
import ru.sber.bootcamp.model_DAO.entity.Account;
import ru.sber.bootcamp.model_DAO.entity.Client;
import ru.sber.bootcamp.service.DataConnectionService;
import ru.sber.bootcamp.service.H2ConnectionServiceImpl;

import java.math.BigDecimal;
import java.sql.Date;

import static org.junit.Assert.*;

public class ClientRepositoryTest {

    static DataConnectionService dataConnectionService;
    static ClientRepository clientRepository;

    @BeforeClass
    public static void init() {
        dataConnectionService = new H2ConnectionServiceImpl(DataBaseConfig.getConfig(),false);
        clientRepository = new ClientRepositoryImpl(dataConnectionService);
        dataConnectionService.start();

    }

    @Test
    public void getClientByAccountNumber() {
        Account testAccount = new Account(1L,1111L, BigDecimal.valueOf(10000.00), Date.valueOf("2020-01-01"));
        Client testClient = new Client(1L,1111L,"Ivan", "Ivanov",89008001234L,testAccount);
        System.out.println(testClient);
        Client client = dataConnectionService.getClientByAccountNumber(1111L);
        Assert.assertEquals(testClient,client);
    }

    @AfterClass
    public static void stop(){
        dataConnectionService.stop();
    }
}