package org.tumasov.rmusicplayer.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.tumasov.rmusicplayer.R;
import org.tumasov.rmusicplayer.helpers.UrlUtils;
import org.tumasov.rmusicplayer.helpers.api.ServerAPI;

public class LoginActivity extends AppCompatActivity {
    private ServerAPI serverAPI = ServerAPI.getInstance();
    private Button signUpButton, loginButton;
    private TextView addressTextView, loginTextView, passwordTextView;
    private ProgressBar loadingBar;
    private SharedPreferences applicationSettings;
    private static Intent openSignUpActivity;
    private static Intent openPlaylistsActivity;
    private static final String settingsName = "R_MUSIC_SETTINGS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        applicationSettings = getSharedPreferences(settingsName, MODE_PRIVATE);
        loadingBar = findViewById(R.id.login_progress);
        addressTextView = findViewById(R.id.login_server);
        signUpButton = findViewById(R.id.login_signUp_button);
        loginButton = findViewById(R.id.login_button);
        loginTextView = findViewById(R.id.login_username);
        passwordTextView = findViewById(R.id.login_password);
        openSignUpActivity = new Intent(this, SignUpActivity.class);
        openPlaylistsActivity = new Intent(this, PlaylistsActivity.class);

        loadingBar.setVisibility(View.INVISIBLE);

        signUpButton.setOnClickListener((listener) -> startActivity(openSignUpActivity));
        loginButton.setOnClickListener((listener) -> {
            if (addressTextView.getText().length() > 0 && loginTextView.getText().length() > 0 && passwordTextView.getText().length() > 0) {
                setLoadingState(true);
                serverAPI.login(UrlUtils.normalize(addressTextView.getText().toString()),
                        loginTextView.getText().toString(), passwordTextView.getText().toString(),
                        (r) -> {
                            setLoadingState(false);
                            Log.i("LOGIN_ACTIVITY", "Result: " + r);
                            if (r.isSuccessful()) {
                                SharedPreferences.Editor settingsEditor = applicationSettings.edit();
                                settingsEditor.putString("apiServer", addressTextView.getText().toString());
                                settingsEditor.putString("username", loginTextView.getText().toString());
                                settingsEditor.putString("password", passwordTextView.getText().toString());
                                settingsEditor.apply();
                                startActivity(openPlaylistsActivity);
                            }
                        });
            }
        });

        if (applicationSettings.contains("apiServer")
                && applicationSettings.contains("username") && applicationSettings.contains("password")) {
            addressTextView.setText(applicationSettings.getString("apiServer", ""));
            loginTextView.setText(applicationSettings.getString("username", ""));
            passwordTextView.setText(applicationSettings.getString("password", ""));
            loginButton.callOnClick();
        }
    }

    private void setLoadingState(boolean isLoading) {
        runOnUiThread(() -> {
            loginButton.setEnabled(!isLoading);
            signUpButton.setEnabled(!isLoading);
            loadingBar.setVisibility(isLoading ? View.VISIBLE : View.INVISIBLE);
        });
    }
}
