package ru.sber.bootcamp;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import ru.sber.bootcamp.model_DAO.repository.AccountRepositoryTest;
import ru.sber.bootcamp.model_DAO.repository.CardRepositoryTest;
import ru.sber.bootcamp.model_DAO.repository.ClientRepositoryTest;
import ru.sber.bootcamp.service.H2ConnectionServiceImpl;
import ru.sber.bootcamp.service.h2ConnectionImplMethods.H2ConnectionCardMethodsTest;
import ru.sber.bootcamp.service.httpServer.GET_Methods.*;
import ru.sber.bootcamp.service.httpServer.POST_methods.MASS_balance_inc;
import ru.sber.bootcamp.service.httpServer.POST_methods.MASS_cart_add;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        H2ConnectionCardMethodsTest.class,
        AccountRepositoryTest.class,
        CardRepositoryTest.class,
        ClientRepositoryTest.class,
        MASS_TestDefault.class,
        MASS_TestGetAllCards.class,
        MASS_TestGetBalanceByCardNumber.class,
        MASS_TestGetClientByAccountNumber.class,
        MASS_TestShowAllAccounts.class,
        MASS_TestGetCardByAccountNumber.class,
        MASS_balance_inc.class,
        MASS_cart_add.class
})


public class RunAllTests {
}