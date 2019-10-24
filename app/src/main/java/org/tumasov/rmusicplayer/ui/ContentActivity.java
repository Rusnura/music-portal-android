package org.tumasov.rmusicplayer.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.tumasov.rmusicplayer.R;

public class ContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);
    }
}
