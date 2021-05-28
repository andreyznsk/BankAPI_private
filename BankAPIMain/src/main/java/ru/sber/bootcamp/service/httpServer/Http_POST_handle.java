package ru.sber.bootcamp.service.httpServer;

import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import ru.sber.bootcamp.controller.ClientController;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.zip.DataFormatException;

public class Http_POST_handle {

    private final ClientController controller;

    public Http_POST_handle(ClientController controller) {
        this.controller = controller;
    }

    protected String handlePOST(HttpExchange t) throws Exception {
        System.out.println("req method is :"+t.getRequestMethod());
        System.out.println("req url is: " + t.getRequestURI());
        String[] path = t.getRequestURI().toString().split("/");
        System.out.println("Path length: " + path.length);
        String response;
        switch (path.length>2 ? path[2].toLowerCase(Locale.ROOT) : ""){
            case "balance_inc": {
                String query = getQuery(t);
                JSONObject jsonObject = new JSONObject(query);
                JSONObject jsonObjectResponse;
                Long cardNumber = null;
                Double amount = null;
                int CVC = 0;
                try {
                    cardNumber = jsonObject.getLong("card_number");
                    amount = jsonObject.getDouble("amount");
                    CVC = jsonObject.getInt("CVC_code");
                } catch (JSONException e) {
                    String message = StringUtils.substringBetween(e.getMessage(),"\"","\"");
                    throw new NullPointerException(message + ":Not_found");
                }
                jsonObjectResponse = controller.incrementBalanceByCardNumber(cardNumber, amount, CVC);
                response = jsonObjectResponse.toString();
                break;
            }
            case "add_card": {
                InputStreamReader isr = new InputStreamReader(t.getRequestBody(), StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr);
                String query = br.readLine();
                JSONObject clientReques = new JSONObject(query);
                Long accountNumber;
                try {
                    accountNumber = clientReques.getLong("account_number");
                } catch (JSONException e) {
                    String message = StringUtils.substringBetween(e.getMessage(),"\"","\"");
                    throw new NullPointerException(message + ":Not_found");
                }
                System.out.println("account_number: " +accountNumber );
                JSONObject jsonObject = controller.addCardByAccountNumber(accountNumber);
                response = jsonObject.toString();
                break;
            }

            default: response = "\nCommand Error!!!";
        }
        // Вывести ответ на запрос на страницу пользователя.
        return response;
    }

    private String getQuery(HttpExchange t) throws IOException {
        InputStreamReader isr = new InputStreamReader(t.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        return br.readLine();
    }

}
