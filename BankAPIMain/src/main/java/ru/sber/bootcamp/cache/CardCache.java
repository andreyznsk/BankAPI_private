package ru.sber.bootcamp.cache;

import ru.sber.bootcamp.modelDao.entity.Card;
import ru.sber.bootcamp.modelDao.repository.CardRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardCache {

    private CardRepository cardRepository;

    private Map<Long, Card> cardMap = new HashMap<>();
    private boolean updatedCache = false;

    public Card getCardMapById(long id) {
        return cardMap.computeIfAbsent(id, p -> cardRepository.getCardById(p));
    }

    public void putCard(Card card) {
        cardMap.put(card.getId(), card);
    }

    public void refreshCache() {
        if (updatedCache) {
            List<Card> cardList = cardRepository.getAllCards();
            for (Card card : cardList) {
                cardMap.put(card.getId(), card);
                updatedCache = true;
            }
        }
    }

    public void needUpdateCache(){
        updatedCache = false;
    }

}

