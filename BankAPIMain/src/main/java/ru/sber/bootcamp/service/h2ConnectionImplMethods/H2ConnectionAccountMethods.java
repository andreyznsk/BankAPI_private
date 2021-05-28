package ru.sber.bootcamp.service.h2ConnectionImplMethods;

import ru.sber.bootcamp.modelDao.entity.Account;
import ru.sber.bootcamp.service.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class H2ConnectionAccountMethods {

    private String psAccountSelectAllSql = "SELECT * FROM account";
    private String psGetAccountByAccountNumberSql = "SELECT * FROM account WHERE account_number = ?";
    private String psGetPsGetAccountByCardNumberSql =                 "SELECT A.* from CARD " +
            "join ACCOUNT A on A.ACCOUNT_NUMBER = CARD.ACCOUNT_NUMBER" +
            " WHERE CARD_NUMBER =?";
    private String psUpdateAccountSql = "UPDATE ACCOUNT SET BALANCE = ? , " +
            "OPEN_DATE = ?  " +
            "WHERE ACCOUNT_NUMBER = ?";
    private String psGetAccountByIdSql = "SELECT * FROM account WHERE id = ?";

    /**
     * Обработка запроса: SELECT * FROM account
     * @return - список счетов
     */

    public List<Account> findAllAccount() {
        List<Account> accounts = new ArrayList<>();
        try(Connection connection = DataSource.getConnection();
        PreparedStatement psAccountSelectAll = connection.prepareStatement(psAccountSelectAllSql) ) {
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
        try(Connection connection = DataSource.getConnection();
        PreparedStatement psGetAccountByAccountNumber = connection.prepareStatement(psGetAccountByAccountNumberSql)) {
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
        try(Connection connection = DataSource.getConnection();
        PreparedStatement psGetPsGetAccountByCardNumber = connection.prepareStatement(psGetPsGetAccountByCardNumberSql) ) {
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

    public int updateAccount(Account account) {
        int result = 0;
        try(Connection connection = DataSource.getConnection();
        PreparedStatement psUpdateAccount = connection.prepareStatement(psUpdateAccountSql)) {
            psUpdateAccount.setBigDecimal(1, account.getBalance());
            psUpdateAccount.setDate(2, (Date) account.getOpenDate());
            psUpdateAccount.setLong(3,account.getAccountNumber());
            result = psUpdateAccount.executeUpdate();
            if (result == 1) {
                System.out.println("Balance update - OK!");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

    public Account getAccountById(long id) {
        Account account = new Account();
        try(Connection connection = DataSource.getConnection();
        PreparedStatement psGetAccountById = connection.prepareStatement(psGetAccountByIdSql)) {
            psGetAccountById.setLong(1, id);
            ResultSet rsAccount = psGetAccountById.executeQuery();
            while(rsAccount.next()){
                account = setAccount(rsAccount);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return account;
    }
}
