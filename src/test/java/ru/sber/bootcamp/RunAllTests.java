package ru.sber.bootcamp;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import ru.sber.bootcamp.model_DAO.repository.AccountRepositoryTest;
import ru.sber.bootcamp.model_DAO.repository.CardRepositoryTest;
import ru.sber.bootcamp.model_DAO.repository.ClientRepositoryTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AccountRepositoryTest.class,
        CardRepositoryTest.class,
        ClientRepositoryTest.class
})


public class RunAllTests {
}