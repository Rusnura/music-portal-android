package org.tumasov.rmusicplayer.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tumasov.rmusicplayer.R;
import org.tumasov.rmusicplayer.helpers.JSONUtils;
import org.tumasov.rmusicplayer.helpers.adapters.AlbumAdapter;
import org.tumasov.rmusicplayer.helpers.api.ServerAPI;
import org.tumasov.rmusicplayer.entities.Album;

import java.util.LinkedList;
import java.util.List;

public class AlbumsActivity extends AppCompatActivity {
    private ServerAPI serverAPI = ServerAPI.getInstance();
    private RecyclerView albumsRecyclerView;
    private AlbumAdapter albumAdapter;
    private Button getAllSongsButton;
    private SharedPreferences applicationSettings;
    private static final String settingsName = "R_MUSIC_SETTINGS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);

        applicationSettings = getSharedPreferences(settingsName, MODE_PRIVATE);
        albumsRecyclerView = findViewById(R.id.albums_list);

        albumAdapter = new AlbumAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        albumsRecyclerView.setLayoutManager(linearLayoutManager);
        albumsRecyclerView.setAdapter(albumAdapter);

//        rootLinearLayout = findViewById(R.id.albumsRootLinearLayout);
//        getAllSongsButton = findViewById(R.id.allSongs_btn);
//        getAllSongsButton.setOnClickListener(l -> onClickToAlbumButton("all"));

        serverAPI.getMyAlbums(r -> {
            if (r.isSuccessful()) {
                try {
                    JSONObject jAlbumsPageable = JSONUtils.parseJSON(r.getBody());
                    JSONArray jAlbums = jAlbumsPageable.getJSONArray("content");
                    List<Album> albums = new LinkedList<>();
                    for (int i = 0; i < jAlbums.length(); i++) {
                        Album album = JSONUtils.getObjectFromJSON(jAlbums.getJSONObject(i), Album.class);
                        if (album != null) albums.add(album);
                    }
                    putElementsToRootLayout(albums);
                } catch (JSONException e) {
                    Log.e("CONTENT-ACTIVITY", "Cannot parse JSON!", e);
                }
            }
        });
    }

    private void putElementsToRootLayout(List<Album> albums) {
        runOnUiThread(() -> {
            albumAdapter.getAlbums().addAll(albums);
            albumAdapter.notifyDataSetChanged();
        });
    }
}
