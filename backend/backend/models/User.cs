using Microsoft.AspNetCore.Identity;

namespace backend.models;

public class User: IdentityUser
{
    //public string? Id { get; set; } 
    //public string UserName { get; set; } 
    //public string Email { get; set; } 
    //public string Password { get; set; } 
    public Settings? Settings { get; set; }

    public List<string>? CustomCardsIds { get; set; } = [];
}
