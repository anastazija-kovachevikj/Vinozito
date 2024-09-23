using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Security.Cryptography;
using System.Text;
using backend.dto;
using backend.models;
using backend.services;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using Microsoft.IdentityModel.Tokens;

namespace backend.controllers;

[ApiController]
[Route("api/auth")]
public class AuthController : ControllerBase
{
    private readonly IUserService _userService;
    //private readonly string _key = "This is my test key"; // Use configuration
    private readonly string _jwtSecret;
    
    public AuthController(IUserService userService, IConfiguration configuration)
    {
        _userService = userService;
        _jwtSecret = configuration["JwtSettings:Secret"]; // Load the secret key
    }


    [HttpPost("login")]
    public async Task<IActionResult> Login([FromBody] UserLoginDto userLogin)
    {
        var user = await _userService.AuthenticateAsync(userLogin.Username, userLogin.Password);
        if (user == null)
        {
            return Unauthorized();
        }

        var token = GenerateJwtToken(user);
        return Ok(new { Token = token });
    }

    [HttpPost("register")]
    public async Task<IActionResult> Register([FromBody] UserRegisterDto userRegister)
    {
        var existingUser = await _userService.GetUserByUsernameAsync(userRegister.Username);
        if (existingUser != null)
        {
            return BadRequest("Username is already taken.");
        }

        var user = new User
        {
            UserName = userRegister.Username,
            PasswordHash = HashPassword(userRegister.Password),
            Email = userRegister.Email// Replace with proper hashing
            // Role = userRegister.Role
        };

        // await _userService.RegisterUserAsync(userRegister.Username,HashPassword(userRegister.Password),userRegister.Email);
        // return Ok("User registered successfully.");
        try
        {
            await _userService.RegisterUserAsync(userRegister.Username, userRegister.Password, userRegister.Email);
            return Ok(user);
        }
        catch (InvalidOperationException ex)
        {
            return BadRequest(new { error = ex.Message });
        }
        catch (KeyNotFoundException ex)
        {
            return NotFound(new { error = ex.Message });
        }
        catch (Exception ex)
        {
            return StatusCode(StatusCodes.Status500InternalServerError, new { error = "An unexpected error occurred." });
        }
    }

    [HttpPost("logout")]
    public IActionResult Logout()
    {
        // No real server-side logout, just inform the client to remove the token
        return Ok("User logged out. Remove token from client.");
    }

    private string GenerateJwtToken(User user)
    {
        var key = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_jwtSecret));
        var creds = new SigningCredentials(key, SecurityAlgorithms.HmacSha256);

        var claims = new[]
        {
            new Claim(ClaimTypes.Name, user.UserName),
            //new Claim(ClaimTypes.Role, user.Role)
        };

        var token = new JwtSecurityToken(
            issuer: "VinozitoApp",
            audience: "VinozitoApp",
            claims: claims,
             
            signingCredentials: creds);

        return new JwtSecurityTokenHandler().WriteToken(token);
    }


    private string HashPassword(string password)
    {
        using (var sha512 = new SHA512CryptoServiceProvider())
        {
            var hashedBytes = sha512.ComputeHash(Encoding.UTF8.GetBytes(password));
            return Convert.ToBase64String(hashedBytes); // Convert the hashed bytes to a base64 string
        }
    }

    private bool VerifyPassword(string password, string storedHash)
    {
        using (var sha512 = new SHA512CryptoServiceProvider())
        {
            var hashedBytes = sha512.ComputeHash(Encoding.UTF8.GetBytes(password));
            var hashedPassword = Convert.ToBase64String(hashedBytes);

            return hashedPassword == storedHash; // Compare the stored hash with the newly hashed password
        }
    }

}