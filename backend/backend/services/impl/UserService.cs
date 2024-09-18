using System.Security.Cryptography;
using System.Text;
using backend.interfaces;
using backend.models;
using backend.services;

public class UserService(IUserRepository userRepository)
    : IUserService
{
    public async Task<User> AuthenticateAsync(string username, string password)
    {
        var user = await userRepository.GetByUsernameAsync(username);
    
        if (user == null || string.IsNullOrEmpty(user.PasswordHash))
        {
            return null; // User not found or invalid password hash
        }

        if (!VerifyPassword(password, user.PasswordHash))
        {
            return null; // Invalid credentials
        }

        return user; // Successful authentication
    }


    // private bool VerifyPassword(string password, string passwordHash)
    // {
    //     // Hash the input password and compare it with the stored password hash
    //     var hashedPassword = HashPassword(password);
    //     return hashedPassword == passwordHash;
    // }

    public async Task<IEnumerable<User>> GetAllUsersAsync()
    {
        return await userRepository.GetAllAsync();
    }

    public async Task<User> GetUserByIdAsync(string id)
    {
        var user = await userRepository.GetByIdAsync(id);
        if (user == null)
        {
            throw new KeyNotFoundException("User not found");
        }

        return user;
    }

    public async Task AddUserAsync(User user)
    {
        // Validation logic for unique username
        var existingUser = await userRepository.GetByUsernameAsync(user.UserName);
        if (existingUser != null)
        {
            throw new InvalidOperationException("Username already exists");
        }

        await userRepository.AddAsync(user);
    }

    public async Task UpdateUserAsync(User user)
    {
        var existingUser = await userRepository.GetByIdAsync(user.Id);
        if (existingUser == null)
        {
            throw new KeyNotFoundException("User not found");
        }

        await userRepository.UpdateAsync(user);
    }

    public async Task DeleteUserAsync(string id)
    {
        var user = await userRepository.GetByIdAsync(id);
        if (user == null)
        {
            throw new KeyNotFoundException("User not found");
        }

        await userRepository.DeleteAsync(id);
    }

    public async Task<User> GetUserByUsernameAsync(string username)
    {
        var user = await userRepository.GetByUsernameAsync(username);
        if (user == null)
        {
            //throw new KeyNotFoundException("User not found");
        }

        return user;
    }

    public async Task<User> RegisterUserAsync(string username, string password, string email)
    {
        var existingUser = await userRepository.GetByUsernameAsync(username);
        if (existingUser != null)
        {
            throw new InvalidOperationException("Username already exists");
        }

        // Hash the password before storing it
        var passwordHash = HashPassword(password);

        var user = new User
        {
            UserName = username,
            PasswordHash = passwordHash,
            Email = email
            //Role = role // Default role is User
        };

        await userRepository.AddAsync(user);

        return user;
    }

    // private string HashPassword(string password)
    // {
    //     using var sha512 = new SHA512CryptoServiceProvider();
    //     byte[] passwordBytes = Encoding.UTF8.GetBytes(password);
    //     byte[] hashBytes = sha512.ComputeHash(passwordBytes);
    //     return Convert.ToBase64String(hashBytes); // Convert the hash to a Base64 string
    // }

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


    public async Task AddCustomCardToListAsync(string userId, string customCardId)
    {
        var user = await userRepository.GetByIdAsync(userId);
        if (user == null)
        {
            throw new KeyNotFoundException("User not found");
        }

        user.CustomCardsIds?.Add(customCardId);

        await userRepository.UpdateAsync(user);
    }
}