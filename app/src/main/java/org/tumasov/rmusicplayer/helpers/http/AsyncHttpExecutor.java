package org.tumasov.rmusicplayer.helpers.http;

import android.os.AsyncTask;
import android.util.Log;
import org.tumasov.rmusicplayer.helpers.http.entities.HttpRequest;
import org.tumasov.rmusicplayer.helpers.http.entities.HttpResponse;
import org.tumasov.rmusicplayer.helpers.http.interfaces.AsyncHttpExecutorListener;

import java.io.IOException;

public class AsyncHttpExecutor extends AsyncTask<Void, Object, Void> {
    private final HttpRequest httpRequest;
    private final AsyncHttpExecutorListener listener;

    public AsyncHttpExecutor(HttpRequest request, AsyncHttpExecutorListener listener) {
        this.httpRequest = request;
        this.listener = listener;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Void doInBackground(Void... voids) {
        HttpResponse response;
        try {
            response = HttpExecutor.execute(httpRequest);
        } catch (IOException e) {
            Log.e("ASYNC_HTTP_EXECUTOR","Cannot execute request!", e);
            response = new HttpResponse(424, null);
        }
        listener.onComplete(response);
        return null;
    }
}
