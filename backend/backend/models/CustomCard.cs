namespace backend.models;

public class CustomCard(string id, string defaultCardId, string voiceAudio, string userId)
{
    public string Id { get; set; } = id;
    public string DefaultCardId { get; set; } = defaultCardId;
    public string VoiceAudio { get; set; } = voiceAudio;
    public string UserId { get; set; } = userId;
}