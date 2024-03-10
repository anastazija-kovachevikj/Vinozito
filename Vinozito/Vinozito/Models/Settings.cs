using System.ComponentModel.DataAnnotations;

namespace Vinozito.Models
{
    public class Settings
    {
        [Key]
        public int Id { get; set; }
        public bool Animations { get; set; }
        public bool Music { get; set; }
        public bool Flashes { get; set; }
        public bool Sounds { get; set; }
        public bool StrongColors { get; set; }
    }

}
