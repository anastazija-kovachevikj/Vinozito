namespace backend.models;
using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

public class DefaultCard
{
    
    public string? Id { get; set; }
    public string AudioVoice { get; set; } 
    public string Image { get; set; } 
    public string Category { get; set; } 
}