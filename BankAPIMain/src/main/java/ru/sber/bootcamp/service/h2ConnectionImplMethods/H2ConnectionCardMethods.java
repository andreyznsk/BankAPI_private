package ru.sber.bootcamp.service.h2ConnectionImplMethods;

import ru.sber.bootcamp.exception.BankApiException;
import ru.sber.bootcamp.modelDao.entity.Card;
import ru.sber.bootcamp.service.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class H2ConnectionCardMethods {

    private static String psGetAllCardsSql = "select * from CARD;";
    private static String psGetAllCardByAccountNumberSql = "SELECT * FROM CARD WHERE ACCOUNT_NUMBER =?;";
    private static String psGetCardBuCardNumberSql = "SELECT * FROM CARD WHERE CARD_NUMBER = ?;";
    private static String psAddCardByAccountNumberSql = "INSERT INTO Card (account_number, card_number, date_valid_thru, cvc_code)\n" +
            "VALUES (?, ?, ?, ?);";
    private static String psGetCardWithMaxCardNumberSql = "SELECT MAX(CARD_NUMBER) FROM CARD WHERE CARD_NUMBER;";
    private static String psGetCardByIdSql = "SELECT * FROM CARD WHERE ID = ?;";
    private static String psGetCardNumberByCardNumberSql = "SELECT CARD_NUMBER FROM CARD WHERE CARD_NUMBER = ?;";


    public Card cardInit(ResultSet rs) throws SQLException {
        Card card = new Card();
        card.setId(rs.getLong(1));
        card.setAccountNumber(rs.getString(2));
        card.setCardNumber(rs.getString(3));
        card.setDateValidThru(rs.getDate(4));
        card.setCVC_code(rs.getInt(5));
       return card;
    }

    public List<Card> getAllCard(){
        List<Card> cards = new ArrayList<>();
        try (Connection connection = DataSource.getConnection();
             PreparedStatement psGetAllCards = connection.prepareStatement(psGetAllCardsSql) ){
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

    public List<Card> getAllCardByAccountNumber(String accountNumber) throws BankApiException {
        List<Card> cards = new ArrayList<>();
        try(Connection connection = DataSource.getConnection();
           PreparedStatement psGetAllCardByAccountNumber =  connection.prepareStatement(psGetAllCardByAccountNumberSql)) {
            psGetAllCardByAccountNumber.setString(1,accountNumber);
            ResultSet rs = psGetAllCardByAccountNumber.executeQuery();
            while (rs.next()){
                Card card = cardInit(rs);
                cards.add(card);
            }
            rs.close();
            if(cards.size()==0){
                throw new BankApiException("IncorrectAccountNumber");
            }
        } catch (SQLException throwables) {
            System.err.println(throwables.getErrorCode());
            throwables.printStackTrace();
        }
        return cards;
    }

    public Card getCardByCardNumber(String cardNumber) {
        if(cardNumber == null) {
            return null;
        }
        Card card = new Card();
        try(Connection connection = DataSource.getConnection();
        PreparedStatement psGetCardBuCardNumber = connection.prepareStatement(psGetCardBuCardNumberSql)) {
            psGetCardBuCardNumber.setString(1,cardNumber);
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

    public int addCardByAccountNumber(Card card) {
        int result = 0;
        try(Connection connection = DataSource.getConnection();
        PreparedStatement psAddCardByAccountNumber = connection.prepareStatement(psAddCardByAccountNumberSql) ) {
            psAddCardByAccountNumber.setString(1,card.getAccountNumber());
            psAddCardByAccountNumber.setString(2,card.getCardNumber());
            psAddCardByAccountNumber.setDate(3, new Date(card.getDateValidThru().getTime()));
            psAddCardByAccountNumber.setInt(4,card.getCVC_code());

            result = psAddCardByAccountNumber.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    public Card getCardWithMaxNumber() {
        Card card = new Card();
        try(Connection connection = DataSource.getConnection();
        PreparedStatement psGetCardWithMaxCardNumber = connection.prepareStatement(psGetCardWithMaxCardNumberSql)) {
            ResultSet rsCard = psGetCardWithMaxCardNumber.executeQuery();
            while (rsCard.next()){
                card.setCardNumber(rsCard.getString(1));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (card.getCardNumber() == null) {
            card.setCardNumber("1"); ;
        }
        return card;

    }

    public Card getCardByCardId(Long id) {
        if(id == null) {
            return null;
        }
        Card card = new Card();
        try(Connection connection = DataSource.getConnection();
        PreparedStatement psGetCardById = connection.prepareStatement(psGetCardByIdSql) ) {
            psGetCardById.setLong(1,id);
            ResultSet rsCard = psGetCardById.executeQuery();

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

    public boolean isCardExist(String cartNumber) {
        try(Connection connection = DataSource.getConnection();
        PreparedStatement psGetCardNumberByCardNumber = connection.prepareStatement(psGetCardNumberByCardNumberSql)) {
            psGetCardNumberByCardNumber.setString(1, cartNumber);
            ResultSet rsCard = psGetCardNumberByCardNumber.executeQuery();
            return rsCard.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
}
