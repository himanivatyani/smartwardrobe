using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace smartwardrobe.Models
{
    public class Cloth
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int id { get; set; }

        public String status { get; set; }

        public String rfid { get; set; }

        public Byte[] image { get; set; }

        public String name { get; set; }

        public String type { get; set; }

        public String color { get; set; }
        
        public DateTime createdAt { get; set; }

        public DateTime lastUpdatedAt { get; set; }

        public int frequency { get; set; }
    }
}
