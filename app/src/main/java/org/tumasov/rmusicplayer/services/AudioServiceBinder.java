package org.tumasov.rmusicplayer.services;

import android.os.Binder;
import org.tumasov.rmusicplayer.helpers.MP3Player;

public class AudioServiceBinder extends Binder {
    private final MP3Player player = MP3Player.getInstance();

    public MP3Player getPlayer() {
        return player;
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
//    private Handler audioProgressUpdateHandler;
//
//    // This is the message signal that inform audio progress updater to update audio progress.
//    public final int UPDATE_AUDIO_PROGRESS_BAR = 1;
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
//    public Handler getAudioProgressUpdateHandler() {
//        return audioProgressUpdateHandler;
//    }
//
//    public void setAudioProgressUpdateHandler(Handler audioProgressUpdateHandler) {
//        this.audioProgressUpdateHandler = audioProgressUpdateHandler;
//    }
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
//                Thread updateAudioProgressThread = new Thread() {
//                    @Override
//                    public void run() {
//                        while (true) {
//                            // Create update audio progress message.
//                            Message updateAudioProgressMsg = new Message();
//                            updateAudioProgressMsg.what = UPDATE_AUDIO_PROGRESS_BAR;
//
//                            // Send the message to caller activity's update audio prgressbar Handler object.
//                            if (audioProgressUpdateHandler != null) {
//                                audioProgressUpdateHandler.sendMessage(updateAudioProgressMsg);
//                            }
//
//                            // Sleep one second.
//                            try {
//                                Thread.sleep(1000);
//                            } catch (InterruptedException ex) {
//                                // NOP
//                            }
//                        }
//                    }
//                };
//                // Run above thread object.
//                updateAudioProgressThread.start();
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
//    public int getCurrentAudioPosition() {
//        if (audioPlayer != null) {
//            return audioPlayer.getCurrentPosition();
//        }
//        return 0;
//    }
//
//    // Return total audio file duration.
//    public int getTotalAudioDuration() {
//        if (audioPlayer != null) {
//            return audioPlayer.getDuration();
//        }
//        return 0;
//    }
//
//    // Return current audio player progress value.
//    public int getAudioProgress() {
//        int currAudioPosition = getCurrentAudioPosition();
//        int totalAudioDuration = getTotalAudioDuration();
//        if (totalAudioDuration > 0) {
//            return (currAudioPosition * 100) / totalAudioDuration;
//        }
//        return 0;
//    }
//
//    public Map<String, String> getHeaders() {
//        return headers;
//    }
}