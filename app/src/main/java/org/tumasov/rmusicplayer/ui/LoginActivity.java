package org.tumasov.rmusicplayer.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import org.tumasov.rmusicplayer.R;
import org.tumasov.rmusicplayer.helpers.AsyncHttpExecutor;
import org.tumasov.rmusicplayer.helpers.entities.HttpRequest;

import java.net.MalformedURLException;
import java.net.URL;

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
        loginButton.setOnClickListener((listener) -> {
            try {
                HttpRequest loginRequest =
                        new HttpRequest.HttpBuilder(new URL("http://192.168.0.105:8080/api/authenticate"), "GET")
                                .build();
                AsyncHttpExecutor asyncHttpExecutor = new AsyncHttpExecutor(loginRequest);
                asyncHttpExecutor.execute();
            } catch (MalformedURLException e) {
                Log.w("LOGIN_ACTIVITY", "Can't parse URL!");
            }
        });
    }
}
