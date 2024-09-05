using backend.data;
using backend.interfaces;
using backend.models;
using MongoDB.Driver;

namespace backend.repositories;

public class DefaultCardRepository(MongoDbContext context) : IDefaultCardRepository
{
    private readonly IMongoCollection<DefaultCard> _defaultCards = context.DefaultCards;

    public async Task<IEnumerable<DefaultCard>> GetAllAsync()
    {
        return await _defaultCards.Find(_ => true).ToListAsync();
    }

    public async Task<DefaultCard> GetByIdAsync(string id)
    {
        return await _defaultCards.Find(dc => dc.Id == id).FirstOrDefaultAsync();
    }

    public async Task AddAsync(DefaultCard defaultCard)
    {
        defaultCard.Id = Guid.NewGuid().ToString(); 
        await _defaultCards.InsertOneAsync(defaultCard);
    }

    public async Task UpdateAsync(string id, DefaultCard defaultCard)
    {
        await _defaultCards.ReplaceOneAsync(dc => dc.Id == id, defaultCard);
    }

    public async Task DeleteAsync(string id)
    {
        await _defaultCards.DeleteOneAsync(dc => dc.Id == id);
    }
}