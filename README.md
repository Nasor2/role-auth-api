# Role-Based Authentication API

![Java](https://img.shields.io/badge/Java-17-blue) ![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.2-green) ![Spring Security](https://img.shields.io/badge/Spring_Security-6.x-brightgreen) ![JWT](https://img.shields.io/badge/JWT-Authentication-orange) ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue) ![Maven](https://img.shields.io/badge/Maven-3.9-red) ![Docker](https://img.shields.io/badge/Docker-ready-blue) ![Swagger UI](https://img.shields.io/badge/Swagger_UI-OpenAPI_3-purple)

A robust and secure RESTful API designed for user management and role-based authentication, leveraging Java and the Spring Boot ecosystem. It implements an `ADMIN` and `USER` role system to control access to different resources, with JWT authentication and refresh tokens for secure and efficient session management.

---

## 🚀 Key Features

* **JWT Authentication:** Full implementation of JSON Web Tokens for API security, including token generation, validation, and refresh.
* **User Roles:** Support for `ADMIN` and `USER` roles with granular role-based authorization using Spring Security.
* **User Management:** Endpoints to register new users, list all users (ADMIN only), get specific user details, and update user roles (ADMIN only).
* **Refresh Tokens:** Mechanism to obtain new Access Tokens without requiring user credentials again, enhancing user experience and security.
* **RESTful API:** Designed following REST principles, using appropriate HTTP status codes and JSON response bodies.
* **Data Validation:** Robust input validation for all DTOs using `jakarta.validation`.
* **Relational Database:** Data persistence using PostgreSQL.
* **Docker Containerization:** Facilitates easy deployment and execution of the application in isolated and portable environments.
* **Interactive Documentation:** Integration with SpringDoc OpenAPI (Swagger UI) for clear and interactive API documentation.

---

## ⚙️ Technologies and Tools

* **Language:** Java 17
* **Framework:** Spring Boot 3.3.2
    * **Security:** Spring Security 6.x
    * **Web:** Spring Web
    * **Data:** Spring Data JPA
* **Database:** PostgreSQL 16
* **Dependency Management:** Maven 3.9.x
* **Containerization:** Docker, Docker Compose
* **API Documentation:** SpringDoc OpenAPI (Swagger UI)
* **JSON Web Tokens:** `jjwt` (Java JWT)
* **Connection Pool:** HikariCP

---

## 🏗️ Architecture and Design Patterns

The project follows a modular and clean architecture, applying several design patterns to ensure maintainability, scalability, and separation of concerns:

The project adheres to the principles of **Clean Architecture**. This approach promotes a strong separation of concerns, high maintainability, and decoupled code, ensuring that core business logic is independent of frameworks, databases, or the user interface.


* **Dependency Injection (DI):** Utilizes Spring's IoC container to manage dependencies between components, promoting decoupled and testable code.
* **Repository Pattern:** Abstracts data access logic, allowing for easy underlying database changes if needed.
* **DTOs (Data Transfer Objects):** DTOs are used for API input and output, decoupling domain models from API representation and enabling specific validations.
* **Token-Based Security (JWT):** Implements a stateless authentication and authorization flow, where tokens are digitally signed and verified with each request.
* **Builder Pattern (implicit in `record` DTOs):** DTOs defined as Java 17 `record` types provide immutability and concise construction, similar to the benefits of the Builder pattern in data contexts.

---

## 📖 API Documentation (Swagger UI)

The API is fully documented with SpringDoc OpenAPI, providing an interactive user interface (Swagger UI) to explore, test, and understand all endpoints.

* **Swagger UI URL:** `http://localhost:8080/swagger-ui.html`
* **OpenAPI Specification URL:** `http://localhost:8080/v3/api-docs`

The documentation includes:
* Detailed description of each endpoint, its input parameters, and response DTOs.
* Definition of the JWT security scheme (`bearerAuth`), allowing testing of protected endpoints directly from the Swagger UI.
* Examples of request bodies and response bodies.

---

## 🔑 Key Endpoints and Functionalities

### **Authentication Endpoints (`/api/v1/auth`)**

* `POST /register`: Registers a new user with a default `USER` role. Returns an `AuthResponseDto` with `accessToken` and `refreshToken`.
    * **Request Body:** `RegisterRequestDto` (username, firstName, lastName, password)
    * **Response:** `AuthResponseDto`
* `POST /login`: Authenticates an existing user. Returns an `AuthResponseDto` with `accessToken` and `refreshToken`.
    * **Request Body:** `LoginRequestDto` (username, password)
    * **Response:** `AuthResponseDto`
* `POST /refresh-token`: Uses an existing `refreshToken` to obtain a new `accessToken` and a new `refreshToken`.
    * **Request Body:** `RefreshTokenRequestDto` (refreshToken)
    * **Response:** `AuthResponseDto`

### **User Management Endpoints (`/api/v1/users`)**

*(Requires JWT Authentication - `ADMIN` role or the user itself)*

* `GET /{id}`: Retrieves details for a specific user by their ID.
    * **Path Parameters:** `id` (Long)
    * **Response:** `UserResponseDto`
    * **Authorization:** `hasRole('ADMIN')` or `#id == authentication.principal.id`

* `PUT /{id}`: Updates details for a specific user by their ID.
    * **Path Parameters:** `id` (Long)
    * **Request Body:** `RegisterRequestDto` (username, firstName, lastName, password)
    * **Response:** `UserResponseDto`
    * **Authorization:** `hasRole('ADMIN')` or `#id == authentication.principal.id`

### **Admin Management Endpoints (`/api/v1/admin`)**

*(Requires JWT Authentication - `ADMIN` role)*

* `GET /users`: Retrieves a list of all registered users in the system.
    * **Response:** `List<UserResponseDto>`
    * **Authorization:** `hasRole('ADMIN')`
* `PUT /users/{id}/role`: Updates the role of a specific user.
    * **Path Parameters:** `id` (Long)
    * **Request Body:** `String` (new role, e.g., "ADMIN" or "USER")
    * **Response:** `UserResponseDto`
    * **Authorization:** `hasRole('ADMIN')`

---

## 🚀 How to Run the Project

### Prerequisites

* Java Development Kit (JDK) 17 or higher
* Maven 3.x
* Docker and Docker Compose

### 1. Environment Configuration

Create a `.env` file in the project's root directory (next to `docker-compose.yaml`) with the following variables:

```env
DB_NAME=db_auth
DB_USER=admin
DB_PASSWORD=123
DB_JWT_SECRET_KEY=8228afb2ef6554fe3f80d2d4a75990afb9ef2f472418a8f643fcff3a482dc784 # Ensure this is a long and secure key!
ADMIN_PASSWORD=devAdminPassword! # Password for the initial ADMIN user
```


### 2. Running with Docker Compose (Recommended)

This is the easiest way to spin up the application along with its PostgreSQL database:

1.  Ensure you are in the project's root directory.
2.  Execute the following command to build the images and bring up the services:
    ```
    docker compose up --build -d
    ```
    * --build: Forces a rebuild of the Docker images (necessary if there are code or pom.xml changes).
    * -d: Runs the containers in detached mode (in the background).

3.  Verify that the containers are running:
    ```
    docker compose ps
    ```
    You should see db and app with a running status.

4.  The application will be available at http://localhost:8080.
    * Access the interactive documentation at: http://localhost:8080/swagger-ui.html

5.  To stop and remove the containers:
    ```
    docker compose down
    ```

---

## 📁 Project Structure

```
.
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/nasor/roleauthapi/
│   │   │       ├── application/
│   │   │       │   ├── dto/
│   │   │       │   ├── AuthService.java
│   │   │       │   └── UserService.java
│   │   │       ├── domain/
│   │   │       │   ├── RefreshToken.java
│   │   │       │   ├── RefreshTokenRepository.java
│   │   │       │   ├── Role.java
│   │   │       │   ├── User.java
│   │   │       │   └── UserRepository.java
│   │   │       └── infraestructure/
│   │   │           ├── config/
│   │   │           ├── controller/
│   │   │           ├── persistence/
│   │   │           ├── repository/
│   │   │           ├── security/
│   │   │           └── service/
│   │   │       └── RoleAuthApiApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── ...
│   └── test/
│       └── ...
├── .env
├── Dockerfile
├── docker-compose.yaml
├── pom.xml
└── README.md
```

---

## 📝 Additional Notes

> [!NOTE]
> **Initial ADMIN User:** Upon the first startup of the application, if the database is empty, an `ADMIN` user will be automatically created using the credentials defined in the `ADMIN_PASSWORD` environment variable. This facilitates initial setup and access to administrative endpoints.

> [!NOTE]
> **JWT and Refresh Tokens:**
> * The `accessToken` has a short validity (1 hour by default) and is used to access protected API resources.
> * The `refreshToken` has a longer validity (7 days by default) and is used to obtain new `accessToken`/`refreshToken` pairs without needing to re-authenticate with credentials.

---

## ⚠️ Important Considerations / Restrictions

> [!IMPORTANT]
> **Fixed Roles:** This API currently supports only two fixed roles: `ADMIN` and `USER`. There is no endpoint to dynamically create or modify roles. Role management is limited to assigning these two roles to existing users.

> [!IMPORTANT]
> **No Password Reset:** The API does not implement a password recovery mechanism (e.g., "forgot password"). In case of a forgotten password, manual database intervention or the creation of a new user would be required.

> [!IMPORTANT]
> **ADMIN Access to User Data:** Users with the `ADMIN` role have full access to view and modify any user's details, including changing roles. Use `ADMIN` credentials with caution.

> [!IMPORTANT]
> **JWT Secret Key:** The secret key (`DB_JWT_SECRET_KEY`) used to sign JWT tokens must be **highly secure** and kept **confidential** (atleast 256 bits of lenght). Never expose it in source code or public logs. A weak or compromised key would nullify token security.
