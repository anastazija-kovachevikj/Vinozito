package finki.nichk.mobile.activities.child;

import static finki.nichk.R.drawable.*;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import finki.nichk.R;

public class BubbleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.motion);

        // Find the ImageButtons in the layout
        ImageButton bubbleButton = findViewById(R.id.bubbleButton);
        ImageButton bubbleButton2 = findViewById(R.id.bubbleButton2);

        // Set up click listeners for each bubble button
        bubbleButton.setOnClickListener(v -> handleBubbleClick(bubbleButton));

        bubbleButton2.setOnClickListener(v -> handleBubbleClick(bubbleButton2));
    }

    private void handleBubbleClick(ImageButton bubbleButton) {
        // Check if the background is an instance of AnimationDrawable
        if (bubbleButton.getBackground() instanceof AnimationDrawable) {
            AnimationDrawable bubbleAnimation = (AnimationDrawable) bubbleButton.getBackground();
            bubbleAnimation.stop();   // Stop the current animation if it's running
            bubbleAnimation.selectDrawable(0);  // Reset to the first frame
            bubbleAnimation.start();  // Start the frame animation again

            // Calculate the total duration of the animation
            int totalDuration = 0;
            for (int i = 0; i < bubbleAnimation.getNumberOfFrames(); i++) {
                totalDuration += bubbleAnimation.getDuration(i);
            }

            // Use a Handler to hide the button after the animation ends
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                // Hide the button
                bubbleButton.setVisibility(View.INVISIBLE);

                // Use another Handler to make the button reappear after 5 seconds
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    // Stop the animation and explicitly set it to the first frame
                    bubbleAnimation.stop();
                    bubbleAnimation.selectDrawable(0); // Ensure it's reset to the first frame

                    // Force a refresh of the ImageButton background to display the first frame
                    bubbleButton.setBackground(bubbleAnimation);

                    // Show the button again

                    bubbleButton.setVisibility(View.VISIBLE);
                }, 2000);  // 5000 milliseconds = 5 seconds
            }, totalDuration - 100);  // Wait for the animation to finish before hiding the button
        }
    }
}
