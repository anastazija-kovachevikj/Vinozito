package finki.nichk.mobile.activities.parent;

import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import finki.nichk.R;
import finki.nichk.models.Card;
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
    private String userToken;
    private String userId;
    private TextView cardName;

    private EditText cardTitle;
    private String category;
    private ImageButton play_default_rec;
    private String soundLink;
    private String imageLink;
    private ImageView cardImage;
    MediaPlayer mediaPlayer;
    Card card;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_custom_card);

        cardService = new CardService();
        userService = new UserService(this);

        cardName = findViewById(R.id.cardNameTexView);
        cardTitle = findViewById(R.id.cardTitle);
        play_default_rec = findViewById(R.id.play_default_rec);
        cardImage = findViewById(R.id.cardImage);


        setCardDetails();
        setPlayListener(play_default_rec);

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

    private void setCardDetails() {

        card = (Card) getIntent().getSerializableExtra("selectedCard");

        assert card != null;
        cardName.setText(card.getName());
        cardTitle.setHint(card.getName());
        soundLink = card.getAudioVoice();
        imageLink = card.getImage();

        Glide.with(CustomCardActivity.this)
                .load(imageLink)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .placeholder(R.drawable.unknown)
                .error(R.drawable.circle_curves)
                .into(cardImage);

    }

    private void setPlayListener(ImageButton button) {
        button.setOnClickListener(view -> {

            playCardSoundFromUrl(soundLink);

        });
    }

    private void playCardSoundFromUrl(String audioFileId) {

        Log.d("CardSound", "Attempting to play sound from URL: " + audioFileId);

        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        } else {
            mediaPlayer.reset();
        }

        try {

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(audioFileId);
            mediaPlayer.prepare();
            mediaPlayer.start();

            mediaPlayer.setOnCompletionListener(mp -> {
                mp.release();
                mediaPlayer = null;
            });

        } catch (Exception e) {
            Log.e("CardSound", "Failed to play sound from URL: " + audioFileId, e);
        }
    }
}
