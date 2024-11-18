package finki.nichk.services.authentication;

import android.content.Context;

import finki.nichk.models.LoginRequest;
import finki.nichk.models.LoginResponse;
import finki.nichk.models.RegisterRequest;
import finki.nichk.models.RegisterResponse;
import finki.nichk.services.retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AuthRepository {
    private AuthService authService;
    private TokenManager tokenManager;

    public AuthRepository(Context context) {
        this.tokenManager = new TokenManager(context); // Initialize TokenManager
        Retrofit retrofit = RetrofitClient.getClient("http://mkpatka.duckdns.org:8080", tokenManager); // Backend base URL with TokenManager
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

    public void register(String username, String email, String password, final RegisterCallback callback) {
        RegisterRequest registerRequest = new RegisterRequest(username, email, password);
        Call<RegisterResponse> call = authService.register(registerRequest);

        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful()) {
                    String message = response.body().getMessage();
                    String token = response.body().getToken(); // If a token is returned upon registration
//                    if (token != null) {
//                        tokenManager.saveToken(token);  // Save the JWT token if applicable
//                    }
                    callback.onSuccess(message);
                } else {
                    callback.onFailure("Registration failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                callback.onFailure("Error: " + t.getMessage());
            }
        });
    }

    public interface LoginCallback {
        void onSuccess(String token);
        void onFailure(String error);
    }

    public interface RegisterCallback {
        void onSuccess(String message);
        void onFailure(String error);
    }
}
