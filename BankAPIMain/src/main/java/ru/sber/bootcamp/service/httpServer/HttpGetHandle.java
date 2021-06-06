package ru.sber.bootcamp.service.httpServer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.sber.bootcamp.configuration.MyServerMessage;
import ru.sber.bootcamp.controller.ClientController;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static ru.sber.bootcamp.configuration.MyServerMessage.ERROR_MESSAGE;

public class HttpGetHandle {
    private final ClientController controller;

    public HttpGetHandle(ClientController controller) {
        this.controller = controller;
    }

    protected String handleGET(HttpExchange t) throws IOException {
        String response;
        String url = t.getRequestURI().toString();
        String[] path = url.split("/");
        System.out.println("Path length: " + path.length);
        switch (path.length>2 ? path[2].toLowerCase(Locale.ROOT) : ""){
            case "show_all_accounts": {//TESTED
                response = controller.getAllAccounts();
                break;
            }
            case "get_client_by_account_number": {
                response =  controller.getClientByAccountNumber(path.length>3? Long.parseLong(path[3]):null);
                break;
            }
            case "get_all_cards" : {//TESTED!!!
                response = controller.getAllCards();
                break;
            }
            case "get_card_by_account" : {//TESTED!!!!
                response = controller.getAllCardsByAccount(path.length>3? Long.parseLong(path[3]):null);
                break;
            }
            case "get_balance_by_card_number": {//TESTED!!!
                response = controller.getBalanceByCardNumber(path.length>3? Long.parseLong(path[3]):null);
                break;
            }
            default: {//TESTED!!!
                ObjectNode serverResponse = new ObjectMapper().createObjectNode();
                serverResponse.put(ERROR_MESSAGE.message,"Command Error");
                response = serverResponse.asText();

            }
        }
       return response;
    }
}
