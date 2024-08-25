using backend.models;

namespace backend.services;

public interface IDefaultCardService
{
    Task<IEnumerable<DefaultCard>> GetAllDefaultCardsAsync();
    Task<DefaultCard> GetDefaultCardByIdAsync(string id);
    Task AddDefaultCardAsync(DefaultCard defaultCard);
    Task UpdateDefaultCardAsync(string id, DefaultCard defaultCard);
    Task DeleteDefaultCardAsync(string id);
}