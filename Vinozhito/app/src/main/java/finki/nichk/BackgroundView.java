package finki.nichk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

public class BackgroundView extends View {
    private Paint paint;
    private Bitmap backgroundBitmap;
    private Bitmap scaledBitmap;
    private float offset = 0;

    public BackgroundView(Context context) {
        super(context);
        init();
    }

    public BackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BackgroundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sky);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // scale the bitmap based on height, maintaining aspect ratio => so it doesn't stretch horizontally
        int bitmapHeight = h;  //match the height of the view
        int bitmapWidth = (int) ((float) backgroundBitmap.getWidth() * ((float) h / (float) backgroundBitmap.getHeight()));

        scaledBitmap = Bitmap.createScaledBitmap(backgroundBitmap, bitmapWidth, bitmapHeight, true);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        if (scaledBitmap != null) {
            int bitmapWidth = scaledBitmap.getWidth();
            int bitmapHeight = scaledBitmap.getHeight();

            // draw the background with horizontal repetition
            int numRepeats = (int) Math.ceil((double) getWidth() / bitmapWidth);  // Calculate how many times to repeat

            for (int i = 0; i <= numRepeats; i++) {
                canvas.drawBitmap(scaledBitmap, -offset + i * bitmapWidth, 0, paint);
            }

            //offset for scrolling
            float scrollSpeed = 1f;
            offset += scrollSpeed;
            if (offset >= bitmapWidth) {
                offset = 0;
            }
        }

        invalidate(); // redraw
    }
}
