package ru.sber.bootcamp.controller;

import org.json.JSONObject;
import ru.sber.bootcamp.model.entity.Client;
import ru.sber.bootcamp.model.repository.AccountRepository;
import ru.sber.bootcamp.model.repository.ClientRepository;
import ru.sber.bootcamp.service.GsonConverter;

import java.util.List;

public class ClientController {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final GsonConverter gsonConverter;

    public ClientController(AccountRepository accountRepository, ClientRepository clientRepository, GsonConverter gsonConverter) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
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
     * @param accountNumber
     * @return
     */
    public String getClientByAccountNumber(Long accountNumber){
        JSONObject jsonObject;
        Client client = clientRepository.getClientByAccountNumber(accountNumber);
        jsonObject = gsonConverter.convertObjectToJson(client);
        return (!(jsonObject.isEmpty()))?jsonObject.toString(5):"Ошибка!! Введите клиент ИД";
    }

}
