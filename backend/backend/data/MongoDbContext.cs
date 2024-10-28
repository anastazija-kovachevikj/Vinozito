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
                    Name = "мајмун",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/мајмун.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/мајмун.png",
                    Category = "Animals"
                },
                new DefaultCard
                {
                    //2
                    Name = "жирафа",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/жирафа.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/жирафа.png",
                    Category = "Animals"
                },
                new DefaultCard
                {
                    //3
                    Name = "мечка",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/мечка.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/мечка.png",
                    Category = "Animals"
                },

                new DefaultCard
                {
                    //5
                    Name = "нилски коњ",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/нилски_коњ.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/нилски_коњ.png",
                    Category = "Animals"
     
                },
                new DefaultCard
                {
                    //6
                    Name = "лав",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/лав.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/лав.png",
                    Category = "Animals"
                },
                new DefaultCard
                {
                    //7
                    Name = "слон",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/слон.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/слон.png",
                    Category = "Animals"
                },
                new DefaultCard
                {
                    //8
                    Name = "зебра",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/зебра.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/зебра.png",
                    Category = "Animals"
                   
                },
                new DefaultCard
                {
                    //9
                    Name = "пингвин",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/пингвин.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/пингвин.png",
                    Category = "Animals"

                },
                new DefaultCard
                {
                    //10
                    Name = "змија",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/змија.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/змија.png",
                    Category = "Animals"

                },
                new DefaultCard
                {
                    //11
                    Name = "пчела",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/пчела.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/пчела.png",
                    Category = "Animals"


                },
                new DefaultCard
                {
                    //12
                    Name = "пајак",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/пајак.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/пајак.png",
                    Category = "Animals"
                },
                new DefaultCard
                {
                    //13
                    Name = "пеперутка",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/пеперутка.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/пеперутка.png",
                    Category = "Animals"
                },
                new DefaultCard
                {
                    //14
                    Name = "бубамара",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/бубамара.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/бубамара.png",
                    Category = "Animals"
                },
                new DefaultCard
                {
                    //15
                    Name = "мравка",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/мравка.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/мравка.png",
                    Category = "Animals"
                    
                },
                new DefaultCard
                {
                    //15
                    Name = "вода",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/вода.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/вода.png",
                    Category = "Drinks"
                    
                },
                new DefaultCard
                {
                    //15
                    Name = "млеко",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/млеко.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/млеко.png",
                    Category = "Drinks"
                    
                },
                new DefaultCard
                {
                    //15
                    Name = "јогурт",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/јогурт.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/јогурт.png",
                    Category = "Drinks"
                    
                },
                new DefaultCard
                {
                    //15
                    Name = "чај",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/чај.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/чај.png",
                    Category = "Drinks"
                    
                },
                new DefaultCard
                {
                    //15
                    Name = "сок",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/сок.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/сок.png",
                    Category = "Drinks"
                    
                },
                new DefaultCard
                {
                    //15
                    Name = "газиран сок",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/газиран_сок.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/газиран_сок.png",
                    Category = "Drinks"
                    
                },
                new DefaultCard
                {
                    //15
                    Name = "топло чоколадо",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/топло_чоколадо.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/топло_чоколадо.png",
                    Category = "Drinks"
                    
                },
                new DefaultCard
                {
                    //15
                    Name = "салеп",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/салеп.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/салеп.png",
                    Category = "Drinks"
                    
                },
                new DefaultCard
                {
                    //15
                    Name = "лимонада",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/лимонада.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/лимонада.png",
                    Category = "Drinks"
                    
                },
                new DefaultCard
                {
                    //15
                    Name = "милкшејк",
                    AudioVoice = "http://mkpatka.duckdns.org:5000/audio/милкшејк.m4a",
                    Image = "http://mkpatka.duckdns.org:5000/images/милкшејк.png",
                    Category = "Drinks"
                    
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