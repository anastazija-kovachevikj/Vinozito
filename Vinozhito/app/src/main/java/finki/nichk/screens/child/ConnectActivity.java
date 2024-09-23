package finki.nichk.screens.child;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import finki.nichk.R;

public class ConnectActivity extends AppCompatActivity {

    private ImageView targetImage;
    private List<Integer> dishImages;
    private int targetImageRes;
    private TextView resultTextView;
    private ImageView firstPlate, secondPlate, thirdPlate, forthPlate;
    private ImageView heart;
    private int currentRound = 1; // Track current round
    private final int totalRounds = 5; // Number of rounds to play

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connecting_main_menu);

        targetImage = findViewById(R.id.targetImage);
        heart = findViewById(R.id.moving_heart);
        //resultTextView = findViewById(R.id.resultTextView);

        firstPlate = findViewById(R.id.firstPlate);
        secondPlate = findViewById(R.id.secondPlate);
        thirdPlate = findViewById(R.id.thirdtPlate);
        forthPlate = findViewById(R.id.forthPlate);

        // Load dish images
        dishImages = new ArrayList<>();
        dishImages.add(R.drawable.fruit_lemon);
        dishImages.add(R.drawable.fruit_pear);
        dishImages.add(R.drawable.watermelon);
        dishImages.add(R.drawable.sb);
        dishImages.add(R.drawable.orange);
        dishImages.add(R.drawable.grape);
        dishImages.add(R.drawable.fruit_banana);

        // listener for the target image
        targetImage.setOnDragListener(new TargetDragListener());

        startNewRound(); // first round
    }

    private void animateFruits() {
        // load animation
        final Animation zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom);

        // delay between each animation
        long delayBetweenPlates = 0;

        secondPlate.setVisibility(View.VISIBLE);
        secondPlate.startAnimation(zoomIn);

        thirdPlate.postDelayed(() -> {
            thirdPlate.setVisibility(View.VISIBLE);
            thirdPlate.startAnimation(zoomIn);
        }, delayBetweenPlates);

        firstPlate.postDelayed(() -> {
            firstPlate.setVisibility(View.VISIBLE);
            firstPlate.startAnimation(zoomIn);
        }, 2 * delayBetweenPlates);

        forthPlate.postDelayed(() -> {
            forthPlate.setVisibility(View.VISIBLE);
            forthPlate.startAnimation(zoomIn);
        }, 3 * delayBetweenPlates);
    }

    private void animateHeart(ImageView heart) {
        // Load the heart floating animation
        Animation floatAwayAnimation = AnimationUtils.loadAnimation(this, R.anim.float_away);
        floatAwayAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                heart.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                heart.setVisibility(View.GONE); // hide the heart after the animation ends
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        heart.startAnimation(floatAwayAnimation);
    }

    private void animateStars() {
        ImageView[] stars = {
                findViewById(R.id.star1),
                findViewById(R.id.star2),
                findViewById(R.id.star3),
                findViewById(R.id.star4),
                findViewById(R.id.star5),
                findViewById(R.id.star6)
        };


        int delay = 500; // for each star

        // Loop through each star to animate
        for (int i = 0; i < stars.length; i++) {
            ImageView star = stars[i];

            // Use a Handler with the main Looper to delay the start of each star's animation
            int finalI = i;
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                star.setVisibility(View.VISIBLE);
                star.setTranslationX(new Random().nextInt(getResources().getDisplayMetrics().widthPixels));

                Animation fallingStarsAnimation = AnimationUtils.loadAnimation(this, R.anim.star_fall);
                fallingStarsAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        star.setVisibility(View.VISIBLE); // Make sure the star is visible
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        star.setVisibility(View.INVISIBLE); // Hide the star after it falls
                        // Check if it's the last star and navigate to the main menu
                        if (finalI == stars.length - 1) {
                            Intent intent = new Intent(ConnectActivity.this, ChildActivity.class);
                            startActivity(intent);
                            finish(); // Close the current activity
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

                // Start the animation
                star.startAnimation(fallingStarsAnimation);

            }, delay * i); // increase delay for each star
        }

        // Transition back to the main menu after all animations
//        new Handler(Looper.getMainLooper()).postDelayed(() -> {
//            Intent intent = new Intent(ConnectActivity.this, ChildActivity.class);
//            startActivity(intent);
//            finish(); // Close the current activity
//        }, delay * stars.length + 500); // Extra delay before transitioning
    }


    private void startNewRound() {
        Collections.shuffle(dishImages, new Random());  // Shuffle the images

        // Set images to plates
        firstPlate.setImageResource(dishImages.get(0));
        secondPlate.setImageResource(dishImages.get(1));
        thirdPlate.setImageResource(dishImages.get(2));
        forthPlate.setImageResource(dishImages.get(3));

        animateFruits();

        // Set up a random target image
        List<Integer> choices = new ArrayList<>();
        choices.add(dishImages.get(0));
        choices.add(dishImages.get(1));
        choices.add(dishImages.get(2));
        choices.add(dishImages.get(3));

        Collections.shuffle(choices, new Random());
        targetImageRes = choices.get(0); // Store the target image resource
        targetImage.setImageResource(targetImageRes);

        // Re-enable and reset appearance for all plates
        resetPlates();

        // Set touch listeners for drag events
        setPlateDragListener(firstPlate, dishImages.get(0));
        setPlateDragListener(secondPlate, dishImages.get(1));
        setPlateDragListener(thirdPlate, dishImages.get(2));
        setPlateDragListener(forthPlate, dishImages.get(3));

        //resultTextView.setText("");  // Clear result for the new round
    }

    private void resetPlates() {
        // Re-enable all plates
        firstPlate.setEnabled(true);
        secondPlate.setEnabled(true);
        thirdPlate.setEnabled(true);
        forthPlate.setEnabled(true);

        // Reset the opacity of all plates to fully visible
        firstPlate.setAlpha(1.0f);
        secondPlate.setAlpha(1.0f);
        thirdPlate.setAlpha(1.0f);
        forthPlate.setAlpha(1.0f);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setPlateDragListener(ImageView plate, int imageRes) {
        plate.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN && v.isEnabled()) {
                // Start the drag event
                ClipData.Item item = new ClipData.Item(String.valueOf(imageRes));
                ClipData dragData = new ClipData(
                        String.valueOf(imageRes),
                        new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN},
                        item
                );

                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDragAndDrop(dragData, shadowBuilder, v, 0);

                return true;
            }
            return false;
        });
    }

    private void playSoundForTarget(int targetImageRes) {
        String soundName = getResources().getResourceEntryName(targetImageRes); // Get the name of the drawable resource
        int soundResId = getResources().getIdentifier(soundName, "raw", getPackageName()); // Get the sound resource ID

        if (soundResId != 0) { // Check if the sound resource exists
            MediaPlayer mediaPlayer = MediaPlayer.create(this, soundResId);
            // Release resources once the sound has finished playing
            mediaPlayer.setOnCompletionListener(MediaPlayer::release);
            mediaPlayer.start(); // Play the sound
        }
    }


    private class TargetDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            final int action = event.getAction();

            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;

                case DragEvent.ACTION_DRAG_ENTERED:
                    return true;

                case DragEvent.ACTION_DROP:
                    ClipData.Item item = event.getClipData().getItemAt(0);
                    int draggedImageRes = Integer.parseInt(item.getText().toString());

                    if (draggedImageRes == targetImageRes) {
                        //resultTextView.setText("Correct!");
                        animateHeart(heart);
                        playSoundForTarget(targetImageRes);
                        currentRound++;

                        if (currentRound > totalRounds) {
                            animateStars();
                        } else {
                            startNewRound();
                        }

                    } else {
                        //resultTextView.setText("Incorrect!");

                        // Disable the dragged plate (you can find it by the tag)
                        View draggedView = (View) event.getLocalState();
                        draggedView.setEnabled(false);
                        draggedView.setAlpha(0.5f); // Visual feedback for disabled plate
                    }
                    return true;

                case DragEvent.ACTION_DRAG_ENDED:
                    return true;

                default:
                    break;
            }

            return false;
        }
    }
}
