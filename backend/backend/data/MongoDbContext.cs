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
                    Name = "црвена",
                    AudioVoice = "https://drive.google.com/file/d/149MenaUfuUIM05MXHeCA84xSCVaJ8zm3/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1K5p8F0ow93iQq-z3A7ubM_7Os4zqrmmy/view?usp=drive_link",
                    Category = "Colors"
                },
                new DefaultCard
                {
                    //
                    Name = "зелена",
                    AudioVoice = "https://drive.google.com/file/d/1_qt2iQDYX2RPEHaZCxX0g-Lug7YxG8jG/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/15wM-rRKUftjaJKkkyzCqHaBU_hzZYCrH/view?usp=drive_link",
                    Category = "Colors"
                },
                new DefaultCard
                {
                    //
                    Name = "сина",
                    AudioVoice = "https://drive.google.com/file/d/1OX5BXsLjqy0cCaLnRf1QHNiJwi9cIpf1/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/18a8m3i7TQJWBdNlLnJX-hPaC4S7yIyOU/view?usp=drive_link",
                    Category = "Colors"
                },
                new DefaultCard
                {
                    //
                    Name = "жолта",
                    AudioVoice = "https://drive.google.com/file/d/1RTdgW3pcJNMz0n4LC2GfOyQsNvJUmgdx/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1VZx9WFrQy9afkGd8fa4f37_ndhiBQO2e/view?usp=drive_link",
                    Category = "Colors"
                },
                new DefaultCard
                {
                    //
                    Name = "розева",
                    AudioVoice = "https://drive.google.com/file/d/1wRd2dH9sY1DYBfDQa_tpWl8Ky8uNW1NA/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1I5pXbSPAF_2wpY1rPOnmbf9ujoEhEZGX/view?usp=drive_link",
                    Category = "Colors"
                },
                new DefaultCard
                {
                    //
                    Name = "портокалова",
                    AudioVoice = "https://drive.google.com/file/d/1lcD7RTFx77E0xnJBDtAnOuf5YWWnvQ3U/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/19K2aJVvQ6AcSlbP09xrdqOkzn_FJyV5E/view?usp=drive_link",
                    Category = "Colors"
                },
                new DefaultCard
                {
                    //
                    Name = "виолетова",
                    AudioVoice = "https://drive.google.com/file/d/1pEfwEKlGHF4Sdy6QfRxVrLP0tGruNMlp/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1xgfoT70QJUgk7jB4N5giU-S_QV2mbpGw/view?usp=drive_link",
                    Category = "Colors"
                },
                new DefaultCard
                {
                    //
                    Name = "светлосина",
                    AudioVoice = "https://drive.google.com/file/d/1Ls_-RDN2NVcD0c9IIRmmiUWeb8NlxsgT/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1WFcCrRlXchftP0XOmLnFBfeShCpTZohw/view?usp=drive_link",
                    Category = "Colors"
                },   new DefaultCard
                {
                    //
                    Name = "светлорозева",
                    AudioVoice = "https://drive.google.com/file/d/109HfHgxLYBxH55Id5BiQRSd1RgsQe1Du/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1oDa8dZjrZCm_w6Vj4fZwRnEbE8SCuLKj/view?usp=drive_link",
                    Category = "Colors"
                },
                new DefaultCard
                {
                    //
                    Name = "тиркизна",
                    AudioVoice = "https://drive.google.com/file/d/1uB2qx0VAqvVhVBpc0SwqBASkth1itXEb/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1sDncZEGxIfgucIGdSysH8bZdx9nVPVF2/view?usp=drive_link",
                    Category = "Colors"
                },
                new DefaultCard
                {
                    //
                    Name = "темнозелена",
                    AudioVoice = "https://drive.google.com/file/d/1rpDN7s9A6QIKKKlSxT4ED9hDM93gy8g5/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1VMDGj_aSPagppDF3QmdHIZ537n8XXOMP/view?usp=drive_link",
                    Category = "Colors"
                },
                new DefaultCard
                {
                    //
                    Name = "кафеава",
                    AudioVoice = "https://drive.google.com/file/d/1TGh4SDIxDjo3MuuSr8bWzFMf-fvasIcm/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/10N16_It71VT291flji2MDal9JSXd2mw_/view?usp=drive_link",
                    Category = "Colors"
                },
                new DefaultCard
                {
                    //
                    Name = "бела",
                    AudioVoice = "https://drive.google.com/file/d/1lHv8qhGSdL-m4g5Z8_TNyH-VHnmp3k6-/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/19dhPRRnLONjONQN939N4JQ1zVLU4nye2/view?usp=drive_link",
                    Category = "Colors"
                },
                new DefaultCard
                {
                    //
                    Name = "црна",
                    AudioVoice = "https://drive.google.com/file/d/1YfPr8QmfYQ9e6mvO_4l_IXmiVVdI1c_A/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/1xsDX_lDZ4hgwQAGmxttQJxUd_ZKlLQGo/view?usp=drive_link",
                    Category = "Colors"
                },
                new DefaultCard
                {
                    //
                    Name = "сива",
                    AudioVoice = "https://drive.google.com/file/d/14pOG8BvO2U1wTYcw1NipQ4x_kTp1W3bQ/view?usp=drive_link",
                    Image = "https://drive.google.com/file/d/178njWBryLdPUQoRgG4G7K6iW8kv4-E5G/view?usp=drive_link",
                    Category = "Colors"
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