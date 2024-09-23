package finki.nichk.screens.parent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import finki.nichk.R;

public class ParentActivity extends AppCompatActivity {

    private static final String PREF_AUTH = "authenticated";
    private boolean authenticated = false;

    private EditText passwordEditText;
    private ImageView eyeIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_login);

        // Ensure the correct IDs are used
        passwordEditText = findViewById(R.id.passField);
        eyeIcon = findViewById(R.id.PassVisibility);

        eyeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });

        // Load authenticated status from SharedPreferences
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        authenticated = sharedPreferences.getBoolean(PREF_AUTH, false);
//
//        if (!authenticated) {
//            ParentAuthenticationFragment dialog = new ParentAuthenticationFragment();
//            dialog.show(getSupportFragmentManager(), "AuthenticationFragment");
//        }
    }

    // Method to update the authenticated status
    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
        // Save the authenticated status to SharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_AUTH, authenticated);
        editor.apply();
    }

    public void togglePasswordVisibility() {
        if (passwordEditText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            // Show password
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT);
            eyeIcon.setImageResource(R.drawable.eye_open); // Change to closed eye icon
        } else {
            // Hide password
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            eyeIcon.setImageResource(R.drawable.close_eye); // Change to open eye icon
        }
        passwordEditText.setSelection(passwordEditText.getText().length()); // Move cursor to end
    }
}
