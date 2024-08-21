namespace backend.models;

public class CustomCard(string id, string defaultCardId, string voiceAudio)
{
    public string Id { get; set; } = id;
    public string DefaultCardId { get; set; } = defaultCardId;
    public string VoiceAudio { get; set; } = voiceAudio;
}