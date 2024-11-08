package finki.nichk.mobile.activities;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;

public class ImageProcessor {

    private Context context;

    public ImageProcessor(Context context) {
        this.context = context;
    }

    public Bitmap loadImageFromAssets(String imageName) {
        AssetManager assetManager = context.getAssets();
        try (InputStream inputStream = assetManager.open("images/" + imageName)) {
            return BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void preprocessAndSaveImages(int[] imageResources) {
        for (int i = 0; i < imageResources.length; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imageResources[i]);
            Bitmap processedBitmap = preprocessImage(bitmap);
            saveBitmapAsFile(processedBitmap, "processed_image_" + i + ".png");
        }
    }

    private Bitmap preprocessImage(Bitmap bitmap) {
        Bitmap processedBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        int threshold = 50; // threshold for detecting black pixels
        int lightGray = Color.rgb(254, 254, 254);

        // loop to process pixel by pixel
        for (int x = 0; x < bitmap.getWidth(); x++) {
            for (int y = 0; y < bitmap.getHeight(); y++) {
                int pixel = bitmap.getPixel(x, y);
                int red = Color.red(pixel);
                int green = Color.green(pixel);
                int blue = Color.blue(pixel);

                if (red < threshold && green < threshold && blue < threshold) {
                    processedBitmap.setPixel(x, y, Color.BLACK); // Mark outline as black
                } else {
                    processedBitmap.setPixel(x, y, Color.WHITE); // Fill with white
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

        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point(x, y));

        while (!queue.isEmpty()) {
            Point point = queue.poll();
            int px = point.x;
            int py = point.y;

            if (px < 0 || py < 0 || px >= bitmap.getWidth() || py >= bitmap.getHeight()) continue;

            int currentColor = bitmap.getPixel(px, py);
            if (currentColor == targetColor) {
                bitmap.setPixel(px, py, replaceColor);

                queue.add(new Point(px + 1, py));
                queue.add(new Point(px - 1, py));
                queue.add(new Point(px, py + 1));
                queue.add(new Point(px, py - 1));
            }
        }
    }

    private void saveBitmapAsFile(Bitmap bitmap, String fileName) {
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "processed_images");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(directory, fileName);
        try (FileOutputStream outStream = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            Log.d("ImageSave", "Image saved: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
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
