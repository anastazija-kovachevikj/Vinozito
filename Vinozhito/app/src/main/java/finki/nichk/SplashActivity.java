package finki.nichk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import finki.nichk.mobile.activities.MobileMainMenuActivity;
import finki.nichk.mobile.activities.child.BubbleActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    private static final long SPLASH_DELAY = 3000; // 3 seconds

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_load);

        new Handler().postDelayed((Runnable) () -> {
            Intent intent = new Intent(SplashActivity.this, BubbleActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_DELAY);
    }
}

