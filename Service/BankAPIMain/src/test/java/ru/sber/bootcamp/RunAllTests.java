package ru.sber.bootcamp;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import ru.sber.bootcamp.modelDao.repository.AccountRepositoryTest;
import ru.sber.bootcamp.modelDao.repository.CardRepositoryTest;
import ru.sber.bootcamp.modelDao.repository.ClientRepositoryTest;
import ru.sber.bootcamp.service.h2ConnectionImplMethods.H2ConnectionCardMethodsTest;
import ru.sber.bootcamp.service.httpServer.getMethods.*;
import ru.sber.bootcamp.service.httpServer.postmethods.MassBalance_inc;
import ru.sber.bootcamp.service.httpServer.postmethods.MassCart_add;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        H2ConnectionCardMethodsTest.class,
        AccountRepositoryTest.class,
        CardRepositoryTest.class,
        ClientRepositoryTest.class,
        MassTestDefault.class,
        MassTestGetAllCards.class,
        MassTestGetBalanceByCardNumber.class,
        MassTestGetClientByAccountNumber.class,
        MassTestShowAllAccounts.class,
        MassTestGetCardByAccountNumber.class,
        MassBalance_inc.class,
        MassCart_add.class
})


public class RunAllTests {
}