using System.ComponentModel.DataAnnotations;


namespace Vinozito_.net_.Models
{
    public class Card
    {
        [Key]
        public int Id { get; set; }
        public virtual required Category Category { get; set; }
        public virtual required DefaultCard DefaultCard { get; set; }
        public bool Custom { get; set; } // is it a custom card or not

    }
}
