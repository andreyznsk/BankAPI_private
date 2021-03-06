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
public class MassCart_add {

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


    public MassCart_add(String serverResponse, Object accountNumber) {
        this.accountNumber = accountNumber;
        this.serverResponse = serverResponse;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {

        return Arrays.asList(new Object[][]{
                {"{\"Error!\":\"IncorrectAccountNumber\"}" ,null},
                {"{\"Error!\":\"IncorrectAccountNumber\"}" ,"Please! give me a new card!"},
                {"{\"Error!\":\"IncorrectAccountNumber\"}" ,1113l},
                {"{\"Error!\":\"IncorrectAccountNumber\"}" ,111334l},
                {"{\"Error!\":\"IncorrectAccountNumber\"}" ,11124l},
                {"{\"Error!\":\"IncorrectAccountNumber\"}" ,1114l},
                {"{\"Server_OK!\":\"CardAdded\"}" ,1111l},
                {"{\"Server_OK!\":\"CardAdded\"}" ,1111l},
                {"{\"Server_OK!\":\"CardAdded\"}" ,1111l},
                {"{\"Server_OK!\":\"CardAdded\"}" ,1111l},
                {"{\"Server_OK!\":\"CardAdded\"}" ,1111l},
                {"{\"Server_OK!\":\"CardAdded\"}" ,1111l},
                {"{\"Server_OK!\":\"CardAdded\"}" ,1111l},
                {"{\"Server_OK!\":\"CardAdded\"}" ,1111l},
                {"{\"Server_OK!\":\"CardAdded\"}" ,1111l},
                {"{\"Server_OK!\":\"CardAdded\"}" ,1111l},
                {"{\"Server_OK!\":\"CardAdded\"}" ,1111l},
                {"{\"Server_OK!\":\"CardAdded\"}" ,1111l},
                {"{\"Server_OK!\":\"CardAdded\"}" ,1111l},



        });
    }




    @Test
    public void postBalanceIncHandler() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonQuery = objectMapper.createObjectNode();
        jsonQuery.put("account_number", String.valueOf(accountNumber));

        URL url = new URL("http://localhost:8000/bank_api/add_card");
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(jsonQuery.toString());
        wr.flush();
        JsonNode jsonNodeExpected = objectMapper.readTree(serverResponse);
        InputStreamReader isr = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8);
        JsonNode jsonNodeActual = objectMapper.readTree(isr);
        Assert.assertEquals(jsonNodeExpected,jsonNodeActual);
    }

    @AfterClass
    public static void stop(){
        httpServerStarter.stop();
        dataService.stop();
    }

}

