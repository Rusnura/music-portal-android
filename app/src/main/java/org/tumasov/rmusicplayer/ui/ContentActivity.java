package org.tumasov.rmusicplayer.ui;

import androidx.appcompat.app.AppCompatActivity;

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
import org.tumasov.rmusicplayer.R;
import org.tumasov.rmusicplayer.helpers.JSONUtils;
import org.tumasov.rmusicplayer.helpers.api.ServerAPI;

public class ContentActivity extends AppCompatActivity {
    private ServerAPI serverAPI = ServerAPI.getInstance();
    private LinearLayout rootLinearLayout;
    private SharedPreferences applicationSettings;
    private static final String settingsName = "R_MUSIC_SETTINGS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);
        applicationSettings = getSharedPreferences(settingsName, MODE_PRIVATE);
        rootLinearLayout = findViewById(R.id.albumsRootLinearLayout);

        serverAPI.getMyAlbums(r -> {
            if (r.isSuccessful()) {
                try {
                    JSONObject jAlbumsPageable = JSONUtils.parseJSON(r.getBody());
                    JSONArray jAlbums = jAlbumsPageable.getJSONArray("content");
                    for (int i = 0; i < jAlbums.length(); i++) {
                        JSONObject album = jAlbums.getJSONObject(i);
                        Button openAlbum = new Button(this);
                        openAlbum.setText(album.getString("name"));
                        putElementToRootLayout(openAlbum, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    }
                } catch (JSONException e) {
                    Log.e("CONTENT-ACTIVITY", "Cannot parse JSON!", e);
                }
            }
        });
    }

    private void putElementToRootLayout(View view, LinearLayout.LayoutParams layoutParams) {
        runOnUiThread(() -> rootLinearLayout.addView(view, layoutParams));
    }
}
