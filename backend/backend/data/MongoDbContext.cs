using backend.models;
using Microsoft.Extensions.Configuration;
using MongoDB.Driver;

namespace backend.data;

public class MongoDbContext
{
    private readonly IMongoDatabase _database;

    public MongoDbContext(IConfiguration configuration)
    {
        var client = new MongoClient(configuration.GetConnectionString("MongoDb"));
        _database = client.GetDatabase("VinozitoDB");
    }

    public IMongoCollection<User> Users => _database.GetCollection<User>("Users");
    public IMongoCollection<DefaultCard> DefaultCards => _database.GetCollection<DefaultCard>("DefaultCards");
    public IMongoCollection<CustomCard> CustomCards => _database.GetCollection<CustomCard>("CustomCards");
    public IMongoCollection<UserCards> UserCards => _database.GetCollection<UserCards>("UserCards");
}
