package finki.nichk.services.authentication;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public TokenManager(Context context) {
        prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void saveToken(String token) {
        editor.putString("jwt_token", token);
        editor.apply();
    }

    public String getToken() {
        return prefs.getString("jwt_token", null);
    }

    public void clearToken() {
        editor.remove("jwt_token");
        editor.apply();
    }
}
