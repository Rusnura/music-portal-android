package org.tumasov.rmusicplayer.ui;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tumasov.rmusicplayer.R;
import org.tumasov.rmusicplayer.entities.Song;
import org.tumasov.rmusicplayer.helpers.JSONUtils;
import org.tumasov.rmusicplayer.helpers.adapters.SongAdapter;
import org.tumasov.rmusicplayer.helpers.api.ServerAPI;
import org.tumasov.rmusicplayer.helpers.http.entities.HttpResponse;
import org.tumasov.rmusicplayer.helpers.player.PlayerMessages;
import org.tumasov.rmusicplayer.services.AudioService;
import org.tumasov.rmusicplayer.services.AudioServiceBinder;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class SongsActivity extends FragmentActivity {
    private ServerAPI serverAPI = ServerAPI.getInstance();
    private View footerRelativeLayout;
    private SeekBar audioPositionBar;
    private String selectedPlaylistId;
    private SongAdapter songAdapter;
    private AudioServiceBinder audioServiceBinder = null;
    private Handler audioProgressUpdateHandler;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            audioServiceBinder = (AudioServiceBinder) iBinder;
            audioServiceBinder.getPlayer().setAudioProgressUpdateHandler(audioProgressUpdateHandler);
            if (audioServiceBinder.getPlayer().getPlaylist() == null) { // Set playlist to MP3Player
                audioServiceBinder.getPlayer().setPlaylist(songAdapter.getSongs());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);

        footerRelativeLayout = findViewById(R.id.audio_list_footer);
        audioPositionBar = findViewById(R.id.audioPosition);
        selectedPlaylistId = getIntent().getStringExtra("playlistId");
        RecyclerView songsRecyclerView = findViewById(R.id.songs_list);
        songsRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        songAdapter = new SongAdapter((song, index) -> {
            try {
                audioServiceBinder.getPlayer().play(getApplicationContext(), index);
                if (footerRelativeLayout.getVisibility() != View.VISIBLE) {
                    footerRelativeLayout.setVisibility(View.VISIBLE);
                }
            } catch (IOException e) {
                Log.e("SongsActivity", "Cannot to start MP3 Player");
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        songsRecyclerView.setLayoutManager(linearLayoutManager);
        songsRecyclerView.setAdapter(songAdapter);

        if (selectedPlaylistId != null) {
            if (selectedPlaylistId.equals("all")) {
                serverAPI.getMySongs(this::handleRequest);
            } else {
                serverAPI.getSongsFromPlaylist(selectedPlaylistId, this::handleRequest);
            }
        }

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

    private void handleRequest(HttpResponse response) {
        if (response.isSuccessful()) {
            try {
                JSONObject jSongsPageable = JSONUtils.parseJSON(response.getBody());
                JSONArray jSongs = jSongsPageable.getJSONArray("content");
                List<Song> songs = new LinkedList<>();
                for (int i = 0; i < jSongs.length(); i++) {
                    Song song = JSONUtils.getObjectFromJSON(jSongs.getJSONObject(i), Song.class);
                    songs.add(song);
                }
                putElementsToRootLayout(songs);
            } catch (JSONException e) {
                Log.e("CONTENT-ACTIVITY", "Cannot parse JSON!", e);
            }
        }
    }

    private void putElementsToRootLayout(List<Song> song) {
        runOnUiThread(() -> {
            songAdapter.getSongs().addAll(song);
            songAdapter.notifyDataSetChanged();
        });
    }

    private void bindAudioService() {
        if (audioServiceBinder == null) {
            Intent intent = new Intent(SongsActivity.this, AudioService.class);

            // Below code will invoke serviceConnection's onServiceConnected method.
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
            Log.i("SongsActivity", "bindAudioService successfully!");
        }
        Log.i("SongsActivity", "bindAudioService ended!");
    }

    private void unBoundAudioService() {
        if (audioServiceBinder != null) {
            unbindService(serviceConnection);
            audioServiceBinder = null;
            Log.i("SongsActivity", "unBoundAudioService successfully!");
        }
        Log.i("SongsActivity", "unBoundAudioService ended!");
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
