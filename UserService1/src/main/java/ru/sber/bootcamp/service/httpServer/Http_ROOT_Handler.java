package ru.sber.bootcamp.service.httpServer;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;

class Http_ROOT_Handler implements HttpHandler {

       private Http_GET_handler handleGET = new Http_GET_handler();
       private Http_POST_handler handlePOST = new Http_POST_handler();



    /**
     * Обработчик входящих соединений
     * @param t - запрос HTTP
     */
    @Override
    public void handle(HttpExchange t) {
        if (t.getRequestMethod().equals("GET")) {
            try {
                handleGET.handleGet(t);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                handlePOST.handlePost(t);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }








}
