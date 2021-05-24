package ru.sber.bootcamp.service.h2ConnectionImplMethods;

import ru.sber.bootcamp.model.entity.Account;
import ru.sber.bootcamp.model.entity.Card;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class H2ConnectionCardMethods {

    private PreparedStatement psGetAllCards;
    Connection connection;

    public H2ConnectionCardMethods(Connection connection) {
        this.connection = connection;
    }

    public void prepareAllStatements() throws SQLException {
            psGetAllCards = connection.prepareStatement("select * from CARD");
    }

    public List<Card> getAllCard(){
        List<Card> cards = new ArrayList<>();
        try {
            ResultSet rs = psGetAllCards.executeQuery();
            while (rs.next()){
                Card card = new Card();
                card.setId(rs.getLong(1));
                card.setAccountNumber(rs.getLong(2));
                card.setCardNumber(rs.getLong(3));
                card.setDateValidThru(rs.getDate(4));
                card.setCVC_code(rs.getInt(5));
                cards.add(card);
            }
            rs.close();
        } catch (SQLException throwables) {
            System.err.println(throwables.getErrorCode());
            throwables.printStackTrace();
        }
        return cards;
    }
}
