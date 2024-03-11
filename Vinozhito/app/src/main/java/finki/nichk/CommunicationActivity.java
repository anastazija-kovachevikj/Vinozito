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

import androidx.appcompat.app.AppCompatActivity;

public class CommunicationActivity extends AppCompatActivity {

    int count = 1; // to count which slot
    ImageButton cardslot; // the slot to be filled


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.communication_cat); // to be updated to communication_layout

        ImageButton backButton = findViewById(R.id.back_button);
        ImageButton clearButton = findViewById(R.id.clearAll);

//        ImageButton banana = findViewById(R.id.banana);
//        ImageButton lemon = findViewById(R.id.lemon);

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

        // listener for the banana button
//        buttonTouchListener(banana, () -> {
//            try {
//                // Get the drawable of the banana button
//                Drawable bananaDrawable = banana.getDrawable();
//                setCardslotImage(bananaDrawable);
//                Log.d("BananaButton", "Image changed for cardslot1");
//            } catch (NullPointerException e) {
//                // Handle null drawable
//                e.printStackTrace();
//                Log.e("BananaButton", "Error: Drawable is null");
//            }
//        });
//
//        // listener for lemon
//        buttonTouchListener(lemon, () -> {
//            try {
//                Drawable bananaDrawable = lemon.getDrawable();
//                setCardslotImage(bananaDrawable);
//
//            } catch (NullPointerException e) {
//                e.printStackTrace();
//                Log.e("BananaButton", "Error: Drawable is null");
//            }
//        });

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
    }

    private void setCardListener(ImageButton cardSlot) {
        cardSlot.setOnClickListener(view -> {
            Drawable cardDrawable = cardSlot.getDrawable();
            setCardslotImage(cardDrawable);
            //cardSlot.setVisibility(View.INVISIBLE);
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
