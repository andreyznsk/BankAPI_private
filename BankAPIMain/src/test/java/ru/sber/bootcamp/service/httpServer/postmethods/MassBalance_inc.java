package ru.sber.bootcamp.service.httpServer.postmethods;


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
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;


@RunWith(Parameterized.class)
public class MassBalance_inc {

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
    Double amount;
    Integer CVC_code;
    Object card_number;


    public MassBalance_inc(String serverResponse, Double amount, Integer CVC_code, Object card_number) {
        this.serverResponse = serverResponse;
        this.amount = amount;
        this.CVC_code = CVC_code;
        this.card_number = card_number;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {

        return Arrays.asList(new Object[][]{
                {"{\"Error!\":\"Card_not_found\"}" ,111.1,111,1111122221L},
                {"{\"Error!\":\"card_number:Not_found\"}" ,111.1,111,"Black Power!"},
                {"{\"Error!\":\"amount:Not_found\"}" ,null,111,1111122221L},
                {"{\"Error!\":\"CVC_code:Not_found\"}"  ,111.1,null,1111122221L},
                {"{\"Error!\":\"card_number:Not_found\"}" ,111.1,111,null},
                {"{\"Error!\":\"card_number:Not_found\"}" ,null,null,null},
                {"{\"Error!\":\"Card_not_found\"}" ,111.1,111,1111122221L},
                {"{\"Server_OK!\":\"Balance_updated_ok\"}" ,111.1,111,1111222233334441L},
                {"{\"Error!\":\"CVC_code_invalid\"}" ,111.1,111,1111222233334442L},
                {"{\"Error!\":\"Amount_is_negative\"}" ,-111.1,111,1111222233334441L},
        });
    }




    @Test
    public void postBalanceIncHandler() throws IOException {
        JSONObject jsonQuery = new JSONObject();
        jsonQuery.put("amount",amount);
        jsonQuery.put("CVC_code",CVC_code);
        jsonQuery.put("card_number",card_number);
        JSONObject jsonObjectResponseTest = new JSONObject(serverResponse);
        // Given
        URL url = new URL("http://localhost:8000/bank_api/balance_inc");
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
        System.out.println("SErver response: TEST: " + sb.toString());
        JSONObject jsonObject = new JSONObject(sb.toString());
        JSONAssert.assertEquals( jsonObjectResponseTest,jsonObject, JSONCompareMode.STRICT);

    }

    @AfterClass
    public static void stop(){
        httpServerStarter.stop();
        dataService.stop();
    }

}

