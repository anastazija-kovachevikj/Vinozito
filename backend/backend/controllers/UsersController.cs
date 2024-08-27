using backend.interfaces;
using backend.models;
using backend.services;
using Microsoft.AspNetCore.Mvc;

namespace backend.controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class UserController : ControllerBase
    {
        private readonly IUserService _userService;
     
        public UserController(IUserService userService)
        {
            _userService = userService;
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<User>>> GetAllUsers()
        {
            var users = await _userService.GetAllUsersAsync();
            return Ok(users);
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<User>> GetUserById(string id)
        {
            try
            {
                var user = await _userService.GetUserByIdAsync(id);
                return Ok(user);
            }
            catch (KeyNotFoundException)
            {
                return NotFound(new { message = "User not found" });
            }
        }

        [HttpPost]
        public async Task<ActionResult> AddUser(User user)
        {
            try
            {
                await _userService.AddUserAsync(user);
                return CreatedAtAction(nameof(GetUserById), new { id = user.Id }, user);
            }
            catch (InvalidOperationException ex)
            {
                return BadRequest(new { message = ex.Message });
            }
        }

        [HttpPut("{id}")]
        public async Task<ActionResult> UpdateUser(string id, User user)
        {
            if (id != user.Id)
            {
                return BadRequest(new { message = "User ID mismatch" });
            }

            try
            {
                await _userService.UpdateUserAsync(user);
                return NoContent();
            }
            catch (KeyNotFoundException)
            {
                return NotFound(new { message = "User not found" });
            }
        }

        [HttpDelete("{id}")]
        public async Task<ActionResult> DeleteUser(string id)
        {
            try
            {
                await _userService.DeleteUserAsync(id);
                return NoContent();
            }
            catch (KeyNotFoundException)
            {
                return NotFound(new { message = "User not found" });
            }
        }

        [HttpGet("username/{username}")]
        public async Task<ActionResult<User>> GetUserByUsername(string username)
        {
            try
            {
                var user = await _userService.GetUserByUsernameAsync(username);
                return Ok(user);
            }
            catch (KeyNotFoundException)
            {
                return NotFound(new { message = "User not found" });
            }
        }
    }
}
