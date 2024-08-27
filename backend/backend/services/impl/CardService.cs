using backend.dto;
using backend.models;

namespace backend.services.impl;

public class CardService(
    CustomCardService customCardService,
    DefaultCardService defaultCardService) : ICardService
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
                    audioVoice: customCard.VoiceAudio,
                    image: dc.Image,
                    category: dc.Category,
                    cardType: CardType.Custom
                );
            }

            return new CardDto(
                id: dc.Id,
                audioVoice: dc.AudioVoice,
                image: dc.Image,
                category: dc.Category,
                cardType: CardType.Default
            );
        }).ToList();

        return cards;
    }
}