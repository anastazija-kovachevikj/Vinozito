package finki.nichk.mobile.activities.parent;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import finki.nichk.R;
import finki.nichk.models.Card;
import finki.nichk.models.User;
import finki.nichk.services.CardService;
import finki.nichk.services.UserService;
import finki.nichk.services.authentication.TokenManager;

public class MyCardsActivity extends AppCompatActivity {

    private GridLayout cardLayout;
    private CardService cardService;
    private UserService userService;
    TokenManager tokenManager;
    private User user;
    private  String userToken;
    private String userId;
    private TextView categoryTextView;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_parent_mycards_list);

        cardService = new CardService();
        userService = new UserService(this);

        cardLayout = findViewById(R.id.card_display); // Your GridLayout ID
        categoryTextView = findViewById(R.id.categoryTextView);

        category = getIntent().getStringExtra("category");

        categoryTextView.setText(category);

        tokenManager = new TokenManager(this);
        userToken = tokenManager.getUsername();
        userService.fetchUserByUsername(new UserService.UserCallback() {
            @Override
            public void onUserFetched(User fetchedUser) {

                user = fetchedUser;
                userId= user.getId();
                fetchAndDisplayCards();
                Log.d("CommunicationMobile", "User ID: " + user.getId());

            }

            @Override
            public void onError(String error) {

                Log.e("CommunicationMobile", "Error fetching user: " + error);
            }
        });


    }

    private void fetchAndDisplayCards() {
        cardService.fetchCardDataByUserIdAndCategory(userId, category, new CardService.CardServiceCallback() {
            @Override
            public void onCardsFetched(List<Card> cards) {
                runOnUiThread(() -> populateCardGrid(cards));
            }

            @Override
            public void onError(Exception e) {
                Log.e("CardService", "Error fetching cards", e);
            }
        });
    }

    private void populateCardGrid(List<Card> cards) {
        cardLayout.removeAllViews(); 
        cardLayout.setColumnCount(3);

        int position = 0;
        for (Card card : cards) {
            String cardName = card.getName();
            String imageLink = card.getImage();


            LayoutInflater inflater = LayoutInflater.from(MyCardsActivity.this);
            View cardView = inflater.inflate(R.layout.tablet_card_layout, cardLayout, false);


            ImageButton imageButton = cardView.findViewById(R.id.card_image);
            TextView textView = cardView.findViewById(R.id.card_text);

            textView.setText(cardName);
            Glide.with(MyCardsActivity.this)
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
            params.setMargins(8, 8, 8, 8); // Set margins

            cardView.setLayoutParams(params);
            cardLayout.addView(cardView);

            position++;
        }
    }
}
