package finki.nichk.screens.child;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.ColorUtils;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

import pl.droidsonroids.gif.GifTextView;
import finki.nichk.R;

public class ColoringScreenActivity extends AppCompatActivity {

    private ImageView coloringImageView;
    private Bitmap coloredBitmap;
    private Bitmap preprocessedBitmap;
    private int currentColor = Color.WHITE; // default
    private GifTextView loadingFlower;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coloring);

        coloringImageView = findViewById(R.id.coloring_image);
        ImageButton backButton = findViewById(R.id.back_button);
        int drawableId = getIntent().getIntExtra("image_resource", -1);

        //int drawableId = getIntent().getIntExtra("image_resource", -1);
        if (drawableId != -1) {

            Log.e("AAAA", String.valueOf(drawableId));

            try {
                VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat.create(getResources(), drawableId, getTheme());
                //Drawable vectorDrawable = vectorDrawableCompat.getCurrent();
                Drawable vectorDrawable = AppCompatResources.getDrawable(getApplicationContext(), drawableId);
                //coloringImageView.setImageResource(drawableId);
                //String resourceName = getResources().getResourceEntryName(drawableId);
                //Log.e("Vector Name", "Loaded image: " + resourceName);
                //Drawable vectorDrawable = VectorDrawableCompat.create(getResources(), drawableId, null);

                coloringImageView.setImageDrawable(vectorDrawable);
//                changePathColor(vectorDrawable, R.id.path2, getResources().getColor(R.color.babyblue));

            } catch (Resources.NotFoundException e) {
                Log.e("Vector", "Vector file not found: " + e.getMessage());
            }
        } else {
            Log.e("Vector", "Invalid resource ID passed");
        }

        setupColorButtons();
        setupResetButton();
        //coloringImageView.setOnTouchListener(this::onTouch);
        buttonTouchListener(backButton, () -> {
            Intent intent = new Intent(this, ChildActivity.class);
            startActivity(intent);
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

    private void setupColorButtons() {
        ImageButton redButton = findViewById(R.id.color_red);
        ImageButton greenButton = findViewById(R.id.color_green);
        ImageButton blueButton = findViewById(R.id.color_blue);

        redButton.setOnClickListener(v -> currentColor = ColorUtils.blendARGB(Color.RED, Color.WHITE, 0.5f));
        greenButton.setOnClickListener(v -> currentColor = ColorUtils.blendARGB(Color.GREEN, Color.WHITE, 0.5f));
        blueButton.setOnClickListener(v -> currentColor = ColorUtils.blendARGB(Color.BLUE, Color.WHITE, 0.5f));
    }

//    private void resetImage() {
//        if (preprocessedBitmap != null) {
//            coloredBitmap = Bitmap.createBitmap(preprocessedBitmap.getWidth(), preprocessedBitmap.getHeight(), Bitmap.Config.ARGB_8888);
//            Canvas canvas = new Canvas(coloredBitmap);
//            canvas.drawBitmap(preprocessedBitmap, 0, 0, null);
//            coloringImageView.setImageBitmap(coloredBitmap);
//        }
//    }


    private void resetImage() {
        int drawableId = getIntent().getIntExtra("image_resource", -1);
        if (drawableId != -1) {
            VectorDrawableCompat vectorDrawable = VectorDrawableCompat.create(getResources(), drawableId, null);
            coloringImageView.setImageDrawable(vectorDrawable);
        }
    }

    private void setupResetButton() {
        ImageButton resetButton = findViewById(R.id.reset_btn);
        resetButton.setOnClickListener(v -> resetImage());
    }

//    @SuppressLint("ClickableViewAccessibility")
//    private boolean onTouch(View view, MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            // Check if drawable is a VectorDrawable and handle the click event
//            if (vectorDrawable instanceof VectorDrawableCompat) {
//                // changes the color of the entire drawable
//                applyColorFilterToVectorDrawable((VectorDrawableCompat) vectorDrawable);
//            }
//        }
//        return true;
//    }


//    private void applyColorFilterToVectorDrawable(VectorDrawableCompat vectorDrawable) {
//        // Create a PorterDuffColorFilter with the current selected color
//        PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(currentColor, PorterDuff.Mode.SRC_IN);
//
//        // Apply the color filter to the vector drawable
//        vectorDrawable.setColorFilter(colorFilter);
//
//        // Redraw the vector on the ImageView
//        coloringImageView.invalidate();
//    }
}
