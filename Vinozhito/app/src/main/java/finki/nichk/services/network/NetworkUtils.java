package finki.nichk.services.network;

import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkUtils {
  //  private static final String BASE_URL = "https://1c82-77-29-6-81.ngrok-free.app/api";
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

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
        void onSuccess(String response);
        void onError(Exception e);
    }
}
