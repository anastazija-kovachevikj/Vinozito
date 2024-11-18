package finki.nichk.mobile.activities.parent;

import android.os.Bundle;
import android.util.Log;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import finki.nichk.R;
import finki.nichk.models.User;
import finki.nichk.services.CardService;
import finki.nichk.services.UserService;
import finki.nichk.services.authentication.TokenManager;

public class CustomCardActivity extends AppCompatActivity {

    private GridLayout cardLayout;
    private CardService cardService;
    private UserService userService;
    TokenManager tokenManager;
    private User user;
    private  String userToken;
    private String userId;
    private TextView cardName;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_custom_card);

        cardService = new CardService();
        userService = new UserService(this);


        cardName = findViewById(R.id.cardNameTexView);

//        category = getIntent().getStringExtra("category");
//
//        categoryTextView.setText(category);

        tokenManager = new TokenManager(this);
        userToken = tokenManager.getUsername();
        userService.fetchUserByUsername(new UserService.UserCallback() {
            @Override
            public void onUserFetched(User fetchedUser) {

                user = fetchedUser;
                Log.d("CommunicationMobile", "User ID: " + user.getId());

            }

            @Override
            public void onError(String error) {

                Log.e("CommunicationMobile", "Error fetching user: " + error);
            }
        });


    }
}
