namespace backend.models;
using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;


public class CustomCard
{
    
    public string? Id { get; set; }
    public string DefaultCardId { get; set; }
    public string VoiceAudio { get; set; } 
    public string Title { get; set; } 
    public string UserId { get; set; } 
}