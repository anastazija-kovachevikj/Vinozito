namespace backend.data;

using backend.models;
using backend.services;
using System.Threading.Tasks;
using Microsoft.Extensions.DependencyInjection;

public static class DataSeeder
{
    public static async Task SeedDataAsync(IServiceProvider serviceProvider)
    {
        using var scope = serviceProvider.CreateScope();
        var defaultCardService = scope.ServiceProvider.GetRequiredService<IDefaultCardService>();

        var defaultCard = new DefaultCard()
        {
            AudioVoice = "https://drive.google.com/file/d/19PGZaHHmSfKC2roQsOj7qq2GuuHXuGUk/view?usp=drive_link",
            Image = "https://drive.google.com/file/d/1626mrBpH3uvODsaG8l9RTdQG7en_hKid/view?usp=drive_link",
            Category = "Fruit"
        };

        await defaultCardService.AddDefaultCardAsync(defaultCard);
    }
}