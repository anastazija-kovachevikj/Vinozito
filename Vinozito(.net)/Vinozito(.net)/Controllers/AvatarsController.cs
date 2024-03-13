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
    public class AvatarsController : ControllerBase
    {
        private readonly ApplicationDbContext _context;

        public AvatarsController(ApplicationDbContext context)
        {
            _context = context;
        }

        // GET: api/Avatars
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Avatar>>> GetAvatar()
        {
            return await _context.Avatar.ToListAsync();
        }

        // GET: api/Avatars/5
        [HttpGet("{id}")]
        public async Task<ActionResult<Avatar>> GetAvatar(int id)
        {
            var avatar = await _context.Avatar.FindAsync(id);

            if (avatar == null)
            {
                return NotFound();
            }

            return avatar;
        }

        // PUT: api/Avatars/5
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPut("{id}")]
        public async Task<IActionResult> PutAvatar(int id, Avatar avatar)
        {
            if (id != avatar.Id)
            {
                return BadRequest();
            }

            _context.Entry(avatar).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!AvatarExists(id))
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

        // POST: api/Avatars
        // To protect from overposting attacks, see https://go.microsoft.com/fwlink/?linkid=2123754
        [HttpPost]
        public async Task<ActionResult<Avatar>> PostAvatar(Avatar avatar)
        {
            _context.Avatar.Add(avatar);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetAvatar", new { id = avatar.Id }, avatar);
        }

        // DELETE: api/Avatars/5
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteAvatar(int id)
        {
            var avatar = await _context.Avatar.FindAsync(id);
            if (avatar == null)
            {
                return NotFound();
            }

            _context.Avatar.Remove(avatar);
            await _context.SaveChangesAsync();

            return NoContent();
        }

        private bool AvatarExists(int id)
        {
            return _context.Avatar.Any(e => e.Id == id);
        }
    }
}
