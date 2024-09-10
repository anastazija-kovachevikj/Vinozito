package finki.nichk.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.media.MediaPlayer;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import finki.nichk.R;
import finki.nichk.models.Card;
import finki.nichk.screens.child.ChildActivity;
import finki.nichk.services.CardService;

public class CommunicationActivity extends AppCompatActivity {
    private GridLayout cardLayout;
    private static final int SLOT_COUNT = 4;

    private CardService cardService;
    private ExecutorService executorService;

    private static final int TAG_IMAGE_URL = 0x1;
    private static final int TAG_AUDIO_VOICE = 0x2;

    private static final int SOUND_DELAY_MILLIS = 1000;
    private int count = 0; // number of occupied slots
    private final ImageButton[] cardSlots = new ImageButton[SLOT_COUNT];
    private final AudioImageButton[] audioButtons = new AudioImageButton[SLOT_COUNT];
    private final int[] cardDrawableIds = new int[SLOT_COUNT];

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.communication_cat);
        cardService = new CardService();

        int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(
                NUMBER_OF_CORES,   // Core pool size
                NUMBER_OF_CORES * 2,  // Maximum pool size
                60L, TimeUnit.SECONDS,  // Keep-alive time
                new LinkedBlockingQueue<Runnable>()  // Work queue
        );
        initializeViews();
        initializeListeners();

    }

    private void initializeViews() {
        // Initialize slot buttons
        cardSlots[0] = findViewById(R.id.cardslot1);
        cardSlots[1] = findViewById(R.id.cardslot2);
        cardSlots[2] = findViewById(R.id.cardslot3);
        cardSlots[3] = findViewById(R.id.cardslot4);

        cardLayout = findViewById(R.id.card_layout);

    }

    private void initializeListeners() {

        cardLayout = findViewById(R.id.card_layout);

        int[] tabIds = {R.id.conversation_tab, R.id.feelings_tab, R.id.people_tab,
                R.id.drinks_tab, R.id.food_tab, R.id.vegetables_tab,
                R.id.fruit_tab, R.id.cutlery_tab, R.id.toys_tab,
                R.id.activities_tab, R.id.animals_tab,
                R.id.clothes_tab, R.id.colors_tab};

        String[] categories = {"conversation", "feelings", "people", "drinks", "food",
                "Vegetable", "Fruit", "cutlery", "toys", "activities",
                "animals", "clothes", "colors"};

        for (int i = 0; i < tabIds.length; i++) {
            ImageButton tabButton = findViewById(tabIds[i]);
            String category = categories[i];
            tabButton.setOnClickListener(view -> updateCardLayoutByCategory(category));
        }
        // Initialize buttons
        ImageButton backButton = findViewById(R.id.back_button);
        ImageButton clearButton = findViewById(R.id.clearAll);
        ImageButton readAll = findViewById(R.id.read_all);

        // Set listeners
        setButtonTouchListener(backButton, this::navigateToChildActivity);
        setButtonTouchListener(clearButton, this::clearAllSlots);
        setButtonTouchListener(readAll, this::playAllCardSounds);

    }

    private void updateCardLayoutByCategory(String category) {
        cardService.fetchCardDataByUserIdAndCategory("64f76d45-f03a-4c9e-9339-4c01524fb08a", category, new CardService.CardServiceCallback() {
            @Override
            public void onCardsFetched(List<Card> cards) {
                runOnUiThread(() -> {
                    cardLayout.removeAllViews();
                    cardLayout.setColumnCount(4);

                    int totalCards = cards.size();
                    int totalRows = (int) Math.ceil((double) totalCards / 4); // Calculate the required number of rows
                    cardLayout.setRowCount(totalRows); // Set row count

                    int position = 0;
                    Log.d("CardInit", "Number of cards: " + totalCards);

                    for (Card card : cards) {
                        String cardName = card.getName();
                        String audioVoice = card.getAudioVoice();
                        String imageLink = card.getImage();


                        String fileId = extractFileIdFromDriveLink(imageLink);
                        String directImageLink = "https://drive.google.com/uc?export=download&id=" + fileId;

                        View cardView = LayoutInflater.from(CommunicationActivity.this)
                                .inflate(R.layout.card_layout, cardLayout, false);

                        // Find the ImageButton and TextView in the card layout
                        ImageButton imageButton = cardView.findViewById(R.id.card_image);
                        TextView textView = cardView.findViewById(R.id.card_text);

                        textView.setText(cardName); // Set card name

                        // Load the image from the direct download link into the ImageButton using Glide

                        Glide.with(CommunicationActivity.this)
                                .load(directImageLink)
                                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
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

                        setCardListener(imageButton, extractFileIdFromDriveLink(audioVoice),directImageLink);
                        position++;
                    }
                });
            }

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

    @SuppressLint("ClickableViewAccessibility")
    private void setCardListener(ImageButton cardButton, String audioVoiceUrl, String imageLink) {
        // Convert Google Drive link to direct download link
       // String directAudioLink = convertGoogleDriveLinkToDirect(audioVoiceUrl);
       // setSlotImage(imageLink);

        cardButton.setOnClickListener(view -> {
            if (count < SLOT_COUNT) {
                // Play the sound when the card is clicked
                playCardSoundFromUrl(audioVoiceUrl);
                Log.d("CardClick", "Audio URL clicked: " + audioVoiceUrl);
                setSlotImage(imageLink,audioVoiceUrl);
                Log.d("CardImage", "Image link: " + imageLink);

            }

        });
        applyTouchFeedback(cardButton);

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

    private void playAllCardSounds() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable playSoundTask = new Runnable() {
            int index = 0;

            @Override
            public void run() {
                if (index < SLOT_COUNT) {
                    if (cardSlots[index].getVisibility() == View.VISIBLE) {
                        applyTouchFeedback(cardSlots[index]);
                        String audio = audioButtons[index].audioUrl;
                        playCardSoundFromUrl(audio);
                       // playCardSound(String.valueOf(cardDrawableIds[index]));
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

    @SuppressLint("ClickableViewAccessibility")
    private void setSlotClickListener(ImageButton slot, String audio) {
        String audioUrl = (String) slot.getTag(R.id.tag_audio_voice);
        Log.d("TAG_AUDIO", "The audio is:" + audioUrl);
        slot.setOnClickListener(view -> {
            int slotIndex = getSlotIndex(slot);
            if (slotIndex != -1) {
               playCardSoundFromUrl(audio);
                //playCardSound(String.valueOf(cardDrawableIds[slotIndex]));

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

    private void setSlotImage(String imageUrl, String audioVoice) {

        if (count < SLOT_COUNT) {

            ImageButton slot = cardSlots[count];
            AudioImageButton audioImageButton = new AudioImageButton(slot, audioVoice);
            audioButtons[count] = audioImageButton;

            Glide.with(CommunicationActivity.this)
                    .load(imageUrl)
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .placeholder(R.drawable.unknown)
                    .error(R.drawable.circle_curves)
                    .into(slot);

            slot.setVisibility(View.VISIBLE);
            setSlotClickListener(slot,audioVoice);

            cardDrawableIds[count] = getResources().getIdentifier(imageUrl, "raw", getPackageName());
            Log.d("CardSlots", "Slot " + count + " updated with image: " + imageUrl);
            count++;


            //Log.d("TEST TAG", "Value of tag image url is " + TAG_IMAGE_URL + " and for audio " + R.id.tag_audio_voice);
        } else {
            Log.w("CardSlotsFULL", "All slots are full. Cannot add more cards.");
        }
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
