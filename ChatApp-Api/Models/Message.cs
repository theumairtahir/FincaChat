namespace ChatApp_Api.Models
{
    using System;
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Data.Entity.Spatial;

    /// <summary>
    /// A Message Sent by the User
    /// </summary>
    [Table("Message")]
    public partial class Message
    {
        /// <summary>
        /// Initializes new instance
        /// </summary>
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2214:DoNotCallOverridableMethodsInConstructors")]
        public Message()
        {
            UserReadsMessages = new HashSet<UserReadsMessage>();
        }

        /// <summary>
        /// Unique Id to get Data
        /// </summary>
        [Key]
        public long MId { get; set; }

        /// <summary>
        /// Message sent by the User
        /// </summary>
        [Column("Message", TypeName = "text")]
        [Required]
        public string Message1 { get; set; }

        /// <summary>
        /// Id of the User who has sent the messsage
        /// </summary>
        public int UserId { get; set; }

        /// <summary>
        /// Date and Time on which the Message has been sent
        /// </summary>
        public DateTime DateTime { get; set; }

        /// <summary>
        /// User who sent the Message
        /// </summary>
        public virtual User User { get; set; }

        /// <summary>
        /// List of Users who have read this Message
        /// </summary>
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2227:CollectionPropertiesShouldBeReadOnly")]
        public virtual ICollection<UserReadsMessage> UserReadsMessages { get; set; }
    }
}
