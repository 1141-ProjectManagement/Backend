# Directory Overview

This directory contains the database schema for a course enrollment system. The schema is described in a series of Markdown files, each representing a table in the database. There is also a PNG image, `DBStructure.png`, which likely visualizes the relationships between these tables.

## Key Files

*   **`Users_202508302117.md`**: This file defines the `Users` table, which is central to the system. It contains user authentication information like `official_id`, `password_hash`, and `email`, as well as a `role_id` to link to the user's role.
*   **`Roles_202508302117.md`**: This file defines the `Roles` table, which outlines the different user roles in the system (e.g., `Student`, `Teacher`, `Super Administrator`).
*   **`Permissions_202508302117.md`**: This file defines the `Permissions` table, which lists all possible actions a user can take within the system.
*   **`Role_Permissions_202508302117.md`**: This file is a junction table that maps roles to their respective permissions, defining the access control for each role.
*   **`Student_Profiles_202508302117.md`**, **`Teacher_Profiles_202508302117.md`**, **`Administrator_Profiles_202508302117.md`**: These files define profile tables for students, teachers, and administrators, containing personal information for each user type.
*   **`Courses_202508302117.md`**: This file defines the `Courses` table, which contains information about the courses offered.
*   **`Enrollment_202508302117.md`**: This file defines the `Enrollment` table, which tracks which students are enrolled in which courses.
*   **`DBStructure.png`**: This is an image file, likely showing an entity-relationship diagram (ERD) of the database schema.

## Usage

This directory is intended for developers and database administrators who need to understand the structure of the course enrollment system's database. The Markdown files provide a clear, human-readable definition of each table, while the `DBStructure.png` image offers a visual representation of the overall schema. This information is crucial for tasks such as:

*   Developing new features that interact with the database.
*   Writing database queries and reports.
*   Performing database maintenance and migrations.
*   Understanding the system's data model and access control.
