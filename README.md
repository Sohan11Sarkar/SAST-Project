Spring Boot Demo Application -
- A simple Spring Boot web app with 3 pages: login, register, and home. Users can create an account, sign in, and log out. Passwords are stored in an H2 in-memory database which resets every time the app restarts. The app runs on a single port (8080) with no separate frontend server needed since Thymeleaf renders the UI directly from Spring Boot. It intentionally contains security bugs like plaintext password storage, a hardcoded backdoor password, and disabled CSRF.

Tech Stack:
- Spring Boot 4.0.6 + Spring Security
- Thymeleaf (UI)
- H2 in-memory database
- Java 21 / Maven

Run the app:
mvn spring-boot:run
Then open http://localhost:8080/login in your browser.

Pages:
- Login - http://localhost:8080/login
- Register - http://localhost:8080/register
- Home - http://localhost:8080/home
- H2 Console - http://localhost:8080/h2-console
