using System.ComponentModel.DataAnnotations;
    using Vinozito_.net_.Models;

namespace Vinozito_.net_.Models
{
    public class Avatar
    {
        [Key]
        public int Id { get; set; }
        public required String Photo { get; set; }
        public required String Name { get; set; }
    }
}
