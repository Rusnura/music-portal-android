package org.tumasov.rmusicplayer.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.tumasov.rmusicplayer.R;
import org.tumasov.rmusicplayer.helpers.UrlUtils;
import org.tumasov.rmusicplayer.helpers.api.ServerAPI;

import java.net.MalformedURLException;

public class LoginActivity extends AppCompatActivity {
    private ServerAPI serverAPI = ServerAPI.getInstance();
    private Button signUpButton, loginButton;
    private TextView addressTextView, loginTextView, passwordTextView;
    private ProgressBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loadingBar = findViewById(R.id.login_progress);
        addressTextView = findViewById(R.id.login_server);
        signUpButton = findViewById(R.id.login_signUp_button);
        loginButton = findViewById(R.id.login_button);
        loginTextView = findViewById(R.id.login_username);
        passwordTextView = findViewById(R.id.login_password);

        loadingBar.setVisibility(View.INVISIBLE);

        // TODO: Remove it later
        addressTextView.setText("192.168.0.105:8080");

        signUpButton.setOnClickListener((listener) -> startActivity(new Intent(this, SignUpActivity.class)));
        loginButton.setOnClickListener((listener) -> {
            if (addressTextView.getText().length() > 0 && loginTextView.getText().length() > 0 && passwordTextView.getText().length() > 0) {
                setLoadingState(true);
                serverAPI.login(UrlUtils.normalize(addressTextView.getText().toString()),
                        loginTextView.getText().toString(), passwordTextView.getText().toString(),
                        (r) -> {
                            setLoadingState(false);
                            Log.i("LOGIN_ACTIVITY", "Result: " + r);
                            if (r.isSuccessful()) {
                                startActivity(new Intent(this, ContentActivity.class));
                            }
                        });
            }
        });
    }

    private void setLoadingState(boolean isLoading) {
        runOnUiThread(() -> {
            loginButton.setEnabled(!isLoading);
            signUpButton.setEnabled(!isLoading);
            loadingBar.setVisibility(isLoading ? View.VISIBLE : View.INVISIBLE);
        });
    }
}
