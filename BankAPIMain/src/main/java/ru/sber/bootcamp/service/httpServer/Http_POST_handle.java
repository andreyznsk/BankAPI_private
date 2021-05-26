package ru.sber.bootcamp.service.httpServer;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;
import ru.sber.bootcamp.controller.ClientController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class Http_POST_handle {

    private final ClientController controller;

    public Http_POST_handle(ClientController controller) {
        this.controller = controller;
    }

    protected void handlePOST(HttpExchange t) throws Exception {
        System.out.println("req method is :"+t.getRequestMethod());
        System.out.println("req url is: " + t.getRequestURI());
        String[] path = t.getRequestURI().toString().split("/");
        System.out.println("Path length: " + path.length);
        String response;
        switch (path.length>2 ? path[2].toLowerCase(Locale.ROOT) : ""){
            case "balance_inc": {
                String query = getQuery(t);
                JSONObject jsonObject = new JSONObject(query);
                //response = jsonObject.toString();
                Long cardNumber = jsonObject.getLong("card_number");
                Double amount = jsonObject.getDouble("amount");
                int CVC = jsonObject.getInt("CVC_code");
                System.out.println("cardNumber: " +cardNumber );
                System.out.println("amount: " + amount);
                System.out.println("CVC code" + CVC);
                int result = controller.updateBalanceByCardNumber(cardNumber, amount, CVC);
                if (result == 1) {
                    response = "Balanceupdated";
                } else if (result == -1) {
                    response = "Cardnotfound";
                } else if (result == -2) {
                    response = "InvalidCVCcode";
                }
                else {
                    response = "Balance not updated";
                }
                break;
            }
            case "add_card": {
                InputStreamReader isr = new InputStreamReader(t.getRequestBody(), StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr);
                String query = br.readLine();
                JSONObject jsonObject = new JSONObject(query);
                response = jsonObject.toString();
                Long accountNumber = jsonObject.getLong("account_number");
                System.out.println("account_number: " +accountNumber );
                controller.addCardByAccountNumber(accountNumber);
                break;
            }

            default: response = "\nCommand Error!!!";
        }
        // Вывести ответ на запрос на страницу пользователя.
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        t.sendResponseHeaders(200, bytes.length);
        OutputStream os = t.getResponseBody();
        os.write(bytes);
        os.close();
    }

    private String getQuery(HttpExchange t) throws IOException {
        InputStreamReader isr = new InputStreamReader(t.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        return br.readLine();
    }

}
