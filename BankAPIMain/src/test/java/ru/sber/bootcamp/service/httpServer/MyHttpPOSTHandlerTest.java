package ru.sber.bootcamp.service.httpServer;


import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.sber.bootcamp.configuration.DataBaseConfig;
import ru.sber.bootcamp.controller.ClientController;
import ru.sber.bootcamp.model_DAO.entity.Account;
import ru.sber.bootcamp.model_DAO.entity.Card;
import ru.sber.bootcamp.model_DAO.entity.Client;
import ru.sber.bootcamp.model_DAO.repository.*;
import ru.sber.bootcamp.service.DataConnectionService;
import ru.sber.bootcamp.service.GsonConverter;
import ru.sber.bootcamp.service.GsonConverterImpl;
import ru.sber.bootcamp.service.H2ConnectionServiceImpl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class MyHttpPOSTHandlerTest {
    static DataConnectionService dataService;
    static AccountRepository accountRepository;
    static ClientRepository clientRepository;
    static  CardRepository cardRepository;
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


    @Test
    public void postBalanceIncHandler() throws IOException {
        String jsonQuery = "{\"amount\":1111,\"card_number\":1111222233334441,\"CVC_code\":111}";
        String jsonResponseObj = "{\"server_status\":\"Balanceupdated\"}";
        JSONObject jsonObjectResponseTest = new JSONObject(jsonResponseObj);
        JSONObject jsonObjectTest = new JSONObject(jsonQuery);

        // Given
        URL url = new URL("http://localhost:8000/bank_api/balance_inc");
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(jsonObjectTest.toString());
        wr.flush();

        StringBuilder sb = new StringBuilder();
        InputStreamReader isr = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8);
        Scanner sc = new Scanner(isr);
        while (sc.hasNextLine())  {
            sb.append(sc.next());
        }
        System.out.println(sb.toString());
        JSONObject jsonObject = new JSONObject(sb.toString());
        System.out.println(jsonObject);

        // Then
        Assert.assertEquals(jsonObject.toString(),jsonObjectResponseTest.toString());
    }


    @Test
    public void postBalanceIncWrongCVCHandler() throws IOException {
        String jsonQuery = "{\"amount\":1111,\"card_number\":1111222233334441,\"CVC_code\":001}";
        String jsonResponseObj = "{\"server_status\":\"InvalidCVCcode\"}";
        JSONObject jsonObjectResponseTest = new JSONObject(jsonResponseObj);
        JSONObject jsonObjectTest = new JSONObject(jsonQuery);
        // Given
        URL url = new URL("http://localhost:8000/bank_api/balance_inc");
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(jsonObjectTest.toString());
        wr.flush();

        StringBuilder sb = new StringBuilder();
        InputStreamReader isr = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8);
        Scanner sc = new Scanner(isr);
        while (sc.hasNextLine())  {
            sb.append(sc.next());
        }
        String body = sb.toString();
        JSONObject jsonObject = new JSONObject(body);

        // Then
        Assert.assertEquals(jsonObject.toString(),jsonObjectResponseTest.toString());
    }


    @AfterClass
    public static void stop(){
       httpServerStarter.stop();
       dataService.stop();
    }
}