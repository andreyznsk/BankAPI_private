package ru.sber.bootcamp.modelDao.repository;

import ru.sber.bootcamp.exception.BankApiException;
import ru.sber.bootcamp.modelDao.entity.Card;

import java.util.List;

public interface CardRepository {

    List<Card> getAllCards();
    Card getCardByCardNumber(String cardNumber);
    Card getCardById(Long id);

    List<Card> getAllCardsByAccountNumber(String accountNumber) throws BankApiException;

    int addCardByAccountNumber(Card card);

    Card getCardWithMaxNumber();

    boolean isCardExist(String cartNumber);
}
