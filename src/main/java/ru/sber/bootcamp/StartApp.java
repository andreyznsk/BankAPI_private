package ru.sber.bootcamp;

import ru.sber.bootcamp.configuration.DataBaseConfig;
import ru.sber.bootcamp.controller.ClientController;
import ru.sber.bootcamp.model.repository.AccountRepoImpl;
import ru.sber.bootcamp.model.repository.AccountRepository;
import ru.sber.bootcamp.service.DataConnectionService;
import ru.sber.bootcamp.service.GsonConverterImpl;
import ru.sber.bootcamp.service.H2ConnectionServiceImpl;
import ru.sber.bootcamp.service.httpServer.HttpServerStarter;


import java.util.Scanner;

public class StartApp {

    public static void main(String[] args) {

        //Data base connection start
        DataConnectionService h2DataService = new H2ConnectionServiceImpl(DataBaseConfig.getConfig());
        h2DataService.start();
        AccountRepository accountRepository = new AccountRepoImpl(h2DataService);

        //Controller start
        ClientController controller = new ClientController(accountRepository, new GsonConverterImpl());

        //HTTP server start
        HttpServerStarter httpServerStarter = new HttpServerStarter(controller);
        httpServerStarter.start();

        System.out.println("BankAPI start successful!!");
        //Console Handler
        Scanner scanner = new Scanner(System.in);
        String command;
        do{
            command = scanner.next();
        } while (!command.equals("exit"));

        httpServerStarter.stop();
        scanner.close();
        h2DataService.stop();


    }
}
