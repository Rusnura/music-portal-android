package org.tumasov.rmusicplayer.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import org.tumasov.rmusicplayer.R;

public class SignUpActivity extends AppCompatActivity {
    private Button backButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        backButton = findViewById(R.id.back_button);
        registerButton = findViewById(R.id.doSignUp_button);

        backButton.setOnClickListener((l) -> startActivity(new Intent(this, LoginActivity.class)));
    }
}
