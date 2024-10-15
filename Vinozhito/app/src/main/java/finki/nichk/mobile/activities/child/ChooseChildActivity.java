package finki.nichk.mobile.activities.child;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import finki.nichk.R;
import finki.nichk.mobile.activities.MobileMainMenuActivity;
import finki.nichk.tablet.screens.MainMenuActivity;
import finki.nichk.tablet.screens.child.ChildActivity;
import finki.nichk.tablet.screens.child.ColoringActivity;
import finki.nichk.mobile.activities.child.ConnectingActivity;
import finki.nichk.tablet.widgets.CommunicationActivity;

public class ChooseChildActivity extends AppCompatActivity {
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_child_menu);

        //GridLayout bckg = findViewById(R.id.child_main);
        //Glide.with(this).asGif().load(R.drawable.c).into(bckg);

        ImageButton backButton = findViewById(R.id.back_button);
        ImageButton communicationButton = findViewById(R.id.btn_govor);
        ImageButton connectButton = findViewById(R.id.btn_povrzuvanje);
        ImageButton coloringButton = findViewById(R.id.btn_boenje);
        ImageButton relaxButton = findViewById(R.id.btn_odmor);

        buttonTouchListener(backButton, () -> {
            Intent intent = new Intent(ChooseChildActivity.this, MobileMainMenuActivity.class);
            startActivity(intent);
        });

        buttonTouchListener(communicationButton, () -> {
            Intent intent = new Intent(ChooseChildActivity.this, CommunicationActivity.class); // CommunicationActivity
            startActivity(intent);
        });

        buttonTouchListener(connectButton, () -> {
            Intent intent = new Intent(ChooseChildActivity.this, ConnectingActivity.class); // ConnectActivity
            startActivity(intent);
        });

        buttonTouchListener(coloringButton, () -> {
            Intent intent = new Intent(ChooseChildActivity.this, ChoosePaintingActivity.class); // ColoringActivity
            startActivity(intent);
        });

    }


    @SuppressLint("ClickableViewAccessibility")
    private void buttonTouchListener(ImageButton button, Runnable onClickAction) {
        button.setOnClickListener(v -> onClickAction.run());

        button.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // transparent when pressed
                    v.setAlpha(0.5f);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    // back to normal released or canceled
                    v.setAlpha(1.0f);
                    break;
            }
            return false;
        });
    }

}
