package ru.sber.bootcamp;

import ru.sber.bootcamp.controller.ClientController;
import ru.sber.bootcamp.modelDao.repository.*;
import ru.sber.bootcamp.service.DataConnectionService;
import ru.sber.bootcamp.service.DataSource;
import ru.sber.bootcamp.service.H2ConnectionServiceImpl;
import ru.sber.bootcamp.service.httpServer.HttpServerStarter;

import java.sql.SQLException;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Logger;

public class StartApp {

    public static Logger LOG = Logger.getLogger(StartApp.class.getName());

    public static void main(String[] args) {
        LOG.info("Server starting ...");
        boolean tcpServer = false;
        if (args.length != 0) {
            System.out.println("System arg is: " + args[0]);
            if (args[0].toLowerCase(Locale.ROOT).equals("tcp")) {
                tcpServer = true;
            }
        }

        //Data base connection start
        DataConnectionService dataService = new H2ConnectionServiceImpl(tcpServer);
        dataService.start();

        AccountRepository accountRepository = new AccountRepoImpl(dataService);
        ClientRepository clientRepository = new ClientRepositoryImpl(dataService);
        CardRepository cardRepository = new CardRepositoryImpl(dataService);

        //Controller start
        ClientController controller = new ClientController(accountRepository,
                clientRepository, cardRepository);


        //HTTP server start
        HttpServerStarter httpServerStarter = new HttpServerStarter(controller);
        httpServerStarter.start();

        System.out.println("BankAPI start successful!!");
        //Console Handler
     /*   Scanner scanner = new Scanner(System.in);
        String command;
       do{
            command = scanner.next();
        } while (!command.equals("exit"));

        httpServerStarter.stop();
        scanner.close();
        try {
            DataSource.stopConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
*/

    }
}
