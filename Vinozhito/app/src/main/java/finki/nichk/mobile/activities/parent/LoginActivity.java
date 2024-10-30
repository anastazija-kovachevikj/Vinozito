package finki.nichk.mobile.activities.parent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import finki.nichk.R;
import finki.nichk.services.authentication.AuthRepository;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_login);

        // Initialize UI components
        usernameEditText = findViewById(R.id.username_input);
        passwordEditText = findViewById(R.id.passField);
        Button loginButton = findViewById(R.id.login_button);

        // Initialize AuthRepository
        authRepository = new AuthRepository(this);

        // Set click listener on the login button
        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            login(username, password);
        });
    }

    private void login(String username, String password) {
        authRepository.login(username, password, new AuthRepository.LoginCallback() {
            @Override
            public void onSuccess(String token) {
                // Handle success
                Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                // Save token, navigate to another activity, etc.
            }

            @Override
            public void onFailure(String error) {
                // Handle failure
                Toast.makeText(LoginActivity.this, "Login Failed: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
