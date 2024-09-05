package finki.nichk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedList;
import java.util.Queue;

public class ColoringScreenActivity extends AppCompatActivity {

    private ImageView coloringImageView;
    private Bitmap originalBitmap;
    private Bitmap coloredBitmap;
    private int currentColor = Color.RED; // Default color

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coloring);

        coloringImageView = findViewById(R.id.coloring_image);
        ImageButton backButton = findViewById(R.id.back_button);

        int drawableId = getIntent().getIntExtra("image_resource", -1);

        if (drawableId != -1) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;

            originalBitmap = BitmapFactory.decodeResource(getResources(), drawableId, options);
            coloredBitmap = Bitmap.createBitmap(originalBitmap.getWidth(), originalBitmap.getHeight(), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(coloredBitmap);
            canvas.drawBitmap(originalBitmap, 0, 0, null);

            coloringImageView.setImageBitmap(coloredBitmap);

            // listener for coloring
            coloringImageView.setOnTouchListener(this::onTouch);

            setupColorButtons();
        } else {
            coloringImageView.setImageResource(R.drawable.unknown);
        }

        backButton.setOnClickListener(v -> finish());
    }

    private void setupColorButtons() {
        ImageButton redButton = findViewById(R.id.color_red);
        ImageButton greenButton = findViewById(R.id.color_green);
        ImageButton blueButton = findViewById(R.id.color_blue);

        redButton.setOnClickListener(v -> currentColor = Color.RED);
        greenButton.setOnClickListener(v -> currentColor = Color.GREEN);
        blueButton.setOnClickListener(v -> currentColor = Color.BLUE);
    }

    private boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            handleTouch((int) event.getX(), (int) event.getY());
        }
        return true;
    }

    private void handleTouch(int x, int y) {
        if (coloredBitmap == null) return;

        if (x < 0 || y < 0 || x >= coloredBitmap.getWidth() || y >= coloredBitmap.getHeight()) return;

        // Use a refined flood-fill algorithm to color the touched area
        floodFill(x, y, currentColor);

        coloringImageView.invalidate(); // Refresh the ImageView
    }

    private void floodFill(int startX, int startY, int color) {
        int width = coloredBitmap.getWidth();
        int height = coloredBitmap.getHeight();
        int targetColor = coloredBitmap.getPixel(startX, startY);

        if (targetColor == color || targetColor == Color.TRANSPARENT) return;

        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point(startX, startY));

        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);

        Canvas canvas = new Canvas(coloredBitmap);

        while (!queue.isEmpty()) {
            Point point = queue.poll();
            int x = point.x;
            int y = point.y;

            if (x < 0 || y < 0 || x >= width || y >= height) continue;
            if (coloredBitmap.getPixel(x, y) != targetColor) continue;

            // Set pixel color
            canvas.drawPoint(x, y, paint);

            // Add neighboring points
            queue.add(new Point(x + 1, y));
            queue.add(new Point(x - 1, y));
            queue.add(new Point(x, y + 1));
            queue.add(new Point(x, y - 1));
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
