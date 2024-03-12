using System.ComponentModel.DataAnnotations;

namespace Vinozito.Models
{
    public class Card
    {
        [Key]
        public int Id { get; set; }
        public Category category { get; set; }
        public String name { get; set; }
        public String voice { get; set; }
    }
}
