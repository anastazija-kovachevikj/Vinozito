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

    private MediaPlayer musicPlayer;

    private MediaPlayer popSoundPlayer;
    private Vibrator vibrator;
    private final String[] musicOptions = {"Без музика", "Весело", "Мирно", "Шум"};
    private ImageButton muteButton;
    private ImageButton vibeButton;
    private boolean isMuted = false;
    private boolean isVibrating = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.motion);

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
        };

        ImageButton backButton = findViewById(R.id.back_button_bubbles);

        // Set up listeners for each bubble
        for (ImageButton bubble : bubbleButtons) {
            bubble.setOnClickListener(v -> handleBubbleClick(bubble));
        }

        buttonTouchListener(backButton, () -> {
            Intent intent = new Intent(BubbleActivity.this, ChooseChildActivity.class);
            if (musicPlayer != null) {
                musicPlayer.release();
                musicPlayer = null;
            }
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

//        muteButton = findViewById(R.id.mute_button);
//        muteButton.setOnClickListener(v -> toggleMute());

        vibeButton = findViewById(R.id.vibe_button);
        vibeButton.setOnClickListener(v -> toggleVibe());
    }

    private void playBackgroundMusic(String music) {
        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer.release();
            musicPlayer = null;
        }

        switch (music) {
            case "Весело":
                musicPlayer = MediaPlayer.create(this, R.raw.veselo);
                break;
            case "Мирно":
                musicPlayer = MediaPlayer.create(this, R.raw.calm);
                break;
            case "Шум":
                musicPlayer = MediaPlayer.create(this, R.raw.whitenoise);
                break;
            default:
                return;
        }

        if (!isMuted && musicPlayer != null) {
            musicPlayer.setLooping(true);
            musicPlayer.start();
        }

        if (musicPlayer != null) {
            musicPlayer.setOnCompletionListener(mp -> {
                mp.release();
                musicPlayer = null;
            });
        }
    }

//    private void toggleMute() {
//        isMuted = !isMuted;
//        if (isMuted) {
//            muteButton.setImageResource(R.drawable.mute);
//            if (musicPlayer != null) {
//                musicPlayer.pause();
//            }
//        } else {
//            muteButton.setImageResource(R.drawable.unmute);
//            if (musicPlayer != null) {
//                musicPlayer.start();
//            }
//        }
//    }

    private void toggleVibe() {
        isVibrating = !isVibrating;
        if (isVibrating) {
            vibeButton.setImageResource(vibe_on);
        } else {
            vibeButton.setImageResource(vibe_off);
        }
    }

    private void applySwayAnimation(ImageButton bubble, Random random) {
        Animation swayAnimation = AnimationUtils.loadAnimation(this, R.anim.sway);
        int delay = random.nextInt(1000);
        swayAnimation.setStartOffset(delay);
        bubble.startAnimation(swayAnimation);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void buttonTouchListener(ImageButton button, Runnable onClickAction) {
        button.setOnClickListener(v -> onClickAction.run());
        button.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.setAlpha(0.5f);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.setAlpha(1.0f);
                    break;
            }
            return false;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (musicPlayer != null) {
            musicPlayer.release();
            musicPlayer = null;
        }
        if (popSoundPlayer != null) {
            popSoundPlayer.release();
            popSoundPlayer = null;
        }
    }

    private void vibrateOnClick() {
        if (isVibrating && vibrator != null && vibrator.hasVibrator()) {
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

        if (bubbleButton.getBackground() instanceof AnimationDrawable) {
            AnimationDrawable bubbleAnimation = (AnimationDrawable) bubbleButton.getBackground();
            bubbleAnimation.stop();
            bubbleAnimation.selectDrawable(0);
            bubbleAnimation.start();

            int totalDuration = 0;
            for (int i = 0; i < bubbleAnimation.getNumberOfFrames(); i++) {
                totalDuration += bubbleAnimation.getDuration(i);
            }

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                bubbleButton.setVisibility(View.INVISIBLE);
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
        if (popSoundPlayer != null) {
            popSoundPlayer.release();
        }
        popSoundPlayer = MediaPlayer.create(this, R.raw.pop);
        popSoundPlayer.start();

        popSoundPlayer.setOnCompletionListener(mp -> {
            mp.release();
            popSoundPlayer = null;
        });
    }
}
