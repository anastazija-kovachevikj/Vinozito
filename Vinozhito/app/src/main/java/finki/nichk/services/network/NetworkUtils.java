package finki.nichk.services.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import finki.nichk.models.Card;

public class NetworkUtils {
  //  private static final String BASE_URL = "https://1c82-77-29-6-81.ngrok-free.app/api";
  private static final ExecutorService executor = new ThreadPoolExecutor(
          1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>()
  );
    public static void getAsync(ApiCallback callback, String urlString) {
        executor.submit(() -> {
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Read the response
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Return the response as a string
                if (callback != null) {
                    callback.onSuccess(response.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (callback != null) {
                    callback.onError(e);
                }
            }
        });
    }

    public interface ApiCallback {
        List<Card> onSuccess(String response);
        void onError(Exception e);
    }
}
