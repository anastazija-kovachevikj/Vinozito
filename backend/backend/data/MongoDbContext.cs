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
                    //banana
                    Name = "банана",
                    AudioVoice = "https://drive.google.com/file/d/1alN5m-80aueWXtaqx7I2zVhMokRMAW4r/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1eYKPF0vGGobnESNfPMI-aB7uNeIkW1kQ/view?usp=drive_link",
                    Category = "Fruit"
                },
                new DefaultCard
                {
                    //carrot
                    Name = "морков",
                    AudioVoice = "https://drive.google.com/file/d/1JvE-FA1YglQprMM4e3hSVE-4JMeKQRz8/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1AY4CiOHnhNS5U6kEMi8Ox0m0e9ap4gRY/view?usp=drive_link",
                    Category = "Vegetable"
                },

                new DefaultCard
                {
                    //cherry
                    Name = "цреша",
                    AudioVoice = "https://drive.google.com/file/d/1Zzo2ELdQdxiLx11MarTPVG6yJHR2aovX/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1mpgMCVTYpCHxLeY916tG1nA7Nnc2l04p/view?usp=drive_link",
                    Category = "Fruit"
                },


                new DefaultCard
                {
                    //lemon
                    Name = "лимон",
                    AudioVoice = "https://drive.google.com/file/d/1gfJu770pfnvbZDRaTMiKz8t8Q0iOFRAj/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1Lj9F8CoynYwRbtxfx5OAp8kZ2oLl8l8b/view?usp=drive_link",
                    Category = "Fruit"
                },
                new DefaultCard
                {
                    //orange
                    Name = "портокал",
                    AudioVoice = "https://drive.google.com/file/d/1QWtA_wAURMglAckEUN5AZQpbnyXKdXxC/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/12tUMKQod9Oq-rEIFUB5c5FvDtUeEDvbp/view?usp=drive_link",
                    Category = "Fruit"
                },
                new DefaultCard
                {
                    //peach
                    Name = "праска",
                    AudioVoice = "https://drive.google.com/file/d/1-XLXjG3Xk94WX2BjidUqW0sI_ARIO0iq/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/11h0YZPMX29yqVOzHOXkukswoaYSB6EOe/view?usp=drive_link",
                    Category = "Fruit"
                },
                new DefaultCard
                {
                    //pear
                    Name = "круша",
                    AudioVoice = "https://drive.google.com/file/d/1uHJce_sxsWutGFcBeRW42zjL4NcPkFfh/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1G8qmVlK2KPtIVBHw51Fh4Ig0ox9FQnji/view?usp=drive_link",
                    Category = "Fruit"
                },
                new DefaultCard
                {
                    //pineapple
                    Name = "ананас",
                    AudioVoice = "https://drive.google.com/file/d/1BuZ2IuaXzSmEs-aLnQnHirGH9JtgXbAX/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1nQmIM5m0BG-4HrARQF-RCxd2njXLWElK/view?usp=drive_link",
                    Category = "Fruit"
                },
                new DefaultCard
                {
                    //pomegranate
                    Name = "калинка",
                    AudioVoice = "https://drive.google.com/file/d/1kpE6luTljXEdsdPx97STI3cW0DqCtOT5/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1pSsYnznyvuU_J0M_Q2Uov8x_lnTLIe_I/view?usp=drive_link",
                    Category = "Fruit"
                },
                new DefaultCard
                {
                    //raspberry
                    Name = "малина",
                    AudioVoice =
                        "https://drive.google.com/file/d/1Y5cD5FF5M4yvBFjQDUZHdtYUQSV9yE4K/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1DqGVfU-bCNP_ybqiqU1vyB-udh60fGkN/view?usp=drive_link",
                    Category = "Fruit"
                },
                new DefaultCard
                {
                    //strawberry
                    Name = "јагода",
                    AudioVoice = "https://drive.google.com/file/d/1xnSrCsRlqFXZ_g-ZmNv-mJ-KPb_QGaBN/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1pMa9dGovXkLh1k8eHSOicpFToirNqXQC/view?usp=drive_link",
                    Category = "Fruit"
                },
                new DefaultCard
                {
                    //tangerine
                    Name = "мандарина",
                    AudioVoice = "https://drive.google.com/file/d/1Ww6-vMISa65dOGX3W-nPyQr831EQm1of/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1wKh3EUBxFpKtMV1WSpAh-u3v-3M5ucye/view?usp=drive_link",
                    Category = "Fruit"
                },
                new DefaultCard
                {
                    //watermelon
                    Name = "лубеница",
                    AudioVoice = "https://drive.google.com/file/d/16pJYexVGA7MqY7FLOY2ldYLeMjl4dGAT/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1satJLDffuMPlSTE_Su8Eth9CoQJLZ_5q/view?usp=drive_link",
                    Category = "Fruit"
                },
                new DefaultCard
                {
                    //grapefruit
                    Name = "цитрон",
                    AudioVoice = "https://drive.google.com/file/d/1Glc76Wu9o7-r3ZReBx3B45jycSCYMINT/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1bMOgEfBlECweijs6AzZOsQM_fmHIBC5e/view?usp=drive_link",
                    Category = "Fruit"
                },
                new DefaultCard
                {
                    //fig
                    Name = "смоква",
                    AudioVoice = "https://drive.google.com/file/d/1A9U0CfwChNBKplyhwmG0VinicVPZfzeb/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/13COW0JQdBocBudxBAakEW9oCDMHNYcXM/view?usp=drive_link",
                    Category = "Fruit"
                },
                new DefaultCard
                {
                    //mango
                    Name = "манго",
                    AudioVoice = "https://drive.google.com/file/d/1GpwBGoJeuxNemZcWoGh-mXxjQWKX9-ln/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/11gOu9m_k45mO00z283oV9dYqf6zwgLnO/view?usp=drive_link",
                    Category = "Fruit"
                },
                new DefaultCard
                {
                    //apricot
                    Name = "кајсија",
                    AudioVoice = "https://drive.google.com/file/d/1GicYLbVlLD-j6t3ZvALpdFWil-iCdUjj/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1Df3Afo1RQbHmJLwHwb1kQ3_0z-q8zVAw/view?usp=drive_link",
                    Category = "Fruit"
                },
                new DefaultCard
                {
                    //grape
                    Name = "грозје",
                    AudioVoice = "https://drive.google.com/file/d/1WC6O7AUf2oOBqOWjkCqi-hQnk0pVjRra/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1xtguwxpKjdDyFOwsLutYnBX0ATk940eY/view?usp=drive_link",
                    Category = "Fruit"
                },
                new DefaultCard
                {
                    //melon
                    Name = "диња",
                    AudioVoice = "https://drive.google.com/file/d/1wLupVUSZ4yyvRu3y2srtII0_wUiAFAWP/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1YmEZFq47LZIjmZ4SQ95jGymTurQD3C5e/view?usp=drive_link",
                    Category = "Fruit"
                },
                new DefaultCard
                {
                    //spinach
                    Name = "спанаќ",
                    AudioVoice = "https://drive.google.com/file/d/1g_j4l-a4jYwPPvJx-dm7M4yykfrtNTNo/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1h7IrUVtsfGrxCKFmQtJvrwk1GUXGQS0z/view?usp=drive_link",
                    Category = "Vegetable"
                },
                new DefaultCard
                {
                    //asparagus
                    Name = "аспарагус",
                    AudioVoice = "https://drive.google.com/file/d/1puwk2pWfJEoOc10Vwdt7OjL6A8rsio43/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1VU8htL-cz5rFDAI122Wdk_7fadOxyD5d/view?usp=drive_link",
                    Category = "Vegetable"
                },
                new DefaultCard
                {
                    //zucchini
                    Name = "тиквица",
                    AudioVoice = "https://drive.google.com/file/d/1AcOMnaE9htMnpOp7jLTW3a7Zj39NAOxH/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1e0FgrU8y06tZ180ZbGy4_KJVaK0hP_Za/view?usp=drive_link",
                    Category = "Vegetable"
                },
                new DefaultCard
                {
                    //sweet potato
                    Name = "домат",
                    AudioVoice = "https://drive.google.com/file/d/189wfCpjSABHT6rDeAjf6sN6bY9fND-24/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1UOtgJCFGKMkdhrZ9fcafYStPmaPa-Z33/view?usp=drive_link",
                    Category = "Vegetable"
                },
                new DefaultCard
                {
                    //sweet potato
                    Name = "компир",
                    AudioVoice = "https://drive.google.com/file/d/1nWYmsqcZpFxF-MOvXOsZXdqEtrBPtVfO/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1184M6fI4pyTpVtvG8qDVmIBkghT_7oqN/view?usp=drive_link",
                    Category = "Vegetable"
                },
                new DefaultCard
                {
                    //pumpkin
                    Name = "кромид",
                    AudioVoice = "https://drive.google.com/file/d/1n9f1v3FHNdLb6VxJr_kMny5_ouxwJGEP/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1W4-fbl-RKA1TEmZ0Ih_FTT2J_hei7Ni_/view?usp=drive_link",
                    Category = "Vegetable"
                },
                new DefaultCard
                {
                    //tomato
                    Name = "пиперка",
                    AudioVoice = "https://drive.google.com/file/d/1QlqC4nfXsOScy22dsUl9SaUcPphX6FiL/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1JyMLsMJNvUnqLrGbfXtnym-cYr_Uvv_D/view?usp=drive_link",
                    Category = "Vegetable"
                },
                new DefaultCard
                {
                    //potato
                    Name = "крставица",
                    AudioVoice = "https://drive.google.com/file/d/19agQJxO5s2E9wEvKjNQbvzaJcZjFUlec/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1djfKOYHu-jKkLcbZpajXsMNQJjExBggb/view?usp=drive_link",
                    Category = "Vegetable"
                },
                new DefaultCard
                {
                    //banana
                    Name = "зелка",
                    AudioVoice = "https://drive.google.com/file/d/1gUQURlOnGdGcGBq83tHjsIe_CGhDdkkF/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1Psh2a_8Z2pTsAU9zmj1r7GummlsfcmR6/view?usp=drive_link",
                    Category = "Vegetable"
                },
                new DefaultCard
                {
                    //banana
                    Name = "модар патлиџан",
                    AudioVoice = "https://drive.google.com/file/d/1O_zVWQY38PGNVZ2DBw14-hxvbUElR-Ss/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1CnJGocGrgbY7zj0eGJosx0IEHrAjHd4y/view?usp=drive_link",
                    Category = "Vegetable"
                },
                new DefaultCard
                {
                    //banana
                    Name = "печурки",
                    AudioVoice = "https://drive.google.com/file/d/1wm7e23l6-uvZ74UTE4VuFljATX8al7zC/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1sJWEiZxdAGXeGw1hM5COTjUk1mMgnUyD/view?usp=drive_link",
                    Category = "Vegetable"
                },
                new DefaultCard
                {
                    //banana
                    Name = "грашок",
                    AudioVoice = "https://drive.google.com/file/d/1Df_Dc79akwtX8PiY4og10Xrpc4ITmgy5/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1qFIAK-On1qLseRuvd83yywF9ukxED2hS/view?usp=drive_link",
                    Category = "Vegetable"
                },
                new DefaultCard
                {
                    //banana
                    Name = "пченка",
                    AudioVoice = "https://drive.google.com/file/d/13mUvh-zeT7zj2mn3TKd4IfYBXxOVKMU8/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1paWMxfdEGxm7lZS9lgjErAxym2n5QrCa/view?usp=drive_link",
                    Category = "Vegetable"
                },
                new DefaultCard
                {
                    //banana
                    Name = "брокула",
                    AudioVoice = "https://drive.google.com/file/d/1585c9dYb8sW5eXMJ8yKfZ0moLyglOkt1/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/13W-p8GqWB6P5mtqmDl83sziuyszNag1d/view?usp=drive_link",
                    Category = "Vegetable"
                },
                new DefaultCard
                {
                    //banana
                    Name = "цвекло",
                    AudioVoice = "https://drive.google.com/file/d/1aaZuQQBZLhRWouk9DJIeZXVmSgjr7mVV/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/11-HvuYo0FZY0SPGwGdTxLj_BAB14lZ43/view?usp=drive_link",
                    Category = "Vegetable"
                },
                new DefaultCard
                {
                    //banana
                    Name = "карфиол",
                    AudioVoice = "https://drive.google.com/file/d/1DUoqSJN7MPiBaki-K9PZYJER-U2jeDGW/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1NsqctWYgQfRwVNEQ1oz-Jpz6KCOQoUyG/view?usp=drive_link",
                    Category = "Vegetable"
                },
                new DefaultCard
                {
                    //banana
                    Name = "сладок компир",
                    AudioVoice = "https://drive.google.com/file/d/15oK4Zz_B95SNAgB-lj2CLFo87MctBrFe/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/11519m2ntQzWsq3xu4_bB0PeWC-8APPjw/view?usp=drive_link",
                    Category = "Vegetable"
                },
                new DefaultCard
                {
                    //banana
                    Name = "тиква",
                    AudioVoice = "https://drive.google.com/file/d/16sZmT3rYtNWQhkGdfgLIr5fYc-Mu65ax/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1dEisuzqy6_AFbTRI2Y8pvKHA3ED0mPcv/view?usp=drive_link",
                    Category = "Vegetable"
                },
                new DefaultCard
                {
                    //banana
                    Name = "марула",
                    AudioVoice = "https://drive.google.com/file/d/1HPM6a6jK9k919HPAh2GGdB6NigydaWbj/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1L3v8dVCiC6PxHx3Ysn0lKs7aYZS4OKlF/view?usp=drive_link",
                    Category = "Vegetable"
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

            DefaultCards.InsertMany(sampleData);
        }
    }
}