package finki.nichk.services;

import java.util.List;

import finki.nichk.models.Card;
import finki.nichk.services.network.NetworkUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class CardService {

    private Gson gson = new Gson();

    public void fetchCardDataByUserIdAndCategory(String userId, String category, CardServiceCallback callback) {

        String url = "https://03e6-79-125-235-106.ngrok-free.app/api/Card/category/" + userId + "?category=" + category;


        NetworkUtils.getAsync(new NetworkUtils.ApiCallback() {

            @Override
            public List<Card> onSuccess(String response) {
                List<Card> cards = parseCards(response);
                if (callback != null) {
                    callback.onCardsFetched(cards); // Pass the cards to the callback
                }
                return cards;
            }

            @Override
            public void onError(Exception e) {
                if (callback != null) {
                    callback.onError(e); // Pass the error to the callback
                }
            }
        }, url);
    }

    private List<Card> parseCards(String jsonResponse) {
        Type cardListType = new TypeToken<List<Card>>() {}.getType();
        return gson.fromJson(jsonResponse, cardListType);
    }

    public interface CardServiceCallback {
        void onCardsFetched(List<Card> cards);
        void onError(Exception e);
    }
}
