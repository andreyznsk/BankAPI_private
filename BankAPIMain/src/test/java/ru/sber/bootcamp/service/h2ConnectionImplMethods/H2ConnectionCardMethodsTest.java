package ru.sber.bootcamp.service.h2ConnectionImplMethods;

import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.sber.bootcamp.configuration.DataBaseConfig;
import ru.sber.bootcamp.controller.ClientController;
import ru.sber.bootcamp.model_DAO.repository.*;
import ru.sber.bootcamp.service.DataConnectionService;
import ru.sber.bootcamp.service.GsonConverter;
import ru.sber.bootcamp.service.GsonConverterImpl;
import ru.sber.bootcamp.service.H2ConnectionServiceImpl;
import ru.sber.bootcamp.service.httpServer.HttpServerStarter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class H2ConnectionCardMethodsTest{



        static DataConnectionService dataService;
        static AccountRepository accountRepository;
        static ClientRepository clientRepository;
        static CardRepository cardRepository;
        static ClientController controller;
        static HttpServerStarter httpServerStarter;
        static GsonConverter gsonConverter;

        @BeforeClass
        public static void init(){
            dataService = new H2ConnectionServiceImpl(DataBaseConfig.getConfig(),false);
            dataService.start();

            accountRepository = new AccountRepoImpl(dataService);
            clientRepository = new ClientRepositoryImpl(dataService);
            cardRepository = new CardRepositoryImpl(dataService);

            //Controller start
            controller = new ClientController(accountRepository, clientRepository, cardRepository, new GsonConverterImpl());


            //HTTP server start
            httpServerStarter = new HttpServerStarter(controller);
            httpServerStarter.start();

            gsonConverter = new GsonConverterImpl();
        }

    public boolean serverResponse;
    public Long cardNumber;

    public H2ConnectionCardMethodsTest(boolean serverResponse, Long cardNumber) {
        this.serverResponse = serverResponse;
        this.cardNumber = cardNumber;
    }


    @Parameterized.Parameters
        public static Collection<Object[]> data() {

            return Arrays.asList(new Object[][]{
                    {false,1L},
                    {false,12L},
                    {false,13L},
                    {false,414441413232134L},
                    {false,1123412341234L},
                    {false,1552151234444444L},
                    {true,1111222233334441L},
                    {true,1111222233334442L},
                    {true,1112222233334441L},
                    {true,1112222233334442L},


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