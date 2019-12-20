package org.tumasov.rmusicplayer.ui;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tumasov.rmusicplayer.R;
import org.tumasov.rmusicplayer.entities.Song;
import org.tumasov.rmusicplayer.helpers.JSONUtils;
import org.tumasov.rmusicplayer.helpers.player.MP3Player;
import org.tumasov.rmusicplayer.ui.adapters.SongAdapter;
import org.tumasov.rmusicplayer.helpers.api.ServerAPI;
import org.tumasov.rmusicplayer.helpers.http.entities.HttpResponse;
import org.tumasov.rmusicplayer.ui.fragments.PlayerFragment;
import java.util.LinkedList;
import java.util.List;

public class SongsActivity extends FragmentActivity {
    private ServerAPI serverAPI = ServerAPI.getInstance();
    private PlayerFragment playerFragment;
    private String selectedPlaylistId;
    private SongAdapter songAdapter;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);

        fragmentManager = getSupportFragmentManager();
        playerFragment = (PlayerFragment) fragmentManager.findFragmentById(R.id.audio_list_footer);
        selectedPlaylistId = getIntent().getStringExtra("playlistId");
        RecyclerView songsRecyclerView = findViewById(R.id.songs_list);
        songsRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        songAdapter = new SongAdapter((song, index) -> {
            MP3Player player = playerFragment.getPlayer();
            if (player.getPlaylistId() == null || !player.getPlaylistId().equals(selectedPlaylistId)) {
                player.setPlaylistId(selectedPlaylistId);
                player.setPlaylist(songAdapter.getSongs());
            }

            if (!playerFragment.play(song, index)) {
                Log.e("SongsActivity", "songAdapter::onItemSelect(): Can't play music! Index: " + index);
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
}
