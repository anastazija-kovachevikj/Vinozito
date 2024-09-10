package finki.nichk.widgets;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
    private static final int SLOT_COUNT = 4;

    private CardService cardService;

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
        cardSlots[3] = findViewById(R.id.cardslot4);

        // Initialize card layout and slots
        GridLayout cardLayout = findViewById(R.id.card_layout);
        for (int i = 0; i < cardLayout.getChildCount(); i++) {
            View cardView = cardLayout.getChildAt(i);
            if (cardView instanceof FrameLayout) {
                FrameLayout frameLayout = (FrameLayout) cardView;
                ImageButton cardButton = (ImageButton) frameLayout.getChildAt(0);
                // setCardListener(cardButton, String.valueOf(cardView));
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
        //setButtonTouchListener(readAll, this::playAllCardSounds);

        // Set click listeners for card slots
//        for (ImageButton slot : cardSlots) {
//            setSlotClickListener(slot);
//        }
    }

    private void updateCardLayoutByCategory(String category) {
        cardService.fetchCardDataByUserIdAndCategory("64f76d45-f03a-4c9e-9339-4c01524fb08a", category, new CardService.CardServiceCallback() {
            @Override
            public void onCardsFetched(List<Card> cards) {
                runOnUiThread(() -> {
                    cardLayout.removeAllViews(); // Clear all previous views
                    cardLayout.setColumnCount(4); // Set the number of columns to 4

                    int totalCards = cards.size();
                    int totalRows = (int) Math.ceil((double) totalCards / 4); // Calculate the required number of rows
                    cardLayout.setRowCount(totalRows); // Set row count

                    int position = 0;
                    Log.d("CardInit", "Number of cards: " + totalCards);

                    for (Card card : cards) {
                        String cardName = card.getName();
                        String audioVoice = card.getAudioVoice();
                        String imageLink = card.getImage();

                        // Transform the Google Drive link to a direct download link
                        String fileId = extractFileIdFromDriveLink(imageLink);
                        String directImageLink = "https://drive.google.com/uc?export=download&id=" + fileId;

                        // Inflate the card layout
                        View cardView = LayoutInflater.from(CommunicationActivity.this)
                                .inflate(R.layout.card_layout, cardLayout, false);

                        // Find the ImageButton and TextView in the card layout
                        ImageButton imageButton = cardView.findViewById(R.id.card_image);
                        TextView textView = cardView.findViewById(R.id.card_text);

                        textView.setText(cardName); // Set card name

                        // Load the image from the direct download link into the ImageButton using Glide
                        Glide.with(CommunicationActivity.this)
                                .load(directImageLink)
                                .placeholder(R.drawable.unknown)
                                .error(R.drawable.circle_curves)
                                .into(imageButton);

                        // Create layout parameters for the GridLayout
                        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                        params.width = 0;
                        params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                        params.columnSpec = GridLayout.spec(position % 4, 1f); // Correct column placement
                        params.rowSpec = GridLayout.spec(position / 4);        // Correct row placement
                        params.setMargins(15, 15, 15, 15);                    // Add some margin around each card

                        // Set the layout parameters and add the card view to the GridLayout
                        cardView.setLayoutParams(params);
                        cardLayout.addView(cardView);

                        setCardListener(imageButton, extractFileIdFromDriveLink(audioVoice));
                        position++;
                    }
                });
            }


            // Helper method to extract file ID from Google Drive link
            private String extractFileIdFromDriveLink(String driveLink) {
                // Match the file ID using regex or string manipulation
                String[] parts = driveLink.split("/");
                return parts[5]; // The file ID is the 5th part in the URL structure
            }

            @Override
            public void onError(Exception e) {
                // Handle the error
                Log.e("CardService", "Error fetching cards", e);
            }
        });

    }

    private String convertGoogleDriveLinkToDirect(String driveLink) {
        if (driveLink.contains("drive.google.com")) {
            // Extract the file ID from the Google Drive URL
            String[] parts = driveLink.split("/");
            String fileId = parts[5]; // Assuming it's in the correct format
            return "https://drive.google.com/uc?export=download&id=" + fileId;
        } else {
            return driveLink; // In case it's not a Google Drive link
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setCardListener(ImageButton cardButton, String audioVoiceUrl) {
        // Convert Google Drive link to direct download link
       // String directAudioLink = convertGoogleDriveLinkToDirect(audioVoiceUrl);

        cardButton.setOnClickListener(view -> {
            if (count < SLOT_COUNT) {
                // Play the sound when the card is clicked
                playCardSoundFromUrl(audioVoiceUrl);
                Log.d("CardClick", "Audio URL clicked: " + audioVoiceUrl);
            }
        });

        // Apply touch feedback (visual effect) for button press
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
    private void playCardSoundFromUrl(String audioFileId) {
        Log.d("CardSound", "Attempting to play sound from URL: " + audioFileId);

        // Construct the Google Drive direct download link
        String directAudioUrl = "https://drive.google.com/uc?export=download&id=" + audioFileId;

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        try {
            mediaPlayer = new MediaPlayer();

            // Set the data source to the direct Google Drive URL
            mediaPlayer.setDataSource(directAudioUrl);
            mediaPlayer.prepare(); // Prepare the media player
            mediaPlayer.start();   // Start playing the audio

            mediaPlayer.setOnCompletionListener(mp -> {
                mp.release(); // Release MediaPlayer once done
                mediaPlayer = null;
            });
        } catch (Exception e) {
            Log.e("CardSound", "Failed to play sound from URL: " + directAudioUrl, e);
        }
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
