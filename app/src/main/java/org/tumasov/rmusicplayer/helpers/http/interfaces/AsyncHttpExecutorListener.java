package org.tumasov.rmusicplayer.helpers.http.interfaces;

import org.tumasov.rmusicplayer.helpers.http.entities.HttpResponse;

public interface AsyncHttpExecutorListener {
    void onComplete(HttpResponse response);
}
