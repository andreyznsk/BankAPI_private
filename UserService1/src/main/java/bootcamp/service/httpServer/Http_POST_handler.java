package bootcamp.service.httpServer;

import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import static bootcamp.ClientServerUtil.*;

public class Http_POST_handler {

    public void handlePost(HttpExchange t) throws Exception {
        String url = t.getRequestURI().toString();
        String[] path = url.split("/");
        System.out.println("Path length: " + path.length);
        switch (path.length>2 ? path[2].toLowerCase(Locale.ROOT) : "") {
            case "balance_inc": {
                InputStreamReader isr = new InputStreamReader(t.getRequestBody(), StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr);
                String query = br.readLine();
                JSONObject js = parseQuery(query);
                String response = sendJsonToURl(js, "http://localhost:8000/bank_api/balance_inc");

                // Вывести запрос на страницу пользователя.
                byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
                t.sendResponseHeaders(200, bytes.length);
                OutputStream os = t.getResponseBody();
                os.write(bytes);
                os.close();
                break;
            }
            case "add_card": {
                InputStreamReader isr = new InputStreamReader(t.getRequestBody(), StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr);
                String query = br.readLine();
                JSONObject js = parseQuery(query);
                String response = sendJsonToURl(js, "http://localhost:8000/bank_api/add_card");

                // Вывести запрос на страницу пользователя.
                byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
                t.sendResponseHeaders(200, bytes.length);
                OutputStream os = t.getResponseBody();
                os.write(bytes);
                os.close();
                break;

            }
            default: {
                break;
            }
        }

    }


}
