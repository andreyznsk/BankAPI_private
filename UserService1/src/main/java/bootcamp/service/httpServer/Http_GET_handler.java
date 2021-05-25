package bootcamp.service.httpServer;


import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import static bootcamp.ClientServerUtil.*;

public class Http_GET_handler {

    public void handleGet(HttpExchange t) throws IOException {
        String response;
        String url = t.getRequestURI().toString();
        String[] path = url.split("/");
        System.out.println("Path length: " + path.length);
        switch (path.length>2 ? path[2].toLowerCase(Locale.ROOT) : "") {
            case "balance_inc": {
                response = getHttpFromFile("UserService1/src/main/resources/balance_inc_form.html");
                break;
            }
            case "add_card": {
                response = getHttpFromFile("UserService1/src/main/resources/add_card_form.html");
                break;
            }
            default:{
                response = "\nCommand Error!!!";
            }
        }
                byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
                t.sendResponseHeaders(200, bytes.length);
                OutputStream os = t.getResponseBody();
                os.write(bytes);
                os.close();

    }


}
