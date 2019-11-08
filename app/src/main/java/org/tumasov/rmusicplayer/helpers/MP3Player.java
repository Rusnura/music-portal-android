package org.tumasov.rmusicplayer.helpers;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
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

    private MP3Player() {
        headers.put("Authorization", "Bearer " + serverAPI.getToken().getToken());
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
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

    public Song getPlayingSong() {
        return playingSong;
    }

    public void setOnMP3PlayerPrepareComplete(MP3PlayerPrepareComplete onMP3PlayerPrepareComplete) {
        this.onMP3PlayerPrepareComplete = onMP3PlayerPrepareComplete;
    }
}
