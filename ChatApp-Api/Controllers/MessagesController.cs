using ChatApp_Api.ApiModels;
using ChatApp_Api.Models;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data.SqlClient;
using System.Net;
using System.Web.Http;
using System.Web.Http.Description;

namespace ChatApp_Api.Controllers
{
    /// <summary>
    /// Manages the sending and receiving of the Messages
    /// </summary>
    public class MessagesController : ApiController
    {
        private readonly ChatAppModel db = new ChatAppModel();
        /// <summary>
        /// Returns all messages present into the database
        /// </summary>
        /// <returns>List of Messages</returns>
        public IEnumerable<MessageApiModel> Get()
        {
            List<MessageApiModel> model = new List<MessageApiModel>();
            foreach (var item in db.Messages)
            {
                model.Add(new MessageApiModel
                {
                    UserId = item.UserId,
                    DateTime = item.DateTime,
                    Message = item.Message1,
                    MId = item.MId
                });
            }
            return model;
        }
        /// <summary>
        /// Returns data of a single message
        /// </summary>
        /// <param name="id">Unqiue Id of the Message</param>
        /// <returns>Data of the requested message</returns>
        [ResponseType(typeof(MessageApiModel))]
        public IHttpActionResult Get(long id)
        {
            Message message = db.Messages.Find(id);
            if (message == null)
            {
                return NotFound();
            }
            MessageApiModel model = new MessageApiModel()
            {
                DateTime = message.DateTime,
                Message = message.Message1,
                MId = message.MId,
                UserId = message.UserId
            };
            return Ok(model);
        }
        /// <summary>
        /// Adds new Message to the Database
        /// </summary>
        /// <param name="model">Contains data for new Message</param>
        /// <returns>newly added user</returns>
        [ResponseType(typeof(UserApiModel))]
        public IHttpActionResult PostMessage([FromBody]MessageApiModel model)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest();
            }
            Message message = new Message()
            {
                DateTime = model.DateTime,
                Message1 = model.Message,
                MId = model.MId,
                UserId = model.UserId
            };
            message = db.Messages.Add(message);
            try
            {
                db.SaveChanges();
                model = new MessageApiModel()
                {
                    DateTime = message.DateTime,
                    Message = message.Message1,
                    MId = message.MId,
                    UserId = message.UserId
                };
            }
            catch (Exception)
            {
                return StatusCode(HttpStatusCode.ExpectationFailed);
            }

            return Ok(model);
        }
        /// <summary>
        /// Use to set the read status of a message for a perticular user
        /// </summary>
        /// <param name="uId">Id of the User</param>
        /// <param name="mId">Id of the Message</param>
        /// <returns>Status Code</returns>
        [Route("api/Messages/{uId}/{mId}/ReadMessage")]
        [HttpGet]
        public IHttpActionResult ReadMessage([FromUri]int uId, [FromUri]long mId)
        {
            User user = db.Users.Find(uId);
            
            Message message = db.Messages.Find(mId);
            
            UserReadsMessage userReadsMessage = new UserReadsMessage()
            {
                Message = message,
                MessageId = mId,
                ReadFlag = true,
                User = user,
                UserId = uId
            };
            db.UserReadsMessages.Add(userReadsMessage);
            try
            {
                db.SaveChanges();
            }
            catch (Exception)
            {
                return StatusCode(HttpStatusCode.ExpectationFailed);
            }
            return Ok();
        }
        /// <summary>
        /// Gets the undread messages for a perticular user
        /// </summary>
        /// <param name="id">Unique Id of the User </param>
        /// <returns>List of unread messages</returns>
        [ResponseType(typeof(IEnumerable<MessageApiModel>))]
        [Route("api/Messages/{id}/GetUnreadMessages")]
        public IHttpActionResult GetUnreadMessages(int id)
        {
            if (db.Users.Find(id) == null)
            {
                return NotFound();
            }
            List<MessageApiModel> model = new List<MessageApiModel>();
            SqlConnection con = new SqlConnection(ConfigurationManager.ConnectionStrings["ChatAppModel"].ConnectionString);
            SqlCommand cmd = new SqlCommand
            {
                Connection = con,
                CommandType = System.Data.CommandType.StoredProcedure,
                CommandText = "GetUnreadMessages"
            };
            cmd.Parameters.Add(new SqlParameter("@userId", System.Data.SqlDbType.Int)).Value=id;
            try
            {
                con.Open();
                SqlDataReader reader = cmd.ExecuteReader();
                while (reader.Read())
                {
                    model.Add(new MessageApiModel
                    {
                        MId = (long)reader[0],
                        Message = (string)reader[1],
                        UserId = (int)reader[2],
                        DateTime = (DateTime)reader[3]
                    });
                }
                con.Close();
            }
            catch (Exception)
            {
                con.Close();
                return StatusCode(HttpStatusCode.ExpectationFailed);
            }
            return Ok(model);
        }
    }
}
