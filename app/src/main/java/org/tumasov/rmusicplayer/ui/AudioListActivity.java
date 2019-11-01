package org.tumasov.rmusicplayer.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tumasov.rmusicplayer.R;
import org.tumasov.rmusicplayer.entities.Song;
import org.tumasov.rmusicplayer.helpers.JSONUtils;
import org.tumasov.rmusicplayer.helpers.adapters.SongAdapter;
import org.tumasov.rmusicplayer.helpers.api.ServerAPI;
import org.tumasov.rmusicplayer.helpers.http.entities.HttpResponse;

public class AudioListActivity extends AppCompatActivity {
    private ServerAPI serverAPI = ServerAPI.getInstance();
    private RecyclerView songsRecyclerView;
    private SharedPreferences applicationSettings;
    private static final String settingsName = "R_MUSIC_SETTINGS";

    private SongAdapter songAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_audio_list);
        applicationSettings = getSharedPreferences(settingsName, MODE_PRIVATE);
        String albumId = getIntent().getStringExtra("albumId");
        songsRecyclerView = findViewById(R.id.songs_list);
        songAdapter = new SongAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        songsRecyclerView.setLayoutManager(linearLayoutManager);
        songsRecyclerView.setAdapter(songAdapter);

        if (albumId != null) {
            if (albumId.equals("all")) {
                serverAPI.getMySongs(this::handleRequest);
            } else {
                serverAPI.getSongsFromAlbum(albumId, this::handleRequest);
            }
        }
    }

    private void handleRequest(HttpResponse response) {
        if (response.isSuccessful()) {
            try {
                JSONObject jSongsPageable = JSONUtils.parseJSON(response.getBody());
                JSONArray jSongs = jSongsPageable.getJSONArray("content");
                for (int i = 0; i < jSongs.length(); i++) {
                    Song song = JSONUtils.getObjectFromJSON(jSongs.getJSONObject(i), Song.class);
                    putElementToRootLayout(song);
                }
            } catch (JSONException e) {
                Log.e("CONTENT-ACTIVITY", "Cannot parse JSON!", e);
            }
        }
    }

    private void putElementToRootLayout(Song song) {
        runOnUiThread(() -> this.songAdapter.getSongs().add(song));
    }
}
