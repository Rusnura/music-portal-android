package org.tumasov.rmusicplayer.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tumasov.rmusicplayer.AudioListActivity;
import org.tumasov.rmusicplayer.R;
import org.tumasov.rmusicplayer.helpers.JSONUtils;
import org.tumasov.rmusicplayer.helpers.adapters.AlbumAdapter;
import org.tumasov.rmusicplayer.helpers.api.ServerAPI;

public class AlbumsActivity extends AppCompatActivity {
    private ServerAPI serverAPI = ServerAPI.getInstance();
    private RecyclerView albumsRecyclerView;
    private AlbumAdapter albumAdapter;
    private LinearLayout rootLinearLayout;
    private Button getAllSongsButton;
    private SharedPreferences applicationSettings;
    private static final String settingsName = "R_MUSIC_SETTINGS";
    private static Intent openAlbumContentsIntent;

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
        openAlbumContentsIntent = new Intent(this, AudioListActivity.class);
        serverAPI.getMyAlbums(r -> {
            if (r.isSuccessful()) {
                try {
                    JSONObject jAlbumsPageable = JSONUtils.parseJSON(r.getBody());
                    JSONArray jAlbums = jAlbumsPageable.getJSONArray("content");
                    for (int i = 0; i < jAlbums.length(); i++) {
                        JSONObject album = jAlbums.getJSONObject(i);
//                        Button openAlbum = new Button(this);
//                        openAlbum.setText(album.getString("name"));
//                        String albumId = album.getString("id");
//                        openAlbum.setOnClickListener((l) -> onClickToAlbumButton(albumId));
                        putElementToRootLayout(album);
                    }
                } catch (JSONException e) {
                    Log.e("CONTENT-ACTIVITY", "Cannot parse JSON!", e);
                }
            }
        });
    }

    private void putElementToRootLayout(JSONObject album) {
        runOnUiThread(() -> albumAdapter.getjAlbums().add(album));
    }

    private void onClickToAlbumButton(String albumId) {
        openAlbumContentsIntent.putExtra("albumId", albumId);
        startActivity(openAlbumContentsIntent);
    }
}
