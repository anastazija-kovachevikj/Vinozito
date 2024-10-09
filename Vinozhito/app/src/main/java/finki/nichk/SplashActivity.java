package finki.nichk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import finki.nichk.mobile.activities.MobileMainMenuActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    private static final long SPLASH_DELAY = 3000; // 3 seconds

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_load);

        new Handler().postDelayed((Runnable) () -> {
            Intent intent = new Intent(SplashActivity.this, MobileMainMenuActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_DELAY);
    }
}

