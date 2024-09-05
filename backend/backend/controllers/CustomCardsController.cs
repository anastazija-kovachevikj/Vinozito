using backend.models;
using backend.services;
using Microsoft.AspNetCore.Mvc;

namespace backend.controllers;

[ApiController]
[Route("api/[controller]")]
public class CustomCardController(ICustomCardService customCardService) : ControllerBase
{
    [HttpGet]
    public async Task<IActionResult> GetAll()
    {
        var customCards = await customCardService.GetAllAsync();
        return Ok(customCards);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetById(string id)
    {
        var customCard = await customCardService.GetByIdAsync(id);
        return Ok(customCard);
    }

    [HttpPost]
    public async Task<IActionResult> Add([FromBody] CustomCard customCard, string userId)
    {
        if (!ModelState.IsValid)
        {
            return BadRequest(ModelState);
        }

        await customCardService.AddAsync(customCard,userId);
        return CreatedAtAction(nameof(GetById), new { id = customCard.Id }, customCard);
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> Update(string id, [FromBody] CustomCard customCard)
    {
        if (id != customCard.Id)
        {
            return BadRequest("ID mismatch");
        }

        var existingCard = await customCardService.GetByIdAsync(id);

        await customCardService.UpdateAsync(customCard);
        return NoContent();
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> Delete(string id)
    {
        var existingCard = await customCardService.GetByIdAsync(id);

        await customCardService.DeleteAsync(id);
        return NoContent();
    }
}
