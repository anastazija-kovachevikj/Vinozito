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
                //========Conversation=========
            new DefaultCard
                {
                    //
                    Name = "да",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/да.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/да.png",
                    Category = "Conversation"
                },
                new DefaultCard
                {
                    //
                    Name = "не",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/не.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/не.png",
                    Category = "Conversation"
                },
                new DefaultCard
                {
                    //
                    Name = "стоп",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/стоп.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/стоп.png",
                    Category = "Conversation"
                },
                new DefaultCard
                {
                    //
                    Name = "зошто",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/зошто.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/зошто.png",
                    Category = "Conversation"
                },
                new DefaultCard
                {
                    //
                    Name = "помош",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/помош.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/помош.png",
                    Category = "Conversation"
                },
                // //========Vegetable=========
                // new DefaultCard
                // {
                //     //
                //     Name = "домат",
                //     AudioVoice = "http://mkpatka.duckdns.org:5000/audio/домат.m4a",
                //     Image = "http://mkpatka.duckdns.org:5000/images/домат.png",
                //     Category = "Vegetable"
                // },
                // new DefaultCard
                // {
                //     //
                //     Name = "морков",
                //     AudioVoice = "http://mkpatka.duckdns.org:5000/audio/морков.m4a",
                //     Image = "http://mkpatka.duckdns.org:5000/images/морков.png",
                //     Category = "Vegetable"
                // },
                // new DefaultCard
                // {
                //     //
                //     Name = "крставица",
                //     AudioVoice = "http://mkpatka.duckdns.org:5000/audio/крставица.m4a",
                //     Image = "http://mkpatka.duckdns.org:5000/images/крставица.png",
                //     Category = "Vegetable"
                // },
                // new DefaultCard
                // {
                //     //
                //     Name = "домат",
                //     AudioVoice = "http://mkpatka.duckdns.org:5000/audio/домат.m4a",
                //     Image = "http://mkpatka.duckdns.org:5000/images/домат.png",
                //     Category = "Vegetable"
                // },
                // new DefaultCard
                // {
                //     //
                //     Name = "домат",
                //     AudioVoice = "http://mkpatka.duckdns.org:5000/audio/домат.m4a",
                //     Image = "http://mkpatka.duckdns.org:5000/images/домат.png",
                //     Category = "Vegetable"
                // },
            };
            foreach (var dfCard in sampleData)
            {
                await defaultCardService.AddDefaultCardAsync(dfCard);
            }

            DefaultCards.InsertMany(sampleData);
        }
    }
}