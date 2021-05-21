package ru.sber.bootcamp.configuration;

public class DataBaseConfig {
    private static String url = "jdbc:h2:mem:";
    private static String user = "";
    private static String password = "";


    public static String getConfig (){
        return url + ';' + user + ';' + password;
    };

    public static void setUrl(String url) {
        DataBaseConfig.url = url;
    }

    public static void setUser(String user) {
        DataBaseConfig.user = user;
    }

    public static void setPassword(String password) {
        DataBaseConfig.password = password;
    }
}
