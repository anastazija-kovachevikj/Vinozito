package finki.nichk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.media.MediaPlayer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;

public class CommunicationActivity extends AppCompatActivity {

    private static final int SLOT_COUNT = 3;
    private static final int SOUND_DELAY_MILLIS = 1000;

    private int count = 0; // Track the number of occupied slots
    private final ImageButton[] cardSlots = new ImageButton[SLOT_COUNT];
    private final int[] cardDrawableIds = new int[SLOT_COUNT];

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.communication_cat);

        initializeViews();
        initializeListeners();
    }

    private void initializeViews() {
        // Initialize slot buttons
        cardSlots[0] = findViewById(R.id.cardslot1);
        cardSlots[1] = findViewById(R.id.cardslot2);
        cardSlots[2] = findViewById(R.id.cardslot3);

        // Initialize card layout and slots
        GridLayout cardLayout = findViewById(R.id.card_layout);
        for (int i = 0; i < cardLayout.getChildCount(); i++) {
            View cardView = cardLayout.getChildAt(i);
            if (cardView instanceof FrameLayout) {
                FrameLayout frameLayout = (FrameLayout) cardView;
                ImageButton cardButton = (ImageButton) frameLayout.getChildAt(0);
                setCardListener(cardButton);
            }
        }
    }

    private void initializeListeners() {
        // Initialize buttons
        ImageButton backButton = findViewById(R.id.back_button);
        ImageButton clearButton = findViewById(R.id.clearAll);
        ImageButton readAll = findViewById(R.id.read_all);

        // Set listeners
        setButtonTouchListener(backButton, this::navigateToChildActivity);
        setButtonTouchListener(clearButton, this::clearAllSlots);
        setButtonTouchListener(readAll, this::playAllCardSounds);

        // Set click listeners for card slots
        for (ImageButton slot : cardSlots) {
            setSlotClickListener(slot);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setCardListener(ImageButton cardButton) {
        cardButton.setOnClickListener(view -> {
            Drawable cardDrawable = cardButton.getDrawable();
            int cardDrawableId = cardButton.getId();
            if (count < SLOT_COUNT) {
                setSlotImage(cardDrawable, cardDrawableId);
            }
            playCardSound(cardDrawableId);
        });

        cardButton.setOnTouchListener((v, event) -> {
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

    @SuppressLint("ClickableViewAccessibility")
    private void setSlotClickListener(ImageButton slot) {
        slot.setOnClickListener(view -> {
            int slotIndex = getSlotIndex(slot);
            if (slotIndex != -1) {
                playCardSound(cardDrawableIds[slotIndex]);
            }
        });

        slot.setOnTouchListener((v, event) -> {
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

    private void setSlotImage(Drawable drawable, int drawableId) {
        if (count < SLOT_COUNT) {
            ImageButton slot = cardSlots[count];
            slot.setImageDrawable(drawable);
            slot.setVisibility(View.VISIBLE);
            cardDrawableIds[count] = drawableId;
            count++;
        }
    }

    private void playCardSound(int cardId) {
        String resourceName = getResources().getResourceEntryName(cardId);
        int soundResId = getResources().getIdentifier(resourceName, "raw", getPackageName());

        if (soundResId != 0) {
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }

            mediaPlayer = MediaPlayer.create(this, soundResId);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(mp -> {
                mp.release();
                mediaPlayer = null;
            });
        } else {
            Log.e("CardSound", "Sound resource not found for: " + resourceName);
        }
    }

    private void playAllCardSounds() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable playSoundTask = new Runnable() {
            int index = 0;

            @Override
            public void run() {
                if (index < SLOT_COUNT) {
                    if (cardSlots[index].getVisibility() == View.VISIBLE) {
                        applyTouchFeedback(cardSlots[index]);
                        playCardSound(cardDrawableIds[index]);
                    }
                    index++;
                    scheduler.schedule(this, SOUND_DELAY_MILLIS, TimeUnit.MILLISECONDS);
                } else {
                    scheduler.shutdown();
                }
            }
        };
        playSoundTask.run();
    }

    private void applyTouchFeedback(final ImageButton slot) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            runOnUiThread(() -> slot.setAlpha(0.5f));
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> slot.setAlpha(1.0f));
        });
        executor.shutdown();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setButtonTouchListener(ImageButton button, Runnable onClickAction) {
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

    private void navigateToChildActivity() {
        Intent intent = new Intent(CommunicationActivity.this, ChildActivity.class);
        startActivity(intent);
    }

    private void clearAllSlots() {
        for (ImageButton slot : cardSlots) {
            slot.setVisibility(View.INVISIBLE);
        }
        count = 0; // Reset slot counter
    }

    private int getSlotIndex(ImageButton slot) {
        for (int i = 0; i < cardSlots.length; i++) {
            if (cardSlots[i] == slot) {
                return i;
            }
        }
        return -1; // Slot not found
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
