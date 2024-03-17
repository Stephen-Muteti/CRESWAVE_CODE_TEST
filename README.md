CRESWAVE_CODE_TEST
This is a blog post application

# Blog Application

## Introduction
This is a blog application developed using Spring Boot. It allows users to create, 
read, update, and delete blog posts and comments. The application also provides user 
authentication and authorization features.

## Project Overview
The main objectives of the application are:
- Allow users to register and log in to the platform.
- Enable users to create, read, update, and delete blog posts.
- Allow users to create, read, update, and delete comments on blog posts.
- Implement authentication and authorization to ensure secure access to the application's features.

## Libraries Used
- Spring Boot: Framework for building robust Java applications with minimal configuration.
- Spring Data JPA (Hibernate): Provides easy and efficient data access to relational databases.
- Spring Security: Provides authentication, authorization, and other security features for Spring applications.
- Lombok: Library to reduce boilerplate code in Java classes.
- MySQL Connector Java: JDBC driver for MySQL database connectivity.
- Jackson: Library for JSON serialization and deserialization in Java.

## Performance and Scalability Considerations
- Spring Boot: Offers built-in features for managing application performance and scalability, 
such as auto-configuration and embedded servers.
- Spring Data JPA: Generates efficient database queries and provides caching mechanisms to improve performance.
- Spring Security: Implements security best practices to protect the application from common vulnerabilities, enhancing overall performance.
- Hibernate: Optimizes database interactions through features like lazy loading and second-level caching, improving scalability.

## Testing Instructions
To test the application locally, follow these steps:

1. Clone the Repository
2. Navigate to the Project Directory
cd blog-application
3. Build the Project
./gradlew build
4. Run the Application
java -jar build/libs/blog-application.jar
5. Access the Application
- Open a web browser and navigate to `http://localhost:8080`
- Use the provided endpoints to register, log in, create blog posts, add comments, etc.

## Contributors
- Stephen Muteti
