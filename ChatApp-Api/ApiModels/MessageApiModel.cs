using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace ChatApp_Api.ApiModels
{
    /// <summary>
    /// Api Model to store Message Data
    /// </summary>
    public class MessageApiModel
    {
        /// <summary>
        /// Unique Id of the Message
        /// </summary>
        public long MId { get; set; }
        /// <summary>
        /// Message body sent by the User
        /// </summary>
        [Required]
        public string Message { get; set; }
        /// <summary>
        /// Id of the User who have sent the Message
        /// </summary>
        [Required]
        public int UserId { get; set; }
        /// <summary>
        /// Date and Time on which the message has been sent
        /// </summary>
        [Required]
        public DateTime DateTime { get; set; }
    }
}