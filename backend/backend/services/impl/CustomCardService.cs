using backend.interfaces;
using backend.models;

namespace backend.services.impl;

public class CustomCardService(ICustomCardRepository customCardRepository,UserService userService) : ICustomCardService
{
    public async Task<IEnumerable<CustomCard>> GetAllAsync()
    {
        return await customCardRepository.GetAllAsync();
    }

    public async Task<CustomCard> GetByIdAsync(string id)
    {
        return await customCardRepository.GetByIdAsync(id);
    }

    public async Task AddAsync(CustomCard entity)
    {
        await customCardRepository.AddAsync(entity);
    }

    public async Task UpdateAsync(CustomCard entity)
    {
        await customCardRepository.UpdateAsync(entity);
    }

    public async Task DeleteAsync(string id)
    {
        await customCardRepository.DeleteAsync(id);
    }

    public Task<IEnumerable<CustomCard>> GetAllByUserId(string id)
    {
        return userService.GetAllCustomCardsAsync(id);
    }
}
