package ru.sber.bootcamp.service.httpServer.getMethods;


import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import ru.sber.bootcamp.controller.ClientController;
import ru.sber.bootcamp.modelDao.repository.*;
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
public class MassTestGetClientByAccountNumber {

    static DataConnectionService dataService;
    static AccountRepository accountRepository;
    static ClientRepository clientRepository;
    static CardRepository cardRepository;
    static ClientController controller;
    static HttpServerStarter httpServerStarter;
    static GsonConverter gsonConverter;

    @BeforeClass
    public static void init(){
        dataService = new H2ConnectionServiceImpl(false);
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
    Object userUrl;


    public MassTestGetClientByAccountNumber(String serverResponse, Object accountNumber) {
        this.serverResponse = serverResponse;
        this.userUrl = accountNumber;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        String client1 = "{\"accountId\":1111,\"firstName\":\"Ivan\",\"phoneNumber\":89008001234,\"id\":1,\"account\":{\"balance\":10000.1,\"id\":1,\"openDate\":\"2020-01-01\",\"accountNumber\":1111},\"lastname\":\"Ivanov\"}";
        String client2 = "{\"accountId\":1112,\"firstName\":\"Петр\",\"phoneNumber\":88009001235,\"id\":2,\"account\":{\"balance\":2000.25,\"id\":2,\"openDate\":\"2020-01-01\",\"accountNumber\":1112},\"lastname\":\"Петров\"}";

        return Arrays.asList(new Object[][]{
                {"{\"Error!\":\"Input_account_number\"}" ,null},
                {"{\"Error!\":\"Incorrect_account_number\"}",1L},
                {"{\"Error!\":\"Incorrect_account_number\"}",2L},
                {client1, 1111L},
                {client2, 1112L},
                {"{\"Error!\":\"Incorrect_account_number\"}",123123123123L},
                {"{\"Error!\":\"Forinputstring:\\\"Pepsi-Cola\\\"\"}", "Pepsi-Cola"},
        });
    }




    @Test
    public void test() throws IOException {
        URL url = new URL("http://localhost:8000/bank_api/get_client_by_account_number/" + ((userUrl !=null)? userUrl :""));
        URLConnection conn = url.openConnection();
        conn.setDoOutput(false);

        StringBuilder sb = new StringBuilder();
        InputStreamReader isr = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8);
        Scanner sc = new Scanner(isr);
        while (sc.hasNextLine())  {
            sb.append(sc.next());
        }
        String body = sb.toString();

        JSONAssert.assertEquals(new JSONObject(body), new JSONObject(serverResponse), JSONCompareMode.STRICT);
    }

    @AfterClass
    public static void stop(){
        httpServerStarter.stop();
        dataService.stop();
    }

}

