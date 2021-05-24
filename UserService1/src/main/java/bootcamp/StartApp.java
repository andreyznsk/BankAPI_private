package bootcamp;


import bootcamp.service.httpServer.HttpServerStarter;

import java.util.Locale;
import java.util.Scanner;

public class StartApp {

    public static void main(String[] args) {

        //HTTP server start
        HttpServerStarter httpServerStarter = new HttpServerStarter();
        httpServerStarter.start();

        System.out.println("Client APP start successful!!!");
        //Console Handler
        Scanner scanner = new Scanner(System.in);
        String command;
        do{
            System.out.println("Type 'exit' to stop server!");
            command = scanner.next();
        } while (!command.equals("exit"));

        httpServerStarter.stop();
        scanner.close();


    }
}
