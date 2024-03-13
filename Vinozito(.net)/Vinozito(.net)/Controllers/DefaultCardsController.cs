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
    public class DefaultCardsController : ControllerBase
    {
        private readonly ApplicationDbContext _context;

        public DefaultCardsController(ApplicationDbContext context)
        {
            _context = context;
        }

        // GET: api/DefaultCards
        [HttpGet]
        public async Task<ActionResult<IEnumerable<DefaultCard>>> GetDefaultCard()
        {
            return await _context.DefaultCard.ToListAsync();
        }

        // GET: api/DefaultCards/5
        [HttpGet("{id}")]
        public async Task<ActionResult<DefaultCard>> GetDefaultCard(int id)
        {
            var defaultCard = await _context.DefaultCard.FindAsync(id);

            if (defaultCard == null)
            {
                return NotFound();
            }

            return defaultCard;
        }

        // PUT: api/DefaultCards/5
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPut("{id}")]
        public async Task<IActionResult> PutDefaultCard(int id, DefaultCard defaultCard)
        {
            if (id != defaultCard.Id)
            {
                return BadRequest();
            }

            _context.Entry(defaultCard).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!DefaultCardExists(id))
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

        // POST: api/DefaultCards
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPost]
        public async Task<ActionResult<DefaultCard>> PostDefaultCard(DefaultCard defaultCard)
        {
            _context.DefaultCard.Add(defaultCard);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetDefaultCard", new { id = defaultCard.Id }, defaultCard);
        }

        // DELETE: api/DefaultCards/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteDefaultCard(int id)
        {
            var defaultCard = await _context.DefaultCard.FindAsync(id);
            if (defaultCard == null)
            {
                return NotFound();
            }

            _context.DefaultCard.Remove(defaultCard);
            await _context.SaveChangesAsync();

            return NoContent();
        }

        private bool DefaultCardExists(int id)
        {
            return _context.DefaultCard.Any(e => e.Id == id);
        }
    }
}
