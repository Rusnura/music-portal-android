package org.tumasov.rmusicplayer.services;

import android.os.Binder;
import org.tumasov.rmusicplayer.entities.Song;
import org.tumasov.rmusicplayer.helpers.player.MP3Player;
import java.util.LinkedList;
import java.util.List;

public class AudioServiceBinder extends Binder {
    private final MP3Player player = MP3Player.getInstance();
    private final List<Song> playlist = new LinkedList<>();

    public MP3Player getPlayer() {
        return player;
    }

    public List<Song> getPlaylist() {
        return playlist;
    }

    //    // Save local audio file uri ( local storage file. ).
//    private Uri audioFileUri = null;
//
//    // Save web audio file url.
//    private String audioFileUrl = "";
//
//    // Media player that play audio.
//    private MediaPlayer audioPlayer = null;
//
//    // Caller activity context, used when play local audio file.
//    private Context context = null;
//
//    // This Handler object is a reference to the caller activity's Handler.
//    // In the caller activity's handler, it will update the audio play progress.
//
//
//    // This is the message signal that inform audio progress updater to update audio progress.
//
//
//    private Map<String, String> headers = new LinkedHashMap<>();
//
//    public Context getContext() {
//        return context;
//    }
//
//    public void setContext(Context context) {
//        this.context = context;
//    }
//
//    public String getAudioFileUrl() {
//        return audioFileUrl;
//    }
//
//    public void setAudioFileUrl(String audioFileUrl) {
//        this.audioFileUrl = audioFileUrl;
//    }
//
//    public Uri getAudioFileUri() {
//        return audioFileUri;
//    }
//
//    public void setAudioFileUri(Uri audioFileUri) {
//        this.audioFileUri = audioFileUri;
//    }
//

//
//    // Start play audio.
//    public void startAudio() {
//        initAudioPlayer();
//        if (audioPlayer != null) {
//            audioPlayer.start();
//        }
//    }
//
//    // Pause playing audio.
//    public void pauseAudio() {
//        if (audioPlayer != null) {
//            audioPlayer.pause();
//        }
//    }
//
//    // Stop play audio.
//    public void stopAudio() {
//        if (audioPlayer != null) {
//            audioPlayer.stop();
//            destroyAudioPlayer();
//        }
//    }
//
//    // Initialise audio player.
//    private void initAudioPlayer() {
//        try {
//            if (audioPlayer == null) {
//                audioPlayer = new MediaPlayer();
//
//                if (!TextUtils.isEmpty(getAudioFileUrl())) {
//                    audioPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                    audioPlayer.setDataSource(getContext(), Uri.parse(getAudioFileUrl()), headers);
//                }
//                audioPlayer.prepare();
//
//                // This thread object will send update audio progress message to caller activity every 1 second.

//                // Run above thread object.
//
//            }
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    // Destroy audio player.
//    private void destroyAudioPlayer() {
//        if (audioPlayer != null) {
//            if (audioPlayer.isPlaying()) {
//                audioPlayer.stop();
//            }
//            audioPlayer.release();
//            audioPlayer = null;
//        }
//    }
//
//    // Return current audio play position.

//
//    // Return total audio file duration.

//
//    // Return current audio player progress value.

//
//    public Map<String, String> getHeaders() {
//        return headers;
//    }
}