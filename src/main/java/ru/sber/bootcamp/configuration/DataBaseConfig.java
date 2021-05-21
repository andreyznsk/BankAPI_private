package ru.sber.bootcamp.configuration;

public class DataBaseConfig {
    public static String url = "jdbc:h2:mem:";
    public static String user = "";
    public static String password = "";


    public static String getConfig (){
        return url + ';' + user + ';' + password;
    };
}
