package org.tumasov.rmusicplayer.helpers.http.interfaces;

public interface AsyncHttpExecutorListener<T> {
    void onComplete(T params);
}
