package org.tumasov.rmusicplayer.helpers;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import org.tumasov.rmusicplayer.entities.Song;
import org.tumasov.rmusicplayer.helpers.api.ServerAPI;
import org.tumasov.rmusicplayer.helpers.interfaces.MP3PlayerPrepareComplete;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class MP3Player {
    private static MP3Player instance = null;
    private ServerAPI serverAPI = ServerAPI.getInstance();
    private MediaPlayer player = new MediaPlayer();
    private Song playingSong = null;
    private Map<String, String> headers = new LinkedHashMap<>();
    private MP3PlayerPrepareComplete onMP3PlayerPrepareComplete;

    private Handler audioProgressUpdateHandler;
    public final int UPDATE_AUDIO_PROGRESS_BAR = 1;
    private Thread updateAudioProgressThread = new Thread() {
        @Override
        public void run() {
            while (true) {
                Message updateAudioProgressMsg = new Message();
                // Create update audio progress message.
                updateAudioProgressMsg.what = UPDATE_AUDIO_PROGRESS_BAR;

                // Send the message to caller activity's update audio prgressbar Handler object.
                if (audioProgressUpdateHandler != null) {
                    audioProgressUpdateHandler.sendMessage(updateAudioProgressMsg);
                }

                // Sleep one second.
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    // NOP
                }
            }
        }
    };

    private MP3Player() {
        headers.put("Authorization", "Bearer " + serverAPI.getToken().getToken());
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        updateAudioProgressThread.start();
        player.setOnPreparedListener((player) -> {
            if (onMP3PlayerPrepareComplete != null) {
                onMP3PlayerPrepareComplete.onPrepareCompleted(this);
            }
            player.start();
        });
    }

    public static synchronized MP3Player getInstance() {
        if (instance == null) {
            instance = new MP3Player();
        }
        return instance;
    }

    public void play(Context context, String albumId, String songId) throws IOException {
        if (player.isPlaying()) {
            player.stop();
        }
        player.reset();
        player.setDataSource(context, Uri.parse(serverAPI.getMP3FileLink(albumId, songId)), headers);
        player.prepareAsync();
    }

    public int getCurrentAudioPosition() {
        if (player != null && player.isPlaying()) {
            return player.getCurrentPosition();
        }
        return 0;
    }

    public int getTotalAudioDuration() {
        if (player != null && player.isPlaying()) {
            return player.getDuration();
        }
        return 0;
    }

    public int getAudioProgress() {
        int currAudioPosition = getCurrentAudioPosition();
        int totalAudioDuration = getTotalAudioDuration();
        if (totalAudioDuration > 0) {
            return (currAudioPosition * 100) / totalAudioDuration;
        }
        return 0;
    }


    public Song getPlayingSong() {
        return playingSong;
    }

    public void setOnMP3PlayerPrepareComplete(MP3PlayerPrepareComplete onMP3PlayerPrepareComplete) {
        this.onMP3PlayerPrepareComplete = onMP3PlayerPrepareComplete;
    }

    public Handler getAudioProgressUpdateHandler() {
        return audioProgressUpdateHandler;
    }

    public void setAudioProgressUpdateHandler(Handler audioProgressUpdateHandler) {
        this.audioProgressUpdateHandler = audioProgressUpdateHandler;
    }
}
