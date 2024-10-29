package finki.nichk.mobile.activities.child;

import static finki.nichk.R.drawable.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import finki.nichk.R;

public class BubbleActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    private final String[] musicOptions = {"None", "Music 1", "Music 2", "Music 3"};
    private ImageButton muteButton;
    private ImageButton vibeButton;
    private boolean isMuted = false;
    private boolean isViberating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.motion);

        //Vibrator vibrator = getSystemService(Vibrator.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            VibratorManager vibratorManager = (VibratorManager) getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
            vibrator = vibratorManager.getDefaultVibrator();
        } else {
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        }

        ImageButton[] bubbleButtons = {
                findViewById(R.id.bubbleButton), findViewById(R.id.bubbleButton2),
                findViewById(R.id.bubbleButton3), findViewById(R.id.bubbleButton4),
                findViewById(R.id.bubbleButton5), findViewById(R.id.bubbleButton6)
//                findViewById(R.id.bubbleButton7)
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

        Spinner musicSpinner = findViewById(R.id.music_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, musicOptions);
        adapter.setDropDownViewResource(R.layout.spinner_item);

        musicSpinner.setAdapter(adapter);
        musicSpinner.setPopupBackgroundResource(spinner_logic);

        musicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedMusic = musicOptions[position];
                playBackgroundMusic(selectedMusic);
                //view.setBackgroundColor(getResources().getColor(R.color.bubbles_bckg));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        muteButton = findViewById(R.id.mute_button);
        muteButton.setOnClickListener(v -> toggleMute());

        vibeButton = findViewById(R.id.vibe_button);
        vibeButton.setOnClickListener(v -> toggleVibe());
    }


    private void playBackgroundMusic(String music) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

        switch (music) {
            case "Music 1":
                mediaPlayer = MediaPlayer.create(this, R.raw.music1); // Replace with actual music file
                break;
            case "Music 2":
                mediaPlayer = MediaPlayer.create(this, R.raw.music2); // Add actual music file
                break;
            case "Music 3":
                mediaPlayer = MediaPlayer.create(this, R.raw.music3); // Add actual music file
                break;
            default:
                return; // no music selected aka "None" selected
        }

        if (!isMuted && mediaPlayer != null) {
            mediaPlayer.setLooping(true); // enable looping
            mediaPlayer.start();
        }

        if (mediaPlayer != null) {
            mediaPlayer.setOnCompletionListener(mp -> {
                mp.release();
                mediaPlayer = null;
            });
        }
    }

    private void toggleMute() {
        isMuted = !isMuted;
        if (isMuted) {
            muteButton.setImageResource(R.drawable.mute);
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
        } else {
            muteButton.setImageResource(R.drawable.unmute);
            if (mediaPlayer != null) {
                mediaPlayer.start();
            }
        }
    }

    private void toggleVibe() {
        isViberating = !isViberating;
        if (isViberating) {
            vibeButton.setImageResource(vibe_off);

        } else {
            vibeButton.setImageResource(vibe_on);
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

    private void vibrateOnClick() {
        if (isViberating && vibrator != null && vibrator.hasVibrator()) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK));
            } else {
                vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
            }
        }
    }

    private void handleBubbleClick(ImageButton bubbleButton) {

        bubbleButton.setClickable(false);
        playPopSound();
        vibrateOnClick();
        //vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK));


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

                // button(bubble) reappear after 5 seconds
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    bubbleAnimation.stop();
                    bubbleAnimation.selectDrawable(0);

                    bubbleButton.setBackground(bubbleAnimation);

                    bubbleButton.setVisibility(View.VISIBLE);

                    bubbleButton.setClickable(true);

                }, 3000);
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
