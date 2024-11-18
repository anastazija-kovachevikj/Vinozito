package finki.nichk.mobile.activities.parent;

import static finki.nichk.R.drawable.record_layout;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.pm.PackageManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;

import finki.nichk.R;
import finki.nichk.models.Card;
import finki.nichk.models.User;
import finki.nichk.services.CardService;
import finki.nichk.services.UserService;
import finki.nichk.services.authentication.TokenManager;

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

    private EditText cardTitle;
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

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};

    private MediaRecorder recorder = null;
    private String fileName = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_custom_card);

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

        audio_layout.setAlpha(0.5f);


        setCardDetails();
        setPlayListener(play_default_rec);

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);


        tokenManager = new TokenManager(this);
        userToken = tokenManager.getUsername();
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

        fileName = getExternalCacheDir().getAbsolutePath() + "/audiorecordtest.3gp";

        ImageButton recordButton = findViewById(R.id.record_button);
        ImageButton stopButton = findViewById(R.id.stop_button);

        recordButton.setOnClickListener(v -> {
            stopButton.setVisibility(View.VISIBLE);
            recordButton.setVisibility(View.INVISIBLE);
            audio_layout.setAlpha(0.5f);
            startRecording();
            record_layout.setBackgroundResource(R.drawable.recording);
            startPulsatingAnimation(stopButton);
        });
        stopButton.setOnClickListener(v -> {

            stopButton.setVisibility(View.INVISIBLE);
            recordButton.setVisibility(View.VISIBLE);
            audio_layout.setAlpha(1.0f);
            soundwave_flat.setImageResource(R.drawable.waves);
            stopPulsatingAnimation(stopButton);
            saveAudio.setVisibility(View.VISIBLE);
            resetAudio.setVisibility(View.VISIBLE);
            record_layout.setBackgroundResource(R.drawable.record_layout);
            stopRecording();
        });

        ImageButton playRecButton = findViewById(R.id.play_rec);
        playRecButton.setOnClickListener(v -> playRecording());


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
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e("AudioRecording", "prepare() failed");
        }

        recorder.start();
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

        scaleX.setDuration(500); // Half a second
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
