package finki.nichk.services.authentication;

import finki.nichk.models.LoginRequest;
import finki.nichk.models.LoginResponse;
import finki.nichk.models.RegisterRequest;
import finki.nichk.models.RegisterResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("api/auth/register")
     Call<RegisterResponse> register(@Body RegisterRequest registerRequest);
}
