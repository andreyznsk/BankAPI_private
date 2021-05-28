package ru.sber.bootcamp.controller;


import org.json.JSONArray;
import org.json.JSONObject;
import ru.sber.bootcamp.configuration.MyErrorMessage;
import ru.sber.bootcamp.model_DAO.entity.Account;
import ru.sber.bootcamp.model_DAO.entity.Card;
import ru.sber.bootcamp.model_DAO.entity.Client;
import ru.sber.bootcamp.model_DAO.repository.AccountRepository;
import ru.sber.bootcamp.model_DAO.repository.CardRepository;
import ru.sber.bootcamp.model_DAO.repository.ClientRepository;
import ru.sber.bootcamp.model_DTO.BalanceDTO;
import ru.sber.bootcamp.model_DTO.BalanceDTOConverter;
import ru.sber.bootcamp.service.GsonConverter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static ru.sber.bootcamp.configuration.MyErrorMessage.*;

public class ClientController {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final CardRepository cardRepository;
    private final GsonConverter gsonConverter;
    private final BalanceDTOConverter balanceDTOConverter;
    private Random random;

    public ClientController(AccountRepository accountRepository,
                            ClientRepository clientRepository,
                            CardRepository cardRepository,
                            GsonConverter gsonConverter) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
        this.cardRepository = cardRepository;
        this.gsonConverter = gsonConverter;
        this.balanceDTOConverter = new BalanceDTOConverter();
        this.random = new Random();
    }

    /**
     * Контроллер для паттерна /~/show_all
     * Возвращает информацию по счетам для всех клиентов
     * @return String of List JsonObjects
     */
    public JSONArray getAllAccounts(){
        //return jsObjects.stream().map(t->t.toString(4)).collect(Collectors.joining(","));
        return gsonConverter.convertListToGson(accountRepository.findAll());
    }

    /**
     * Контроллер для паттерна /~/get_client/{accountNumber}
     * Получить клиента по номеру счета
     * @param accountNumber номер счета
     * @return информцию клиента по номеру счета
     */
    public JSONObject getClientByAccountNumber(Long accountNumber){
        if(accountNumber == null) {
            System.out.println("null");
            throw new NullPointerException("Input_account_number");
        }
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
    public  JSONArray getAllCards() {
        return gsonConverter.convertListToGson(cardRepository.getAllCards());

    }

    /**
     * Контроллер для паттерна /~/get_card_by_account/{accountNumber}
     * TODO
     * @param accountNumber
     * @return
     */
    public  JSONArray getAllCardsByAccount(Long accountNumber) throws NullPointerException {
        if(accountNumber == null) {
            System.out.println("null");
            throw new NullPointerException("Input Account number");
        }
            //gsonConverter.convertListToGson();
        return gsonConverter.convertListToGson(cardRepository.getAllCardsByAccountNumber(accountNumber));
    }

    /**
     * Контроллер для паттерна /~/get_balance_by_card_number/{cardNumber}
     *
     * @param cardNumber -  номер карты
     * @return - баланс в виде строки
     */
    public JSONObject getBalanceByCardNumber(Long cardNumber) {
        if(cardNumber == null) {
            System.out.println("null");
            throw new NullPointerException("Input_Card_number");
        }
        JSONObject jsonObject;
        Account account = accountRepository.getAccountByCardNumber(cardNumber);
        if(account.getBalance()==null) {
            System.out.println("Card_Number_incorrect");
            throw new NullPointerException("Card_Number_incorrect");
        }
        BalanceDTO balanceDTO = balanceDTOConverter.balanceDTO(account);
        jsonObject = gsonConverter.convertObjectToJson(balanceDTO);
        return jsonObject;
    }

    /**
     *
     * @param cardNumber
     * @param amount
     * @param CVC
     */
    public JSONObject incrementBalanceByCardNumber(Long cardNumber, Double amount, int CVC) {
        if(amount < 0) {
            throw new NullPointerException("Amount_is_negative");
        }
        Card card = cardRepository.getCardByCardNumber(cardNumber);
        JSONObject jsonObject = new JSONObject();
        if(card==null) {
            System.out.println("Карта не найдена");
            jsonObject = new JSONObject();
            jsonObject.put(ERROR_MESSAGE.message, "Card_not_found");
            return jsonObject;
        }
        System.out.println(card);
        if(card.getCVC_code()==CVC ){
            Account account = accountRepository.getAccountByCardNumber(cardNumber);
            account.incBalance(amount);
           int result = accountRepository.updateAccount(account);
           if(result == 1) {
               jsonObject.put(SERVER_OK.message,"Balance_updated_ok");
           }
        } else {
            System.out.println("CVC code invalid");
            jsonObject = new JSONObject();
            jsonObject.put("Error!","CVC_code_invalid");
        }

        return jsonObject;

    }

    /**
     * Выпуст ковой карьты: Достаем клиента из базы по номеру счета если клиента нет, возвращаем отрицательный результат
     * если клиент находится: - генерируем случайны номер карты, проверям чть такого номера нет,
     *                         - генерируем CVC код,
     *                         - создаем дату и добвляем три года
     * и добавляем карту в список
     * @param accountNumber - Номер счета по которому необходимо выпустить карту
     *
     * @return - Резултат добавления, либо исключение если номер пуст
     */
    public JSONObject addCardByAccountNumber(Long accountNumber) throws NullPointerException {
        JSONObject jsonObject = new JSONObject();
        if(accountNumber == null) {
            throw new NullPointerException("Account number is empty!");
        }
        Client client = clientRepository.getClientByAccountNumber(accountNumber);

        if(client.getAccount().getAccountNumber() == null){
            jsonObject.put(SERVER_ERROR.message,"Account number incorrect");
        } else {
            Card card = new Card();
            Long cartNumber;
            do{
                cartNumber = getRandomLong(1000_0000_0000_0000L,9999_9999_9999_9999L);
            } while (cardRepository.isCardExist(cartNumber));

            int CVC = getRandomCVC(999);
            Date date = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.YEAR,3);
            Date updateDate = c.getTime();
            card.setCardNumber(cartNumber);
            System.out.println("Generated card number: " + cartNumber);
            card.setAccountNumber(client.getAccount().getAccountNumber());
            card.setCVC_code(CVC);
            card.setDateValidThru(updateDate);
            int result = cardRepository.addCardByAccountNumber(card);
            if (result == 0)  {
                jsonObject.put(SERVER_ERROR.message,"Card not added");
            } else {
                jsonObject.put(SERVER_OK.message,"Card_added");
            }
        }
        return jsonObject;
    }

    private int getRandomCVC(int max) {
        return random.nextInt(999);
    }

    private Long getRandomLong(long min, long max) {
        return random.nextLong() % (max - min) + max;
    }


}
