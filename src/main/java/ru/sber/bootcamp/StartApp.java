package ru.sber.bootcamp;

import ru.sber.bootcamp.configuration.DataBaseConfig;
import ru.sber.bootcamp.service.DataConnectionService;
import ru.sber.bootcamp.service.H2ConnectionService;

public class StartApp {

    public static void main(String[] args) {

        DataConnectionService h2DataService = new H2ConnectionService(DataBaseConfig.getConfig());
        h2DataService.start();




    }
}
