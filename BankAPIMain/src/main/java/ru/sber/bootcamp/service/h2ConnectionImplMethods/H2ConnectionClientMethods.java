package ru.sber.bootcamp.service.h2ConnectionImplMethods;

import ru.sber.bootcamp.model_DAO.entity.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class H2ConnectionClientMethods {
    private final Connection connection;
    private PreparedStatement psGetClientByAccountNumber;
    private H2ConnectionAccountMethods h2ConnectionAccountMethods;

    public H2ConnectionClientMethods(Connection connection, H2ConnectionAccountMethods h2ConnectionAccountMethods) {
        this.h2ConnectionAccountMethods = h2ConnectionAccountMethods;
        this.connection = connection;
    }


    public void prepareAllStatements() throws SQLException {
        psGetClientByAccountNumber =connection.prepareStatement("SELECT * FROM client WHERE account_number = ?");

    }

    public Client getClientByAccountNumber(Long accountNumber) {
       if (accountNumber == null) {
           return null;
       }
        Client client = new Client();

        try {
            psGetClientByAccountNumber.setLong(1,accountNumber);
            ResultSet rsClient = psGetClientByAccountNumber.executeQuery();

            while (rsClient.next()){
                client.setId(rsClient.getLong(1));
                client.setAccountId(rsClient.getLong(2));
                client.setFirstName(rsClient.getString(3));
                client.setLastname(rsClient.getString(4));
                client.setPhoneNumber(rsClient.getLong(5));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (client.getId() != null) {
            client.setAccount(h2ConnectionAccountMethods.getAccountByAccountNumber(accountNumber));
        }
        return client;
    }
}
