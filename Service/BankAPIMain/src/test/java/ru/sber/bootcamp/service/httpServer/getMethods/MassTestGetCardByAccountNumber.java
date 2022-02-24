package ru.sber.bootcamp.service.httpServer.getMethods;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
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
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@RunWith(Parameterized.class)
public class MassTestGetCardByAccountNumber {

    static DataConnectionService dataService;
    static AccountRepository accountRepository;
    static ClientRepository clientRepository;
    static CardRepository cardRepository;
    static ClientController controller;
    static HttpServerStarter httpServerStarter;
    static ObjectWriter objectWriter;

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

    String serverResponseExpected;
    String accountNumber;


    public MassTestGetCardByAccountNumber(String serverResponse, String accountNumber) {
        this.serverResponseExpected = serverResponse;
        this.accountNumber = accountNumber;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() throws JsonProcessingException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter()
                .withoutSpacesInObjectEntries()
                .withArrayIndenter(new DefaultPrettyPrinter.NopIndenter());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        objectWriter = new ObjectMapper().writer()
                .with(pp)
                .with(df);
        List<Card> cardList =  new ArrayList<>();
        cardList.add(new Card(1l,"1111","1111222233334441",Date.valueOf("2023-01-01"),111));
        cardList.add(new Card(2l,"1111","1111222233334442", Date.valueOf("2023-01-01"),112));
        return Arrays.asList(new Object[][]{
                {"{\"Error!\":\"InputAccountNumber\"}" ,""},
                {"{\"Error!\":\"IncorrectAccountNumber\"}","1"},
                {"{\"Error!\":\"IncorrectAccountNumber\"}","2"},
                {objectWriter.writeValueAsString(cardList), "1111"},
                {"{\"Error!\":\"IncorrectAccountNumber\"}","123123123123"},
                {"{\"Error!\":\"IncorrectAccountNumber\"}", "Pepsi-Cola"},
        });
    }




    @Test
    public void test() throws IOException {
        URL url = new URL("http://localhost:8000/bank_api/get_card_by_account/" + accountNumber);
        System.out.println("url: " + url);
        URLConnection conn = url.openConnection();
        conn.setDoOutput(false);
        InputStreamReader isr = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8);
        JsonNode jsonNodeActual = new ObjectMapper().readTree(isr);
        JsonNode jsonNodeExpected = new ObjectMapper().readTree(serverResponseExpected);
        Assert.assertEquals(jsonNodeExpected,jsonNodeActual);
    }

    @AfterClass
    public static void stop(){
        httpServerStarter.stop();
        dataService.stop();
    }

}
