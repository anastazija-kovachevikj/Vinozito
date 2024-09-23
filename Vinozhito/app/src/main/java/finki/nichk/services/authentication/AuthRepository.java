package finki.nichk.services.authentication;

import android.content.Context;

import finki.nichk.models.LoginRequest;
import finki.nichk.models.LoginResponse;
import finki.nichk.services.retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit;

public class AuthRepository {
    private AuthService authService;
    private TokenManager tokenManager;

    public AuthRepository(Context context) {
        this.tokenManager = new TokenManager(context); // Initialize TokenManager
        Retrofit retrofit = RetrofitClient.getClient("https://441e-95-180-215-146.ngrok-free.app", tokenManager); // Backend base URL with TokenManager
        authService = retrofit.create(AuthService.class);
    }

    public void login(String username, String password, final LoginCallback callback) {
        LoginRequest loginRequest = new LoginRequest(username, password);
        Call<LoginResponse> call = authService.login(loginRequest);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    String token = response.body().getToken();
                    tokenManager.saveToken(token);  // Save the JWT token
                    callback.onSuccess(token);
                } else {
                    callback.onFailure("Login failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                callback.onFailure("Error: " + t.getMessage());
            }
        });
    }

    public interface LoginCallback {
        void onSuccess(String token);
        void onFailure(String error);
    }
}
