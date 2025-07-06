# job-finder
Spring Boot application to clone Job Finding Applications 

# Features
- Clone job finding applications like Naukri, Indeed, etc.
- Search for jobs based on various criteria
- Apply for jobs directly through the application
- User authentication and profile management
- Admin panel for managing job listings and user accounts
- API could be consumed by Mobile Applications or Web Applications.
- Job Alerts via Email using Spring Boot Email Service
- Job Recommendations based on user profile and search history

# Technologies Used
- Java 21
- Spring Boot 3.2.5
- Spring Data JPA
- Spring Security
- Spring Boot Email Service
- Redis for caching
- PostgreSQL for database
- RabbitMQ for messaging

# Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/mohammedysamir/job-finder
   ```
2. Navigate to the project directory.
3. Install dependencies:
   ```bash
   ./mvnw clean install
   ```
4. Configure the application properties in `src/main/resources/application.properties`.
5. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```
6. Run Redis, RabbitMQ, and PostgreSQL services if not already running.
7. Access the application at `http://localhost:8080`.

# Contributing
Contributions are welcome! Please fork the repository and submit a pull request with your changes.
