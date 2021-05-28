package ru.sber.bootcamp.service.httpServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONException;
import org.json.JSONObject;
import ru.sber.bootcamp.configuration.MyErrorMessage;
import ru.sber.bootcamp.controller.ClientController;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.DataFormatException;

import static ru.sber.bootcamp.configuration.MyErrorMessage.ERROR_MESSAGE;

class MyHttp_ROOT_Handler implements HttpHandler {

   private final ClientController controller;

   private Http_POST_handle http_post_handle;
   private Http_GET_hendle http_get_hendle;

    public MyHttp_ROOT_Handler(ClientController controller) {
        this.controller = controller;
        http_post_handle = new Http_POST_handle(controller);
        http_get_hendle = new Http_GET_hendle(controller);

    }

    /**
     * Обработчик входящих соединений
     * @param t - запрос HTTP
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange t) {
        System.out.printf("Server get request. Type: %s, URL: %s ", t.getRequestMethod(),t.getRequestURI());
        String response = "";
        if (t.getRequestMethod().equals("GET")) {
            try {
             response = http_get_hendle.handleGET(t);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException | NumberFormatException e){
                //e.printStackTrace();
                System.err.println(e);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(ERROR_MESSAGE.message,e.getMessage());
                response = jsonObject.toString();
            }
        } else {
            try {
             response = http_post_handle.handlePOST(t);
            } catch (NullPointerException | NumberFormatException e){
                //e.printStackTrace();
                System.err.println(e);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(ERROR_MESSAGE.message, e.getMessage());
                response = jsonObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        t.getResponseHeaders().set("Content-Type", "application/json; charset=" + StandardCharsets.UTF_8);

        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        try {
            t.sendResponseHeaders(200, bytes.length);
            OutputStream os = t.getResponseBody();
            os.write(bytes);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
