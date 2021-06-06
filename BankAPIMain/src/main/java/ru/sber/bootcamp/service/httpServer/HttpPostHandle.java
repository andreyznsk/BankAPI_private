package ru.sber.bootcamp.service.httpServer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.lang3.StringUtils;
import ru.sber.bootcamp.controller.ClientController;
import ru.sber.bootcamp.exception.BankApiException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class HttpPostHandle {

    private final ClientController controller;

    public HttpPostHandle(ClientController controller) {
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
                JsonNode query = getQuery(t);
                Long cardNumber = null;
                Double amount = null;
                int CVC = 0;
                cardNumber = query.get("card_number").asLong();
                amount = query.get("amount").asDouble();
                CVC = query.get("CVC_code").asInt();
                response = controller.incrementBalanceByCardNumber(cardNumber, amount, CVC);
                break;
            }
            case "add_card": {
                JsonNode query = getQuery(t);
                Long accountNumber = null;
                accountNumber = query.get("account_number").asLong();
                System.out.println("account_number: " +accountNumber );
                response = controller.addCardByAccountNumber(accountNumber);
                break;
            }

            default: response = "\nCommand Error!!!";
        }
        // Вывести ответ на запрос на страницу пользователя.
        return response;
    }

    private JsonNode getQuery(HttpExchange t) throws IOException {
        InputStreamReader isr = new InputStreamReader(t.getRequestBody(), StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(isr);
    }

}
