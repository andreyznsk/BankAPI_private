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
import ru.sber.bootcamp.modelDao.entity.Account;
import ru.sber.bootcamp.modelDao.entity.Client;
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
public class MassTestGetClientByAccountNumber {

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
    String userUrl;


    public MassTestGetClientByAccountNumber(String serverResponse, String accountNumber) {
        this.serverResponse = serverResponse;
        this.userUrl = accountNumber;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        String client1 = "{\"accountId\":1111,\"firstName\":\"Ivan\",\"phoneNumber\":89008001234,\"id\":1,\"account\":{\"balance\":10000.1,\"id\":1,\"openDate\":\"2020-01-01\",\"accountNumber\":1111},\"lastname\":\"Ivanov\"}";
        String client2 = "{\"accountId\":1112,\"firstName\":\"????????\",\"phoneNumber\":88009001235,\"id\":2,\"account\":{\"balance\":2000.25,\"id\":2,\"openDate\":\"2020-01-01\",\"accountNumber\":1112},\"lastname\":\"????????????\"}";

        return Arrays.asList(new Object[][]{
                {"{\"Error!\":\"InputAccountNumber\"}" ,""},
                {"{\"Error!\":\"IncorrectAccountNumber\"}","1"},
                {"{\"Error!\":\"IncorrectAccountNumber\"}","2"},
                {client1, "1111"},
                {client2, "1112"},
                {"{\"Error!\":\"IncorrectAccountNumber\"}","123123123123"},
                {"{\"Error!\":\"IncorrectAccountNumber\"}", "Pepsi-Cola"},
        });
    }




    @Test
    public void test() throws IOException {
        URL url = new URL("http://localhost:8000/bank_api/get_client_by_account_number/" + userUrl);
        URLConnection conn = url.openConnection();
        conn.setDoOutput(false);

        InputStreamReader isr = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNodeActual = objectMapper.readTree(isr);
        JsonNode jsonNodeExpected = objectMapper.readTree(serverResponse);
        if(jsonNodeExpected.has("Error!") & jsonNodeActual.has("Error!")){
            Assert.assertEquals(jsonNodeExpected,jsonNodeActual);
        } else {
            Client clientExpected = objectMapper.convertValue(jsonNodeExpected,Client.class);
            Client clientActual = objectMapper.convertValue(jsonNodeActual,Client.class);
            Assert.assertEquals(clientExpected,clientActual);
        }
    }

    @AfterClass
    public static void stop(){
        httpServerStarter.stop();
        dataService.stop();
    }

}

