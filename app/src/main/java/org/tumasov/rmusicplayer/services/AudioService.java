package org.tumasov.rmusicplayer.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import org.tumasov.rmusicplayer.R;
import org.tumasov.rmusicplayer.helpers.player.MP3Player;
import org.tumasov.rmusicplayer.ui.SongsActivity;

public class AudioService extends Service {
    private static final int NOTIFICATION_ID = 16122019;
    private final MP3Player player = MP3Player.getInstance();
    private final IBinder audioServiceBinder = new AudioServiceBinder();

    @Override
    public void onCreate() {
        super.onCreate();

        player.setOnMP3PlayerPrepareCompleteListener(p -> {
            Intent songsActivityIntent = new Intent(this, SongsActivity.class);
            songsActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendInt = PendingIntent.getActivity(this, 0, songsActivityIntent, 0);
            Notification.Builder builder = new Notification.Builder(this);

            builder.setContentIntent(pendInt)
                    .setSmallIcon(R.drawable.play)
                    .setTicker(p.getPlayingSong().getTitle())
                    .setOngoing(true)
                    .setContentTitle(p.getPlayingSong().getArtist() + " - " + p.getPlayingSong().getTitle())
                    .setContentText(p.getPlayingSong().getTitle());
            Notification notification = builder.build();
            startForeground(NOTIFICATION_ID, notification);
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        return audioServiceBinder;
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
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
