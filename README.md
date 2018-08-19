# Restaurant Services in Spring boot(v1.5.6.RELEASE)

This project was generated with [Eclipse ](https://www.eclipse.org/) version Oxygen.

## Requirements

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `de.codecentric.springbootsample.Application` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```
### Introduction
This project is a maven multimodule project which basically have 3 modules in it.
-  # restaurant-common #
-->this project basically have all the common dependency which has been used accross other modules
- # restaurant-auth #
-->This is a kind of auth server which will be running independently and will be managing all the authentication and authorization stuff.
- # restaurant-studio #
->This is again a microservice which basically handles all the crud operations.

**Note**:-All the end points of studio are secured and do strict check for the token given by auth server.For Authentication And Authorization here i am using spring security which i have customized according to need.

As a database i have used Mongodb here.


