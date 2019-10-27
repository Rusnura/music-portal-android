package org.tumasov.rmusicplayer.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.tumasov.rmusicplayer.R;
import org.tumasov.rmusicplayer.helpers.JSONUtils;
import org.tumasov.rmusicplayer.helpers.api.ServerAPI;

public class ContentActivity extends AppCompatActivity {
    private ServerAPI serverAPI = ServerAPI.getInstance();
    private Button getMySongsButton;
    private LinearLayout rootLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);

        getMySongsButton = findViewById(R.id.getMySongs_btn);
        rootLinearLayout = findViewById(R.id.albumsRootLinearLayout);

        getMySongsButton.setOnClickListener(l -> {
            serverAPI.getMySongs(r -> {
                if (r.isSuccessful()) {
                    try {
                        JSONObject jSongPageable = JSONUtils.parseJSON(r.getBody());
                        JSONArray jSongs = jSongPageable.getJSONArray("content");
                        for (int i = 0; i < jSongs.length(); i++) {
                            JSONObject song = jSongs.getJSONObject(i);
                            Button playButton = new Button(this);
                            playButton.setText(song.getString("artist") + " - " + song.getString("title"));
                            putElementToRootLayout(playButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        }
                    } catch (JSONException e) {
                        Log.e("CONTENT-ACTIVITY", "Cannot parse JSON!", e);
                    }
                }
            });
        });
    }

    private void putElementToRootLayout(View view, LinearLayout.LayoutParams layoutParams) {
        runOnUiThread(() -> {
            rootLinearLayout.addView(view, layoutParams);
        });
    }
}
