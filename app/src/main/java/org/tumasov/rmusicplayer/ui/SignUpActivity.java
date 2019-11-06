package org.tumasov.rmusicplayer.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.tumasov.rmusicplayer.R;
import org.tumasov.rmusicplayer.entities.User;
import org.tumasov.rmusicplayer.helpers.UrlUtils;
import org.tumasov.rmusicplayer.helpers.api.ServerAPI;

public class SignUpActivity extends AppCompatActivity {
    private ServerAPI serverAPI = ServerAPI.getInstance();
    private Button backButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        TextView serverTextView = findViewById(R.id.server_signUp);
        TextView usernameTextView = findViewById(R.id.username_signUp);
        TextView passwordTextView = findViewById(R.id.password_signUp);
        TextView repeatPasswordTextView = findViewById(R.id.repeatPassword_signUp);
        TextView nameTextView = findViewById(R.id.name_signUp);
        TextView lastnameTextView = findViewById(R.id.lastname_signUp);

        backButton = findViewById(R.id.back_button);
        registerButton = findViewById(R.id.doSignUp_button);

        backButton.setOnClickListener((l) -> startActivity(new Intent(this, LoginActivity.class)));
        registerButton.setOnClickListener((l) -> {
            if (serverTextView.getText().length() > 0
                && usernameTextView.getText().length() > 0
                && passwordTextView.getText().length() > 0
                && repeatPasswordTextView.getText().length() > 0
                && nameTextView.getText().length() > 0
                && lastnameTextView.getText().length() > 0) {
                if (passwordTextView.getText().toString().equals(repeatPasswordTextView.getText().toString())) {
                    User user = new User();
                    user.setUsername(usernameTextView.getText().toString());
                    user.setPassword(passwordTextView.getText().toString());
                    user.setName(nameTextView.getText().toString());
                    user.setLastname(lastnameTextView.getText().toString());
                    serverAPI.register(UrlUtils.normalize(serverTextView.getText().toString()), user, (r) -> {
                        if (r.isSuccessful()) {
                            startActivity(new Intent(this, LoginActivity.class));
                        } else {
                            Toast.makeText(this, "Request error: " + r.getBody(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(this, "Password isn't equal!", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Required field must be entered!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
