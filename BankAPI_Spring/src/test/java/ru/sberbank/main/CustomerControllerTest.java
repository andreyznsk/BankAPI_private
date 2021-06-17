package ru.sberbank.main;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.sberbank.main.controller.ClientController;
import ru.sberbank.main.modelDAO.entity.Account;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerControllerTest {

    @Autowired
    ClientController clientController;

    @Test
    public void getAllAccount() {
        List<Account> accountsTest = new ArrayList<>();

        accountsTest.add( new Account(1L,"1111", BigDecimal.valueOf(10000.10), Date.valueOf("2020-01-01"), null));
        accountsTest.add( new Account(2L,"1112", BigDecimal.valueOf(2000.25), Date.valueOf("2020-01-01"), null));
        List<Account> accounts = clientController.getAllAccounts();
        Assert.assertEquals(accounts,accountsTest);
    }
}