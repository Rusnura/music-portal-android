package org.tumasov.rmusicplayer.ui;

import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tumasov.rmusicplayer.PlayerActivity;
import org.tumasov.rmusicplayer.R;
import org.tumasov.rmusicplayer.entities.Song;
import org.tumasov.rmusicplayer.helpers.JSONUtils;
import org.tumasov.rmusicplayer.helpers.adapters.SongAdapter;
import org.tumasov.rmusicplayer.helpers.api.ServerAPI;
import org.tumasov.rmusicplayer.helpers.http.entities.HttpResponse;
import org.tumasov.rmusicplayer.services.AudioService;
import org.tumasov.rmusicplayer.services.AudioServiceBinder;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class AudioListActivity extends AppCompatActivity {
    private ServerAPI serverAPI = ServerAPI.getInstance();
    private RelativeLayout footerRelativeLayout;
    private SeekBar audioPositionBar;
    private String selectedAlbumId;
    private SongAdapter songAdapter;
    private AudioServiceBinder audioServiceBinder = null;
    private Handler audioProgressUpdateHandler;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            audioServiceBinder = (AudioServiceBinder) iBinder;
            audioServiceBinder.getPlayer().setAudioProgressUpdateHandler(audioProgressUpdateHandler);
            Log.i("AudioListActivity", "onServiceConnected successfully!");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_list);

        footerRelativeLayout = findViewById(R.id.audio_list_footer);
        audioPositionBar = findViewById(R.id.audioPosition);
        selectedAlbumId = getIntent().getStringExtra("albumId");
        RecyclerView songsRecyclerView = findViewById(R.id.songs_list);
        songAdapter = new SongAdapter((song) -> {
            try {
                audioServiceBinder.getPlayer().play(getApplicationContext(), selectedAlbumId, song.getId());
                if (footerRelativeLayout.getVisibility() != View.VISIBLE) {
                    footerRelativeLayout.setVisibility(View.VISIBLE);
                    startActivity(new Intent(this, PlayerActivity.class));
                }
            } catch (IOException e) {
                Log.e("AudioListActivity", "Cannot to start MP3 Player");
            }
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

        audioProgressUpdateHandler = new Handler(message -> {
            if (audioServiceBinder != null) {
                if (message.what == audioServiceBinder.getPlayer().UPDATE_AUDIO_PROGRESS_BAR) {
                    int currentProgress = audioServiceBinder.getPlayer().getAudioProgress();
                    audioPositionBar.setProgress(currentProgress);
                }
                return true;
            }
            return false;
        });
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
            Intent intent = new Intent(AudioListActivity.this, AudioService.class);

            // Below code will invoke serviceConnection's onServiceConnected method.
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
            Log.i("AudioListActivity", "bindAudioService successfully!");
        }
        Log.i("AudioListActivity", "bindAudioService ended!");
    }

    private void unBoundAudioService() {
        if (audioServiceBinder != null) {
            unbindService(serviceConnection);
            audioServiceBinder = null;
            Log.i("AudioListActivity", "unBoundAudioService successfully!");
        }
        Log.i("AudioListActivity", "unBoundAudioService ended!");
    }

    @Override
    protected void onDestroy() {
        unBoundAudioService();
        super.onDestroy();
    }
}
