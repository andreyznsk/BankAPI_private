package ru.sber.bootcamp.service.httpServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.sber.bootcamp.controller.ClientController;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
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
        String url = t.getRequestURI().toString();
        String[] path = url.split("/");
        System.out.println("Path length: " + path.length);
        switch (path.length>2 ? path[2].toLowerCase(Locale.ROOT) : ""){
            case "showall": response = controller.getAllAccounts();
                            break;
            case "getclient": {
                response = controller.getClientByAccountNumber(path.length>3? Long.valueOf(path[3]):null);
                System.out.println(response);
                break;
            }
            case "getallcards" : {
                response = controller.getAllCards();
                break;
            }
            default: response = "commandError!!!";
        }
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        t.getResponseHeaders().set("Content-Type", "application/json; charset=" + StandardCharsets.UTF_8);
        t.sendResponseHeaders(200, bytes.length);
        OutputStream os = t.getResponseBody();
        os.write(bytes);
        os.close();
    }
}
