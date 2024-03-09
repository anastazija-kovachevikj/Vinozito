package finki.nichk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class ChildActivity extends AppCompatActivity {
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_main_screen);

        //GridLayout bckg = findViewById(R.id.child_main);
        //Glide.with(this).asGif().load(R.drawable.c).into(bckg);

        ImageButton backButton = findViewById(R.id.back_button);
        ImageButton communicationButton = findViewById(R.id.communication_btn);
        ImageButton connectButton = findViewById(R.id.connect_btn);
        ImageButton coloringButton = findViewById(R.id.coloring_btn);

        buttonTouchListener(backButton, () -> {
            Intent intent = new Intent(ChildActivity.this, MainMenuActivity.class);
            startActivity(intent);
        });

        buttonTouchListener(communicationButton, () -> {
            Intent intent = new Intent(ChildActivity.this, CommunicationActivity.class); // CommunicationActivity
            startActivity(intent);
        });

        buttonTouchListener(connectButton, () -> {
            Intent intent = new Intent(ChildActivity.this, ConnectActivity.class); // ConnectActivity
            startActivity(intent);
        });

        buttonTouchListener(coloringButton, () -> {
            Intent intent = new Intent(ChildActivity.this, ColoringActivity.class); // ColoringActivity
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
