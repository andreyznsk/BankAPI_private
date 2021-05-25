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
        Map<String, Object> parameters = new HashMap<>();
        InputStreamReader isr = new InputStreamReader(t.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        String query = br.readLine();
        parseQuery(query, parameters);

        sendJsonToURl(query, "http://localhost:8000/");

        // Вывести запрос на страницу пользователя.
        StringBuilder response = new StringBuilder();
        for (String key : parameters.keySet())
            response.append(key).append(" = ").append(parameters.get(key)).append("\n");
        byte[] bytes = response.toString().getBytes(StandardCharsets.UTF_8);
        t.sendResponseHeaders(200, bytes.length);
        OutputStream os = t.getResponseBody();
        os.write(bytes);
        os.close();
    }

    private void sendJsonToURl(String query, String targetURL) throws Exception {
     /*   try {
            System.out.println("Query: " + query);
            HttpURLConnection connection;
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=" + StandardCharsets.UTF_8);
            byte[] bytes = query.getBytes(StandardCharsets.UTF_8);
            connection.setRequestProperty("Content-Length",String.valueOf(-1));
            connection.setRequestProperty("Content-Language", "ru-RU");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send request
            OutputStream os = connection.getOutputStream();
            os.write(bytes);
            os.close();
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }*/
        Socket socket = new Socket(targetURL, 8000);
        String request = "GET / HTTP/1.0\r\n\r\n";
        OutputStream os = socket.getOutputStream();
        os.write(request.getBytes());
        os.flush();

        InputStream is = socket.getInputStream();
        int ch;
        while( (ch=is.read())!= -1)
            System.out.print((char)ch);
        socket.close();
    }

    public void handleGet(HttpExchange t) throws IOException {
        System.out.println("req method is "+t.getRequestMethod());
        File databaseScript = new File("UserService1/src/main/resources/form.html");
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

    public static void parseQuery(String query, Map<String,
            Object> parameters) throws UnsupportedEncodingException {

        if (query != null) {
            String pairs[] = query.split("[&]");
            for (String pair : pairs) {
                String param[] = pair.split("[=]");
                String key = null;
                String value = null;
                if (param.length > 0) {
                    key = URLDecoder.decode(param[0],
                            System.getProperty("file.encoding"));
                }

                if (param.length > 1) {
                    value = URLDecoder.decode(param[1],
                            System.getProperty("file.encoding"));
                }

                if (parameters.containsKey(key)) {
                    Object obj = parameters.get(key);
                    if (obj instanceof List<?>) {
                        List<String> values = (List<String>) obj;
                        values.add(value);

                    } else if (obj instanceof String) {
                        List<String> values = new ArrayList<String>();
                        values.add((String) obj);
                        values.add(value);
                        parameters.put(key, values);
                    }
                } else {
                    parameters.put(key, value);
                }
            }
        }
    }
}
