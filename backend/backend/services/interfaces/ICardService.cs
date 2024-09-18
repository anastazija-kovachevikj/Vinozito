using backend.dto;

namespace backend.services;

public interface ICardService
{
    Task<IEnumerable<CardDto>> GetCardsById(string id);
    Task<IEnumerable<CardDto>> GetCardsByIdAndCategroyTask(string id,string category);
}