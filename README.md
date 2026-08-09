# Ctrl+Learn LMS

Ctrl+Learn is a monolithic Learning Management System (LMS) designed for a College of Technology. The platform supports comprehensive user, course, enrollment, content, assessment, file, progress, and notification management. Students can log in via a traditional login page or by using Google OAuth2. Upon signing in with Google, a JWT access token is issued, which the student can then use to access secured endpoints. The system also sends email notifications for important events using templated emails powered by Thymeleaf.

---

## Table of Contents


- [Features](#features)
- [Architecture & Technology Stack](#architecture--technology-stack)
- [Project Structure](#project-structure)
- [Setup and Installation](#setup-and-installation)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Seeding the Database](#seeding-the-database)
- [Version Control and GitHub Usage](#version-control-and-github-usage)
- [Contributing](#contributing)
- [Logout Implementation Summary](#logout-implementation-summary)
- [Application Configuration](#application-configuration)
- [License](#license)
- [Contact](#contact)

---

## Overview

Ctrl+Learn LMS is built with Spring Boot as a single deployable unit. It uses the H2 in-memory database for local development, making setup simple and fast. The application supports both form-based login and Google OAuth2 authentication, issuing JWT access tokens for secured API access. Additionally, the platform features robust email notifications, file management, progress tracking, and detailed profiles for instructors and students.

---

## Features

- **User Management & Authentication:**
  - Registration and login using a custom login page.
  - "Continue with Google" option for OAuth2-based authentication.
  - JWT-based authentication for securing API endpoints.
  - Role-based access control (Admin, Instructor, Student).

- **Course Management:**
  - Create, update, delete, and list courses.

- **Enrollment System:**
  - Students can enroll in or drop courses.
  - Track enrollment progress and completion percentage.

- **Content Management:**
  - Instructors can upload course content (e.g., PDFs, PPTs, videos).
  - Students can view and download course materials.

- **Assessment Module:**
  - Create quizzes and assignments.
  - Auto-grade quizzes, store scores, and provide detailed feedback.
  - Manage grades for quizzes and assignments.

- **Progress Tracking:**
  - Record and update module completion progress.
  - Calculate overall course progress based on completed modules.

- **File Management:**
  - Secure file upload and download for assignments, submissions, and course content.
  - Local file storage in designated directories.

- **Notifications:**
  - Email notifications for course updates, assignment due dates, enrollment confirmations, grade releases, and more.
  - Uses JavaMailSender with Thymeleaf templates.

- **Profiles:**
  - Separate profiles for instructors and students with detailed personal and academic information.

- **Interactive API Documentation:**
  - Swagger UI for exploring and testing RESTful endpoints.

---

## Technology Stack

- **Backend Framework:** Spring Boot (Monolithic Architecture)
- **Database:** H2 (in-memory database for development)
- **Authentication & Security:** Spring Security, JWT, OAuth2 (Google integration)
- **Persistence:** Spring Data JPA / Hibernate
- **Templating & Emails:** Thymeleaf, JavaMailSender
- **API Documentation:** Swagger (springdoc-openapi)
- **File Handling:** Local file storage with secure upload/download methods
- **Others:** Lombok, Spring HATEOAS

---

## Project Structure

The project is organized by domain. Key packages include:

- **`com.example.project`**  
  - Main application bootstrap and configuration.

- **User & Authentication:**  
  - `com.example.project.Users`: User entities, DTOs, repositories, services, and mappers.  
  - `com.example.project.Security`: JWT utilities, authentication filters, OAuth2 handlers, and controllers.

- **Course & Enrollment:**  
  - `com.example.project.Courses`: Course management (CRUD operations).  
  - `com.example.project.Enrollments`: Enrollment processing and progress tracking.

- **Assessment Module:**  
  - `com.example.project.Assessments`: Includes quizzes, assignments, submissions, and grading.

- **Content & File Management:**  
  - `com.example.project.CourseContents`: Manages course contents.  
  - `com.example.project.Files`: File metadata, storage, and download endpoints.

- **Notifications:**  
  - `com.example.project.Notifications`: Email service, notification entities (and enums for types, channels, and statuses), and controllers.

- **Profiles:**  
  - `com.example.project.Profiles`: Instructor and student profiles.

- **Others:**  
  - Global exception handling, auditing, and scheduling (e.g., token cleanup, directory creation).

---

## Prerequisites

- **Java:** JDK 11 or higher.
- **Maven:** Build tool.
- **IDE:** IntelliJ IDEA, Eclipse, or your preferred Java IDE.
- **Git:** Version control system.

---

## Installation & Setup

### Configuration

1. **Database Configuration:**  
   The application uses H2 as its in-memory database for local development. No additional setup is required.  
   You can access the H2 console at: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)  

2. **JWT & Security:**  
   The JWT secret is configured in the `application.properties` file. Update this value for production deployments.

3. **Email Settings:**  
   Email configuration (SMTP host, port, username, and password) is defined in `application.properties`. Adjust these settings as needed for your email provider.

4. **OAuth2 Settings:**  
   Configure your Google OAuth2 client credentials in the `application.properties` file under the OAuth2 section.

5. **Swagger:**  
   Swagger UI is enabled and can be accessed at [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).

---

## Running the Application

1. **Clone the Repository:**

   ```bash
   git clone <repository-url>
   cd <repository-directory>
   ```

2. **Build the Application:**

   ```bash
   mvn clean install
   ```

3. **Run the Application:**

   ```bash
   mvn spring-boot:run
   ```
   Alternatively, run the `ProjectApplication` main class from your IDE.

4. **Access the Application:**

   - **Login Page:**  
     Navigate to [http://localhost:8080/login](http://localhost:8080/login) to view the custom login page. You can log in using the traditional form or click on "Continue with Google."  
     When you sign in with Google, you will receive an access token that is used to authenticate subsequent API requests.
   
   - **Swagger UI:**  
     View and test API endpoints at [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).

   - **H2 Console:**  
     Access the H2 database console at [http://localhost:8080/h2-console](http://localhost:8080/h2-console) for database inspection.

---

## API Documentation

Interactive API documentation is provided via Swagger. Once the application is running, open your browser and navigate to:

**[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

This interface allows you to explore and test all available REST endpoints.

---

## Contributing Guidelines

Contributions are welcome! To contribute:

1. **Fork the Repository:** Create your own fork.
2. **Create a Feature Branch:** Use descriptive names (e.g., `feature/course-management` or `fix/login-auth`).
3. **Commit Frequently:** Make small, clear commits with detailed messages.
4. **Submit a Pull Request:** Describe your changes for review.

Please adhere to the project's code style and guidelines. For additional details, see our [Code of Conduct](#).

---

## Logout Implementation Summary
The logout mechanism enhances security by combining JWT with refresh token management:

1. **Logout Endpoint**:
   - `POST /auth/logout` allows authenticated users to log out.
   - On logout, the user's refresh token is removed from the database, preventing further token refresh.

2. **Access Token Expiry**:
   - Tokens expire in 10 minutes, ensuring short-lived authentication post-logout.

3. **Stateless Flow**:
   - JWT is stateless, so tokens expire naturally rather than being revoked instantly.
   - The frontend clears stored tokens and redirects users to the login page.

**Outcome**:
- Enhanced security through token invalidation.
- Clean session management with quick token expiry.
- Seamless frontend integration for a comprehensive logout process.

---

## Application Configuration
## File Upload Settings
manage file uploads. define a maximum file size of 100MB and specify separate directories for assignments, submissions, and course content

# ---------------------------------
# FILE UPLOAD CONFIG
# ---------------------------------

- **Maximum File Size**: 100MB (`file.upload.max-size=104857600`)
- **Upload Directories**:
  - Assignments: `uploads/assignments/`
  - Submissions: `uploads/submissions/`
  - Course Content: `uploads/courseContents/`



---


## Testing & Deployment

- **Testing:**  
  Run tests using Maven:
  ```bash
  mvn test
  ```
- **Deployment:**  
  The application is packaged as a single deployable unit. For production, update configuration settings (e.g., database, security, email) accordingly.
- **Environment Variables:**  
  Sensitive data (JWT secret, email credentials) are set in `application.properties`. For production, consider using environment variables or a secrets manager.

---

## License

This project is licensed under the Apache License 2.0. For more details, see the [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).

---

## Contact

For questions, support, or feedback, please contact:

- **Ctrl+Learn Support Team**
- **Email:** [meera.paraskiva@gmail.com](mailto:meera.paraskiva@gmail.com)
