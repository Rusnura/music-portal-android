package org.tumasov.rmusicplayer.helpers.player;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import org.tumasov.rmusicplayer.entities.Song;
import org.tumasov.rmusicplayer.helpers.api.ServerAPI;
import org.tumasov.rmusicplayer.helpers.interfaces.MP3PlayerCompletedListener;
import org.tumasov.rmusicplayer.helpers.interfaces.MP3PlayerErrorListener;
import org.tumasov.rmusicplayer.helpers.interfaces.MP3PlayerPrePrepareListener;
import org.tumasov.rmusicplayer.helpers.interfaces.MP3PlayerPrepareCompleteListener;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class MP3Player {
    private MP3PlayerPrePrepareListener onMP3PlayerPrePrepareListener;
    private MP3PlayerPrepareCompleteListener onMP3PlayerPrepareCompleteListener;
    private MP3PlayerErrorListener onMP3PlayerErrorListener;
    private MP3PlayerCompletedListener onMP3PlayerCompletedListener;

    private static MP3Player instance = null;
    private ServerAPI serverAPI = ServerAPI.getInstance(); // FIXME: Player don't must known about serverAPI
    private MediaPlayer player = new MediaPlayer();
    private Song playingSong;
    private Map<String, String> headers = new LinkedHashMap<>();
    private Handler audioProgressUpdateHandler;
    private Thread updateAudioProgressThread = new Thread() {
        @Override
        public void run() {
            while (true) {
                if (isPlaying()) {
                    Message updateAudioProgressMsg = new Message();
                    updateAudioProgressMsg.what = PlayerMessages.UPDATE_AUDIO_PROGRESS_BAR;
                    if (audioProgressUpdateHandler != null) {
                        audioProgressUpdateHandler.sendMessage(updateAudioProgressMsg);
                    }
                }

                try {
                    Thread.sleep(500);
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
            if (onMP3PlayerPrepareCompleteListener != null) {
                onMP3PlayerPrepareCompleteListener.onPrepareCompleted(this);
            }
            player.start();
        });

        player.setOnErrorListener((player, what, extra) -> {
            if (onMP3PlayerErrorListener != null) {
                return onMP3PlayerErrorListener.onError(this, what, extra);
            }
            return false;
        });

        player.setOnCompletionListener((player) -> {
            if (onMP3PlayerCompletedListener != null) {
                onMP3PlayerCompletedListener.onCompleted(this);
            }
        });
    }

    public static synchronized MP3Player getInstance() {
        if (instance == null) {
            instance = new MP3Player();
        }
        return instance;
    }

    public void play(Context context, Song song) throws IOException {
        if (onMP3PlayerPrePrepareListener != null) {
            onMP3PlayerPrePrepareListener.onPrePrepare(this);
        }

        if (player.isPlaying()) {
            player.stop();
        }
        player.reset();
        player.setDataSource(context, Uri.parse(serverAPI.getMP3FileLink(song)), headers);
        this.playingSong = song;
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

    public boolean isPlaying() {
        return player.isPlaying();
    }

    public Song getPlayingSong() {
        return playingSong;
    }

    public void setOnMP3PlayerPrepareCompleteListener(MP3PlayerPrepareCompleteListener onMP3PlayerPrepareCompleteListener) {
        this.onMP3PlayerPrepareCompleteListener = onMP3PlayerPrepareCompleteListener;
    }

    public Handler getAudioProgressUpdateHandler() {
        return audioProgressUpdateHandler;
    }

    public void setAudioProgressUpdateHandler(Handler audioProgressUpdateHandler) {
        this.audioProgressUpdateHandler = audioProgressUpdateHandler;
    }
}