package finki.nichk.tablet.screens.parent;

import android.content.Intent;
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
import finki.nichk.mobile.activities.MobileMainMenuActivity;
import finki.nichk.mobile.activities.child.ChooseChildActivity;
import finki.nichk.mobile.activities.parent.ParentProfileActivity;
import finki.nichk.services.authentication.AuthRepository;
import finki.nichk.services.authentication.TokenManager;

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
        setContentView(R.layout.mobile_register);
        isLoginLayout = false;

        usernameEditText = findViewById(R.id.UsernameField);
        passwordEditText = findViewById(R.id.passField);
        emailEditText = findViewById(R.id.passFieldRepeat);
        Button registerButton = findViewById(R.id.register_button);
        ImageButton loginSwitchButton = findViewById(R.id.cancel_register);

        registerButton.setOnClickListener(v -> register());
        loginSwitchButton.setOnClickListener(v -> setLoginLayout());
    }

    private void login() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        authRepository.login(username, password, new AuthRepository.LoginCallback() {
            @Override
            public void onSuccess(String token) {
                // Initialize TokenManager and save token and user details
                TokenManager tokenManager = new TokenManager(ParentActivity.this);
                tokenManager.saveToken(token);
                tokenManager.saveUserDetails(username, "user_email@example.com"); // Replace with actual email if available

                // Set user as authenticated and proceed to the profile activity
                setAuthenticated(true);
                Toast.makeText(ParentActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ParentActivity.this, ParentProfileActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(ParentActivity.this, "Login Failed: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void register() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String email = emailEditText.getText().toString(); // Get email input

        authRepository.register(username, password, email, new AuthRepository.RegisterCallback() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(ParentActivity.this, message, Toast.LENGTH_SHORT).show();
                setLoginLayout();
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
