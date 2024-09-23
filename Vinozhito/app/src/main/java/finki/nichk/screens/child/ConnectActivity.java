package finki.nichk.screens.child;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import finki.nichk.R;

public class ConnectActivity extends AppCompatActivity {

    private ImageView targetImage;
    private GridLayout imageGrid;
    private List<Integer> imageResources; // List of drawable resource IDs
    private int targetImageRes;
    private TextView resultTextView; // Add this line

    public ConnectActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connecting_main_menu);

        targetImage = findViewById(R.id.targetImage);
        imageGrid = findViewById(R.id.imageGrid);
        resultTextView = findViewById(R.id.resultTextView);

        imageResources = Arrays.asList(R.drawable.animals, R.drawable.blocks, R.drawable.bevg, R.drawable.acc, R.drawable.clothes); // Add your images

        setupGame();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupGame() {
        Collections.shuffle(imageResources);
        targetImageRes = imageResources.get(0); // Pick the first one as the target
        targetImage.setImageResource(targetImageRes);

        for (int i = 0; i < 4; i++) {
            ImageView optionImage = new ImageView(this);
            optionImage.setImageResource(imageResources.get(i));

            // option images
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 200;
            params.height = 200;
            optionImage.setLayoutParams(params);
            optionImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

            optionImage.setOnDragListener(new MyDragListener());
            optionImage.setOnTouchListener(new MyTouchListener());
            imageGrid.addView(optionImage);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private static class MyTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDrag(data, shadowBuilder, v, 0);
                return true;
            }
            return false;
        }
    }

    private class MyDragListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_ENDED:
                    if (event.getResult()) {

                        ImageView draggedImageView = (ImageView) event.getLocalState();
                        if (draggedImageView.getDrawable().getConstantState() == getResources().getDrawable(targetImageRes).getConstantState()) {
                            resultTextView.setVisibility(View.VISIBLE);
                            resultTextView.setText("Correct!");
                        } else {
                            resultTextView.setVisibility(View.GONE);
                        }
                    }
                    break;
            }
            return true;
        }
    }
}
