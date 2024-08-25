using backend.models;
using backend.services;
using Microsoft.AspNetCore.Mvc;

namespace backend.controllers;

[ApiController]
[Route("api/[controller]")]
public class DefaultCardController(IDefaultCardService defaultCardService) : ControllerBase
{
    [HttpGet]
    public async Task<IActionResult> GetAllDefaultCards()
    {
        var defaultCards = await defaultCardService.GetAllDefaultCardsAsync();
        return Ok(defaultCards);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetDefaultCardById(string id)
    {
        var defaultCard = await defaultCardService.GetDefaultCardByIdAsync(id);
        return Ok(defaultCard);
    }

    [HttpPost]
    public async Task<IActionResult> CreateDefaultCard([FromBody] DefaultCard defaultCard)
    {
        await defaultCardService.AddDefaultCardAsync(defaultCard);
        return CreatedAtAction(nameof(GetDefaultCardById), new { id = defaultCard.Id }, defaultCard);
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> UpdateDefaultCard(string id, [FromBody] DefaultCard defaultCard)
    {
        await defaultCardService.UpdateDefaultCardAsync(id, defaultCard);
        return NoContent();
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteDefaultCard(string id)
    {
        await defaultCardService.DeleteDefaultCardAsync(id);
        return NoContent();
    }
}
