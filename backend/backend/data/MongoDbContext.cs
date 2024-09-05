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
                    //apple

                    Name = "apple",
                    AudioVoice = "https://drive.google.com/file/d/19PGZaHHmSfKC2roQsOj7qq2GuuHXuGUk/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1626mrBpH3uvODsaG8l9RTdQG7en_hKid/view?usp=drive_link",
                    Category = "Fruit"
                },
                new DefaultCard
                {
                    //banana
                    Name = "banana",
                    AudioVoice =
                        "https://drive.google.com/file/d/1alN5m-80aueWXtaqx7I2zVhMokRMAW4r/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1eYKPF0vGGobnESNfPMI-aB7uNeIkW1kQ/view?usp=drive_link",
                    Category = "Fruit"
                },
                new DefaultCard
                {
                    //carrot
                    Name = "carrot",
                    AudioVoice =
                        "https://drive.google.com/file/d/1JvE-FA1YglQprMM4e3hSVE-4JMeKQRz8/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1AY4CiOHnhNS5U6kEMi8Ox0m0e9ap4gRY/view?usp=drive_link",
                    Category = "Vegetable"
                },

                new DefaultCard
                {
                    //cherry
                    Name = "cherry",
                    AudioVoice =
                        "https://drive.google.com/file/d/1Zzo2ELdQdxiLx11MarTPVG6yJHR2aovX/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1mpgMCVTYpCHxLeY916tG1nA7Nnc2l04p/view?usp=drive_link",
                    Category = "Fruit"
                },

                new DefaultCard
                {
                    //kiwi
                    Name = "kiwi",
                    AudioVoice =
                        "https://drive.google.com/file/d/1itPKhzLCpPZCsjPw3pQqoxvJdurwsRm6/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1917wT4Rh9lE9ohMtGQQiONnJVh4VgnsS/view?usp=drive_link",
                    Category = "Fruit"
                },
                new DefaultCard
                {
                    //lemon
                    Name = "lemon",
                    AudioVoice =
                        "https://drive.google.com/file/d/1gfJu770pfnvbZDRaTMiKz8t8Q0iOFRAj/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1Lj9F8CoynYwRbtxfx5OAp8kZ2oLl8l8b/view?usp=drive_link",
                    Category = "Fruit"
                },
                new DefaultCard
                {
                    //orange
                    Name = "orange",
                    AudioVoice =
                        "https://drive.google.com/file/d/1QWtA_wAURMglAckEUN5AZQpbnyXKdXxC/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/12tUMKQod9Oq-rEIFUB5c5FvDtUeEDvbp/view?usp=drive_link",
                    Category = "Fruit"
                },
                new DefaultCard
                {
                    //peach
                    Name = "peach",
                    AudioVoice =
                        "https://drive.google.com/file/d/1-XLXjG3Xk94WX2BjidUqW0sI_ARIO0iq/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/11h0YZPMX29yqVOzHOXkukswoaYSB6EOe/view?usp=drive_link",
                    Category = "Fruit"
                },
                new DefaultCard
                {
                    //pear
                    Name = "pear",
                    AudioVoice =
                        "https://drive.google.com/file/d/1uHJce_sxsWutGFcBeRW42zjL4NcPkFfh/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1G8qmVlK2KPtIVBHw51Fh4Ig0ox9FQnji/view?usp=drive_link",
                    Category = "Fruit"
                },
                new DefaultCard
                {
                    //pineapple
                    Name = "pineaplle",
                    AudioVoice =
                        "https://drive.google.com/file/d/1BuZ2IuaXzSmEs-aLnQnHirGH9JtgXbAX/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1nQmIM5m0BG-4HrARQF-RCxd2njXLWElK/view?usp=drive_link",
                    Category = "Fruit"
                },
                new DefaultCard
                {
                    //pomegranate
                    Name = "pomegranate",
                    AudioVoice =
                        "https://drive.google.com/file/d/1kpE6luTljXEdsdPx97STI3cW0DqCtOT5/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1pSsYnznyvuU_J0M_Q2Uov8x_lnTLIe_I/view?usp=drive_link",
                    Category = "Fruit"
                },
                new DefaultCard
                {
                    //raspberry
                    Name = "raspberry",
                    AudioVoice =
                        "https://drive.google.com/file/d/1Y5cD5FF5M4yvBFjQDUZHdtYUQSV9yE4K/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1DqGVfU-bCNP_ybqiqU1vyB-udh60fGkN/view?usp=drive_link",
                    Category = "Fruit"
                },
                new DefaultCard
                {
                    //strawberry
                    Name = "strawberry",
                    AudioVoice =
                        "https://drive.google.com/file/d/1xnSrCsRlqFXZ_g-ZmNv-mJ-KPb_QGaBN/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1pMa9dGovXkLh1k8eHSOicpFToirNqXQC/view?usp=drive_link",
                    Category = "Fruit"
                },
                new DefaultCard
                {
                    //tangerine
                    Name = "tangerine",
                    AudioVoice =
                        "https://drive.google.com/file/d/1Ww6-vMISa65dOGX3W-nPyQr831EQm1of/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1wKh3EUBxFpKtMV1WSpAh-u3v-3M5ucye/view?usp=drive_link",
                    Category = "Fruit"
                },
                new DefaultCard
                {
                    //watermelon
                    Name = "watermelon",
                    AudioVoice =
                        "https://drive.google.com/file/d/16pJYexVGA7MqY7FLOY2ldYLeMjl4dGAT/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1satJLDffuMPlSTE_Su8Eth9CoQJLZ_5q/view?usp=drive_link",
                    Category = "Fruit"
                },
                new DefaultCard
                {
                    //grapefruit
                    Name = "grapefruit",
                    AudioVoice =
                        "https://drive.google.com/file/d/1Glc76Wu9o7-r3ZReBx3B45jycSCYMINT/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1bMOgEfBlECweijs6AzZOsQM_fmHIBC5e/view?usp=drive_link",
                    Category = "Fruit"
                },
                new DefaultCard
                {
                    //fig
                    Name = "fig",
                    AudioVoice =
                        "https://drive.google.com/file/d/1A9U0CfwChNBKplyhwmG0VinicVPZfzeb/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/13COW0JQdBocBudxBAakEW9oCDMHNYcXM/view?usp=drive_link",
                    Category = "Fruit"
                },
                new DefaultCard
                {
                    //mango
                    Name = "mango",
                    AudioVoice =
                        "https://drive.google.com/file/d/1GpwBGoJeuxNemZcWoGh-mXxjQWKX9-ln/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/11gOu9m_k45mO00z283oV9dYqf6zwgLnO/view?usp=drive_link",
                    Category = "Fruit"
                },
                new DefaultCard
                {
                    //apricot
                    Name = "apricot",
                    AudioVoice =
                        "https://drive.google.com/file/d/1GicYLbVlLD-j6t3ZvALpdFWil-iCdUjj/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1Df3Afo1RQbHmJLwHwb1kQ3_0z-q8zVAw/view?usp=drive_link",
                    Category = "Fruit"
                },
                new DefaultCard
                {
                    //grape
                    Name = "grape",
                    AudioVoice =
                        "https://drive.google.com/file/d/1WC6O7AUf2oOBqOWjkCqi-hQnk0pVjRra/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1xtguwxpKjdDyFOwsLutYnBX0ATk940eY/view?usp=drive_link",
                    Category = "Fruit"
                },
                new DefaultCard
                {
                    //melon
                    Name = "melon",
                    AudioVoice =
                        "https://drive.google.com/file/d/1wLupVUSZ4yyvRu3y2srtII0_wUiAFAWP/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1YmEZFq47LZIjmZ4SQ95jGymTurQD3C5e/view?usp=drive_link",
                    Category = "Fruit"
                },
                // new DefaultCard
                // {
                //     //
                //     Name = "kiwi",
                //     AudioVoice = "",
                //     Image = "",
                //     Category = "Fruit"
                // }
            };
            foreach (var dfCard in sampleData)
            {
                await defaultCardService.AddDefaultCardAsync(dfCard);
            }
            //DefaultCards.InsertMany(sampleData);
        }
    }
}