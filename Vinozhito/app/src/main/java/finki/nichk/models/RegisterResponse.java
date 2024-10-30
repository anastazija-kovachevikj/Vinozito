package finki.nichk.models;


public class RegisterResponse {
    private String message;
    private String token; // Optional: in case a token is returned upon registration

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
