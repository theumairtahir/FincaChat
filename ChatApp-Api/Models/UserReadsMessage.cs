namespace ChatApp_Api.Models
{
    using System;
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Data.Entity.Spatial;

    /// <summary>
    /// Model to get Data whether User read a message
    /// </summary>
    [Table("UserReadsMessage")]
    public partial class UserReadsMessage
    {
        /// <summary>
        /// Id of User who read the Message
        /// </summary>
        [Key]
        [Column(Order = 0)]
        [DatabaseGenerated(DatabaseGeneratedOption.None)]
        public int UserId { get; set; }

        /// <summary>
        /// Id of Message which is read by the User
        /// </summary>
        [Key]
        [Column(Order = 1)]
        [DatabaseGenerated(DatabaseGeneratedOption.None)]
        public long MessageId { get; set; }

        /// <summary>
        /// Flag which will be true if the message is read by a User
        /// </summary>
        public bool ReadFlag { get; set; }

        /// <summary>
        /// Message which is read
        /// </summary>
        public virtual Message Message { get; set; }

        /// <summary>
        /// User who read the Message
        /// </summary>
        public virtual User User { get; set; }
    }
}
