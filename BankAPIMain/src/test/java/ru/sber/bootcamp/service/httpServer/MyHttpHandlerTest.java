package ru.sber.bootcamp.service.httpServer;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
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
import java.util.Locale;
import java.util.Scanner;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class MyHttpHandlerTest {
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
    public void anyAddress()
            throws ClientProtocolException, IOException {

        // Given
        String name = RandomStringUtils.randomAlphabetic( 8 );
        System.out.println(name);
        HttpUriRequest request = new HttpGet( "http://localhost:8000/bank_api/"+name );

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );
        StringBuilder sb = new StringBuilder();
        InputStreamReader isr = new InputStreamReader(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8);
        Scanner sc = new Scanner(isr);
        while (sc.hasNextLine())  {
            sb.append(sc.next());
        }
        String response = sb.toString();
        // Then
        assertEquals("CommandError!!!",response);
    }

    @Test
    public void getJson()
            throws ClientProtocolException, IOException {
        Card card = new Card(1l,1111l,1111222233334441l, Date.valueOf("2023-01-01"),111);

        JSONObject jsonObjectTest = gsonConverter.convertObjectToJson(card);
        // Given
        HttpUriRequest request = new HttpGet( "http://localhost:8000/bank_api/get_card_by_account/1111" );

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );

        StringBuilder sb = new StringBuilder();
        InputStreamReader isr = new InputStreamReader(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8);
        Scanner sc = new Scanner(isr);
        while (sc.hasNextLine())  {
            sb.append(sc.next());
        }
        String body = sb.toString();
        JSONObject jsonObject = new JSONObject(body);
        // Then
        Assert.assertEquals(jsonObject.toString(),jsonObjectTest.toString());
    }

    @Test
    public void getClientByIdHandle()
            throws ClientProtocolException, IOException {
        Account testAccount = new Account(1L,1111L, BigDecimal.valueOf(10000.00), Date.valueOf("2020-01-01"));
        Client testClient = new Client(1L,1111L,"Ivan", "Ivanov",89008001234L,testAccount);

        JSONObject jsonObjectTest = gsonConverter.convertObjectToJson(testClient);
        // Given
        HttpUriRequest request = new HttpGet( "http://localhost:8000/bank_api/get_client/1111" );

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );

        StringBuilder sb = new StringBuilder();
        InputStreamReader isr = new InputStreamReader(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8);
        Scanner sc = new Scanner(isr);
        while (sc.hasNextLine())  {
            sb.append(sc.next());
        }
        String body = sb.toString();
        JSONObject jsonObject = new JSONObject(body);
        // Then
        Assert.assertEquals(jsonObject.toString(),jsonObjectTest.toString());
    }

    @Test
    public void postBalanceIncHandler()
            throws ClientProtocolException, IOException {
        String jsonQuery = "{\"amount\":1111,\"card_number\":1111222233334441,\"CVC_code\":111}";
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

        // Then
        Assert.assertEquals("balanceupdated",body.toLowerCase(Locale.ROOT));
    }


    @Test
    public void postBalanceIncWronfCVCHandler()
            throws ClientProtocolException, IOException {
        String jsonQuery = "{\"amount\":1111,\"card_number\":1111222233334441,\"CVC_code\":001}";
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

        // Then
        Assert.assertEquals("invalidcvccode",body.toLowerCase(Locale.ROOT));
    }


    @AfterClass
    public static void stop(){
       httpServerStarter.stop();
       dataService.stop();
    }
}