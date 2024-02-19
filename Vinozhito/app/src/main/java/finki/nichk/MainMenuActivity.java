package finki.nichk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        ImageButton parentButton = findViewById(R.id.parent_btn);
        ImageButton childButton = findViewById(R.id.child_btn);

        //View parentCircle = findViewById(R.id.parent_circle);

        parentButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenuActivity.this, ParentAvtivity.class);
            startActivity(intent);
            //parentButton.setAlpha(0.5f);
            //parentCircle.setBackgroundResource(R.drawable.circle_curves);
        });


        parentButton.setOnTouchListener((v, event) -> {
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

        childButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenuActivity.this, ParentAvtivity.class);
            startActivity(intent);
        });


        childButton.setOnTouchListener((v, event) -> {
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
