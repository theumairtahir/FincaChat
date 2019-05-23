using ChatApp_Api.Models;
using System.ComponentModel.DataAnnotations;

namespace ChatApp_Api.Common
{
    /// <summary>
    /// Uses to get a unique value for the Username
    /// </summary>
    public class UniqueUsername : ValidationAttribute
    {
        ChatAppModel model = new ChatAppModel();
        public override bool IsValid(object value)
        {
            string username = (string)value;
            foreach (var item in model.Users)
            {
                if (item.UserName == username)
                {
                    return false;
                }
            }
            return true;
        }
    }
}