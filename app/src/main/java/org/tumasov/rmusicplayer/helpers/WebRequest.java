package org.tumasov.rmusicplayer.helpers;

import androidx.annotation.Nullable;

import org.tumasov.rmusicplayer.helpers.entities.HttpHeader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class WebRequest {
    public String GET(String url, @Nullable List<HttpHeader> headers) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        try {
            connection.setRequestMethod("GET");
            if (headers != null) {
                for (HttpHeader header: headers) {
                    connection.addRequestProperty(header.getName(), header.getValue());
                }
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
