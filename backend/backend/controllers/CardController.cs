using backend.services;
using Microsoft.AspNetCore.Mvc;
using System.Threading.Tasks;

namespace backend.controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class CardController(ICardService cardService) : ControllerBase
    {
        [HttpGet("{id}")]
        public async Task<IActionResult> GetCardsByUserId(string id)
        {
            var cards = await cardService.GetCardsByUserId(id);
            return Ok(cards);
        }
        
        [HttpGet("category/{id}")]
        public async Task<IActionResult> GetCardsByIdAndCategroyTask(string id,string category)
        {
            var cards = await cardService.GetCardsByUserIdAndCategroyTask(id, category);
            return Ok(cards);
        }
    }
}