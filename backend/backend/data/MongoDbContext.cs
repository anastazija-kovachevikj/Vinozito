using backend.models;
using backend.services;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
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

        public async void InsertSampleDataAsync(IServiceProvider serviceProvider)
        {
            using var scope = serviceProvider.CreateScope();
            var defaultCardService = scope.ServiceProvider.GetRequiredService<IDefaultCardService>();

            var sampleData = new List<DefaultCard>
            {
            new DefaultCard
                {
                    //
                    Name = "да",
                    AudioVoice = "https://drive.google.com/file/d/1YvPxmdmHJTcwQ8ZadaqN-lHSnI3TfTmF/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/19t5Mb6_2pDeOyyvCZHSDQR9l7XRUh-aI/view?usp=drive_link",
                    Category = "Conversation"
                },
                new DefaultCard
                {
                    //
                    Name = "не",
                    AudioVoice = "https://drive.google.com/file/d/1nAfGlDno8BEthoBq6RhiReUSWGLlBv30/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/17Ec9lHU30LkLGwxJgV4G_SeUqBYkUnXk/view?usp=drive_link",
                    Category = "Conversation"
                },
                new DefaultCard
                {
                    //
                    Name = "стоп",
                    AudioVoice = "https://drive.google.com/file/d/1DYGZZk5N4MbUqKRN2g18k5j7tU32Tr_O/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1uWD21JplQnUaLZF9bVmjDyz3DsoQinP_/view?usp=drive_link",
                    Category = "Conversation"
                },
                new DefaultCard
                {
                    //
                    Name = "зошто",
                    AudioVoice = "https://drive.google.com/file/d/1ejA9T-tQeL1sr_Gdvy-KzLxf02YvVVMI/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1tdQaCDF0MRqqbBQdgarviK5FumG7Bgt5/view?usp=drive_link",
                    Category = "Conversation"
                },
                new DefaultCard
                {
                    //
                    Name = "помош",
                    AudioVoice = "https://drive.google.com/file/d/1BRYx5JwOjBOEXfM-aTJiQwVW82MD7-t2/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1ge2g4wbYgCR3X-hLDJYFyC4j9Zexpd3b/view?usp=drive_link",
                    Category = "Conversation"
                },
            };
            foreach (var dfCard in sampleData)
            {
                await defaultCardService.AddDefaultCardAsync(dfCard);
            }

            DefaultCards.InsertMany(sampleData);
        }
    }
}