using backend.interfaces;
using backend.models;

namespace backend.services.impl;

public class UserService(IUserRepository userRepository)
    : IUserService
{
    public async Task<IEnumerable<User>> GetAllUsersAsync()
    {
        return await userRepository.GetAllAsync();
    }

    public async Task<User> GetUserByIdAsync(string id)
    {
        var user = await userRepository.GetByIdAsync(id);
        if (user == null)
        {
            throw new KeyNotFoundException("User not found");
        }
        return user;
    }

    public async Task AddUserAsync(User user)
    {
        // Add any validation logic here (e.g., checking if the username already exists)
        var existingUser = await userRepository.GetByUsernameAsync(user.Username);
        if (existingUser != null)
        {
            throw new InvalidOperationException("Username already exists");
        }

        await userRepository.AddAsync(user);
    }

    public async Task UpdateUserAsync(User user)
    {
        var existingUser = await userRepository.GetByIdAsync(user.Id);
        if (existingUser == null)
        {
            throw new KeyNotFoundException("User not found");
        }

        await userRepository.UpdateAsync(user);
    }

    public async Task DeleteUserAsync(string id)
    {
        var user = await userRepository.GetByIdAsync(id);
        if (user == null)
        {
            throw new KeyNotFoundException("User not found");
        }

        await userRepository.DeleteAsync(id);
    }

    public async Task<User> GetUserByUsernameAsync(string username)
    {
        var user = await userRepository.GetByUsernameAsync(username);
        if (user == null)
        {
            throw new KeyNotFoundException("User not found");
        }
        return user;
    }

    public async Task AddCustomCardToListAsync(string userId,string customCardId)
    {
        var user = await userRepository.GetByIdAsync(userId);
        if (user == null)
        {
            throw new KeyNotFoundException("User not found");
        }
        
        user.CustomCardsIds?.Add(customCardId);

        await userRepository.UpdateAsync(user);
    }
}
