# 🎬 CineVerse — Movie Review System

A full-stack, web-based Movie Review application built according to the **Movie Review System** specifications and system architecture workflow.

---

## 📋 Project Abstract

> **The Movie Review System** is a web-based full-stack application designed to provide a platform for users to browse movies, view movie details, submit ratings, and write reviews. Users can register, log in, search for movies using titles or genres, and rate movies from 1 to 5 stars. Users can also write, edit, and delete their own reviews and read reviews submitted by other users. The system calculates and displays average movie ratings. An administrator can add, update, and delete movie details and manage users and reviews. The application is developed using **Java, Spring Boot, Maven, MySQL, HTML, CSS, JavaScript, and Bootstrap**, with **REST APIs** for communication between the frontend and backend.

---

## 🛠️ Technology Stack

| Layer | Technologies |
| :--- | :--- |
| **Frontend** | HTML5, CSS3, Vanilla JavaScript (ES6+ Fetch API), Bootstrap 5.3, Bootstrap Icons |
| **Backend** | Java 21, Spring Boot 3.3.4, Spring Security, JWT (io.jsonwebtoken 0.12.6) |
| **Build Tool** | Apache Maven 3.9+ |
| **Database** | MySQL 8.0 (with zero-config H2 profile for instant testing) |
| **ORM** | Spring Data JPA (Hibernate) |
| **API** | RESTful APIs (JSON) |
| **Server** | Embedded Apache Tomcat |
| **Tools** | VS Code / IntelliJ IDEA / Terminal |

---

## 🏛️ System Architecture

```
+-------------------------------------------------------+
|                       FRONTEND                        |
|        HTML5 • CSS3 • JavaScript • Bootstrap 5        |
+---------------------------+---------------------------+
                            | HTTP / REST API (JSON)
                            v
+-------------------------------------------------------+
|                    SPRING BOOT BACKEND                |
|  [Controllers]   -->   [Services]   -->  [Repositories]
|  - AuthController       - UserService     - UserRepository
|  - MovieController      - MovieService    - MovieRepository
|  - ReviewController     - ReviewService   - ReviewRepository
|  - UserController
|  - StatsController
+---------------------------+---------------------------+
                            | Spring Data JPA (Hibernate)
                            v
+-------------------------------------------------------+
|                     DATABASE LAYER                    |
|             MySQL 8.0  /  H2 In-Memory                |
|      (Tables: users, movies, reviews, indexes)        |
+-------------------------------------------------------+
```

---

## 🔄 System Workflow & Features

1. **User Authentication & Role-Based Access Control**:
   - Register new user accounts and log in via stateless **JWT Authentication**.
   - Dual roles: `ROLE_USER` and `ROLE_ADMIN`.
2. **Movie Browsing & Discovery**:
   - Dynamic real-time search by movie title, director, or genre.
   - Interactive genre filter pills (*All Genres, Sci-Fi, Action, Drama, Crime, Animation*).
   - Movie cards showcasing high-definition posters, release year, duration, and calculated average star rating.
3. **Movie Details & Media**:
   - Full plot synopsis, director, and cast.
   - **Watch Official Trailer**: Embedded 16:9 YouTube video player modal.
   - Average community star rating and review count breakdown.
4. **Rating & Review System**:
   - Interactive 1 to 5 star rating selector.
   - Rich critique/review comments.
   - Edit and delete own reviews directly from the movie page or **"My Written Reviews"** modal.
   - Live recalculation of average ratings upon any review submission, edit, or deletion.
5. **Administrator Dashboard**:
   - High-level platform statistics (Total Movies, Total Reviews, Active Users, Platform Avg Rating).
   - **Movie Management**: Add new movies, edit existing movie metadata, or delete movies.
   - **Review Moderation**: View all platform reviews with status (`APPROVED`, `PENDING`, `REJECTED`), approve/reject flags, or permanently remove spam.
   - **User Management**: View registered user accounts, toggle active/deactivated status, or delete accounts.

---

## 🔑 Pre-Configured Demo Accounts

When the application boots for the first time, it automatically creates sample movies and initial accounts:

| Role | Username | Email | Password |
| :--- | :--- | :--- | :--- |
| **Administrator** | `admin` | `admin@movies.com` | `admin123` |
| **Standard User** | `john_doe` | `john@movies.com` | `user123` |
| **Standard User** | `sarah_connor` | `sarah@movies.com` | `user123` |

---

## 🚀 Quick Start Guide

### Option 1: Instant Run (Zero Setup with In-Memory H2)

The application comes pre-configured with the `h2` profile active, allowing you to launch and test immediately:

```bash
cd "C:\Users\dantham enosh\.gemini\antigravity\scratch\movie-review-system"
mvn spring-boot:run
```

Once started:
- **Application URL**: [http://localhost:8080](http://localhost:8080)
- **H2 Web Console**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (JDBC URL: `jdbc:h2:mem:moviereviewdb`, User: `sa`, Password: *(leave blank)*)

---

### Option 2: Running with Local MySQL 8.0 Server

Your system has MySQL 8.0 service installed and running! To connect to MySQL:

1. **Create the Database and Tables**:
   Open MySQL Workbench or your terminal and execute `schema.sql`:
   ```sql
   mysql -u root -p < schema.sql
   ```
   *(Or copy the commands from `schema.sql` into MySQL Workbench)*

2. **Configure your MySQL Password**:
   Open `src/main/resources/application-mysql.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/movie_review_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   spring.datasource.username=root
   spring.datasource.password=YOUR_MYSQL_ROOT_PASSWORD
   ```

3. **Activate the MySQL profile**:
   In `src/main/resources/application.properties`, change:
   ```properties
   spring.profiles.active=mysql
   ```
   Or launch directly with Maven:
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=mysql
   ```

---

## 📡 REST API Reference

### 🔐 Authentication (`/api/auth`)
- `POST /api/auth/register` — Register a new account (`{ username, email, password }`)
- `POST /api/auth/login` — Sign in and receive JWT token (`{ usernameOrEmail, password }`)
- `GET /api/auth/me` — Get details of current authenticated user

### 🎬 Movies (`/api/movies`)
- `GET /api/movies` — List all movies (optional query params: `?search=inception&genre=Sci-Fi`)
- `GET /api/movies/{id}` — Get single movie details
- `GET /api/movies/genres` — Get list of distinct genres
- `POST /api/movies` *(Admin)* — Create a new movie
- `PUT /api/movies/{id}` *(Admin)* — Update existing movie details
- `DELETE /api/movies/{id}` *(Admin)* — Delete a movie

### ⭐ Reviews (`/api`)
- `GET /api/movies/{movieId}/reviews` — Get all approved public reviews for a movie
- `GET /api/movies/{movieId}/my-review` — Get the current user's review for a movie
- `POST /api/movies/{movieId}/reviews` — Submit a rating (1-5) and review comment
- `PUT /api/reviews/{id}` — Update user's own review
- `DELETE /api/reviews/{id}` — Delete user's review
- `GET /api/admin/reviews` *(Admin)* — View all platform reviews
- `POST /api/admin/reviews/{id}/approve` *(Admin)* — Approve review
- `POST /api/admin/reviews/{id}/reject` *(Admin)* — Reject/flag review

### 👤 Users & Stats (`/api`)
- `GET /api/users/profile` — Get profile of logged-in user
- `GET /api/users/profile/reviews` — Get list of reviews written by logged-in user
- `GET /api/admin/stats` *(Admin)* — Get metrics (total movies, reviews, users, pending, platform rating)
- `GET /api/admin/users` *(Admin)* — View all users
- `PATCH /api/admin/users/{id}/toggle-status` *(Admin)* — Enable/disable a user account
- `DELETE /api/admin/users/{id}` *(Admin)* — Remove user account

---

## 📂 Project Directory Structure

```
movie-review-system/
├── pom.xml                                   # Maven dependencies & build configuration
├── schema.sql                                # Complete MySQL 8.0 database schema
├── README.md                                 # Documentation & usage guide
└── src/
    └── main/
        ├── java/com/moviereview/
        │   ├── MovieReviewApplication.java   # Spring Boot entry point
        │   ├── config/
        │   │   ├── SecurityConfig.java       # Spring Security & route protections
        │   │   ├── JwtTokenProvider.java     # JWT token generation & validation
        │   │   ├── JwtAuthenticationFilter.java # Bearer token filter
        │   │   └── WebConfig.java            # CORS configuration
        │   ├── controller/
        │   │   ├── AuthController.java       # Registration, Login, Identity
        │   │   ├── MovieController.java      # Movies CRUD & search
        │   │   ├── ReviewController.java     # Reviews CRUD & moderation
        │   │   ├── UserController.java       # Profiles & user management
        │   │   └── StatsController.java      # Dashboard metrics
        │   ├── dto/                          # Request & response data transfer objects
        │   ├── entity/                       # JPA entities (User, Movie, Review, Enums)
        │   ├── repository/                   # Spring Data JPA repositories
        │   ├── service/                      # Business logic & average rating calculations
        │   └── data/
        │       └── DataInitializer.java      # Automatic seeding of sample movies & accounts
        └── resources/
            ├── application.properties        # Main config & profile selector
            ├── application-h2.properties     # H2 database profile
            ├── application-mysql.properties  # MySQL 8.0 database profile
            └── static/                       # Full-Stack Web Frontend
                ├── index.html                # Single-page interface & Bootstrap modals
                ├── css/style.css             # Cinematic dark theme styling
                └── js/                       # Modular ES6 JavaScript
                    ├── api.js                # Fetch API client
                    ├── auth.js               # Authentication & session management
                    ├── movies.js             # Movie browsing, search, details, trailers
                    ├── reviews.js            # Star ratings, review submission & editing
                    ├── admin.js              # Admin dashboard & moderations
                    └── app.js                # Application initialization
```
