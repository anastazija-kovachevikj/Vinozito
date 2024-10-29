package finki.nichk.mobile.activities.child;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import finki.nichk.R;
import finki.nichk.models.Card;
import finki.nichk.services.CardService;

import finki.nichk.tablet.widgets.AudioImageButton;


public class CommunicationMobileActivity extends AppCompatActivity {
    private GridLayout cardLayout;
    private static final int SLOT_COUNT = 8;
    private CardService cardService;
    private static final int SOUND_DELAY_MILLIS = 1500;

    private int count = 0; // number of occupied slots
    private final ImageButton[] cardSlots = new ImageButton[SLOT_COUNT];
    private final AudioImageButton[] audioButtons = new AudioImageButton[SLOT_COUNT];
    private final int[] cardDrawableIds = new int[SLOT_COUNT];
    private MediaPlayer mediaPlayer;
    private ImageButton previouslySelectedTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_communication);
        cardService = new CardService();
        previouslySelectedTab = null;
        initializeViews();
        initializeListeners();

    }

    void selectTab(ImageButton newTab) {
        if (previouslySelectedTab != null) {
            previouslySelectedTab.setBackgroundResource(R.drawable.icon_circle); // default
        }
        newTab.setBackgroundResource(R.drawable.icon_selected_circle); // selected
        previouslySelectedTab = newTab;
    }


    private void initializeViews() {
        // Initialize slot buttons
        cardSlots[0] = findViewById(R.id.cardslot1);
        cardSlots[1] = findViewById(R.id.cardslot2);
        cardSlots[2] = findViewById(R.id.cardslot3);
        cardSlots[3] = findViewById(R.id.cardslot4);
        cardSlots[4] = findViewById(R.id.cardslot5);
        cardSlots[5] = findViewById(R.id.cardslot6);
        cardSlots[6] = findViewById(R.id.cardslot7);
        cardSlots[7] = findViewById(R.id.cardslot8);

        cardLayout = findViewById(R.id.card_layout2);

    }

    private void initializeListeners() {

        cardLayout = findViewById(R.id.card_layout2);

        int[] tabIds = {R.id.conversation_tab, R.id.feelings_tab, R.id.people_tab,
                R.id.drinks_tab, R.id.food_tab, R.id.vegetables_tab,
                R.id.fruit_tab, R.id.activities_tab, R.id.animals_tab,
                R.id.clothes_tab, R.id.colors_tab};

        String[] categories = {"Conversation", "Feelings", "People", "Drinks", "Food",
                "Vegetable", "Fruit", "Activities",
                "Animals", "Clothes", "Colors"};

        for (int i = 0; i < tabIds.length; i++) {
            ImageButton tabButton = findViewById(tabIds[i]);
            String category = categories[i];
            tabButton.setOnClickListener(view -> updateCardLayoutByCategory(category,tabButton));

        }
        // Initialize buttons
        ImageButton backButton = findViewById(R.id.back_button);
        ImageButton clearButton = findViewById(R.id.clear_button);
        ImageButton readAll = findViewById(R.id.read_button);

        setButtonTouchListener(backButton, this::navigateToChildActivity);
        setButtonTouchListener(clearButton, this::clearAllSlots);
        setButtonTouchListener(readAll, this::playAllCardSounds);

    }

    private void updateCardLayoutByCategory(String category,ImageButton btn) {

        cardService.fetchCardDataByUserIdAndCategory("1f2732fe-05c1-4d50-a74e-e5f065112374", category, new CardService.CardServiceCallback() {
            @Override
            public void onCardsFetched(List<Card> cards) {
                runOnUiThread(() -> {
                    cardLayout.removeAllViews();
                    cardLayout.setColumnCount(3);

                    int totalCards = cards.size();
                    int totalRows = (int) Math.ceil((double) totalCards / 3); // Calculate the required number of rows
                    cardLayout.setRowCount(totalRows); // Set row count

                    int position = 0;
                    Log.d("CardInit", "Number of cards: " + totalCards);

                    for (Card card : cards) {
                        String cardName = card.getName();
                        String audioVoice = card.getAudioVoice();
                        String imageLink = card.getImage();



                        View cardView = LayoutInflater.from(CommunicationMobileActivity.this)
                                .inflate(R.layout.tablet_card_layout, cardLayout, false);

                        // Find the ImageButton and TextView in the card layout
                        ImageButton imageButton = cardView.findViewById(R.id.card_image);
                        TextView textView = cardView.findViewById(R.id.card_text);

                        textView.setText(cardName); // Set card name

                        // Load the image from the direct download link into the ImageButton using Glide
                        Glide.with(CommunicationMobileActivity.this)
                                .load(imageLink)
                                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                                .placeholder(R.drawable.unknown)
                                .error(R.drawable.circle_curves)
                                .into(imageButton);

                        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                        params.width = 0;
                        params.height = GridLayout.LayoutParams.WRAP_CONTENT;

                        params.columnSpec = GridLayout.spec(position % 3, 1f); // Correct column placement
                        params.rowSpec = GridLayout.spec(position / 3); // Correct row placement
                        params.setMargins(1, 5, 1, 1);

                        // Set the layout parameters and add the card view to the GridLayout
                        cardView.setLayoutParams(params);
                        cardLayout.addView(cardView);

                        setCardListener(imageButton, audioVoice, imageLink);
                        position++;
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                Log.e("CardService", "Error fetching cards", e);
            }
        });
        selectTab(btn);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void setCardListener(ImageButton cardButton, String audioVoiceUrl, String imageLink) {

        cardButton.setOnClickListener(view -> {
            if (count < SLOT_COUNT) {
                // Play the sound when the card is clicked
                playCardSoundFromUrl(audioVoiceUrl);
                Log.d("CardClick", "Audio URL clicked: " + audioVoiceUrl);
                setSlotImage(imageLink, audioVoiceUrl);
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

        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        } else {
            mediaPlayer.reset(); // Reset the MediaPlayer to reuse it
        }

        try {

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(audioFileId);
            mediaPlayer.prepare(); // Prepare the media player
            mediaPlayer.start();   // Start playing the audio

            mediaPlayer.setOnCompletionListener(mp -> {
                mp.release(); // Release MediaPlayer once done
                mediaPlayer = null;
            });

        } catch (Exception e) {
            Log.e("CardSound", "Failed to play sound from URL: " + audioFileId , e);
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
        //playSoundTask.run();
        scheduler.schedule(playSoundTask, 0, TimeUnit.MILLISECONDS); // Start immediately
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

            Glide.with(CommunicationMobileActivity.this)
                    .load(imageUrl)
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .placeholder(R.drawable.unknown)
                    .error(R.drawable.circle_curves)
                    .into(slot);

            slot.setVisibility(View.VISIBLE);
            setSlotClickListener(slot, audioVoice);

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
        Intent intent = new Intent(CommunicationMobileActivity.this, ChooseChildActivity.class);
        startActivity(intent);
    }

    private void clearAllSlots() {

        for (ImageButton slot : cardSlots) {
            Log.d("Image delete button", "Slot is " + slot.getTag());
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
