using backend.data;
using backend.interfaces;
using backend.models;
using MongoDB.Driver;

namespace backend.repositories;

public class UserRepository(MongoDbContext context) : IUserRepository
{
    public async Task<IEnumerable<User>> GetAllAsync()
    {
        return await context.Users.Find(_ => true).ToListAsync();
    }

    public async Task<User> GetByIdAsync(string id)
    {
        return await context.Users.Find(u => u.Id == id).FirstOrDefaultAsync();
    }

    public async Task AddAsync(User entity)
    {
        entity.Id = Guid.NewGuid().ToString(); 
        await context.Users.InsertOneAsync(entity);
    }

    public async Task UpdateAsync(User entity)
    {
        await context.Users.ReplaceOneAsync(u => u.Id == entity.Id, entity);
    }

    public async Task DeleteAsync(string id)
    {
        await context.Users.DeleteOneAsync(u => u.Id == id);
    }

    public async Task<User> GetByUsernameAsync(string username)
    {
        return await context.Users.Find(u => u.UserName == username).FirstOrDefaultAsync();
    }
}