package ru.sber.bootcamp.service.httpServer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.sber.bootcamp.controller.ClientController;
import ru.sber.bootcamp.exception.BankApiException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static ru.sber.bootcamp.configuration.MyServerMessage.ERROR_MESSAGE;

class MyHttpRootHandler implements HttpHandler {

   private final ClientController controller;

   private HttpPostHandle http_postHandle;
   private HttpGetHandle httpGetHandle;

    public MyHttpRootHandler(ClientController controller) {
        this.controller = controller;
        http_postHandle = new HttpPostHandle(controller);
        httpGetHandle = new HttpGetHandle(controller);

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
             response = httpGetHandle.handleGET(t);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (BankApiException e){
                //e.printStackTrace();
                System.err.println(e);
                ObjectNode serverResponse = new ObjectMapper().createObjectNode();
                serverResponse.put(ERROR_MESSAGE.message,e.getMessage());
                response = serverResponse.toString();
            } catch (Exception e){
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        } else {
            try {
             response = http_postHandle.handlePOST(t);
            } catch (BankApiException e){
                //e.printStackTrace();
                System.err.println(e);
                ObjectNode serverResponse = new ObjectMapper().createObjectNode();
                serverResponse.put(ERROR_MESSAGE.message, e.getMessage());
                response = serverResponse.toString();
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
