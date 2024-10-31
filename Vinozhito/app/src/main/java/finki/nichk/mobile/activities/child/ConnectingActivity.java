package finki.nichk.mobile.activities.child;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import finki.nichk.R;
import finki.nichk.mobile.activities.child.ChooseChildActivity;

public class ConnectingActivity extends AppCompatActivity {
    private ImageView targetImage;
    private ImageView bee;
    private List<Integer> dishImages;
    private int targetImageRes;
    private ImageView firstBranch, secondBranch, thirdBranch, forthBranch;
    private ImageView beeReaction;
    private int currentRound = 1; // current round
    private final int totalRounds = 5; // number of rounds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_connecting); //tablet_connecting_main_menu

        targetImage = findViewById(R.id.targetImage);
        beeReaction = findViewById(R.id.bee_reaction);
        bee = findViewById(R.id.imageViewBee);

        //resultTextView = findViewById(R.id.resultTextView);
        ImageButton backButton = findViewById(R.id.back_button);

        buttonTouchListener(backButton, () -> {
            Intent intent = new Intent(this, ChooseChildActivity.class);
            startActivity(intent);
        });

        firstBranch = findViewById(R.id.firstBranch);
        secondBranch = findViewById(R.id.secondBranch);
        thirdBranch = findViewById(R.id.thirdBranch);
        forthBranch = findViewById(R.id.forthBarnch);

        // Load dish images
        dishImages = new ArrayList<>();
        dishImages.add(R.drawable.fruit_lemon);
        dishImages.add(R.drawable.fruit_pear);
        dishImages.add(R.drawable.watermelon);
        dishImages.add(R.drawable.sb);
        dishImages.add(R.drawable.orange);
        dishImages.add(R.drawable.grape);
        dishImages.add(R.drawable.fruit_banana);
        dishImages.add(R.drawable.apple);
        dishImages.add(R.drawable.pineapple);
        dishImages.add(R.drawable.cherry);

        // listener for the target image
        //targetImage.setOnDragListener(new ConnectingActivity.TargetDragListener());
        bee.setOnDragListener(new TargetDragListener());

        startNewRound(); // first round

        MediaPlayer mediaPlayer = MediaPlayer.create(ConnectingActivity.this, R.raw.instructions2);
        if (mediaPlayer != null) {
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(MediaPlayer::release);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void buttonTouchListener(ImageButton button, Runnable onClickAction) {
        button.setOnClickListener(v -> onClickAction.run());

        button.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: // transparent when pressed
                    v.setAlpha(0.5f);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL: // back to normal released or canceled
                    v.setAlpha(1.0f);
                    break;
            }
            return false;
        });
    }

    private void animateFruits() {
        final Animation zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom);

        long delayBetweenBranchs = 0;

        secondBranch.setVisibility(View.VISIBLE);
        secondBranch.startAnimation(zoomIn);

        thirdBranch.postDelayed(() -> {
            thirdBranch.setVisibility(View.VISIBLE);
            thirdBranch.startAnimation(zoomIn);
        }, delayBetweenBranchs);

        firstBranch.postDelayed(() -> {
            firstBranch.setVisibility(View.VISIBLE);
            firstBranch.startAnimation(zoomIn);
        }, 2 * delayBetweenBranchs);

        forthBranch.postDelayed(() -> {
            forthBranch.setVisibility(View.VISIBLE);
            forthBranch.startAnimation(zoomIn);
        }, 3 * delayBetweenBranchs);
    }

    private void animateReaction(boolean isCorrect) {
        ImageView react = findViewById(R.id.bee_reaction);

        if (isCorrect) {
            react.setImageResource(R.drawable.hearts);

        } else {
            react.setImageResource(R.drawable.wrong);
        }

        react.setVisibility(View.VISIBLE);

        Animation reactAnimation = AnimationUtils.loadAnimation(this, R.anim.float_away); // reuse the same animation
        react.startAnimation(reactAnimation);

        reactAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                react.setImageResource(R.drawable.hearts);  // reset to default heart image
                react.setVisibility(View.INVISIBLE); // hide again after the animation
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        react.startAnimation(reactAnimation);
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

        int delay = 500; // delay for each star

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
                        star.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        star.setVisibility(View.INVISIBLE); // hide

                        if (finalI == stars.length - 1) { // on the last star go back to menu
                            Intent intent = new Intent(ConnectingActivity.this, ChooseChildActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

                star.startAnimation(fallingStarsAnimation); // start animation

            }, delay * i); // increase delay for each star
        }
    }

    private void startNewRound() {
        Collections.shuffle(dishImages, new Random());  // shuffle

        // set images to Branchs
        firstBranch.setImageResource(dishImages.get(0));
        secondBranch.setImageResource(dishImages.get(1));
        thirdBranch.setImageResource(dishImages.get(2));
        forthBranch.setImageResource(dishImages.get(3));

        animateFruits();

        // random target image
        List<Integer> choices = new ArrayList<>();
        choices.add(dishImages.get(0));
        choices.add(dishImages.get(1));
        choices.add(dishImages.get(2));
        choices.add(dishImages.get(3));

        Collections.shuffle(choices, new Random());
        targetImageRes = choices.get(0); // target image resource
        targetImage.setImageResource(targetImageRes);

        resetBranchs();

        setBranchDragListener(firstBranch, dishImages.get(0));
        setBranchDragListener(secondBranch, dishImages.get(1));
        setBranchDragListener(thirdBranch, dishImages.get(2));
        setBranchDragListener(forthBranch, dishImages.get(3));
    }

    private void resetBranchs() {

        firstBranch.setEnabled(true);
        secondBranch.setEnabled(true);
        thirdBranch.setEnabled(true);
        forthBranch.setEnabled(true);

        firstBranch.setAlpha(1.0f);
        secondBranch.setAlpha(1.0f);
        thirdBranch.setAlpha(1.0f);
        forthBranch.setAlpha(1.0f);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setBranchDragListener(ImageView Branch, int imageRes) {
        Branch.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN && v.isEnabled()) {

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
        String soundName = getResources().getResourceEntryName(targetImageRes); // name from drawable
        int soundResId = getResources().getIdentifier(soundName, "raw", getPackageName());

        if (soundResId != 0) {
            MediaPlayer mediaPlayer = MediaPlayer.create(this, soundResId);
            mediaPlayer.setOnCompletionListener(MediaPlayer::release);
            mediaPlayer.start(); // play sound
        }
    }

    private class TargetDragListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            final int action = event.getAction();

            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;

                case DragEvent.ACTION_DROP:
                    ClipData.Item item = event.getClipData().getItemAt(0);
                    int draggedImageRes = Integer.parseInt(item.getText().toString());

                    if (draggedImageRes == targetImageRes) { // check if the dragged fruit matches the target image
                        // Trigger correct animation and sounds
                        animateReaction(true);
                        playSoundForTarget(targetImageRes);
                        currentRound++;

                        if (currentRound > totalRounds) { // Start a new round or show the stars animation
                            animateStars();
                        } else {
                            startNewRound();
                        }
                    } else { // incorrect answer
                        MediaPlayer mediaPlayer = MediaPlayer.create(ConnectingActivity.this, R.raw.try_again);
                        if (mediaPlayer != null) {
                            mediaPlayer.start();
                            mediaPlayer.setOnCompletionListener(MediaPlayer::release);
                        }

                        animateReaction(false);

                        // Disable the dragged fruit image for further dragging
                        View draggedView = (View) event.getLocalState();
                        draggedView.setEnabled(false);
                        draggedView.setAlpha(0.5f);
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
