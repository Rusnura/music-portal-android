package org.tumasov.rmusicplayer.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import org.tumasov.rmusicplayer.helpers.player.MP3Player;

public class AudioService extends Service {
    private final MP3Player player = MP3Player.getInstance();
    private final IBinder audioServiceBinder = new AudioServiceBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return audioServiceBinder;
    }

    public class AudioServiceBinder extends Binder {
        AudioService getService() {
            return AudioService.this;
        }

        public MP3Player getPlayer() {
            return player;
        }
    }
}
