using backend.models;

namespace backend.services;

public interface ICustomCardService
{
    Task<IEnumerable<CustomCard>> GetAllAsync();
    Task<CustomCard> GetByIdAsync(string id);
    Task AddAsync(CustomCard entity, string userId);
    Task UpdateAsync(CustomCard entity);
    Task DeleteAsync(string id);
    Task<IEnumerable<CustomCard>> GetAllByUserId(string id);
}
