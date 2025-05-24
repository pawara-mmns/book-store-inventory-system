# Book Store Inventory System

A full-featured Spring Boot CRUD application for managing book inventory in a bookstore. The system supports two roles: **Admin** and **Employee**, and is built using layered architecture with MySQL, Hibernate, and Swagger for documentation.

---

## Tech Stack

- **Backend:** Spring Boot
- **Architecture:** Layered (Controller, Service, Repository, DTO, Entity)
- **Database:** MySQL
- **ORM:** Hibernate (JPA)
- **Build Tool:** Maven
- **Security:** Spring Security with JWT
- **API Documentation:** Swagger

---

## Features

### 1. Core CRUD System (Main Focus)
- Add, update, view, and delete books.
- Fully RESTful API with proper role access.
- Admin can perform full CRUD; Employee can only view and update books.

### 2. Role-Based Access Control
- **Admin**:
  - Full access to books.
  - Can manage employees.
  - Can view dashboard statistics.
- **Employee**:
  - Can only view and update books.

### 3. Book Categorization
- Categorize books by **author** and **category**.
- Search and filter capabilities by title, author, category, etc.

### 4. Stock Management
- Display stock availability.
- Restock books (admin-only).

### 5. Sales Module *(optional but recommended)*
- Record sales transactions.
- Track who sold which book and when.

### 6. Dashboard (Admin)
- Total books
- Total sales
- Low stock alerts
- Most sold books

### 7. Soft Delete
- Use `isDeleted` flag to logically remove books.

### 8. Swagger Integration
- Document all API endpoints.
- Group endpoints by module (Books, Users, Categories, Sales).

---

## Project Structure (Layered Architecture)

---

## Required Entities

### 1. **Book**
- `id`: Long
- `title`: String
- `author`: String
- `category`: String
- `isbn`: String
- `price`: Double
- `publishedYear`: Integer
- `stockQuantity`: Integer
- `description`: String
- `isDeleted`: Boolean

### 2. **User**
- `id`: Long
- `username`: String
- `password`: String
- `email`: String
- `role`: Enum (ADMIN, EMPLOYEE)

### 3. **Category** *(Optional, normalized design)*
- `id`: Long
- `name`: String

### 4. **Author** *(Optional)*
- `id`: Long
- `name`: String

### 5. **Sale** *(Optional)*
- `id`: Long
- `bookId`: Long
- `quantity`: Integer
- `soldBy`: String
- `soldDate`: Date
