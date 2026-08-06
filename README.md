Just turning my learning into meaningful projects.

# 🏦 Banking & Account Management REST API

A **Spring Boot RESTful Web Application** for managing **Banks, Branches, Addresses, and Customer Accounts**. The application follows a **layered architecture** and is built using **Spring Boot, Spring Data JPA, Hibernate, and MySQL**. It demonstrates REST API development, entity relationships, transaction management, custom exception handling, and standardized API responses.

---

## 🚀 Features

- CRUD operations for Bank management
- Manage Bank, Address, and Account entities
- One-to-One and One-to-Many entity relationships
- RESTful APIs using Spring Boot
- Spring Data JPA with Hibernate ORM
- Transaction management using `@Transactional`
- Custom exception handling
- Standardized API response wrapper
- JPQL and derived query methods
- MySQL database integration
- Postman tested APIs

---

## 🛠️ Tech Stack

| Technology | Description |
|------------|-------------|
| Java 17+ | Programming Language |
| Spring Boot 3.x | Backend Framework |
| Spring Data JPA | Persistence Layer |
| Hibernate ORM | ORM Framework |
| MySQL | Database |
| Maven | Build Tool |
| Postman | API Testing |
| Eclipse / IntelliJ IDEA | IDE |

---

# 🏗️ Project Architecture

```
                Client
                   │
                   ▼
            REST Controller
                   │
                   ▼
               Service Layer
                   │
                   ▼
            Repository Layer
                   │
                   ▼
               MySQL Database
```

The application follows a **Layered Architecture**, separating business logic from persistence logic, making the project clean, maintainable, and scalable.

---

# 📂 Project Structure

```
src
├── controller
│   ├── BankController.java
│   └── AccountController.java
│
├── service
│   ├── BankService.java
│   └── AccountService.java
│
├── repository
│   ├── BankRepository.java
│   ├── AddressRepository.java
│   └── AccountRepository.java
│
├── entity
│   ├── Bank.java
│   ├── Address.java
│   └── Account.java
│
├── exception
│   ├── ResourceNotFoundException.java
│   ├── InvalidDataException.java
│   └── GlobalExceptionHandler.java
│
├── util
│   └── ResponseStructure.java
│
└── BankingApplication.java
```

---

# 🔗 Entity Relationship

```
                    1 : 1
+----------------+-------------------+
|     Bank       |                   |
|----------------|                   |
| bankId         |                   |
| bankName       |                   |
| ifsc           |                   |
| branchName     |                   |
| contactNo      |                   |
+----------------+                   |
         │                           |
         │                           |
         ▼                           ▼
                +------------------------+
                |       Address          |
                |------------------------|
                | addressId              |
                | street                 |
                | city                   |
                | state                  |
                | pincode                |
                +------------------------+

                1
                │
                │
                │
                ▼
          Many Accounts

+------------------------+
|        Account         |
|------------------------|
| accountId              |
| accountNumber          |
| balance                |
| accountType            |
+------------------------+
```

### Relationship Mapping

- **Bank ↔ Address**
    - `@OneToOne`
    - `CascadeType.ALL`
    - `orphanRemoval = true`

- **Bank ↔ Account**
    - `@OneToMany`
    - `mappedBy = "bank"`
    - `@JsonIgnore` prevents infinite recursion during JSON serialization.

---

# 🌐 REST API Endpoints

## 🏦 Bank APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/bank/all` | Get all banks |
| PUT | `/bank/update` | Update bank details |

---

## 💳 Account APIs

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/account/greater?value=5000` | Fetch accounts having balance greater than specified amount |

---

# 📥 Sample Request

## Update Bank

```json
{
  "bankId": 2,
  "bankName": "Axis Bank",
  "branchName": "Jayanagar 4th Block",
  "contactNo": "9876500002",
  "ifsc": "UTIB0003003",
  "address": {
    "street": "4th Block Main Road",
    "city": "Bengaluru",
    "state": "Karnataka",
    "pincode": "560041"
  }
}
```

---

# 📤 Sample Response

```json
{
  "statusCode": 200,
  "message": "Bank updated successfully",
  "data": {
    "bankId": 2,
    "bankName": "Axis Bank",
    "branchName": "Jayanagar 4th Block",
    "ifsc": "UTIB0003003",
    "contactNo": "9876500002"
  }
}
```

---

# ❌ Error Response

```json
{
  "statusCode": 404,
  "message": "Bank not found",
  "data": null
}
```

---

# ⚠️ Exception Handling

The application uses centralized exception handling with `@RestControllerAdvice`.

### Custom Exceptions

- `ResourceNotFoundException`
- `InvalidDataException`

All responses are returned using a common wrapper:

```java
ResponseStructure<T>
```

This ensures consistent API responses across the application.

---

# 🔑 Key Concepts Used

- Spring Boot
- Spring MVC
- Spring Data JPA
- Hibernate ORM
- RESTful Web Services
- Dependency Injection (IoC)
- Layered Architecture
- Entity Relationships
- JPQL
- Derived Query Methods
- Transaction Management
- Exception Handling
- ResponseEntity
- MySQL Integration

---

# ⚙️ Transaction Management

The update operations are annotated with:

```java
@Transactional
```

This enables:

- Hibernate Dirty Checking
- Automatic persistence of managed entities
- Atomic database transactions
- Rollback on failures

---

# 🧪 API Testing

The APIs were tested using **Postman**.

Example:

- GET Requests
- PUT Requests
- Query Parameters
- JSON Payloads
- Error Responses

---

# ▶️ Getting Started

## 1. Clone Repository

```bash
git clone https://github.com/your-username/banking-management-api.git
```

```bash
cd banking-management-api
```

---

## 2. Configure Database

Update `application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/banking_db
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## 3. Build Project

```bash
mvn clean install
```

---

## 4. Run Application

```bash
mvn spring-boot:run
```

or

```bash
java -jar target/banking-management-api.jar
```

---

# 🚀 Future Enhancements

- Spring Security with JWT Authentication
- Swagger/OpenAPI Documentation
- Bean Validation
- Pagination & Sorting
- Unit Testing using JUnit & Mockito
- Docker Support
- CI/CD Integration
- Logging using SLF4J & Logback

---

# 👨‍💻 Author

**R K Nag**

Information Science Engineering Student

Java Full Stack Developer

---

# 📄 License

This project is licensed under the **MIT License**.
