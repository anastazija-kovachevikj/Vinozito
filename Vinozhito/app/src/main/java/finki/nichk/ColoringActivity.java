package finki.nichk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class ColoringActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coloring_main_menu); // to be updated to coloring_layout

        ImageButton imageButton1 = findViewById(R.id.imageView1);
        ImageButton imageButton2 = findViewById(R.id.imageView2);
        ImageButton backButton = findViewById(R.id.back_button);

        imageButton1.setOnClickListener(v -> openColoringScreen(R.drawable.ladybug));
        imageButton2.setOnClickListener(v -> openColoringScreen(R.drawable.dino));

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