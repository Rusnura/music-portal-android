package org.tumasov.rmusicplayer.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import org.tumasov.rmusicplayer.R;
import org.tumasov.rmusicplayer.helpers.api.UserAPI;
import org.tumasov.rmusicplayer.helpers.http.AsyncHttpExecutor;
import org.tumasov.rmusicplayer.helpers.http.entities.HttpRequest;
import org.tumasov.rmusicplayer.helpers.http.interfaces.AsyncHttpExecutorListener;

import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    private UserAPI userAPI = new UserAPI();
    private Button signUpButton, loginButton;
    private TextView loginTextView, passwordTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signUpButton = findViewById(R.id.login_signUp_button);
        loginButton = findViewById(R.id.login_button);
        loginTextView = findViewById(R.id.login_username);
        passwordTextView = findViewById(R.id.login_password);

        signUpButton.setOnClickListener((listener) -> startActivity(new Intent(this, SignUpActivity.class)));
        loginButton.setOnClickListener((listener) -> {
            if (loginTextView.getText().length() > 0 && passwordTextView.getText().length() > 0) {
                try {
                    userAPI.login("http://192.168.0.105:8080",
                            loginTextView.getText().toString(),
                            passwordTextView.getText().toString(),
                            (r) -> Log.i("LOGIN_ACTIVITY", "Result" + r));
                } catch (MalformedURLException e) {
                    Log.w("LOGIN_ACTIVITY", "Can't parse URL!");
                }
            }
        });
    }
}
