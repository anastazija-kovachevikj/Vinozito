package finki.nichk.mobile.activities.child;

import android.animation.ObjectAnimator;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_coloring);


        coloringImageView = findViewById(R.id.coloring_image);
        ImageButton backButton = findViewById(R.id.back_button);
        loadingFlower = findViewById(R.id.loading_flower);

        // rotating flower GIF
        loadingFlower.setVisibility(View.VISIBLE);

        int drawableId = getIntent().getIntExtra("image_resource", -1);

        if (drawableId != -1) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableId, options);

            // Process image on a background thread
            new Thread(() -> {
                preprocessedBitmap = preprocessImage(bitmap);
                runOnUiThread(() -> {
                    coloredBitmap = Bitmap.createBitmap(preprocessedBitmap.getWidth(), preprocessedBitmap.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(coloredBitmap);
                    canvas.drawBitmap(preprocessedBitmap, 0, 0, null);
                    coloringImageView.setImageBitmap(coloredBitmap);
                    loadingFlower.setVisibility(View.GONE);
                });
            }).start();

            coloringImageView.setOnTouchListener(this::onTouch);
            setupColorButtons();
            //setupResetButton();

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
        ImageButton darktblueButton = findViewById(R.id.color_darkblue);
        ImageButton pinkButton = findViewById(R.id.color_pink);
        View selectedColorView = findViewById(R.id.crayons);

        redButton.setOnClickListener(v -> handleColorButtonClick(redButton, Color.RED, selectedColorView, 0.3f));
        orangeButton.setOnClickListener(v -> handleColorButtonClick(orangeButton, Color.parseColor("#FFA500"), selectedColorView, 0.4f));
        yellowButton.setOnClickListener(v -> handleColorButtonClick(yellowButton, Color.YELLOW, selectedColorView, 0.5f));
        greenButton.setOnClickListener(v -> handleColorButtonClick(greenButton, Color.GREEN, selectedColorView, 0.6f));
        purpleButton.setOnClickListener(v -> handleColorButtonClick(purpleButton, Color.parseColor("#800080"), selectedColorView, 0.7f));
        lightblueButton.setOnClickListener(v -> handleColorButtonClick(lightblueButton, Color.parseColor("#ADD8E6"), selectedColorView, 0.4f));
        darktblueButton.setOnClickListener(v -> handleColorButtonClick(darktblueButton, Color.BLUE, selectedColorView, 0.5f));
        pinkButton.setOnClickListener(v -> handleColorButtonClick(pinkButton, Color.parseColor("#FFC0CB"), selectedColorView, 0.6f));
    }

    private void handleColorButtonClick(ImageButton clickedButton, int color, View selectedColorView, float blendRatio) {
        float slideDistance = -50f; // how far it slides left

        if (currentOpenCrayon != null && currentOpenCrayon != clickedButton) {
            // slide back the previous crayon to its original position
            ObjectAnimator slideBackAnimator = ObjectAnimator.ofFloat(currentOpenCrayon, "translationX", currentOpenCrayon.getTranslationX(), 0f);
            slideBackAnimator.setDuration(300);
            slideBackAnimator.start();
        }

        if (currentOpenCrayon == clickedButton) {
            // If the clicked crayon is already the current open crayon, slide it back to its original position
            ObjectAnimator slideBackAnimator = ObjectAnimator.ofFloat(clickedButton, "translationX", clickedButton.getTranslationX(), 0f);
            slideBackAnimator.setDuration(300);
            slideBackAnimator.start();
            currentOpenCrayon = null; // No crayon is currently open
        } else {
            // slide the clicked crayon to the left
            ObjectAnimator slideOutAnimator = ObjectAnimator.ofFloat(clickedButton, "translationX", clickedButton.getTranslationX(), slideDistance);
            slideOutAnimator.setDuration(300);
            slideOutAnimator.start();

            currentOpenCrayon = clickedButton;
        }

        currentColor = ColorUtils.blendARGB(color, Color.WHITE, blendRatio);
//        selectedColorView.setBackgroundColor(currentColor);
    }


    private void resetImage() {
        if (preprocessedBitmap != null) {
            coloredBitmap = Bitmap.createBitmap(preprocessedBitmap.getWidth(), preprocessedBitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(coloredBitmap);
            canvas.drawBitmap(preprocessedBitmap, 0, 0, null);
            coloringImageView.setImageBitmap(coloredBitmap);
        }
    }

//    private void setupResetButton() {
//        ImageButton resetButton = findViewById(R.id.reset_btn);
//        resetButton.setOnClickListener(v -> resetImage());
//    }

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


    private Bitmap preprocessImage(Bitmap bitmap) {
        Bitmap processedBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        //Canvas canvas = new Canvas(processedBitmap);
        int threshold = 50; // Threshold for detecting black pixels
        int lightGray = Color.rgb(254, 254, 254);

        for (int x = 0; x < bitmap.getWidth(); x++) {
            for (int y = 0; y < bitmap.getHeight(); y++) {
                int pixel = bitmap.getPixel(x, y);
                int red = Color.red(pixel);
                int green = Color.green(pixel);
                int blue = Color.blue(pixel);

                if (red < threshold && green < threshold && blue < threshold) {
                    // mark the outline as black
                    processedBitmap.setPixel(x, y, Color.BLACK);
                } else {
                    processedBitmap.setPixel(x, y, Color.WHITE);
                }
            }
        }

        for (int x = 0; x < processedBitmap.getWidth(); x++) {
            floodFillOutside(processedBitmap, x, 0, Color.WHITE, lightGray); // Top edge
            floodFillOutside(processedBitmap, x, processedBitmap.getHeight() - 1, Color.WHITE, lightGray); // Bottom edge
        }

        for (int y = 0; y < processedBitmap.getHeight(); y++) {
            floodFillOutside(processedBitmap, 0, y, Color.WHITE, lightGray); // Left edge
            floodFillOutside(processedBitmap, processedBitmap.getWidth() - 1, y, Color.WHITE, lightGray); // Right edge
        }

        return processedBitmap;
    }

    private void floodFillOutside(Bitmap bitmap, int x, int y, int targetColor, int replaceColor) {
        if (x < 0 || x >= bitmap.getWidth() || y < 0 || y >= bitmap.getHeight()) return;
        if (bitmap.getPixel(x, y) != targetColor) return;

        Queue<ColorPaintingActivity.Point> queue = new LinkedList<>();
        queue.add(new ColorPaintingActivity.Point(x, y));

        while (!queue.isEmpty()) {
            ColorPaintingActivity.Point point = queue.poll();
            int px = point.x;
            int py = point.y;

            if (px < 0 || py < 0 || px >= bitmap.getWidth() || py >= bitmap.getHeight()) continue;

            int currentColor = bitmap.getPixel(px, py);
            if (currentColor == targetColor) {
                bitmap.setPixel(px, py, replaceColor);

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
