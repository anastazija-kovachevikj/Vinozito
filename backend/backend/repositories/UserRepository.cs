using backend.data;
using backend.interfaces;
using backend.models;
using MongoDB.Driver;

namespace backend.repositories
{
    public class UserRepository : IUserRepository
    {
        private readonly MongoDbContext _context;

        public UserRepository(MongoDbContext context)
        {
            _context = context;
        }

        public async Task<IEnumerable<User>> GetAllAsync()
        {
            return await _context.Users.Find(_ => true).ToListAsync();
        }

        public async Task<User> GetByIdAsync(string id)  // Change id to string, since MongoDB usually uses ObjectId as a string
        {
            return await _context.Users.Find(u => u.Id == id).FirstOrDefaultAsync();
        }

        public async Task AddAsync(User entity)
        {
            await _context.Users.InsertOneAsync(entity);
        }

        public async Task UpdateAsync(User entity)
        {
            await _context.Users.ReplaceOneAsync(u => u.Id == entity.Id, entity);
        }

        public async Task DeleteAsync(string id)
        {
            await _context.Users.DeleteOneAsync(u => u.Id == id);
        }

        public async Task<User> GetByUsernameAsync(string username)
        {
            return await _context.Users.Find(u => u.Username == username).FirstOrDefaultAsync();
        }
    }
}