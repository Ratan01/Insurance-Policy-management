# 🛡️ Insurance Policy Management System

A **full‑stack Insurance Policy Management System** built using **Spring Boot (Backend)** and **React.js (Frontend)** with **JWT‑based authentication and role‑based authorization**. This project demonstrates enterprise‑level backend design, secure REST APIs, and modern frontend integration.

---

## 📌 Features

### 🔐 Authentication & Authorization

* User Registration & Login
* JWT Token‑based authentication
* Role‑based access control (**ADMIN / USER**)
* Secure API endpoints using Spring Security

### 👤 User Management

* Register new users
* Login with JWT
* Fetch users (Admin only)
* Update & delete users

### 📄 Insurance Policy Management

* Create insurance policies (User)
* View own policies (User)
* View all policies (Admin)
* Update policy (Owner only)
* Delete policy (Owner only)

### 🧪 Testing & Quality

* Unit & Controller tests using **JUnit 5, Mockito, MockMvc**
* JaCoCo code coverage (80%+ target)

---

## 🧰 Technology Stack

### Backend

* Java 17+
* Spring Boot 3.x
* Spring Security + JWT
* Spring Data JPA (Hibernate)
* PostgreSQL (Production)
* H2 Database (Testing)
* Maven

### Frontend

* React.js
* React Router
* Axios
* JWT Authentication (Interceptor)

---

## 📂 Project Structure

```
insurance-policy-management/
│
├── backend/
│   └── Insurance-Policy-management/
│       ├── src/main/java/com/java/ipm/
│       │   ├── controller/
│       │   ├── service/
│       │   ├── repository/
│       │   ├── entity/
│       │   ├── dto/
│       │   └── security/
│       ├── src/test/java/
│       └── pom.xml
│
├── frontend/
│   └── insurance-policy-management/
│       ├── src/
│       ├── public/
│       └── package.json
│
├── .gitignore
└── README.md
```

---

## 🔑 API Endpoints

### Authentication

| Method | Endpoint             | Description     | Access |
| ------ | -------------------- | --------------- | ------ |
| POST   | `/api/auth/register` | Register user   | Public |
| POST   | `/api/auth/login`    | Login & get JWT | Public |

### Insurance Policies

| Method | Endpoint             | Description      | Access             |
| ------ | -------------------- | ---------------- | ------------------ |
| GET    | `/api/policies`      | Get all policies | Admin              |
| GET    | `/api/policies/{id}` | Get policy by ID | User/Admin (Owner) |
| POST   | `/api/policies`      | Create policy    | User               |
| PUT    | `/api/policies/{id}` | Update policy    | User (Owner)       |
| DELETE | `/api/policies/{id}` | Delete policy    | User (Owner)       |

---

## ⚙️ Backend Setup

### 1️⃣ Clone Repository

```bash
git clone <repository-url>
cd backend/Insurance-Policy-management
```

### 2️⃣ Configure Database

Update `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/insurance_db
spring.datasource.username=postgres
spring.datasource.password=your_password
```

### 3️⃣ Run Backend

```bash
mvn clean install
mvn spring-boot:run
```

Backend will start at:

```
http://localhost:8080
```

---

## ⚛️ Frontend Setup

### 1️⃣ Navigate to frontend

```bash
cd frontend/insurance-policy-management
```

### 2️⃣ Install dependencies

```bash
npm install
```

### 3️⃣ Run React app

```bash
npm start
```

Frontend will start at:

```
http://localhost:3000
```

---

## 🧪 Running Tests

```bash
mvn test
```

### Code Coverage

```bash
mvn verify
```

JaCoCo report:

```
target/site/jacoco/index.html
```

---

## 🔒 Security Highlights

* Password encryption using **BCrypt**
* JWT filter for request validation
* Stateless session management
* Role‑based endpoint access

---

## 🚀 Future Enhancements

* Refresh Token support
* Swagger / OpenAPI documentation
* Email notifications
* Admin dashboard (UI)
* Dockerization

---

## 👨‍💻 Author

**Ratan Kumar**
Java Full‑Stack Developer
(Spring Boot | React | Angular | PostgreSQL)

---

## 📜 License

This project is for learning and demonstration purposes.
