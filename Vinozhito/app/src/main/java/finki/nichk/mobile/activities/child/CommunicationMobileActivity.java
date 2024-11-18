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
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import finki.nichk.R;
import finki.nichk.models.Card;
import finki.nichk.models.User;
import finki.nichk.services.CardService;

import finki.nichk.services.UserService;
import finki.nichk.services.authentication.TokenManager;
import finki.nichk.tablet.widgets.AudioImageButton;


public class CommunicationMobileActivity extends AppCompatActivity {
    private GridLayout cardLayout;
    TokenManager tokenManager;

    private static final int SLOT_COUNT = 8;
    private CardService cardService;
    private UserService userService;
    private static final int SOUND_DELAY_MILLIS = 1500;

    private  String userToken;

    private User user;
    private String userId;
    private int count = 0; // number of occupied slots
    private final ImageButton[] cardSlots = new ImageButton[SLOT_COUNT];
    private final AudioImageButton[] audioButtons = new AudioImageButton[SLOT_COUNT];
    private final int[] cardDrawableIds = new int[SLOT_COUNT];
    private MediaPlayer mediaPlayer;
    private ImageButton previouslySelectedTab;
    private ImageView selectedSlot = null; // Tracks the currently selected slot
    private ImageButton deleteButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_communication);
        cardService = new CardService();
        userService = new UserService(this);
        previouslySelectedTab = null;

        deleteButton = findViewById(R.id.clear_button);
        deleteButton.setOnClickListener(v -> clearSelectedCard());

        ImageButton conversationTab = findViewById(R.id.conversation_tab);

        initializeViews();
        tokenManager = new TokenManager(this);
        userToken = tokenManager.getUsername();
        userService.fetchUserByUsername(new UserService.UserCallback() {
            @Override
            public void onUserFetched(User fetchedUser) {

                user = fetchedUser;
                userId= user.getId();
                Log.d("CommunicationMobile", "User ID: " + user.getId());

                updateCardLayoutByCategory("Conversation", conversationTab); // Communivation tab selected on opening
            }

            @Override
            public void onError(String error) {

                Log.e("CommunicationMobile", "Error fetching user: " + error);
            }
        });

        initializeListeners();
        setupCardSlotListeners();
    }

    private void setupCardSlotListeners() {
        for (ImageButton slot : cardSlots) {
            slot.setOnLongClickListener(v -> {
                if (selectedSlot == slot) { // if slot is selected, unselect it
                    selectedSlot.setBackgroundResource(R.drawable.card_background); // reset
                    selectedSlot.setTag(null); // clear tag
                    selectedSlot = null; // reset the selected slot
                    deleteButton.setImageResource(R.drawable.trash_closed);
                    Toast.makeText(this, "Картичка одселектирана", Toast.LENGTH_SHORT).show(); // display message
                } else { // if not selected, select slot
                    if (selectedSlot != null) {
                        selectedSlot.setBackgroundResource(R.drawable.card_background);
                        selectedSlot.setTag(null);
                        Toast.makeText(this, "Картичка селектирана", Toast.LENGTH_SHORT).show();

                    }
                    selectedSlot = (ImageView) v; // set new selected slot
                    selectedSlot.setBackgroundResource(R.drawable.card_background_delete); // highlight slot
                    selectedSlot.setTag("card_present"); // Indicate a card is present
                    deleteButton.setImageResource(R.drawable.trash_open_empty);
                }
                return true; // event handled
            });

            slot.setOnClickListener(v -> {
                if ("card_present".equals(slot.getTag())) {
                    if (selectedSlot != null && selectedSlot != slot) {
                        selectedSlot.setBackgroundResource(R.drawable.card_background); // deselect the previously selected slot
                    }
                    // select the current slot
                    selectedSlot = (ImageView) v;
                    selectedSlot.setBackgroundResource(R.drawable.card_background_delete);
                }
            });
        }
    }

    private void clearSelectedCard() {
        if (selectedSlot != null && "card_present".equals(selectedSlot.getTag())) { // if the tag is "card_present" -> remove card
            int selectedIndex = -1;

            for (int i = 0; i < cardSlots.length; i++) { // loop to find the index of the currently selected card/slot
                if (cardSlots[i] == selectedSlot) {
                    selectedIndex = i;
                    break;
                }
            }

            if (selectedIndex != -1) { // selected index found aka card is selected to be removed
                // remove (card and audio)
                selectedSlot.setImageDrawable(null);
                selectedSlot.setTag(null);
                selectedSlot.setVisibility(View.INVISIBLE);
                audioButtons[selectedIndex] = null;

                // shift the cards and audio
                for (int i = selectedIndex; i < cardSlots.length - 1; i++) {
                    // move the card and tag from the next slot (on the right)
                    cardSlots[i].setImageDrawable(cardSlots[i + 1].getDrawable());
                    cardSlots[i].setTag(cardSlots[i + 1].getTag());
                    cardSlots[i].setVisibility(cardSlots[i + 1].getVisibility());

                    audioButtons[i] = audioButtons[i + 1]; // set new audio for the slot

                    setSlotClickListener(cardSlots[i], i);
                }

                // clear last slot and audio
                cardSlots[cardSlots.length - 1].setImageDrawable(null);
                cardSlots[cardSlots.length - 1].setTag(null);
                cardSlots[cardSlots.length - 1].setVisibility(View.INVISIBLE);
                audioButtons[cardSlots.length - 1] = null;

                count--; // one less slot
            }
            // reset
            deleteButton.setImageResource(R.drawable.trash_closed);
            selectedSlot.setBackgroundResource(R.drawable.card_background);
            selectedSlot = null;
        } else { // CLEAR ALL -> no card selected, but trash pressed
            for (int i = 0; i < cardSlots.length; i++) {
                cardSlots[i].setImageDrawable(null);
                cardSlots[i].setTag(null);
                cardSlots[i].setVisibility(View.INVISIBLE);
                audioButtons[i] = null;
            }
            count = 0; // reset slot count

            HorizontalScrollView scrollView = findViewById(R.id.cardsScroll);
            scrollView.smoothScrollTo(0, 0);
        }
    }

//    public void checkSelectedSlot() {
//        if (selectedSlot != null) {
//            int slotId = selectedSlot.getId(); // ID of selected slot
//            Toast.makeText(this, "Selected Slot ID: " + slotId, Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "No slot selected", Toast.LENGTH_SHORT).show();
//        }
//    }

    void selectTab(ImageButton newTab) {
        if (previouslySelectedTab != null) {
            previouslySelectedTab.setBackgroundResource(R.drawable.icon_circle); // default
        }
        newTab.setBackgroundResource(R.drawable.icon_selected_circle); // selected
        previouslySelectedTab = newTab;
    }

//    private void clearAllSlots() {
//        for (ImageButton slot : cardSlots) {
//            Log.d("Image delete button", "Slot is " + slot.getTag());
//            slot.setVisibility(View.INVISIBLE);
//        }
//        count = 0;
//    }

    private void initializeViews() {
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
            tabButton.setOnClickListener(view -> updateCardLayoutByCategory(category, tabButton));
        }

        ImageButton backButton = findViewById(R.id.back_button);
        ImageButton readAll = findViewById(R.id.read_button);

        setButtonTouchListener(backButton, this::navigateToChildActivity);
        setButtonTouchListener(readAll, this::playAllCardSounds);
    }

    private void updateCardLayoutByCategory(String category, ImageButton btn) {

        cardService.fetchCardDataByUserIdAndCategory(userId, category, new CardService.CardServiceCallback() {
            @Override
            public void onCardsFetched(List<Card> cards) {
                runOnUiThread(() -> {
                    cardLayout.removeAllViews();
                    cardLayout.setColumnCount(3);

                    int totalCards = cards.size();
                    int totalRows = (int) Math.ceil((double) totalCards / 3); // calculate the required number of rows
                    cardLayout.setRowCount(totalRows); // set row count

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

        if (mediaPlayer == null || audioFileId.isEmpty()) { // dodadov ili
            mediaPlayer = new MediaPlayer();
        } else {
            mediaPlayer.reset(); // Reset the MediaPlayer to reuse it
        }

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(audioFileId);
            mediaPlayer.prepare();
            mediaPlayer.start();   // start playing the audio

            mediaPlayer.setOnCompletionListener(mp -> {
                mp.release();
                mediaPlayer = null;
            });

        } catch (Exception e) {
            Log.e("CardSound", "Failed to play sound from URL: " + audioFileId, e);
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
    private void setSlotClickListener(ImageButton slot, int slotIndex) {
        slot.setOnClickListener(view -> {
            // Get the current audio URL from audioButtons or the slot's tag
            if (audioButtons[slotIndex] != null) {
                String audioUrl = audioButtons[slotIndex].audioUrl;
                Log.d("TAG_AUDIO", "Playing audio for slot: " + slotIndex + ", URL: " + audioUrl);
                playCardSoundFromUrl(audioUrl);
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

            // update the audio button array with the current slot and audio URL
            audioButtons[count] = new AudioImageButton(slot, audioVoice);

            // load image into slot
            Glide.with(CommunicationMobileActivity.this)
                    .load(imageUrl)
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .placeholder(R.drawable.unknown)
                    .error(R.drawable.circle_curves)
                    .into(slot);

            slot.setVisibility(View.VISIBLE);

            setSlotClickListener(slot, count);// click listener with slot index

            cardDrawableIds[count] = getResources().getIdentifier(imageUrl, "raw", getPackageName()); // keep track of drawable IDs if needed
            Log.d("CardSlots", "Slot " + count + " updated with image: " + imageUrl);

            HorizontalScrollView scrollView = findViewById(R.id.cardsScroll); // follow tha last card

            slot.post(() -> {
                int cardWidth = slot.getWidth() + 65;  // slot width plus padding
                int thirdCardPosition = cardWidth * (count - 2); // Position for the 3rd card to be visible


                if (count > 2) { // start scrolling if there are enough cards added aka more than 2
                    scrollView.smoothScrollTo(thirdCardPosition, 0);
                }
            });
            count++; // track filled slots

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

//    private int getSlotIndex(ImageButton slot) {
//        for (int i = 0; i < cardSlots.length; i++) {
//            if (cardSlots[i] == slot) {
//                return i;
//            }
//        }
//        return -1; // Slot not found
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
