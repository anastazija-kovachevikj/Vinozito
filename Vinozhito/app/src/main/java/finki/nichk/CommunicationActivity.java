package finki.nichk;

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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;

public class CommunicationActivity extends AppCompatActivity {
    private GridLayout cardLayout;
    private static final int SLOT_COUNT = 4;
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
        //playCardSound("fruit_banana"); // Replace with an actual audio file name you have

    }

    private void initializeViews() {
        // Initialize slot buttons
        cardSlots[0] = findViewById(R.id.cardslot1);
        cardSlots[1] = findViewById(R.id.cardslot2);
        cardSlots[2] = findViewById(R.id.cardslot3);
        cardSlots[3] = findViewById(R.id.cardslot4);

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

        conversationTab.setOnClickListener(view -> updateGridLayout("conversation"));
        feelingsTab.setOnClickListener(view -> updateGridLayout("feelings"));
        peopleTab.setOnClickListener(view -> updateGridLayout("people"));
        drinksTab.setOnClickListener(view -> updateGridLayout("drinks"));
        foodTab.setOnClickListener(view -> updateGridLayout("food"));
        vegetablesTab.setOnClickListener(view -> updateGridLayout("vegetables"));
        fruitTab.setOnClickListener(view -> updateGridLayout("fruit"));
        cutleryTab.setOnClickListener(view -> updateGridLayout("cutlery"));
        toysTab.setOnClickListener(view -> updateGridLayout("toys"));
        activitiesTab.setOnClickListener(view -> updateGridLayout("activities"));
        animalsTab.setOnClickListener(view -> updateGridLayout("animals"));
        clothesTab.setOnClickListener(view -> updateGridLayout("clothes"));
        colorsTab.setOnClickListener(view -> updateGridLayout("colors"));

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

    private void updateGridLayout(String category) {
        // Clear existing views
        cardLayout.removeAllViews();

        // Set the column count for the GridLayout
        cardLayout.setColumnCount(4); // 4 columns

        // Get the image names for the selected category
        String[] imageNames = getImageNamesForCategory(category);

        // Inflate the card layout and add it to the GridLayout
        for (int i = 0; i < imageNames.length; i++) {
            String imageName = imageNames[i];

            // Inflate the card layout
            View cardView = LayoutInflater.from(this).inflate(R.layout.card_layout, cardLayout, false);

            // Find the ImageButton and TextView in the card layout
            ImageButton imageButton = cardView.findViewById(R.id.card_image);
            TextView textView = cardView.findViewById(R.id.card_text);

            // Set the image resource and text
            int imageResId = getResources().getIdentifier(imageName, "drawable", getPackageName());
            if (imageResId != 0) {
                imageButton.setImageResource(imageResId);
            } else {
                imageButton.setImageResource(R.drawable.unknown); // Default image if not found
            }
            textView.setText(imageName); // Set the text to the image name

            // Create layout parameters for the GridLayout
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(i % 4, 1f);
            params.rowSpec = GridLayout.spec(i / 4);
            params.setMargins(15, 15, 15, 15);

            // Set the layout parameters and add the card view to the GridLayout
            cardView.setLayoutParams(params);
            cardLayout.addView(cardView);

            // Set listener for the card button, passing the imageName as well
            setCardListener(imageButton, imageName);
        }
    }

    private String[] getImageNamesForCategory(String category) {
        // Placeholder: Replace with actual logic to get image names that contain the category
        switch (category) { // strings od linkovi od DB
            case "conversation": //
                return new String[]{"conversation_talking"};
            case "feelings":
                return new String[]{"feelings_happy", "feelings_sad"};
            case "people":
                return new String[]{"people_man", "people_woman", "people_child"};
            case "drinks":
                return new String[]{"drinks_water", "drinks_juice", "drinks_soda"};
            case "food":
                return new String[]{"food_apple", "food_banana", "food_grapes", "food_banana", "peach"};
            case "fruit":
                return new String[]{"fruit_banana", "fruit_pear", "fruit_lemon", "fruit_pineapple", "apple", "raspberries"};
            default:
                return new String[]{};
        }
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
