package ru.sber.bootcamp;

import org.json.JSONObject;
import ru.sber.bootcamp.configuration.DataBaseConfig;
import ru.sber.bootcamp.model.entity.Account;
import ru.sber.bootcamp.service.DataConnectionService;
import ru.sber.bootcamp.service.H2ConnectionServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class StartApp {

    public static void main(String[] args) {

        DataConnectionService h2DataService = new H2ConnectionServiceImpl(DataBaseConfig.getConfig());
        h2DataService.start();

        List<Account> accounts = h2DataService.findAllAccuont();

        for (Account account : accounts) {
            JSONObject json = new JSONObject(account); // Convert text to object
            System.out.println(json.toString(4));
        }

        h2DataService.stop();


    }
}
