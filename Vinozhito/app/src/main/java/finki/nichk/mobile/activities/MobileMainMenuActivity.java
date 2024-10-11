package finki.nichk.mobile.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import finki.nichk.R;
import finki.nichk.mobile.activities.child.ChooseChildActivity;
import finki.nichk.tablet.screens.MainMenuActivity;
import finki.nichk.tablet.screens.child.ChildMobileActivity;
import finki.nichk.tablet.screens.parent.ParentActivity;

public class MobileMainMenuActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_main_menu);

        ImageButton parentButton = findViewById(R.id.parent_btn);
        ImageButton childButton = findViewById(R.id.child_btn);

        // Initialize MediaPlayer
        mediaPlayer = MediaPlayer.create(this, R.raw.btnclick); // Replace R.raw.your_sound_file with your sound file

        // PARENTS
        buttonTouchListener(parentButton, () -> {
            // Play sound
            playSound();
            Intent intent = new Intent(MobileMainMenuActivity.this, ParentActivity.class);
            startActivity(intent);
        });

        // CHILDREN
        buttonTouchListener(childButton, () -> {
            // Play sound
            playSound();
            Intent intent = new Intent(MobileMainMenuActivity.this, ChooseChildActivity.class);
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


    private void playSound() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}
