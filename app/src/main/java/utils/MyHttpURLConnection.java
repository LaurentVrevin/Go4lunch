package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyHttpURLConnection {
    public static String startHttpRequest (String urlString){

        StringBuilder stringBuilder = new StringBuilder();
        try {
            // 1. Declare a URL connection
            URL url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            // 2. Open InputStream to connection
            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            // 3. Download and decode the string response
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
