using backend.interfaces;
using backend.models;

namespace backend.services.impl;

public class UserService(IUserRepository userRepository, ICustomCardRepository customCardRepository)
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

    public async Task<IEnumerable<CustomCard>> GetAllCustomCardsAsync(string id)
    {
        var user = await userRepository.GetByIdAsync(id);

        if (user.CustomCardsIds == null || !user.CustomCardsIds.Any())
        {
            return Enumerable.Empty<CustomCard>();
        }

        var customCardTasks = user.CustomCardsIds.Select(async cardId => await customCardRepository.GetByIdAsync(cardId));

        var customCards = await Task.WhenAll(customCardTasks);

        return customCards.Where(c => true);
    }
  
}
