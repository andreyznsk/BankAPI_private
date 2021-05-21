package ru.sber.bootcamp.controller;

import org.json.JSONObject;
import ru.sber.bootcamp.model.entity.Client;
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
     * Возвращает всех информацию по счетам для всех клиентов
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

    /**
     *
     * @param id
     * @return
     */
    public String getClientByAccountNumber(Long id){
        JSONObject jsonObject;
        Client client = dataConnectionService.getClientByAccountNumber(id);
        jsonObject = gsonConverter.convertObjectToJson(client);
        return (!(jsonObject == null))?jsonObject.toString(5):"Ошибка!! Введите слиент ИД";
    }

}
