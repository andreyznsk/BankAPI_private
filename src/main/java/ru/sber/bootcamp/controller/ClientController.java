package ru.sber.bootcamp.controller;

import org.json.JSONObject;
import ru.sber.bootcamp.model.entity.Client;
import ru.sber.bootcamp.model.repository.AccountRepository;
import ru.sber.bootcamp.service.DataConnectionService;
import ru.sber.bootcamp.service.GsonConverter;

import java.util.List;

public class ClientController {

    private final AccountRepository accountRepository;
    private final GsonConverter gsonConverter;

    public ClientController(AccountRepository accountRepository, GsonConverter gsonConverter) {
        this.accountRepository = accountRepository;
        this.gsonConverter = gsonConverter;
    }

    /**
     * Контроллер для паттерна /text/findAllAccounts
     * Возвращает всех информацию по счетам для всех клиентов
     * @return String of List JsonObjects
     */
    public String getAllAccounts(){
        List<JSONObject> jsObjects = gsonConverter.convertListToGson(accountRepository.findAll());
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
        Client client = accountRepository.getClientByAccountNumber(id);
        jsonObject = gsonConverter.convertObjectToJson(client);
        return (!(jsonObject == null))?jsonObject.toString(5):"Ошибка!! Введите слиент ИД";
    }

}
