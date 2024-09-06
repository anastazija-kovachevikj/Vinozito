package finki.nichk.models;

public class Card {

    private String id;
    private String name;
    private String audioVoice;
    private String image;
    private String category;
    private CardType cardType;

    public Card() {
    }

    public Card(String id, String name, String audioVoice, String image, String category, CardType cardType) {
        this.id = id;
        this.name = name;
        this.audioVoice = audioVoice;
        this.image = image;
        this.category = category;
        this.cardType = cardType;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAudioVoice() {
        return audioVoice;
    }

    public void setAudioVoice(String audioVoice) {
        this.audioVoice = audioVoice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
