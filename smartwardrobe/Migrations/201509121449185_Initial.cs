namespace smartwardrobe.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class Initial : DbMigration
    {
        public override void Up()
        {
            CreateTable(
                "dbo.Actions",
                c => new
                    {
                        id = c.Int(nullable: false, identity: true),
                        status = c.String(),
                        rfid = c.String(),
                        createdAt = c.DateTime(nullable: false),
                    })
                .PrimaryKey(t => t.id);
            
            CreateTable(
                "dbo.Clothes",
                c => new
                    {
                        id = c.Int(nullable: false, identity: true),
                        status = c.String(),
                        rfid = c.String(),
                        image = c.Binary(),
                        name = c.String(),
                        type = c.String(),
                        color = c.String(),
                        createdAt = c.DateTime(nullable: false),
                        lastUpdatedAt = c.DateTime(nullable: false),
                        frequency = c.Int(nullable: false),
                    })
                .PrimaryKey(t => t.id);
            
        }
        
        public override void Down()
        {
            DropTable("dbo.Clothes");
            DropTable("dbo.Actions");
        }
    }
}
