package finki.nichk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.media.MediaPlayer;

import androidx.appcompat.app.AppCompatActivity;

public class CommunicationActivity extends AppCompatActivity {

    int count = 1; // to count which slot
    ImageButton cardslot; // the slot to be filled

    private MediaPlayer mediaPlayer_jabolko;
    private MediaPlayer mediaPlayer_banana;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.communication_cat); // to be updated to communication_layout

        ImageButton backButton = findViewById(R.id.back_button);
        ImageButton clearButton = findViewById(R.id.clearAll);

        // TAB BUTTONS -> CATEGORIES
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
        ImageButton read_all = findViewById(R.id.read_all);

        ImageButton banana_card = findViewById(R.id.banana);
        ImageButton jabolko_card = findViewById(R.id.jabolko);

        ImageButton cardslot1 = findViewById(R.id.cardslot1);
        ImageButton cardslot2 = findViewById(R.id.cardslot2);
        ImageButton cardslot3 = findViewById(R.id.cardslot3);

        buttonTouchListener(backButton, () -> {
            Intent intent = new Intent(CommunicationActivity.this, ChildActivity.class);
            startActivity(intent);
        });

        buttonTouchListener(clearButton, () -> {
            cardslot1.setVisibility(View.INVISIBLE);
            cardslot2.setVisibility(View.INVISIBLE);
            cardslot3.setVisibility(View.INVISIBLE);
            count = 1; // reset
        });

        GridLayout cardLayout = findViewById(R.id.card_layout); // the grid with the cards

        // check each child view in the GridLayout
        for (int i = 0; i < cardLayout.getChildCount(); i++) {
            View cardView = cardLayout.getChildAt(i);

            // if the child is a FrameLayout containing an ImageButton
            if (cardView instanceof FrameLayout) {
                FrameLayout frameLayout = (FrameLayout) cardView;
                ImageButton cardButton = (ImageButton) frameLayout.getChildAt(0);

                // set a listener for the button
                setCardListener(cardButton);
            }
        }

        // Sounds
        // Initialize MediaPlayer with the audio file from raw folder
        mediaPlayer_banana = MediaPlayer.create(this, R.raw.banana);
        mediaPlayer_jabolko = MediaPlayer.create(this, R.raw.jabolko);

        // Set onClickListener for the button
//        banana_card.setOnClickListener(v -> {
//            // Play the sound
//            if (mediaPlayer_banana  != null) {
//                mediaPlayer_banana .start();
//            }
//        });
//
//        jabolko_card.setOnClickListener(v -> {
//            // Play the sound
//            if (mediaPlayer_jabolko  != null) {
//                mediaPlayer_jabolko .start();
//            }
//        });
    }


    private void setCardListener(ImageButton cardSlot) {
        cardSlot.setOnClickListener(view -> {
            Drawable cardDrawable = cardSlot.getDrawable();
            setCardslotImage(cardDrawable);

            // Check if the clicked card is jabolko or banana and play the corresponding sound
            if (cardSlot.getId() == R.id.jabolko) {
                if (mediaPlayer_jabolko != null) {
                    mediaPlayer_jabolko.start();
                }
            } else if (cardSlot.getId() == R.id.banana) {
                if (mediaPlayer_banana != null) {
                    mediaPlayer_banana.start();
                }
            }
        });
    }


    @SuppressLint("ClickableViewAccessibility")
    private void buttonTouchListener(ImageButton button, Runnable onClickAction) {
        button.setOnClickListener(v -> onClickAction.run());

        button.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // transparent when pressed
                    v.setAlpha(0.5f);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    // back to normal released or canceled
                    v.setAlpha(1.0f);
                    break;
            }
            return false;
        });
    }

    private void setCardslotImage(Drawable drawable) { // 3 card slots in total
        switch (count) {
            case 1:
                cardslot = findViewById(R.id.cardslot1);
                break;
            case 2:
                cardslot = findViewById(R.id.cardslot2);
                break;
            case 3:
                cardslot = findViewById(R.id.cardslot3);
                break;
            default:
                count = 1; // reset count
                cardslot = findViewById(R.id.cardslot1);
        }

        if (cardslot.getVisibility() != View.VISIBLE) { // slot not empty
            // slot is empty -> card can be added
            cardslot.setImageDrawable(drawable);
            cardslot.setVisibility(View.VISIBLE);
            count++;
        }
    }
}
