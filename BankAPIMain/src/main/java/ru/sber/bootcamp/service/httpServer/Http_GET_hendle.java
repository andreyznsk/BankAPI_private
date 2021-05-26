package ru.sber.bootcamp.service.httpServer;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;
import ru.sber.bootcamp.controller.ClientController;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Http_GET_hendle {
    private final ClientController controller;

    public Http_GET_hendle(ClientController controller) {
        this.controller = controller;
    }

    protected void handleGET(HttpExchange t) throws IOException {
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
}
