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
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class MyHttpGETHandlerTest {
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


    /*@Test
    public void getAnyAddress() throws IOException {

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
    public void getCard_by_Account()
            throws IOException {
        List<Card> cardList =  new ArrayList<>();
        cardList.add(new Card(1l,1111l,1111222233334441l,Date.valueOf("2023-01-01"),111));
        cardList.add(new Card(2l,1111l,1111222233334442l, Date.valueOf("2023-01-01"),112));


        JSONArray jsonObjectsTest = gsonConverter.convertListToGson(cardList);
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
        JSONArray jsonArray = new JSONArray(body);
        // Then
        Assert.assertEquals(jsonArray.toString(),jsonObjectsTest.toString());
    }

    @Test
    public void getBalanceByCardNumber()
            throws IOException {
        String testBalance = "{\"balance\":10000}";
        JSONObject jsonObjectTest = new JSONObject(testBalance);
        // Given
        HttpUriRequest request = new HttpGet( "http://localhost:8000/bank_api/get_balance_by_card_number/1111222233334441" );

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
    public void getBalanceByCardNumberNoNumber()
            throws IOException {
        String testBalance = "{\"balance\":10000}";
        JSONObject jsonObjectTest = new JSONObject(testBalance);
        // Given
        HttpUriRequest request = new HttpGet( "http://localhost:8000/bank_api/get_balance_by_card_number/" );

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
    public void getBalanceByCardNumberInvalid()
            throws IOException {
        String testBalance = "{\"balance\":10000}";
        JSONObject jsonObjectTest = new JSONObject(testBalance);
        // Given
        HttpUriRequest request = new HttpGet( "http://localhost:8000/bank_api/get_balance_by_card_number/11111111111" );

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
            throws IOException {
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
    }*/

    @AfterClass
    public static void stop(){
       httpServerStarter.stop();
       dataService.stop();
    }
}