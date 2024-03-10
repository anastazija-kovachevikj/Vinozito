using System.ComponentModel.DataAnnotations;

namespace Vinozito.Models
{
    public class Avatar
    {
        [Key]
        public int Id { get; set; }

        public String photo { get; set; }
        public String name { get; set; }
    }
}
