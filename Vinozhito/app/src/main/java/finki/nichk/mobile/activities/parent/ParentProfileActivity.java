package finki.nichk.mobile.activities.parent;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import finki.nichk.R;
import finki.nichk.services.authentication.TokenManager;

public class ParentProfileActivity extends AppCompatActivity {
    TextView username;
    TextView email;

    TokenManager tokenManager;
    Button mycards;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_view);

        tokenManager = new TokenManager(this);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);

        username.setText(tokenManager.getUsername());
        email.setText(tokenManager.getEmail());


    }


}
