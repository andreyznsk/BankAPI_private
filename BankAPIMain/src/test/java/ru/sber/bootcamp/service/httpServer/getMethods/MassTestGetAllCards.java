package ru.sber.bootcamp.service.httpServer.getMethods;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.sber.bootcamp.controller.ClientController;
import ru.sber.bootcamp.modelDao.entity.Card;
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
import java.util.List;
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
        String serverResponse = "[{\"cvc_code\":111,\"dateValidThru\":\"2023-01-01\",\"id\":1,\"accountNumber\":1111,\"cardNumber\":1111222233334441}," +
                "{\"cvc_code\":112,\"dateValidThru\":\"2023-01-01\",\"id\":2,\"accountNumber\":1111,\"cardNumber\":1111222233334442}," +
                "{\"cvc_code\":121,\"dateValidThru\":\"2023-01-01\",\"id\":3,\"accountNumber\":1112,\"cardNumber\":1112222233334441}," +
                "{\"cvc_code\":122,\"dateValidThru\":\"2023-01-01\",\"id\":4,\"accountNumber\":1112,\"cardNumber\":1112222233334442}," +
                "{\"cvc_code\":123,\"dateValidThru\":\"2023-01-01\",\"id\":5,\"accountNumber\":1112,\"cardNumber\":1112222233334443}," +
                "{\"cvc_code\":124,\"dateValidThru\":\"2023-01-01\",\"id\":6,\"accountNumber\":1112,\"cardNumber\":1112222233334444}]";

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
        ObjectMapper objectMapper = new ObjectMapper();
        List<Card> cardsExpected = Arrays.asList(objectMapper.readValue(serverResponse,Card[].class));
        URL url = new URL("http://localhost:8000/bank_api/get_all_cards/" + accountNumber);
        URLConnection conn = url.openConnection();
        conn.setDoOutput(false);
        InputStreamReader isr = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8);
        List<Card> cardsActual = Arrays.asList(objectMapper.readValue(isr,Card[].class));
        Assert.assertEquals(cardsExpected,cardsActual);
    }

    @AfterClass
    public static void stop(){
        httpServerStarter.stop();
        dataService.stop();
    }

}

