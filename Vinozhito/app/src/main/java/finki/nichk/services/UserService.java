package finki.nichk.services;

import android.content.Context;

import finki.nichk.services.authentication.AuthService;
import finki.nichk.services.authentication.TokenManager;
import finki.nichk.services.retrofit.RetrofitClient;
import retrofit2.Retrofit;

public class UserService {
    private AuthService authService;

    public UserService(Context context) {
        TokenManager tokenManager = new TokenManager(context);
        Retrofit retrofit = RetrofitClient.getClient("https://441e-95-180-215-146.ngrok-free.app/api", tokenManager);
        authService = retrofit.create(AuthService.class);
    }

    // Example of an authenticated request
    public void getProfile() {
        // Make an API request with the JWT token included in the headers.
    }
}
