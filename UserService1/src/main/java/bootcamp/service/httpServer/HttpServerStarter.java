package bootcamp.service.httpServer;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpServerStarter {

    HttpServer server = null;


    public HttpServerStarter() {
        try {
            server = HttpServer.create(new InetSocketAddress(9000), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpHandler clientHttpHandler = new ClientHttpHandler();
        server.createContext("/balance_inc", clientHttpHandler);
        server.setExecutor(null);
        System.out.println(server.getAddress());
    }

    public void start(){
        server.start();
    }

    public void stop(){
        server.stop(0);
    }

}
