package ru.sber.bootcamp.service.httpServer.GET_Methods;


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
public class MASS_TestGetAllCards {

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


    public MASS_TestGetAllCards(String serverResponse, Object accountNumber) {
        this.serverResponse = serverResponse;
        this.userUrl = accountNumber;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        String serverResponse = "[{\"CVC_code\":111,\"dateValidThru\":\"2023-01-01\",\"id\":1,\"accountNumber\":1111,\"cardNumber\":1111222233334441}," +
                "{\"CVC_code\":112,\"dateValidThru\":\"2023-01-01\",\"id\":2,\"accountNumber\":1111,\"cardNumber\":1111222233334442}," +
                "{\"CVC_code\":121,\"dateValidThru\":\"2023-01-01\",\"id\":3,\"accountNumber\":1112,\"cardNumber\":1112222233334441}," +
                "{\"CVC_code\":122,\"dateValidThru\":\"2023-01-01\",\"id\":4,\"accountNumber\":1112,\"cardNumber\":1112222233334442}," +
                "{\"CVC_code\":123,\"dateValidThru\":\"2023-01-01\",\"id\":5,\"accountNumber\":1112,\"cardNumber\":1112222233334443}," +
                "{\"CVC_code\":124,\"dateValidThru\":\"2023-01-01\",\"id\":6,\"accountNumber\":1112,\"cardNumber\":1112222233334444}]";

        return Arrays.asList(new Object[][]{
                {serverResponse ,null},
                {serverResponse,1L},
                {serverResponse,2L},
                {serverResponse , 1111L},
                {serverResponse,123123123123L},
                {serverResponse, "Pepsi-Cola"},
        });
    }




    @Test
    public void test() throws IOException {
        URL url = new URL("http://localhost:8000/bank_api/get_all_cards/" + ((userUrl !=null)? userUrl :""));
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

