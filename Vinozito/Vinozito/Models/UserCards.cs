using System.ComponentModel.DataAnnotations;

namespace Vinozito.Models
{
    public class UserCards
    {
        [Key]
        public int Id { get; set; }
        public User user { get; set; }
        public Category category { get; set; }
        public Card card { get; set; }


    }
}
