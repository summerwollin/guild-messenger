# Guild Messenger App

This is the repository for the Guild Messenger project. This code was written by myself (Summer Wollin) to serve
as a coding sample. All requirements and parameters for this project were assigned.

## Running the App

Required to run:
* Docker - https://docs.docker.com/get-docker/

This application has been dockerized to allow users to run the app locally without having to download a bunch
of other dependencies.

Steps to run:
```
# Build the images
docker-compose build

# Run the application
docker-compose up
```

Then the app should be live at http://localhost:8080/

The Swagger Docs can be found at http://localhost:8080/swagger-ui/index.html

## Running Tests

All unit tests are automatically run on `docker-compose build`. If you would like to run
tests individually or outside of Docker you will need to install the following on your machine:
* JDK 17 or later - https://www.oracle.com/java/technologies/downloads/
* Gradle 4+ - https://gradle.org/install/

Steps to run tests:
```
# Run all tests
./gradlew test

# Run a specific test
./gradlew test --tests *exampleTestMethodName
```

## Notes

#### Language

I chose to use Java/Spring and Postgres for this application not only because I am very familiar with it,
but also because Spring in particular is designed for easily spinning up REST APIs. Spring offers a lot of out-of-the-box
solutions for request validation, exception handling, testing, and database management.

#### Security and Auth

This is a basic app with no authentication or user management. One option for handling authentication would be to utilize
JSON Web Tokens either fully in this service or proxied through this service to an identity/auth management service. User
management could also be through an external service or a new Users table and API could be configured inside this existing service.

One important change to the existing code would be to assign a unique identifier (like a UUID) to each registered user for use as 
their senderId (or recipientId for incoming messages). Their ID could be pulled directly out of the JWT and used as their senderId
to prevent users from sending messages as someone else.

#### Improvements

* Add pagination to allow users more flexibility to set the number of messages per page, and number of pages of messages they want 
to get. If our limit of only returning up to 100 messages was due to response size constraints and database access constraints
then pagination would help alleviate those concerns and allow our users to get more messages.
* Make the 30-day expiration limit on messages a TTL for the data in the db, and delete expired fields from the db table.
One example of the above would be a cron job on a regular schedule that runs automatically.
* Better database management and data sanitization for security purposes and data integrity concerns.
* Improved documentation that is more descriptive, and that utilizes Swagger's "try it out" feature.