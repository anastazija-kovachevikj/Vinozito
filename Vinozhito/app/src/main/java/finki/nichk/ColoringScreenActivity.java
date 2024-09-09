package finki.nichk;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedList;
import java.util.Queue;

import androidx.core.graphics.ColorUtils;

public class ColoringScreenActivity extends AppCompatActivity {

    private ImageView coloringImageView;
    private Bitmap coloredBitmap;
    private Bitmap preprocessedBitmap;
    private int currentColor = Color.WHITE; // default

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coloring);

        coloringImageView = findViewById(R.id.coloring_image);
        ImageButton backButton = findViewById(R.id.back_button);

        //drawable ID from the intent
        int drawableId = getIntent().getIntExtra("image_resource", -1);

        if (drawableId != -1) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableId, options);
            preprocessedBitmap = preprocessImage(bitmap); // to ensure only black and white

            coloredBitmap = Bitmap.createBitmap(preprocessedBitmap);

            coloringImageView.setImageBitmap(coloredBitmap);

            // touch listener for coloring
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

        redButton.setOnClickListener(v -> {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) redButton.getLayoutParams();
            params.leftMargin = 18; // move into view (test)
            redButton.setLayoutParams(params);
            currentColor = ColorUtils.blendARGB(Color.RED, Color.WHITE, 0.5f); // blending colors
        });

        greenButton.setOnClickListener(v -> {
            currentColor = ColorUtils.blendARGB(Color.GREEN, Color.WHITE, 0.5f);
        });

        blueButton.setOnClickListener(v -> {
            currentColor = ColorUtils.blendARGB(Color.BLUE, Color.WHITE, 0.5f);
        });
    }

    private void resetImage() {
        coloredBitmap = Bitmap.createBitmap(preprocessedBitmap);
        coloringImageView.setImageBitmap(coloredBitmap);
    }

    private void setupResetButton() {
        ImageButton resetButton = findViewById(R.id.reset_btn);
        resetButton.setOnClickListener(v -> resetImage());
    }


    private boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // touch coordinates relative to the ImageView
            float touchX = event.getX();
            float touchY = event.getY();

            // Get the image matrix scaling values (if image is scaled inside the ImageView)
            float[] values = new float[9];
            coloringImageView.getImageMatrix().getValues(values);
            float scaleX = values[Matrix.MSCALE_X];
            float scaleY = values[Matrix.MSCALE_Y];

            int drawableWidth = coloringImageView.getDrawable().getIntrinsicWidth();
            int drawableHeight = coloringImageView.getDrawable().getIntrinsicHeight();

            // the bitmap's position within the ImageView
            int actualX = (int) ((touchX - values[Matrix.MTRANS_X]) / scaleX);
            int actualY = (int) ((touchY - values[Matrix.MTRANS_Y]) / scaleY);

            // make sure the touch coordinates are within the bitmap bounds
            if (actualX >= 0 && actualX < drawableWidth && actualY >= 0 && actualY < drawableHeight) {
                handleTouch(actualX, actualY); // Pass the adjusted coordinates to the flood fill method
            }
        }
        return true;
    }


    private void handleTouch(int x, int y) { // FLOOD FILL
        if (coloredBitmap == null) return;

        // bounds check
        if (x < 0 || y < 0 || x >= coloredBitmap.getWidth() || y >= coloredBitmap.getHeight())
            return;

        floodFill(coloredBitmap, x, y, currentColor);
        coloringImageView.invalidate(); // refresh ImageView
    }

    private void floodFill(Bitmap bitmap, int x, int y, int newColor) {
        int targetColor = bitmap.getPixel(x, y);

        // if the pixel is black do not flood fill
        if (targetColor == newColor || targetColor == Color.BLACK) return;

        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point(x, y));

        while (!queue.isEmpty()) {
            Point point = queue.poll();
            assert point != null;
            int px = point.x;
            int py = point.y;

            // bounds check
            if (px < 0 || py < 0 || px >= bitmap.getWidth() || py >= bitmap.getHeight()) continue;

            int currentColor = bitmap.getPixel(px, py);

            if (currentColor == targetColor) {
                bitmap.setPixel(px, py, newColor);

                queue.add(new Point(px + 1, py)); // Right
                queue.add(new Point(px - 1, py)); // Left
                queue.add(new Point(px, py + 1)); // Down
                queue.add(new Point(px, py - 1)); // Up
            }
        }
    }

    private Bitmap preprocessImage(Bitmap bitmap) {
        Bitmap processedBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        // threshold for "almost black"
        int threshold = 50;

        // iterate through all pixels and turn almost black into pure black, and everything else to white
        for (int x = 0; x < processedBitmap.getWidth(); x++) {
            for (int y = 0; y < processedBitmap.getHeight(); y++) {
                int pixel = processedBitmap.getPixel(x, y);

                // extract RGB values
                int red = Color.red(pixel);
                int green = Color.green(pixel);
                int blue = Color.blue(pixel);

                // calculate brightness to determine if the pixel is close to black
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
