using backend.models;
using Microsoft.Extensions.Configuration;
using MongoDB.Driver;

namespace backend.data
{
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
        
        public void InsertSampleData()
        {
            var sampleData = new List<DefaultCard>
            {
                new DefaultCard
                {
                    AudioVoice = "https://drive.google.com/file/d/19PGZaHHmSfKC2roQsOj7qq2GuuHXuGUk/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1626mrBpH3uvODsaG8l9RTdQG7en_hKid/view?usp=drive_link",
                    Category = "Fruit"
                }
            };

            DefaultCards.InsertMany(sampleData);
        }
    }
}