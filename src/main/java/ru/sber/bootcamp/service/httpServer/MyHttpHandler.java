package ru.sber.bootcamp.service.httpServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;
import ru.sber.bootcamp.controller.ClientController;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

class MyHttpHandler implements HttpHandler {

   private final ClientController controller;

    public MyHttpHandler(ClientController controller) {
        this.controller = controller;
    }

    /**
     * Обработчик входящих соединений
     * @param t - запрос HTTP
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange t) {
        System.out.println("Пришел запрос!!!");
        if (t.getRequestMethod().equals("GET")) {
            try {
                handleGET(t);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                handlePOST(t);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    public void handleGET(HttpExchange t) throws IOException {
        String response;
        String url = t.getRequestURI().toString();
        String[] path = url.split("/");
        System.out.println("Path length: " + path.length);
        switch (path.length>2 ? path[2].toLowerCase(Locale.ROOT) : ""){
            case "show_all": {
                List<JSONObject> jsObjects = controller.getAllAccounts();
                response = jsObjects.stream().map(o->o.toString(4)).collect(Collectors.joining(","));
                break;
            }
            case "get_client": {
                JSONObject jsonObject = controller.getClientByAccountNumber(path.length>3? Long.parseLong(path[3]):-1L);
                response = (!(jsonObject.isEmpty()))?jsonObject.toString(5):"Ошибка!! Введите клиент ИД";
                break;
            }
            case "get_all_cards" : {
                List<JSONObject> jsonObjectList = controller.getAllCards();
                response = jsonObjectList.stream().map(o->o.toString(5))
                .collect(Collectors.joining(",\n")) ;
                break;
            }
            case "get_card_by_account" : {
                List<JSONObject> jsonObjectList = controller.getAllCardsByAccount(path.length>3? Long.parseLong(path[3]):-1L);
                response= (jsonObjectList.size()!=0)?jsonObjectList.stream().map(o->o.toString(5))
                        .collect(Collectors.joining(",\n")):"Введите номер счета клиента";
                break;
            }
            case "get_balance_by_card_number": {
                JSONObject jsonObject = controller.getBalanceByCardNumber(path.length>3? Long.parseLong(path[3]):-1L);
                response = (!(jsonObject.isEmpty()))?jsonObject.toString(5):"Ошибка!! Введите номер карты";
                break;
            }
            default: response = "\nCommand Error!!!";
        }
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        t.getResponseHeaders().set("Content-Type", "application/json; charset=" + StandardCharsets.UTF_8);
        t.sendResponseHeaders(200, bytes.length);
        OutputStream os = t.getResponseBody();
        os.write(bytes);
        os.close();
    }

    private void handlePOST(HttpExchange t) throws Exception {
        System.out.println("req method is :"+t.getRequestMethod());
        System.out.println("req url is: " + t.getRequestURI());
        String[] path = t.getRequestURI().toString().split("/");
        System.out.println("Path length: " + path.length);
        String response;
        switch (path.length>2 ? path[2].toLowerCase(Locale.ROOT) : ""){
            case "balance_inc": {
                InputStreamReader isr = new InputStreamReader(t.getRequestBody(), StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr);
                String query = br.readLine();
                JSONObject jsonObject = new JSONObject(query);
                response = jsonObject.toString(2);
                Long cardNumber = jsonObject.getLong("card_number");
                Double amount = jsonObject.getDouble("amount");
                Integer CVC = jsonObject.getInt("CVC");
                System.out.println("cardNumber: " +cardNumber );
                System.out.println("amount: " + amount);
                controller.UpdateBalanceByCardNumber(cardNumber,amount, CVC);
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

}
