package ru.sber.bootcamp.service.h2ConnectionImplMethods;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.sber.bootcamp.controller.ClientController;
import ru.sber.bootcamp.modelDao.repository.*;
import ru.sber.bootcamp.service.DataConnectionService;
import ru.sber.bootcamp.service.H2ConnectionServiceImpl;
import ru.sber.bootcamp.service.httpServer.HttpServerStarter;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class H2ConnectionCardMethodsTest{



        static DataConnectionService dataService;
        static AccountRepository accountRepository;
        static ClientRepository clientRepository;
        static CardRepository cardRepository;
        static ClientController controller;
        static HttpServerStarter httpServerStarter;

        @BeforeClass
        public static void init(){
            dataService = new H2ConnectionServiceImpl(false);
            dataService.start();

            accountRepository = new AccountRepoImpl(dataService);
            clientRepository = new ClientRepositoryImpl(dataService);
            cardRepository = new CardRepositoryImpl(dataService);

            //Controller start
            controller = new ClientController(accountRepository, clientRepository, cardRepository);


            //HTTP server start
            httpServerStarter = new HttpServerStarter(controller);
            httpServerStarter.start();
        }

    public boolean serverResponse;
    public String cardNumber;

    public H2ConnectionCardMethodsTest(boolean serverResponse, String cardNumber) {
        this.serverResponse = serverResponse;
        this.cardNumber = cardNumber;
    }


    @Parameterized.Parameters
        public static Collection<Object[]> data() {

            return Arrays.asList(new Object[][]{
                    {false,"1"},
                    {false,"12"},
                    {false,"13"},
                    {false,"414441413232134"},
                    {false,"1123412341234"},
                    {false,"1552151234444444"},
                    {true,"1111222233334441"},
                    {true,"1111222233334442"},
                    {true,"1112222233334441"},
                    {true,"1112222233334442"},


            });
        }



    @Test
    public void test() {
        boolean serverResponseTest =
                dataService.isCardExist(cardNumber);
        Assert.assertEquals(serverResponse,serverResponseTest);
    }



        @AfterClass
        public static void stop(){
            httpServerStarter.stop();
            dataService.stop();
        }

}