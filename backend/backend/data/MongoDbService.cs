using MongoDB.Bson;
using MongoDB.Driver;

namespace backend.data;

public class MongoDbService
{
    private readonly IMongoCollection<BsonDocument> _collection;

    public MongoDbService()
    {
        // Set all needed arguments for connecting with MongoDB
        string connectionString = "mongodb://root:example@localhost:27017";
        string databaseName = "VinozitoDB";
        string collectionName = "DefaultCards";

        var client = new MongoClient(connectionString);
        var database = client.GetDatabase(databaseName);
        _collection = database.GetCollection<BsonDocument>(collectionName);
    }

    public void InsertSampleData()
    {
        // Sample data to insert
        var documents = new List<BsonDocument>
        {
            new BsonDocument { { "AudioVoice", "https://drive.google.com/file/d/19PGZaHHmSfKC2roQsOj7qq2GuuHXuGUk/view?usp=drive_link" },
                                { "Image", "https://drive.google.com/file/d/1626mrBpH3uvODsaG8l9RTdQG7en_hKid/view?usp=drive_link" }, 
                                { "Category", "Fruit" } },

        };

        // Insert data into MongoDB
        _collection.InsertMany(documents);
    }
}