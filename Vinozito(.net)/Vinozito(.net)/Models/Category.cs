using System.ComponentModel.DataAnnotations;
using Vinozito_.net_.Models;

namespace Vinozito_.net_.Models
{
    public class Category
    {
        [Key]
        public int Id { get; set; }
        public required string Name { get; set; }
        public virtual required List<Card> CardCollection { get; set; }

    }
}
