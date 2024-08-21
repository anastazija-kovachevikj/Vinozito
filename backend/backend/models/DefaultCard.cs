namespace backend.models;

public class DefaultCard(string id, string audioVoice, string image, string category)
{
    public string Id { get; set; } = id;
    public string AudioVoice { get; set; } = audioVoice;
    public string Image { get; set; } = image;
    public string Category { get; set; } = category;
}