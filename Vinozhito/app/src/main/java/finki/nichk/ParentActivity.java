package finki.nichk;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

public class ParentActivity extends AppCompatActivity {

    private static final String PREF_AUTH = "authenticated";
    private boolean authenticated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_screen);

        setAuthenticated(false);

        // load authenticated status from SharedPreferences
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(this);
        authenticated = sharedPreferences.getBoolean(PREF_AUTH, false);

        if (!authenticated) { // if savedInstanceState is null it means the activity is created for the first time
            ParentAuthenticationFragment dialog = new ParentAuthenticationFragment();
            dialog.show(getSupportFragmentManager(), "AuthenticationFragment");
            //authenticated = true;
        }
    }

    // method to update the authenticated status
    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
        // save the authenticated status to SharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_AUTH, authenticated);
        editor.apply();
    }

}
