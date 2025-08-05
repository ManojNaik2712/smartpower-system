# SmartPower System 

SmartPower System is a Spring Boot-based microservices project designed to modernize and streamline electricity management for power distribution companies, especially in rural areas. It offers secure JWT-based user authentication, real-time outage notifications via SMS and email using Kafka-driven event communication, and allows users to make payments and submit complaints easily. With location-based pincode mapping, admins can notify users in specific areas about outages or faults, and users can report issues directly to their local admin. The system bridges the communication gap for users who may not have access to email but rely on SMS, ensuring timely updates and efficient resolution of electricity-related concerns. Integrated Swagger documentation and modular architecture make it scalable, maintainable, and suitable for real-world deployment.

---

## Project Structre

This is a multi-module Maven project with the following services:

  smartpower-system/
 - auth-service         : Handles login, registration, and JWT generation
 - user-service         : Stores user data and manages user operations
 - payment-service      : Manages user bills, payments, and due date tracking
 - notification-service : Sends outage & due date alerts via Kafka
 - common               : Shared DTOs and utilities across services
 - common-exceptions    : Handles the customized exceptions

---

##  Tech Stack

- **Java + Spring Boot**
- **Maven** (multi-module structure)
- **Spring Security + JWT** (Authentication)
- **Spring Kafka** (Async Communication)
- **MySQL** (Database)
- **Lombok, FeignClient**
- **Postman** for API testing
- **MailSender** for email alerts
- **Swagger/OpenAPI** for API docs
    
---

##  Features

-  User registration/login with role-based access (`ADMIN`, `USER`)
-  Outage alerts sent to users by pin code (via Email)
-  Bill generation, payment, and tracking due dates
-  User complaints module with status management
-  Email notification support using Spring Mail
-  Kafka-based communication for sending notifications and events
-  Swagger UI for each service
  
---

## 🛠️ Running the Project Locally

### 1. **Clone the repository**
git clone https://github.com/ManojNaik2712/smartpower-system.git  
cd smartpower-system  

### 2. **Import as Maven Project**
- Open your IDE (e.g., IntelliJ, Eclipse).
- Import the project as a Maven multi-module project.

### 3. **Database Setup**
- Make sure MySQL is installed and running.
- Create the database:
   CREATE DATABASE smartpower;

### 4. **Run Microservices Individually**
  Each microservice has its own module:
- auth-service
- user-service
- payment-service
- notification-service

You can run each one using your IDE or via CLI:

cd auth-service

./mvnw spring-boot:run

Repeat this for each service.

### 5. **Access Swagger UI**
Swagger UI is enabled for each service:
- Auth Service: http://localhost:6001/swagger-ui/index.html

- User Service: http://localhost:6002/swagger-ui/index.html

- Payment Service: http://localhost:6003/swagger-ui/index.html

- Notification Service: http://localhost:6004/swagger-ui/index.html

---

##  Postman Collection

SmartPower System Postman Collection:

https://www.postman.com/payload-physicist-15218924/workspace/smartpowersystem/collection/39955671-8d7aa289-d22b-4e95-b1d9-4d60adf61307?action=share&creator=39955671

---

##  Future Improvements

Here are some features and enhancements planned for future updates:

-  **Integrate API Gateway** for centralized routing and security
-  **Add Frontend Dashboard** using React.js for user interaction
-  **Implement Real Payment Gateway** like Razorpay or Stripe for secure transactions
-  **Dockerize All Services** for containerized deployment
-  **Add Unit & Integration Tests** for key services

---

## 🙋‍♂️ Author

**Manoj Naik** – A recent college graduate passionate about software development, backend systems, and building real-world applications. [GitHub](https://github.com/ManojNaik2712)
  

