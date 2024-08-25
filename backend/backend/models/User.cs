namespace backend.models;

public class User(string id, string username, string email, string password)
{
    public string Id { get; set; } = id;
    public string Username { get; set; } = username;
    public string Email { get; set; } = email;
    public string Password { get; set; } = password;

    public List<string>? CustomCardsIds { get; set; } = [];
}
