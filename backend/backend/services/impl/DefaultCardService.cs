using backend.interfaces;
using backend.models;

namespace backend.services.impl;

public class DefaultCardService(IDefaultCardRepository defaultCardRepository) : IDefaultCardService
{
    private readonly IDefaultCardRepository _defaultCardRepository = defaultCardRepository;

    public async Task<IEnumerable<DefaultCard>> GetAllDefaultCardsAsync()
    {
        return await _defaultCardRepository.GetAllAsync();
    }

    public async Task<DefaultCard> GetDefaultCardByIdAsync(string id)
    {
        return await _defaultCardRepository.GetByIdAsync(id);
    }

    public async Task AddDefaultCardAsync(DefaultCard defaultCard)
    {
        await _defaultCardRepository.AddAsync(defaultCard);
    }

    public async Task UpdateDefaultCardAsync(string id, DefaultCard defaultCard)
    {
        await _defaultCardRepository.UpdateAsync(id, defaultCard);
    }

    public async Task DeleteDefaultCardAsync(string id)
    {
        await _defaultCardRepository.DeleteAsync(id);
    }
}
