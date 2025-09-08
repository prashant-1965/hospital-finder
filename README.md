# 🏥 Doctor-Hospital Finder

A **backend-focused Spring Boot project** designed to help users find hospitals, doctors, facilities, and manage appointments — built with **clean architecture, performance optimization, and production-ready practices** in mind.

---

## 🚀 Features

- **Clean, Layered Architecture** – Controller → Service → Repository → Entity  
- **100+ Java Classes** for scalability and maintainability  
- **50+ JPQL/HQL Queries** – optimized for efficient database operations  
- **Pagination & Sorting** – for seamless handling of large datasets  
- **Redis Caching** – dramatically reduces response time  
- **Custom DTOs & Projections** – send only the required data to the client  
- **Global Exception Handling** – consistent error responses  
- **Dependency Injection & Loose Coupling** – following Spring best practices  

---

## 🛠 Tech Stack

- **Backend:** Spring Boot, Spring Data JPA  
- **Database:** PostgreSQL  
- **Caching:** Redis  
- **Build Tool:** Maven  
- **Version Control:** Git  

---

## 📂 Project Structure

```plaintext
doctor-hospital-finder/
├── src/main/java/com/example/
│   ├── controller/       # REST Controllers (Doctor, Hospital, Facility, Appointment)
│   ├── service/          # Business logic layer
│   ├── repository/       # Spring Data JPA repositories + JPQL queries
│   ├── dto/              # Data Transfer Objects
│   ├── projection/       # Interface-based projections
│   ├── config/           # Configurations (Redis, pagination, etc.)
│   └── exception/        # Global exception handling
└── src/main/resources/
    └── application.yml   # Configuration (DB, Redis, etc.)
