package finki.nichk;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import finki.nichk.models.Card;
import finki.nichk.screens.MainMenuActivity;
import finki.nichk.services.CardService;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private CardService cardService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cardService = new CardService();

        // Initialize MediaPlayer with your sound file
        mediaPlayer = MediaPlayer.create(this, R.raw.btnclick);

        // Call the async method to fetch data
       // fetchCardData();
        cardService.fetchCardDataByUserId("64f76d45-f03a-4c9e-9339-4c01524fb08a");

        ImageButton startButton = findViewById(R.id.start_btn);
        startButton.setOnClickListener(view -> {
            // Play the sound
            playSound();

            // Open main menu
            startActivity(new Intent(MainActivity.this, MainMenuActivity.class));

            // Close this activity
            finish();
        });
    }



    private void playSound() {
        // Check if mediaPlayer is not null and not already playing
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release MediaPlayer resources
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
