package ru.sber.bootcamp.controller;

import org.json.JSONObject;
import ru.sber.bootcamp.model_DAO.entity.Account;
import ru.sber.bootcamp.model_DAO.entity.Card;
import ru.sber.bootcamp.model_DAO.entity.Client;
import ru.sber.bootcamp.model_DAO.repository.AccountRepository;
import ru.sber.bootcamp.model_DAO.repository.CardRepository;
import ru.sber.bootcamp.model_DAO.repository.ClientRepository;
import ru.sber.bootcamp.model_DTO.BalanceDTO;
import ru.sber.bootcamp.model_DTO.BalanceDTOConverter;
import ru.sber.bootcamp.service.GsonConverter;

import java.util.List;

public class ClientController {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final CardRepository cardRepository;
    private final GsonConverter gsonConverter;
    private BalanceDTOConverter balanceDTOConverter;

    public ClientController(AccountRepository accountRepository,
                            ClientRepository clientRepository,
                            CardRepository cardRepository,
                            GsonConverter gsonConverter) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
        this.cardRepository = cardRepository;
        this.gsonConverter = gsonConverter;
        this.balanceDTOConverter = new BalanceDTOConverter();
    }

    /**
     * Контроллер для паттерна /~/show_all
     * Возвращает информацию по счетам для всех клиентов
     * @return String of List JsonObjects
     */
    public List<JSONObject> getAllAccounts(){
        List<JSONObject> jsObjects = gsonConverter.convertListToGson(accountRepository.findAll());
        //return jsObjects.stream().map(t->t.toString(4)).collect(Collectors.joining(","));
        return jsObjects;
    }

    /**
     * Контроллер для паттерна /~/get_client/{accountNumber}
     * Получить клиента по номеру счета
     * @param accountNumber номер счета
     * @return информцию клиента по номеру счета
     */
    public JSONObject getClientByAccountNumber(Long accountNumber){
        JSONObject jsonObject;
        Client client = clientRepository.getClientByAccountNumber(accountNumber);
        jsonObject = gsonConverter.convertObjectToJson(client);
        return jsonObject;
        //return (!(jsonObject.isEmpty()))?jsonObject.toString(5):"Ошибка!! Введите клиент ИД";
    }


    /**
     * Контроллер для паттерна /~/get_all_cards
     * Получить список всех карт
     * @return список всех карт в видей строки
     */
    public  List<JSONObject> getAllCards() {
        return gsonConverter.convertListToGson(cardRepository.getAllCards());

    }

    /**
     * Контроллер для паттерна /~/get_card_by_account/{accountNumber}
     * TODO
     * @param accountNumber
     * @return
     */
    public  List<JSONObject> getAllCardsByAccount(Long accountNumber) {
        return gsonConverter.convertListToGson(cardRepository.getAllCardsByAccountNumber(accountNumber));
    }

    /**
     * Контроллер для паттерна /~/get_balance_by_card_number/{cardNumber}
     *
     * @param cardNumber -  номер карты
     * @return - баланс в виде строки
     */
    public JSONObject getBalanceByCardNumber(Long cardNumber) {
        JSONObject jsonObject;
        Account account = accountRepository.getAccountByCardNumber(cardNumber);
        BalanceDTO balanceDTO = balanceDTOConverter.balanceDTO(account);
        jsonObject = gsonConverter.convertObjectToJson(balanceDTO);
        return jsonObject;
    }

    public void updateBalanceByCardNumber(Long cardNumber, Double amount, int CVC) {

        Card card = cardRepository.getCardByCardNumber(cardNumber);
        if(card==null) {
            System.out.println("Карта не найдена");
            return;
        }
        System.out.println(card);
        if(card.getCVC_code()==CVC ){
            Account account = accountRepository.getAccountByCardNumber(cardNumber);
            account.incBalance(amount);
            accountRepository.updateAccount(account);
        }

    }

    public void addCardByAccountNumber(Long accountNumber) {
        if(accountNumber == null) {
            return;
        }
        Card card = new Card();
        cardRepository.addCardByAccountNumber(card);
    }
}
