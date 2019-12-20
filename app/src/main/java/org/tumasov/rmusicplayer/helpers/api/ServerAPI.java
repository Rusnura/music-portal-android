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

public class ServerAPI {
    private static ServerAPI instance;
    private static Token token;
    private static String SERVER_URL;
    private static String SERVER_LOGIN;
    private static String SERVER_PASSWORD;
    private static final String CONTENT_TYPE = "application/json";

    private ServerAPI() {}

    public static synchronized ServerAPI getInstance() {
        return instance != null ? instance : (instance = new ServerAPI());
    }

    public void register(@NonNull String serverUrl, @NonNull User user, @NonNull AsyncHttpExecutorListener listener) {
        String requestBody = "{" +
                " \"username\":\"" + user.getUsername() + "\"," +
                " \"password\":\"" + user.getPassword() + "\"," +
                " \"name\":\"" + user.getName() + "\"," +
                " \"lastname\":\"" + user.getLastname() + "\"" +
                "}";
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
                .connectionTimeout(10 * 1000)
                .addHeader(new HttpParameter("Content-Type", CONTENT_TYPE))
                .body(requestBody)
                .build();
        AsyncHttpExecutorListener authListener = (r) -> {
            if (r.isSuccessful()) {
                try {
                    token = JSONUtils.getObjectFromJSON(r.getBody(), Token.class);
                    SERVER_URL = serverUrl;
                    SERVER_LOGIN = username;
                    SERVER_PASSWORD = password;
                    Log.d("SERVER_API", "login(): JSON Token received: " + token.getToken());
                } catch (JSONException e) {
                    Log.e("SERVER_API", "login(): Request is successful, but cannot parse token! Error: " + e.getMessage(), e);
                }
            }
            listener.onComplete(r);
        };
        new AsyncHttpExecutor(request, authListener).execute();
    }

    void reAuth(AsyncHttpExecutorListener listener) {
        login(SERVER_URL, SERVER_LOGIN, SERVER_PASSWORD, listener);
    }

    public void getMyPlaylists(@NonNull AsyncHttpExecutorListener listener) {
        HttpRequest request = new HttpRequest.HttpBuilder(SERVER_URL + "api/playlists", "GET")
                .addHeader(new HttpParameter("Content-Type", CONTENT_TYPE))
                .addHeader(new HttpParameter("Authorization", "Bearer " + token.getToken()))
                .build();
        new AsyncHttpExecutor(request, new RequestPostProcessor(request, listener)).execute();
    }

    public void getPlaylists(@NonNull String username, @NonNull AsyncHttpExecutorListener listener) {
        HttpRequest request = new HttpRequest.HttpBuilder(SERVER_URL + "api/user/"+username+"/playlists", "GET")
                .addHeader(new HttpParameter("Content-Type", CONTENT_TYPE))
                .addHeader(new HttpParameter("Authorization", "Bearer " + token.getToken()))
                .build();
        new AsyncHttpExecutor(request, new RequestPostProcessor(request, listener)).execute();
    }

    public void getMySongs(@NonNull AsyncHttpExecutorListener listener) {
        HttpRequest request = new HttpRequest.HttpBuilder(SERVER_URL + "api/songs", "GET")
                .addHeader(new HttpParameter("Content-Type", CONTENT_TYPE))
                .addHeader(new HttpParameter("Authorization", "Bearer " + token.getToken()))
                .build();
        new AsyncHttpExecutor(request, new RequestPostProcessor(request, listener)).execute();
    }

    public void getSongsFromPlaylist(@NonNull String playlistId, @NonNull AsyncHttpExecutorListener listener) {
        HttpRequest request = new HttpRequest.HttpBuilder(SERVER_URL + "api/playlist/"+playlistId+"/songs", "GET")
                .addHeader(new HttpParameter("Content-Type", CONTENT_TYPE))
                .addHeader(new HttpParameter("Authorization", "Bearer " + token.getToken()))
                .build();
        new AsyncHttpExecutor(request, new RequestPostProcessor(request, listener)).execute();
    }

    public void getMP3File(@NonNull String playlistId, @NonNull String id, @NonNull AsyncHttpExecutorListener listener) {
        HttpRequest request = new HttpRequest.HttpBuilder(SERVER_URL + "api/playlist/"+playlistId+"/songs", "GET")
                .addHeader(new HttpParameter("Content-Type", CONTENT_TYPE))
                .addHeader(new HttpParameter("Authorization", "Bearer " + token.getToken()))
                .build();
        new AsyncHttpExecutor(request, new RequestPostProcessor(request, listener)).execute();
    }

    public String getMP3FileLink(@NonNull Song song) {
        return SERVER_URL + "api/playlist/" + song.getPlaylistId() + "/song/" + song.getId() + "/mp3";
    }

    public Token getToken() {
        return token;
    }
}
