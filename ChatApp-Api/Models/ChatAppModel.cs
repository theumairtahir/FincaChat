namespace ChatApp_Api.Models
{
    using System;
    using System.Data.Entity;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Linq;

    /// <summary>
    /// Data handler for Chat-App Database
    /// </summary>
    public partial class ChatAppModel : DbContext
    {
        /// <summary>
        /// initializes new instance
        /// </summary>
        public ChatAppModel()
            : base("name=ChatAppModel")
        {
        }

        /// <summary>
        /// Messages into the Database
        /// </summary>
        public virtual DbSet<Message> Messages { get; set; }
        /// <summary>
        /// User registered to the App
        /// </summary>
        public virtual DbSet<User> Users { get; set; }
        /// <summary>
        /// Messages which have been read by the User
        /// </summary>
        public virtual DbSet<UserReadsMessage> UserReadsMessages { get; set; }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="modelBuilder"></param>
        protected override void OnModelCreating(DbModelBuilder modelBuilder)
        {
            modelBuilder.Entity<Message>()
                .Property(e => e.Message1)
                .IsUnicode(false);

            modelBuilder.Entity<Message>()
                .HasMany(e => e.UserReadsMessages)
                .WithRequired(e => e.Message)
                .HasForeignKey(e => e.MessageId)
                .WillCascadeOnDelete(false);

            modelBuilder.Entity<User>()
                .Property(e => e.Name)
                .IsUnicode(false);

            modelBuilder.Entity<User>()
                .Property(e => e.Phone)
                .IsFixedLength();

            modelBuilder.Entity<User>()
                .Property(e => e.UserName)
                .IsUnicode(false);

            modelBuilder.Entity<User>()
                .Property(e => e.Password)
                .IsFixedLength();

            modelBuilder.Entity<User>()
                .HasMany(e => e.Messages)
                .WithRequired(e => e.User)
                .WillCascadeOnDelete(false);

            modelBuilder.Entity<User>()
                .HasMany(e => e.UserReadsMessages)
                .WithRequired(e => e.User)
                .WillCascadeOnDelete(false);
        }
    }
}
