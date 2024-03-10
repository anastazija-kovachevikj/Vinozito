package finki.nichk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class CommunicationActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.communication_cat); // to be updated to communication_layout

        ImageButton backButton = findViewById(R.id.back_button);
        ImageButton banana = findViewById(R.id.banana);
        ImageButton cardslot1 = findViewById(R.id.cardslot1);

        buttonTouchListener(backButton, () -> {
            Intent intent = new Intent(CommunicationActivity.this, ChildActivity.class);
            startActivity(intent);
        });

        // Set listener for the banana button
        // Set listener for the banana button
        buttonTouchListener(banana, () -> {
            try {
                // Get the drawable of the banana button
                Drawable bananaDrawable = banana.getDrawable();

                // Set the drawable of cardslot1 to the drawable of banana
                cardslot1.setImageDrawable(bananaDrawable);
                cardslot1.setVisibility(View.VISIBLE);
                Log.d("BananaButton", "Image changed for cardslot1");
            } catch (NullPointerException e) {
                // Handle null drawable
                e.printStackTrace();
                Log.e("BananaButton", "Error: Drawable is null");
            }
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
