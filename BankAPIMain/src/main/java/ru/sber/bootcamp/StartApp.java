package ru.sber.bootcamp;

import ru.sber.bootcamp.configuration.DataBaseConfig;
import ru.sber.bootcamp.controller.ClientController;
import ru.sber.bootcamp.model_DAO.repository.*;
import ru.sber.bootcamp.service.DataConnectionService;
import ru.sber.bootcamp.service.GsonConverterImpl;
import ru.sber.bootcamp.service.H2ConnectionServiceImpl;
import ru.sber.bootcamp.service.httpServer.HttpServerStarter;

import java.util.Locale;
import java.util.Scanner;

public class StartApp {

    public static void main(String[] args) {
        boolean tcpServer = false;
        if (args.length != 0) {
            System.out.println("System arg is: " + args[0]);
            if (args[0].toLowerCase(Locale.ROOT).equals("tcp")) {
                tcpServer = true;
            }
        }

        //Data base connection start
        DataConnectionService dataService = new H2ConnectionServiceImpl(DataBaseConfig.getConfig(),tcpServer);
        dataService.start();

        AccountRepository accountRepository = new AccountRepoImpl(dataService);
        ClientRepository clientRepository = new ClientRepositoryImpl(dataService);
        CardRepository cardRepository = new CardRepositoryImpl(dataService);

        //Controller start
        ClientController controller = new ClientController(accountRepository, clientRepository, cardRepository, new GsonConverterImpl());


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
        dataService.stop();


    }
}
