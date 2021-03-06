package ru.sber.bootcamp.modelDao.repository;

import ru.sber.bootcamp.exception.BankApiException;
import ru.sber.bootcamp.modelDao.entity.Card;
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
    public Card getCardByCardNumber(String cardNumber) {
        return dataConnectionService.getCardByCardNumber(cardNumber);
    }

    @Override
    public Card getCardById(Long id) {
        return dataConnectionService.getCardByCardId(id);
    }

    @Override
    public List getAllCardsByAccountNumber(String accountNumber) throws BankApiException {
        return dataConnectionService.getAllCardByAccountNumber(accountNumber);
    }

    @Override
    public int addCardByAccountNumber(Card card) {
       return dataConnectionService.addCardByAccountNumber(card);
    }

    @Override
    public Card getCardWithMaxNumber() {
        return dataConnectionService.getCardWithMaxNumber();
    }

    @Override
    public boolean isCardExist(String cartNumber) {
        return dataConnectionService.isCardExist(cartNumber);
    }
}
