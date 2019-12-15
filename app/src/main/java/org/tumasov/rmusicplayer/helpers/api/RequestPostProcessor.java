package org.tumasov.rmusicplayer.helpers.api;

import android.util.Log;
import org.json.JSONException;
import org.tumasov.rmusicplayer.entities.Token;
import org.tumasov.rmusicplayer.helpers.JSONUtils;
import org.tumasov.rmusicplayer.helpers.http.AsyncHttpExecutor;
import org.tumasov.rmusicplayer.helpers.http.entities.HttpParameter;
import org.tumasov.rmusicplayer.helpers.http.entities.HttpRequest;
import org.tumasov.rmusicplayer.helpers.http.entities.HttpResponse;
import org.tumasov.rmusicplayer.helpers.http.interfaces.AsyncHttpExecutorListener;

public class RequestPostProcessor implements AsyncHttpExecutorListener {
    private AsyncHttpExecutorListener callbackListener;
    private HttpRequest request;

    RequestPostProcessor(HttpRequest request, AsyncHttpExecutorListener callback) {
        this.request = request;
        this.callbackListener = callback;
    }

    @Override
    public void onComplete(HttpResponse response) {
        Log.d("RequestPostProcessor", "Response: " + response);
        if (!response.isAuthorized()) {
            Log.d("RequestPostProcessor", "Response is Unauthorized... Try to re-auth!");
            ServerAPI.getInstance().reAuth((l) -> {
                if (l.isSuccessful()) {
                    Token token = null;
                    try {
                        token = JSONUtils.getObjectFromJSON(l.getBody(), Token.class);
                    } catch (JSONException e) {
                        // NOP
                    }

                    if (token != null) {
                        for (HttpParameter header : request.getHeaders()) {
                            if ("Authorization".equals(header.getName())) {
                                header.setValue("Bearer " + token.getToken());
                            }
                        }
                    }
                    new AsyncHttpExecutor(request, callbackListener).execute();
                } else {
                    callbackListener.onComplete(response);
                }
            });
        } else {
            callbackListener.onComplete(response);
        }
    }
}
