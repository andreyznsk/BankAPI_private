package ru.sber.bootcamp.service.httpServer.POST_methods;


import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
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
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;


@RunWith(Parameterized.class)
public class MASS_cart_add {

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
    Object accountNumber;


    public MASS_cart_add(String serverResponse,Object accountNumber) {
        this.accountNumber = accountNumber;
        this.serverResponse = serverResponse;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {

        return Arrays.asList(new Object[][]{
                {"{\"Error!\":\"account_number:Not_found\"}" ,null},
                {"{\"Error!\":\"account_number:Not_found\"}" ,"Please! give me a new card!"},
                {"{\"Error!\":\"Incorrect_account_number\"}" ,1113l},
                {"{\"Error!\":\"Incorrect_account_number\"}" ,111334l},
                {"{\"Error!\":\"Incorrect_account_number\"}" ,11124l},
                {"{\"Error!\":\"Incorrect_account_number\"}" ,1114l},
                {"{\"Server_OK!\":\"Card_added\"}" ,1111l},
                {"{\"Server_OK!\":\"Card_added\"}" ,1111l},
                {"{\"Server_OK!\":\"Card_added\"}" ,1111l},
                {"{\"Server_OK!\":\"Card_added\"}" ,1111l},
                {"{\"Server_OK!\":\"Card_added\"}" ,1111l},
                {"{\"Server_OK!\":\"Card_added\"}" ,1111l},
                {"{\"Server_OK!\":\"Card_added\"}" ,1111l},
                {"{\"Server_OK!\":\"Card_added\"}" ,1111l},
                {"{\"Server_OK!\":\"Card_added\"}" ,1111l},
                {"{\"Server_OK!\":\"Card_added\"}" ,1111l},
                {"{\"Server_OK!\":\"Card_added\"}" ,1111l},
                {"{\"Server_OK!\":\"Card_added\"}" ,1111l},
                {"{\"Server_OK!\":\"Card_added\"}" ,1111l},



        });
    }




    @Test
    public void postBalanceIncHandler() throws IOException {
        JSONObject jsonQuery = new JSONObject();
        jsonQuery.put("account_number",accountNumber);
        JSONObject jsonObjectResponseTest = new JSONObject(serverResponse);
        // Given
        URL url = new URL("http://localhost:8000/bank_api/add_card");
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(jsonQuery.toString());
        wr.flush();

        StringBuilder sb = new StringBuilder();
        InputStreamReader isr = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8);
        Scanner sc = new Scanner(isr);
        while (sc.hasNextLine())  {
            sb.append(sc.next());
        }
        JSONObject jsonObject = new JSONObject(sb.toString());
        System.out.println(jsonObject);
        JSONAssert.assertEquals( jsonObjectResponseTest,jsonObject, JSONCompareMode.STRICT);

    }

    @AfterClass
    public static void stop(){
        httpServerStarter.stop();
        dataService.stop();
    }

}

