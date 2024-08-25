using backend.data;
using backend.interfaces;
using backend.models;
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

builder.Services.AddScoped<IUserService, UserService>();
builder.Services.AddScoped<ICustomCardService, CustomCardService>();
builder.Services.AddScoped<IDefaultCardService, DefaultCardService>();
builder.Services.AddSingleton<MongoDbContext>();
builder.Services.AddScoped<IUserRepository, UserRepository>();
builder.Services.AddScoped<ICustomCardRepository, CustomCardRepository>();
builder.Services.AddScoped<IDefaultCardRepository, DefaultCardRepository>();

var app = builder.Build();

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

// Enable middleware to serve generated Swagger as a JSON endpoint
app.UseSwagger();

// Enable middleware to serve Swagger UI, specifying the Swagger JSON endpoint
app.UseSwaggerUI(c =>
{
    c.SwaggerEndpoint("/swagger/v1/swagger.json", "Your API V1");
    c.RoutePrefix = string.Empty; // To serve Swagger UI at the app's root (http://localhost:<port>/)
});

app.Run();
