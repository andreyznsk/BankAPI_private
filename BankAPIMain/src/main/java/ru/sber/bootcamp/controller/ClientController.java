package ru.sber.bootcamp.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ru.sber.bootcamp.exception.BankApiException;
import ru.sber.bootcamp.modelDao.entity.Account;
import ru.sber.bootcamp.modelDao.entity.Card;
import ru.sber.bootcamp.modelDao.entity.Client;
import ru.sber.bootcamp.modelDao.repository.AccountRepository;
import ru.sber.bootcamp.modelDao.repository.CardRepository;
import ru.sber.bootcamp.modelDao.repository.ClientRepository;
import ru.sber.bootcamp.modelDto.BalanceDto;
import ru.sber.bootcamp.modelDto.BalanceDtoConverter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static ru.sber.bootcamp.configuration.MyServerMessage.*;

public class ClientController {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final CardRepository cardRepository;
    private final BalanceDtoConverter balanceDTOConverter;
    private final Random random;
    private final ObjectWriter objectWriter;

    public ClientController(AccountRepository accountRepository,
                            ClientRepository clientRepository,
                            CardRepository cardRepository) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
        this.cardRepository = cardRepository;
        this.balanceDTOConverter = new BalanceDtoConverter();
        this.random = new Random();

        DefaultPrettyPrinter pp = new DefaultPrettyPrinter()
                .withoutSpacesInObjectEntries()
                .withArrayIndenter(new DefaultPrettyPrinter.NopIndenter());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        this.objectWriter = new ObjectMapper()
                .writer()
                .with(pp)
                .with(df);
    }

    /**
     * Контроллер для паттерна /~/show_all
     * Возвращает информацию по счетам для всех клиентов
     * @return String of List JsonObjects
     */
    public String getAllAccounts() throws JsonProcessingException {

        List<Account> accountsObj = accountRepository.findAll();
        return objectWriter.writeValueAsString(accountsObj);

    }

    /**
     * Контроллер для паттерна /~/get_client/{accountNumber}
     * Получить клиента по номеру счета
     * @param accountNumber номер счета
     * @return информцию клиента по номеру счета
     */
    public String getClientByAccountNumber(String accountNumber) throws JsonProcessingException {
        if(accountNumber == null) {
            System.out.println("null");
            throw new NullPointerException("Input_account_number");
        }
        Client client = clientRepository.getClientByAccountNumber(accountNumber);
        return objectWriter.writeValueAsString(client);
    }


    /**
     * Контроллер для паттерна /~/get_all_cards
     * Получить список всех карт
     * @return список всех карт в видей строки
     */
    public  String getAllCards() throws JsonProcessingException {
        List<Card> allCards = cardRepository.getAllCards();
        return objectWriter.writeValueAsString(allCards);
    }

    /**
     * Контроллер для паттерна /~/get_card_by_account/{accountNumber}
     *
     * @param accountNumber - Номер счета
     * @return - Массив всех карт по номеру счета
     */
    public  String getAllCardsByAccount(String accountNumber) throws NullPointerException, JsonProcessingException {
        if(accountNumber == null) {
            System.out.println("null");
            throw new NullPointerException("Input Account number");
        }
            //gsonConverter.convertListToGson();
        List<Card> allCardsByAccountNumber = cardRepository.getAllCardsByAccountNumber(accountNumber);
        return objectWriter.writeValueAsString(allCardsByAccountNumber);
    }

    /**
     * Контроллер для паттерна /~/get_balance_by_card_number/{cardNumber}
     *
     * @param cardNumber -  номер карты
     * @return - баланс в виде строки
     */
    public String getBalanceByCardNumber(String cardNumber) throws Exception {
        if(cardNumber == null) {
            System.out.println("null");
            throw new NullPointerException("Input_Card_number");
        }
        Account account = accountRepository.getAccountByCardNumber(cardNumber);
        if(account.getBalance()==null) {
            System.out.println("Card_Number_incorrect");
            throw new BankApiException("Card_Number_incorrect");
        }
        BalanceDto balanceDTO = balanceDTOConverter.balanceDTO(account);
        return objectWriter.writeValueAsString(balanceDTO);
    }

    /**
     * Добавить сумму на баланс счета по карте
     * Если сумма отрицательная вернуть сообщение - сумма отрицательная
     * Получить карту из базы, проверить код карты если совпадает то добавить сумма
     * @param cardNumber - номер карты
     * @param amount - сумма
     * @param CVC - Код карты
     */
    public String incrementBalanceByCardNumber(String cardNumber, Double amount, int CVC)
            throws BankApiException {
        if(amount < 0) {
            throw new BankApiException("Amount_is_negative");
        }
        Card card = cardRepository.getCardByCardNumber(cardNumber);
        ObjectNode serverResponse = new ObjectMapper().createObjectNode();
        if(card==null) {
            System.out.println("Карта не найдена");
            serverResponse.put(ERROR_MESSAGE.message, "Card_not_found");
            return serverResponse.asText();
        }
        System.out.println(card);
        if(card.getCVC_code()==CVC ){
            Account account = accountRepository.getAccountByCardNumber(cardNumber);
            account.incBalance(amount);
           int result = accountRepository.updateAccount(account);
           if(result == 1) {
               serverResponse.put(SERVER_OK.message,"Balance_updated_ok");
           }
        } else {
            System.out.println("CVC code invalid");
            serverResponse.put(ERROR_MESSAGE.message,"CVC_code_invalid");
        }
        return serverResponse.asText();

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
    public String addCardByAccountNumber(String accountNumber) throws BankApiException {
        ObjectNode serverResponse = new ObjectMapper().createObjectNode();
        if(accountNumber == null) {
            throw new BankApiException("Account number is empty!");
        }
        Client client = clientRepository.getClientByAccountNumber(accountNumber);

        if(client.getAccount().getAccountNumber() == null){
            serverResponse.put(SERVER_ERROR.message,"Account number incorrect");
        } else {
            Card card = new Card();
            Long cartNumber = null;
            do{
                cartNumber = getRandomLong(1000_0000_0000_0000L,9999_9999_9999_9999L);
            } while (cardRepository.isCardExist(cartNumber.toString()));

            int CVC = getRandomCVC(999);
            Date date = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.YEAR,3);
            Date updateDate = c.getTime();
            card.setCardNumber(cartNumber.toString());
            System.out.println("Generated card number: " + cartNumber);
            card.setAccountNumber(client.getAccount().getAccountNumber());
            card.setCVC_code(CVC);
            card.setDateValidThru(updateDate);
            int result = cardRepository.addCardByAccountNumber(card);
            if (result == 0)  {
                serverResponse.put(SERVER_ERROR.message,"Card not added");
            } else {
                serverResponse.put(SERVER_OK.message,"Card_added");
            }
        }
        return serverResponse.asText();
    }

    private int getRandomCVC(int max) {
        return random.nextInt(max);
    }

    private Long getRandomLong(long min, long max) {
        return random.nextLong() % (max - min) + max;
    }


}
