using backend.dto;
using backend.models;

namespace backend.services.impl;

public class CardService(
    ICustomCardService customCardService,
    IDefaultCardService defaultCardService) : ICardService
{
    public async Task<IEnumerable<CardDto>> GetCardsById(string id)
    {
        var customCards = await customCardService.GetAllByUserId(id);
        var defaultCards = await defaultCardService.GetAllDefaultCardsAsync();
        
        var customCardDict = customCards.ToDictionary(cc => cc.DefaultCardId);
        
        var cards = defaultCards.Select(dc =>
        {
            if (customCardDict.TryGetValue(dc.Id, out var customCard))
            {
                return new CardDto(
                    id: dc.Id,
                    name: dc.Name,
                    audioVoice: customCard.VoiceAudio,
                    image: dc.Image,
                    category: dc.Category,
                    cardType: CardType.Custom
                );
            }
        
            return new CardDto(
                id: dc.Id,
                name: dc.Name,
                audioVoice: dc.AudioVoice,
                image: dc.Image,
                category: dc.Category,
                cardType: CardType.Default
            );
        }).ToList();

        return cards;
    }

    public async Task<IEnumerable<CardDto>> GetCardsByIdAndCategroyTask(string id, string category)
    {
        var cards = await GetCardsById(id);
        var cardsCategory = new List<CardDto>();
        foreach (var card in cards)
        {
            if (card.Category == category)
            {
                cardsCategory.Add(card);
            }
        }
        return cardsCategory.ToList();
        
    }
    
}