package ru.sber.bootcamp.service.httpServer.getMethods;


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

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;


@RunWith(Parameterized.class)
public class MassTestGetBalanceByCardNumber {

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

    String serverResponse;
    String cardNumber;


    public MassTestGetBalanceByCardNumber(String serverResponse, String cardNumber) {
        this.serverResponse = serverResponse;
        this.cardNumber = cardNumber;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {

        return Arrays.asList(new Object[][]{
                {"{\"Error!\":\"InputCardNumber\"}" ,""},
                {"{\"Error!\":\"CardNumberIncorrect\"}","1"},
                {"{\"Error!\":\"CardNumberIncorrect\"}","2"},
                {"{\"Error!\":\"CardNumberIncorrect\"}" , "1111"},
                {"{\"Error!\":\"CardNumberIncorrect\"}","123123123123"},
                {"{\"Error!\":\"CardNumberIncorrect\"}", "Pepsi-Cola"},
                {"{\"balance\":10000.10}","1111222233334441"},
                {"{\"balance\":10000.10}","1111222233334442"},
                {"{\"balance\":2000.25}","1112222233334441"},
                {"{\"balance\":2000.25}","1112222233334442"},
                {"{\"balance\":2000.25}","1112222233334443"},
                {"{\"balance\":2000.25}","1112222233334444"}
        });
    }




    @Test
    public void test() throws IOException {
        URL url = new URL("http://localhost:8000/bank_api/get_balance_by_card_number/" + cardNumber);
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

