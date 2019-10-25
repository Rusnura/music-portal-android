package org.tumasov.rmusicplayer.helpers;

import org.tumasov.rmusicplayer.helpers.entities.HttpParameter;
import org.tumasov.rmusicplayer.helpers.entities.HttpRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class HttpExecutor {
    public static String execute(HttpRequest request) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) request.getURL().openConnection();
        try {
            connection.setRequestMethod(request.getMethod());
            connection.setUseCaches(request.isUseCache());
            connection.setReadTimeout(request.getReadTimeout());
            connection.setDoInput(request.isDoInput());
            connection.setDoOutput(request.isDoOutput());
            for (HttpParameter header : request.getHeaders()) {
                connection.addRequestProperty(header.getName(), (String)header.getValue());
            }
            BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();

            String inputLine;
            while ((inputLine = bf.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString();
        } finally {
            connection.disconnect();
        }
    }
}
