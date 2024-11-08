package finki.nichk.mobile.activities.child;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.ColorUtils;

import java.util.LinkedList;
import java.util.Queue;

import finki.nichk.R;
import pl.droidsonroids.gif.GifTextView;

public class ColorPaintingActivity extends AppCompatActivity {

    private ImageView coloringImageView;
    private Bitmap coloredBitmap;
    private Bitmap preprocessedBitmap;
    private int currentColor = Color.WHITE; // default
    private GifTextView loadingFlower;
    private ImageButton currentOpenCrayon = null;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_coloring);

        coloringImageView = findViewById(R.id.coloring_image);
        loadingFlower = findViewById(R.id.loading_flower);
        ImageButton backButton = findViewById(R.id.back_button);

        loadingFlower.setVisibility(View.VISIBLE);

        byte[] byteArray = getIntent().getByteArrayExtra("image_png"); // byte array passed via Intent

        if (byteArray != null) {

            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length); // byte array into a Bitmap

            new Thread(() -> {
                preprocessedBitmap = bitmap; // set the preprocessed bitmap -> to be fixed - redundant
                runOnUiThread(() -> {
                    // mutable bitmap to draw on
                    coloredBitmap = Bitmap.createBitmap(preprocessedBitmap.getWidth(), preprocessedBitmap.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(coloredBitmap);
                    canvas.drawBitmap(preprocessedBitmap, 0, 0, null);

                    coloringImageView.setImageBitmap(coloredBitmap);

                    // hide flower gif
                    loadingFlower.setVisibility(View.GONE);
                });
            }).start();

            coloringImageView.setOnTouchListener(this::onTouch);
            setupColorButtons();
            setupResetButton();

        } else {
            coloringImageView.setImageResource(R.drawable.unknown);
        }

        backButton.setOnClickListener(v -> finish());
    }

    private void setupColorButtons() {
        ImageButton redButton = findViewById(R.id.color_red);
        ImageButton orangeButton = findViewById(R.id.color_orange);
        ImageButton yellowButton = findViewById(R.id.color_yellow);
        ImageButton greenButton = findViewById(R.id.color_green);
        ImageButton purpleButton = findViewById(R.id.color_purple);
        ImageButton lightblueButton = findViewById(R.id.color_lightblue);
        ImageButton darkblueButton = findViewById(R.id.color_darkblue);
        ImageButton pinkButton = findViewById(R.id.color_pink);
        ImageButton whiteButton = findViewById(R.id.color_white);
        ImageButton blackButton = findViewById(R.id.color_blackish);
        ImageButton brownButton = findViewById(R.id.color_brown);
        View selectedColorView = findViewById(R.id.crayons);

        redButton.setOnClickListener(v -> handleColorButtonClick(redButton, Color.RED, selectedColorView, 0.3f));
        orangeButton.setOnClickListener(v -> handleColorButtonClick(orangeButton, Color.parseColor("#FFA500"), selectedColorView, 0.4f));
        yellowButton.setOnClickListener(v -> handleColorButtonClick(yellowButton, Color.YELLOW, selectedColorView, 0.5f));
        greenButton.setOnClickListener(v -> handleColorButtonClick(greenButton, Color.GREEN, selectedColorView, 0.6f));
        purpleButton.setOnClickListener(v -> handleColorButtonClick(purpleButton, Color.parseColor("#800080"), selectedColorView, 0.5f));
        lightblueButton.setOnClickListener(v -> handleColorButtonClick(lightblueButton, Color.parseColor("#ADD8E6"), selectedColorView, 0.4f));
        darkblueButton.setOnClickListener(v -> handleColorButtonClick(darkblueButton, Color.BLUE, selectedColorView, 0.5f));
        pinkButton.setOnClickListener(v -> handleColorButtonClick(pinkButton, Color.parseColor("#FFC0CB"), selectedColorView, 0.0f));
        whiteButton.setOnClickListener(v -> handleColorButtonClick(whiteButton, Color.WHITE, selectedColorView, 0.0f));
        blackButton.setOnClickListener(v -> handleColorButtonClick(blackButton, Color.BLACK, selectedColorView, 0.5f));
        brownButton.setOnClickListener(v -> handleColorButtonClick(brownButton, Color.parseColor("#836343"), selectedColorView, 0.1f));
    }

    private void handleColorButtonClick(ImageButton clickedButton, int color, View selectedColorView, float blendRatio) {
        float slideDistance = -50f; // to slide up

        if (currentOpenCrayon != null && currentOpenCrayon != clickedButton) {
            // slide back the previous crayon to its original position
            ObjectAnimator slideBackAnimator = ObjectAnimator.ofFloat(currentOpenCrayon, "translationY", currentOpenCrayon.getTranslationY(), 0f);
            slideBackAnimator.setDuration(300);
            slideBackAnimator.start();
        }

        if (currentOpenCrayon == clickedButton) {
            // if the clicked crayon is already the current open crayon, slide it back to its original position
            ObjectAnimator slideBackAnimator = ObjectAnimator.ofFloat(clickedButton, "translationY", clickedButton.getTranslationY(), 0f);
            slideBackAnimator.setDuration(300);
            slideBackAnimator.start();
            currentOpenCrayon = null; // No crayon is currently open
        } else {
            // slide clicked crayon up
            ObjectAnimator slideOutAnimator = ObjectAnimator.ofFloat(clickedButton, "translationY", clickedButton.getTranslationY(), slideDistance);
            slideOutAnimator.setDuration(300);
            slideOutAnimator.start();

            currentOpenCrayon = clickedButton;
        }
        currentColor = ColorUtils.blendARGB(color, Color.WHITE, blendRatio);
    }

    private void resetImage() {
        if (preprocessedBitmap != null) {
            coloredBitmap = Bitmap.createBitmap(preprocessedBitmap.getWidth(), preprocessedBitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(coloredBitmap);
            canvas.drawBitmap(preprocessedBitmap, 0, 0, null);
            coloringImageView.setImageBitmap(coloredBitmap);
        }
    }

    private void setupResetButton() {
        ImageButton resetButton = findViewById(R.id.reset_btn);
        resetButton.setOnClickListener(v -> resetImage());
    }

    private boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float touchX = event.getX();
            float touchY = event.getY();

            float[] values = new float[9];
            coloringImageView.getImageMatrix().getValues(values);
            float scaleX = values[Matrix.MSCALE_X];
            float scaleY = values[Matrix.MSCALE_Y];

            int drawableWidth = coloringImageView.getDrawable().getIntrinsicWidth();
            int drawableHeight = coloringImageView.getDrawable().getIntrinsicHeight();

            int actualX = (int) ((touchX - values[Matrix.MTRANS_X]) / scaleX);
            int actualY = (int) ((touchY - values[Matrix.MTRANS_Y]) / scaleY);

            if (actualX >= 0 && actualX < drawableWidth && actualY >= 0 && actualY < drawableHeight) {
                handleTouch(actualX, actualY);
            }
        }
        return true;
    }

    private void handleTouch(int x, int y) {
        if (coloredBitmap != null) {
            new Thread(() -> {
                floodFill(coloredBitmap, x, y, currentColor);
                runOnUiThread(() -> coloringImageView.invalidate());
            }).start();
        }
    }

    private void floodFill(Bitmap bitmap, int x, int y, int newColor) {
        int targetColor = bitmap.getPixel(x, y);
        int lightGray = Color.rgb(254, 254, 254);  //very light gray color

        if (targetColor == newColor || targetColor == lightGray) return;

        Queue<ColorPaintingActivity.Point> queue = new LinkedList<>();
        queue.add(new ColorPaintingActivity.Point(x, y));

        while (!queue.isEmpty()) {
            ColorPaintingActivity.Point point = queue.poll();
            int px = point.x;
            int py = point.y;

            // Skip if out of bounds
            if (px < 0 || py < 0 || px >= bitmap.getWidth() || py >= bitmap.getHeight()) continue;

            int currentColor = bitmap.getPixel(px, py);

            if (currentColor == targetColor && currentColor != Color.BLACK && currentColor != lightGray) {
                bitmap.setPixel(px, py, newColor);

                queue.add(new ColorPaintingActivity.Point(px + 1, py));
                queue.add(new ColorPaintingActivity.Point(px - 1, py));
                queue.add(new ColorPaintingActivity.Point(px, py + 1));
                queue.add(new ColorPaintingActivity.Point(px, py - 1));
            }
        }
    }

    private static class Point {
        int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
