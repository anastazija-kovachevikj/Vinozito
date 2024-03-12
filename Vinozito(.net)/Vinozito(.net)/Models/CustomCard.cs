using System.ComponentModel.DataAnnotations;


namespace Vinozito_.net_.Models
{
    public class CustomCard
    {
        [Key]
        public int Id { get; set; }
        public required String Voice { get; set; }
        public virtual required DefaultCard DefaultCard { get; set; }
    }
}
