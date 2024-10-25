package finki.nichk.mobile.activities.child;

import static finki.nichk.R.drawable.*;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import finki.nichk.R;
import finki.nichk.mobile.activities.MobileMainMenuActivity;

public class BubbleActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.motion);

        ImageButton[] bubbleButtons = {
                findViewById(R.id.bubbleButton), findViewById(R.id.bubbleButton2),
                findViewById(R.id.bubbleButton3), findViewById(R.id.bubbleButton4),
                findViewById(R.id.bubbleButton5), findViewById(R.id.bubbleButton6),
                findViewById(R.id.bubbleButton7)
        };

        ImageButton backButton = findViewById(R.id.back_button_bubbles);

        // listeners for each bubble
        for (ImageButton bubble : bubbleButtons) {
            bubble.setOnClickListener(v -> handleBubbleClick(bubble));
        }

        buttonTouchListener(backButton, () -> {
            Intent intent = new Intent(BubbleActivity.this, ChooseChildActivity.class);
            startActivity(intent);
        });

        Random random = new Random();

        for (ImageButton bubble : bubbleButtons) {
            applySwayAnimation(bubble, random);
        }
    }

    private void applySwayAnimation(ImageButton bubble, Random random) {
        Animation swayAnimation = AnimationUtils.loadAnimation(this, R.anim.sway);
        int delay = random.nextInt(1000);  // random delay
        swayAnimation.setStartOffset(delay);
        bubble.startAnimation(swayAnimation);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void buttonTouchListener(ImageButton button, Runnable onClickAction) {
        button.setOnClickListener(v -> onClickAction.run());

        button.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.setAlpha(0.5f); // transparent when pressed
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.setAlpha(1.0f); // back to normal released or canceled
                    break;
            }
            return false;
        });
    }


//    private void playSound() {
//        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
//            mediaPlayer.start();
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void handleBubbleClick(ImageButton bubbleButton) {

        playPopSound();

        if (bubbleButton.getBackground() instanceof AnimationDrawable) {
            AnimationDrawable bubbleAnimation = (AnimationDrawable) bubbleButton.getBackground();
            bubbleAnimation.stop();
            bubbleAnimation.selectDrawable(0);  // first frame
            bubbleAnimation.start();  // start the frame animation again

            int totalDuration = 0;
            for (int i = 0; i < bubbleAnimation.getNumberOfFrames(); i++) {
                totalDuration += bubbleAnimation.getDuration(i);
            }

            // hide the button after the animation ends
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                bubbleButton.setVisibility(View.INVISIBLE);

                // button reappear after 5 seconds
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    bubbleAnimation.stop();
                    bubbleAnimation.selectDrawable(0);

                    bubbleButton.setBackground(bubbleAnimation);

                    bubbleButton.setVisibility(View.VISIBLE);

                }, 2000);
            }, totalDuration - 100);
        }
    }

    private void playPopSound() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.pop);
        mediaPlayer.start();

        mediaPlayer.setOnCompletionListener(mp -> {
            mp.release();
            mediaPlayer = null;
        });
    }
}
