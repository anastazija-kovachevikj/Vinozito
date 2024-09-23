using backend.models;

namespace backend.interfaces
{
    public interface IUserRepository
    {
        Task<IEnumerable<User>> GetAllAsync();
        Task<User> GetByIdAsync(string id);  // Assuming id is of type string for MongoDB
        Task AddAsync(User entity);
        Task UpdateAsync(User entity);
        Task DeleteAsync(string id);
        Task<User> GetByUsernameAsync(string username);
        //Task<IEnumerable<CustomCard>> GetCustomCardsAsync(string id);
    }
}