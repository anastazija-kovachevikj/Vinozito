using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;
using Vinozito_.net_.Models;

namespace Vinozito_.net_.Data
{
    public class ApplicationDbContext : IdentityDbContext
    {
        public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options)
            : base(options)
        {
        }

        public DbSet<Avatar> Avatar { get; set; }
        public DbSet<Card> Card { get; set; }
        public DbSet<Category> Category { get; set; }
        public DbSet<CustomCard> CustomCard { get; set; }
        public DbSet<DefaultCard> DefaultCard { get; set; }
        public DbSet<Settings> Settings { get; set; }
        public DbSet<User> User { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);

            // AVATAR
            modelBuilder.Entity<Avatar>()
               .HasKey(a => a.Id);

            modelBuilder.Entity<Avatar>()
                .Property(a => a.Photo)
                .IsRequired();

            modelBuilder.Entity<Avatar>()
                .Property(a => a.Name)
                .IsRequired();

            // CARD
            modelBuilder.Entity<Card>()
                .HasKey(c => c.Id);

            modelBuilder.Entity<Card>()
                .Property(c => c.Custom)
                .IsRequired();

            modelBuilder.Entity<Card>()
                .HasOne(c => c.DefaultCard);

            // USER
            modelBuilder.Entity<User>()
               .HasKey(u => u.Id);

            modelBuilder.Entity<User>()
                .Property(u => u.Username)
                .IsRequired();

            modelBuilder.Entity<User>()
                .Property(u => u.Password)
                .IsRequired();

            modelBuilder.Entity<User>()
                .Property(u => u.Email)
                .IsRequired();

            modelBuilder.Entity<User>()
                .HasOne(u => u.Avatar);
            // .WithOne()
            //.IsRequired();

            modelBuilder.Entity<User>()
                .HasOne(u => u.Settings);

            modelBuilder.Entity<User>()
                .HasMany(u => u.Cards);

            // CATEGORY
            modelBuilder.Entity<Category>()
               .HasKey(c => c.Id);

            modelBuilder.Entity<Category>()
                .Property(c => c.Name)
                .IsRequired();

            modelBuilder.Entity<Category>()
                .HasMany(c => c.CardCollection);

            // CUSTOM CARD
            modelBuilder.Entity<CustomCard>()
               .HasKey(c => c.Id);

            modelBuilder.Entity<CustomCard>()
                .Property(c => c.Voice)
                .IsRequired();

            modelBuilder.Entity<CustomCard>()
                .HasOne(c => c.DefaultCard); // maybe add .WithMany

            // DEAULT CARD
            modelBuilder.Entity<DefaultCard>()
               .HasKey(d => d.Id);

            modelBuilder.Entity<DefaultCard>()
                .HasOne(dc => dc.Category)
                .WithMany()
                .IsRequired();

            modelBuilder.Entity<DefaultCard>()
                .Property(c => c.Name)
                .IsRequired();

            modelBuilder.Entity<DefaultCard>()
                .Property(c => c.Voice)
                .IsRequired();

            modelBuilder.Entity<DefaultCard>()
                .Property(c => c.Photo)
                .IsRequired();

            // SETTINGS
            modelBuilder.Entity<Settings>()
               .HasKey(s => s.Id);

            modelBuilder.Entity<Settings>()
                .Property(c => c.Animations);

            modelBuilder.Entity<Settings>()
               .Property(c => c.Music);

            modelBuilder.Entity<Settings>()
               .Property(c => c.Flashes);

            modelBuilder.Entity<Settings>()
               .Property(c => c.Sounds);

            modelBuilder.Entity<Settings>()
               .Property(c => c.StrongColors);

        }
    }
}
