package ru.sber.bootcamp.service;

import org.h2.tools.Server;
import ru.sber.bootcamp.modelDao.entity.Account;
import ru.sber.bootcamp.modelDao.entity.Card;
import ru.sber.bootcamp.modelDao.entity.Client;
import ru.sber.bootcamp.service.h2ConnectionImplMethods.H2ConnectionAccountMethods;
import ru.sber.bootcamp.service.h2ConnectionImplMethods.H2ConnectionCardMethods;
import ru.sber.bootcamp.service.h2ConnectionImplMethods.H2ConnectionClientMethods;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;

public class H2ConnectionServiceImpl implements DataConnectionService {

    private final boolean enableTcpServer;
    private final boolean enableAutoCommit;
    private Server server;

    private H2ConnectionCardMethods h2ConnectionCardMethods;
    H2ConnectionAccountMethods h2ConnectionAccountMethods;
    private H2ConnectionClientMethods h2ConnectionClientMethods;

    /**
     * Инициализвция БД
     *
     */
    public H2ConnectionServiceImpl(boolean enableTCP) {
        this.enableTcpServer = enableTCP;
        this.enableAutoCommit = true;
    }



    /**
     * Служебный метод старта соединения с БД для старта использовать
     *  public void start()
     * @throws SQLException может выкинуть исключение в слочае проблем подключения
     */
    private void connect() throws SQLException {

            System.out.println("Server autoCommit mode: " + enableAutoCommit);
            this.h2ConnectionCardMethods = new H2ConnectionCardMethods();
            this.h2ConnectionAccountMethods = new H2ConnectionAccountMethods();
            this.h2ConnectionClientMethods = new H2ConnectionClientMethods(h2ConnectionAccountMethods);
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
            DataSource.stopConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Пользовательский метод старта подключения к БД
     * Получает подключение к БД и инициализирует все подготовленные запросы
     */
    @Override
    public void start() {
        System.out.println("Auth service is running");

        try {
            connect();
            h2DbBaseInit();

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
    private void h2DbBaseInit() throws SQLException {
        //Загрузка БД из файла
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
        try(Connection connection = DataSource.getConnection();
            Statement stmt = connection.createStatement()) {
            stmt.execute(stringBuilder.toString());
        }

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

    /**
     * Получить объект счет по номеру карты. Данный зпрос выпонлняется с использованием card JOIN clients
     * @param cardNumber
     * @return
     */
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
        return h2ConnectionCardMethods.getCardByCardId(id);
    }

    @Override
    public Card getCardByCardNumber(Long cardNumber) {
        return h2ConnectionCardMethods.getCardByCardNumber(cardNumber);
    }

    @Override
    public int addCardByAccountNumber(Card card) {
        return h2ConnectionCardMethods.addCardByAccountNumber(card);
    }

    @Override
    public Card getCardWithMaxNumber() {
        return h2ConnectionCardMethods.getCardWithMaxNumber();
    }

    @Override
    public void setDisableAutocommit() {

    }

    @Override
    public Account getAccountById(long id) {
        return h2ConnectionAccountMethods.getAccountById(id);
    }

    @Override
    public boolean isCardExist(String cartNumber) {
        return h2ConnectionCardMethods.isCardExist(cartNumber);
    }
}

