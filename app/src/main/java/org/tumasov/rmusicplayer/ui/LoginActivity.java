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
import org.tumasov.rmusicplayer.helpers.api.UserAPI;
import org.tumasov.rmusicplayer.helpers.http.AsyncHttpExecutor;
import org.tumasov.rmusicplayer.helpers.http.entities.HttpRequest;
import org.tumasov.rmusicplayer.helpers.http.interfaces.AsyncHttpExecutorListener;

import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    private UserAPI userAPI = new UserAPI();
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
                try {
                    userAPI.login(UrlUtils.normilize(addressTextView.getText().toString()),
                            loginTextView.getText().toString(), passwordTextView.getText().toString(),
                            (r) -> {
                                setLoadingState(false);
                                Log.i("LOGIN_ACTIVITY", "Result: " + r);
                            });
                } catch (MalformedURLException e) {
                    Log.w("LOGIN_ACTIVITY", "Can't parse URL!");
                    setLoadingState(false);
                }
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
