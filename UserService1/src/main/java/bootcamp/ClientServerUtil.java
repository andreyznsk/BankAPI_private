package bootcamp;

import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class ClientServerUtil {
    public static JSONObject parseQuery(String query) {
        JSONObject jsonObject = new JSONObject();
        if (query != null) {
            String[] pairs = query.split("[&]");
            String[][] params = new String[pairs.length][];
            for (int i = 0; i < pairs.length; i++) {
                params[i] = pairs[i].split("[=]");
                if(params[i][0].equals("card_number")) {
                    jsonObject.put(params[i][0],Long.parseLong(params[i][1]));
                    continue;
                }
                jsonObject.put(params[i][0],Double.parseDouble(params[i][1]));
            }
            System.out.println("Parse json from query: " + jsonObject);
            return jsonObject;
        }
        return jsonObject;
    }

    public static String sendJsonToURl(JSONObject jsonObject, String targetURL){

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
            return "Send to Server OK!:\n" + jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return e.getMessage();
        }

    }

    public static String getHttpFromFile(String path) {
        File databaseScript = new File(path);
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Scanner myReader = new Scanner(databaseScript);
            System.out.println(myReader.hasNextLine());
            while (myReader.hasNextLine()) {
                stringBuilder.append(myReader.nextLine());
            }
            myReader.close();
         return stringBuilder.toString();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return "An error occurred!!";
        }
    }
}
