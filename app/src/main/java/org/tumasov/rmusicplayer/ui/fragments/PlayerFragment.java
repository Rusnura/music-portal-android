package org.tumasov.rmusicplayer.ui.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import org.tumasov.rmusicplayer.R;
import org.tumasov.rmusicplayer.helpers.player.MP3Player;
import org.tumasov.rmusicplayer.helpers.player.PlayerMessages;
import org.tumasov.rmusicplayer.services.AudioService;
import org.tumasov.rmusicplayer.services.AudioServiceBinder;
import org.tumasov.rmusicplayer.ui.SongsActivity;
import java.io.IOException;

public class PlayerFragment extends Fragment {
    private AudioServiceBinder audioServiceBinder = null;
    private Handler audioProgressUpdateHandler;
    private SeekBar audioPositionBar;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            FragmentActivity activity = getActivity();
            audioServiceBinder = (AudioServiceBinder) iBinder;
            MP3Player player = audioServiceBinder.getPlayer();
            player.setAudioProgressUpdateHandler(audioProgressUpdateHandler);
            if (player.getPlaylist() == null && activity instanceof SongsActivity) {
                player.setPlaylist(((SongsActivity) activity).getSongAdapter().getSongs());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {}
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_player, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        audioProgressUpdateHandler = new Handler(message -> {
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

        audioPositionBar = view.findViewById(R.id.audioPosition);
        audioPositionBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && audioServiceBinder != null) {
                    audioServiceBinder.getPlayer().setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bindAudioService()) {
            Log.d("PlayerFragment", "onResume::BindAudioService successfully!");
        } else {
            Log.e("PlayerFragment", "onResume::BindAudioService NOT successfully!");
        }
    }

    @Override
    public void onPause() {
        if (unBoundAudioService()) {
            Log.d("PlayerFragment", "onPause::UNBoundAudioService successfully!");
        } else {
            Log.e("PlayerFragment", "onPause::UNBoundAudioService NOT successfully!");
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        unBoundAudioService();
        super.onDestroy();
    }

    private boolean bindAudioService() {
        FragmentActivity activity = getActivity();
        if (audioServiceBinder == null && activity != null) {
            Intent intent = new Intent(getActivity(), AudioService.class);
            activity.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
            return true;
        }
        return false;
    }

    private boolean unBoundAudioService() {
        FragmentActivity activity = getActivity();
        if (audioServiceBinder != null && activity != null) {
            getActivity().unbindService(serviceConnection);
            audioServiceBinder = null;
            return true;
        }
        return false;
    }

    public boolean play(int index) {
        if (audioServiceBinder != null) {
            try {
                audioServiceBinder.getPlayer().play(getContext(), index);
                return true;
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }
}