package ru.sber.bootcamp.service.httpServer;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.sber.bootcamp.controller.ClientController;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpServerStarter {

    HttpServer server = null;


    public HttpServerStarter(ClientController controller) {
        try {
            server = HttpServer.create(new InetSocketAddress(8000), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpHandler myHttpHandler = new MyHttpHandler(controller);
        server.createContext("/bank_api", myHttpHandler);
        server.setExecutor(null); // creates a default executor

    }

    public void start(){
        server.start();

    }

    public void stop(){
        server.stop(0);
    }

}
