package org.tumasov.rmusicplayer.helpers.api;

import android.util.Log;
import androidx.annotation.NonNull;
import org.json.JSONException;
import org.tumasov.rmusicplayer.entities.Song;
import org.tumasov.rmusicplayer.entities.User;
import org.tumasov.rmusicplayer.helpers.JSONUtils;
import org.tumasov.rmusicplayer.entities.Token;
import org.tumasov.rmusicplayer.helpers.http.AsyncHttpExecutor;
import org.tumasov.rmusicplayer.helpers.http.entities.HttpParameter;
import org.tumasov.rmusicplayer.helpers.http.entities.HttpRequest;
import org.tumasov.rmusicplayer.helpers.http.interfaces.AsyncHttpExecutorListener;
import java.text.MessageFormat;

public class ServerAPI {
    private static ServerAPI instance;
    private static Token token;
    private static String SERVER_URL;
    private static final String CONTENT_TYPE = "application/json";

    private ServerAPI() {}

    public static synchronized ServerAPI getInstance() {
        if (instance == null) {
            instance = new ServerAPI();
        }
        return instance;
    }

    public void register(@NonNull String serverUrl, @NonNull User user, @NonNull AsyncHttpExecutorListener listener) {
        String requestBody = "{" +
                " \"username\":\"" + user.getUsername() + "\"," +
                " \"password\":\"" + user.getPassword() + "\"," +
                " \"name\":\"" + user.getName() + "\"," +
                " \"lastname\":\"" + user.getLastname() + "\"" +
                "}";
//        String requestBody = "{\"username\": " + user.getUsername() + ", \"password\": " + user.getPassword()  + ", " +
//                "\"name\": " + user.getName() + ", \"lastname\": " + user.getLastname() + "}";
        HttpRequest request = new HttpRequest.HttpBuilder(serverUrl + "api/register", "POST")
                .doOutput(true)
                .addHeader(new HttpParameter("Content-Type", CONTENT_TYPE))
                .body(requestBody)
                .build();
        new AsyncHttpExecutor(request, listener).execute();
    }

    public void login(@NonNull String serverUrl, @NonNull String username, @NonNull String password,
                      @NonNull AsyncHttpExecutorListener listener) {
        String requestBody = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";
        HttpRequest request = new HttpRequest.HttpBuilder(serverUrl + "api/authenticate", "POST")
                .doOutput(true)
                .addHeader(new HttpParameter("Content-Type", CONTENT_TYPE))
                .body(requestBody)
                .build();
        AsyncHttpExecutorListener authListener = (r) -> {
            if (r.isSuccessful()) {
                try {
                    token = JSONUtils.getObjectFromJSON(r.getBody(), Token.class);
                    SERVER_URL = serverUrl;
                    Log.d("SERVER_API", "login(): JSON Token received: " + token.getToken());
                } catch (JSONException e) {
                    Log.e("SERVER_API", "login(): Request is successful, but cannot parse token! Error: " + e.getMessage(), e);
                }
            }
            listener.onComplete(r);
        };
        new AsyncHttpExecutor(request, authListener).execute();
    }

    public void getMyAlbums(@NonNull AsyncHttpExecutorListener listener) {
        HttpRequest request = new HttpRequest.HttpBuilder(SERVER_URL + "api/albums", "GET")
                .addHeader(new HttpParameter("Content-Type", CONTENT_TYPE))
                .addHeader(new HttpParameter("Authorization", "Bearer " + token.getToken()))
                .build();
        new AsyncHttpExecutor(request, listener).execute();
    }

    public void getAlbums(@NonNull String username, @NonNull AsyncHttpExecutorListener listener) {
        HttpRequest request = new HttpRequest.HttpBuilder(SERVER_URL + "api/user/"+username+"/albums", "GET")
                .addHeader(new HttpParameter("Content-Type", CONTENT_TYPE))
                .addHeader(new HttpParameter("Authorization", "Bearer " + token.getToken()))
                .build();
        new AsyncHttpExecutor(request, listener).execute();
    }

    public void getMySongs(@NonNull AsyncHttpExecutorListener listener) {
        HttpRequest request = new HttpRequest.HttpBuilder(SERVER_URL + "api/songs", "GET")
                .addHeader(new HttpParameter("Content-Type", CONTENT_TYPE))
                .addHeader(new HttpParameter("Authorization", "Bearer " + token.getToken()))
                .build();
        new AsyncHttpExecutor(request, listener).execute();
    }

    public void getSongsFromAlbum(@NonNull String albumId, @NonNull AsyncHttpExecutorListener listener) {
        HttpRequest request = new HttpRequest.HttpBuilder(SERVER_URL + "api/album/"+albumId+"/songs", "GET")
                .addHeader(new HttpParameter("Content-Type", CONTENT_TYPE))
                .addHeader(new HttpParameter("Authorization", "Bearer " + token.getToken()))
                .build();
        new AsyncHttpExecutor(request, listener).execute();
    }

    public void getMP3File(@NonNull String albumId, @NonNull String id, @NonNull AsyncHttpExecutorListener listener) {
        HttpRequest request = new HttpRequest.HttpBuilder(SERVER_URL + "api/album/"+albumId+"/songs", "GET")
                .addHeader(new HttpParameter("Content-Type", CONTENT_TYPE))
                .addHeader(new HttpParameter("Authorization", "Bearer " + token.getToken()))
                .build();
        new AsyncHttpExecutor(request, listener).execute();
    }

    public String getMP3FileLink(@NonNull String albumId, @NonNull String id) {
        return SERVER_URL + "api/album/" + albumId + "/song/" + id + "/mp3";
    }

    public Token getToken() {
        return token;
    }
}
