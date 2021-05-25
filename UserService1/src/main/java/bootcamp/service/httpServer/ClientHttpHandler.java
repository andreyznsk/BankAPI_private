package bootcamp.service.httpServer;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

class ClientHttpHandler implements HttpHandler {
    /**
     * Обработчик входящих соединений
     * @param t - запрос HTTP
     */
    @Override
    public void handle(HttpExchange t) {
        if (t.getRequestMethod().equals("GET")) {
            try {
                handleGet(t);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                handlePOST(t);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    private void handlePOST(HttpExchange t) throws Exception {
        System.out.println("req method is "+t.getRequestMethod());
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
    }

    private String sendJsonToURl(JSONObject jsonObject, String targetURL){

        try {
            URL url = new URL(targetURL);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(jsonObject.toString());
            wr.flush();

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
            }
            wr.close();
            rd.close();
            return "Send to Server OK!";
        } catch (Exception ignored) {
        }
        return "some Error!!!";

    }

    public void handleGet(HttpExchange t) throws IOException {
        System.out.println("req method is "+t.getRequestMethod());
        File databaseScript = new File("UserService1/src/main/resources/balance_inc_form.html");
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Scanner myReader = new Scanner(databaseScript);
            System.out.println(myReader.hasNextLine());
            while (myReader.hasNextLine()) {
                stringBuilder.append(myReader.nextLine());
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        String response = stringBuilder.toString();
        String url = t.getRequestURI().toString();
        String[] path = url.split("/");
        System.out.println("Path length: " + path.length);
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        t.sendResponseHeaders(200, bytes.length);
        OutputStream os = t.getResponseBody();
        os.write(bytes);
        os.close();

    }

    public static JSONObject parseQuery(String query) {
        JSONObject jsonObject = new JSONObject();
        if (query != null) {
            String[] pairs = query.split("[&]");
            String[] card_number = pairs[0].split("[=]");
            String[] amount = pairs[1].split("[=]");
            String[] cvc = pairs[2].split("[=]");
            jsonObject.put("card_number",Long.parseLong(card_number[1]));
            jsonObject.put("amount",Double.parseDouble(amount[1]));
            jsonObject.put("CVC",Integer.parseInt(cvc[1]));
            return jsonObject;
            //String[] key = new String[2];
            //Long cardNumber;
            /*double amount_double;
                if (card_number.length > 0) {
                    key[0] = URLDecoder.decode((card_number[0]),
                            System.getProperty("file.encoding"));
                }

                if (card_number.length > 1) {
                    cardNumber = Long.parseLong(card_number[1]);
                }
            if (amount.length > 0) {
                key[1] = URLDecoder.decode((amount[0]),
                        System.getProperty("file.encoding"));
            }

            if (amount.length > 1) {
                amount_double = Double.parseDouble(amount[1]);
            }*/

        }
        return jsonObject;
    }
}
