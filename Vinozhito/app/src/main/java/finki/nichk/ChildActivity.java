package finki.nichk;

import android.os.Bundle;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class ChildActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_main_screen);
    }
}
