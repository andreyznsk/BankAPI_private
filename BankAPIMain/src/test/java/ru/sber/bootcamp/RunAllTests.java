package ru.sber.bootcamp;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import ru.sber.bootcamp.model_DAO.repository.AccountRepositoryTest;
import ru.sber.bootcamp.model_DAO.repository.CardRepositoryTest;
import ru.sber.bootcamp.model_DAO.repository.ClientRepositoryTest;
import ru.sber.bootcamp.service.httpServer.GET_Methods.*;
import ru.sber.bootcamp.service.httpServer.POST_methods.MASS_balance_inc;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AccountRepositoryTest.class,
        CardRepositoryTest.class,
        ClientRepositoryTest.class,
        MASS_TestDefault.class,
        MASS_TestGetAllCards.class,
        MASS_TestGetBalanceByCardNumber.class,
        MASS_TestGetClientByAccountNumber.class,
        MASS_TestShowAllAccounts.class,
        MASS_TestGetCardByAccountNumber.class,
        MASS_balance_inc.class
})


public class RunAllTests {
}