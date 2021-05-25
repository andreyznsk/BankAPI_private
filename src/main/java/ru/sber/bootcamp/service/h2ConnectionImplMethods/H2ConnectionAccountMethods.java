package ru.sber.bootcamp.service.h2ConnectionImplMethods;

import ru.sber.bootcamp.model_DAO.entity.Account;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class H2ConnectionAccountMethods {
    private final Connection connection;

    private PreparedStatement psAccountSelectAll;
    private PreparedStatement psGetAccountByAccountNumber;
    private PreparedStatement psGetPsGetAccountByCardNumber;
    private PreparedStatement psUpdateAccount;

    public H2ConnectionAccountMethods(Connection connection) {
        this.connection = connection;
    }


    public void prepareAllStatements() throws SQLException {
        psAccountSelectAll = connection.prepareStatement("SELECT * FROM account");
        psGetAccountByAccountNumber = connection.prepareStatement("SELECT * FROM account WHERE account_number = ?");
        psGetPsGetAccountByCardNumber = connection.prepareStatement(
                "SELECT A.* from CARD " +
                "join ACCOUNT A on A.ACCOUNT_NUMBER = CARD.ACCOUNT_NUMBER" +
                " WHERE CARD_NUMBER =?");
        psUpdateAccount = connection.prepareStatement(
                "UPDATE ACCOUNT SET BALANCE = ? , " +
                "OPEN_DATE = ?  " +
                        "WHERE ACCOUNT_NUMBER = ?");
    }

    /**
     * Обработка запроса: SELECT * FROM account
     * @return - список счетов
     */

    public List<Account> findAllAccount() {
        List<Account> accounts = new ArrayList<>();
        try {//Блок провеки через подготовленный запрос
            ResultSet rs = psAccountSelectAll.executeQuery();
            while (rs.next()){
                Account account = new Account();
                account.setId(rs.getLong(1));
                account.setAccountNumber(rs.getLong(2));
                account.setBalance(rs.getBigDecimal(3));
                account.setOpenDate(rs.getDate(4));
                accounts.add(account);
            }
            rs.close();
        } catch (SQLException throwables) {
            System.err.println(throwables.getErrorCode());
            throwables.printStackTrace();
        }

        return accounts;

    }

    public Account setAccount(ResultSet resultSet) throws SQLException {
        Account account = new Account();
        account.setId(resultSet.getLong(1));
        account.setAccountNumber(resultSet.getLong(2));
        account.setBalance(resultSet.getBigDecimal(3));
        account.setOpenDate(resultSet.getDate(4));
        return account;
    }


    /**
     * Обработка SELECT * FROM account WHERE account_number = ?
     * @param accountNumber - номкр счета
     * @return - счет
     */
    public Account getAccountByAccountNumber(Long accountNumber) {
        Account account = new Account();
        try {
            psGetAccountByAccountNumber.setLong(1, accountNumber);
            ResultSet rsAccount = psGetAccountByAccountNumber.executeQuery();
            while(rsAccount.next()){
                account = setAccount(rsAccount);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return account;
    }

    public Account getAccountByCardNumber(Long cardNumber) {
        Account account = new Account();
        try {
            psGetPsGetAccountByCardNumber.setLong(1, cardNumber);
            ResultSet rsAccount = psGetPsGetAccountByCardNumber.executeQuery();
            while(rsAccount.next()){
                account = setAccount(rsAccount);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return account;
    }

    public void updateAccount(Account account) {
        try {
            psUpdateAccount.setBigDecimal(1, account.getBalance());
            psUpdateAccount.setDate(2, (Date) account.getOpenDate());
            psUpdateAccount.setLong(3,account.getAccountNumber());
            int rsAccount = psUpdateAccount.executeUpdate();
            System.out.println(rsAccount);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
