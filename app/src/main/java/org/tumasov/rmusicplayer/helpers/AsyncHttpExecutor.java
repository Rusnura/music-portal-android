package org.tumasov.rmusicplayer.helpers;

import android.os.AsyncTask;
import android.util.Log;
import org.tumasov.rmusicplayer.helpers.entities.HttpRequest;
import java.io.IOException;

public class AsyncHttpExecutor extends AsyncTask<Void, Object, String> {
    private final HttpRequest httpRequest;

    public AsyncHttpExecutor(HttpRequest request) {
        this.httpRequest = request;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            return HttpExecutor.execute(httpRequest);
        } catch (IOException e) {
            Log.e("ASYNC_HTTP_EXECUTOR","Cannot execute request!", e);
        }

        return null;
    }
}
