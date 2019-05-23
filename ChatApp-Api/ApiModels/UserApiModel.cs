using ChatApp_Api.Common;
using System.ComponentModel.DataAnnotations;

namespace ChatApp_Api.ApiModels
{
    /// <summary>
    /// Contains the data of the User
    /// </summary>
    public class UserApiModel
    {
        /// <summary>
        /// Unique Id
        /// </summary>
        public int Id { get; set; }
        /// <summary>
        /// User Name
        /// </summary>
        [Required]
        public string Name { get; set; }
        /// <summary>
        /// User Phone NUmber
        /// </summary>
        [Required]
        public string Phone { get; set; }
        /// <summary>
        /// Username to log into the app
        /// </summary>
        [Required]
        [UniqueUsername]
        public string UserName { get; set; }
        /// <summary>
        /// Password to log-on
        /// </summary>
        [Required]
        public string Password { get; set; }
    }
}