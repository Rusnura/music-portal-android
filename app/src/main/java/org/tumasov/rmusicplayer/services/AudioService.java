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
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.tumasov.rmusicplayer.R;
import org.tumasov.rmusicplayer.helpers.player.MP3Player;
import org.tumasov.rmusicplayer.ui.SongsActivity;

public class AudioService extends Service {
    private static final String NOTIFICATION_ID = "17122019";
    private final MP3Player player = MP3Player.getInstance();
    private final IBinder audioServiceBinder = new AudioServiceBinder();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();

        player.setOnMP3PlayerPrepareCompleteListener(p -> {
            Intent songsActivityIntent = new Intent(this, SongsActivity.class);
            songsActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendInt = PendingIntent.getActivity(this, 0, songsActivityIntent, 0);
            Notification.Builder builder = new Notification.Builder(this);
            builder.setContentIntent(pendInt)
                    .setSmallIcon(R.drawable.play)
                    .setTicker(p.getPlayingSong().getTitle())
                    .setOngoing(true)
                    .setChannelId(NOTIFICATION_ID)
                    .setContentTitle(p.getPlayingSong().getArtist() + " - " + p.getPlayingSong().getTitle())
                    .setContentText(p.getPlayingSong().getTitle());
            Notification notification = builder.build();
            startForeground(Integer.parseInt(NOTIFICATION_ID), notification);

//            Notification notification = new NotificationCompat.Builder(this, NOTIFICATION_ID)
//                    .setSmallIcon(R.drawable.play)
//                    .setContentTitle(p.getPlayingSong().getTitle())
//                    .setContentText("Song")
//                    .setOngoing(true)
//                    .addAction(R.drawable.previous_24dp, "Previous", playbackAction(3))
//                    .addAction(plaorpa, "Pause", playbackAction(1))
//                    .addAction(R.drawable.next_24dp, "Next", playbackAction(2))
//                    .setContentIntent(contentIntent)
//                    .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
//                            .setShowActionsInCompactView(0, 1, 2)
//                            .setMediaSession(mediaSession.getSessionToken()))
//                    .setSubText(artist)
//                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                    .build();
//            notificationManager.notify(Integer.parseInt(NOTIFICATION_ID), notification);
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_ID,
                    "Song", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("HEY");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return audioServiceBinder;
    }

    @Override
    public void onDestroy() {
        Log.d("AudioService", "AudioService::onDestroy()");
        //stopForeground(true);
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
