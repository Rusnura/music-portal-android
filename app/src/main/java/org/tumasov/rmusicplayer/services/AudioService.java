package org.tumasov.rmusicplayer.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import org.tumasov.rmusicplayer.R;
import org.tumasov.rmusicplayer.helpers.player.MP3Player;
import org.tumasov.rmusicplayer.ui.SongsActivity;
import java.util.Objects;

public class AudioService extends Service {
    private static final String NOTIFICATION_ID = "17122019";
    private final MP3Player player = MP3Player.getInstance();
    private final IBinder audioServiceBinder = new AudioServiceBinder();
    private Notification notification;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }

        player.setOnMP3PlayerPrepareCompleteListener(p -> {
            Intent songsActivityIntent = new Intent(this, SongsActivity.class);
            songsActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendInt = PendingIntent.getActivity(this, 0, songsActivityIntent, 0);
            Notification.Builder builder = new Notification.Builder(this);
            builder.setContentIntent(pendInt)
                .setSmallIcon(R.drawable.play)
                .setTicker(p.getPlayingSong().getTitle())
                .setContentTitle(p.getPlayingSong().getArtist() + " - " + p.getPlayingSong().getTitle())
                .setContentText(p.getPlayingSong().getTitle());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder.setChannelId(NOTIFICATION_ID);
            }
            notification = builder.build();
            startForegroundNotification();
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_ID,
                    "RMusicPlayer", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            Objects.requireNonNull(manager).createNotificationChannel(notificationChannel);
        }
    }

    public void startForegroundNotification() {
        startForeground(Integer.parseInt(NOTIFICATION_ID), notification);
    }

    public void stopForegroundNotification() {
        stopForeground(true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return audioServiceBinder;
    }

    public class AudioServiceBinder extends Binder {
        public AudioService getService() {
            return AudioService.this;
        }

        public MP3Player getPlayer() {
            return player;
        }
    }
}
