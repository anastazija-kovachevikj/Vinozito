using backend.models;

namespace backend.interfaces;

public interface IDefaultCardRepository
{
    Task<IEnumerable<DefaultCard>> GetAllAsync();
    Task<DefaultCard> GetByIdAsync(string id);
    Task AddAsync(DefaultCard defaultCard);
    Task UpdateAsync(string id, DefaultCard defaultCard);
    Task DeleteAsync(string id);
}