using backend.dto;

namespace backend.services;

public interface ICardService
{
    Task<IEnumerable<CardDto>> GetCardsByUserId(string id);
    Task<IEnumerable<CardDto>> GetCardsByUserIdAndCategroyTask(string id,string category);
}