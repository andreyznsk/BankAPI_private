package ru.sberbank.main.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.sberbank.main.exception.BankApiException;
import ru.sberbank.main.modelDAO.entity.Account;
import ru.sberbank.main.modelDAO.entity.Card;
import ru.sberbank.main.modelDAO.entity.Client;
import ru.sberbank.main.modelDAO.repository.AccountRepository;
import ru.sberbank.main.modelDAO.repository.CardRepository;
import ru.sberbank.main.modelDAO.repository.ClientRepository;
import ru.sberbank.main.modelDTO.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


@RequiredArgsConstructor
@RestController
public class ClientController {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final CardRepository cardRepository;
    private final BalanceDtoConverter balanceDTOConverter;
    private final Random random;


    /**
     * Контроллер для паттерна /~/show_all
     * Возвращает информацию по счетам для всех клиентов
     * @return String of List JsonObjects
     */

    @GetMapping(value = "/show_all",
            produces = {"application/json; charset=UTF-8"}
    )
    public List<Account> getAllAccounts() {
      return accountRepository.findAll();
    }

    /**
     * Контроллер для паттерна /~/get_client/{accountNumber}
     * Получить клиента по номеру счета
     * @param accountNumber номер счета
     * @return информцию клиента по номеру счета
     */
    @GetMapping(value = "/get_client/{accountNumber}",
            produces = {"application/json; charset=UTF-8"}
            )
    public Client getClientByAccountNumber(@PathVariable String accountNumber) throws BankApiException {
        if(accountNumber == null) {
            System.out.println("null");
            throw new BankApiException("InputAccountNumber");
        }
        return clientRepository.getClientByAccount_AccountNumber(accountNumber);
    }


    /**
     * Контроллер для паттерна /~/get_all_cards
     * Получить список всех карт
     * @return список всех карт в видей строки
     */
    @GetMapping(value ="/get_all_cards",
            produces = {"application/json; charset=UTF-8"}
            )
    public  List<Card> getAllCards() throws JsonProcessingException {
        return cardRepository.findAll();
    }

    /**
     * Контроллер для паттерна /~/get_card_by_account/{accountNumber}
     *
     * @param accountNumber - Номер счета
     * @return - Массив всех карт по номеру счета
     */
    @GetMapping(value = "/get_card_by_account/{accountNumber}",
            produces = {"application/json; charset=UTF-8"}
            )
    public  List<Card> getAllCardsByAccount(@PathVariable String accountNumber) throws BankApiException {
        if(accountNumber == null) {
            System.out.println("null");
            throw new BankApiException("InputAccountNumber");
        }
        return cardRepository.getAllCardsByAccountNumber(accountNumber);
    }

    /**
     * Контроллер для паттерна /~/get_balance_by_card_number/{cardNumber}
     *
     * @param cardNumber -  номер карты
     * @return - баланс в виде строки
     */
    @GetMapping(value = "get_balance_by_card_number/{cardNumber}",
            produces = {"application/json; charset=UTF-8"}
            )
    public BalanceDto getBalanceByCardNumber(@PathVariable String cardNumber) throws Exception {
        if(cardNumber == null) {
            System.out.println("null");
            throw new BankApiException("InputCardNumber");
        }
        Optional<Card> cardByCardNumberOptional = cardRepository.getCardByCardNumber(cardNumber);
        Card card = cardByCardNumberOptional.orElseThrow(()->new NullPointerException("CardNotFound"));
        Account account = accountRepository.getAccountByAccountNumber(card.getAccountNumber());
        if(account.getBalance()==null) {
            System.out.println("Card_Number_incorrect");
            throw new BankApiException("CardNumberIncorrect");
        }
        return balanceDTOConverter.balanceDTO(account);
    }

    /**
     * Добавить сумму на баланс счета по карте
     * Если сумма отрицательная вернуть сообщение - сумма отрицательная
     * Получить карту из базы, проверить код карты если совпадает то добавить сумма
     * @param balanceIncDTO - json
     */

    @PostMapping(
            value = "/balance_inc",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ServerResponseDTO incrementBalanceByCardNumber(@RequestBody BalanceIncDTO balanceIncDTO)
            throws BankApiException {
        if(balanceIncDTO.getAmount() < 0) {
            throw new BankApiException("AmountIsNegative");
        }
        Optional<Card> cardOptional = cardRepository.getCardByCardNumber(balanceIncDTO.getCardNumber());
        Card card = cardOptional.orElseThrow(()->new NullPointerException("cardNotFound"));
        if(card.getCVC_code()==balanceIncDTO.getCvc()){
            Account account = accountRepository.getAccountByAccountNumber(card.getAccountNumber());
            account.setBalance(account.getBalance().add(BigDecimal.valueOf(balanceIncDTO.getAmount())));
            accountRepository.save(account);
        } else {
            throw new BankApiException("CVCCodeInvalid");
        }

        return new ServerResponseDTO("BalanceUpdateOk");

    }

    /**
     * Выпуск ковой карьты: Достаем клиента из базы по номеру счета если клиента нет, возвращаем отрицательный результат
     * если клиент находится: - генерируем случайны номер карты, проверям чть такого номера нет,
     *                         - генерируем CVC код,
     *                         - создаем дату и добвляем три года
     * и добавляем карту в список
     * @param accountNumber - Номер счета по которому необходимо выпустить карту
     *
     * @return - Резултат добавления, либо исключение если номер пуст
     */
    @PostMapping(
            value = "/add_card",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ServerResponseDTO addCardByAccountNumber(@RequestBody AccountNumberDTO accountNumber) throws BankApiException {
        ObjectNode serverResponse = new ObjectMapper().createObjectNode();
        if(accountNumber == null) {
            throw new BankApiException("Account number is empty!");
        }
        Client client = clientRepository.getClientByAccount_AccountNumber(accountNumber.getAccountNumber());

        if(client.getAccount().getAccountNumber() == null){
            throw new BankApiException("AccountNumberIncorrect");
        } else {
            Card card = new Card();
            Long cartNumber = null;
            do{
                cartNumber = getRandomLong(0000000000000000L,9999999999999999L);
                System.out.println(cartNumber);
            } while (cardRepository.existsByCardNumber(cartNumber.toString()));
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
            cardRepository.save(card);
        }
        return new ServerResponseDTO("CardAdded");
    }

    private int getRandomCVC(int max) {
        return random.nextInt(max);
    }

    private Long getRandomLong(long min, long max) {
        //return random.nextLong() % (max - min) + min;
        return ThreadLocalRandom.current().nextLong(min,max);
    }


}
