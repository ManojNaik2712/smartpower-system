# SmartPower System 

A SmartPower System is a Spring Boot microservices-based electricity management system that handles user registration, authentication, billing, payment due reminders, and power outage notifications. Built for a scalable backend architecture.

---

## Project Structre

  smartpower-system/
 - auth-service         : Handles login, registration, and JWT generation
 - user-service         : Manages user profiles and stores pincode info
 - payment-service      : Manages payments and due dates
 - notification-service : Sends outage & due date alerts via Kafka
 - common               : Shared DTOs and utilities across services

---

##  Tech Stack
 - Java + Spring Boot

 - Spring Security + JWT (Authentication)

 - Spring Kafka (Async Communication)

 - MySQL (Database)

 - Lombok, FeignClient, RestTemplate

 - Postman for API testing
    
---

##  Features

- ✅ User Registration/Login (JWT Secured)

- ✅ Role-based Access (Admin & User)

- ✅ User Pincode Registration for location-based alerts

- ✅ Outage Notifications via Kafka

- ✅ Bill Payment System

- ✅ Scheduled Due Date Reminders

- ✅ Inter-service communication via FeignClient

---

##  Microservices Flow

1. Registration & Authentication :
   - User registers → auth-service
   - Login → JWT Token issued

2. User Profile :
   - Stored in user-service
   - Includes name, email, and pincode

3. Bill & Payment :
   - User makes payment → record updated in DB
   - Due date automaticaly stored in user DB

4. Due Date Reminder
   - Cron job (scheduler) checks daily
   - If due in 3 days, sends Kafka event → notification-service

5. Power Outage Alerts
   - Admin sends alert to users of a specific pincode
   - notification-service filters users and sends Kafka events

---

##  How to Run

1. Clone the repo:
    git clone https://github.com/ManojNaik2712/smartpower-system.git
  
2. Set up MySQL with databases:
    auth_db, user_db, payment_db, notification_db

3. Configure application.properties in each service:
    Add MySQL URL, username, password
    Kafka config (localhost:9092)

4. Start Zookeeper & Kafka (local setup):
    - Zookeeper
      bin/zookeeper-server-start.sh config/zookeeper.properties
      
    - Kafka
      bin/kafka-server-start.sh config/server.properties
  
5. Run each microservice via IDE or terminal.

---

## 🧠 Future Enhancements

- Add Spring Cloud Gateway for API routing

- Integrate Eureka for service discovery

- Add Docker support for containerization

- Integrate Razropay for real payment transaction

---

## 🤝 Contributing

- Fork this repo

- Create a new branch (feature/your-feature)

- Commit your changes

- Push and create a PR
