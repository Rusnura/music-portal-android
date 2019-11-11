package org.tumasov.rmusicplayer.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tumasov.rmusicplayer.R;
import org.tumasov.rmusicplayer.entities.Song;
import org.tumasov.rmusicplayer.helpers.JSONUtils;
import org.tumasov.rmusicplayer.helpers.adapters.SongAdapter;
import org.tumasov.rmusicplayer.helpers.api.ServerAPI;
import org.tumasov.rmusicplayer.helpers.http.entities.HttpResponse;
import org.tumasov.rmusicplayer.services.AudioService;
import org.tumasov.rmusicplayer.services.AudioServiceBinder;

import java.util.LinkedList;
import java.util.List;

public class AudioListActivity extends AppCompatActivity {
    private ServerAPI serverAPI = ServerAPI.getInstance();
    private RecyclerView songsRecyclerView;
    private SharedPreferences applicationSettings;
    private String selectedAlbumId;
    private static final String settingsName = "R_MUSIC_SETTINGS";
    private SongAdapter songAdapter;
    private AudioServiceBinder audioServiceBinder = null;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            audioServiceBinder = (AudioServiceBinder) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_audio_list);
        bindAudioService();
        applicationSettings = getSharedPreferences(settingsName, MODE_PRIVATE);
        selectedAlbumId = getIntent().getStringExtra("albumId");
        songsRecyclerView = findViewById(R.id.songs_list);
        songAdapter = new SongAdapter(selectedAlbumId, (song) -> {
            // Set web audio file url
            audioServiceBinder.getHeaders().put("Authorization", "Bearer " + serverAPI.getToken().getToken());
            audioServiceBinder.setAudioFileUrl(serverAPI.getMP3FileLink(selectedAlbumId, song.getId()));

            // Web audio is a stream audio.
            audioServiceBinder.setStreamAudio(true);

            // Set application context.
            audioServiceBinder.setContext(getApplicationContext());

            // Initialize audio progress bar updater Handler object.
            // createAudioProgressbarUpdater();
            // audioServiceBinder.setAudioProgressUpdateHandler(audioProgressUpdateHandler);

            // Start audio in background service.
            audioServiceBinder.startAudio();
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        songsRecyclerView.setLayoutManager(linearLayoutManager);
        songsRecyclerView.setAdapter(songAdapter);

        if (selectedAlbumId != null) {
            if (selectedAlbumId.equals("all")) {
                serverAPI.getMySongs(this::handleRequest);
            } else {
                serverAPI.getSongsFromAlbum(selectedAlbumId, this::handleRequest);
            }
        }
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
        if(audioServiceBinder == null) {
            Intent intent = new Intent(AudioListActivity.this, AudioService.class);

            // Below code will invoke serviceConnection's onServiceConnected method.
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    // Unbound background audio service with caller activity.
    private void unBoundAudioService() {
        if(audioServiceBinder != null) {
            unbindService(serviceConnection);
        }
    }

    @Override
    protected void onDestroy() {
        // Unbound background audio service when activity is destroyed.
        unBoundAudioService();
        super.onDestroy();
    }
}
