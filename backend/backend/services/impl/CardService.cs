using backend.dto;
using backend.models;

namespace backend.services.impl;

public class CardService(
    ICustomCardService customCardService,
    IDefaultCardService defaultCardService) : ICardService
{
   public async Task<IEnumerable<CardDto>> GetCardsByUserId(string id)
{
    // Fetch custom and default cards, assigning empty lists if they are null
    var customCards = await customCardService.GetAllByUserId(id);
    var defaultCards = await defaultCardService.GetAllDefaultCardsAsync();
    
    // Filter out any null entries and any CustomCard with a null DefaultCardId
    var customCardDict = customCards
        .Where(cc => true)  // Check for non-null entries and valid DefaultCardId
        .ToDictionary(cc => cc.DefaultCardId);

    var cards = defaultCards.Select(dc =>
    {
        // If there's a matching custom card, use its properties; otherwise, fall back to default card properties
        if (dc.Id != null && customCardDict.TryGetValue(dc.Id, out var customCard))
        {
            return new CardDto(
                id: dc.Id,
                name: customCard.Title ?? dc.Name,  // Use default name if custom title is null
                audioVoice: customCard.VoiceAudio ?? dc.AudioVoice, // Use default audio if custom audio is null
                image: dc.Image,
                category: dc.Category,
                cardType: CardType.Custom
            );
        }

        // Create a CardDto using default card properties
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


    public async Task<IEnumerable<CardDto>> GetCardsByUserIdAndCategroyTask(string id, string category)
    {
        var cards = await GetCardsByUserId(id);
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