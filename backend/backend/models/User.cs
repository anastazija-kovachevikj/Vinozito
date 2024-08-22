namespace backend.models;

public class User(string id, string username, string email, string pass)
{
    public string Id { get; set; } = id;
    public string Username { get; set; } = username;
    public string Email { get; set; } = email;
    public string Pass { get; set; } = pass;
}