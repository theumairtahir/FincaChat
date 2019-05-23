namespace ChatApp_Api.Models
{
    using System;
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Data.Entity.Spatial;

    /// <summary>
    /// User Registered to the App
    /// </summary>
    [Table("User")]
    public partial class User
    {
        /// <summary>
        /// Initialzes new instance
        /// </summary>
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2214:DoNotCallOverridableMethodsInConstructors")]
        public User()
        {
            Messages = new HashSet<Message>();
            UserReadsMessages = new HashSet<UserReadsMessage>();
        }

        /// <summary>
        /// Unique Id to get the data of Unique User
        /// </summary>
        public int Id { get; set; }

        /// <summary>
        /// Name of the User
        /// </summary>
        [Required]
        public string Name { get; set; }

        /// <summary>
        /// Phone Number of the User
        /// </summary>
        [Required]
        [StringLength(13)]
        public string Phone { get; set; }

        /// <summary>
        /// Unique Username of the User
        /// </summary>
        [Required]
        [StringLength(50)]
        public string UserName { get; set; }

        /// <summary>
        /// Password to log into the app
        /// </summary>
        [Required]
        public string Password { get; set; }

        /// <summary>
        /// List of Messages sent by this User
        /// </summary>
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2227:CollectionPropertiesShouldBeReadOnly")]
        public virtual ICollection<Message> Messages { get; set; }

        /// <summary>
        /// List of Messages read by this User
        /// </summary>
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2227:CollectionPropertiesShouldBeReadOnly")]
        public virtual ICollection<UserReadsMessage> UserReadsMessages { get; set; }
    }
}
