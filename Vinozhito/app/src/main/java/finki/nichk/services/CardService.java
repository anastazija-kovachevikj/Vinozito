package finki.nichk.services;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import finki.nichk.models.Card;
import finki.nichk.services.network.NetworkUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import finki.nichk.models.Card;
import finki.nichk.services.network.NetworkUtils;

public class CardService {

    private Gson gson = new Gson(); // Create a Gson instance

    public void fetchCardDataByUserId(String userId) {

        String url = "https://1c82-77-29-6-81.ngrok-free.app/api/Card/";
        NetworkUtils.getAsync(new NetworkUtils.ApiCallback() {
            @Override
            public void onSuccess(String response) {

                System.out.println(response);

                // Parse the response using Gson and update a List<Card>
                List<Card> cards = parseCards(response);

                // Now you can work with the cards list

                System.out.println(cards);
            }

            @Override
            public void onError(Exception e) {
                // Handle the error here
                e.printStackTrace();
            }
        }, url + userId);
    }

    private List<Card> parseCards(String jsonResponse) {
        // Define the type for List<Card>
        Type cardListType = new TypeToken<List<Card>>() {}.getType();

        // Parse the JSON response into a List<Card> using Gson
        return gson.fromJson(jsonResponse, cardListType);
    }
}
