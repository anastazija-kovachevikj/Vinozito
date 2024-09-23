package finki.nichk.screens.child;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageButton;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import finki.nichk.R;

public class ColoringActivity extends AppCompatActivity {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coloring_main_menu);

        ImageButton imageButton1 = findViewById(R.id.imageButton_ladybug);
        ImageButton imageButton2 = findViewById(R.id.imageButton_dino);
        ImageButton imageButton3 = findViewById(R.id.imageButton_cake);
        ImageButton imageButton4 = findViewById(R.id.imageButton_butterfly);
        ImageButton imageButton5 = findViewById(R.id.imageButton_rainbow);
        ImageButton imageButton6 = findViewById(R.id.imageButton_cow);
        ImageButton imageButton7 = findViewById(R.id.imageButton_cactus);
        ImageButton imageButton8 = findViewById(R.id.imageButton_cp);
        ImageButton imageButton9 = findViewById(R.id.imageButton_ufo);
        ImageButton imageButton10 = findViewById(R.id.imageButton_horse);
        ImageButton backButton = findViewById(R.id.back_button);

        imageButton1.setOnClickListener(v -> openColoringScreen(R.drawable.bug_vector));
        imageButton2.setOnClickListener(v -> openColoringScreen(R.drawable.dino_vector));
        imageButton3.setOnClickListener(v -> openColoringScreen(R.drawable.cake_vector));
        imageButton4.setOnClickListener(v -> openColoringScreen(R.drawable.butterfly_vector));
        imageButton5.setOnClickListener(v -> openColoringScreen(R.drawable.rainbow_vector));
        imageButton6.setOnClickListener(v -> openColoringScreen(R.drawable.cow_vector));
        imageButton7.setOnClickListener(v -> openColoringScreen(R.drawable.cactus));  // TREBA DA SE SMENI
        imageButton8.setOnClickListener(v -> openColoringScreen(R.drawable.cp_vector));
        imageButton9.setOnClickListener(v -> openColoringScreen(R.drawable.ufo_vector));
        imageButton10.setOnClickListener(v -> openColoringScreen(R.drawable.horse_vector));


        buttonTouchListener(backButton, () -> {
            Intent intent = new Intent(ColoringActivity.this, ChildActivity.class);
            startActivity(intent);
        });
    }

    private void openColoringScreen(int drawableId) {
        Intent intent = new Intent(ColoringActivity.this, ColoringScreenActivity.class);
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