package finki.nichk.mobile.activities.child;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import finki.nichk.R;
import finki.nichk.mobile.activities.MobileMainMenuActivity;
import finki.nichk.tablet.screens.child.ChildActivity;
import finki.nichk.tablet.screens.child.ColoringActivity;
import finki.nichk.tablet.screens.child.ColoringScreenActivity;
import finki.nichk.tablet.screens.parent.ParentActivity;

public class ChoosePaintingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_coloring_menu);

        ImageButton imageButton1 = findViewById(R.id.imageButton_ladybug);
//        ImageButton imageButton2 = findViewById(R.id.imageButton_dino);
        ImageButton imageButton3 = findViewById(R.id.imageButton_cake);
        ImageButton imageButton4 = findViewById(R.id.imageButton_butterfly);
        ImageButton imageButton5 = findViewById(R.id.imageButton_rainbow);
        ImageButton imageButton8 = findViewById(R.id.imageButton_rainbow2);
        ImageButton imageButton6 = findViewById(R.id.imageButton_cow);
        ImageButton imageButton7 = findViewById(R.id.imageButton_cow2);
        ImageButton imageButton10 = findViewById(R.id.imageButton_horse);
        ImageButton backButton = findViewById(R.id.back_button);

        imageButton1.setOnClickListener(v -> openColoringScreen(R.drawable.ladybug));
//        imageButton2.setOnClickListener(v -> openColoringScreen(R.drawable.dino));
        imageButton3.setOnClickListener(v -> openColoringScreen(R.drawable.cake));
        imageButton4.setOnClickListener(v -> openColoringScreen(R.drawable.butterfly));
        imageButton5.setOnClickListener(v -> openColoringScreen(R.drawable.rainbow));
        imageButton5.setOnClickListener(v -> openColoringScreen(R.drawable.rainbow));
        imageButton8.setOnClickListener(v -> openColoringScreen(R.drawable.cow));
        imageButton7.setOnClickListener(v -> openColoringScreen(R.drawable.cow));
        imageButton10.setOnClickListener(v -> openColoringScreen(R.drawable.horse));

        buttonTouchListener(backButton, () -> {
            Intent intent = new Intent(ChoosePaintingActivity.this, ChooseChildActivity.class);
            startActivity(intent);
        });
    }

    private void openColoringScreen(int drawableId) {
        Intent intent = new Intent(ChoosePaintingActivity.this, ColorPaintingActivity.class);
        intent.putExtra("image_resource", drawableId);
        startActivity(intent);
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
