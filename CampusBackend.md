# Campus Course System - Backend API Documentation

## 1. Project Overview

This document provides a comprehensive guide to the backend API of the Campus Course System. It is intended for frontend developers who need to interact with the API.

The backend is a Spring Boot application that provides RESTful APIs for managing courses, students, enrollments, and more. It uses JWT for authentication and role-based access control (RBAC) to secure the endpoints.

## 2. Getting Started

To run the backend server locally, you will need to have Java 17 and Maven installed.

1.  **Clone the repository:**
    ```bash
    git clone <repository-url>
    cd <repository-directory>
    ```

2.  **Build the project:**
    ```bash
    mvn clean install
    ```

3.  **Run the application:**
    ```bash
    mvn spring-boot:run
    ```

The server will start on `http://localhost:8080`.

## 3. Authentication

The API uses JSON Web Tokens (JWT) for authentication. To access protected endpoints, you need to include a valid JWT in the `Authorization` header of your requests.

**Header Format:**

```
Authorization: Bearer <your-jwt-token>
```

### 3.1. Login

To obtain a JWT, you need to send a POST request to the `/api/auth/login` endpoint with the user's credentials.

*   **Endpoint:** `POST /api/auth/login`
*   **Request Body:**

    ```json
    {
      "officialId": "your-official-id",
      "password": "your-password"
    }
    ```

*   **Response:**

    ```json
    {
      "accessToken": "<jwt-token>",
      "tokenType": "Bearer"
    }
    ```

## 4. API Endpoints

This section details all the available API endpoints.

### 4.1. Users (`/api/users`)

#### GET `/api/users/me/profile`

*   **Description:** Get the profile of the currently authenticated user.
*   **Permission:** `profile.view.own`
*   **Response:** `200 OK`

    ```json
    {
      "userId": 1,
      "officialId": "student123",
      "email": "student@test.com",
      "firstName": "Test",
      "lastName": "User",
      "dateOfBirth": "2000-01-01"
    }
    ```

#### PUT `/api/users/me/profile`

*   **Description:** Update the profile of the currently authenticated user.
*   **Permission:** `profile.edit.own`
*   **Request Body:** `ProfileUpdateDto`
*   **Response:** `200 OK`

### 4.2. Courses (`/api/courses`)

#### GET `/api/courses`

*   **Description:** Get a list of all available courses.
*   **Permission:** `course.view.catalog`
*   **Response:** `200 OK`

    ```json
    [
      {
        "id": 1,
        "courseName": "Introduction to Programming",
        "courseDescription": "A beginner's course on programming.",
        "credits": 3,
        "teacherName": "Dr. Smith"
      }
    ]
    ```

#### POST `/api/courses`

*   **Description:** Create a new course.
*   **Permission:** `course.create`
*   **Request Body:** `CourseCreationDto`
*   **Response:** `201 Created`

#### PUT `/api/courses/{courseId}`

*   **Description:** Update a course.
*   **Permission:** `course.update`
*   **Request Body:** `CourseCreationDto`
*   **Response:** `200 OK`

#### DELETE `/api/courses/{courseId}`

*   **Description:** Delete a course.
*   **Permission:** `course.delete`
*   **Response:** `204 No Content`

### 4.3. Teachers (`/api/teachers`)

#### GET `/api/teachers/{courseId}/students`

*   **Description:** Get a list of students enrolled in a course.
*   **Permission:** `grade.view.all`
*   **Response:** `200 OK`

#### PUT `/api/teachers/{courseId}/grades`

*   **Description:** Update grades for students in a course.
*   **Permission:** `grade.update`
*   **Request Body:** `List<GradeUpdateDto>`
*   **Response:** `200 OK`

### 4.4. Admin (`/api/admin`)

This section contains endpoints for managing the system.

#### GET `/api/admin/users`

*   **Description:** Get a list of all users.
*   **Permission:** `student.view` or `teacher.view`
*   **Response:** `200 OK`

## 5. Data Models (DTOs)

This section describes the main Data Transfer Objects (DTOs) used in the API.

### `CourseCreationDto`

```json
{
  "courseName": "string",
  "courseDescription": "string",
  "credits": "integer",
  "teacherId": "integer"
}
```

### `ProfileUpdateDto`

```json
{
  "firstName": "string",
  "lastName": "string",
  "dateOfBirth": "string (yyyy-MM-dd)"
}
```

## 6. Error Handling

The API uses standard HTTP status codes to indicate the success or failure of a request. In case of an error, the response body will contain a JSON object with the following structure:

```json
{
  "timestamp": "long",
  "status": "integer",
  "error": "string",
  "message": "string",
  "path": "string"
}
```

**Common Error Codes:**

*   `400 Bad Request`: The request was malformed or invalid.
*   `401 Unauthorized`: The request requires authentication, but no valid JWT was provided.
*   `403 Forbidden`: The authenticated user does not have the required permission to access the resource.
*   `404 Not Found`: The requested resource could not be found.