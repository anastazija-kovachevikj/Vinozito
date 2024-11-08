package finki.nichk.mobile.activities.child;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;

import finki.nichk.R;
import finki.nichk.mobile.activities.ImageProcessor;


public class ChoosePaintingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_coloring_menu);

        ImageProcessor imageProcessor = new ImageProcessor(ChoosePaintingActivity.this);

        ImageButton imageButton1 = findViewById(R.id.imageButton_ladybug);
        ImageButton imageButton2 = findViewById(R.id.imageButton_horse);
        ImageButton imageButton3 = findViewById(R.id.imageButton_cake);
        ImageButton imageButton4 = findViewById(R.id.imageButton_butterfly);
        ImageButton imageButton5 = findViewById(R.id.imageButton_rainbow);
        ImageButton imageButton6 = findViewById(R.id.imageButton_cow);
        ImageButton imageButton7 = findViewById(R.id.imageButton_umbrella);
        ImageButton imageButton8 = findViewById(R.id.imageButton_hat);
        ImageButton backButton = findViewById(R.id.back_button);

        Bitmap bitmapLadybug = imageProcessor.loadImageFromAssets("processed_image_0.png");
        Bitmap bitmapHorse = imageProcessor.loadImageFromAssets("processed_image_1.png");
        Bitmap bitmapCake = imageProcessor.loadImageFromAssets("processed_image_2.png");
        Bitmap bitmapRainbow = imageProcessor.loadImageFromAssets("processed_image_3.png");
        Bitmap bitmapHat = imageProcessor.loadImageFromAssets("processed_image_4.png");
        Bitmap bitmapUmbrella = imageProcessor.loadImageFromAssets("processed_image_5.png");
        Bitmap bitmapCow = imageProcessor.loadImageFromAssets("processed_image_6.png");
        Bitmap bitmapButterfly = imageProcessor.loadImageFromAssets("processed_image_7.png");
        Bitmap bitmapWorm = imageProcessor.loadImageFromAssets("processed_image_8.png");
        Bitmap bitmapCandy = imageProcessor.loadImageFromAssets("processed_image_9.png");
        Bitmap bitmapFlower = imageProcessor.loadImageFromAssets("processed_image_10.png");
        Bitmap bitmapRocket = imageProcessor.loadImageFromAssets("processed_image_11.png");

        imageButton1.setOnClickListener(v -> openColoringScreen(bitmapWorm));
        imageButton2.setOnClickListener(v -> openColoringScreen(bitmapLadybug));
        imageButton3.setOnClickListener(v -> openColoringScreen(bitmapRainbow));
        imageButton4.setOnClickListener(v -> openColoringScreen(bitmapRocket));
        imageButton5.setOnClickListener(v -> openColoringScreen(bitmapCake));
        imageButton6.setOnClickListener(v -> openColoringScreen(bitmapCandy));
        imageButton7.setOnClickListener(v -> openColoringScreen(bitmapFlower));
        imageButton8.setOnClickListener(v -> openColoringScreen(bitmapButterfly));

//        imageButton1.setOnClickListener(v -> openColoringScreen(R.drawable.ladybug));
//        imageButton2.setOnClickListener(v -> openColoringScreen(R.drawable.dino));
//        imageButton3.setOnClickListener(v -> openColoringScreen(R.drawable.cake));
//        imageButton4.setOnClickListener(v -> openColoringScreen(R.drawable.butterfly));
//        imageButton5.setOnClickListener(v -> openColoringScreen(R.drawable.rainbow));
//        imageButton5.setOnClickListener(v -> openColoringScreen(R.drawable.umbrella));
//        imageButton8.setOnClickListener(v -> openColoringScreen(R.drawable.cow));
//        imageButton7.setOnClickListener(v -> openColoringScreen(R.drawable.hat));
//        imageButton10.setOnClickListener(v -> openColoringScreen(R.drawable.horse));

        buttonTouchListener(backButton, () -> {
            Intent intent = new Intent(ChoosePaintingActivity.this, ChooseChildActivity.class);
            startActivity(intent);
        });
    }

    private void openColoringScreen(Bitmap bitmap) {
        Intent intent = new Intent(this, ColorPaintingActivity.class);

        // Bitmap to byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        intent.putExtra("image_png", byteArray);
        startActivity(intent);
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
}
