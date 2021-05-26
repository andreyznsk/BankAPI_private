package ru.sber.bootcamp.service.httpServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.sber.bootcamp.controller.ClientController;

import java.io.IOException;

class MyHttpHandler implements HttpHandler {

   private final ClientController controller;

   private Http_POST_handle http_post_handle;
   private Http_GET_hendle http_get_hendle;

    public MyHttpHandler(ClientController controller) {
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
        if (t.getRequestMethod().equals("GET")) {
            try {
               http_get_hendle.handleGET(t);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
               http_post_handle.handlePOST(t);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
