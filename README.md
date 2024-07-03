# AuthFortress

AuthFortress is a secure user authentication and authorization system built with Spring Boot and PostgreSQL.

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Setup Instructions](#setup-instructions)
- [API Documentation](#api-documentation)
- [Contributing](#contributing)
- [License](#license)

## Introduction

AuthFortress provides robust user authentication and authorization capabilities using Spring Security and JWT tokens. It manages user registration, login, account activation via email, and role-based access control.

## Features

- User registration with email verification
- User login with JWT token generation
- Role-based access control (ADMIN, USER)
- Secure password hashing using BCrypt
- Integration with PostgreSQL database

## Technologies Used

- Spring Boot
- Spring Security
- PostgreSQL
- JWT (JSON Web Token)
- Swagger (OpenAPI)
- Lombok
- JavaMailSender

## Prerequisites

Before you begin, ensure you have the following installed:

- Java Development Kit (JDK) 8 or higher
- Apache Maven
- PostgreSQL database
- IDE (IntelliJ IDEA, Eclipse, etc.)

## Setup Instructions

1. **Clone the repository:**

   ```bash
   git clone https://github.com/Deraclassic/AuthFortress.git
   cd AuthFortress

## Setup Instructions

1. **Set up PostgreSQL database:**
    - Create a PostgreSQL database and configure the connection in `application.properties`.
2. **Email Setup:**
    - Configure your email settings in `application.properties` for sending verification emails.
   ```properties
   spring.mail.host=smtp.example.com
   spring.mail.port=465
   spring.mail.username=${EMAIL}
   spring.mail.password=${EMAIL_PASSWORD}
   spring.mail.properties.mail.smtp.auth=true
   spring.mail.properties.mail.smtp.starttls.enable=true
     ```
   
    - Configure your EMAIL and EMAIL_PASSWORD in your Environment Variables.
3. **JWT Setup:**
   - Configure your JWT settings in `application.properties`.

     ```properties
     jwt.secret=${SECRET_KEY}
     jwt.expiration=86400000
     ```
     
4. **Build and run the application:**

    - Build the application using Maven:

      ```bash
      mvn clean install
      ```

    - Run the application:

      ```bash
      mvn spring-boot:run
      ```

5. **Access the application:**
   - Open a web browser and go to `http://localhost:8080`.

## API Documentation

For API documentation and endpoints, refer to the Swagger UI available at:

- Local: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Contributing

Contributions are welcome! Please fork the repository and submit pull requests to contribute to AuthFortress.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.