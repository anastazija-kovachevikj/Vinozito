using backend.services;
using backend.services.impl;
using Microsoft.AspNetCore.Mvc;

namespace backend.controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class CardController(ICardService cardService) : ControllerBase
    {
        [HttpGet("{id}")]
        public async Task<IActionResult> GetCardsByUserId(string id)
        {
            var cards = await cardService.GetCardsById(id);
            return Ok(cards);
        }
    }
}
