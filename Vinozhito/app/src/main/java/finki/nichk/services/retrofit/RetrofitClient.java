package finki.nichk.services.retrofit;
import finki.nichk.services.authentication.AuthInterceptor;
import finki.nichk.services.authentication.TokenManager;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//
//public class RetrofitClient {
//
//    private static final String BASE_URL = "https://5d35-92-55-107-139.ngrok-free.app/";
//    private static Retrofit retrofit = null;
//
//    public static Retrofit getClient() {
//        if (retrofit == null) {
//            retrofit = new Retrofit.Builder()
//                    .baseUrl(BASE_URL)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//        }
//        return retrofit;
//    }
//}

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl, TokenManager tokenManager) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(tokenManager))
                .build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}


