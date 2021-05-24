package ru.sber.bootcamp.controller;

import org.json.JSONObject;
import ru.sber.bootcamp.model.entity.Client;
import ru.sber.bootcamp.model.repository.AccountRepository;
import ru.sber.bootcamp.model.repository.CardRepository;
import ru.sber.bootcamp.model.repository.ClientRepository;
import ru.sber.bootcamp.service.GsonConverter;

import java.util.List;
import java.util.stream.Collectors;

public class ClientController {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final CardRepository cardRepository;
    private final GsonConverter gsonConverter;

    public ClientController(AccountRepository accountRepository,
                            ClientRepository clientRepository,
                            CardRepository cardRepository,
                            GsonConverter gsonConverter) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
        this.cardRepository = cardRepository;
        this.gsonConverter = gsonConverter;
    }

    /**
     * Контроллер для паттерна /text/findAllAccounts
     * Возвращает всех информацию по счетам для всех клиентов
     * @return String of List JsonObjects
     */
    public String getAllAccounts(){
        List<JSONObject> jsObjects = gsonConverter.convertListToGson(accountRepository.findAll());
        return jsObjects.stream().map(t->t.toString(4)).collect(Collectors.joining(","));
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

    public String getAllCards() {
        List<JSONObject> jsonObjectList = gsonConverter.convertListToGson(cardRepository.getAllCards());
       return jsonObjectList.stream().map(t->t.toString(5))
                .collect(Collectors.joining(","));

    }
}
