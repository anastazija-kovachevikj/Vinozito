package finki.nichk;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;

import finki.nichk.tablet.screens.MainMenuActivity;
import finki.nichk.services.CardService;
import finki.nichk.tablet.screens.child.ChildMobileActivity;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private CardService cardService;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tablet_child_main_screen);
        cardService = new CardService();

        // Get the ExecutorService from the application
        MyApplication app = (MyApplication) getApplication();
        executorService = app.getExecutorService();

        // Initialize MediaPlayer with your sound file
        mediaPlayer = MediaPlayer.create(this, R.raw.btnclick);

        // Call the async method to fetch data
        fetchCardDataAsync();

        ImageButton startButton = findViewById(R.id.communication_btn);
        startButton.setOnClickListener(view -> {
            // Play the sound
            playSound();

            // Open main menu
            startActivity(new Intent(MainActivity.this, ChildMobileActivity.class));

            // Close this activity
            finish();
        });
    }

    private void fetchCardDataAsync() {
        // Use the thread pool to run background tasks
        executorService.execute(() -> {
            // Simulate fetching data
            //List<Card> cards = cardService.fetchCardDataByUserId("64f76d45-f03a-4c9e-9339-4c01524fb08a");
            // Update the UI with fetched data on the main thread
            runOnUiThread(() -> {
                // Update UI elements here
            });
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
