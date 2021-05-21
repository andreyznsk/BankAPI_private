package ru.sber.bootcamp.service;

import javax.swing.text.html.Option;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;

public class H2ConnectionService implements DataConnectionService {

    private final String url;
    private final String user;
    private final String password;
    private static Connection connection;
    private static Statement stmt;
    private static PreparedStatement createDB;
    private static PreparedStatement psSelect;

    /**
     * Инициализвция БД
     * @param properties - параметы подключеия к БД в формте url;user;password
     * @return - Ссылка на объкт класса возимодейсвия с БД
     */
    public H2ConnectionService(String properties) {
        String[] property = new String[3];
        property = properties.split(";");
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

        psSelect = connection.prepareStatement("SELECT * FROM clients");
    }

    @Override
    public void stop() {
        System.out.println("Auth service has been stopped");
        disconnect();
    }

    public void faindAllById() {

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


    }
}

