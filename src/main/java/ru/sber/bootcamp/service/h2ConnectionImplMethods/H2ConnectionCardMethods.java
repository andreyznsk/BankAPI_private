package ru.sber.bootcamp.service.h2ConnectionImplMethods;

import ru.sber.bootcamp.model_DAO.entity.Card;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class H2ConnectionCardMethods {

    private PreparedStatement psGetAllCards;
    private PreparedStatement psGetAllCardByAccountNumber;
    private PreparedStatement psGetCardBuCardNumber;
    Connection connection;

    public H2ConnectionCardMethods(Connection connection) {
        this.connection = connection;
    }

    public void prepareAllStatements() throws SQLException {
            psGetAllCards = connection.prepareStatement("select * from CARD");
            psGetAllCardByAccountNumber = connection.prepareStatement("SELECT * FROM CARD WHERE ACCOUNT_NUMBER =?");
            psGetCardBuCardNumber = connection.prepareStatement("SELECT * FROM CARD WHERE CARD_NUMBER = ?");
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
}
