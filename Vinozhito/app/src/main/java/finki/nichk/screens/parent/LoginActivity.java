package finki.nichk.screens.parent;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import finki.nichk.R;
import finki.nichk.services.authentication.AuthRepository;

public class LoginActivity extends AppCompatActivity {
    private AuthRepository authRepository;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Initialize AuthRepository with TokenManager
        authRepository = new AuthRepository(this);

        // Initialize UI components
        usernameEditText = findViewById(R.id.usernameInput);
        passwordEditText = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);

        // Set login button click listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                // Perform login
                performLogin(username, password);
            }
        });
    }

    private void performLogin(String username, String password) {
        // Call AuthRepository login method
        authRepository.login(username, password, new AuthRepository.LoginCallback() {
            @Override
            public void onSuccess(String token) {
                // Save token, proceed to next activity, or show success message
                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                // Proceed to another activity after successful login
                // Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                // startActivity(intent);
                // finish(); // Close the LoginActivity
            }

            @Override
            public void onFailure(String error) {
                // Handle login failure, show error message
                Toast.makeText(LoginActivity.this, "Login Failed: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
