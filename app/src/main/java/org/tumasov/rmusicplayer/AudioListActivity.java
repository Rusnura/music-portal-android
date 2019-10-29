package org.tumasov.rmusicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tumasov.rmusicplayer.helpers.JSONUtils;
import org.tumasov.rmusicplayer.helpers.api.ServerAPI;
import org.tumasov.rmusicplayer.helpers.http.entities.HttpResponse;

public class AudioListActivity extends AppCompatActivity {
    private ServerAPI serverAPI = ServerAPI.getInstance();
    private LinearLayout rootLinearLayout;
    private SharedPreferences applicationSettings;
    private static final String settingsName = "R_MUSIC_SETTINGS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_audio_list);
        applicationSettings = getSharedPreferences(settingsName, MODE_PRIVATE);
        String albumId = getIntent().getStringExtra("albumId");
        rootLinearLayout = findViewById(R.id.audiosRootLinearLayout);

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
                    JSONObject song = jSongs.getJSONObject(i);
                    Button playMP3 = new Button(this);
                    playMP3.setText(song.getString("artist") + " - " + song.getString("title"));
                    putElementToRootLayout(playMP3, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                }
            } catch (JSONException e) {
                Log.e("CONTENT-ACTIVITY", "Cannot parse JSON!", e);
            }
        }
    }

    private void putElementToRootLayout(View view, LinearLayout.LayoutParams layoutParams) {
        runOnUiThread(() -> rootLinearLayout.addView(view, layoutParams));
    }
}
