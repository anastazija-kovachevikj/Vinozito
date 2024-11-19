package finki.nichk.mobile.activities.parent;

import static finki.nichk.R.drawable.other;
import static finki.nichk.R.drawable.record_layout;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.IOException;

import finki.nichk.ApiService;
import finki.nichk.R;
import finki.nichk.models.Card;
import finki.nichk.models.CustomCard;
import finki.nichk.models.User;
import finki.nichk.services.CardService;
import finki.nichk.services.UserService;
import finki.nichk.services.authentication.TokenManager;
import finki.nichk.services.retrofit.ApiClient;
import finki.nichk.services.retrofit.ApiClientCards;
import finki.nichk.tablet.screens.parent.ParentActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomCardActivity extends AppCompatActivity {

    private GridLayout cardLayout;
    private CardService cardService;
    private UserService userService;
    TokenManager tokenManager;
    private User user;
    private String userToken;
    private String userId;
    private TextView cardName;
    private ImageButton saveAudio;
    private ImageButton resetAudio;
    private boolean CustomTitle;
    private boolean CustomAudio;

    private EditText cardTitle;
    private Button save_button;
    private String category;
    private ImageButton play_default_rec;

    private String soundLink;
    private String imageLink;
    private ImageView cardImage;
    private ImageView soundwave_flat;
    MediaPlayer mediaPlayer;
    Card card;
    ConstraintLayout record_layout;
    private RelativeLayout audio_layout;
    private   ImageButton playRecButton;

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};

    private MediaRecorder recorder = null;
    private String fileName = null;
    private String originalName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_custom_card);
        getElementsByViewId();
        audio_layout.setAlpha(0.5f);


        setCardDetails();
        setPlayListener(play_default_rec);

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);


        tokenManager = new TokenManager(this);
        userToken = tokenManager.getToken();

        getUserInfo();

        originalName =card.getName();
        String titleCard = card.getName();
        fileName = getExternalCacheDir().getAbsolutePath() + "/" + tokenManager.getUsername() + "_" + titleCard + ".3gp";

        ImageButton recordButton = findViewById(R.id.record_button);
        ImageButton stopButton = findViewById(R.id.stop_button);

        recordButton.setOnClickListener(v -> {
            setRecordingStatus(stopButton, recordButton);
        });
        stopButton.setOnClickListener(v -> {
            setStopRecordingStatus(stopButton,recordButton);

        });

//        saveAudio.setOnClickListener(v -> {
//            CustomAudio = true;
//            uploadAudioFile();
//        });
        save_button.setOnClickListener(v -> {

            checkCustomTitle();


            if(CustomAudio && CustomTitle)
            {

                uploadAudioFile();
                String url = "http://mkpatka.duckdns.org:5000/audio/" + tokenManager.getUsername() + "_" + titleCard + ".3gp";
                createCustomCard(card.getId(), url, cardTitle.getText().toString());
                Toast.makeText(CustomCardActivity.this, "Успешно зачувана картичка!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CustomCardActivity.this, ChooseCategoryMyCardsActivity.class);
                startActivity(intent);
            }

            else if (CustomAudio) {

                uploadAudioFile();
                String url = "http://mkpatka.duckdns.org:5000/audio/" + tokenManager.getUsername() + "_" + titleCard + ".3gp";
                createCustomCard(card.getId(), url, card.getName());
                Toast.makeText(CustomCardActivity.this, "Успешно зачувана картичка!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CustomCardActivity.this, ChooseCategoryMyCardsActivity.class);
                startActivity(intent);

            }

            else if(CustomTitle)
            {
                createCustomCard(card.getId(),card.getAudioVoice(),cardTitle.getText().toString() );
                Toast.makeText(CustomCardActivity.this, "Успешно зачувана картичка!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CustomCardActivity.this, ChooseCategoryMyCardsActivity.class);
                startActivity(intent);
            }



        });


        playRecButton.setOnClickListener(v -> playRecording());


    }


    public void checkCustomTitle()
    {

        String currentName = cardTitle.getText().toString();
        if(!currentName.equals(originalName))
            CustomTitle = true;

    }

    public void getElementsByViewId()
    {
        cardService = new CardService();
        userService = new UserService(this);
        cardName = findViewById(R.id.cardNameTexView);
        cardTitle = findViewById(R.id.cardTitle);
        play_default_rec = findViewById(R.id.play_default_rec);
        cardImage = findViewById(R.id.cardImage);
        record_layout = findViewById(R.id.card_layout2);
        audio_layout = findViewById(R.id.audio_layout2);
        soundwave_flat = findViewById(R.id.soundwave_flat);
        saveAudio = findViewById(R.id.saveAudio);
        resetAudio = findViewById(R.id.restartAudio);
        save_button = findViewById(R.id.save_button);
        playRecButton = findViewById(R.id.play_rec);
    }

    public void setRecordingStatus(ImageButton stopButton, ImageButton recordButton)
    {
        stopButton.setVisibility(View.VISIBLE);
        recordButton.setVisibility(View.INVISIBLE);
        audio_layout.setAlpha(0.5f);
        startRecording();
        record_layout.setBackgroundResource(R.drawable.recording);
        startPulsatingAnimation(stopButton);
        CustomAudio = true;
    }

    public void setStopRecordingStatus(ImageButton stopButton, ImageButton recordButton)
    {
        stopButton.setVisibility(View.INVISIBLE);
        recordButton.setVisibility(View.VISIBLE);
        audio_layout.setAlpha(1.0f);
        soundwave_flat.setImageResource(R.drawable.waves);
        stopPulsatingAnimation(stopButton);
        saveAudio.setVisibility(View.VISIBLE);
        resetAudio.setVisibility(View.VISIBLE);
        record_layout.setBackgroundResource(R.drawable.record_layout);
        stopRecording();
    }

    public void getUserInfo()
    {
        userService.fetchUserByUsername(new UserService.UserCallback() {
            @Override
            public void onUserFetched(User fetchedUser) {

                user = fetchedUser;
                Log.d("CommunicationMobile", "User ID: " + user.getId());

            }

            @Override
            public void onError(String error) {

                Log.e("CommunicationMobile", "Error fetching user: " + error);
            }
        });
    }



    public void createCustomCard(String defaultCardId, String uploadedUrl, String cardTitle) {
        ApiService apiService = ApiClientCards.getRetrofitInstance().create(ApiService.class);
        String title = cardTitle;
        String userId = user.getId();

        CustomCard customCard = new CustomCard(
                null,
                defaultCardId,
                uploadedUrl,
                user.getId(),
                title
        );

        Call<ResponseBody> call = apiService.postCustomCard(user.getId(), customCard);
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("CustomCardCreation", "Custom card created successfully!");
                } else {
                    try {
                        String errorMessage = response.errorBody().string();
                        Log.e("CustomCardCreation", "Creation failed: " + errorMessage);
                    } catch (Exception e) {
                        Log.e("CustomCardCreation", "Error reading error body: " + e.getMessage());
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("CustomCardCreation", "Error: " + t.getMessage());
            }
        });
    }


    private void uploadAudioFile() {

        File file = new File(fileName);
        if (!file.exists()) {
            Log.e("AudioUpload", "File not found: " + fileName);
            return;
        }

        RequestBody requestFile = RequestBody.create(file, okhttp3.MediaType.parse("audio/3gp"));
        MultipartBody.Part multipartFile = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);


        Call<ResponseBody> call = apiService.uploadFile(multipartFile);
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("AudioUpload", "Upload successful: " + response.message());
                } else {
                    Log.e("AudioUpload", "Upload failed. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("AudioUpload", "Upload failed: " + t.getMessage());
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (!permissionToRecordAccepted) finish();
        }
    }


    private void startRecording() {
        if (recorder == null) {
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setOutputFile(fileName);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        }

        try {
            recorder.prepare();
            recorder.start();
            Log.d("AudioRecording", "Recording started");
        } catch (IOException e) {
            Log.e("AudioRecording", "Failed to start recording: " + e.getMessage());
            recorder.release();
            recorder = null;
        }
    }


    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;

        Log.d("AudioRecording", "Recording saved to: " + fileName);
    }

    private MediaPlayer player = null;

    private void playRecording() {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e("AudioPlaying", "prepare() failed");
        }

        player.setOnCompletionListener(mp -> {
            mp.release();
            player = null;
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }
        if (player != null) {
            player.release();
            player = null;
        }
    }

    private void startPulsatingAnimation(ImageButton button) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(button, "scaleX", 1f, 1.2f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(button, "scaleY", 1f, 1.2f);

        scaleX.setDuration(500);
        scaleY.setDuration(500);

        scaleX.setRepeatMode(ObjectAnimator.REVERSE);
        scaleY.setRepeatMode(ObjectAnimator.REVERSE);

        scaleX.setRepeatCount(ObjectAnimator.INFINITE);
        scaleY.setRepeatCount(ObjectAnimator.INFINITE);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.start();
    }

    private void stopPulsatingAnimation(ImageButton button) {
        button.clearAnimation();
    }


    private void setCardDetails() {

        card = (Card) getIntent().getSerializableExtra("selectedCard");

        assert card != null;
        cardName.setText(card.getName());
        cardTitle.setHint(card.getName());
        soundLink = card.getAudioVoice();
        imageLink = card.getImage();

        Glide.with(CustomCardActivity.this)
                .load(imageLink)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .placeholder(R.drawable.unknown)
                .error(R.drawable.circle_curves)
                .into(cardImage);

    }

    private void setPlayListener(ImageButton button) {
        button.setOnClickListener(view -> {

            playCardSoundFromUrl(soundLink);

        });
    }

    private void playCardSoundFromUrl(String audioFileId) {

        Log.d("CardSound", "Attempting to play sound from URL: " + audioFileId);

        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        } else {
            mediaPlayer.reset();
        }

        try {

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(audioFileId);
            mediaPlayer.prepare();
            mediaPlayer.start();

            mediaPlayer.setOnCompletionListener(mp -> {
                mp.release();
                mediaPlayer = null;
            });

        } catch (Exception e) {
            Log.e("CardSound", "Failed to play sound from URL: " + audioFileId, e);
        }
    }
}
