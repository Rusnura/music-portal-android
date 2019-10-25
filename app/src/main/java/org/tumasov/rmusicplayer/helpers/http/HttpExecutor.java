package org.tumasov.rmusicplayer.helpers.http;

import org.tumasov.rmusicplayer.helpers.http.entities.HttpParameter;
import org.tumasov.rmusicplayer.helpers.http.entities.HttpRequest;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;

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

            if (request.isDoOutput() && request.getBody() != null) {
                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, Charset.forName("UTF-8")));
                writer.write(request.getBody());
                writer.flush();
                writer.close();
                outputStream.close();
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
