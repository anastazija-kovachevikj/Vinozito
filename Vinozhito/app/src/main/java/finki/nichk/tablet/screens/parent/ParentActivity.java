package finki.nichk.tablet.screens.parent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import finki.nichk.R;
import finki.nichk.services.authentication.AuthRepository;

public class ParentActivity extends AppCompatActivity {

    private static final String PREF_AUTH = "authenticated";
    private boolean authenticated = false;

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText emailEditText; // Added for registration
    private ImageView eyeIcon;
    private AuthRepository authRepository;

    private boolean isLoginLayout = true; // Track the current layout

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLoginLayout();  // Start with login layout

        // Initialize AuthRepository
        authRepository = new AuthRepository(this);
    }

    private void setLoginLayout() {
        setContentView(R.layout.mobile_login);
        isLoginLayout = true;
        usernameEditText = findViewById(R.id.UsernameField);
        passwordEditText = findViewById(R.id.passField);
        eyeIcon = findViewById(R.id.PassVisibility);

        Button loginButton = findViewById(R.id.login_button);
        Button registerButton = findViewById(R.id.register_button);

        loginButton.setOnClickListener(v -> login());
        registerButton.setOnClickListener(v -> setRegisterLayout());

        eyeIcon.setOnClickListener(v -> togglePasswordVisibility());
    }

    private void setRegisterLayout() {
        setContentView(R.layout.mobile_register); // Switch to the registration layout
        isLoginLayout = false;

        usernameEditText = findViewById(R.id.UsernameField); // Ensure these IDs exist in mobile_register.xml
        passwordEditText = findViewById(R.id.passField);
        emailEditText = findViewById(R.id.passFieldRepeat); // New EditText for email input

        Button registerButton = findViewById(R.id.register_button); // Register button in registration layout
        ImageButton loginSwitchButton = findViewById(R.id.cancel_register); // Button to go back to login

        registerButton.setOnClickListener(v -> register()); // Set register button click listener
        loginSwitchButton.setOnClickListener(v -> setLoginLayout());
    }

    // Method to log in the user
    private void login() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        authRepository.login(username, password, new AuthRepository.LoginCallback() {
            @Override
            public void onSuccess(String token) {
                setAuthenticated(true);
                Toast.makeText(ParentActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(ParentActivity.this, "Login Failed: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to register the user
    private void register() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String email = emailEditText.getText().toString(); // Get email input

        authRepository.register(username, password, email, new AuthRepository.RegisterCallback() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(ParentActivity.this, message, Toast.LENGTH_SHORT).show();
                setLoginLayout(); // Optionally switch back to login layout after successful registration
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(ParentActivity.this, "Registration Failed: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void togglePasswordVisibility() {
        if (passwordEditText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT);
            eyeIcon.setImageResource(R.drawable.eye_open);
        } else {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            eyeIcon.setImageResource(R.drawable.close_eye);
        }
        passwordEditText.setSelection(passwordEditText.getText().length());
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_AUTH, authenticated);
        editor.apply();
    }
}
