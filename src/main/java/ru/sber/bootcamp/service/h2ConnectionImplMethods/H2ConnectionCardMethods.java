package ru.sber.bootcamp.service.h2ConnectionImplMethods;

import org.w3c.dom.ls.LSOutput;
import ru.sber.bootcamp.model_DAO.entity.Card;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class H2ConnectionCardMethods {

    private final  Connection connection;

    private PreparedStatement psGetAllCards;
    private PreparedStatement psGetAllCardByAccountNumber;
    private PreparedStatement psGetCardBuCardNumber;
    private PreparedStatement psAddCardByAccountNumber;
    private PreparedStatement psGetCardWithMaxCardNumber;

    public H2ConnectionCardMethods(Connection connection) {
        this.connection = connection;
    }

    public void prepareAllStatements() throws SQLException {
            psGetAllCards = connection.prepareStatement("select * from CARD");
            psGetAllCardByAccountNumber = connection.prepareStatement("SELECT * FROM CARD WHERE ACCOUNT_NUMBER =?");
            psGetCardBuCardNumber = connection.prepareStatement("SELECT * FROM CARD WHERE CARD_NUMBER = ?");
            psAddCardByAccountNumber = connection.prepareStatement("INSERT INTO Card (account_number, card_number, date_valid_thru, cvc_code)\n" +
                    "VALUES (?, ?, ?, ?);");
            psGetCardWithMaxCardNumber = connection.prepareStatement("SELECT MAX(CARD_NUMBER) FROM CARD WHERE CARD_NUMBER");

    }

    public Card cardInit(ResultSet rs) throws SQLException {
        Card card = new Card();
        card.setId(rs.getLong(1));
        card.setAccountNumber(rs.getLong(2));
        card.setCardNumber(rs.getLong(3));
        card.setDateValidThru(rs.getDate(4));
        card.setCVC_code(rs.getInt(5));
       return card;
    }

    public List<Card> getAllCard(){
        List<Card> cards = new ArrayList<>();
        try {
            ResultSet rs = psGetAllCards.executeQuery();
            while (rs.next()) {
                Card card = cardInit(rs);
                cards.add(card);
            }
            rs.close();
        } catch (SQLException throwables) {
            System.err.println(throwables.getErrorCode());
            throwables.printStackTrace();
        }
        return cards;
    }

    public List getAllCardByAccountNumber(Long accountNumber) {
        if(accountNumber == null) {
            return null;
        }
        List<Card> cards = new ArrayList<>();
        try {
            psGetAllCardByAccountNumber.setLong(1,accountNumber);
            ResultSet rs = psGetAllCardByAccountNumber.executeQuery();
            while (rs.next()){
                Card card = cardInit(rs);
                cards.add(card);
            }
            rs.close();
        } catch (SQLException throwables) {
            System.err.println(throwables.getErrorCode());
            throwables.printStackTrace();
        }
        return cards;
    }

    public Card getCardByCardNumber(Long cardNumber) {
        if(cardNumber == null) {
            return null;
        }
        Card card = new Card();
        try {
            psGetCardBuCardNumber.setLong(1,cardNumber);
            ResultSet rsCard = psGetCardBuCardNumber.executeQuery();

            while (rsCard.next()){
             card = cardInit(rsCard);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (card.getId() == null) {
           return null;
        }
        return card;
    }

    public void addCardByAccountNumber(Card card) {
        if (card.getAccountNumber() == null) {
            System.err.println("card is null in method addCardByAccount");
            return;
        }
        try {
            psAddCardByAccountNumber.setLong(1,card.getAccountNumber());
            psAddCardByAccountNumber.setLong(2,card.getCardNumber());
            psAddCardByAccountNumber.setDate(3, new Date(card.getDateValidThru().getTime()));
            psAddCardByAccountNumber.setInt(4,card.getCVC_code());

            if ((psAddCardByAccountNumber.executeUpdate() == 1)) {
                System.out.println("Card added");
            } else {
                System.err.println("Card not added!!!");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Card getCardWithMaxNumber() {
        Card card = new Card();
        try {
            ResultSet rsCard = psGetCardWithMaxCardNumber.executeQuery();
            while (rsCard.next()){
                card.setCardNumber(rsCard.getLong(1));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (card.getCardNumber() == null) {
            card.setCardNumber(1L); ;
        }
        return card;

    }
}
