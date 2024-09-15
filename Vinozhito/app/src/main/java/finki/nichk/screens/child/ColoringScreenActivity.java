package finki.nichk.screens.child;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.ColorUtils;

import java.util.LinkedList;
import java.util.Queue;

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
        loadingFlower = findViewById(R.id.loading_flower);

        // Initially show the rotating flower GIF
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
            setupResetButton();

        } else {
            coloringImageView.setImageResource(R.drawable.unknown);
        }

        backButton.setOnClickListener(v -> finish());
    }

    private void setupColorButtons() {
        ImageButton redButton = findViewById(R.id.color_red);
        ImageButton greenButton = findViewById(R.id.color_green);
        ImageButton blueButton = findViewById(R.id.color_blue);

        redButton.setOnClickListener(v -> currentColor = ColorUtils.blendARGB(Color.RED, Color.WHITE, 0.5f));
        greenButton.setOnClickListener(v -> currentColor = ColorUtils.blendARGB(Color.GREEN, Color.WHITE, 0.5f));
        blueButton.setOnClickListener(v -> currentColor = ColorUtils.blendARGB(Color.BLUE, Color.WHITE, 0.5f));
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
        if (targetColor == newColor || targetColor == Color.BLACK) return;

        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point(x, y));

        while (!queue.isEmpty()) {
            Point point = queue.poll();
            int px = point.x;
            int py = point.y;

            if (px < 0 || py < 0 || px >= bitmap.getWidth() || py >= bitmap.getHeight()) continue;

            int currentColor = bitmap.getPixel(px, py);

            if (currentColor == targetColor) {
                bitmap.setPixel(px, py, newColor);

                queue.add(new Point(px + 1, py));
                queue.add(new Point(px - 1, py));
                queue.add(new Point(px, py + 1));
                queue.add(new Point(px, py - 1));
            }
        }
    }

    private Bitmap preprocessImage(Bitmap bitmap) {
        Bitmap processedBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(processedBitmap);
        int threshold = 50;

        for (int x = 0; x < bitmap.getWidth(); x++) {
            for (int y = 0; y < bitmap.getHeight(); y++) {
                int pixel = bitmap.getPixel(x, y);
                int red = Color.red(pixel);
                int green = Color.green(pixel);
                int blue = Color.blue(pixel);

                if (red < threshold && green < threshold && blue < threshold) {
                    processedBitmap.setPixel(x, y, Color.BLACK);
                } else {
                    processedBitmap.setPixel(x, y, Color.WHITE);
                }
            }
        }
        return processedBitmap;
    }

    private static class Point {
        int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
