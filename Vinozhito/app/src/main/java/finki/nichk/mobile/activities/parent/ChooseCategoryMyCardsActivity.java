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

public class ChooseCategoryMyCardsActivity extends AppCompatActivity {

    private ImageButton backButton;
    TextView username;
    TextView email;

    TokenManager tokenManager;
    Button mycards;
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_parent_cards);

        tokenManager = new TokenManager(this);



        backButton=findViewById(R.id.back_button_login);

        buttonTouchListener(backButton, () -> {
            Intent intent = new Intent(ChooseCategoryMyCardsActivity.this, ParentProfileActivity.class);
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

}
