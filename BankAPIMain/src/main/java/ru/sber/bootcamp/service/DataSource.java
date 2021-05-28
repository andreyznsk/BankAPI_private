package ru.sber.bootcamp.service;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.sber.bootcamp.configuration.DataBaseConfig;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DataSource {

    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    static {
        String property[] = DataBaseConfig.getConfig().split(";");
        //enableTcpServer = enableTCP;
        String url = (property.length > 0) ? property[0] : "";
        String user = (property.length > 1) ? property[1] : "";
        String password = (property.length > 2) ? property[2] : "";

        config.setJdbcUrl( url );
        config.setUsername( user );
        config.setPassword( password );
        config.addDataSourceProperty( "cachePrepStmts" , "true" );
        config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
        config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
        ds = new HikariDataSource( config );
    }

    private DataSource() {}

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public static void stopConnection() throws SQLException {
        try(Connection connection = ds.getConnection();
            Statement statement = connection.createStatement();
                ) {
            statement.execute("DROP TABLE CLIENT;DROP TABLE CARD; DROP TABLE ACCOUNT;");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}