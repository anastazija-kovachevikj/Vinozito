using backend.data;
using backend.interfaces;
using backend.models;
using IdGen;
using MongoDB.Driver;

namespace backend.repositories;

public class CustomCardRepository(MongoDbContext context) : ICustomCardRepository
{
    public async Task<IEnumerable<CustomCard>> GetAllAsync()
    {
        return await context.CustomCards.Find(_ => true).ToListAsync();
    }

    public async Task<CustomCard> GetByIdAsync(string id)
    {
        return await context.CustomCards.Find(c => c.Id == id).FirstOrDefaultAsync();
    }

    public async Task AddAsync(CustomCard entity)
    {
        entity.Id = Guid.NewGuid().ToString(); 
        await context.CustomCards.InsertOneAsync(entity);
    }
    public async Task UpdateAsync(CustomCard entity)
    {
        await context.CustomCards.ReplaceOneAsync(c => c.Id == entity.Id, entity);
    }

    public async Task DeleteAsync(string id)
    {
        await context.CustomCards.DeleteOneAsync(c => c.Id == id);
    }
}
