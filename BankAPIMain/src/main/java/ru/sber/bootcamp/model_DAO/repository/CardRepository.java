package ru.sber.bootcamp.model_DAO.repository;

import ru.sber.bootcamp.model_DAO.entity.Card;

import java.util.List;

public interface CardRepository {

    List<Card> getAllCards();
    Card getCardByCardNumber(Long cardNumber);
    Card getCardById(Long id);

    List getAllCardsByAccountNumber(Long accountNumber);

    int addCardByAccountNumber(Card card);

    Card getCardWithMaxNumber();

    boolean isCardExist(Long cartNumber);
}
