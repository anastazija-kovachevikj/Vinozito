using backend.data;
using backend.interfaces;
using backend.repositories;
using backend.services;
using backend.services.impl;
using Microsoft.AspNetCore.Builder;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;

var builder = WebApplication.CreateBuilder(args);

// Register services
builder.Services.AddControllers();

// Register Swagger services
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

// Dependency Injection (DI) registration
builder.Services.AddScoped<IDefaultCardService, DefaultCardService>();
builder.Services.AddSingleton<MongoDbContext>();
builder.Services.AddScoped<IDefaultCardRepository, DefaultCardRepository>();
builder.Services.AddScoped<IUserRepository, UserRepository>();
builder.Services.AddScoped<IUserService, UserService>();
builder.Services.AddScoped<ICustomCardService, CustomCardService>();
builder.Services.AddScoped<ICustomCardRepository, CustomCardRepository>();
builder.Services.AddScoped<ICardService, CardService>();

builder.Services.AddSingleton<MongoDbContext>();

var app = builder.Build();

using (var scope = app.Services.CreateScope())
{
    var dbContext = scope.ServiceProvider.GetRequiredService<MongoDbContext>();
    dbContext.InsertSampleDataAsync(scope.ServiceProvider); // Make sure this method is async
}


// Configure the HTTP request pipeline
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Home/Error");
    app.UseHsts();
}

app.UseHttpsRedirection();
app.UseStaticFiles();

app.UseRouting();

app.UseAuthorization();

app.MapControllers();

// Enable Swagger
app.UseSwagger();
app.UseSwaggerUI(c =>
{
    c.SwaggerEndpoint("/swagger/v1/swagger.json", "Your API V1");
    c.RoutePrefix = string.Empty;
});

app.Run();