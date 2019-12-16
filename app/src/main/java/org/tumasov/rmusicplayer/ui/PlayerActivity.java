package org.tumasov.rmusicplayer.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.SeekBar;

import org.tumasov.rmusicplayer.R;
import org.tumasov.rmusicplayer.helpers.player.PlayerMessages;
import org.tumasov.rmusicplayer.services.AudioService;

@Deprecated
public class PlayerActivity extends AppCompatActivity {
    private AudioService.AudioServiceBinder audioServiceBinder = null;
    private Handler audioProgressUpdateHandler;
    private SeekBar audioPositionBar;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            audioServiceBinder = (AudioService.AudioServiceBinder) iBinder;
            audioServiceBinder.getPlayer().setAudioMessageHandler(audioProgressUpdateHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        audioPositionBar = findViewById(R.id.player_audioPosition);
        bindAudioService();

        audioProgressUpdateHandler = new Handler(message -> { // FIXME: Duplicate
            if (audioServiceBinder != null) {
                if (message.what == PlayerMessages.UPDATE_AUDIO_PROGRESS_BAR) {
                    int currentProgress = audioServiceBinder.getPlayer().getCurrentAudioPosition();
                    int totalProgress = audioServiceBinder.getPlayer().getTotalAudioDuration();
                    if (totalProgress != audioPositionBar.getMax()) {
                        audioPositionBar.setMax(totalProgress);
                    }
                    audioPositionBar.setProgress(currentProgress);
                }
                return true;
            }
            return false;
        });
    }

    private void bindAudioService() {
        if (audioServiceBinder == null) {
            Intent intent = new Intent(PlayerActivity.this, AudioService.class);
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    private void unBoundAudioService() {
        if (audioServiceBinder != null) {
            unbindService(serviceConnection);
            audioServiceBinder = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        bindAudioService();
    }

    @Override
    public void onPause() {
        unBoundAudioService();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        unBoundAudioService();
        super.onDestroy();
    }
}
