package ru.sber.bootcamp.service.httpServer;


import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.sber.bootcamp.configuration.DataBaseConfig;
import ru.sber.bootcamp.controller.ClientController;
import ru.sber.bootcamp.model_DAO.repository.*;
import ru.sber.bootcamp.service.DataConnectionService;
import ru.sber.bootcamp.service.GsonConverter;
import ru.sber.bootcamp.service.GsonConverterImpl;
import ru.sber.bootcamp.service.H2ConnectionServiceImpl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;


@RunWith(Parameterized.class)
public class MASS_TestDefault {

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
    Object userUrl;


    public MASS_TestDefault(String serverResponse, Object accountNumber) {
        this.serverResponse = serverResponse;
        this.userUrl = accountNumber;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {

        return Arrays.asList(new Object[][]{
                {"{\"Error\":\"CommandError\"}" ,null},
                {"{\"Error\":\"CommandError\"}",1L},
                {"{\"Error\":\"CommandError\"}",2L},
                {"{\"Error\":\"CommandError\"}" , 1111L},
                {"{\"Error\":\"CommandError\"}",123123123123L},
                {"{\"Error\":\"CommandError\"}", "Pepsi-Cola"},
        });
    }




    @Test
    public void test() throws IOException {
        URL url = new URL("http://localhost:8000/bank_api/" + ((userUrl !=null)? userUrl :""));
        URLConnection conn = url.openConnection();
        conn.setDoOutput(false);

        StringBuilder sb = new StringBuilder();
        InputStreamReader isr = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8);
        Scanner sc = new Scanner(isr);
        while (sc.hasNextLine())  {
            sb.append(sc.next());
        }
        String body = sb.toString();
        Assert.assertEquals(serverResponse,body);
    }

    @AfterClass
    public static void stop(){
        httpServerStarter.stop();
        dataService.stop();
    }

}

