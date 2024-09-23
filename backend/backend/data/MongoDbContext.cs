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
                 //========Animals=========
                new DefaultCard
                {
                    //1
                    Name = "куче",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/куче.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/куче.png",
                    Category = "Animals"
                },
                new DefaultCard
                {
                    //2
                    Name = "мачка",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/мачка.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/мачка.png",
                    Category = "Animals"
                },
                new DefaultCard
                {
                    //3
                    Name = "хрчак",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/хрчак.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/хрчак.png",
                    Category = "Animals"
                },

                new DefaultCard
                {
                    //5
                    Name = "папагал",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/папагал.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/папагал.png",
                    Category = "Animals"
     
                },
                new DefaultCard
                {
                    //6
                    Name = "желка",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/желка.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/желка.png",
                    Category = "Animals"
                },
                new DefaultCard
                {
                    //7
                    Name = "зајак",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/зајак.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/зајак.png",
                    Category = "Animals"
                },
                new DefaultCard
                {
                    //8
                    Name = "кокошка",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/кокошка.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/кокошка.png",
                    Category = "Animals"
                   
                },
                new DefaultCard
                {
                    //9
                    Name = "прасе",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/прасе.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/прасе.png",
                    Category = "Animals"

                },
                new DefaultCard
                {
                    //10
                    Name = "овца",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/овца.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/овца.png",
                    Category = "Animals"

                },
                new DefaultCard
                {
                    //11
                    Name = "крава",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/крава.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/крава.png",
                    Category = "Animals"


                },
                new DefaultCard
                {
                    //12
                    Name = "коза",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/коза.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/коза.png",
                    Category = "Animals"
                },
                new DefaultCard
                {
                    //13
                    Name = "коњ",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/коњ.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/коњ.png",
                    Category = "Animals"
                },
                new DefaultCard
                {
                    //14
                    Name = "гулаб",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/гулаб.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/гулаб.png",
                    Category = "Animals"
                },
                new DefaultCard
                {
                    //15
                    Name = "був",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/був.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/був.png",
                    Category = "Animals"
                    
                },
                // },
                // new DefaultCard
                // {
                //     //4
                //     Name = "апостолки",
                //     AudioVoice = "http://mkpatka.duckdns.org:5000/audio/апостолки.m4a",
                //     Image = "http://mkpatka.duckdns.org:5000/images/апостолки.png",
                //     Category = "Animals"
                //
                // },
                // new DefaultCard
                // {
                //     //16
                //     Name = "капа",
                //     AudioVoice = "http://mkpatka.duckdns.org:5000/audio/капа.m4a",
                //     Image = "http://mkpatka.duckdns.org:5000/images/капа.png",
                //     Category = "Animals"
                // },
                // new DefaultCard
                // {
                //     //17
                //     Name = "шал",
                //     AudioVoice = "http://mkpatka.duckdns.org:5000/audio/шал.m4a",
                //     Image = "http://mkpatka.duckdns.org:5000/images/шал.png",
                //     Category = "Animals"
                // },
                // new DefaultCard
                // {
                //     //18
                //     Name = "ракавици",
                //     AudioVoice = "http://mkpatka.duckdns.org:5000/audio/ракавици.m4a",
                //     Image = "http://mkpatka.duckdns.org:5000/images/ракавици.png",
                //     Category = "Animals"
                // },
                // new DefaultCard
                // {
                //     //19
                //     Name = "патики",
                //     AudioVoice = "http://mkpatka.duckdns.org:5000/audio/патики.m4a",
                //     Image = "http://mkpatka.duckdns.org:5000/images/патики.png",
                //     Category = "Animals"
                // },
                // new DefaultCard
                // {
                //     //20
                //     Name = "чизми",
                //     AudioVoice = "http://mkpatka.duckdns.org:5000/audio/чизми.m4a",
                //     Image = "http://mkpatka.duckdns.org:5000/images/чизми.png",
                //     Category = "Animals"
                // },
                // new DefaultCard
                // {
                //     //21
                //     Name = "чинија",
                //     AudioVoice = "http://mkpatka.duckdns.org:5000/audio/чинија.m4a",
                //     Image = "http://mkpatka.duckdns.org:5000/images/чинија.png",
                //     Category = "Animals"
                // },
                // new DefaultCard
                // {
                //     //22
                //     Name = "лажица",
                //     AudioVoice = "http://mkpatka.duckdns.org:5000/audio/лажица.m4a",
                //     Image = "http://mkpatka.duckdns.org:5000/images/лажица.png",
                //     Category = "Animals"
                //
                // },
                // new DefaultCard
                // {
                //     //23
                //     Name = "вилушка",
                //     AudioVoice = "http://mkpatka.duckdns.org:5000/audio/вилушка.m4a",
                //     Image = "http://mkpatka.duckdns.org:5000/images/вилушка.png",
                //     Category = "Animals"
                // },
                // new DefaultCard
                // {
                //     //24
                //
                //     Name = "нож",
                //     AudioVoice = "http://mkpatka.duckdns.org:5000/audio/нож.m4a",
                //     Image = "http://mkpatka.duckdns.org:5000/images/нож.png",
                //     Category = "Animals"
                // },
                // new DefaultCard
                // {
                //     //25
                //     Name = "салфетка",
                //     AudioVoice = "http://mkpatka.duckdns.org:5000/audio/салфетка.m4a",
                //     Image = "http://mkpatka.duckdns.org:5000/images/салфетка.png",
                //     Category = "Animals"
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