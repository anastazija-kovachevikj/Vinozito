package finki.nichk;

import finki.nichk.models.CustomCard;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {
    @Multipart
    @POST("upload/?folders=audio,nina")
    Call<ResponseBody> uploadFile(
            @Part MultipartBody.Part file
    );


    @POST("/api/CustomCard")
    Call<ResponseBody> postCustomCard(
            @Query("userId") String userId,
            @Body CustomCard customCard
    );
}