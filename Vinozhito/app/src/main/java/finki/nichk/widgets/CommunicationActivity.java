package finki.nichk.widgets;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.media.MediaPlayer;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import finki.nichk.R;
import finki.nichk.models.Card;
import finki.nichk.screens.child.ChildActivity;
import finki.nichk.services.CardService;

public class CommunicationActivity extends AppCompatActivity {
    private GridLayout cardLayout;
    private CardService cardService;

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
        cardService = new CardService();

        initializeViews();
        initializeListeners();
        //playCardSound("fruit_banana"); // Replace with an actual audio file name you have

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
                setCardListener(cardButton, String.valueOf(cardView));
            }
        }
    }

    private void initializeListeners() {

        cardLayout = findViewById(R.id.card_layout);

        // Category TABs
        ImageButton conversationTab = findViewById(R.id.conversation_tab);
        ImageButton feelingsTab = findViewById(R.id.feelings_tab);
        ImageButton peopleTab = findViewById(R.id.people_tab);
        ImageButton drinksTab = findViewById(R.id.drinks_tab);
        ImageButton foodTab = findViewById(R.id.food_tab);
        ImageButton vegetablesTab = findViewById(R.id.vegetables_tab);
        ImageButton fruitTab = findViewById(R.id.fruit_tab);
        ImageButton cutleryTab = findViewById(R.id.cutlery_tab);
        ImageButton toysTab = findViewById(R.id.toys_tab);
        ImageButton activitiesTab = findViewById(R.id.activities_tab);
        ImageButton animalsTab = findViewById(R.id.animals_tab);
        ImageButton clothesTab = findViewById(R.id.clothes_tab);
        ImageButton colorsTab = findViewById(R.id.colors_tab);

        conversationTab.setOnClickListener(view -> updateCardLayoutByCategory("conversation"));
        feelingsTab.setOnClickListener(view -> updateCardLayoutByCategory("feelings"));
        peopleTab.setOnClickListener(view -> updateCardLayoutByCategory("people"));
        drinksTab.setOnClickListener(view -> updateCardLayoutByCategory("drinks"));
        foodTab.setOnClickListener(view -> updateCardLayoutByCategory("food"));
        vegetablesTab.setOnClickListener(view -> updateCardLayoutByCategory("Vegetable"));
        fruitTab.setOnClickListener(view -> updateCardLayoutByCategory("Fruit"));
        cutleryTab.setOnClickListener(view -> updateCardLayoutByCategory("cutlery"));
        toysTab.setOnClickListener(view -> updateCardLayoutByCategory("toys"));
        activitiesTab.setOnClickListener(view -> updateCardLayoutByCategory("activities"));
        animalsTab.setOnClickListener(view -> updateCardLayoutByCategory("animals"));
        clothesTab.setOnClickListener(view -> updateCardLayoutByCategory("clothes"));
        colorsTab.setOnClickListener(view -> updateCardLayoutByCategory("colors"));

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

    private void updateCardLayoutByCategory(String category) {
        cardService.fetchCardDataByUserIdAndCategory("64f76d45-f03a-4c9e-9339-4c01524fb08a", "Fruit", new CardService.CardServiceCallback() {
            @Override
            public void onCardsFetched(List<Card> cards) {
                cardLayout.removeAllViews();
                cardLayout.setColumnCount(4);

                int position = 0;
                for (Card card : cards) {

                    String cardName = card.getName();
                    String audioVoice = card.getAudioVoice();
                    String imageLink = card.getImage();

                    // Inflate the card layout
                    View cardView = LayoutInflater.from(CommunicationActivity.this).inflate(R.layout.card_layout, cardLayout, false);

                    // Find the ImageButton and TextView in the card layout
                    ImageButton imageButton = cardView.findViewById(R.id.card_image);
                    TextView textView = cardView.findViewById(R.id.card_text);

                    textView.setText(cardName);
//                    Glide.with(CommunicationActivity.this)
//                            .load(imageLink)
//                            .apply(new RequestOptions().placeholder(R.drawable.unknown)) // Show default image while loading
//                            .into(imageButton);


                    // Create layout parameters for the GridLayout
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    params.width = 0;
                    params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                    params.columnSpec = GridLayout.spec(position % 4, 1f); // Place item in correct column
                    params.rowSpec = GridLayout.spec(position / 4);        // Place item in correct row
                    params.setMargins(15, 15, 15, 15);              // Add some margin around each card

                    // Set the layout parameters and add the card view to the GridLayout
                    cardView.setLayoutParams(params);
                    cardLayout.addView(cardView);

                    // Set listener for the card button, passing the imageName as well
                    //setCardListener(imageButton, imageName);

                    position++;

                }
            }

            @Override
            public void onError(Exception e) {
                // Handle the error
                Log.e("CardService", "Error fetching cards", e);
            }
        });

    }


    @SuppressLint("ClickableViewAccessibility")
    private void setCardListener(ImageButton cardButton, String imageName) {
        cardButton.setOnClickListener(view -> {
            if (count < SLOT_COUNT) {
                setSlotImage(cardButton.getDrawable(), imageName);
                playCardSound(imageName);
                Log.d("CardClick", "Image clicked: " + imageName);
                System.out.println(imageName);

            }
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
                playCardSound(String.valueOf(cardDrawableIds[slotIndex]));
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

    private void setSlotImage(Drawable drawable, String imageName) {
        if (count < SLOT_COUNT) {
            ImageButton slot = cardSlots[count];
            slot.setImageDrawable(drawable);
            slot.setVisibility(View.VISIBLE);
            cardDrawableIds[count] = getResources().getIdentifier(imageName, "raw", getPackageName());
            Log.d("CardSlots", "Slot " + count + " updated with image: " + imageName);
            count++;
        } else {
            Log.w("CardSlotsFULL", "All slots are full. Cannot add more cards.");
        }
    }


    private void playCardSound(String imageName) {
        Log.d("CardSound", "Attempting to play sound for: " + imageName);

        int soundResId = getResources().getIdentifier(imageName, "raw", getPackageName());

        if (soundResId != 0) {
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }

            mediaPlayer = MediaPlayer.create(this, soundResId);
            if (mediaPlayer != null) {
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(mp -> {
                    mp.release();
                    mediaPlayer = null;
                });
            } else {
                Log.e("CardSound", "Failed to create MediaPlayer for: " + imageName);
            }
        } else {
            Log.e("CardSound", "Sound resource not found for: " + imageName);
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
                        playCardSound(String.valueOf(cardDrawableIds[index]));
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
