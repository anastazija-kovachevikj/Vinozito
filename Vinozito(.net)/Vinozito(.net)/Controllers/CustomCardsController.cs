using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Vinozito_.net_.Data;
using Vinozito_.net_.Models;

namespace Vinozito_.net_.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class CustomCardsController : ControllerBase
    {
        private readonly ApplicationDbContext _context;

        public CustomCardsController(ApplicationDbContext context)
        {
            _context = context;
        }

        // GET: api/CustomCards
        [HttpGet]
        public async Task<ActionResult<IEnumerable<CustomCard>>> GetCustomCard()
        {
            return await _context.CustomCard.ToListAsync();
        }

        // GET: api/CustomCards/5
        [HttpGet("{id}")]
        public async Task<ActionResult<CustomCard>> GetCustomCard(int id)
        {
            var customCard = await _context.CustomCard.FindAsync(id);

            if (customCard == null)
            {
                return NotFound();
            }

            return customCard;
        }

        // PUT: api/CustomCards/5
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPut("{id}")]
        public async Task<IActionResult> PutCustomCard(int id, CustomCard customCard)
        {
            if (id != customCard.Id)
            {
                return BadRequest();
            }

            _context.Entry(customCard).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!CustomCardExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return NoContent();
        }

        // POST: api/CustomCards
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPost]
        public async Task<ActionResult<CustomCard>> PostCustomCard(CustomCard customCard)
        {
            _context.CustomCard.Add(customCard);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetCustomCard", new { id = customCard.Id }, customCard);
        }

        // DELETE: api/CustomCards/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteCustomCard(int id)
        {
            var customCard = await _context.CustomCard.FindAsync(id);
            if (customCard == null)
            {
                return NotFound();
            }

            _context.CustomCard.Remove(customCard);
            await _context.SaveChangesAsync();

            return NoContent();
        }

        private bool CustomCardExists(int id)
        {
            return _context.CustomCard.Any(e => e.Id == id);
        }
    }
}
