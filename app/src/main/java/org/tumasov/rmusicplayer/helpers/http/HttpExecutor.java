package org.tumasov.rmusicplayer.helpers.http;

import org.tumasov.rmusicplayer.helpers.http.entities.HttpParameter;
import org.tumasov.rmusicplayer.helpers.http.entities.HttpRequest;
import org.tumasov.rmusicplayer.helpers.http.entities.HttpResponse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;

public class HttpExecutor {
    public static HttpResponse execute(HttpRequest request) throws IOException {
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

            StringBuilder response = new StringBuilder();
            if (request.isDoInput()) {
                InputStream is = (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) ? connection.getInputStream() : connection.getErrorStream();
                BufferedReader bf = new BufferedReader(new InputStreamReader(is));

                String inputLine;
                while ((inputLine = bf.readLine()) != null) {
                    response.append(inputLine);
                }
            }
            return new HttpResponse(connection.getResponseCode(), response.toString());
        } finally {
            connection.disconnect();
        }
    }
}
