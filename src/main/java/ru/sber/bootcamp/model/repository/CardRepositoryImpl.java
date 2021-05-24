package ru.sber.bootcamp.model.repository;

import ru.sber.bootcamp.model.entity.Card;
import ru.sber.bootcamp.service.DataConnectionService;

import java.util.List;

public class CardRepositoryImpl implements CardRepository {

   private final DataConnectionService dataConnectionService;

    public CardRepositoryImpl(DataConnectionService dataConnectionService) {
        this.dataConnectionService = dataConnectionService;
    }

    @Override
    public List<Card> getAllCards() {
        return dataConnectionService.findAllCards();
    }

    @Override
    public Card getCardByCardNumber(Long cardNumber) {
        return dataConnectionService.getCardByCardNumber(cardNumber);
    }

    @Override
    public Card getCardById(Long id) {
        return dataConnectionService.getCardByCardId(id);
    }

}
