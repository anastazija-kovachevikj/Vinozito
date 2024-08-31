using backend.interfaces;
using backend.models;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace backend.services.impl
{
    public class CustomCardService(ICustomCardRepository customCardRepository, UserService userService)
        : ICustomCardService
    {
        // Constructor for dependency injection

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

        public async Task<IEnumerable<CustomCard>> GetAllByUserId(string id)
        {
            var user = await userService.GetUserByIdAsync(id);

            if (user.CustomCardsIds == null || !user.CustomCardsIds.Any())
            {
                return Enumerable.Empty<CustomCard>();
            }

            var customCardTasks =
                user.CustomCardsIds.Select(async cardId => await customCardRepository.GetByIdAsync(cardId));

            var customCards = await Task.WhenAll(customCardTasks);

            return customCards.Where(c => true);
        }
    }
}