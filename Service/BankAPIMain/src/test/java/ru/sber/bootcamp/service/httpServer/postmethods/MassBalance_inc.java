package ru.sber.bootcamp.service.httpServer.postmethods;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;


@RunWith(Parameterized.class)
public class MassBalance_inc {

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

    String serverResponseEcpected;
    Double amount;
    Integer CVC_code;
    String card_number;


    public MassBalance_inc(String serverResponse, Double amount, Integer CVC_code, String card_number) {
        this.serverResponseEcpected = serverResponse;
        this.amount = amount;
        this.CVC_code = CVC_code;
        this.card_number = card_number;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {

        return Arrays.asList(new Object[][]{
                {"{\"Error!\":\"CardNotFound\"}" ,111.1,111,"1111122221"},
                {"{\"Error!\":\"CardNotFound\"}" ,111.1,111,"Black Power!"},
                {"{\"Error!\":\"CardNotFound\"}" ,null,111,"1111122221"},
                {"{\"Error!\":\"CardNotFound\"}"  ,111.1,null,"1111122221"},
                {"{\"Error!\":\"CardNotFound\"}" ,111.1,111,null},
                {"{\"Error!\":\"CardNotFound\"}" ,null,null,null},
                {"{\"Error!\":\"CardNotFound\"}" ,111.1,111,"1111122221"},
                {"{\"Server_OK!\":\"Balance_updated_ok\"}" ,111.1,111,"1111222233334441"},
                {"{\"Error!\":\"CVCCodeInvalid\"}" ,111.1,111,"1111222233334442"},
                {"{\"Error!\":\"AmountIsNegative\"}" ,-111.1,111,"1111222233334441"},
        });
    }




    @Test
    public void postBalanceIncHandler() throws Exception {
        ObjectNode jsonQuery = new ObjectMapper().createObjectNode();
        jsonQuery.put("amount",amount);
        jsonQuery.put("CVC_code",CVC_code);
        jsonQuery.put("card_number",card_number);
        JsonNode jsonObjectResponseExpected = new ObjectMapper().readTree(serverResponseEcpected);
        // Given
        URL url = new URL("http://localhost:8000/bank_api/balance_inc");
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(jsonQuery.toString());
        wr.flush();
        InputStreamReader isr = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8);
        JsonNode jsonNodeActual = new ObjectMapper().readTree(isr);
        Assert.assertEquals(jsonObjectResponseExpected, jsonNodeActual);
    }

    @AfterClass
    public static void stop(){
        httpServerStarter.stop();
        dataService.stop();
    }

}

