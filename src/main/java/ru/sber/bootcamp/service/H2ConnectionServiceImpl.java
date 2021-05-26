package ru.sber.bootcamp.service;

import org.h2.tools.Server;
import ru.sber.bootcamp.model_DAO.entity.Account;
import ru.sber.bootcamp.model_DAO.entity.Card;
import ru.sber.bootcamp.model_DAO.entity.Client;
import ru.sber.bootcamp.service.h2ConnectionImplMethods.H2ConnectionAccountMethods;
import ru.sber.bootcamp.service.h2ConnectionImplMethods.H2ConnectionCardMethods;
import ru.sber.bootcamp.service.h2ConnectionImplMethods.H2ConnectionClientMethods;

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
    private boolean enableAutoCommit;

    private H2ConnectionCardMethods h2ConnectionCardMethods;
    H2ConnectionAccountMethods h2ConnectionAccountMethods;
    private H2ConnectionClientMethods h2ConnectionClientMethods;

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
        this.enableAutoCommit = true;
    }




    /**
     * Служебный метод старта соединения с БД для старта использовать
     *  public void start()
     * @throws SQLException может выкинуть исключение в слочае проблем подключения
     */
    private void connect() throws SQLException {

            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            this.connection.setAutoCommit(enableAutoCommit);
            System.out.println("Server autoCommit mode: " + connection.getAutoCommit());
            this.stmt = connection.createStatement();
            this.h2ConnectionCardMethods = new H2ConnectionCardMethods(connection);
            this.h2ConnectionAccountMethods = new H2ConnectionAccountMethods(connection);
            this.h2ConnectionClientMethods = new H2ConnectionClientMethods(connection, h2ConnectionAccountMethods);
        System.out.println("Server TCP/IP mode: " + enableTcpServer);
        if (enableTcpServer) {
            server = Server.createTcpServer().start();
            System.out.println(server.getStatus());
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
        h2ConnectionCardMethods.prepareAllStatements();
        h2ConnectionAccountMethods.prepareAllStatements();
        h2ConnectionClientMethods.prepareAllStatements();

    }

    @Override
    public void stop() {
        System.out.println("Auth service has been stopped");
        if (server!=null) {
            server.stop();
        }
        disconnect();
    }
    //===================Account methods===============

    /**
     * Вывод всех счетов всех пользователей
     * @return Список счетов пользователей
     */
    @Override
    public List<Account> findAllAccuont() {
        return h2ConnectionAccountMethods.findAllAccount();
    }

    /**
     * Получить объект счет по номеру счета
     * @param accountNumber - номер счета
     * @return - объект счет
     */
    @Override
    public Account getAccountByAccountNumber(Long accountNumber) {
      return h2ConnectionAccountMethods.getAccountByAccountNumber(accountNumber);

    }

    @Override
    public Account getAccountByCardNumber(Long cardNumber) {
        return h2ConnectionAccountMethods.getAccountByCardNumber(cardNumber);
    }

    @Override
    public int updateAccount(Account account) {
        return h2ConnectionAccountMethods.updateAccount(account);
    }

    //================Client methods=========================
    /**
     * Метод полчения клиента по номеру счета
     * @param accountNumber - номер счета клиента
     * @return - возвращате объект клиент со всем его картами, а так же с информацией по его счету
     */
    @Override
    public Client getClientByAccountNumber(Long accountNumber) {
      return h2ConnectionClientMethods.getClientByAccountNumber(accountNumber);
    }

    //==================Card Methods==========================


    @Override
    public List<Card> findAllCards() {
        return h2ConnectionCardMethods.getAllCard();
    }

    @Override
    public List<Card> getAllCardByAccountNumber(Long accountNumber) {
        return h2ConnectionCardMethods.getAllCardByAccountNumber(accountNumber);
    }

    @Override
    public Card getCardByCardId(Long id) {
        return null;
    }

    @Override
    public Card getCardByCardNumber(Long cardNumber) {
        return h2ConnectionCardMethods.getCardByCardNumber(cardNumber);
    }

    @Override
    public void addCardByAccountNumber(Card card) {
        h2ConnectionCardMethods.addCardByAccountNumber(card);
    }

    @Override
    public Card getCardWithMaxNumber() {
        return h2ConnectionCardMethods.getCardWithMaxNumber();
    }

    @Override
    public void setDisableAutocommit() {
        this.enableAutoCommit = false;
    }

    @Override
    public Account getAccountById(long id) {
        return h2ConnectionAccountMethods.getAccountById(id);
    }
}

