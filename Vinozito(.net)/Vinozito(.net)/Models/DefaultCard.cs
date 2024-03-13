using System.ComponentModel.DataAnnotations;

namespace Vinozito_.net_.Models
{
    public class DefaultCard
    {
        [Key]
        public int Id { get; set; }
        public required String Name { get; set; }
        public required String Voice { get; set; }
        public required String Photo { get; set; }
    }
}
