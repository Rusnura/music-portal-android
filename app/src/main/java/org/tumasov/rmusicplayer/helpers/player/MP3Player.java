package org.tumasov.rmusicplayer.helpers.player;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import org.tumasov.rmusicplayer.entities.Song;
import org.tumasov.rmusicplayer.helpers.api.ServerAPI;
import org.tumasov.rmusicplayer.helpers.interfaces.MP3PlayerCompletedListener;
import org.tumasov.rmusicplayer.helpers.interfaces.MP3PlayerErrorListener;
import org.tumasov.rmusicplayer.helpers.interfaces.MP3PlayerPrePrepareListener;
import org.tumasov.rmusicplayer.helpers.interfaces.MP3PlayerPrepareCompleteListener;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MP3Player {
    private MP3PlayerPrePrepareListener onMP3PlayerPrePrepareListener;
    private MP3PlayerPrepareCompleteListener onMP3PlayerPrepareCompleteListener;
    private MP3PlayerErrorListener onMP3PlayerErrorListener;
    private MP3PlayerCompletedListener onMP3PlayerCompletedListener;

    private static MP3Player instance = null;
    private MediaPlayer player = new MediaPlayer();
    private Context context; // FIXME: Please, fix it, so bad :(
    private ServerAPI serverAPI = ServerAPI.getInstance(); // FIXME: Player don't must known about serverAPI
    private List<Song> playlist = null; // FIXME: It's correct???
    private String playlistId;
    private Song playingSong;
    private int playingSongId;
    private String playerStatus;
    private Map<String, String> headers = new LinkedHashMap<>();
    private Handler audioMessageHandler;
    private Thread updateAudioProgressThread = new Thread() {
        @Override
        public void run() {
            while (true) {
                if (isPlaying()) {
                    Message updateAudioProgressMessage = new Message();
                    updateAudioProgressMessage.what = PlayerMessages.UPDATE_AUDIO_PROGRESS_BAR;
                    if (audioMessageHandler != null) {
                        audioMessageHandler.sendMessage(updateAudioProgressMessage);
                    }
                }

                Message statusAudioMessage = new Message();
                statusAudioMessage.what = PlayerMessages.UPDATE_AUDIO_MESSAGE;
                if (audioMessageHandler != null) {
                    audioMessageHandler.sendMessage(statusAudioMessage);
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
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        updateAudioProgressThread.start();
        player.setOnPreparedListener((player) -> {
            if (onMP3PlayerPrepareCompleteListener != null) {
                onMP3PlayerPrepareCompleteListener.onPrepareCompleted(this);
            }
            playerStatus = playingSong.getArtist() + " - " + playingSong.getTitle();
            player.start();
        });

        player.setOnErrorListener((player, what, extra) -> {
            playerStatus = "Ошибка :(";
            if (onMP3PlayerErrorListener != null) {
                return onMP3PlayerErrorListener.onError(this, what, extra);
            }
            return false;
        });

        player.setOnCompletionListener((player) -> {
            Log.d("MP3Player", "player.setOnCompletionListener");
            if (onMP3PlayerCompletedListener != null) {
                onMP3PlayerCompletedListener.onCompleted(this);
            }
            next();
        });
    }

    public static synchronized MP3Player getInstance() {
        return (instance != null) ? instance : (instance = new MP3Player());
    }

    private void play0(Context context, int playingSongId) {
        try {
            play(context, playingSongId);
        } catch (IOException e) {
            Log.e("MP3Player", "player.setOnCompletionListener::play(Context, int) exception", e);
        }
    }

    public void next() {
        if (context != null && playlist != null && playlist.size() > 0) {
            if ((playingSongId + 1) >= playlist.size()) {
                playingSongId = 0;
            } else {
                playingSongId++;
            }
            play0(context, playingSongId);
        }
    }

    public void previous() {
        if (context != null && playlist != null && playlist.size() > 0) {
            if ((playingSongId - 1) < 0) {
                playingSongId = playlist.size() - 1;
            } else {
                playingSongId--;
            }
            play0(context, playingSongId);
        }
    }

    public void pauseOrResume() {
        if (context != null) {
            if (player.isPlaying()) {
                player.pause();
            } else {
                player.start();
            }
        }
    }

    public void play(Context context, int songId) throws IOException {
        headers.put("Authorization", "Bearer " + serverAPI.getToken().getToken());
        playerStatus = "Подготовка...";
        Song song = playlist.get(songId);
        if (this.context == null) {
            this.context = context;
        }

        if (onMP3PlayerPrePrepareListener != null) {
            onMP3PlayerPrePrepareListener.onPrePrepare(this);
        }

        if (player.isPlaying()) {
            player.stop();
        }
        player.reset();
        player.setDataSource(context, Uri.parse(serverAPI.getMP3FileLink(song)), headers);
        this.playingSong = song;
        this.playingSongId = songId;
        player.prepareAsync();
    }

    public int getCurrentAudioPosition() {
        if (player != null && player.isPlaying()) {
            return player.getCurrentPosition();
        }
        return 0;
    }

    public int getTotalAudioDuration() {
        if (player != null) {
            return player.getDuration();
        }
        return 0;
    }

    public void setProgress(int msec) {
        if (player != null) {
            player.seekTo(msec);
        }
    }

    public boolean isPlaying() {
        return player.isPlaying();
    }

    public Song getPlayingSong() {
        return playingSong;
    }

    public List<Song> getPlaylist() {
        return playlist;
    }

    public void setPlaylist(List<Song> playlist) {
        this.playlist = playlist;
    }

    public String getStatus() {
        return playerStatus;
    }

    public void setOnMP3PlayerErrorListener(MP3PlayerErrorListener onMP3PlayerErrorListener) {
        this.onMP3PlayerErrorListener = onMP3PlayerErrorListener;
    }

    public void setOnMP3PlayerCompletedListener(MP3PlayerCompletedListener onMP3PlayerCompletedListener) {
        this.onMP3PlayerCompletedListener = onMP3PlayerCompletedListener;
    }

    public void setOnMP3PlayerPrePrepareListener(MP3PlayerPrePrepareListener onMP3PlayerPrePrepareListener) {
        this.onMP3PlayerPrePrepareListener = onMP3PlayerPrePrepareListener;
    }

    public void setOnMP3PlayerPrepareCompleteListener(MP3PlayerPrepareCompleteListener onMP3PlayerPrepareCompleteListener) {
        this.onMP3PlayerPrepareCompleteListener = onMP3PlayerPrepareCompleteListener;
    }

    public void setAudioMessageHandler(Handler audioMessageHandler) {
        this.audioMessageHandler = audioMessageHandler;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public String getPlaylistId() {
        return playlistId;
    }
}
