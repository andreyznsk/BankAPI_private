package ru.sber.bootcamp.service.h2ConnectionImplMethods;

import ru.sber.bootcamp.model.entity.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class H2ConnectionAccountMethods {
    private final Connection connection;

    private PreparedStatement psAccountSelectAll;
    private PreparedStatement psGetAccountByAccountNumber;

    public H2ConnectionAccountMethods(Connection connection) {
        this.connection = connection;
    }


    public void prepareAllStatements() throws SQLException {
        psAccountSelectAll = connection.prepareStatement("SELECT * FROM account");
        psGetAccountByAccountNumber = connection.prepareStatement("SELECT * FROM account WHERE account_number = ?");

    }

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

    public Account getAccountByAccountNumber(Long accountNumber) {
        Account account = new Account();
        try {
            psGetAccountByAccountNumber.setLong(1, accountNumber);
            ResultSet rsAccount = psGetAccountByAccountNumber.executeQuery();
            while(rsAccount.next()){
                account.setId(rsAccount.getLong(1));
                account.setAccountNumber(rsAccount.getLong(2));
                account.setBalance(rsAccount.getBigDecimal(3));
                account.setOpenDate(rsAccount.getDate(4));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return account;
    }
}
