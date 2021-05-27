package ru.sber.bootcamp.service.httpServer;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONArray;
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

    protected String handleGET(HttpExchange t) throws IOException {
        String response;
        String url = t.getRequestURI().toString();
        String[] path = url.split("/");
        System.out.println("Path length: " + path.length);
        switch (path.length>2 ? path[2].toLowerCase(Locale.ROOT) : ""){
            case "show_all_accounts": {//TESTED
                JSONArray allAccounts = controller.getAllAccounts();
                response = allAccounts.toString(4);
                break;
            }
            case "get_client_by_account_number": {
                JSONObject jsonObject = controller.getClientByAccountNumber(path.length>3? Long.parseLong(path[3]):null);
                response = jsonObject.toString(4);
                break;
            }
            case "get_all_cards" : {//TESTED!!!
                JSONArray jsonObjectList = controller.getAllCards();
                response = jsonObjectList.toString(5) ;
                break;
            }
            case "get_card_by_account" : {//TESTED!!!!
                JSONArray jsonObjectList = controller.getAllCardsByAccount(path.length>3? Long.parseLong(path[3]):null);
                response= jsonObjectList.toString(5);
                break;
            }
            case "get_balance_by_card_number": {//TESTED!!!
                JSONObject jsonObject = controller.getBalanceByCardNumber(path.length>3? Long.parseLong(path[3]):null);
                response = jsonObject.toString();
                break;
            }
            default: {//TESTED!!!
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Error","Command Error");
                response = jsonObject.toString();

            }
        }
       return response;
    }
}
