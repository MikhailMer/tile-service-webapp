# Tile Service Web App

Production-grade web application developed for a real tile installation service business. The repository contains the public, sanitized open-source version of the project's architecture and backend logic.

## About The Project

**Tile Service Web App** is a full-featured web solution designed for a tile installation and remodeling business. The platform bridges the gap between clients and service providers by offering:
* **Interactive Cost Calculator:** A dynamic JavaScript-powered tool that allows customers to instantly estimate project pricing based on their specific inputs.
* **Service Request Submission:** A clean, user-friendly contact form where clients can submit their personal details and project specifics.
* **Direct Database Workflow:** Submitted requests are securely validated, saved into a relational PostgreSQL database, and made instantly available in the administrative dashboard for follow-up via phone.

## Tech Stack
* **Core:** Java 17, Spring Boot
* **Web & UI:** Spring MVC, Thymeleaf, HTML5, CSS3, JavaScript
* **Data & Persistence:** Spring Data JPA, Hibernate, PostgreSQL
* **Validation & Security:** Spring Boot Validation, Spring Security, Cloudflare Turnstile
* **Build Tool:** Maven

## Security & Protection Layers
* **Environment-Based Config:** Zero hardcoded secrets; configuration relies strictly on environment variables.
* **Authentication & Authorization:** Role-based access control powered by Spring Security, protecting administrative routes.
* **Anti-Bot & Spam Protection:** Integrated Cloudflare Turnstile CAPTCHA and a custom **Honeypot** mechanism for form submissions.
* **Request Integrity:** Built-in **CSRF protection** and server-side input validation to secure user sessions and endpoints.

## Key Features
* **Interactive Service & Calculation Forms:** Client forms equipped with server-side validation, Cloudflare Turnstile CAPTCHA, and an anti-bot **Honeypot** mechanism.
* **Custom Authentication UI:** A dedicated, brand-styled login page secured against automated bot attacks and brute-force attempts.
* **Admin Dashboard & Order Management:** A protected control panel where administrators can view aggregated client data, including contact details, specific project zones, and whether demolition work is required.
* **Safe Record Deletion:** Interactive confirmation prompts built into deletion actions to prevent accidental data loss in the database.
* **Data Integrity & Security:** Role-based access control, encrypted password hashing (BCrypt), and CSRF protection across sensitive routes.

## Database Structure (PostgreSQL)

The application uses a relational database schema designed to cleanly separate system users from client orders:
* **`users` table:** Stores administrative credentials with securely hashed passwords.
* **`orders` table:** Captures primary contact information submitted through the website forms (names, phone numbers).
* **`order_zones` table:** Relates individual clients to their specific projects (supporting multiple installation areas or rooms per single client).

## Quick Start & Local Setup

To run this project locally for testing and evaluation, follow these steps:

1. **Clone the repository:**
   ```bash
   git clone https://github.com/your-username/TileWebsite.git
   cd TileWebsite
   ```
   
2. **Configure the Database:**
   Make sure PostgreSQL is installed, then create a local database (e.g., tile_db). Update your credentials in src/main/resources/application.properties or pass them via environment variables:
    ```bash   
    export DB_URL=jdbc:postgresql://localhost:5432/tile_db
    export DB_USERNAME=postgres
    export DB_PASSWORD=your_password
    ```
3. **Run the Application:**

You can run the project via Maven or directly through your IDE (IntelliJ IDEA):
```bash 
mvn spring-boot:run
```

4. **Access the Application:**

Open your browser and navigate to: http://localhost:8080
