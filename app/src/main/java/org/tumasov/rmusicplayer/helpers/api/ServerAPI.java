package org.tumasov.rmusicplayer.helpers.api;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;
import org.tumasov.rmusicplayer.helpers.JSONUtils;
import org.tumasov.rmusicplayer.helpers.http.AsyncHttpExecutor;
import org.tumasov.rmusicplayer.helpers.http.entities.HttpParameter;
import org.tumasov.rmusicplayer.helpers.http.entities.HttpRequest;
import org.tumasov.rmusicplayer.helpers.http.interfaces.AsyncHttpExecutorListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

public class ServerAPI {
    private static String JWT_TOKEN = null;
    private static final String CONTENT_TYPE = "application/json";

    public void register(@NonNull String serverUrl, @NonNull String username, @NonNull String password,
                         @NonNull String name, @NonNull String lastname, @NonNull AsyncHttpExecutorListener listener) throws MalformedURLException {
        URL url = new URL(serverUrl + "api/register");
        String requestBody = MessageFormat.format("{username: {0}, password: {1}, name: {2}, lastname: {3}}",
                                username, password, name, lastname);
        HttpRequest request = new HttpRequest.HttpBuilder(url, "POST")
                .doOutput(true)
                .addHeader(new HttpParameter("Content-Type", CONTENT_TYPE))
                .body(requestBody)
                .build();
        new AsyncHttpExecutor(request, listener).execute();
    }

    public void login(@NonNull String serverUrl, @NonNull String username, @NonNull String password,
                      @NonNull AsyncHttpExecutorListener listener) throws MalformedURLException {
        URL url = new URL(serverUrl + "api/authenticate");

        String requestBody = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";
        HttpRequest request = new HttpRequest.HttpBuilder(url, "POST")
                .doOutput(true)
                .addHeader(new HttpParameter("Content-Type", CONTENT_TYPE))
                .body(requestBody)
                .build();
        AsyncHttpExecutorListener authListener = (r) -> {
            if (r.isSuccessful()) {
                try {
                    JSONObject jToken = JSONUtils.parseJSON(r.getBody());
                    JWT_TOKEN = jToken.getString("token");
                    Log.d("SERVER_API", "login(): JSON Token received: " + JWT_TOKEN);
                } catch (JSONException e) {
                    Log.e("SERVER_API", "login(): Request is successful, but cannot parse token! Error: " + e.getMessage(), e);
                }
            }
            listener.onComplete(r);
        };
        new AsyncHttpExecutor(request, authListener).execute();
    }

    public void getMySongs() {
        // TODO: Releae
    }
}
