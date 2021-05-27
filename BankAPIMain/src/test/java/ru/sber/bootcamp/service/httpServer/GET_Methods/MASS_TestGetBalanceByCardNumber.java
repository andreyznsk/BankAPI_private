package ru.sber.bootcamp.service.httpServer.GET_Methods;


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


@RunWith(Parameterized.class)
public class MASS_TestGetBalanceByCardNumber {

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

    String serverResponse;
    Object cardNumber;


    public MASS_TestGetBalanceByCardNumber(String serverResponse, Object accountNumber) {
        this.serverResponse = serverResponse;
        this.cardNumber = accountNumber;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {

        return Arrays.asList(new Object[][]{
                {"{\"Error!\":\"Input_Card_number\"}" ,null},
                {"{\"Error!\":\"Card_Number_incorrect\"}",1L},
                {"{\"Error!\":\"Card_Number_incorrect\"}",2L},
                {"{\"Error!\":\"Card_Number_incorrect\"}" , 1111L},
                {"{\"Error!\":\"Card_Number_incorrect\"}",123123123123L},
                {"{\"Error!\":\"Forinputstring:\\\"Pepsi-Cola\\\"\"}", "Pepsi-Cola"},
                {"{\"balance\":10000.1}",1111222233334441L},
                {"{\"balance\":10000.1}",1111222233334442L},
                {"{\"balance\":2000.25}",1112222233334441L},
                {"{\"balance\":2000.25}",1112222233334442L},
                {"{\"balance\":2000.25}",1112222233334443L},
                {"{\"balance\":2000.25}",1112222233334444L}
        });
    }




    @Test
    public void test() throws IOException {
        URL url = new URL("http://localhost:8000/bank_api/get_balance_by_card_number/" + ((cardNumber !=null)? cardNumber :""));
        URLConnection conn = url.openConnection();
        conn.setDoOutput(false);

        StringBuilder sb = new StringBuilder();
        InputStreamReader isr = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8);
        Scanner sc = new Scanner(isr);
        while (sc.hasNextLine())  {
            sb.append(sc.next());
        }
        String body = sb.toString();
        Assert.assertEquals(serverResponse,body);
    }

    @AfterClass
    public static void stop(){
        httpServerStarter.stop();
        dataService.stop();
    }

}

