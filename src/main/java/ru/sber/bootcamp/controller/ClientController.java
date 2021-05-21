package ru.sber.bootcamp.controller;

import org.json.JSONObject;
import ru.sber.bootcamp.service.DataConnectionService;
import ru.sber.bootcamp.service.GsonConverter;

import java.util.List;

public class ClientController {

    private final DataConnectionService dataConnectionService;
    private final GsonConverter gsonConverter;

    public ClientController(DataConnectionService dataConnectionService, GsonConverter gsonConverter) {
        this.dataConnectionService = dataConnectionService;
        this.gsonConverter = gsonConverter;
    }

    /**
     * Контроллер для паттерна /text/findAllAccounts
     * @return String of List JsonObjects
     */
    public String getAllAccounts(){
        List<JSONObject> jsObjects = gsonConverter.convertListToGson(dataConnectionService.findAllAccuont());
        StringBuilder sb = new StringBuilder();
        for (JSONObject jsObject : jsObjects) {
            sb.append(jsObject.toString(4));
        }
        return sb.toString();
    }

}
