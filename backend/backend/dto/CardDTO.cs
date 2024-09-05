namespace backend.dto;

public class CardDto(string id, string audioVoice, string image, string category, CardType cardType)
{
    public string Id { get; set; } = id;
    public string AudioVoice { get; set; } = audioVoice;
    public string Image { get; set; } = image;
    public string Category { get; set; } = category;
    public CardType CardType { get; set; } = cardType;
}