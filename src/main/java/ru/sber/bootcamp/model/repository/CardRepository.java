package ru.sber.bootcamp.model.repository;

import ru.sber.bootcamp.model.entity.Card;

import java.util.List;

public interface CardRepository {

    List<Card> getAllCards();
    Card getCardByCardNumber(Long cardNumber);
    Card getCardById(Long id);
}
