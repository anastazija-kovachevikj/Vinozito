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
import finki.nichk.mobile.activities.parent.ParentProfileActivity;
import finki.nichk.services.authentication.TokenManager;
import finki.nichk.tablet.screens.MainMenuActivity;
import finki.nichk.tablet.screens.child.ChildMobileActivity;
import finki.nichk.tablet.screens.parent.ParentActivity;

public class MobileMainMenuActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_main_menu);

        tokenManager = new TokenManager(this);

        ImageButton parentButton = findViewById(R.id.parent_btn);
        ImageButton childButton = findViewById(R.id.child_btn);

        mediaPlayer = MediaPlayer.create(this, R.raw.btnclick);

        // PARENTS
        buttonTouchListener(parentButton, () -> {
            playSound();
            if(tokenManager.getToken()!=null)
            {
                Intent intent = new Intent(MobileMainMenuActivity.this, ParentProfileActivity.class);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(MobileMainMenuActivity.this, ParentActivity.class);
                startActivity(intent);
            }
        });

        // CHILDREN
        buttonTouchListener(childButton, () -> {
            playSound();
            Intent intent = new Intent(MobileMainMenuActivity.this, ChooseChildActivity.class);
            startActivity(intent);
        });

//        int[] imageResources = {
//                R.drawable.worm,
//                R.drawable.rocket,
//                R.drawable.candy,
//                R.drawable.flower
//        };
//
//        ImageProcessor imageProcessor = new ImageProcessor(getApplicationContext());
//        imageProcessor.preprocessAndSaveImages(imageResources);

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

//    private void stopButtonClickSound() {
//        if (mediaPlayer != null) {
//            mediaPlayer.stop();
//            mediaPlayer.release();
//            mediaPlayer = null;
//        }
//    }

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
