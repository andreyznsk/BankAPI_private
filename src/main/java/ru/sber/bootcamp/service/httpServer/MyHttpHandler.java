package ru.sber.bootcamp.service.httpServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.sber.bootcamp.controller.ClientController;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

class MyHttpHandler implements HttpHandler {

   private final ClientController controller;

    public MyHttpHandler(ClientController controller) {
        this.controller = controller;
    }

    /**
     * Обработчик входящих соединений
     * @param t - запрос HTTP
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange t) throws IOException {
        String response;
        System.out.println(t.getRequestMethod());
        String url = t.getRequestURI().toString();
        System.out.println(Thread.currentThread());
        String[] path = url.split("/");
        System.out.println("Path length: " + path.length);
        switch (path.length>2 ? path[2].toLowerCase(Locale.ROOT) : ""){
            case "showall": response = controller.getAllAccounts();
                            break;
            case "getclient": response = controller.getClientByAccountNumber(path.length>3? Long.valueOf(path[3]):null);
                System.out.println(response);
                            break;
            default: response = "commandError!!!";
        }

        t.sendResponseHeaders(200, response.length());
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
