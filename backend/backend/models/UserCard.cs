namespace backend.models;

public class UserCards(int userId, int customCardId, User user, CustomCard customCard)
{
    public int UserId { get; set; } = userId;
    public int CustomCardId { get; set; } = customCardId;

    public User User { get; set; } = user;
    public CustomCard CustomCard { get; set; } = customCard;
}