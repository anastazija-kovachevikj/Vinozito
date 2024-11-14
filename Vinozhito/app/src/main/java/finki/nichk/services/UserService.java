package finki.nichk.services;

import android.content.Context;
import android.util.Log;

import finki.nichk.models.User;
import finki.nichk.services.authentication.AuthService;
import finki.nichk.services.authentication.TokenManager;
import finki.nichk.services.retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserService {
    private AuthService authService;
    private TokenManager tokenManager;



    public UserService(Context context) {
        tokenManager = new TokenManager(context);  // Initialize TokenManager with context
        Retrofit retrofit = RetrofitClient.getClient("http://mkpatka.duckdns.org:8080/", tokenManager);
        authService = retrofit.create(AuthService.class);
    }
    // Method to fetch user by username
    public interface UserCallback {
        void onUserFetched(User user);
        void onError(String error);
    }

    public void fetchUserByUsername(final UserCallback callback) {
        String username = tokenManager.getUsername();  // Get username from token
        if (username != null) {
            Call<User> call = authService.getUserByUsername(username);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        User user = response.body();
                        if (user != null) {
                            callback.onUserFetched(user);
                        } else {
                            callback.onError("User not found");
                        }
                    } else {
                        callback.onError("Failed to fetch user by username");
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    callback.onError("Error fetching user by username");
                }
            });
        } else {
            callback.onError("Username not found in token");
        }
    }


}
