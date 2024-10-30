using backend.interfaces;
using backend.models;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace backend.services.impl
{
    public class CustomCardService(ICustomCardRepository customCardRepository, IUserService userService)
        : ICustomCardService
    {
     

        public async Task<IEnumerable<CustomCard>> GetAllAsync()
        {
            return await customCardRepository.GetAllAsync();
        }

        public async Task<CustomCard> GetByIdAsync(string id)
        {
            return await customCardRepository.GetByIdAsync(id);
        }

        public async Task AddAsync(CustomCard entity, string userId)
        {
            await customCardRepository.AddAsync(entity);
            await userService.AddCustomCardToListAsync(userId, entity.Id);
            
        }

        public async Task UpdateAsync(CustomCard entity)
        {
            await customCardRepository.UpdateAsync(entity);
        }

        public async Task DeleteAsync(string id)
        {   
            CustomCard card = await customCardRepository.GetByIdAsync(id);
            await customCardRepository.DeleteAsync(id);
            await userService.RemoveCustomCardFromListAsync(card.UserId, id);
            
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