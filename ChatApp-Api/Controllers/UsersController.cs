using ChatApp_Api.ApiModels;
using ChatApp_Api.Models;
using System.Collections.Generic;
using System.Data.Entity.Infrastructure;
using System.Linq;
using System.Web.Http;
using System.Web.Http.Description;

namespace ChatApp_Api.Controllers
{
    /// <summary>
    /// Manages the Tasks performed by the User
    /// </summary>
    public class UsersController : ApiController
    {
        private ChatAppModel db = new ChatAppModel();
        /// <summary>
        /// Gets the Users registered with the App
        /// </summary>
        /// <returns>List of Users</returns>
        public IEnumerable<UserApiModel> Get()
        {
            List<UserApiModel> users = new List<UserApiModel>();
            foreach (var item in db.Users)
            {
                users.Add(new UserApiModel
                {
                    Id = item.Id,
                    Name = item.Name,
                    Password = item.Password,
                    Phone = item.Phone,
                    UserName = item.UserName
                });
            }
            return users;
        }
        /// <summary>
        /// Gets the Data of a unique user
        /// </summary>
        /// <param name="id">Unique Id</param>
        /// <returns>Data of User</returns>
        [ResponseType(typeof(UserApiModel))]
        public IHttpActionResult Get(int id)
        {
            User user = db.Users.Find(id);
            if (user == null)
            {
                return NotFound();
            }
            UserApiModel model = new UserApiModel()
            {
                Id = user.Id,
                Name = user.Name,
                Password = user.Password,
                Phone = user.Phone,
                UserName = user.UserName
            };
            return Ok(model);
        }
        /// <summary>
        /// Adds a new user
        /// </summary>
        /// <param name="model">Data for the new user to be added</param>
        /// <returns>User which is newly added</returns>
        [ResponseType(typeof(UserApiModel))]
        public IHttpActionResult PostUser([FromBody]UserApiModel model)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest();
            }
            User user = new User()
            {
                Name = model.Name,
                Password = model.Password,
                Phone = model.Phone,
                UserName = model.UserName
            };
            user = db.Users.Add(user);
            db.SaveChanges();
            model = new UserApiModel()
            {
                Id = user.Id,
                UserName = user.UserName,
                Phone = user.Phone,
                Name = user.Name,
                Password = user.Password
            };
            return Ok(model);
        }
        /// <summary>
        /// Updates the User Data
        /// </summary>
        /// <param name="id">Unique Id</param>
        /// <param name="model">Updating values</param>
        /// <returns>Updated User Data</returns>
        public IHttpActionResult PutUser(int id, [FromBody]UserApiModel model)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }
            if (id != model.Id)
            {
                return BadRequest();
            }
            if (!UserExists(id))
            {
                return NotFound();
            }
            User user = db.Users.Find(id);
            user.Name = model.Name;
            user.Password = model.Password;
            user.Phone = model.Phone;
            user.UserName = model.UserName;
            db.Entry(user).State = System.Data.Entity.EntityState.Modified;
            try
            {
                db.SaveChanges();
            }
            catch (DbUpdateConcurrencyException)
            {
                return StatusCode(System.Net.HttpStatusCode.ExpectationFailed);
            }
            return StatusCode(System.Net.HttpStatusCode.NoContent);
        }
        /// <summary>
        /// Use to login to the app
        /// </summary>
        /// <param name="username">User's Unique Username</param>
        /// <param name="password">Password to complete login</param>
        /// <returns>User data</returns>
        [Route("api/Login/{username}/{password}")]
        [ResponseType(typeof(UserApiModel))]
        [HttpGet]
        public IHttpActionResult LogIn(string username, string password)
        {
            User user = new Models.User();
            foreach (var item in db.Users)
            {
                if (item.Password == password && item.UserName == username)
                {
                    user = item;
                    UserApiModel model = new UserApiModel()
                    {
                        Id = user.Id,
                        Name = user.Name,
                        Password = user.Password,
                        Phone = user.Phone,
                        UserName = user.UserName
                    };
                    return Ok(model);
                }
            }
            return BadRequest("Login Failed");
        }
        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }
        private bool UserExists(int id)
        {
            return db.Users.Count(e => e.Id == id) > 0;
        }
    }
}
