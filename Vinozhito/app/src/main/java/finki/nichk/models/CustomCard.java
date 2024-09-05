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


}
