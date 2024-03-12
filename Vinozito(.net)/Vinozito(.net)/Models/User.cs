using System.ComponentModel.DataAnnotations;
using Vinozito_.net_.Models;

namespace Vinozito_.net_.Models
{
    public class User
    {
        [Key]
        public int Id { get; set; }

        public required String Username { get; set; }
        public required String Password { get; set; }
        public required String Email { get; set; }
        public virtual required Avatar Avatar { get; set; }
        public virtual required Settings Settings { get; set; }
        public virtual required List<Card> Cards { get; set; }

    }
}
