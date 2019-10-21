package org.tumasov.rmusicplayer;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {
    private Button signUpButton;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signUpButton = findViewById(R.id.signUp_button);
        loginButton = findViewById(R.id.login_button);
        signUpButton.setOnClickListener((listener) -> startActivity(new Intent(this, SignUpActivity.class)));
    }
}
