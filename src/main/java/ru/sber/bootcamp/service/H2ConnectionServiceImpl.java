package ru.sber.bootcamp.service;

import ru.sber.bootcamp.model.entity.Account;

import javax.swing.text.html.Option;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.*;

public class H2ConnectionServiceImpl implements DataConnectionService {

    private final String url;
    private final String user;
    private final String password;
    private Connection connection;
    private Statement stmt;
    private PreparedStatement createDB;
    private PreparedStatement psSelect;
    private PreparedStatement psAccountSelectAll;

    /**
     * Инициализвция БД
     * @param properties - параметы подключеия к БД в формте url;user;password
     */
    public H2ConnectionServiceImpl(String properties) {
        String[] property = properties.split(";");
        this.url = (property.length > 0) ? property[0] : "";
        this.user = (property.length > 1) ? property[1] : "";
        this.password = (property.length > 2) ? property[2] : "";
    }

    private void connect() throws Exception {

        connection = DriverManager.getConnection(url, user, password);
        stmt = connection.createStatement();

    }

    private void disconnect() {
        try {
            stmt.close();
            connection.close();
            System.out.println("DB Close");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void start() {
        System.out.println("Auth service is running");

        try {
            connect();
            prepareAllStatements();
            System.out.println("Connect to bd main is successful");
        } catch (Exception e) {
            System.err.println("Auth serv err");
            e.printStackTrace();
        }
    }

    private void prepareAllStatements() throws SQLException {
        File databaseScript = new File("data.sql");
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Scanner myReader = new Scanner(databaseScript);
            System.out.println(myReader.hasNextLine());
            while (myReader.hasNextLine()) {
                stringBuilder.append(myReader.nextLine());
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        /*createDB = connection.prepareStatement(stringBuilder.toString());
        createDB.execute();*/
        stmt.execute(stringBuilder.toString());

        //psSelect = connection.prepareStatement("SELECT * FROM clients");
        psAccountSelectAll = connection.prepareStatement("SELECT * FROM account");


    }

    @Override
    public void stop() {
        System.out.println("Auth service has been stopped");
        disconnect();
    }

    /*public void faindAllById() {

        try {//Блок провеки через подготовленный запрос
            ResultSet rs = psSelect.executeQuery();
            while (rs.next()){
                System.out.print(rs.getString(1) + " ");
                System.out.print(rs.getString(2) + " ");
                System.out.print(rs.getString(3) + "\n");

            }

        } catch (SQLException throwables) {
            System.err.println(throwables);
            throwables.printStackTrace();
        }


    }*/

    @Override
    public List<Account> findAllAccuont() {
        List<Account> accounts = new ArrayList<>();
        try {//Блок провеки через подготовленный запрос
            ResultSet rs = psAccountSelectAll.executeQuery();
            while (rs.next()){
                Account account = new Account();
                account.setId(rs.getLong(1));
                account.setAccountNumber(rs.getLong(2));
                account.setBalance(3);
                account.setOpenDate(rs.getDate(4));
                accounts.add(account);
            }

        } catch (SQLException throwables) {
            System.err.println(throwables.getErrorCode());
            throwables.printStackTrace();
        }
        return accounts;
    }
}

