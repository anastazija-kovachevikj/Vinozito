package finki.nichk.models;

public class CustomCard {

    private String id;
    private String defaultCardId;
    private String voiceAudio;
    private String userId;

    // Constructor
    public CustomCard(String id, String defaultCardId, String voiceAudio, String userId) {
        this.id = id;
        this.defaultCardId = defaultCardId;
        this.voiceAudio = voiceAudio;
        this.userId = userId;
    }

    // Default constructor (for Gson)
    public CustomCard() {
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDefaultCardId() {
        return defaultCardId;
    }

    public void setDefaultCardId(String defaultCardId) {
        this.defaultCardId = defaultCardId;
    }

    public String getVoiceAudio() {
        return voiceAudio;
    }

    public void setVoiceAudio(String voiceAudio) {
        this.voiceAudio = voiceAudio;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
