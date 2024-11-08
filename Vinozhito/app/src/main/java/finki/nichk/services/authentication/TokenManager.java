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
        editor.remove("username"); // Clear user details as well
        editor.remove("email");
        editor.apply();
    }

    // New methods for saving and retrieving user details
    public void saveUserDetails(String username, String email) {
        editor.putString("username", username);
        editor.putString("email", email);
        editor.apply();
    }

    public String getUsername() {
        return prefs.getString("username", null);
    }

    public String getEmail() {
        return prefs.getString("email", null);
    }
}
