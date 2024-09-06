#!/bin/bash

# Wait for MongoDB to start
sleep 30

# Restore the database
mongorestore --username root --password example --authenticationDatabase admin --nsInclude=VinozitoDB.* /dump
