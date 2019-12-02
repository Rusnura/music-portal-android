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
import org.tumasov.rmusicplayer.entities.Playlist;
import org.tumasov.rmusicplayer.helpers.JSONUtils;
import org.tumasov.rmusicplayer.helpers.adapters.PlaylistAdapter;
import org.tumasov.rmusicplayer.helpers.api.ServerAPI;

import java.util.LinkedList;
import java.util.List;

public class PlaylistsActivity extends AppCompatActivity {
    private ServerAPI serverAPI = ServerAPI.getInstance();
    private RecyclerView playlistRecyclerView;
    private PlaylistAdapter playlistAdapter;
    private SharedPreferences applicationSettings;
    private static final String settingsName = "R_MUSIC_SETTINGS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlists);

        applicationSettings = getSharedPreferences(settingsName, MODE_PRIVATE);
        playlistRecyclerView = findViewById(R.id.playlist);

        playlistAdapter = new PlaylistAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        playlistRecyclerView.setLayoutManager(linearLayoutManager);
        playlistRecyclerView.setAdapter(playlistAdapter);

//        rootLinearLayout = findViewById(R.id.playlistsRootLinearLayout);
//        getAllSongsButton = findViewById(R.id.allSongs_btn);
//        getAllSongsButton.setOnClickListener(l -> onClickToPlaylistButton("all"));

        serverAPI.getMyPlaylists(r -> {
            if (r.isSuccessful()) {
                try {
                    JSONObject jPlaylistsPageable = JSONUtils.parseJSON(r.getBody());
                    JSONArray jPlaylists = jPlaylistsPageable.getJSONArray("content");
                    List<Playlist> playlists = new LinkedList<>();
                    for (int i = 0; i < jPlaylists.length(); i++) {
                        Playlist playlist = JSONUtils.getObjectFromJSON(jPlaylists.getJSONObject(i), Playlist.class);
                        if (playlist != null) playlists.add(playlist);
                    }
                    putElementsToRootLayout(playlists);
                } catch (JSONException e) {
                    Log.e("CONTENT-ACTIVITY", "Cannot parse JSON!", e);
                }
            }
        });
    }

    private void putElementsToRootLayout(List<Playlist> playlists) {
        runOnUiThread(() -> {
            playlistAdapter.getPlaylists().addAll(playlists);
            playlistAdapter.notifyDataSetChanged();
        });
    }
}
