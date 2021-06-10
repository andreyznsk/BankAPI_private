package ru.sber.bootcamp.modelDao.repository;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.sber.bootcamp.modelDao.entity.Card;
import ru.sber.bootcamp.service.DataConnectionService;
import ru.sber.bootcamp.service.H2ConnectionServiceImpl;

import java.sql.Date;
import java.util.List;

public class CardRepositoryTest {
    static DataConnectionService dataConnectionService;
    static CardRepository cardRepository;

    @BeforeClass
    public static void init() {
        dataConnectionService = new H2ConnectionServiceImpl(false);
        cardRepository = new CardRepositoryImpl(dataConnectionService);
        dataConnectionService.setDisableAutocommit();
        dataConnectionService.start();
    }

    @Test
    public void getAllCards() {
        Card card = new Card(1l,"1111","1111222233334441", Date.valueOf("2023-01-01"),111);
        List<Card> allCards = dataConnectionService.findAllCards();
        Assert.assertEquals(allCards.get(0),card);
        Assert.assertNotEquals(allCards.get(1),card);
    }

    @Test
    public void getCardByCardNumber() {
        Card card = new Card(1l,"1111","1111222233334441", Date.valueOf("2023-01-01"),111);
        Card cardTest = dataConnectionService.getCardByCardNumber("1111222233334441");
        Assert.assertEquals(card,cardTest);
    }

    @Test
    public void getCardById() {
        Card card = new Card(1l,"1111","1111222233334441", Date.valueOf("2023-01-01"),111);
        Card cardByCardId = dataConnectionService.getCardByCardId(1l);
        Assert.assertEquals(cardByCardId,card);
    }

    @Test
    public void getAllCardsByAccountNumber() {
        Card card = new Card(1l,"1111","1111222233334441", Date.valueOf("2023-01-01"),111);
        List<Card> allCardByAccountNumber = dataConnectionService.getAllCardByAccountNumber("1111");
        Assert.assertEquals(allCardByAccountNumber.get(0),card);
        Assert.assertNotEquals(allCardByAccountNumber.get(1),card);
    }

    @Test
    public void addCardByAccountNumber() {
        Card card = new Card();
        card.setAccountNumber("1111");
        card.setCardNumber("1111111");
        card.setDateValidThru(Date.valueOf("2000-01-01"));
        dataConnectionService.addCardByAccountNumber(card);
        List<Card> allCardByAccountNumber = dataConnectionService.getAllCardByAccountNumber("1111");
        System.out.println(allCardByAccountNumber);
        Assert.assertEquals(allCardByAccountNumber.get(2),card);

    }

    @Test
    public void getCardWithMaxNumber() {
        Card card = new Card(null,null,"1112222233334444",null,0);
        Card cardWithMaxNumber = dataConnectionService.getCardWithMaxNumber();
        Assert.assertEquals(card,cardWithMaxNumber);
    }

    @AfterClass
    public static void stop(){
        dataConnectionService.stop();
    }
}