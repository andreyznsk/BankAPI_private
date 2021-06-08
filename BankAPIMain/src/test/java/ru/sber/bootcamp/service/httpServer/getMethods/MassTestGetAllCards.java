package ru.sber.bootcamp.service.httpServer.getMethods;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class MassTestGetAllCards {

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
    Object accountNumber;


    public MassTestGetAllCards(String serverResponse, String accountNumber) {
        this.serverResponse = serverResponse;
        this.accountNumber = accountNumber;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        String serverResponse = "[{\"CVC_code\":111,\"dateValidThru\":\"2023-01-01\",\"id\":1,\"accountNumber\":1111,\"cardNumber\":1111222233334441}," +
                "{\"CVC_code\":112,\"dateValidThru\":\"2023-01-01\",\"id\":2,\"accountNumber\":1111,\"cardNumber\":1111222233334442}," +
                "{\"CVC_code\":121,\"dateValidThru\":\"2023-01-01\",\"id\":3,\"accountNumber\":1112,\"cardNumber\":1112222233334441}," +
                "{\"CVC_code\":122,\"dateValidThru\":\"2023-01-01\",\"id\":4,\"accountNumber\":1112,\"cardNumber\":1112222233334442}," +
                "{\"CVC_code\":123,\"dateValidThru\":\"2023-01-01\",\"id\":5,\"accountNumber\":1112,\"cardNumber\":1112222233334443}," +
                "{\"CVC_code\":124,\"dateValidThru\":\"2023-01-01\",\"id\":6,\"accountNumber\":1112,\"cardNumber\":1112222233334444}]";

        return Arrays.asList(new Object[][]{
                {serverResponse ,""},
                {serverResponse,"1"},
                {serverResponse,"2"},
                {serverResponse , "1111"},
                {serverResponse,"123123123123"},
                {serverResponse, "Pepsi-Cola"},
        });
    }




    @Test
    public void test() throws IOException {
        JsonNode jsonNodeExpected = new ObjectMapper().readTree(serverResponse);
        URL url = new URL("http://localhost:8000/bank_api/get_all_cards/" + accountNumber);
        URLConnection conn = url.openConnection();
        conn.setDoOutput(false);
        InputStreamReader isr = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8);
        JsonNode jsonNodeActual = new ObjectMapper().readTree(isr);
        Assert.assertEquals(jsonNodeExpected,jsonNodeActual);
    }

    @AfterClass
    public static void stop(){
        httpServerStarter.stop();
        dataService.stop();
    }

}

