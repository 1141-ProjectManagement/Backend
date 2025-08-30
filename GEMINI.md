# Gemini Code Understanding

## Project Overview

This project is a course enrollment system developed using Spring Boot. It provides REST APIs for managing courses, students, and enrollments. The system uses JWT for authentication and authorization, ensuring that only authenticated users can access protected endpoints. The database is accessed through Spring Data JPA, and the application is built using Maven.

### Key Technologies:

*   **Backend:** Java 17, Spring Boot
*   **Authentication:** Spring Security with JWT
*   **Database:** Spring Data JPA with MariaDB
*   **Build Tool:** Maven
*   **API Documentation:** SpringDoc (Swagger UI)

### Architecture:

The application follows a standard layered architecture:

*   **Controllers:** Handle incoming HTTP requests, delegate business logic to services, and return responses.
*   **Services:** Contain the core business logic of the application.
*   **Repositories:** Interface with the database using Spring Data JPA.
*   **Models:** Represent the data structures of the application.
*   **Security:** Manages user authentication and authorization.

## Building and Running

To build and run the project, you can use the following Maven commands:

*   **Build:** `mvn clean install`
*   **Run:** `mvn spring-boot:run`

The application will start on the default port (8080). You can access the API documentation at `http://localhost:8080/swagger-ui.html`.

## Development Conventions

*   **Authentication:** The system uses JWT for authentication. The `AuthController` handles the login process, and a JWT is returned upon successful authentication. This token must be included in the `Authorization` header of subsequent requests.
*   **Authorization:** Role-based access control is implemented using Spring Security. Endpoints are secured with `@PreAuthorize` annotations to restrict access to specific roles (e.g., `STUDENT`, `TEACHER`, `ADMIN`).
*   **API Design:** The project provides RESTful APIs for managing resources. The API endpoints are documented using SpringDoc, which can be accessed through the Swagger UI.
*   **Error Handling:** A centralized exception handler (`RestExceptionHandler`) is used to manage and format error responses.
