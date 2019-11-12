package org.tumasov.rmusicplayer.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AudioService extends Service {
    private final AudioServiceBinder audioServiceBinder = new AudioServiceBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return audioServiceBinder;
    }
}
