using backend.models;

namespace backend.interfaces;

public interface ICustomCardRepository
{
    Task<IEnumerable<CustomCard>> GetAllAsync();
    Task<CustomCard> GetByIdAsync(string id);
    Task AddAsync(CustomCard entity);
    Task UpdateAsync(CustomCard entity);
    Task DeleteAsync(string id);
}
