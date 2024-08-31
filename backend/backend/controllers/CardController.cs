using backend.services;
using Microsoft.AspNetCore.Mvc;
using System.Threading.Tasks;

namespace backend.controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class CardController : ControllerBase
    {
        private readonly ICardService _cardService;

        public CardController(ICardService cardService)
        {
            _cardService = cardService;
        }

        [HttpGet("{id}")]
        public async Task<IActionResult> GetCardsByUserId(string id)
        {
            var cards = await _cardService.GetCardsById(id);
            return Ok(cards);
        }
    }
}