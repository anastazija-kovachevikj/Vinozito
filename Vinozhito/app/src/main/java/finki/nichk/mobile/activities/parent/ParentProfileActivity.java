package finki.nichk.mobile.activities.parent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import finki.nichk.R;
import finki.nichk.mobile.activities.MobileMainMenuActivity;
import finki.nichk.mobile.activities.child.ChooseChildActivity;
import finki.nichk.services.authentication.TokenManager;
import finki.nichk.tablet.screens.parent.ParentActivity;

public class ParentProfileActivity extends AppCompatActivity {

    private ImageButton backButton;
    private Button logout;
    private Button myCards;
    TextView username;
    TextView email;

    TokenManager tokenManager;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_parent_view);

        tokenManager = new TokenManager(this);

        username = findViewById(R.id.username);
//        email = findViewById(R.id.email);

        username.setText(tokenManager.getUsername());
        //email.setText(tokenManager.getEmail());

        backButton=findViewById(R.id.back_button_login);
        myCards=findViewById(R.id.mycards_button);
        logout=findViewById(R.id.logout_button);

        buttonTouchListener(backButton, () -> {
            Intent intent = new Intent(ParentProfileActivity.this, MobileMainMenuActivity.class);
            startActivity(intent);
        });

        buttonTouchListener(myCards, () -> {
            Intent intent = new Intent(ParentProfileActivity.this, ChooseCategoryMyCardsActivity.class);
            startActivity(intent);
        });
        buttonTouchListener(logout, () -> {
            Intent intent = new Intent(ParentProfileActivity.this, ParentActivity.class);
            tokenManager.clearToken();
            startActivity(intent);
        });


    }

    @SuppressLint("ClickableViewAccessibility")
    private void buttonTouchListener(ImageButton button, Runnable onClickAction) {
        button.setOnClickListener(v -> onClickAction.run());

        button.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.setAlpha(0.5f);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.setAlpha(1.0f);
                    break;
            }
            return false;
        });
    }
    @SuppressLint("ClickableViewAccessibility")
    private void buttonTouchListener(Button button, Runnable onClickAction) {
        button.setOnClickListener(v -> onClickAction.run());

        button.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.setAlpha(0.5f);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.setAlpha(1.0f);
                    break;
            }
            return false;
        });
    }


}
