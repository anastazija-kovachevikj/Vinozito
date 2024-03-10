using System.ComponentModel.DataAnnotations;

namespace Vinozito.Models
{
    public class User
    {
        [Key]
        public int Id { get; set; }

        public required String username { get; set; }
        public required String password { get; set; }
    }
}
