package ru.sber.bootcamp.service;

import org.h2.tools.Server;
import ru.sber.bootcamp.model.entity.Account;
import ru.sber.bootcamp.model.entity.Client;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.*;

public class H2ConnectionServiceImpl implements DataConnectionService {

    private final String URL;
    private final String USER;
    private final String PASSWORD;
    private Connection connection;
    private Statement stmt;
    private Server server;
    private boolean enableTcpServer;
    //private PreparedStatement createDB;
    //private PreparedStatement psSelect;
    private PreparedStatement psAccountSelectAll;
    private PreparedStatement psGetClientByAccountNumber;
    private PreparedStatement psGetAccountByAccountNumber;

    /**
     * Инициализвция БД
     * @param properties - параметы подключеия к БД в формте url;user;password
     */
    public H2ConnectionServiceImpl(String properties, boolean enableTCP) {
        String[] property = properties.split(";");
        this.enableTcpServer = enableTCP;
        this.URL = (property.length > 0) ? property[0] : "";
        this.USER = (property.length > 1) ? property[1] : "";
        this.PASSWORD = (property.length > 2) ? property[2] : "";

    }


    /**
     * Служебный метод старта соединения с БД для старта использовать
     *  public void start()
     * @throws SQLException может выкинуть исключение в слочае проблем подключения
     */
    private void connect() throws SQLException {

            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            stmt = connection.createStatement();
        if (enableTcpServer) {
            server = Server.createTcpServer().start();
        }

    }

    /**
     * Служебный метод остановки поключения к БД. Для вызова использовать
     *  public void stop()
     */
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

    /**
     * Подготовка всех предустановленных запросов
     * @throws SQLException - может выбросить исключение при недоступности БД, а так же при ошибках поделючения
     */
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
        psGetClientByAccountNumber =connection.prepareStatement("SELECT * FROM client WHERE account_number = ?");
        psGetAccountByAccountNumber = connection.prepareStatement("SELECT * FROM account WHERE account_number = ?");


    }

    @Override
    public void stop() {
        System.out.println("Auth service has been stopped");
        server.stop();
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

    /**
     * Вывод всех счетов всех пользователей
     * @return Список счетов пользователей
     */
    @Override
    public List<Account> findAllAccuont() {
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

    /**
     * Метод полчения клиента по номеру счета
     * @param accountNumber - номер счета клиента
     * @return - возвращате объект клиент со всем его картами, а так же с информацией по его счету
     */
    @Override
    public Client getClientByAccountNumber(Long accountNumber) {
        Client client = new Client();
        Account account = new Account();

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
        account = getAccountByAccountNumber(accountNumber);
        client.setAccount(account);
        return client;
    }

    @Override
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

